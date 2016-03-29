package com.example.rabia.myproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rabia on 27-Apr-15.
 */
public class UserDatabase extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "usersDB.db";

    // Contacts table name
    private static final String TABLE_USERS = "users";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phoneNo";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_LAST_SEEN = "lastSeen";
    private static final String KEY_CODE = "code";

    //for friends???

    public UserDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                +   KEY_ID +        " INTEGER PRIMARY KEY,"
                +   KEY_NAME +      " TEXT,"
                +   KEY_PHONE +     " TEXT,"
                +   KEY_LOCATION +  " TEXT,"
                +   KEY_LAST_SEEN + " TEXT,"
                +   KEY_CODE      + " INT"      + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    void addPerson(Person user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName()); //  Name
        values.put(KEY_LOCATION,user.getLocation()); // Location
        values.put(KEY_LAST_SEEN,user.get_lastSeen()); // Location
        values.put(KEY_PHONE,user.get_phoneNo()); // Location
        values.put(KEY_CODE, user.get_code()); // Location

        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        db.close(); // Closing database connection
    }
    void addAll(List<Person> list)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        for(int i= 0 ; i<list.size();i++) {
            Person user = list.get(i);
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, user.getName()); //  Name
            values.put(KEY_LOCATION, user.getLocation()); // Location
            values.put(KEY_LAST_SEEN, user.get_lastSeen()); // Location
            values.put(KEY_PHONE, user.get_phoneNo()); // Location
            values.put(KEY_CODE, user.get_code()); // Location

        // Inserting Row
        db.insert(TABLE_USERS, null, values);
        }
        db.close(); // Closing database connection
    }
    // Getting single user
    Person getPerson(String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
//yahan check krna hy
        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PHONE, KEY_LOCATION,KEY_CODE }, KEY_PHONE + "=?" ,
                new String[] { phone}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Person user = new Person(cursor.getString(1),cursor.getString(3), cursor.getString(2), cursor.getString(2),null);
        user.set_code(cursor.getInt(4));
        // return contact

        return user;
    }

    // Getting All Users
    public ArrayList<Person> getAllUsers() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ArrayList<Person> userList = new ArrayList<Person>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Person user = new Person(cursor.getString(1),cursor.getString(3), cursor.getString(2), cursor.getString(2),null);
                user.set_code(cursor.getInt(4));

                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userList;
    }

    // Updating single contact
    public int updatePerson(Person user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_CODE,user.get_code()); // Username
        values.put(KEY_PHONE,user.get_phoneNo()); // Password
        values.put(KEY_LOCATION,user.getLocation()); // Location
        values.put(KEY_LAST_SEEN,user.get_lastSeen()); // LAST SEEN

        return db.update(TABLE_USERS, values, KEY_PHONE + " = ?", new String[] { String.valueOf(user.get_phoneNo()) });
    }
    public void commitPerson(Person user) {             //updates if already exists, otherwise insert a new one
       if(exists(user))
           updatePerson(user);
        else
           addPerson(user);
     }
    public boolean exists(Person user) {             //updates if already exists, otherwise insert a new one
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID }, KEY_PHONE + "=?" ,
                new String[] { user.get_phoneNo()}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else return false;
db.close();
       return cursor.getCount()>0;

    }
    // Deleting single record
    public void deletePerson(Person contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[] { contact.get_phoneNo() });
        db.close();
    }
    // Getting  Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        // return count
        return cursor.getCount();
    }

    public ArrayList<Person> getUsersofType(FriendType f)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_ID,
                        KEY_NAME, KEY_PHONE, KEY_LOCATION, KEY_CODE}, KEY_CODE + "=?",
                new String[]{String.valueOf(f.ordinal())}, null, null, null, null);
       ArrayList<Person> userList = new ArrayList<Person>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Person user = new Person(cursor.getString(1),cursor.getString(3), cursor.getString(2), cursor.getString(2),null);
                user.set_code(cursor.getInt(4));

                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userList;

    }

    public ArrayList<Person> getAll()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[] { KEY_ID,
                        KEY_NAME, KEY_PHONE, KEY_LOCATION,KEY_CODE }, null ,
                null, null, null, null, null);
        ArrayList<Person> userList = new ArrayList<Person>();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Person user = new Person(cursor.getString(1),cursor.getString(3), cursor.getString(2), cursor.getString(2),null);
                user.set_code(cursor.getInt(4));

                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userList;

    }
    public void delAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS,null,null);
        db.close();
    }

}
