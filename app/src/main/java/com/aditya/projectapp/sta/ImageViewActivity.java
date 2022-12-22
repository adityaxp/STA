package com.aditya.projectapp.sta;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewActivity extends AppCompatActivity {

    Toolbar toolbar;
    PhotoView targetImage;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        toolbar =  findViewById(R.id.imageViewToolBar);
        setSupportActionBar(toolbar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        targetImage = (PhotoView) findViewById(R.id.targetImage);

       // Toast.makeText(this, intent.getStringExtra("assessmentMode"), Toast.LENGTH_SHORT).show();


        intent = getIntent();

        Glide.with(this).load(intent.getStringExtra("Image")).into(targetImage);

    }



}