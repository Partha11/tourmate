package com.techmave.tourmate.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefData {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static final String EVENT_ID = "eventId";
    private static final String PREF_USER_ID = "userId";
    private static final String PREF_NAME = "events";

    public SharedPrefData(Context context) {

        prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    public void insertEventId(String eventId) {

        editor = prefs.edit();

        if (!TextUtils.isEmpty(eventId)) {

            editor.putString(EVENT_ID, eventId);
            editor.commit();
        }
    }

    public String fetchEventId() {

        return prefs.getString(EVENT_ID, null);
    }

    public String getUserId() {

        return prefs.getString(PREF_USER_ID, null);
    }
}
