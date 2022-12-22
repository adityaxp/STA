package com.aditya.projectapp.sta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;


import android.view.View;
import android.widget.Toast;


import com.scanlibrary.ScanActivity;
import com.scanlibrary.ScanConstants;

import java.io.IOException;

public class STAQuickScanActivity extends AppCompatActivity {


    //Button gallery, camera;
    private int STORAGE_PERMISSION_CODE = 1;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_t_a_quick_scan);

        toolbar = findViewById(R.id.quickScanToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });



        if(ContextCompat.checkSelfPermission(STAQuickScanActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
        }else {
            new AlertDialog.Builder(STAQuickScanActivity.this)
                    .setTitle("Please grant storage permission")
                    .setMessage("STA needs storage permission to fetch various data attributes in order to function properly.")
                    .setCancelable(false)
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(STAQuickScanActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestStoragePermission();
                        }
                    }).show();

        }


        final CharSequence[] items = {"Open camera", "Pick an image from gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How would you like to scan your document?");
        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                Toast.makeText(STAQuickScanActivity.this, items[item], Toast.LENGTH_SHORT).show();
                if(items[item] == "Open camera"){
                    int REQUEST_CODE = 99;
                    int preference = ScanConstants.OPEN_CAMERA;
                    Intent intent = new Intent(STAQuickScanActivity.this, ScanActivity.class);
                    intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent, REQUEST_CODE);
                }else{
                    int REQUEST_CODE = 99;
                    int preference = ScanConstants.OPEN_MEDIA;
                    Intent intent = new Intent(STAQuickScanActivity.this, ScanActivity.class);
                    intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                    startActivityForResult(intent, REQUEST_CODE);
                }

            }
        });
        AlertDialog alert = builder.create();
        alert.show();



    /*    gallery = (Button) findViewById(R.id.gallery);
        camera = (Button) findViewById(R.id.camera);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 99;
                int preference = ScanConstants.OPEN_MEDIA;
                Intent intent = new Intent(STAQuickScanActivity.this, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int REQUEST_CODE = 99;
                int preference = ScanConstants.OPEN_CAMERA;
                Intent intent = new Intent(STAQuickScanActivity.this, ScanActivity.class);
                intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, preference);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


     */

    }

    private void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            // permission check don't touch this
        }else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Storage permission granted by user", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(STAQuickScanActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getExtras().getParcelable(ScanConstants.SCANNED_RESULT);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
               // getContentResolver().delete(uri, null, null);
             //   scannedImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}