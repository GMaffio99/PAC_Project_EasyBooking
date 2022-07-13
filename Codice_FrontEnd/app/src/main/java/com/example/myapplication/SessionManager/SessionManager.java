package com.example.myapplication.SessionManager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    private static final String IS_LOGGED_IN = "IsLoggedIn";

    public static final String KEY_ID = "UserID";
    public static final String KEY_NAME = "Name";
    public static final String KEY_SURNAME = "Surname";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_CODICEFISCALE = "CodiceFiscale";
    public static final String KEY_DOB = "DateOfBirth";

    public SessionManager(Context _context) {

        context = _context;
        userSession = context.getSharedPreferences("userLoginSession", Context.MODE_PRIVATE);
        editor = userSession.edit();

    }

    public void createLoginSession(String id, String name, String surname,String email, String codiceFiscale, String dob) {

        editor.putBoolean(IS_LOGGED_IN,true);

        editor.putString(KEY_ID,id);
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_SURNAME,surname);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_CODICEFISCALE,codiceFiscale);
        editor.putString(KEY_DOB,dob);

        editor.commit();

    }

    public HashMap<String,String> getUserDetailsFromSession(){

        HashMap<String,String> userData = new HashMap<String,String>();

        userData.put(KEY_ID,userSession.getString(KEY_ID,null));
        userData.put(KEY_NAME,userSession.getString(KEY_NAME,null));
        userData.put(KEY_SURNAME,userSession.getString(KEY_SURNAME,null));
        userData.put(KEY_EMAIL,userSession.getString(KEY_EMAIL,null));
        userData.put(KEY_CODICEFISCALE,userSession.getString(KEY_CODICEFISCALE,null));
        userData.put(KEY_DOB,userSession.getString(KEY_DOB,null));

        return userData;

    }

    public Boolean checkLogin(){
        if (userSession.getBoolean(IS_LOGGED_IN,false))
            return true;
        else
            return false;
    }

    public void logoutUserFromSession() {
        editor.clear();
        editor.commit();
    }

}
