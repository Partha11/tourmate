package com.twainarc.tourmate.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.twainarc.tourmate.model.Event;
import com.twainarc.tourmate.model.Image;
import com.twainarc.tourmate.utils.Constants;

import java.util.List;

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEvents(Event... events);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertImages(Image... images);

    @Query("SELECT * FROM " + Constants.TABLE_EVENTS)
    LiveData<List<Event>> getAllEvents();
}
