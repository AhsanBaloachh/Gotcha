package com.example.rabia.myproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.analytics.tracking.android.EasyTracker;

import java.util.Arrays;


public class FacebookLogin extends Activity {

    Context c;
    CallbackManager callbackManager;
    private EasyTracker easyTracker = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook__login);
        easyTracker = EasyTracker.getInstance(this);

        c=this;
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_buttonSec);
        loginButton.setReadPermissions(Arrays.asList("user_friends", "public_profile", "email", "user_photos", "user_birthday"));
        // If using in a fragment
        //  loginButton.setFragment(this);
        // Other app specific specialization

        callbackManager = CallbackManager.Factory.create();
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Toast.makeText(c, "LOGGED IN SUCCESSFULLY" + loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
                AccessToken token = loginResult.getAccessToken();

                String userID= token.getUserId();

                Profile.fetchProfileForCurrentAccessToken();
                Profile profile = Profile.getCurrentProfile();
                if(profile!=null)
                {
                    Toast.makeText(c,"Logged in as "+ profile.getName(), Toast.LENGTH_LONG).show();

                    Intent intent = getIntent();
                    setResult(RESULT_OK, intent);
                    // finish()  quits the current activity and returns back to the previous one
                    finish();

                }
                else
                    Toast.makeText(c,"Null profile", Toast.LENGTH_LONG).show();

            }
            @Override
            public void onCancel() {
                // App code
                Toast.makeText(c, "LOG IN CANCELED", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(c, "LOG IN ERROR", Toast.LENGTH_LONG).show();

            }
        });
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
