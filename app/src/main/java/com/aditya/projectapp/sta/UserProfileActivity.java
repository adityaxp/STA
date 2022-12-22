package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView userImageRound;
    ImageButton updateImageButton, editProfileNameButton, editEmailButton, editInstituteButton;
    EditText userNameEditText, emailEditText, instituteEditText, accountTypeEditText;
    Button saveProfileButton;
    String userImageURL;
    CustomLoadingDialog customLoadingDialog;
    Boolean editUsername = false;
    Boolean editEmail = false;
    Boolean editProfileImage = false;
    Uri uri;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        toolbar = (Toolbar) findViewById(R.id.userProfileActionBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        userImageRound = (ImageView) findViewById(R.id.userImageRound);
        updateImageButton = (ImageButton) findViewById(R.id.updateImageButton);
        editEmailButton = (ImageButton) findViewById(R.id.editEmailButton);
        editProfileNameButton = (ImageButton) findViewById(R.id.editProfileNameButton);
        editInstituteButton = (ImageButton) findViewById(R.id.editInstituteButton);
        saveProfileButton = (Button) findViewById(R.id.saveProfileButton);
        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        instituteEditText = (EditText) findViewById(R.id.instituteEditText);
        accountTypeEditText = (EditText) findViewById(R.id.accountTypeEditText);
        customLoadingDialog = new CustomLoadingDialog(this);


        customLoadingDialog.customLoadingDialogShow();
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Glide.with(UserProfileActivity.this).load(snapshot.child("userImage").getValue().toString()).into(userImageRound);
                userNameEditText.setText(snapshot.child("userName").getValue().toString());
                emailEditText.setText( snapshot.child("userEmail").getValue().toString());
                accountTypeEditText.setText(snapshot.child("userType").getValue().toString());
                instituteEditText.setText(snapshot.child("userInstitute").getValue().toString());
                customLoadingDialog.customLoaingDialogDismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileImage  = true;
                selectImage(v);
            }
        });


        editProfileNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUsername = true;
                userNameEditText.setEnabled(true);
            }
        });

        editEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editEmail = true;
                new AlertDialog.Builder(UserProfileActivity.this)
                        .setTitle("Authentication failed.")
                        .setCancelable(false)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        editInstituteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserProfileActivity.this)
                        .setTitle("Do you really want to change your institute?")
                        .setMessage("You will have to login again after changing your institute")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(UserProfileActivity.this, SelectInstituteActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editProfileImage){
                    uploadImage();
                    new CountDownTimer(2000, 100) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }
                        @Override
                        public void onFinish() {
                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userImage").setValue(userImageURL);
                            Toast.makeText(UserProfileActivity.this, "Profile saved...", Toast.LENGTH_SHORT).show();
                            editProfileImage = false;
                        }
                    }.start();
                }else if(editUsername){
                    FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName").setValue(userNameEditText.getText().toString().trim());
                    Toast.makeText(UserProfileActivity.this, "Profile saved...", Toast.LENGTH_SHORT).show();
                    editUsername = false;
                    userNameEditText.setEnabled(false);
                }else if(editEmail){

               /*
                 Code is a bit buggy and inconsistent does'nt work all the time will work on it in final phase.
                 FirebaseAuth.getInstance().getCurrentUser().updateEmail(emailEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userEmail").setValue(emailEditText.getText().toString().trim());
                            Toast.makeText(UserProfileActivity.this, "Profile saved...", Toast.LENGTH_SHORT).show();
                            editEmail = false;
                            emailEditText.setEnabled(false);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                */

                }else {
                    Toast.makeText(UserProfileActivity.this, "Nothing to change?", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void selectImage(View view){
        Intent selectPhoto = new Intent(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, 1);
    }

    public void uploadImage() {
        if (uri != null) {
            customLoadingDialog = new CustomLoadingDialog(this);
            customLoadingDialog.customLoadingDialogShow();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("UserImage").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            userImageURL = String.valueOf(task.getResult());
                            customLoadingDialog.customLoaingDialogDismiss();
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this, "Please select an image to continue", Toast.LENGTH_SHORT).show();
            customLoadingDialog.customLoaingDialogDismiss();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            uri = data.getData();
            userImageRound.setImageURI(uri);
        }else {
            Toast.makeText(UserProfileActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
}