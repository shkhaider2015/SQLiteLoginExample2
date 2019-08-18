package com.example.sqliteloginexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    //DATABASE NAME
    public static final String DATABASE_NAME = "clints";

    //DATABASE VERSION
    public static final int DATABASE_VERSION = 1;

    //TABLE NAME
    public static final String TABLE_USERS = "users";

    //TABLE USERS COLUMNS
    //ID COLUMN @primaryKey
    public static final String KEY_ID = "id";

    //COLUMN user name
    public static final String KEY_USER_NAME = "username";

    //COLUMN email
    public static final String KEY_EMAIL = "email";

    //COLUMN password
    public static final String KEY_PASSWORD = "password";

    //COLUMN phone
    public static final String KEY_PHONE = "phone";

    //COLUMN profile picture
    public static final String KEY_PROFILE_PIC = "picture";

    //SQL for creating users table
    public static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USER_NAME + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT, "
            + KEY_PHONE + "TEXT, "
            + KEY_PROFILE_PIC + "TEXT"
            + " ) ";


    public SQLiteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
    }

    public void addUsers(UserDataModel userDataModel)
    {
        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //creating content values to insert
        ContentValues contentValues = new ContentValues();

        //Put username in  @values
        contentValues.put(KEY_USER_NAME, userDataModel.getName());
        contentValues.put(KEY_EMAIL, userDataModel.getEmail());
        contentValues.put(KEY_PASSWORD, userDataModel.getPassword());
        contentValues.put(KEY_PHONE, userDataModel.getCellNumber());
        contentValues.put(KEY_PROFILE_PIC, userDataModel.getProfilePicture());

        //insert row
        long todo_id = db.insert(TABLE_USERS, null, contentValues);
    }

    public UserDataModel authenticate(UserDataModel userDataModel)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,
                new String[]{
                        KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_PHONE, KEY_PROFILE_PIC
                },
                KEY_EMAIL + "=?",
                new String[]{
                        userDataModel.getEmail()
                }, null, null, null);

        if(cursor != null && cursor.moveToFirst() && cursor.getCount() > 0)
        {
            UserDataModel newUser = new UserDataModel();

            newUser.setId(cursor.getString(0));
            newUser.setName(cursor.getString(1));
            newUser.setEmail(cursor.getString(2));
            newUser.setPassword(cursor.getString(3));
            newUser.setCellNumber(cursor.getString(4));
            newUser.setProfilePicture(cursor.getBlob(5));

            if(userDataModel.getPassword().equals(newUser.getPassword()))
            {
                return newUser;
            }

        }
        return null;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_PHONE, KEY_PROFILE_PIC},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }
}
