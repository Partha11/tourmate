package com.syntaxerror.tourmate.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 3;

    public static final String DB_NAME = "user_data";
    public static final String TABLE_NAME = "mails";
    public static final String KEY_MAIL = "user_email";
    public static final String KEY_NAME = "user_name";

    public static final String EVENT_TABLE_NAME = "events";
    public static final String EVENT_KEY_ID = "event_id";
    public static final String EVENT_KEY_FROM_DATE = "from_date";
    public static final String EVENT_KEY_TO_DATE = "to_date";
    public static final String EVENT_KEY_DETAILS = "event_details";
    public static final String EVENT_KEY_BUDGET = "event_budget";

    private static String userdataQuery = "CREATE TABLE " + TABLE_NAME + "(" + KEY_MAIL + " TEXT PRIMARY KEY, " + KEY_NAME + " TEXT)";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}