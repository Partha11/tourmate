package com.syntaxerror.tourmate.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.EventLog;

import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.FullName;
import com.syntaxerror.tourmate.pojos.SingleUser;

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

        String fullName = singleUser.getFullName().getFirstName() + " " + singleUser.getFullName().getLastName();

        contentValues.put(DatabaseHelper.KEY_USER_ID, singleUser.getUserId());
        contentValues.put(DatabaseHelper.KEY_USER_NAME, singleUser.getUserName());
        contentValues.put(DatabaseHelper.KEY_USER_FULL_NAME, fullName);

        if (singleUser.getUserGender() == null)

            contentValues.put(DatabaseHelper.KEY_USER_GENDER, "Not set");

        else

            contentValues.put(DatabaseHelper.KEY_USER_GENDER, singleUser.getUserGender());

        if (singleUser.getUserAge() == null)

            contentValues.put(DatabaseHelper.KEY_USER_AGE, "Not set");

        else

            contentValues.put(DatabaseHelper.KEY_USER_AGE, singleUser.getUserAge());

        long isInserted = database.insert(DatabaseHelper.USER_TABLE_NAME, null, contentValues);

        database.close();

        if (isInserted > 0)

            return true;

        else

            return false;
    }

    public boolean insertEventData(Events event) {

        database = helper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.EVENT_KEY_ID, event.getEventId());
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

            if (mUserGender == null)

                mUserGender = "Male";

            if (mUserAge == null)

                mUserAge = "Not set";

            String[] names = TextUtils.split(mUserFullName, " ");
            singleUser = new SingleUser(mUserId, mUserMail, mUserName, mUserGender, mUserAge, new FullName(names[0], names[1]));
        }

        database.close();
        return singleUser;
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

                String mTravelId = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_ID));
                String mTravelDescription = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_DETAILS));
                String mTravelEstBudget = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_BUDGET));
                String mTravelFromDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_FROM_DATE));
                String mTravelToDate = cursor.getString(cursor.getColumnIndex(DatabaseHelper.EVENT_KEY_TO_DATE));

                event = new Events(mTravelId, mTravelDescription, mTravelEstBudget, mTravelFromDate, mTravelToDate);
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