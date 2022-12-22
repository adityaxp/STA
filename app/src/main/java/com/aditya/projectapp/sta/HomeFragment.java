package com.aditya.projectapp.sta;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends androidx.fragment.app.Fragment {

    DatabaseReference databaseReference, databaseReference1;
    String userInstitute;
    String instituteURL;
    WebView instituteWebView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        instituteWebView = (WebView) view.findViewById(R.id.instituteWebView);
        CardView instituteCardView = (CardView) view.findViewById(R.id.instituteCardView);
        CardView whatsNewCardView = (CardView) view.findViewById(R.id.whatsNewCardView);
        CardView reportBugCardView = (CardView) view.findViewById(R.id.reportBugCardView);

        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userInstitute = snapshot.child("userInstitute").getValue().toString();
                databaseReference1 = FirebaseDatabase.getInstance().getReference("Institute").child(userInstitute);
                databaseReference1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        instituteURL = snapshot.child("itemDomain").getValue().toString();
                        if(!instituteURL.equals("")){
                            instituteWebView.setWebViewClient(new WebViewClient());
                            WebSettings webSettings = instituteWebView.getSettings();
                            instituteWebView.setInitialScale(50);
                            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
                            webSettings.setBuiltInZoomControls(true);
                            webSettings.setDisplayZoomControls(false);
                            webSettings.getDisplayZoomControls();
                            webSettings.setJavaScriptEnabled(true);
                            instituteWebView.loadUrl(instituteURL);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                new AlertDialog.Builder(getActivity())
                        .setMessage(error.toString())
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });


        whatsNewCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
            }
        });

        reportBugCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "adityabalsane10@gmail.com" });
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Report a bug");
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        Toast.makeText(getActivity(), "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                } catch(Exception e) {
                    Toast.makeText(getActivity(), "Sorry! STA couldn't find an mail app on your device :(", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        instituteCardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instituteURL));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setPackage("com.android.chrome");
                try {
                    getActivity().startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Chrome browser presumably not installed so allow user to choose instead
                    intent.setPackage(null);
                    getActivity().startActivity(intent);
                }
                return true;
            }
        });



        return view;

    }



}