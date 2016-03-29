package com.example.rabia.myproject;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.ContactsContract;
import android.view.View;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by AHSAN on 5/14/2015.
 */
public class GlobalVariables {
    public static ArrayList<Person> listContact = new ArrayList<>();

    public static void setListContacts(ArrayList<Person> list) {
        listContact = list;
    }

    public static ArrayList<Person> getListContacts() {
        return listContact;
    }

    public static ArrayList<Person> getRequests() {
        return requests;
    }

    public static ArrayList<Person> trustedList = new ArrayList<>();

    public static ArrayList<Person> requests = new ArrayList<>();

    public static void setRequests(ArrayList<Person> list) {
        requests = list;
    }

    private static List<ParseUser> appUsers;

    public static void setTrustedList(ArrayList<Person> list) {
        trustedList = list;
    }

    public static ArrayList<Person> getTrustedList() {
        return trustedList;
    }

    GlobalVariables(Context _c) {
        c = _c;
    }

    public static ArrayList<ParseUser> allUsers = new ArrayList<>();

    public static View mapActivity;

    public static View getmapActivity() {
        return mapActivity;
    }

    public static void setmapActivity(View v) {
        mapActivity = v;
    }

    public static List<Friendship> objects;

    public static List<Friendship> getFriendshipList() {
        return objects;
    }

    public static ParseGeoPoint curLocation = new ParseGeoPoint();

    public static ParseGeoPoint getCurLocation() {
        return curLocation;
    }

    public static void setCurLocation(ParseGeoPoint p) {
        curLocation = p;
    }

    static public Context c;

    public static void GenListContact(Context _c) {

        c = _c;
       // listContact.clear();
        ArrayList<Person> templist = new ArrayList<>();

//        if(!getConnectivity())
//        {
//            UserDatabase db = new UserDatabase(c);
//            listContact=  db.getUsersofType(FriendType.APPUSER);
//            listContact.addAll(db.getUsersofType(FriendType.INVITABLE));
//
//            return;
//        }
        // listContact.add(new Person("Ahsan","Karim Block","abc@gmail.com","+92 336 4306816",null));
        ContentResolver cr = c.getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if (cur.getCount() > 0) {
            while (cur.moveToNext()) {
                String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
               // if(name.contains("+21"))
                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    // System.out.println("name : " + name + ", ID : " + id);

                    // get the phone number
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    if (pCur.moveToNext()) {
                        String phone = pCur.getString(
                                pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        //System.out.println("phone" + phone);
                            if(!phone.contains("+92"))
                                phone="+92 "+phone.substring(1);
                        templist.add(new Person(name, "Karim Block", "abc@gmail.com", phone, null));
                    }
                    pCur.close();

                    // get email and type
                }
            }
        }
        listContact.clear();
        listContact.addAll(templist);
        cur.close();

    }

