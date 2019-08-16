package com.example.sqliteloginexample;

import android.content.ContentValues;
import android.content.Context;
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
}
