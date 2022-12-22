package com.aditya.projectapp.sta;


import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class SignUpActivity extends AppCompatActivity {

    ImageView userImageView;
    AutoCompleteTextView accountTypeTextView;
    TextInputLayout enterPassword;
    EditText enterNameEditText;
    EditText enterEmailEditText;
    Uri uri;
    Button signUpButton;
    UploadTask uploadTask;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String userImage = "";
    String selectedInstitute = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

       enterNameEditText = (EditText) findViewById(R.id.enterNameEditText);
       enterEmailEditText = (EditText) findViewById(R.id.enterEmailEditText);
       enterPassword = (TextInputLayout) findViewById(R.id.enterPassword);
       accountTypeTextView = (AutoCompleteTextView) findViewById(R.id.accountTypeTextView);
       signUpButton = (Button) findViewById(R.id.signUpButton);
       TextView logInTextView = (TextView) findViewById(R.id.logInTextView);
       userImageView = (ImageView) findViewById(R.id.userImageRound);
       final CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(SignUpActivity.this);


       final String[] userType = {"Teacher", "Student"};
       final ArrayAdapter<String> arrayAdapter =  new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, userType);

       accountTypeTextView.setAdapter(arrayAdapter);
       accountTypeTextView.setThreshold(1);
       accountTypeTextView.setOnKeyListener(new View.OnKeyListener() {
          @Override
           public boolean onKey(View v, int keyCode, KeyEvent event) {
              if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                       (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    return true;
               }
                return false;
           }
       });


       firebaseAuth = FirebaseAuth.getInstance();
       databaseReference = FirebaseDatabase.getInstance().getReference("User");


        signUpButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
            if(enterNameEditText.getText().toString().equals("") || enterEmailEditText.getText().toString().equals("") || enterPassword.getEditText().getText().toString().equals("") || accountTypeTextView.getText().toString().equals("")){
                Toast.makeText(SignUpActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
            }else if( uri == null){
                Toast.makeText(SignUpActivity.this, "Please select an image to continue", Toast.LENGTH_SHORT).show();
            } else {
                if(accountTypeTextView.getText().toString().equals("Student") || accountTypeTextView.getText().toString().equals("Teacher")){
                    customLoadingDialog.customLoadingDialogShow();
                    firebaseAuth.createUserWithEmailAndPassword(enterEmailEditText.getText().toString(), enterPassword.getEditText().getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        uploadImage();

                                        // Well I thought this was the stupidest shit ever
                                        // I thought computers were fast enough for this shit and wasted my entire day on it .
                                        // I don't know why I thought this would work out anyways saved me from stackoverflow toxicity
                                        new CountDownTimer(2000, 100){
                                            @Override
                                            public void onTick(long millisUntilFinished) {
                                                // Why this even works seriously I don't have any idea :(
                                            }
                                            @Override
                                            public void onFinish() {
                                                SignUpClass signUpClass = new SignUpClass(enterNameEditText.getText().toString(), enterEmailEditText.getText().toString(), accountTypeTextView.getText().toString(), userImage, selectedInstitute);
                                                FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(signUpClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(SignUpActivity.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(getApplicationContext(), SelectInstituteActivity.class));
                                                    }
                                                });

                                            }
                                        }.start();



                                    } else {
                                        Toast.makeText(SignUpActivity.this, "Sign Up failed please try again" , Toast.LENGTH_SHORT).show();
                                        customLoadingDialog.customLoaingDialogDismiss();
                                    }

                                    // ...
                                }
                            });

                }else {
                    Toast.makeText(SignUpActivity.this, "Account type can either be student or teacher!", Toast.LENGTH_SHORT).show();
                }
            }
           }
        });

        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
           public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }
        });

    }
    public void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void uploadImage() {
        if (uri != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("UserImage").child(uri.getLastPathSegment());
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            getImageUrl(String.valueOf(task.getResult()));
                            //  Log.i("some", String.valueOf(task.getResult()));//Null check false then why it does'nt fckin work
                        }
                    });
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
            userImageView.setImageURI(uri);
        }else {
            Toast.makeText(SignUpActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }

    public void selectUserImage(View view) {
        Intent selectPhoto = new Intent(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, 1);
    }

    public void getImageUrl(String url){
        String imageUrl = url;
        userImage = imageUrl;
    }

}
