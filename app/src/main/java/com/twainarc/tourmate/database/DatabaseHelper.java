package com.twainarc.tourmate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 11;

    public static final String DB_NAME = "userdata_updated_v5";
    public static final String USER_TABLE_NAME = "users";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_MAIL = "user_email";
    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_FULL_NAME = "user_full_name";
    public static final String KEY_USER_GENDER = "user_gender";
    public static final String KEY_USER_AGE = "user_age";
    public static final String KEY_USER_IMG = "user_image";

    public static final String EVENT_TABLE_NAME = "events";
    public static final String EVENT_KEY_ID = "event_id";
    public static final String EVENT_KEY_FROM_DATE = "from_date";
    public static final String EVENT_KEY_TO_DATE = "to_date";
    public static final String EVENT_KEY_DETAILS = "event_details";
    public static final String EVENT_KEY_BUDGET = "event_budget";

    public static final String MEMORIES_TABLE_NAME = "memories";
    public static final String MEMORIES_KEY_ID = "event_key";
    public static final String MEMORIES_KEY_URI = "uri";

    private static String userdataQuery = "CREATE TABLE " + USER_TABLE_NAME + "("
            + KEY_USER_ID + " TEXT PRIMARY KEY, " + KEY_USER_NAME + " TEXT, "
            + KEY_USER_MAIL + " TEXT, " + KEY_USER_FULL_NAME+ " TEXT, " + KEY_USER_GENDER+ " TEXT, "
            + KEY_USER_AGE + " TEXT, " + KEY_USER_IMG + " TEXT)";

    private static String eventQuery = "CREATE TABLE " + EVENT_TABLE_NAME + "(" + EVENT_KEY_ID + " TEXT PRIMARY KEY, "
            + EVENT_KEY_DETAILS + " TEXT, " + EVENT_KEY_FROM_DATE + " TEXT, " + EVENT_KEY_TO_DATE + " TEXT, " +
            EVENT_KEY_BUDGET + " TEXT)";

    private static String memoriesQuery = "CREATE TABLE " + MEMORIES_TABLE_NAME + "(" + MEMORIES_KEY_URI + " TEXT PRIMARY KEY, "
            + MEMORIES_KEY_ID + " TEXT);";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(userdataQuery);
        sqLiteDatabase.execSQL(eventQuery);
        sqLiteDatabase.execSQL(memoriesQuery);

        Log.d("Database", sqLiteDatabase.getAttachedDbs().toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}