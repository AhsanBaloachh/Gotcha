package com.example.rabia.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

/**
 * Created by AHS on 4/29/2015.
 */
public class ConfirmationDialog {
    String message;
    Context context;
    String title;
    boolean ans=false;
    DialogInterface.OnClickListener yes;
    public ConfirmationDialog(String _title,String _message,DialogInterface.OnClickListener _yes, Context c) {
    yes=_yes;
        this.message = _message;
        context =c;
        title=_title;
    }
    public boolean ask() throws Resources.NotFoundException {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(
                        null)
                .setPositiveButton(
                        "YES",yes)
                .setNegativeButton(
                        "NO",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here
                                ans=false;
                            }
                        }
                       ).show();

        return ans;
    }
}
