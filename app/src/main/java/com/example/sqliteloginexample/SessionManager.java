package com.example.sqliteloginexample;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "prefName";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String FLAG = "flag";

    private static String TAG = SessionManager.class.getSimpleName();

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    public SessionManager(Context context)
    {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();

    }

    public void setEmail(String email)
    {
        editor.putString(EMAIL, email);
        editor.commit();
    }
    public String getEmail()
    {
        return preferences.getString(EMAIL, null);
    }
}
