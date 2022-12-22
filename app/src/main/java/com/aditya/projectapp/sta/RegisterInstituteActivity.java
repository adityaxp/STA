package com.aditya.projectapp.sta;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;



public class RegisterInstituteActivity extends AppCompatActivity {

    ImageView instituteImageRound;
   // Button selectButton, uploadButton, registerButton;
    EditText descriptionRegisterEditText,domainRegisterEditText, registerNameEditText;
    Uri uri;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_institute);

        instituteImageRound = (ImageView) findViewById(R.id.instituteImageRound);
        descriptionRegisterEditText = (EditText) findViewById(R.id.descriptionRegisterEditText);
        domainRegisterEditText = (EditText) findViewById(R.id.domainRegisterEditText);
        registerNameEditText = (EditText) findViewById(R.id.registerNameEditText);


    }

    public void selectImage(View view){
        Intent selectPhoto = new Intent(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, 1);
    }

    public void uploadImage() {
        if (uri != null) {
            final CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(this);
            customLoadingDialog.customLoadingDialogShow();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("InstituteImage").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    uploadInstituteInfo();
                    
                    customLoadingDialog.customLoaingDialogDismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RegisterInstituteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    customLoadingDialog.customLoaingDialogDismiss();
                }
            });
        }else {
            Toast.makeText(this, "Please select an image to continue", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            uri = data.getData();
            instituteImageRound.setImageURI(uri);
        }else {
            Toast.makeText(RegisterInstituteActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
    public void registerInstitute(View view){

        if(registerNameEditText.getText().toString().equals("") || descriptionRegisterEditText.getText().toString().equals("") || domainRegisterEditText.getText().toString().equals("")){
            Toast.makeText(RegisterInstituteActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }
    public void uploadInstituteInfo(){

      InstituteData instituteData = new InstituteData(registerNameEditText.getText().toString(), descriptionRegisterEditText.getText().toString(),imageUrl ,domainRegisterEditText.getText().toString());

//        you have to fix up this mess quick - priority level - 3 working (not necessary)
//        ParseObject parseObject =  new ParseObject("Institute");
//        parseObject.put("instituteName", registerNameEditText.getText().toString());
//        parseObject.put("instituteDescription", descriptionRegisterEditText.getText().toString());
//        parseObject.put("instituteDomain", domainRegisterEditText.getText().toString());
//        parseObject.put("instituteImage", imageUrl);


      FirebaseDatabase.getInstance().getReference("Institute").child(registerNameEditText.getText().toString()).setValue(instituteData).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterInstituteActivity.this, "Registered!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
       }).addOnFailureListener(new OnFailureListener() {
          @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RegisterInstituteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
           }
        });
    }
}
