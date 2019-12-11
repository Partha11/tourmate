package com.techmave.tourmate.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techmave.tourmate.pojo.Response;
import com.techmave.tourmate.utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class LoginRepository {

    public MutableLiveData<Response> signInUser(String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        MutableLiveData<Response> isSuccess = new MutableLiveData<>();
        Response response = new Response();

        response.setSuccessful(false);
        response.setUserId("");
        response.setFailReason("Connection error");

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();

                        if (user != null) {

                            response.setSuccessful(true);
                            response.setUserId(user.getUid());
                            response.setFailReason("");

                            isSuccess.setValue(response);

                        } else {

                            response.setUserId("");
                            response.setSuccessful(false);
                            response.setFailReason("Something went wrong");
                            isSuccess.setValue(response);
                        }
                    }

                }).addOnFailureListener(e -> {

            response.setUserId("");
            response.setSuccessful(false);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                response.setFailReason("Invalid email or password");

            } else if (e instanceof FirebaseAuthInvalidUserException) {

                if (((FirebaseAuthInvalidUserException) e).getErrorCode().equals(Constants.USER_NOT_FOUND)) {

                    response.setFailReason("No matching account found");

                } else if (((FirebaseAuthInvalidUserException) e).getErrorCode().equals(Constants.USER_DISABLED)) {

                    response.setFailReason("User account has been disabled");

                } else {

                    response.setFailReason(e.getLocalizedMessage());                            }

            } else {

                response.setFailReason(e.getLocalizedMessage());
            }

            isSuccess.setValue(response);
        });

        return isSuccess;
    }

    public MutableLiveData<Response> createUser(String email, String password) {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        MutableLiveData<Response> isSuccess = new MutableLiveData<>();
        Response response = new Response();

        response.setSuccessful(false);
        response.setUserId("");
        response.setFailReason("Connection error");

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        FirebaseUser user = auth.getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                        response.setSuccessful(true);
                        response.setFailReason("");

                        if (user != null) {

                            Map<String, String> data = new HashMap<>();
                            data.put(Constants.USER_EMAIL, email);

                            response.setUserId(user.getUid());
                            reference.child(user.getUid()).setValue(data);
                        }

                    } else {

                        response.setSuccessful(false);
                        response.setUserId("");
                        response.setFailReason("Something went wrong");
                    }

                    isSuccess.setValue(response);

                }).addOnFailureListener(e -> {

            response.setUserId("");
            response.setSuccessful(false);

            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                response.setFailReason("Invalid email or password");

            } else if (e instanceof FirebaseAuthInvalidUserException) {

                if (((FirebaseAuthInvalidUserException) e).getErrorCode().equals(Constants.USER_NOT_FOUND)) {

                    response.setFailReason("No matching account found");

                } else if (((FirebaseAuthInvalidUserException) e).getErrorCode().equals(Constants.USER_DISABLED)) {

                    response.setFailReason("User account has been disabled");

                } else {

                    response.setFailReason(e.getLocalizedMessage());
                }

            } else {

                response.setFailReason(e.getLocalizedMessage());
            }

            isSuccess.setValue(response);
        });

        return isSuccess;
    }
}
