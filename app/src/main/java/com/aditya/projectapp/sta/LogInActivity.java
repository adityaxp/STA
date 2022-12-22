package com.aditya.projectapp.sta;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;



public class LogInActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
//    Toolbar toolbar;
//    TextView tootbarTitle;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
      /*   View mView = getLayoutInflater().inflate(R.layout.custom_action_bar,null);
            tootbarTitle = (TextView) mView.findViewById(R.id.toolbar_title);
            tootbarTitle.setText("Welcome to STA!");
            toolbar = (Toolbar) findViewById(R.id.customActionBar);
            setSupportActionBar(toolbar);

      /*
        getSupportActionBar().hide();
        Spannable text = new SpannableString("STA Login");
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        getSupportActionBar().setTitle(text);
        getSupportActionBar().setIcon(R.drawable.sta);
      */

             viewPager = (ViewPager) findViewById(R.id.viewPager);
             tabLayout = (TabLayout) findViewById(R.id.tab);

             setupViewPager(viewPager);

             tabLayout.setupWithViewPager(viewPager);


            tabLayout.getTabAt(0).setIcon(R.drawable.student);
            tabLayout.getTabAt(1).setIcon(R.drawable.teacher);

    }
    public void setupViewPager(ViewPager viewPager){
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new StudentLoginFragment(), "Student Login");
        pagerAdapter.addFragment(new TeacherLoginFragment(), "Teacher Login");
        viewPager.setAdapter(pagerAdapter);

    }


}
