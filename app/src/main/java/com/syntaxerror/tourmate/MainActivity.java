package com.syntaxerror.tourmate;

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
    private HashMap<Integer, SingleUser> userListMap;

    private static int id;
    private boolean isNewUser;
    private String userName;
    private static boolean isSuccessful;

    private static final String PREF_KEY = "loggedIn";
    private static final String PREF_USER_NAME = "username";
    private static final String PREF_USER_MAIL = "usermail";
    private static final String PREF_USER_ID = "userId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = mAuth.getCurrentUser();

                if (user != null) {

                    Log.e("Prefs Data", String.valueOf(isNewUser));
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
        userListMap = new HashMap<>();

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    userList.add(i.getValue(SingleUser.class));
                    userListMap.put(userListMap.size() , i.getValue(SingleUser.class));
                }

                id = userListMap.size();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "Failed To Fetch", Toast.LENGTH_SHORT).show();
            }
        });

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        isNewUser = prefs.getBoolean(PREF_KEY, false);
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
    public void isLoggedIn(AccessToken accessToken) {

        AuthCredential authCredential = FacebookAuthProvider.getCredential(accessToken.getToken());

        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    user = mAuth.getCurrentUser();

                    if (user != null)

                        updatePrefs(user);

                    switchActivity();
                }

                else {

                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void userLogIn(String userEmailOrName, String userPassword) {

        if (TextUtils.isEmpty(userEmailOrName) || TextUtils.isEmpty(userPassword)) {

            Toast.makeText(this, "Username or Password Empty!", Toast.LENGTH_SHORT).show();

            loadHomeFragment();
            finish();
        }

        String userEmail;

        if (SingleUser.isEmail(userEmailOrName))

            userEmail = userEmailOrName;

        else {

            userEmail = dbManager.getUserMail(userEmailOrName);
            userName = userEmailOrName;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {

                    finish();
                    switchActivity();
                }

                else {

                    Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("Error", e.getLocalizedMessage());
            }
        });
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
    public void onUserRegistered(final String userEmail, final String userPassword) {

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(MainActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();

                    user = mAuth.getCurrentUser();

                    if (user != null)

                        updatePrefs(user);
                }

                else {

                    if (userPassword.length() < 6)

                        Toast.makeText(MainActivity.this, "Password Must Contain 6 Characters", Toast.LENGTH_SHORT).show();

                    else

                        Toast.makeText(MainActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();

                    if (dbManager.deleteData(userEmail))

                        Log.e("Data Deletion", "successful");

                    else

                        Log.e("Data Deletion", "failed");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("Error", e.getLocalizedMessage());
            }
        });
    }

    @Override
    public boolean onUserRegistered(SingleUser singleUser) {

        boolean isDataUnique;

        if (userListMap.containsValue(singleUser))

            isDataUnique = false;

        else

            isDataUnique = true;

        if (!isDataUnique) {

            Toast.makeText(this, "Email or Username Exists", Toast.LENGTH_SHORT).show();

            loadHomeFragment();
            return false;
        }

        dbReference.child(String.valueOf(++id)).setValue(singleUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())

                    isSuccessful = true;

                else

                    isSuccessful = false;
            }
        }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                Log.e("Error", e.getLocalizedMessage());
            }
        });

        return isSuccessful;
    }

    private void updatePrefs(boolean isLoggedIn) {

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(PREF_KEY, isLoggedIn);
        editor.commit();

        Log.e("Prefs Written: ", String.valueOf(isLoggedIn));
    }

    private void updatePrefs(FirebaseUser user) {

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (user.getDisplayName() != null)

            editor.putString(PREF_USER_NAME, user.getDisplayName());

        else

            editor.putString(PREF_USER_NAME, userName);

        editor.putString(PREF_USER_ID, user.getUid());
        editor.putString(PREF_USER_MAIL, user.getEmail());
        editor.commit();

        Log.e("Message", "Write To Prefs Complete " + user.getDisplayName() + " " + user.getEmail());
    }

    private void switchActivity() {

        Intent intent = new Intent(MainActivity.this, UpdatedMainMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
