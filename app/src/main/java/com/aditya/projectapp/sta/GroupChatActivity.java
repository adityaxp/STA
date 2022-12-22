package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class GroupChatActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText messageEditText;
    ImageButton sendMessageButton;
    ImageView groupChatImage;
    TextView groupNameTextView;
    String groupKey, imageURL, senderName, groupChatImageURL; //, chatToken;
    RecyclerView.LayoutManager layoutManager;
    ScrollView groupChatScrollView;
    GroupChatAdapter groupChatAdapter;
    DatabaseReference databaseReference, databaseReference1, databaseReference2;
    RecyclerView messageListRecylerView;
    Uri uri;
    Boolean picture = false;
    ArrayList<GroupChatData> messageList = new ArrayList<>();

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        toolbar = (Toolbar) findViewById(R.id.groupActionBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        messageListRecylerView = (RecyclerView) findViewById(R.id.messageListRecylerView);
        groupChatImage = (ImageView) findViewById(R.id.groupChatImage);
        groupNameTextView = (TextView) findViewById(R.id.groupNameTextView);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        groupChatScrollView = (ScrollView) findViewById(R.id.groupChatScrollView);
        sendMessageButton = (ImageButton) findViewById(R.id.sendMessageButton);
        layoutManager = new LinearLayoutManager(this);
        messageEditText.setHorizontalScrollBarEnabled(true);



        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
            Glide.with(this).load(bundle.getString("groupImage")).into(groupChatImage);
            groupNameTextView.setText(bundle.getString("groupName"));
            groupKey = bundle.getString("groupId");

        }else {
            Toast.makeText(GroupChatActivity.this, "Could not retrieve the data", Toast.LENGTH_SHORT).show();
        }



        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userImage");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageURL = dataSnapshot.getValue().toString();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupChatActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });




        databaseReference1 = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
        databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                senderName = dataSnapshot.getValue().toString();
                databaseReference2 = FirebaseDatabase.getInstance().getReference().child("GroupChats").child(groupNameTextView.getText().toString());
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();
                        for(DataSnapshot itemSnapshot: snapshot.getChildren()){
                            if(itemSnapshot.child("messageType").getValue().toString().equals("Text")) {
                                picture = false;
                                GroupChatData groupChatData = itemSnapshot.getValue(GroupChatData.class);
                                messageList.add(groupChatData);
                            }else if(itemSnapshot.child("messageType").getValue().toString().equals("image")){
                                picture = true;
                                GroupChatData groupChatData = itemSnapshot.getValue(GroupChatData.class);
                                messageList.add(groupChatData);
                            }
                            if(picture){
                                groupChatAdapter = new GroupChatAdapter(GroupChatActivity.this, messageList, senderName, true);
                                messageListRecylerView.setLayoutManager(layoutManager);
                                messageListRecylerView.setAdapter(groupChatAdapter);
                            }else{
                                groupChatAdapter = new GroupChatAdapter(GroupChatActivity.this, messageList, senderName, false);
                                messageListRecylerView.setLayoutManager(layoutManager);
                                messageListRecylerView.setAdapter(groupChatAdapter);
                            }
                            groupChatAdapter.notifyDataSetChanged();

                        }
                        groupChatScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(GroupChatActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        messageEditText.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(messageEditText.getCompoundDrawables()[2]!=null){
                        if(event.getX() >= (messageEditText.getRight()- messageEditText.getLeft() - messageEditText.getCompoundDrawables()[2].getBounds().width())) {
                            Intent selectPhoto = new Intent(Intent.ACTION_PICK);
                            selectPhoto.setType("image/*");
                            startActivityForResult(selectPhoto, 1);
                            }
                        }
                    }
                return false;
            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(messageEditText.getText().toString().equals("")){
                    Toast.makeText(GroupChatActivity.this, "Message can't be empty", Toast.LENGTH_SHORT).show();
                }else {
                    String sortByTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    long timeStamp = -1 * new Date().getTime();
                    GroupChatData groupChatData = new GroupChatData(groupNameTextView.getText().toString(), senderName, messageEditText.getText().toString().trim(), imageURL, sortByTime, "Text");
                    FirebaseDatabase.getInstance().getReference("GroupChats").child(groupNameTextView.getText().toString()).child(String.valueOf(timeStamp)).setValue(groupChatData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference("Groups").child(groupKey).child("recentMessage").setValue(senderName +": "+ messageEditText.getText().toString().trim());
                            Toast.makeText(GroupChatActivity.this, "Message sent!", Toast.LENGTH_SHORT).show();
                            messageEditText.setText("");
                            hideKeyboard();
                            groupChatScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GroupChatActivity.this, "Failed! please try again...", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
//    void randomString(){
//        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
//        StringBuilder randomString = new StringBuilder();
//        Random rnd = new Random();
//        while (randomString.length() < 10) { // length of the random string.
//            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
//            randomString.append(SALTCHARS.charAt(index));
//        }
//        chatToken = randomString.toString();
//    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.group_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.video_item){
            SelectVideoApp selectVideoApp = new SelectVideoApp(GroupChatActivity.this);
            selectVideoApp.chooseApp();

        }


        if(item.getItemId() == R.id.group_info){
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(GroupChatActivity.this);
            final android.app.AlertDialog alertDialog;

            View view = getLayoutInflater().inflate(R.layout.custom_group_info_alert_dialog_box, null);
            Button closeButton = (Button) view.findViewById(R.id.closeButton);
            Button copyButton = (Button) view.findViewById(R.id.copyButton);
            TextView groupNameTextView1 = (TextView) view.findViewById(R.id.groupNameTextView);
            final TextView groupAdminTextView = (TextView) view.findViewById(R.id.groupAdminTextView);
            TextView groupKeyTextView = (TextView) view.findViewById(R.id.groupKeyTextView);


            builder.setView(view);
            builder.setCancelable(false);

            alertDialog = builder.create();
            alertDialog.show();

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });

            groupNameTextView1.setText(groupNameTextView.getText().toString());
            databaseReference = FirebaseDatabase.getInstance().getReference("Groups").child(groupKey).child("createdBy");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    groupAdminTextView.setText(snapshot.getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(GroupChatActivity.this, "Failed to retrieve group info", Toast.LENGTH_SHORT).show();
                }
            });
            groupKeyTextView.setText(groupKey);


            copyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("groupkey", groupKey);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(GroupChatActivity.this, "Group key copied!", Toast.LENGTH_SHORT).show();
                }
            });


            return true;
        }

        if(item.getItemId() == R.id.leave_group){
            new AlertDialog.Builder(GroupChatActivity.this)
                    .setTitle("Do you really want to leave this group?")
                    .setCancelable(false)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference("Groups").child(groupKey).child("groupMembers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(GroupChatActivity.this, "Group left", Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(GroupChatActivity.this, "Something went wrong please try again!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            uri = data.getData();
            uploadImage();
        }else {
            Toast.makeText(GroupChatActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }


    public void uploadImage() {
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("GroupChatImages").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            groupChatImageURL = task.getResult().toString();
                                    final CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(GroupChatActivity.this);
                                    customLoadingDialog.customLoadingDialogShow();
                                    String sortByTime = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                                    long timeStamp = -1 * new Date().getTime();
                                    GroupChatData groupChatData = new GroupChatData(groupNameTextView.getText().toString(), senderName, senderName + ": sent an image", imageURL, sortByTime, groupChatImageURL, "image");
                                    FirebaseDatabase.getInstance().getReference("GroupChats").child(groupNameTextView.getText().toString()).child(String.valueOf(timeStamp)).setValue(groupChatData).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            customLoadingDialog.customLoaingDialogDismiss();
                                            Toast.makeText(GroupChatActivity.this, "Failed please try again", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            customLoadingDialog.customLoaingDialogDismiss();
                                            Toast.makeText(GroupChatActivity.this, "Image sent!", Toast.LENGTH_SHORT).show();
                                            hideKeyboard();
                                        }
                                    });
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this, "Please select an image to continue", Toast.LENGTH_SHORT).show();
        }
    }

    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}