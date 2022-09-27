package com.twainarc.tourmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.twainarc.tourmate.model.Event;
import com.twainarc.tourmate.model.SingleUser;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {

    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public DatabaseManager(Context context) {

        helper = new DatabaseHelper(context);
    }

    public boolean insertSingleUser(SingleUser singleUser) {

        database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        String fullName = "haha";

        contentValues.put(DatabaseHelper.KEY_USER_ID, singleUser.getUserId());
        contentValues.put(DatabaseHelper.KEY_USER_NAME, singleUser.getUserName());
        contentValues.put(DatabaseHelper.KEY_USER_MAIL, singleUser.getUserMail());
        contentValues.put(DatabaseHelper.KEY_USER_FULL_NAME, fullName);
        contentValues.put(DatabaseHelper.KEY_USER_IMG, singleUser.getProfileImage());

        Log.d("DatabaseManager", singleUser.getProfileImage());

        if (singleUser.getUserGender() == null)

            contentValues.put(DatabaseHelper.KEY_USER_GENDER, "");

        else

            contentValues.put(DatabaseHelper.KEY_USER_GENDER, singleUser.getUserGender());

        if (singleUser.getUserAge() == null)

            contentValues.put(DatabaseHelper.KEY_USER_AGE, "");

        else

            contentValues.put(DatabaseHelper.KEY_USER_AGE, singleUser.getUserAge());

        long isInserted = database.insert(DatabaseHelper.USER_TABLE_NAME, null, contentValues);

        database.close();

        if (isInserted > 0)

            return true;

        else

            return false;
    }

    public boolean insertSingleMemory(String id, String uri) {

        database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.MEMORIES_KEY_URI, uri);
        contentValues.put(DatabaseHelper.MEMORIES_KEY_ID, id);

        long isInserted = database.insert(DatabaseHelper.MEMORIES_TABLE_NAME, null, contentValues);

        database.close();

        if (isInserted > 0)

            return true;

        else

            return false;
    }

    public boolean insertEventData(Event event) {

        database = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.EVENT_KEY_DETAILS, event.getDescription());
        contentValues.put(DatabaseHelper.EVENT_KEY_FROM_DATE, event.getFromDate());
        contentValues.put(DatabaseHelper.EVENT_KEY_TO_DATE, event.getToDate());
        contentValues.put(DatabaseHelper.EVENT_KEY_BUDGET, event.getBudget());

        long isInserted = database.insert(DatabaseHelper.EVENT_TABLE_NAME, null, contentValues);

        database.close();

        if (isInserted > 0)

            return true;

        else

            return false;
    }

    public SingleUser getSingleUserData() {

        database = helper.getReadableDatabase();
        SingleUser singleUser = new SingleUser();

        String query = "SELECT * FROM " + DatabaseHelper.USER_TABLE_NAME + ";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            String mUserId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_ID));
            String mUserName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_NAME));
            String mUserMail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_MAIL));
            String mUserFullName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_FULL_NAME));
            String mUserGender = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_GENDER));
            String mUserAge = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_AGE));
            String mUserImg = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_IMG));

            if (mUserGender == null)

                mUserGender = "Male";

            if (mUserAge == null)

                mUserAge = "Not set";

            String[] names = TextUtils.split(mUserFullName, " ");
            singleUser = new SingleUser();
        }

        database.close();
        return singleUser;
    }

    public List<Event> getAllEventsData() {

        database = helper.getReadableDatabase();
        List<Event> eventList = new ArrayList<>();
        Event event = new Event();
        String query = "SELECT * FROM " + DatabaseHelper.EVENT_TABLE_NAME + ";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            do {

                String mTravelId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_ID));
                String mTravelDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_DETAILS));
                String mTravelEstBudget = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_BUDGET));
                String mTravelFromDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_FROM_DATE));
                String mTravelToDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_TO_DATE));

                event = new Event();
                eventList.add(event);

            } while (cursor.moveToNext());
        }

        database.close();

        return eventList;
    }

    public List<Uri> getAllMemories() {

        database = helper.getReadableDatabase();
        List<Uri> memoriesList = new ArrayList<>();
        String singleMemory;
        String query = "SELECT * FROM " + DatabaseHelper.MEMORIES_TABLE_NAME + ";";

        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            do {

                singleMemory = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MEMORIES_KEY_URI));
                memoriesList.add(Uri.parse(singleMemory));

            } while (cursor.moveToNext());
        }

        database.close();

        return memoriesList;
    }

    public String getUserMail(String userName) {

        database = helper.getReadableDatabase();

        Cursor cursor;
        String userMail = "";
        String query = "SELECT * FROM " + DatabaseHelper.USER_TABLE_NAME + " WHERE " + DatabaseHelper.KEY_USER_NAME +
                " = '" + userName + "';";

        cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {

            userMail = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_USER_MAIL));

            database.close();
        }

        if (!TextUtils.isEmpty(userMail))

            return userMail;

        else

            return null;
    }

    public void deleteSingleUser() {

        database = helper.getWritableDatabase();
        database.delete(DatabaseHelper.USER_TABLE_NAME, null, null);
        database.close();
    }

    public void deleteSingleEvent(String eventId) {

        database = helper.getWritableDatabase();

        String whereClause = DatabaseHelper.EVENT_KEY_ID + " = ?";
        String[] whereArgs = {eventId};
        database.delete(DatabaseHelper.EVENT_TABLE_NAME, whereClause, whereArgs);

        database.close();
    }

    public boolean deleteAllData() {

        database = helper.getWritableDatabase();
        int isDeleted = database.delete(DatabaseHelper.EVENT_TABLE_NAME, null, null);

        database.close();

        if (isDeleted > 0)

            return true;

        else

            return false;
    }
}