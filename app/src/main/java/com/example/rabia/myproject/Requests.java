package com.example.rabia.myproject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AHS on 5/6/2015.
 */
public class Requests  extends ActionBarActivity {

    Context c;

    ArrayList<Person> requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.requests);
        requests = new  ArrayList<Person>();
        c=this;
        Person p0 = new Person("Zain haider","Karim Block","abc@gmail.com","123456",null);
        Person p1 = new Person("Minhaj Shahid","Karim Block","abc@gmail.com","123456",null);
        Person p2 = new Person("Daniyal Mughal","Karim Block","abc@gmail.com","123456",null);
        Person p3 = new Person("Rabia Athar","Karim Block","abc@gmail.com","123456",null);
        Person p4 = new Person("Meena S. Sheikh","Karim Block","abc@gmail.com","123456",null);
        p0.set_lastSeen("Sent on : 14 Aug 2014, 12:45 AM");
        p1.set_lastSeen("Sent on : 5 Jan 2015, 12:45 AM");
        p2.set_lastSeen("Sent on : 27 Jan 2015, 1:45 PM");
        p3.set_lastSeen("Sent on : 1 March 2015, 3:45 PM");
        p4.set_lastSeen("Sent on : Today, 10:06 AM");
        requests.add(p0);
        requests.add(p3);
        requests.add(p1);
        requests.add(p2);
        requests.add(p4);
        ListView lv  = (ListView) findViewById(R.id.req_list);
        lv.setAdapter(new RequestAdapter(this,R.layout.req_row,GlobalVariables.getRequests()));

       TextView req_count  = (TextView) findViewById(R.id.req_count);
       String req_text = "You have ";
        int count= GlobalVariables.getRequests().size();
        if(count>0)
        req_text+= String.valueOf (count);
        else
            req_text+="NO";

        req_text+=" Trust Request(s)";

         req_count.setText(req_text);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.requests_menu, menu);
        return true;
    }
}
