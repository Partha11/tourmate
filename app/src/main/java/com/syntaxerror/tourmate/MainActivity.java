package com.syntaxerror.tourmate;

import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements RegisterFragment.OnFragmentInteractionListener,
        SignInFragment.OnFragmentInteractionListener, HomePageFragment.OnFragmentInteractionListener {

    private FrameLayout frameLayout;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private HomePageFragment homePageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
        loadHomeFragment();
    }

    private void initFields() {

        frameLayout = findViewById(R.id.fragmentLayout);

        homePageFragment = new HomePageFragment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    private void loadHomeFragment() {

        fragmentTransaction.replace(R.id.fragmentLayout, homePageFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
