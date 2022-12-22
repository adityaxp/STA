package com.aditya.projectapp.sta;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Aditya on 9/24/2020.
 */

public class CustomLoadingDialog {


    Activity activity;
    AlertDialog alertDialog;

    public CustomLoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    void customLoadingDialogShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_progress_dialog, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void  customLoaingDialogDismiss(){
        alertDialog.dismiss();
    }
}