    public static void setAppUsers() {

        if (!getConnectivity()) return;
        List<String> list = new ArrayList<>();

        for (int i = 0; i < GlobalVariables.getListContacts().size(); i++) {
            list.add(GlobalVariables.getListContacts().get(i).get_phoneNo());
        }
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereContainedIn("username", list); // find adults

        try {
            List<ParseUser> objects = query.find();
            allUsers.addAll(objects);
            for (int i = 0; i < objects.size(); i++) {
                String user = objects.get(i).getUsername();
                if(contain(user,trustedList)<0) {
                    int ind = contain(user, listContact);
                    listContact.get(ind).set_lastSeen(objects.get(i).getUpdatedAt().toString());
                    Object last = objects.get(i).get("lastPlace");
                    if (last != null)
                        listContact.get(ind).setLocation(last.toString());
                    if (ind >= 0) {
                        listContact.get(ind).set_code(1);

                    }
                }
            }
            //appUsers=objects;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SOrt(listContact);
    }

    private static int contain(String phone, List<Person> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).get_phoneNo().equals(phone)) return i;
        }
        return -1;
    }

    public static void GenTrusted() {
        //Person per = new Person("ZAIN","Karim Block","abc@gmail.com","+92 345 6665060" ,null);
      //  trustedList.clear();
        if(trustedList.size()<1)
        {
          //  Person per = new Person(listContact.get(0).getName(), "Karim Block", "abc@gmail.com", listContact.get(0).get_phoneNo(), null);
          //  per.set_code(0);
          //  trustedList.add(per);
        }
     //   if (!getConnectivity()) return;

        String userID = ParseUser.getCurrentUser().getUsername();

        ParseQuery<Friendship> query = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("UserID", userID);
        query.whereEqualTo("isAccepted", true);

        try {
            objects = query.find();
            int ind;
            for (int i = 0; i < objects.size(); i++) {
                String user = objects.get(i).getFriendID();
                ind = contain(user, listContact);
                int already = contain(user, trustedList);
                if (objects.get(i).isAccepted()) {
                    if (ind >= 0 && already<0) {
                        listContact.get(ind).set_code(0);
                        ParseUser curUser = getPersonAppUser(listContact.get(ind));
                        //ParseUser curUser = objects.get(i).getUser();

                        listContact.get(ind).setCordinates(curUser.getParseGeoPoint("location"));
                        listContact.get(ind).setLocation(curUser.getString("lastPlace"));

                        UserDatabase db = new UserDatabase(c);
//                        db.updatePerson( listContact.get(ind));
//                        db.close();

                        trustedList.add(listContact.get(ind));

                        listContact.remove(ind);
                    } else {

                        String friendID = objects.get(i).getFriendID();
                        Person person ;
                        if(already<0)
                        person = new Person(friendID, "Karim Block", "abc@gmail.com", friendID, null);
                        else
                        person = trustedList.get(already);
                        person.set_code(0);

                        ParseQuery<ParseUser> getUser = ParseUser.getQuery();
                        getUser.whereEqualTo("username", friendID);
                        ParseUser cur=null;
                        try {
                             cur= getUser.find().get(0);
                            person.setCordinates(cur.getParseGeoPoint("location"));
                            person.setLocation(cur.getString("lastPlace"));


                        } catch (Exception e) {

                        }

                        UserDatabase db = new UserDatabase(c);
                       if(already<0) {
                           trustedList.add(person);
                           // db.addPerson(person);
                          if(cur!=null)
                           allUsers.add(cur);
                       }
                      //  else  db.updatePerson( person);

                        db.close();
                    }
                }

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    static ParseUser getPersonAppUser(Person p) {
        for (int i = 0; i < allUsers.size(); i++) {
            String Uname = allUsers.get(i).getUsername();
            if (Uname.equals(p.get_phoneNo())) return allUsers.get(i);
        }
        return null;
    }

    public static void GenRequests() {
       // if (!getConnectivity()) return;

        //Person per = new Person("ZAIN","Karim Block","abc@gmail.com","+92 345 6665060" ,null);

        String userID = ParseUser.getCurrentUser().getUsername();

        ParseQuery<Friendship> query = ParseQuery.getQuery("Friendship");
        query.whereEqualTo("FriendID", userID);
    requests.clear();
        try {
            objects = query.find();
            int ind;
            for (int i = 0; i < objects.size(); i++) {
                String user = objects.get(i).getUserID();
                ind = contain(user, listContact);
                int already = contain(user, requests);
                if (!objects.get(i).isAccepted()&& already<0) {
                    if (ind >= 0 ) {
                        listContact.get(ind).set_code(3);
                        UserDatabase db = new UserDatabase(c);
                       // db.updatePerson( listContact.get(ind));
                        db.close();
                        requests.add(listContact.get(ind));
                        listContact.remove(ind);

                    } else {
                        Person person = new Person(objects.get(i).getUserID(), "Karim Block", "abc@gmail.com", objects.get(i).getUserID(), null);
                        person.set_code(3);
                        requests.add(person);
                        ParseQuery<ParseUser> getUser = ParseUser.getQuery();
                        query.whereEqualTo("username", objects.get(i).getUserID()); // find adults
                        try {
                            allUsers.add(getUser.find().get(0));
                        } catch (Exception e) {

                        }
                        UserDatabase db = new UserDatabase(c);
                        if(already>=0)
                        {
                          //  db.updatePerson( person);
                        }
                        else
                           // db.addPerson(person);
                        db.close();

                    }
                }


            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    static boolean UpdateDb(Context con) {
        try {
            UserDatabase db = new UserDatabase(con);
            for (int i = 0; i < listContact.size(); i++) {
                db.commitPerson(listContact.get(i));
            }

            for (int i = 0; i < trustedList.size(); i++) {
                db.commitPerson(trustedList.get(i));
            }
            for (int i = 0; i < requests.size(); i++) {
                db.commitPerson(requests.get(i));
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    static boolean getConnectivity() {
        ConnectivityManager connectivity = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        if (info != null) {
            if (!info.isConnected()) {
                return false;
            } else {
                return true;
            }

        } else {
            // OFF
            return false;
        }

    }


    public static ParseGeoPoint getLatestLocation() {
        Criteria criteria = new Criteria();
        LocationManager locationManager;

        // Get the LocationManager object from the System Service LOCATION_SERVICE
        locationManager = (LocationManager) c.getSystemService(c.LOCATION_SERVICE);

        // Get the name of the best available provider
        String provider = locationManager.getBestProvider(criteria, true);

        // We can use the provider immediately to get the last known location
        Location location = locationManager.getLastKnownLocation(provider);
        ParseGeoPoint p = new ParseGeoPoint();
        p.setLatitude(location.getLatitude());
        p.setLongitude(location.getLongitude());
        return p;
    }

    static public void SOrt(List<Person> data)
    {
        Collections.sort(data, new Comparator<Person>()
            {
                @Override
                public int compare(Person lhs, Person rhs) {
                    return lhs.getName().toLowerCase().compareTo(rhs.getName().toLowerCase());
                }
            }

    );

    Collections.sort(data,new Comparator<Person>()
    {
        @Override
        public int compare (Person lhs, Person rhs){
        return (lhs.get_code() - rhs.get_code());
    }
    }

    );
}
}
