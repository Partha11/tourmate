package com.syntaxerror.tourmate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.syntaxerror.tourmate.adapters.EventAdapter;
import com.syntaxerror.tourmate.database.DatabaseManager;
import com.syntaxerror.tourmate.pojos.ApiClient;
import com.syntaxerror.tourmate.pojos.ApiInterface;
import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.Expenses;
import com.syntaxerror.tourmate.pojos.FirebaseData;
import com.syntaxerror.tourmate.pojos.GetNearbyPlacesData;
import com.syntaxerror.tourmate.pojos.Nearby;
import com.syntaxerror.tourmate.pojos.NearbyPlaceData;
import com.syntaxerror.tourmate.pojos.NearbyPlaces;
import com.syntaxerror.tourmate.pojos.Result;
import com.syntaxerror.tourmate.pojos.SharedPrefData;
import com.syntaxerror.tourmate.pojos.SingleUser;

import java.util.ArrayList;
import java.util.List;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;
import io.github.kobakei.materialfabspeeddial.FabSpeedDial;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.syntaxerror.tourmate.MainMenuActivity.LOCATION_PERMISSION;

public class UpdatedMainMenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        RadioRealButtonGroup.OnClickedButtonListener, ViewEventsFragment.OnFragmentInteractionListener,
        ViewExpensesFragment.OnFragmentInteractionListener, AddEventFragment.OnFragmentInteractionListener,
        AddExpenseFragment.OnFragmentInteractionListener, NearbyPlacesFragment.OnFragmentInteractionListener,
        DisplayNearbyPlacesFragment.OnFragmentInteractionListener, UpdatedNearbyPlacesFragment.OnFragmentInteractionListener {

    private BottomNavigationView navigation;

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

    private List<Events> eventsList;
    private List<Expenses> expensesList;

    private PlaceDetectionClient placeDetectionClient;
    private LatLng latLng;
    private String latLngString;

    private List<Result> results;
    private List<NearbyPlaces> nearbyPlacesList;

    private ApiInterface apiInterface;

    public static final int REQUEST_LOCATION_PERMISSION = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updated_main_menu);

        initFields();
        fetchEvents();
        loadViewEventsFragment();

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkLocationPermission();
        }

        if (!isGooglePlayServicesAvailable()) {

            Log.d("onCreate", "Finishing test case since Google Play Services are not available");
            finish();
        }

        else {

            Log.d("onCreate","Google Play Services available.");
        }
    }

    private void initFields() {

        dbReference = FirebaseDatabase.getInstance().getReference();
        dbEventNode = dbReference.child("Events");
        dbExpenseNode = dbReference.child("Expenses");

        navigation = findViewById(R.id.navigation);
        radioButtonGroup = findViewById(R.id.radioButtonGroup);

        fragmentManager = getSupportFragmentManager();

        viewEventsFragment = new ViewEventsFragment();
        viewExpensesFragment = new ViewExpensesFragment();

        firebaseData = new FirebaseData(UpdatedMainMenuActivity.this);
        dbManager = new DatabaseManager(this);

        radioButtonGroup.setPosition(0);

        navigation.setOnNavigationItemSelectedListener(this);
        radioButtonGroup.setOnClickedButtonListener(this);

        if (checkLocationPermission()) {

            placeDetectionClient = com.google.android.gms.location.places.Places.getPlaceDetectionClient(this, null);
            fetchLocation();
        }
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

    private void fetchLocation() {

        SmartLocation.with(this).location().oneFix().start(new OnLocationUpdatedListener() {

            @Override
            public void onLocationUpdated(android.location.Location location) {

                latLngString = String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude());
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });
    }

    @Override
    public List<NearbyPlaces> findNearByPlaces(String placeName) {

        fetchLocation();

        Log.e("Error", "Location- " + latLngString);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<Nearby> call = apiInterface.searchPlaces(latLngString, placeName, "true", "distance",
                ApiClient.GOOGLE_PLACE_API_KEY);

        call.enqueue(new Callback<Nearby>() {

            @Override
            public void onResponse(Call<Nearby> call, Response<Nearby> response) {

                Nearby nearbyPlace = response.body();

                Log.e("Info", nearbyPlace.getStatus());

                if (nearbyPlace.getStatus() != null) {

                    if (nearbyPlace.getStatus().equals("OK")) {

                        results = nearbyPlace.getResults();
                        nearbyPlacesList = new ArrayList<>();

                        for (int i = 0; i < results.size(); i++) {

                            if (i >= 20)

                                break;

                            nearbyPlacesList.add(new NearbyPlaces(nearbyPlace.getResults().get(i).getName(),
                                    nearbyPlace.getResults().get(i).getVicinity()));
                        }
                    }

                    else {

                        Toast.makeText(getApplicationContext(), "No matches found near you", Toast.LENGTH_SHORT).show();
                    }

                }

                else if (response.code() != 200) {

                    Toast.makeText(getApplicationContext(), "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Nearby> call, Throwable t) {
                // Log error here since request failed
                Log.e("Error", t.getLocalizedMessage());
            }
        });

        return nearbyPlacesList;
    }

    private void nearbyPlacesClicked() {

        radioButtonGroup.setVisibility(View.GONE);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, new UpdatedNearbyPlacesFragment());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void fetchEvents() {

        eventsList = new ArrayList<>();

        dbEventNode.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                eventsList.clear();

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    Events singleEvent = i.getValue(Events.class);
                    dbManager.insertEventData(singleEvent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Error Occurred", databaseError.getMessage());
            }
        });
    }

    public List<Expenses> fetchExpenses(String id) {

        expensesList = new ArrayList<>();

        Log.e("Event ID", id);

        dbExpenseNode.child(id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot i : dataSnapshot.getChildren()) {

                    expensesList.add(i.getValue(Expenses.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Log.e("Failed", databaseError.getMessage());
            }
        });

        return expensesList;
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

                Toast.makeText(UpdatedMainMenuActivity.this, "Maps", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.navigation_memories:

                Toast.makeText(UpdatedMainMenuActivity.this, "Memories", Toast.LENGTH_SHORT).show();
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

    private void loadViewEventsFragment() {

        radioButtonGroup.setVisibility(View.VISIBLE);

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, viewEventsFragment);
        fragmentTransaction.commit();
    }

    private void loadViewExpenseFragment() {

        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.updatedFragmentLayout, viewExpensesFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public LatLng getLatLng() {

        final LatLng[] mLatLng = new LatLng[1];

        SmartLocation.with(this).location().oneFix().start(new OnLocationUpdatedListener() {

            @Override
            public void onLocationUpdated(Location location) {

                mLatLng[0] = new LatLng(location.getLatitude(), location.getLongitude());
            }
        });

        return mLatLng[0];
    }

    @Override
    public List<Expenses> fetchExpenseData(String id) {

        return fetchExpenses(id);
    }

    @Override
    public void addExpense(Expenses expense) {

        if (firebaseData.addExpense(expense))

            Log.e("Expense", "added");
    }

    @Override
    public void addEventDetails(Events event) {

        firebaseData.addEvent(event);
        fetchEvents();
    }

    @Override
    public List<Events> getAllEvents() {

        List<Events> mEventsList = dbManager.getAllEventsData();

        if (mEventsList != null) {

            fetchEvents();
            Log.e("Event Data", String.valueOf(mEventsList.size()));
        }

        else

            fetchEvents();

        return mEventsList;
    }

    public boolean checkLocationPermission(){
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
        } else {
            return true;
        }
    }
}
