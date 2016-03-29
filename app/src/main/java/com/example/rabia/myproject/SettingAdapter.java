package com.example.rabia.myproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AHS on 5/5/2015.
 */
public class SettingAdapter extends ArrayAdapter<Setting> {
    Context c;
    int layoutFile;
    // ArrayList<MyDataClass> data;
    ArrayList<Setting> data;
    boolean isForTrusted;
    public SettingAdapter(Context context, int resource, ArrayList<Setting> objects) {
        super(context, resource, objects);
        c = context;
        layoutFile = resource;
        data = objects;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v, row;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) c).getLayoutInflater();
            row = inflater.inflate(R.layout.settings_row, parent, false);
        } else {
            row = (View) convertView;
        }

        Setting cur = data.get(position);
        TextView txt = (TextView) row.findViewById(R.id.setting_title);
        txt.setText(cur.get_title());

        TextView desc = (TextView) row.findViewById(R.id.setting_desc);
        desc.setText(cur.get_desc());


        CheckBox chk = (CheckBox) row.findViewById(R.id.setting_check);
        chk.setTag(position);
        chk.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos =(int) buttonView.getTag();
                CheckBox chkbox =(CheckBox)buttonView;
                if(pos ==4)
                {
                    Settings.revertVibrate(c);
                    Settings.init(c);
                    chkbox.setChecked(Settings.vibrate);
                }
                if(pos ==1)
                {
                    Settings.revertWiFi(c);
                    Settings.init(c);
                    chkbox.setChecked(Settings.syncOverWiFi);

                }

            }
        });
        if(!cur.get_isCheckAvailable())
        {
            chk.setVisibility(View.GONE);
        }
        else if(cur.get_checked())
        {
            chk.setChecked(cur.get_checked());
        }

        View line = row.findViewById(R.id.line);
        if(!cur.get_isCategory())
        {
            line.setVisibility(View.GONE);
        }
        return row;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }
}