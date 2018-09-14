package com.syntaxerror.tourmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {

        helper = new DatabaseHelper(context);
    }

    public boolean insertData(SingleUser singleUser) {

        database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.KEY_MAIL, singleUser.getUserMail());
        contentValues.put(DatabaseHelper.KEY_NAME, singleUser.getUserName());

        long isInserted = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);

        database.close();

        if (isInserted > 0)

            return true;

        else

            return false;
    }

    public String getUserMail(String userName) {

        database = helper.getReadableDatabase();

        Cursor cursor;
        String userMail = "";
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.KEY_NAME +
                " = '" + userName + "';";

        cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            userMail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_MAIL));

            database.close();
        }

        if (!TextUtils.isEmpty(userMail))

            return userMail;

        else

            return null;
    }

    public boolean deleteData(String userEmail) {

        database = helper.getWritableDatabase();

        String whereClause = DatabaseHelper.KEY_MAIL + " = ?";
        String[] whereArgs = {userEmail};

        int isDeleted = database.delete(DatabaseHelper.TABLE_NAME, whereClause, whereArgs);

        if (isDeleted > 0)

            return true;

        else

            return false;
    }
}