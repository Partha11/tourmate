package com.techmave.tourmate.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.techmave.tourmate.pojo.Response;
import com.techmave.tourmate.repository.LoginRepository;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;

    public LoginViewModel(@NonNull Application application) {

        super(application);
        repository = new LoginRepository();
    }

    public LiveData<Response> signInUser(String email, String password) {

        return repository.signInUser(email, password);
    }

    public LiveData<Response> registerUser(String email, String password) {

        return repository.createUser(email, password);
    }
}
