package com.example.shen_mini_itx.msgapp.Common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.shen_mini_itx.msgapp.Activity.SplashActivity;

/**
 * Created by shen-mini-itx on 21-Jul-16.
 */
public class Session {

    // Shared pref file name
    private static final String PREFER_NAME = "MsgAppSessionPref";
    private static final String IS_USER_LOGIN = "IsUserLoggedIn";
    private static final String USER_ID = "id";
    private static final String USER_G_ID = "g_id";
    private static final String USER_NAME = "name";
    private static final String USER_EMAIL = "email";
    private static final String USER_IMAGE = "image";
    private static final String API = "api";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public Session(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createUserLoginSession(String id, String g_id, String name, String email, String image, String api) {

        editor.putBoolean(IS_USER_LOGIN, true);
        editor.putString(USER_ID, id);
        editor.putString(USER_G_ID, g_id);
        editor.putString(USER_NAME, name);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_IMAGE, image);
        editor.putString(API, api);

        editor.commit();
    }

    public String getApi() {
        return pref.getString(API, null);
    }

    public void setApi(String api) {
        editor.putString(API, api);
        editor.commit();
    }

    public String getUserId() {
        return pref.getString(USER_ID, null);
    }

    public String getUserGId() {
        return pref.getString(USER_G_ID, null);
    }

    public String getUserName() {
        return pref.getString(USER_NAME, null);
    }

    public void setUserName(String name) {
        editor.putString(USER_NAME, name);
        editor.commit();
    }

    public String getUserEmail() {
        return pref.getString(USER_EMAIL, null);
    }

    public void setUserEmail(String email) {
        editor.putString(USER_EMAIL, email);
        editor.commit();
    }

    public String getUserImage() {
        return pref.getString(USER_IMAGE, null);
    }

    public void setUserImage(String image) {
        editor.putString(USER_IMAGE, image);
        editor.commit();
    }

    public void logoutUser() {

        // Clearing all user data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Login Activity
        Intent i = new Intent(context, SplashActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
    }


    // Check for login
    public boolean isUserLoggedIn() {
        return pref.getBoolean(IS_USER_LOGIN, false);
    }
}
