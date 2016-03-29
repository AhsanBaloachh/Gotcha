package com.example.rabia.myproject;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by AHSAN on 5/14/2015.
 */
@ParseClassName("Friendship")
public class Friendship extends ParseObject {
    public Friendship()
    {
        super();
    }

    public String getUserID() {
        return getString("UserID");
    }

    public String getFriendID() {
        return getString("FriendID");
    }
    public boolean isAccepted() {
        return getBoolean("isAccepted");
    }
    // Use put to modify field values


    // Get the user for this item
    public ParseUser getUser()  {
        return getParseUser("owner");
    }


    public void setUser(ParseUser user) {
        put("owner", user);
    }

    public void setUserID(String ID) {
        put("UserID", ID);
    }
    public void setFriendID(String ID) {
        put("FriendID", ID);
    }

    public void clearAccepted() {
        put("isAccepted", false);
    }
    public void Accept() {
        put("isAccepted", true);
    }
}
