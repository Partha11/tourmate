package com.syntaxerror.tourmate;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;

    public static final String DB_NAME = "username_db";
    public static final String TABLE_NAME = "mails";
    public static final String KEY_ID = "id";
    public static final String KEY_MAIL = "user_email";
    public static final String KEY_NAME = "user_name";

    private static String query = "CREATE TABLE " + TABLE_NAME + "(" + KEY_MAIL + " TEXT PRIMARY KEY, " + KEY_NAME + " TEXT)";

    public DatabaseHelper(Context context) {

        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}