package com.example.rabia.myproject;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by AHSAN on 5/18/2015.
 */
public class thisApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Crash Reporting.
        ParseCrashReporting.enable(this);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        // Add your initialization code here

        ParseObject.registerSubclass(Friendship.class);

        Parse.initialize(this, "yqlmU9RJUAyRqDRA25dJpYJd0Q8xTsB4pwiWRitg", "8VTDj8QQnhaAS0wUwjeA8s94IQQKD39QitOZ6Jlc");



        //FacebookSdk.sdkInitialize(this);
    }

}
