package com.aditya.projectapp.sta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SelectVideoApp {

    Activity activity;
    AlertDialog alertDialog;
    Button zoomButton, googleMeetButton, ciscoWebexButton, msTeamsButton, skypeButton;

    public SelectVideoApp(Activity activity) {
        this.activity = activity;
    }

    void chooseApp(){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.choose_video_alert_dialog_box, null);

        zoomButton = (Button) view.findViewById(R.id.zoomButton);
        googleMeetButton = (Button) view.findViewById(R.id.googleMeetButton);
        ciscoWebexButton = (Button) view.findViewById(R.id.ciscoWebexButton);
        msTeamsButton = (Button) view.findViewById(R.id.msTeamsButton);
        skypeButton = (Button) view.findViewById(R.id.skypeButton);

        builder.setView(view);

        alertDialog = builder.create();
        alertDialog.show();

        zoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager pm = activity.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("us.zoom.videomeetings");
                    activity.startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    alertDialog.dismiss();
                    Toast.makeText(activity, "Zoom not installed", Toast.LENGTH_SHORT).show();
                    new androidx.appcompat.app.AlertDialog.Builder(activity)
                            .setTitle("Do you want to install Zoom?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(
                                            "https://play.google.com/store/apps/details?id=us.zoom.videomeetings"));
                                    intent.setPackage("com.android.vending");
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });


        googleMeetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager pm = activity.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("com.google.android.apps.meetings");
                    activity.startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    alertDialog.dismiss();
                    Toast.makeText(activity, "Google Meet not installed", Toast.LENGTH_SHORT).show();
                    new androidx.appcompat.app.AlertDialog.Builder(activity)
                            .setTitle("Do you want to install Google Meet?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(
                                            "https://play.google.com/store/apps/details?id=com.google.android.apps.meetings"));
                                    intent.setPackage("com.android.vending");
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });

        ciscoWebexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager pm = activity.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("com.cisco.webex.meetings");
                    activity.startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    alertDialog.dismiss();
                    Toast.makeText(activity, "Cisco Webex not installed", Toast.LENGTH_SHORT).show();
                    new androidx.appcompat.app.AlertDialog.Builder(activity)
                            .setTitle("Do you want to install Cisco Webex?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(
                                            "https://play.google.com/store/apps/details?id=com.cisco.webex.meetings"));
                                    intent.setPackage("com.android.vending");
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });

        msTeamsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager pm = activity.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("com.microsoft.teams");
                    activity.startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    alertDialog.dismiss();
                    Toast.makeText(activity, "Microsoft Teams not installed", Toast.LENGTH_SHORT).show();
                    new androidx.appcompat.app.AlertDialog.Builder(activity)
                            .setTitle("Do you want to install Microsoft Teams?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(
                                            "https://play.google.com/store/apps/details?id=com.microsoft.teams"));
                                    intent.setPackage("com.android.vending");
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });

        skypeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    PackageManager pm = activity.getPackageManager();
                    Intent intent = pm.getLaunchIntentForPackage("com.skype.raider");
                    activity.startActivity(intent);
                    alertDialog.dismiss();
                }catch (Exception e){
                    alertDialog.dismiss();
                    Toast.makeText(activity, "Skype not installed", Toast.LENGTH_SHORT).show();
                    new androidx.appcompat.app.AlertDialog.Builder(activity)
                            .setTitle("Do you want to install Skype?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse(
                                            "https://play.google.com/store/apps/details?id=com.skype.raider"));
                                    intent.setPackage("com.android.vending");
                                    activity.startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }
            }
        });


    }




}
