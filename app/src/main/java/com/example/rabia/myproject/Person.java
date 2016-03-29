package com.example.rabia.myproject;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import java.util.ArrayList;

/**
 * Created by rabia on 17-Apr-15.
 */

public class Person  {
    String name;
    ParseGeoPoint cordinates;
    ArrayList<Person> friends;
    String location="NA";
    String username=" ";
    String password=" ";
    String lastSeen="NA";
    String phoneNo=" ";
    int code=2;
    Person(){}
    Person(String n,String locate,String usern,String phone,ArrayList<Person> obj){
        name=n;
        location=locate;
        friends=obj;
        username=usern;
        phoneNo=phone;
    }
    void setCordinates( ParseGeoPoint cordinate)
    {
        cordinates=cordinate;
    }
    ParseGeoPoint getCordinates( )
    {
        return cordinates;
    }

    String get_lastSeen()
    {	return lastSeen;	}
    void set_lastSeen(String _lastSeen)
    {	lastSeen= _lastSeen;	}
    String get_phoneNo()
    {	return phoneNo;	}
    void set_Phone(String phone)
    {	phoneNo= phone;	}

    void setUsername(String n){username=n;}
    String getUsername()
    {return username;}
    int get_code()
    {	return code;	}
    void set_code(int _code)
    {	code= _code;	}

    void setName(String n){name=n;}
    String getName()
    {return name;}

    void setPassword(String n){password=n;}
    String getPassword()
    {return password;}

    void setLocation(String n){location=n;}
    String getLocation()
    {return location;}
}
