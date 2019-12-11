package com.techmave.tourmate.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {

        this.prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setInstalled(boolean value) {

        editor = prefs.edit();

        editor.putBoolean(Constants.PREF_INSTALLED, value);
        editor.apply();
    }

    public boolean getInstalled() {

        return prefs.getBoolean(Constants.PREF_INSTALLED, false);
    }

    public void setUid(String userId) {

        editor = prefs.edit();

        editor.putString(Constants.PREF_UID, userId);
        editor.apply();
    }

    public String getUid() {

        return prefs.getString(Constants.PREF_UID, "");
    }

    public void setUserEmail(String email) {

        editor = prefs.edit();

        editor.putString(Constants.PREF_EMAIL, email);
        editor.apply();
    }

    public void setToken(String token) {

        editor = prefs.edit();

        editor.putString(Constants.PREF_TOKEN, token);
        editor.apply();
    }

    public String getToken() {

        return prefs.getString(Constants.PREF_TOKEN, "");
    }
}
