package com.syntaxerror.tourmate;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.syntaxerror.tourmate.database.DatabaseManager;
import com.syntaxerror.tourmate.permissions.RequestPermissionHandler;
import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.Expenses;
import com.syntaxerror.tourmate.pojos.FirebaseData;
import com.syntaxerror.tourmate.pojos.SingleUser;
import com.syntaxerror.tourmate.pojos.StaticData;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class UpdatedMainMenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        RadioRealButtonGroup.OnClickedButtonListener, ViewEventsFragment.OnFragmentInteractionListener,
        ViewExpensesFragment.OnFragmentInteractionListener, AddEventFragment.OnFragmentInteractionListener,
        AddExpenseFragment.OnFragmentInteractionListener, DisplayNearbyPlacesFragment.OnFragmentInteractionListener,
        UpdatedNearbyPlacesFragment.OnFragmentInteractionListener, UserProfileFragment.OnFragmentInteractionListener,
        WeatherFragment.OnFragmentInteractionListener, MemoriesFragment.OnFragmentInteractionListener {

    private AHBottomNavigation navigation;

    private RadioRealButtonGroup radioButtonGroup;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ViewEventsFragment viewEventsFragment;
    private ViewExpensesFragment viewExpensesFragment;

    private static FirebaseData firebaseData;
    private static DatabaseManager dbManager;

    private static DatabaseReference dbReference;
    private static DatabaseReference dbEventNode;
    private static DatabaseReference dbExpenseNode;

    private static LinearLayout linearLayout;

    private static List<Events> eventsList;
    private static List<Expenses> expensesList;

    public static String userId;

    public static final int REQUEST_LOCATION_PERMISSION = 69;
    public static final int REQUEST_STORAGE_PERMISSION = 70;
    public static final int RC_READ_STORAGE = 72;

    private RequestPermissionHandler mRequestPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_main_menu);

        eventsList = new ArrayList<>();
        expensesList = new ArrayList<>();

        eventsList.clear();

        if (userId != null)

            userId = null;

        initFields();
        checkPermissions();
        loadViewEventsFragment();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();
        }

        if (!isGooglePlayServicesAvailable() && checkLocationPermission()) {

            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }

        else {

            Log.d("onCreate","Google Play Services available.");
        }
    }

    private void checkPermissions() {

        mRequestPermission = new RequestPermissionHandler();

        mRequestPermission.requestPermission(this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE },
                REQUEST_STORAGE_PERMISSION, new RequestPermissionHandler.RequestPermissionListener() {

            @Override
            public void onSuccess() {

                Log.d("UpdatedMainMenuActivity", "Permission Granted");
            }

            @Override
            public void onFailed() {

                Log.d("UpdatedMainMenuActivity", "Permission Denied");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mRequestPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initFields() {

        readPrefs();

        dbReference = FirebaseDatabase.getInstance().getReference().child(userId);
        dbEventNode = dbReference.child("Events");
        dbExpenseNode = dbReference.child("Expenses");
        dbEventNode.keepSynced(true);

        navigation = findViewById(R.id.navigation);
        radioButtonGroup = findViewById(R.id.radioButtonGroup);
        linearLayout = findViewById(R.id.radioLayout);

        fragmentManager = getSupportFragmentManager();

        viewEventsFragment = new ViewEventsFragment();
        viewExpensesFragment = new ViewExpensesFragment();

        firebaseData = new FirebaseData(UpdatedMainMenuActivity.this);
        dbManager = new DatabaseManager(this);

        fetchUserData();

        radioButtonGroup.setPosition(0);

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
        navigation.setCurrentItem(0);

        // Set listeners
        navigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {

            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {

                switch (position) {

                    case 0:

                        loadViewEventsFragment();
                        return true;

                    case 1:

                        nearbyPlacesClicked();
                        return true;

                    case 2:

                        userProfileClicked();
                        return true;

                    case 3:

                        showMemoriesClicked();
                        return true;

                    case 4:

                        showWeatherClicked();
                        return true;
                }

                return false;
            }
        });

        navigation.setOnNavigationPositionListener(new AHBottomNavigation.OnNavigationPositionListener() {

            @Override public void onPositionChange(int y) {

            }
        });


        //navigation.setOnNavigationItemSelectedListener(this);
        radioButtonGroup.setOnClickedButtonListener(this);
    }

    private void readPrefs() {

        SharedPreferences prefs = this.getSharedPreferences(MainActivity.USER_PREF_NAME, Context.MODE_PRIVATE);
        userId = prefs.getString(MainActivity.PREF_USER_ID, "Cant read data");

        Log.e("Message", userId);
    }

    private boolean isGooglePlayServicesAvailable() {

        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();

        int result = googleAPI.isGooglePlayServicesAvailable(this);

        if(result != ConnectionResult.SUCCESS) {

            if(googleAPI.isUserResolvableError(result)) {

                googleAPI.getErrorDialog(this, result, 0).show();
            }

            return false;
        }

        return true;
    }

    private void nearbyPlacesClicked() {

        linearLayout.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, new UpdatedNearbyPlacesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void userProfileClicked() {

        linearLayout.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, new UserProfileFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void fetchUserData() {

        dbReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                SingleUser singleUser = dataSnapshot.getValue(SingleUser.class);

                Log.d("UpdatedMainMenuActivity", singleUser.getProfileImage());

                if (dbManager.insertSingleUser(singleUser))

                    Log.d("Database", "User inserted");

                else

                    Log.d("Database", "User insertion failed");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.d("Userdata", databaseError.getMessage());
            }
        });
    }

    public void fetchEvents() {

        dbEventNode.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int count = 0;

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    Events singleEvent = i.getValue(Events.class);
                    dbManager.insertEventData(singleEvent);

                    ++count;
                }

                StaticData.setNumberOfEvents(count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Error Occurred", databaseError.getMessage());
            }
        });
    }

    public void fetchExpenses(String id) {

        dbExpenseNode.child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    expensesList.add(i.getValue(Expenses.class));
                }

                //StaticData.setTotalExpenseAmount(expensesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Failed", databaseError.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.navigation_home:

                loadViewEventsFragment();
                return true;

            case R.id.navigation_nearby:

                nearbyPlacesClicked();
                return true;

            case R.id.navigation_maps:

                showWeatherClicked();
                return true;

            case R.id.navigation_memories:

                userProfileClicked();
                return true;
        }

        return false;
    }

    @Override
    public void onClickedButton(RadioRealButton button, int position) {

        if (position == 0)

            loadViewEventsFragment();

        else if (position == 1)

            loadViewExpenseFragment();
    }

    @Override
    protected void onPause() {

        super.onPause();
        dbManager.deleteSingleUser();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        dbManager.deleteSingleUser();
    }

    private void loadViewEventsFragment() {

        radioButtonGroup.setPosition(0);
        linearLayout.setVisibility(View.VISIBLE);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, viewEventsFragment);
        fragmentTransaction.commit();
    }

    private void loadViewExpenseFragment() {

        radioButtonGroup.setPosition(1);
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, viewExpensesFragment);
        fragmentTransaction.commit();
    }

    private void showWeatherClicked() {

        radioButtonGroup.setPosition(0);
        linearLayout.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, new WeatherFragment());
        fragmentTransaction.commit();
    }

    private void showMemoriesClicked() {

        radioButtonGroup.setPosition(0);
        linearLayout.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, new MemoriesFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public SingleUser getUserInfo() {

        fetchUserData();
        return dbManager.getSingleUserData();
    }

    @Override
    public LatLng getLatLng() {

        return null;
    }

    @Override
    public void addExpense(Expenses expense) {

        if (firebaseData.addExpense(expense))

            Log.e("Expense", "added");
    }

    @Override
    public void addEventDetails(Events event) {

        firebaseData.addEvent(event);
    }

    @Override
    public List<Events> getAllEvents() {

        eventsList = dbManager.getAllEventsData();
        StaticData.setNumberOfEvents(eventsList.size());
        return eventsList;
    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this,

                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION_PERMISSION);
            }

            return false;
        }

        else {

            return true;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_logout:

                signOutUser();
                break;

            case R.id.action_settings:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOutUser() {

        if (eventsList != null)

            eventsList.clear();

        if (expensesList != null)

            expensesList.clear();

        if (dbManager.deleteAllData())

            Log.e("Deletion", "successful");

        else

            Log.e("Deletion", "failed");

        dbManager.deleteSingleUser();

        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(UpdatedMainMenuActivity.this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
