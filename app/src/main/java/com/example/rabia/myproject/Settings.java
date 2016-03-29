package com.example.rabia.myproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

import java.util.ArrayList;




public class Settings extends ActionBarActivity {

    private EasyTracker easyTracker = null;

    static int time;
    static int circle;
    static  boolean syncOverWiFi;
    static  boolean vibrate;
    Context c;
    static ArrayList<Setting> settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ListView lv = (ListView) findViewById(R.id.setting_list);

        easyTracker = EasyTracker.getInstance(this);
        c=this;

        init(c);

        lv.setAdapter(new SettingAdapter(this, R.layout.settings_row, settings));

        AdapterView.OnItemClickListener rowListener = new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                if(position==1) {
                    revertWiFi(c);
                    CheckBox chk = (CheckBox)(v.findViewById(R.id.setting_check));
                    chk.setChecked(syncOverWiFi);
                }
                if(position==4) {
                    revertVibrate(c);
                    CheckBox chk = (CheckBox)(v.findViewById(R.id.setting_check));
                    chk.setChecked(vibrate);
                }

                Toast.makeText(c,settings.get(position).get_title()+" Clicked", Toast.LENGTH_SHORT).show();

            }
        };
        lv.setOnItemClickListener(rowListener);
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

    static public void init(Context con)
    {
        SharedPreferences sharedPref = con.getSharedPreferences(con.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        time = sharedPref.getInt("interval",5);
        circle = sharedPref.getInt("circle",3);
        syncOverWiFi = sharedPref.getBoolean("syncOverWiFi", true);
        vibrate = sharedPref.getBoolean("vibrate", true);
        if(settings==null)
        settings = new ArrayList<Setting>();
        else
        settings.clear();


        Setting syncTime = new Setting("Sync Location Every", String.valueOf(time)+" minutes",false,false);
        Setting wifiOnly = new Setting("Sync Location on","WiFi only",syncOverWiFi,true);
        Setting notificationCat = new Setting("","NOTIFICATIONS",false,false);
        notificationCat.set_isCategory(true);
        Setting circle = new Setting("Circle for notification",String.valueOf(time)+" km",false,false);
        Setting vibration = new Setting("Vibrate on notification","",vibrate,true);


        settings.add(syncTime);
        settings.add(wifiOnly);
        settings.add(notificationCat);
        settings.add(circle);
        settings.add(vibration);


    }

    static public  void revertWiFi(Context con)
    {
        SharedPreferences sharedPref = con.getSharedPreferences(con.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
       syncOverWiFi = sharedPref.getBoolean("syncOverWiFi",true);
        syncOverWiFi = ! syncOverWiFi;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("syncOverWiFi",syncOverWiFi);
        editor.commit();
        if(syncOverWiFi)
            Toast.makeText(con, "Syncing will be over WiFi only", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(con,"Syncing will be over all possible net traffic", Toast.LENGTH_LONG).show();
    }
    static public void revertVibrate(Context con)
    {
        SharedPreferences sharedPref = con.getSharedPreferences(con.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
         vibrate = sharedPref.getBoolean("vibrate",vibrate);
        vibrate = ! vibrate;
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("vibrate",vibrate);
        editor.commit();

        if(vibrate)
            Toast.makeText(con, "Vibration on", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(con,"Vibration off", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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
}