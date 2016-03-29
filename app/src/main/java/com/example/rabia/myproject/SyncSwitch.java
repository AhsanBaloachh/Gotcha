package com.example.rabia.myproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AHSAN on 5/16/2015.
 */
public class SyncSwitch  extends BroadcastReceiver {

    Timer timer = new Timer();
    static Context c;
   static boolean isAvtive=false;
  static Handler handler = new Handler();
    static int interval = 5*60*1000;
    static Runnable SyncLocation = new Runnable() {


        public void run() {
            if(isAvtive) {

               // try
                {

                Criteria criteria = new Criteria();
                LocationManager locationManager;

                // Get the LocationManager object from the System Service LOCATION_SERVICE
                locationManager = (LocationManager)c.getSystemService(c.LOCATION_SERVICE);

                // Get the name of the best available provider
                String provider = locationManager.getBestProvider(criteria, true);

                // We can use the provider immediately to get the last known location
                Location location = locationManager.getLastKnownLocation(provider);
                ParseGeoPoint p= GlobalVariables.getCurLocation();
                p.setLatitude(location.getLatitude());
                p.setLongitude(location.getLongitude());
                ParseUser user = ParseUser.getCurrentUser();
                user.put("location",p);

                    user.saveEventually();
                }


                UserDatabase db = new UserDatabase(c);
                List<Person> all = db.getUsersofType(FriendType.TRUSTED);
                List<String> keys = new ArrayList<>();
                for (int i = 0; i < all.size(); i++) {
                    keys.add(all.get(i).get_phoneNo());
                }

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereContainedIn("username", keys);

                try {
                    List<ParseUser> objects = query.find();
                    for (int i = 0; i < objects.size(); i++) {
                        Object last = objects.get(i).get("lastPlace");
                        if (last != null) {
                            all.get(i).setLocation(last.toString());
                            db.updatePerson(all.get(i));
                        }
                        ParseGeoPoint point = objects.get(i).getParseGeoPoint("location");
                        if(point!=null) {
                            if (point.distanceInKilometersTo(GlobalVariables.getCurLocation()) < 5) {
                                Toast.makeText(c, all.get(i).getName() + " is near you", Toast.LENGTH_LONG).show();
                            }
                        }

                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(c,e.getMessage(),Toast.LENGTH_LONG).show();
                }

                handler.postDelayed(this, interval);
            }
        }
    };
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        c=context;
        Toast.makeText(c, "Syncing...", Toast.LENGTH_LONG).show();
        handler.postDelayed(SyncLocation, interval);

                            // TODO Auto-generated method stub
                            ConnectivityManager connectivity = (ConnectivityManager) c
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo info = connectivity.getActiveNetworkInfo();
                            if ( info != null)
                            {
                                if(!info.isConnected())
                                {
                                    isAvtive=false;
                                }
                                else
                                {
                                    isAvtive=true;
                                }

                            }
                            else
                            {
                                // OFF
                                isAvtive=false;
                            }

    }

  }

