package com.aditya.projectapp.sta;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    Intent intent;
    String mainUserType;
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView logoImageView = (ImageView) findViewById(R.id.logoImageView);
        logoImageView.animate().scaleX(1.5f).scaleY(1.5f).setDuration(2000);


        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
          splashScreen();
          // jump();
        }else {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Please grant storage permission")
                    .setMessage("STA needs storage permission to fetch various data attributes in order to function properly.")
                    .setCancelable(false)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            splashScreen();
                            finish();
                        }
                    })
                    .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestStoragePermission();
                        }
                    }).show();

        }





    }
//
    public void  jump(){
        intent = new Intent(MainActivity.this , STAQuickScanActivity.class);
        startActivity(intent);
    }

    private void requestStoragePermission(){
       if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)  && ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
       }else {
           ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
       }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Storage permission granted by user", Toast.LENGTH_SHORT).show();
                splashScreen();
            }else {
                Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void splashScreen(){
        //  Valid institute check
        //  (for future Aditya) Do not touch this else app will literally break :)
        new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }
            @Override
            public void onFinish() {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userInstitute");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String selectedInstituteUser = dataSnapshot.getValue().toString();
                            if(selectedInstituteUser.equals("")){
                                intent = new Intent(MainActivity.this,  SelectInstituteActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                intent = new Intent(MainActivity.this,  STAHomeActivity.class);
                                DatabaseReference userType = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userType");
                                userType.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        mainUserType = snapshot.getValue().toString();
                                        intent.putExtra("userType",mainUserType);
                                        startActivity(intent);
                                        finish();
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, error.toString() + "User signed out", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                            intent = new Intent(MainActivity.this,  LogInActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    intent = new Intent(MainActivity.this,  LogInActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();


    }


}
