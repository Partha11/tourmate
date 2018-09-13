package com.syntaxerror.tourmate;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener, HomePageFragment.OnFragmentInteractionListener {

    private FrameLayout frameLayout;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private HomePageFragment homePageFragment;
    private SignInFragment signInFragment;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseUser user;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();

        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user == null) {

                    loadHomeFragment();
                }

                else {

                    switchActivity();
                }
            }
        };
    }

    private void initFields() {

        frameLayout = findViewById(R.id.fragmentLayout);

        homePageFragment = new HomePageFragment();
        signInFragment = new SignInFragment();

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    private void loadHomeFragment() {

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
                    switchActivity();
                }

                else {

                    Toast.makeText(MainActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void isLoggedIn(String userEmail, String userPassword) {

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {

                    switchActivity();
                    finish();
                }

                else {

                    Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                }
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
    public void onUserRegistered(String userEmail, String userPassword) {

        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful())

                    Toast.makeText(MainActivity.this, "Registered!!", Toast.LENGTH_SHORT).show();

                else

                    Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void switchActivity() {

        Intent intent = new Intent(MainActivity.this, SignedUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
