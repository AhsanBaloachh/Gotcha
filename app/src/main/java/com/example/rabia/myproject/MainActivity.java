package com.example.rabia.myproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.share.widget.ShareDialog;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;

public class MainActivity extends ActionBarActivity  {
    Context c;
   // CallbackManager callbackManager;
    ShareDialog shareDialog;
    String filepath; // filepath contains path of the file
    public static GoogleAnalytics analytics;
    public static Tracker tracker;
   public static long count=100;
    private EasyTracker easyTracker = null;

    //EasyTracker tracker1 = EasyTracker.getInstance(this);
   // tracker1.set(Fields.SCREEN_NAME, name);
    //tracker1.send(MapBuilder.createAppView().build());


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GlobalVariables.c = this;
        //Analytics

        analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(1800);

        tracker = analytics.newTracker("UA-63141090-1"); // Replace with actual tracker/property Id
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);

 easyTracker = EasyTracker.getInstance(MainActivity.this);



        c=this;
   // shareDialog = new ShareDialog(this);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new SampleFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));
// Give the SlidingTabLayout the ViewPager
       SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
// Center the tabs in the layout
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(viewPager);
        // Customize tab color
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.RED;
            }
        });
      //  Parse.enableLocalDatastore(this);
        askforGPS();
        LocationManager locationManager=    (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        MyCurrentLoctionListener locationListener = new MyCurrentLoctionListener();
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }
    public class MyCurrentLoctionListener implements LocationListener {

        public void onLocationChanged(Location location) {
           if(count==100) { count=0;}
            else{ count++; return;}
            location.getLatitude();
            location.getLongitude();

            String myLocation = "Latitude = " + location.getLatitude() + " Longitude = " + location.getLongitude();

            //I make a log to see the results
           // Toast.makeText(c,myLocation,Toast.LENGTH_LONG).show();
            Log.e("MY CURRENT LOCATION", myLocation);
            ParseGeoPoint p = new ParseGeoPoint();
            p.setLongitude(location.getLongitude());
            p.setLatitude(location.getLatitude());
            ParseUser user = ParseUser.getCurrentUser();
            user.put("location", p);

            List<Address> geocodeMatches = null;
            try {
                geocodeMatches =
                        new Geocoder(c).getFromLocation(p.getLatitude(), p.getLongitude(), 1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!geocodeMatches.isEmpty())
            {
                StringBuilder str = new StringBuilder();

                String Address1 = geocodeMatches.get(0).getAddressLine(0);
               String  Address2 = geocodeMatches.get(0).getAddressLine(1);
               String State = geocodeMatches.get(0).getAdminArea();
                // Zipcode = geocodeMatches.get(0).getPostalCode();
                String Country = geocodeMatches.get(0).getCountryName();

                ///String str=Address1;

                str.append(Address1+","+ Address2 + ",");
                str.append(State + "," + Country + ".");

                user.put("lastPlace", str.toString());
                 //Toast.makeText(c, str.toString(), Toast.LENGTH_LONG).show();

            //    address.setText( str );
                PageFragment.place.setText(str);

            }
            user.saveEventually();
          //  PersonDetails.drawMarker(location,PageFragment.map);
        }

        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        public void onProviderEnabled(String s) {

        }

        public void onProviderDisabled(String s) {

        }
    }
    public void askforGPS()
    {
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Build the alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }
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
        if(requestCode==2)
        {
         //   if(resultCode==RESULT_OK)
        //    postOnFacebook();
        }
        else{}
        //callbackManager.onActivityResult(requestCode, resultCode, data);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void postOnFacebook()
    {
//        Profile profile =  Profile.getCurrentProfile();
//        if(profile==null) {
//        startActivityForResult(new Intent(this, FacebookLogin.class), 2, null);
//        }
//       /*.setContentTitle("Friend Locator")
//            .setContentDescription(
//                    "Get in touch with your close ones..")
//            .setContentUrl(Uri.parse("http://developers.facebook.com/android"))*/
//        else {
//            if (ShareDialog.canShow(ShareLinkContent.class)) {
//                ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                        .build();
//                shareDialog.show(linkContent);
//            }
//        }
            /*Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
           SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
            shareDialog.show(content);*/
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,Settings.class));
            return true;
        }
         else   if (id == R.id.logout)
        {
            DialogInterface.OnClickListener  yes =  new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog,
                                    int which) {
                    //Do Something Here
                    SharedPreferences sharedPref = c.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("LoginID", "");
                    editor.commit();
                  //  LoginManager.getInstance().logOut();
                    ParseUser.logOut();
                    UserDatabase db = new UserDatabase(c);
                    db.delAll();
                    finish();
                    startActivity(new Intent(c, LoginActivity.class));

                }
            };

            ConfirmationDialog confirm = new ConfirmationDialog("Logout","Are you sure to want to logout",yes,this);
            confirm.ask();
            return true;
        }
        else if(id==R.id.req)
        {
            startActivity(new Intent(this,Requests.class));

        }
        else if(id==R.id.fbPost)
        {

        //    postOnFacebook();
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
       // AppEventsLogger.deactivateApp(this);
    }
}
