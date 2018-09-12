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
        SignInFragment.OnFragmentInteractionListener, View.OnClickListener {

    private RelativeLayout frameLayout;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private SignInFragment signInFragment;
    private RegisterFragment registerFragment;

    private Button registerButton;
    private Button signInButton;

    private TextView homeLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFields();
    }

    private void initFields() {

        registerButton = findViewById(R.id.register);
        signInButton = findViewById(R.id.signIn);

        homeLogo = findViewById(R.id.homeLogo);

        frameLayout = findViewById(R.id.fragmentLayout);

        fragmentManager = getSupportFragmentManager();

        registerButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    private void setFieldsInvisible() {

        homeLogo.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
    }

    private void setFieldsVisible() {

        homeLogo.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.VISIBLE);
        signInButton.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.GONE);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.register:

                registerButtonClicked();
                break;

            case R.id.signIn:

                signInButtonClicked();
                break;
        }
    }

    private void registerButtonClicked() {

        registerFragment = new RegisterFragment();

        setFieldsInvisible();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, registerFragment);
        fragmentTransaction.commit();

        setFieldsInvisible();
    }

    private void signInButtonClicked() {

        signInFragment = new SignInFragment();

        setFieldsInvisible();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragmentLayout, signInFragment);
        fragmentTransaction.commit();

        setFieldsVisible();
    }
}
