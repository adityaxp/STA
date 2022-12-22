package com.aditya.projectapp.sta;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class InstituteDescriptionActivity extends AppCompatActivity {

    ImageView instituteDescriptionImageView;
    TextView instituteDescription, link, instituteDescriptionTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institute_description);

        instituteDescription = (TextView) findViewById(R.id.instituteDescription);
        instituteDescriptionImageView = (ImageView) findViewById(R.id.descriptionImage);
        instituteDescriptionTitle = (TextView) findViewById(R.id.instituteDescriptionTitle);
        link = (TextView) findViewById(R.id.link);


        Bundle bundle = getIntent().getExtras();

        if(bundle!= null){
             //   instituteDescriptionImageView.setImageResource(bundle.getInt("Image"));
                instituteDescription.setText(bundle.getString("Description"));
                Glide.with(this).load(bundle.getString("Image")).into(instituteDescriptionImageView);
                instituteDescriptionTitle.setText(bundle.getString("Title"));
                link.setText(bundle.getString("URL"));
                link.setLinkTextColor(Color.BLUE);
        }else {
            Toast.makeText(InstituteDescriptionActivity.this, "Could not retrieve the data", Toast.LENGTH_SHORT).show();
        }

    }
}
