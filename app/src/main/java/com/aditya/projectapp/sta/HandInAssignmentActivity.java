package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class HandInAssignmentActivity extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;
    Drawable drawable;
    Uri uri;
    CustomLoadingDialog customLoadingDialog;
    String fileURL = "";
    EditText descriptionEditText, titleEditText;
    TextView noteTextView;
    Button submitButton;
    ImageView attachImageView;
    String groupName, getFileType;
    String fileType ="Image"; //default


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hand_in_assignment);

        toolbar = findViewById(R.id.handInToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        titleEditText = (EditText) findViewById(R.id.titleEditText);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        noteTextView = (TextView) findViewById(R.id.noteTextView);
        submitButton = (Button) findViewById(R.id.submitButton);
        drawable = getResources().getDrawable(R.drawable.pdf);
        customLoadingDialog = new CustomLoadingDialog(this);
        attachImageView = (ImageView) findViewById(R.id.attachImageView);


        intent = getIntent();
        groupName = intent.getStringExtra("groupName");
      //  Toast.makeText(this, groupName, Toast.LENGTH_SHORT).show();


        if(String.valueOf(intent.getCharSequenceExtra("fileType")).equals("Image")){
            noteTextView.setText("Note: Please note that STA only supports images formats .jpg, .jpeg and .png");
            attachImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage();
                }
            });

        }else {
            noteTextView.setText("filepath");
            attachImageView.setImageDrawable(drawable);
            attachImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectPDF();
                }
            });
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(titleEditText.getText().toString().equals("") || uri == null || descriptionEditText.getText().toString().equals("")){
                    Toast.makeText(HandInAssignmentActivity.this, "All fields are necessary", Toast.LENGTH_SHORT).show();
                }else{
                    uploadFile();
                }
            }
        });

    }


    public void selectPDF(){
        Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
        intentPDF.setType("application/pdf");
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intentPDF, 1);
    }

    public void selectImage(){
        Intent selectPhoto = new Intent(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, 1);
    }


    public void uploadFile() {
        if (uri != null) {
            customLoadingDialog = new CustomLoadingDialog(this);
            customLoadingDialog.customLoadingDialogShow();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("StudentAssignmentFiles").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            fileURL = String.valueOf(task.getResult());
                            uploadAssignment();
                            customLoadingDialog.customLoaingDialogDismiss();
                        }
                    });
                }
            });
        }else {
            Toast.makeText(this, "Please select a file to continue", Toast.LENGTH_SHORT).show();
            customLoadingDialog.customLoaingDialogDismiss();
        }
    }


    public void  uploadAssignment(){
        String createdOn = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        long timeStamp = -1 * new Date().getTime();
        StudentAssignmentData studentAssignmentData = new StudentAssignmentData(titleEditText.getText().toString(), intent.getStringExtra("assignmentTitle"), descriptionEditText.getText().toString(), String.valueOf(intent.getCharSequenceExtra("fileType")), fileURL, createdOn, groupName, String.valueOf(timeStamp), "In assessment", FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseDatabase.getInstance().getReference("StudentAssignments").child(groupName).child(intent.getStringExtra("assignmentTitle")).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(studentAssignmentData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(HandInAssignmentActivity.this, "Assignment uploaded successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HandInAssignmentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            uri = data.getData();
            if(fileType.equals(String.valueOf(intent.getCharSequenceExtra("fileType")))){
                attachImageView.setImageURI(uri);
                getFileType = "Image";
            }else{
                noteTextView.setText("Filepath: "+ uri.toString());
                getFileType = "Pdf";
            }

        }else {
            Toast.makeText(HandInAssignmentActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
}