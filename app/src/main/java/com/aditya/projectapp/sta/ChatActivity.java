package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView userNameTextView;
    ImageView userChatImage;
    RecyclerView userMessageListRecylerView;
    EditText userMessageEditText;
    ImageButton userSendMessageButton;
    String senderName, chatToken;
    ScrollView chatScrollView;
    ChatAdapter chatAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ChatData> messageList = new ArrayList<>();
    DatabaseReference databaseReference, databaseReference1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar = (Toolbar) findViewById(R.id.chatActionBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userNameTextView = (TextView) findViewById(R.id.userNameTextView);
        userChatImage = (ImageView) findViewById(R.id.userChatImage);
        userMessageListRecylerView = (RecyclerView) findViewById(R.id.userMessageListRecylerView);
        userMessageEditText = (EditText) findViewById(R.id.userMessageEditText);
        userSendMessageButton = (ImageButton) findViewById(R.id.userSendMessageButton);
        chatScrollView = (ScrollView) findViewById(R.id.ChatScrollView);
        layoutManager = new LinearLayoutManager(this);


        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            Glide.with(this).load(bundle.getString("userImage")).into(userChatImage);
            userNameTextView.setText(bundle.getString("userName"));
          }else {
            Toast.makeText(ChatActivity.this, "Could not retrieve the data", Toast.LENGTH_SHORT).show();
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 senderName = snapshot.getValue().toString();
                 chatAdapter = new ChatAdapter(ChatActivity.this, messageList, senderName);
                 userMessageListRecylerView.setLayoutManager(layoutManager);
                 userMessageListRecylerView.setAdapter(chatAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        userMessageEditText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(userMessageEditText.getCompoundDrawables()[2]!=null){
                        if(event.getX() >= (userMessageEditText.getRight()- userMessageEditText.getLeft() - userMessageEditText.getCompoundDrawables()[2].getBounds().width())) {
                            new AlertDialog.Builder(ChatActivity.this)
                                    .setMessage("It's a bit buggy for now and It may not even work but I'm working on it to make it more efficient")
                                    .setCancelable(false)
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(ChatActivity.this, "Internal error: An unexpected exception occurred.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).show();
                        }
                    }
                }
                return false;
            }
        });



        userSendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMessageEditText.getText().toString().equals("")){
                    Toast.makeText(ChatActivity.this, "Message can't be empty", Toast.LENGTH_SHORT).show();
                }else{
                    String sortByTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    long timeStamp = -1 * new Date().getTime();
                    ChatData chatData = new ChatData(senderName+"-"+userNameTextView.getText().toString(), senderName, userMessageEditText.getText().toString(), sortByTime, senderName,  userNameTextView.getText().toString());
                    FirebaseDatabase.getInstance().getReference("UserChats").child(String.valueOf(timeStamp)).setValue(chatData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(ChatActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                            userMessageEditText.setText("");
                            hideKeyboard();
                            chatScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });
        

        databaseReference1 = FirebaseDatabase.getInstance().getReference("UserChats");
        databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                    for (DataSnapshot itemSnapshot: snapshot.getChildren()) {
                        if (itemSnapshot.child("senderId").getValue().toString().equals(senderName) || itemSnapshot.child("receiverId").getValue().toString().equals(senderName) && itemSnapshot.child("senderId").getValue().toString().equals(userNameTextView.getText().toString()) || itemSnapshot.child("receiverId").getValue().toString().equals(userNameTextView.getText().toString())) {
                            String token = itemSnapshot.child("chatToken").getValue().toString();
                            String[] tokenName = token.split("-");
                            String userID = tokenName[0];
                            String user = tokenName[1];

                            if(user.equals(userNameTextView.getText().toString()) || userID.equals(userNameTextView.getText().toString())){
                                ChatData chatData = itemSnapshot.getValue(ChatData.class);
                                messageList.add(chatData);
                            }
                        }
                    }
                chatAdapter.notifyDataSetChanged();
                chatScrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_chat_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.chat_video_item){
            SelectVideoApp selectVideoApp = new SelectVideoApp(ChatActivity.this);
            selectVideoApp.chooseApp();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}