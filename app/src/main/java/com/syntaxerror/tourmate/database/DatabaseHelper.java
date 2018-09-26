package com.syntaxerror.tourmate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 8;

    public static final String DB_NAME = "userdata_updated_v2";
    public static final String USER_TABLE_NAME = "users";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_MAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_FULL_NAME = "user_full_name";
    public static final String KEY_USER_GENDER = "user_gender";
    public static final String KEY_USER_AGE = "user_age";

    public static final String EVENT_TABLE_NAME = "events";
    public static final String EVENT_KEY_ID = "event_id";
    public static final String EVENT_KEY_FROM_DATE = "from_date";
    public static final String EVENT_KEY_TO_DATE = "to_date";
    public static final String EVENT_KEY_DETAILS = "event_details";
    public static final String EVENT_KEY_BUDGET = "event_budget";

    private static String userdataQuery = "CREATE TABLE " + USER_TABLE_NAME + "("
            + KEY_USER_ID + " TEXT PRIMARY KEY, " + KEY_USER_NAME + " TEXT, "
            + KEY_USER_MAIL + " TEXT, " + KEY_USER_FULL_NAME+ " TEXT, " + KEY_USER_GENDER+ " TEXT, "
            + KEY_USER_AGE + " TEXT)";

    private static String eventQuery = "CREATE TABLE " + EVENT_TABLE_NAME + "(" + EVENT_KEY_ID + " TEXT PRIMARY KEY, "
            + EVENT_KEY_DETAILS + " TEXT, " + EVENT_KEY_FROM_DATE + " TEXT, " + EVENT_KEY_TO_DATE + " TEXT, " +
            EVENT_KEY_BUDGET + " TEXT)";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(userdataQuery);
        sqLiteDatabase.execSQL(eventQuery);

        Log.d("Database", sqLiteDatabase.getAttachedDbs().toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}