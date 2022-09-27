package com.twainarc.tourmate.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.twainarc.tourmate.model.Event;
import com.twainarc.tourmate.model.Image;
import com.twainarc.tourmate.utils.Constants;

@Database(entities = {Event.class, Image.class}, version = Constants.DATABASE_VERSION, exportSchema = false)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao getUserDao();
    private static UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context) {

        if (instance == null) {

            instance = Room.databaseBuilder(context, UserDatabase.class, Constants.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
