package com.example.rabia.myproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ProfileTracker;
import com.google.analytics.tracking.android.EasyTracker;
import com.parse.ParseException;
import com.parse.ParseUser;


/**
 * Created by rabia on 18-Apr-15.
 */
public class LoginActivity extends Activity {
    private EditText username=null;
    private EditText no2 =null;
    private TextView  title=null;
    private TextView attempts;
    private Button login;
    private ProgressBar loading;
    int counter = 3;
    Context c;
 //   CallbackManager callbackManager;
    ProfileTracker ptracker;

    private EasyTracker easyTracker = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //FacebookSdk.sdkInitialize(getApplicationContext());


        easyTracker = EasyTracker.getInstance(this);

        setContentView(R.layout.login);
//        AdView mAdView = (AdView) findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        c=this;
        username = (EditText)findViewById(R.id.editText1);
        no2 = (EditText)findViewById(R.id.no2);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if( s.length()==3)
                    {
                        username.clearFocus();;
                      no2.requestFocus();
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        title = (TextView)findViewById(R.id.title);
        attempts = (TextView)findViewById(R.id.textView5);
        attempts.setText(Integer.toString(counter));
        login = (Button)findViewById(R.id.button1);
        loading = (ProgressBar)findViewById(R.id.loading);
        //LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        //loginButton.setReadPermissions(Arrays.asList("user_friends", "public_profile", "email", "user_photos", "user_birthday"));
        // If using in a fragment
      //  loginButton.setFragment(this);
        // Other app specific specialization

//         callbackManager = CallbackManager.Factory.create();
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//               Toast.makeText(c, "LOGGED IN SUCCESSFULLY"+loginResult.getAccessToken().getUserId(), Toast.LENGTH_LONG).show();
//                AccessToken token = loginResult.getAccessToken();
//
//               String userID= token.getUserId();
////                GraphRequest request = GraphRequest.newMeRequest(
////                        loginResult.getAccessToken(),
////                        new GraphRequest.GraphJSONObjectCallback() {
////                            @Override
////                            public void onCompleted(
////                                    JSONObject object,
////                                    GraphResponse response) {
////                                // Application code
////
////                                ((TextView) findViewById(R.id.userName)).setText(response.toString());
////                            }
////                        });
////                Bundle parameters = new Bundle();
////                parameters.putString("fields", "id,name,email");
////                request.setParameters(parameters);
////                request.executeAsync();
//
//               Profile.fetchProfileForCurrentAccessToken();
//                Profile profile = Profile.getCurrentProfile();
//                if(profile!=null)
//                {
//                    Toast.makeText(c,"DOWN "+ profile.getName(), Toast.LENGTH_LONG).show();
//
////                    try {
////                        URL imageURL = new URL("https://graph.facebook.com/" + profile.getId() + "/picture?type=large");
////                        ImageView image =(ImageView) findViewById(R.id.profilePic);
////                        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
////                        image.setImageBitmap(bitmap);
////
////                    } catch (MalformedURLException e) {
////                        e.printStackTrace();
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
//                }
//                else
//                    Toast.makeText(c,"Null profile", Toast.LENGTH_LONG).show();
//
//                //Intent  intent=new Intent(getApplicationContext(),MainActivity.class);
//                //SharedPreferences sharedPref = c.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
//                //SharedPreferences.Editor editor = sharedPref.edit();
//                //editor.putString("LoginID", profile.getName());
//                //editor.commit();
//                //intent.putExtra("userID",userID);
//                //Toast.makeText(c, "LOGGED IN SUCCESSFULLY " + userID, Toast.LENGTH_LONG).show();
//                //finish();
//                //startActivity(intent);
//            }
//            @Override
//            public void onCancel() {
//                // App code
//                Toast.makeText(c, "LOGGED IN CANCELED", Toast.LENGTH_LONG).show();
//
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//                Toast.makeText(c, "LOG IN ERROR", Toast.LENGTH_LONG).show();
//
//            }
//        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
     //   callbackManager.onActivityResult(requestCode, resultCode, data);

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
    boolean successfulLogin=false;
    Intent  intent=null;
    String no ="";
    public void login(View view) {
        if(username.getText().toString().length()!=3||no2.getText().toString().length()!=7)
        {
            Toast.makeText(c,"Please enter a valid phone number",Toast.LENGTH_LONG).show();
            return;
        }
        //user.setEmail("email@example.com");
        loading.setVisibility(View.VISIBLE);
        title.setText("Setting for the first time. Please wait");
        login.setVisibility(View.INVISIBLE);
    no="+92 "+username.getText().toString()+" "+no2.getText().toString();
        (new AsyncTask<Void,String,Void>()
{

    @Override
    protected Void doInBackground(Void... params) {


    ParseUser user = new ParseUser();
    user.setUsername(no);
    user.setPassword("1");
          intent=new Intent(getApplicationContext(),MainActivity.class);

//        user.signUpInBackground(new SignUpCallback() {
//            public void done(ParseException e) {
//                if (e == null) {        // SUCCEED
        publishProgress("Logging In...");
        try {
            ParseUser.logIn(no,"1");
//            Toast.makeText(getApplicationContext(), "Redirecting...",
//                    Toast.LENGTH_SHORT).show();

            SharedPreferences sharedPref = c.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("LoginID", no);
            editor.commit();

            publishProgress("Logged In successfully...");
            successfulLogin=true;
        } catch (ParseException e1) {
            e1.printStackTrace();

            try
            {

                publishProgress("No User found. Signing ou up....");
                user.signUp();

                publishProgress("Successfully signed up...");
                successfulLogin=true;
            } catch (ParseException e)
            {

                publishProgress("Signing up failed");
                e.printStackTrace();
             }

        }
        if(successfulLogin) {

            publishProgress("Fetching friend list...");
            GlobalVariables.GenListContact(c);

            publishProgress("Syncing friends...");
            GlobalVariables.setAppUsers();

            publishProgress("Fetching Trusted Friends...");
            GlobalVariables.GenTrusted();

            publishProgress("Fetching Trust requests...");
            GlobalVariables.GenRequests();
            UserDatabase db = new UserDatabase(c);

            publishProgress("Removing temp files...");
            db.delAll();
//            (new AsyncTask<Void, Void,Void>() {
//                @Override
//                protected Void doInBackground(Void params) {
//                    return null;
//                }
//            }).execute();
            publishProgress("Saving information...");
            db.addAll(GlobalVariables.listContact);
            db.addAll(GlobalVariables.requests);
            db.addAll(GlobalVariables.trustedList);

        }

            return null;
    }


            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                    title.setText(values[0]);
            }
                @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (successfulLogin && intent!=null) {
            finish();
            startActivity(intent);
        }
        else {
            Toast.makeText(c, "Error Occured\nCheck you intenet connection", Toast.LENGTH_LONG).show();
            login.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);

            title.setText("Enter your cell number to login");
        }

    }
}).execute();



    }


}
