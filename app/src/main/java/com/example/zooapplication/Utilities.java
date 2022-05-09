package com.example.zooapplication;
/**
 * File Name: Utilities.java
 * Description: show error box if users type some illegal input.
 */

import android.app.Activity;
import android.app.AlertDialog;

/**
 * show alert if users type inappropriate things.
 */
public class Utilities {
    public static void showAlert(Activity activity, String message){
        //construct error box
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
