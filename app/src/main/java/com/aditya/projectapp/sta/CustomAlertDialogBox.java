package com.aditya.projectapp.sta;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAlertDialogBox {

    Activity activity;
    AlertDialog alertDialog;
    Button okButton;
    TextView okTextView;


    public CustomAlertDialogBox(Activity activity) {
        this.activity = activity;
    }

    void CustomAlertDialogBoxShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.custom_institute_dialogbox, null);

        okButton = view.findViewById(R.id.okButton);
        okTextView = view.findViewById(R.id.troubleText);


        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    void CustomAlertDialogBoxShow(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        View view = activity.getLayoutInflater().inflate(R.layout.custom_institute_dialogbox, null);

        okButton = view.findViewById(R.id.okButton);
        okTextView = view.findViewById(R.id.troubleText);

        okTextView.setText(message);

        builder.setView(view);
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }


    void  CustomAlertDialogBoxDismiss(){
        alertDialog.dismiss();
    }

}
