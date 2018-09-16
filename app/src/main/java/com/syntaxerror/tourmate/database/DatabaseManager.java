package com.syntaxerror.tourmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.EventLog;

import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.SingleUser;

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

    public boolean insertEventData(Events event) {

        database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.EVENT_KEY_DETAILS, event.getTravelDescription());
        contentValues.put(DatabaseHelper.EVENT_KEY_FROM_DATE, event.getFromDate());
        contentValues.put(DatabaseHelper.EVENT_KEY_TO_DATE, event.getToDate());
        contentValues.put(DatabaseHelper.EVENT_KEY_BUDGET, event.getEstimatedBudget());

        long isInserted = database.insert(DatabaseHelper.EVENT_TABLE_NAME, null, contentValues);

        database.close();

        if (isInserted > 0)

            return true;

        else

            return false;
    }

    public List<Events> getAllEventsData() {

        database = helper.getReadableDatabase();

        List<Events> eventsList = new ArrayList<>();

        Events event = new Events();

        Cursor cursor;
        String userMail = "";
        String query = "SELECT * FROM " + DatabaseHelper.EVENT_TABLE_NAME + ";";

        cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            while (cursor.moveToNext()) {

                String mTravelDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_DETAILS));
                String mTravelEstBudget = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_BUDGET));
                String mTravelFromDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_FROM_DATE));
                String mTravelToDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_TO_DATE));

                event = new Events(mTravelDescription, mTravelEstBudget, mTravelFromDate, mTravelToDate);
                eventsList.add(event);
            }
        }

        database.close();

        return eventsList;
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