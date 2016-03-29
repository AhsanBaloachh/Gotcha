package com.example.rabia.myproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

/**
 * Created by AHS on 5/7/2015.
 */
public class RequestAdapter extends ArrayAdapter<Person> {
    Context c;
    int layoutFile;
    ArrayList<Person> data;

    public RequestAdapter(Context context, int resource, ArrayList<Person> objects) {
        super(context, resource, objects);
        c = context;
        layoutFile = resource;
        data = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v, row;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) c).getLayoutInflater();
            row = inflater.inflate(R.layout.req_row, parent, false);
        } else {
            row = (View) convertView;
        }
        TextView txt = (TextView) row.findViewById(R.id.req_name);
        txt.setText(data.get(position).getName());

        TextView time = (TextView) row.findViewById(R.id.req_descr);
        time.setText(data.get(position).get_lastSeen());

        Button noBtn = (Button) row.findViewById(R.id.req_no);
        noBtn.setTag(position);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index =(int) ((Button)v).getTag();
                ParseQuery<Friendship> query = new ParseQuery<Friendship>("Friendship");
                query.whereEqualTo("UserID",data.get(index).get_phoneNo());
                query.whereEqualTo("FriendID", ParseUser.getCurrentUser().getUsername());
                try {
                    Friendship friendship = query.find().get(0);

                    friendship.clearAccepted();
                    friendship.saveEventually();
                    Toast.makeText(c,data.get(index).getName()+"'s request declined",Toast.LENGTH_LONG).show();
                    GlobalVariables.getRequests().remove(index);
                    notifyDataSetChanged();
                }
               catch (Exception e)
               {
                   Toast.makeText(c,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();

               }
                 }
        });
        Button yesBtn = (Button) row.findViewById(R.id.req_yes);
        yesBtn.setTag(position);
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index =(int) ((Button)v).getTag();
                ParseQuery<Friendship> query = new ParseQuery<Friendship>("Friendship");
                query.whereEqualTo("UserID",data.get(index).get_phoneNo());
                query.whereEqualTo("FriendID", ParseUser.getCurrentUser().getUsername());

                try {
                    Friendship friendship = query.find().get(0);
                    friendship.Accept();
                    friendship.saveEventually();
                    Toast.makeText(c,data.get(index).getName()+"'s request accepted",Toast.LENGTH_LONG).show();
                    GlobalVariables.getRequests().remove(index);
                    notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Toast.makeText(c,"Error "+e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

        return row;
    }
    @Override
    public int getCount()
    {
        return data.size();
    }
}


