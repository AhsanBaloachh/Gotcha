package com.example.rabia.myproject;

import android.app.Activity;
import android.content.Context;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by AHS on 4/30/2015.
 */
public class FriendAdapter extends ArrayAdapter<Person> {
    Context c;
    int layoutFile;
    // ArrayList<MyDataClass> data;
    ArrayList<Person> data;
    boolean isForTrusted;
    public FriendAdapter(Context context, int resource, ArrayList<Person> objects, boolean _isForTrusted ) {
        super(context, resource, objects);
        isForTrusted = _isForTrusted;
        c = context;
        layoutFile = resource;
        data = objects;

        GlobalVariables.SOrt(data);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v, row;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) c).getLayoutInflater();
            row = inflater.inflate(R.layout.friends_row, parent, false);
        } else {
            row = (View) convertView;
        }
        TextView txt = (TextView) row.findViewById(R.id.name);

      //  ((ImageView) row.findViewById(R.id.pic)).setImageResource(R.drawable.person);

        // txt.setText(data.get(position).getName());
        txt.setText(data.get(position).getName());
        // data[position].getTxtTitle();

        Button btn = (Button) row.findViewById(R.id.btn);
        // For UnTrusted Friends
        if(!isForTrusted) {

            btn.setTag(position);
            TextView text = (TextView) row.findViewById(R.id.descr);
            if (data.get(position).get_code() == 1) {
                // btn.setBackgroundColor(Color.BLUE);
                // btn.setImageResource(R.drawable.pin);
                 btn.setText("Request");
                text.setText("Send TRUST REQUEST");
            } else if (data.get(position).get_code() == 2) {
                // btn.setBackgroundColor(Color.GREEN);
                // btn.setImageResource(R.drawable.invite);
                text.setText("Send INVITE");
                btn.setText("Invite");

            }
            else if (data.get(position).get_code() == 3) {
                // btn.setBackgroundColor(Color.GREEN);
                // btn.setImageResource(R.drawable.invite);
                text.setText("Accept Request");
                btn.setText("Accept");

            }

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) ((Button) v).getTag();
                    String text = "btn Click!";
                    String msg  = "\nTry GOTCHA! It keeps you in touch with your close ones.\n\nDownload from http://gotcha.maxtechx.com";
                    String friendName = data.get(position).getName();
                    try {
                        if (data.get(position).get_code() == 1) {
                            text = "Request Sent to ";
                            Friendship friendship = new Friendship();
                            friendship.clearAccepted();
                            friendship.setUserID(ParseUser.getCurrentUser().getUsername());
                            friendship.setFriendID(data.get(position).get_phoneNo());
                            friendship.saveInBackground();
                        } else if (data.get(position).get_code() == 2) {
                            text = "Invitation sent to ";
                            String phoneNo = data.get(position).get_phoneNo();
                            SmsManager.getDefault().sendTextMessage(phoneNo, null, "Hi " + friendName + msg, null, null);
                        } else if (data.get(position).get_code() == 3) {
                            text = "Accepted of ";
                            List<Friendship> objects = GlobalVariables.getFriendshipList();
                            for (int i = 0; i < objects.size(); i++) {
                                if (objects.get(i).getFriendID() == data.get(position).get_phoneNo()) {
                                    objects.get(i).Accept();
                                    objects.get(i).saveInBackground();
                                    break;
                                }
                            }
                        }
                    }
                    catch(Exception e)
                    {
                        Toast.makeText(c, e.toString(), Toast.LENGTH_LONG).show();

                    }
                    text = text + friendName;
                    Toast.makeText(c, text, Toast.LENGTH_LONG).show();
                }

            });

        }
        else {
            btn.setVisibility(View.GONE);
            ((TextView) row.findViewById(R.id.descr)).setText("Latest Location : "+data.get(position).getLocation());
        }

        return row;
    }


}