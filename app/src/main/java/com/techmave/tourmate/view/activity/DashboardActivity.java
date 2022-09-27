package com.techmave.tourmate.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.techmave.tourmate.AddEventFragment;
import com.techmave.tourmate.AddExpenseFragment;
import com.techmave.tourmate.DisplayNearbyPlacesFragment;
import com.techmave.tourmate.R;
import com.techmave.tourmate.UserProfileFragment;
import com.techmave.tourmate.ViewEventsFragment;
import com.techmave.tourmate.pojo.Event;
import com.techmave.tourmate.pojo.Expenses;
import com.techmave.tourmate.pojo.SingleUser;
import com.techmave.tourmate.utils.Constants;
import com.techmave.tourmate.utils.SharedPrefs;
import com.techmave.tourmate.view.fragment.EventFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        AddEventFragment.OnFragmentInteractionListener, AddExpenseFragment.OnFragmentInteractionListener,
        DisplayNearbyPlacesFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener {

    @BindView(R.id.bottom_navigation)
    AHBottomNavigation navigation;

    private SharedPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && FirebaseApp.getApps(this).isEmpty()) {

            FirebaseApp.initializeApp(this);
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        initialize();
        checkPermissions();

/*        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();
        }

        if (!isGooglePlayServicesAvailable() && checkLocationPermission()) {

            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();

        } else {

            Log.d("onCreate", "Google Play Services available.");
        }*/
    }

    private void checkPermissions() {

        //Added Later
    }

    private void initialize() {

        prefs = new SharedPrefs(this);

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home", R.drawable.ic_home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Nearby", R.drawable.ic_maps);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Profile", R.drawable.ic_user_tp);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("Memories", R.drawable.ic_memories);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem("Weather", R.drawable.ic_weather);

        navigation.addItem(item1);
        navigation.addItem(item2);
        navigation.addItem(item3);
        navigation.addItem(item4);
        navigation.addItem(item5);

        navigation.setDefaultBackgroundColor(Color.parseColor("#bebdbd"));
        navigation.setAccentColor(Color.parseColor("#006A5D"));
        navigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);

        replaceFragment(Constants.HOME_FRAGMENT);
    }

    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();

        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if (result != ConnectionResult.SUCCESS) {

            if (googleAPI.isUserResolvableError(result)) {

                googleAPI.getErrorDialog(this, result, 0).show();
            }

            return false;
        }

        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int fragment = 0;

        switch (item.getItemId()) {

            case R.id.navigation_home:
            case R.id.navigation_nearby:
            case R.id.navigation_maps:
            case R.id.navigation_memories:
                fragment = Constants.HOME_FRAGMENT;
                break;
        }

        replaceFragment(fragment);
        return false;
    }

    private void replaceFragment(int fragment) {

        if (fragment == Constants.HOME_FRAGMENT) {

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            transaction.replace(R.id.frame_layout, new EventFragment());
            transaction.commit();
        }
    }

/*    @Override
    public void onClickedButton(RadioRealButton button, int position) {

        if (position == 0)

            loadViewEventsFragment();

        else if (position == 1)

            loadViewExpenseFragment();
    }*/

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public SingleUser getUserInfo() {

        return null;
    }

    @Override
    public void addExpense(Expenses expense) {

    }

    @Override
    public void addEventDetails(Event event) {

    }

    @Override
    public List<Event> getAllEvents() {

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_logout) {

            signOutUser();
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOutUser() {

        prefs.setInstalled(false);
        FirebaseAuth.getInstance().signOut();

        Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);

        startActivity(intent);
        finishAffinity();
    }
}
