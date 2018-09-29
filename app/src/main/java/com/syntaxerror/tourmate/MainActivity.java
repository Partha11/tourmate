package com.syntaxerror.tourmate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.syntaxerror.tourmate.database.DatabaseManager;
import com.syntaxerror.tourmate.pojos.FullName;
import com.syntaxerror.tourmate.pojos.SingleUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener {

    private FrameLayout frameLayout;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SignInFragment signInFragment;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private DatabaseReference dbReference;

    private ProgressBar progressBar;

    private DatabaseManager dbManager;

    private List<SingleUser> userList;
    private String userEmail;
    private String userId;
    private boolean dataFound;

    public static final String USER_PREF_NAME = "CurrentUser";
    public static final String PREF_KEY = "loggedIn";
    public static final String PREF_USER_NAME = "username";
    public static final String PREF_USER_MAIL = "usermail";
    public static final String PREF_USER_ID = "userId";
    public static final String PREF_USER_PASSWORD = "userPassword";
    public static final String PREF_USER_FB_TOKEN = "fbtoken";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = mAuth.getCurrentUser();

                if (user == null) {

                    loadHomeFragment();
                }

                else {

                    finish();
                    switchActivity();
                }
            }
        };
    }

    private void initFields() {

        frameLayout = findViewById(R.id.fragmentLayout);
        progressBar = findViewById(R.id.progressBar);

        signInFragment = new SignInFragment();
        dbManager = new DatabaseManager(this);

        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference();

        userList = new ArrayList<>();
    }

    private void loadHomeFragment() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, signInFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onStart() {

        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {

        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void facebookLogin(final AccessToken accessToken) {

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());

        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    user = mAuth.getCurrentUser();
                    String userId = dbReference.push().getKey();

                    if (user == null)

                        loadHomeFragment();

                    else {

                        Log.e("Facebook", accessToken.toString());

                        String[] fullNameArray = TextUtils.split(user.getDisplayName(), " ");

                        FullName fullName = new FullName(fullNameArray[0], fullNameArray[1]);
                        SingleUser singleUser = new SingleUser(userId, user.getEmail(), user.getDisplayName(), fullName);

                        dbReference.child(userId)
                                .setValue(singleUser)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        updatePrefsPassword(null);
                                        switchActivity();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {

                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                                loadHomeFragment();
                            }
                        });
                    }
                }

                else {

                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void userLogIn(final String userEmailOrName, final String userPassword) {

        progressBar.setVisibility(View.VISIBLE);

        if (SingleUser.isEmail(userEmailOrName))

            userEmail = userEmailOrName;

        else {

            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    dataFound = false;

                    for (DataSnapshot data : dataSnapshot.getChildren()) {

                        SingleUser singleUser = data.getValue(SingleUser.class);

                        if (singleUser != null) {

                            if (TextUtils.equals(userEmailOrName, singleUser.getUserName())) {

                                userEmail = singleUser.getUserMail();
                                userId = singleUser.getUserId();
                                dataFound = true;
                                break;
                            }
                        }
                    }

                    if (userId != null)

                        Log.e("Logging In", userId);

                    else

                        Log.e("Logging In", "Id null");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                    Toast.makeText(MainActivity.this, "Please try again", Toast.LENGTH_SHORT).show();
                    Log.e("Login Error", databaseError.getMessage());
                }
            });
        }

        progressBar.setVisibility(View.GONE);

        if (dataFound) {

            mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progressBar.setVisibility(View.GONE);

                            if (task.isSuccessful()) {

                                updatePrefs(userId);
                                updatePrefsPassword(userPassword);
                                finish();
                                switchActivity();
                            }

                            else {

                                Toast.makeText(MainActivity.this, "Incorrect Email or Password", Toast.LENGTH_SHORT).show();
                                Log.e("task", task.toString());
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {

                @Override
                public void onFailure(@NonNull Exception e) {

                    Log.e("Error", e.getLocalizedMessage());
                }
            });
        }

        else {

            Toast.makeText(this, "Username or Email Incorrect", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        try {

            for (Fragment fragment : getSupportFragmentManager().getFragments()) {

                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

        catch (Exception e) {

            Log.d("Error", e.toString());
        }
    }

    @Override
    public void onUserRegistered(final SingleUser singleUser, final String userEmail, final String userPassword) {

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    userId = dbReference.push().getKey();

                    SingleUser userWithId = new SingleUser(userId, singleUser.getUserMail(),
                            singleUser.getUserName(), singleUser.getFullName());

                    dbReference.child(userId).setValue(userWithId)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        updatePrefs(userId);
                                        finish();
                                        switchActivity();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {

                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.e("Error", e.getLocalizedMessage());
                        }
                    });

                    updatePrefsPassword(userPassword);
                    Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                }

                else {

                    if (userPassword.length() < 6)

                        Toast.makeText(MainActivity.this, "Password Must Contain 6 Characters", Toast.LENGTH_SHORT).show();

                    else

                        Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("Error", e.getLocalizedMessage());
            }
        });
    }

    private void updatePrefs(String userId) {

        SharedPreferences prefs = this.getSharedPreferences(USER_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_USER_ID, userId);
        editor.apply();

        Log.e("Message", userId);
    }

    private void updatePrefsPassword(String userPassword) {

        SharedPreferences prefs = this.getSharedPreferences(USER_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_USER_PASSWORD, userPassword);
        editor.apply();

        Log.e("Message", userPassword);
    }

    private void updatePrefs(String userId, String fbToken) {

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString(PREF_USER_ID, userId);
        editor.putString(PREF_USER_FB_TOKEN, fbToken);
        editor.commit();

        Log.e("Message", userId);
    }

    private void switchActivity() {

        Intent intent = new Intent(MainActivity.this, UpdatedMainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
