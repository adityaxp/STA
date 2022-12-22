package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class CreateGroupActivity extends AppCompatActivity {

    EditText groupNameEditText, groupKeyEditText;
    Button  generateRandomKeyButton, createGroupButton;
    ImageView groupImageRound;
    Uri uri;
    ImageButton selectImageGroupButton;
    String randomStringText;
    String groupImageURL;
    CustomLoadingDialog customLoadingDialog;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        groupNameEditText = (EditText) findViewById(R.id.groupNameEditText);
        groupKeyEditText = (EditText) findViewById(R.id.groupKeyEditText);
        selectImageGroupButton = (ImageButton)findViewById(R.id.selectImageGroupButton);
        generateRandomKeyButton = (Button) findViewById(R.id.genrateRandomKeyButton);
        createGroupButton = (Button) findViewById(R.id.createGroupButton);
        groupImageRound = (ImageView) findViewById(R.id.groupImageRound);
        toolbar = (Toolbar) findViewById(R.id.createGroupToolBar);


        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        selectImageGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectPhoto = new Intent(Intent.ACTION_PICK);
                selectPhoto.setType("image/*");
                startActivityForResult(selectPhoto, 1);
            }
        });

        generateRandomKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                randomString();
                groupKeyEditText.setText(randomStringText);
            }
        });


        createGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupKeyEditText.getText().toString().equals("") || groupNameEditText.getText().toString().equals("")){
                    Toast.makeText(CreateGroupActivity.this, "Field's cant be empty", Toast.LENGTH_SHORT).show();
                }else{
                    customLoadingDialog = new CustomLoadingDialog(CreateGroupActivity.this);
                    customLoadingDialog.customLoadingDialogShow();
                    uploadImage();
                    // I can't still understand why this works :(
                    new CountDownTimer(2000, 100){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }
                        @Override
                        public void onFinish() {
                            Intent intent = getIntent();
                            GroupData groupData = new GroupData(groupNameEditText.getText().toString(), groupKeyEditText.getText().toString(), " ", groupImageURL, intent.getStringExtra("userName"), FirebaseAuth.getInstance().getCurrentUser().getEmail());
                            FirebaseDatabase.getInstance().getReference("Groups").child(groupKeyEditText.getText().toString()).setValue(groupData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    customLoadingDialog.customLoaingDialogDismiss();
                                    share();
                                    Toast.makeText(CreateGroupActivity.this, "Group created", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customLoadingDialog.customLoaingDialogDismiss();
                                    Toast.makeText(CreateGroupActivity.this, "Failed! Please try again", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }.start();
                }
            }
        });


    }

    void randomString(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder randomString = new StringBuilder();
        Random rnd = new Random();
        while (randomString.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            randomString.append(SALTCHARS.charAt(index));
        }
        randomStringText = randomString.toString();
    }

    void share(){
        new AlertDialog.Builder(CreateGroupActivity.this)
                .setTitle("Do you want to share group details?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, "STA - Group Details\n" + "\n" + "Group Name: " + groupNameEditText.getText().toString().trim() + "\n" + "Group Key: " + groupKeyEditText.getText().toString());
                            intent.setType("text/plain");
                            Intent shareIntent = Intent.createChooser(intent, null);
                            startActivity(shareIntent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CreateGroupActivity.this, STAHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .show();
    }

    public void uploadImage() {
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("GroupImages").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            groupImageURL = task.getResult().toString();
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this, "Please select an image to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            uri = data.getData();
            groupImageRound.setImageURI(uri);
        }else {
            Toast.makeText(CreateGroupActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

//    public void getImageUrl(String url){
//        String imageUrl = url;
//        groupImageURL = imageUrl;
//    }

}