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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class UploadStudyMaterialActivity extends AppCompatActivity {

    Toolbar toolbar;
    Intent intent;
    ImageView attachImageView;
    Button attachShareButton;
    TextView noteTextView;
    String fileType ="Image"; //default
    Drawable drawable;
    Uri uri;
    CustomLoadingDialog customLoadingDialog;
    String fileURL;
    EditText descriptionEditText, titleEditText;
    String groupName, getFileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_study_material);

        toolbar =  findViewById(R.id.attachToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

        attachImageView = (ImageView) findViewById(R.id.attachImageView);
        attachShareButton = (Button) findViewById(R.id.attachShareButton);
        noteTextView = (TextView) findViewById(R.id.noteTextView);
        drawable = getResources().getDrawable(R.drawable.pdf);
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        titleEditText = (EditText) findViewById(R.id.titleEditText);

        intent = getIntent();
        groupName = intent.getStringExtra("groupName");

        if(fileType.equals(intent.getStringExtra("fileType"))){
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

        attachShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(descriptionEditText.getText().toString().equals("") || titleEditText.getText().toString().equals("") || uri == null){
                    Toast.makeText(UploadStudyMaterialActivity.this, "All fields are necessary", Toast.LENGTH_SHORT).show();
                }else {
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
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("StudyMaterial").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            fileURL = String.valueOf(task.getResult());
                            uploadStudyMaterial();
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

    public void  uploadStudyMaterial(){
        String createdOn = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        long timeStamp = -1 * new Date().getTime();
        StudyMaterialData studyMaterialData = new StudyMaterialData(titleEditText.getText().toString(), descriptionEditText.getText().toString(), fileURL, createdOn, groupName, getFileType, String.valueOf(timeStamp));
        FirebaseDatabase.getInstance().getReference("StudyMaterial").child(String.valueOf(timeStamp)).setValue(studyMaterialData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(UploadStudyMaterialActivity.this, "Study material uploaded successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UploadStudyMaterialActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            uri = data.getData();
            if(fileType.equals(intent.getStringExtra("fileType"))){
                attachImageView.setImageURI(uri);
                getFileType = "Image";
            }else{
                noteTextView.setText("Filepath: "+ uri.toString());
                getFileType = "Pdf";
            }

        }else {
            Toast.makeText(UploadStudyMaterialActivity.this, "Please select " + fileType, Toast.LENGTH_SHORT).show();
        }
    }
}