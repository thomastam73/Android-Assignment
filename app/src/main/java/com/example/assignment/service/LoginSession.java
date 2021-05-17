package com.example.assignment.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.assignment.activity.Login;

public class LoginSession {
    private static final String loginStatus = "status_login",
            userID = "UserID",
            userEmail = "UserEmail";

    private static SharedPreferences getSharedReferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void setDataLogin(Context context, boolean status) {
        SharedPreferences.Editor editor = getSharedReferences(context).edit();
        editor.putBoolean(loginStatus, status);
        editor.apply();
    }

    public static boolean getDataLogin(Context context) {
        return getSharedReferences(context).getBoolean(loginStatus, false);
    }

    public static void setUserID(Context context, String id) {
        SharedPreferences.Editor editor = getSharedReferences(context).edit();
        editor.putString(userID, id);
        editor.apply();
    }

    public static String getUserID(Context context) {
        return getSharedReferences(context).getString(userID, "");
    }

    public static void setUserEmail(Context context, String email) {
        SharedPreferences.Editor editor = getSharedReferences(context).edit();
        editor.putString(userEmail, email);
        editor.apply();
    }

    public static String getUserEmail(Context context) {
        return getSharedReferences(context).getString(userEmail, "");
    }


    public static void clearData(Context context) {
        SharedPreferences.Editor editor = getSharedReferences(context).edit();
        editor.remove(loginStatus);
        editor.remove(userID);
        editor.remove(userEmail);
        editor.apply();
        Intent intent = new Intent();
        intent.setClass(context, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

}
