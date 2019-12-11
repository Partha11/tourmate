package com.techmave.tourmate.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.techmave.tourmate.database.UserDao;
import com.techmave.tourmate.pojo.Event;

import java.util.List;

public class DashboardRepository {

    private UserDao dao;
    private LiveData<List<Event>> eventListLive;
    private FirebaseQueryLiveData liveData;

    public DashboardRepository(UserDao dao) {

        this.dao = dao;
        this.eventListLive = dao.getAllEvents();
    }

    public LiveData<List<Event>> getEventList() {

        return eventListLive;
    }

    public void insertEvents(List<Event> eventList) {

        Event[] events = new Event[eventList.size()];
        new InsertAsync(dao).execute(events);
    }

    static class InsertAsync extends AsyncTask<Event, Void, Void> {

        private UserDao dao;

        InsertAsync(UserDao dao) {

            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Event... events) {

            dao.insertEvents(events);
            return null;
        }
    }
}
