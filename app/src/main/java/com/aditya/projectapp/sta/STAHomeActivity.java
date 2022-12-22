 package com.aditya.projectapp.sta;

import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

 public class STAHomeActivity extends AppCompatActivity {

     ViewPager viewPager;
     TabLayout tabLayout;
     Toolbar toolbar;
     String userType;
    // Uri uri;

//    public void logOut(View view){
//
//        Intent intent = new Intent(this, LogInActivity.class);
//        startActivity(intent);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stahome);


        toolbar = (Toolbar) findViewById(R.id.STAHomeToolBar);
        viewPager = (ViewPager) findViewById(R.id.STAHomeViewPager);
        tabLayout = (TabLayout) findViewById(R.id.homeTabLayout);
        setSupportActionBar(toolbar);


        Intent intent = getIntent();
        userType =  intent.getStringExtra("userType");

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
//
        tabLayout.getTabAt(0).setIcon(R.drawable.home);

        if(userType.equals("Student")){
            tabLayout.getTabAt(1).setIcon(R.drawable.student);
        }else {
            tabLayout.getTabAt(1).setIcon(R.drawable.teacher);
        }
        tabLayout.getTabAt(2).setIcon(R.drawable.text);

        viewPager.setCurrentItem(1);

    }

     public void setupViewPager(ViewPager viewPager){
         PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
         pagerAdapter.addFragment(new HomeFragment(), "Home");
         if(userType.equals("Student")) {
             pagerAdapter.addFragment(new StudentFragment(), "Student");
         }else {
             pagerAdapter.addFragment(new TeacherFragment(), "Teacher");
         }
         pagerAdapter.addFragment(new ChatFragment(), "Chats");
         viewPager.setAdapter(pagerAdapter);
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {

         MenuInflater menuInflater = getMenuInflater();
         menuInflater.inflate(R.menu.menu, menu);

         return super.onCreateOptionsMenu(menu);
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.userProfile){
            Intent intent = new Intent(STAHomeActivity.this, UserProfileActivity.class);
            startActivity(intent);
            return true;
        }

        if(item.getItemId() == R.id.about){
            new AlertDialog.Builder(STAHomeActivity.this)
                    .setTitle("About")
                    .setMessage("STA (Early Access) is an application which let's you do some stuff made by aditya")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("Give Feedback", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("*/*");
                                intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "adityabalsane10@gmail.com" });
                                intent.putExtra(Intent.EXTRA_SUBJECT, "STA feedback");
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    Toast.makeText(STAHomeActivity.this, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }
                            } catch(Exception e) {
                                Toast.makeText(STAHomeActivity.this, "Sorry! STA couldn't find an mail app on your device :(", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    })
                    .show();
                return true;
        }

        if(item.getItemId() == R.id.setting){
            new AlertDialog.Builder(STAHomeActivity.this)
                    .setMessage("You need to login as root user")
                    .setCancelable(false)
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();

            return true;
        }

         if(item.getItemId() == R.id.logout) {
             FirebaseAuth.getInstance().signOut();
             Intent intent = new Intent(STAHomeActivity.this, LogInActivity.class);
             startActivity(intent);
             return true;
         }
         return super.onOptionsItemSelected(item);
     }


     @Override
     public void onBackPressed() {
         super.onBackPressed();
     }


 }
