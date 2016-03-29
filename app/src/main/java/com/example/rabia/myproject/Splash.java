package com.example.rabia.myproject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.parse.ParseUser;

import java.util.ArrayList;
/**
 * Created by AHS on 4/29/2015.
 */
public class Splash  extends Activity {
    Intent intent;
    Activity c;
    String id="";
    ProgressBar progress;
    ParseUser user;
    String error= " ";
    //private MobileServiceClient mobileServiceClient;
    private EasyTracker easyTracker = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
      //  FacebookSdk.sdkInitialize(this);
       // Parse.enableLocalDatastore(this);
GlobalVariables.c=this;
        easyTracker = EasyTracker.getInstance(this);

        progress =  (ProgressBar) findViewById(R.id.progressBar);
        c=this;
    user = ParseUser.getCurrentUser();
      /*  try {
            mobileServiceClient = new MobileServiceClient("https://gotcha1.azure-mobile.net/",
                    "hHJovueHaPeroTsQfTUktpZHqpGCpN67",
                    this
            );
        }
        catch (Exception e)
        {

        }
        User user2 = new User();
        user2.name="Our Zeni";
        try {
            mobileServiceClient.getTable(User.class).insert(user2, new TableOperationCallback<User>() {
                @Override
                public void onCompleted(User entity, Exception exception, ServiceFilterResponse response) {
                    if (exception == null)
                        Toast.makeText(c, "inserted", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(c, "failed", Toast.LENGTH_SHORT).show();


                }
            });
        }
        catch(Exception e)
        {
            Toast.makeText(c, "insertion failed", Toast.LENGTH_SHORT).show();
        }
       */

        new AsyncTask<Void,Integer,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                publishProgress(10);
                //SharedPreferences sharedPref = c.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                if(user!=null) {

                    try {
                        // ParseUser.logIn(id, "1");
                        UserDatabase db = new UserDatabase(c);

                        intent = new Intent(c, MainActivity.class);
                       publishProgress(30);
                        ArrayList<Person> list = db.getAllUsers();
                        publishProgress(40);

                 //       ArrayList<Person> invite = db.getUsersofType(FriendType.INVITABLE);
                      //  ArrayList<Person> invite = db.getUsersofType(FriendType.INVITABLE);
                     //   list.addAll(invite);
                        GlobalVariables.GenListContact(c);
                        publishProgress(60);
                        GlobalVariables.setAppUsers();
                       // GlobalVariables.setTrustedList(db.getUsersofType(FriendType.TRUSTED));
                        //if(GlobalVariables.getConnectivity())
                        {
                            GlobalVariables.GenTrusted();

                            GlobalVariables.GenRequests();
                        }
                        publishProgress(70);
                        GlobalVariables.setRequests(db.getUsersofType(FriendType.REQUESTED));

//                        GlobalVariables.GenListContact(c);
//
//                        GlobalVariables.setAppUsers();
//                        GlobalVariables.GenTrusted();
//                        GlobalVariables.GenRequests();

                        //  if(GlobalVariables.getConnectivity())
                    //    GlobalVariables.UpdateDb(c);
                        publishProgress(80);
                    } catch (Exception e) {
                        error=e.getMessage();
                        publishProgress(1);
                        intent=new Intent(c,LoginActivity.class);
                    }
                }
                else {
                    publishProgress(20);
                    intent = new Intent(c, LoginActivity.class);
                }



               // if(!id.equals("")) {

                return null;

            }

            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                TextView status = (TextView) c.findViewById(R.id.status);
                int val = values[0];
                if(val==1){Toast.makeText(c,error, Toast.LENGTH_LONG).show();return; }
                if (val==10 )  status.setText("Checking Login...");
                else if (val==20 && id=="") status.setText("No logged in user found");
                else if (val==20 ) status.setText("Welcome back "+id);
                else if (val==30  ) status.setText("Fetching contact list...");
                else if (val==40) status.setText("Fetching friend List...");
                else if (val==60) status.setText("Fetching trust requests...");
                else if (val==70) status.setText("Saving Data...");
                else if (val==80) status.setText("Redirecting to home...");
                else {

                }
                progress.setProgress(val);
            }
            @Override
        protected  void onPostExecute(Void parm)
            {
                super.onPostExecute(parm);

                if(intent!=null) {
                    finish();
                    startActivity(intent);
                }
            }
        }.execute();
    }



    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }
}
