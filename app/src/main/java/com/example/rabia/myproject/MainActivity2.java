package com.example.rabia.myproject;

/**
 * Created by AHSAN on 5/15/2015.
 */
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;


    public class MainActivity2 extends ActionBarActivity implements LocationListener
    {

        GoogleMap mMap;
        static final LatLng HAMBURG = new LatLng(31.5214, 74.4025);

        Location location;

        LatLng source,destination;
        private LocationManager locManager;
        private LocationListener locListener = new MyLocationListener();





        @Override
        public void onLocationChanged(Location location) {
            if (mMap != null)
            {drawMarker(location);
            }
        }

        @Override
        public void onProviderDisabled(String arg0) {
            // Do something here if you would like to know when the provider is disabled by the user
        }

        @Override
        public void onProviderEnabled(String arg0) {
            // Do something here if you would like to know when the provider is enabled by the user
        }

        @Override
        public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
            // Do something here if you would like to know when the provider status changes
        }





        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.home);

            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

            LocationManager locationManager;
            locManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

            // Get the LocationManager object from the System Service LOCATION_SERVICE
            locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);

            boolean enabledGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean enabledWiFi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!enabledGPS ) {
                Toast.makeText(this, "GPS signal not found", Toast.LENGTH_LONG).show();
            }
            else
            {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);

                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);

            }



            // Create a criteria object needed to retrieve the provider
            Criteria criteria = new Criteria();

            // Get the name of the best available provider
            String provider = locationManager.getBestProvider(criteria, true);

            // We can use the provider immediately to get the last known location
            Location location = locationManager.getLastKnownLocation(provider);

// request that the provider send this activity GPS updates every 20 seconds
            locationManager.requestLocationUpdates(provider, 20000, 0, this);

            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            // onLocationChanged(location);

            destination=new LatLng(HAMBURG.latitude,HAMBURG.longitude);
            //source=new LatLng(location.getLatitude(),location.getLongitude());
//drawMarker(location);


            Marker hamburg = mMap.addMarker(new MarkerOptions().position(HAMBURG)
                    .title("Hamburg"));



            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));



        }


        private void drawMarker(Location location){
            mMap.clear();

//  convert the location object to a LatLng object that can be used by the map API
            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

            source=new LatLng(location.getLatitude(), location.getLongitude());

// zoom to the current location
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition,16));

// add a marker to the map indicating our current position
            mMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .snippet("Lat:" + location.getLatitude() + "Lng:"+ location.getLongitude()));
        }

        public void getDirections(View view)
        {



            //String ending = String.format(Locale.ENGLISH, "geo:%f,%f", HAMBURG.latitude, HAMBURG.longitude);

            // String starting = String.format(Locale.ENGLISH, "geo:%f,%f", location.getLatitude(), location.getLongitude());

            String geoUri="http://maps.google.com/maps?addr="+source.latitude+","+source.longitude+ "&daddr="+destination.latitude+","+destination.longitude;
            Intent mapCall = new Intent (Intent.ACTION_VIEW, Uri.parse(geoUri));
            startActivity(mapCall);

            //new GetDirections().execute();

        }




        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
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

        class MyLocationListener implements LocationListener {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    // This needs to stop getting the location data and save the battery power.
                    locManager.removeUpdates(locListener);

                    source = new LatLng(location.getLatitude(),location.getLongitude());



                }
            }

            @Override
            public void onProviderDisabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onProviderEnabled(String provider) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                // TODO Auto-generated method stub

            }
        }


    }

