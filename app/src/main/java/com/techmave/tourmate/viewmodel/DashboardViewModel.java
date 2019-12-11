package com.techmave.tourmate.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techmave.tourmate.database.UserDatabase;
import com.techmave.tourmate.pojo.Event;
import com.techmave.tourmate.repository.DashboardRepository;
import com.techmave.tourmate.repository.FirebaseQueryLiveData;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private FirebaseQueryLiveData liveData;
    private DashboardRepository repository;

    public DashboardViewModel(@NonNull Application application) {

        super(application);
        repository = new DashboardRepository(UserDatabase.getInstance(application).getUserDao());
    }

    public void setLiveData(String uid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("events");
        liveData = new FirebaseQueryLiveData(reference);
    }

    public LiveData<DataSnapshot> getAllEvents() {

        return liveData;
    }

    public void insertEvents(List<Event> eventList) {

        repository.insertEvents(eventList);
    }

    public LiveData<DataSnapshot> getUserToken(String uid) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                .child("users").child(uid).child("token");
        return new FirebaseQueryLiveData(reference);
    }
}
