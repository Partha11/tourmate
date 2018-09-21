package com.syntaxerror.tourmate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.maps.model.LatLng;
import com.syntaxerror.tourmate.database.DatabaseManager;
import com.syntaxerror.tourmate.pojos.ApiClient;
import com.syntaxerror.tourmate.pojos.ApiInterface;
import com.syntaxerror.tourmate.pojos.Events;
import com.syntaxerror.tourmate.pojos.Nearby;
import com.syntaxerror.tourmate.pojos.NearbyPlaces;
import com.syntaxerror.tourmate.pojos.Result;

import java.util.ArrayList;
import java.util.List;

import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainMenuContentsFragment.OnFragmentInteractionListener,
        AddEventFragment.OnFragmentInteractionListener, ViewEventsFragment.OnFragmentInteractionListener,
        NearbyPlacesFragment.OnFragmentInteractionListener, DisplayNearbyPlacesFragment.OnFragmentInteractionListener {

    public static final int LOCATION_PERMISSION = 1;

    private FrameLayout fragmentLayout;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private DatabaseManager dbManager;

    private MainMenuContentsFragment mainMenu;

    private PlaceDetectionClient placeDetectionClient;
    private LatLng latLng;
    private String latLngString;

    private List<Result> results;
    private List<NearbyPlaces> nearbyPlacesList;

    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initFields();
        loadMainMenu();
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {

            drawer.closeDrawer(GravityCompat.START);
        }

        else {

            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        }

        else if (id == R.id.nav_gallery) {

        }

        else if (id == R.id.nav_slideshow) {

        }

        else if (id == R.id.nav_manage) {

        }

        else if (id == R.id.nav_share) {

        }

        else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initFields() {

        fragmentLayout = findViewById(R.id.mainMenuFragment);
        mainMenu = new MainMenuContentsFragment();
        dbManager = new DatabaseManager(MainMenuActivity.this);

        if (checkLocationPermission()) {

            placeDetectionClient = com.google.android.gms.location.places.Places.getPlaceDetectionClient(this, null);
            fetchLocation();
        }
    }

    private void loadMainMenu() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.mainMenuFragment, mainMenu);
        fragmentTransaction.commit();
    }

    public boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(MainMenuActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainMenuActivity.this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);

            return false;
        }

        else

            return true;
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

/*    private void fetchDistance(final Places.CustomA info) {

        Call<DistanceLine.ResultDistanceMatrix> call = apiInterface.getDistance(ApiClient.GOOGLE_PLACE_API_KEY,
                latLngString, info.geometry.locationA.lat + "," + info.geometry.locationA.lng);

        call.enqueue(new Callback<DistanceLine.ResultDistanceMatrix>() {

            @Override
            public void onResponse(Call<DistanceLine.ResultDistanceMatrix> call, Response<DistanceLine.ResultDistanceMatrix> response) {

                DistanceLine.ResultDistanceMatrix resultDistance = response.body();

                if ("OK".equalsIgnoreCase(resultDistance.status)) {

                    DistanceLine.ResultDistanceMatrix.InfoDistanceMatrix infoDistanceMatrix =
                            (DistanceLine.ResultDistanceMatrix.InfoDistanceMatrix) resultDistance.rows.get(0);

                    DistanceLine.ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement distanceElement =
                            (DistanceLine.ResultDistanceMatrix.InfoDistanceMatrix.DistanceElement) infoDistanceMatrix.elements.get(0);

                    if ("OK".equalsIgnoreCase(distanceElement.status)) {

                        DistanceLine.ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDuration = distanceElement.duration;
                        DistanceLine.ResultDistanceMatrix.InfoDistanceMatrix.ValueItem itemDistance = distanceElement.distance;

                        String totalDistance = String.valueOf(itemDistance.text);
                        String totalDuration = String.valueOf(itemDuration.text);

                        nearbyPlacesList.add(new NearbyPlaces(info.name, info.vicinity, totalDistance));
                    }

                }

            }

            @Override
            public void onFailure(Call<DistanceLine.ResultDistanceMatrix> call, Throwable t) {

                Log.e("Error", t.getLocalizedMessage());
            }
        });
    }*/

    @Override
    public void onFragmentInteraction(Uri uri) {

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

    @Override
    public List<Events> getAllEvents() {

        List<Events> eventsList = dbManager.getAllEventsData();
        return eventsList;
    }

    @Override
    public void addEventDetails(Events event) {

        if (dbManager.insertEventData(event))

            Toast.makeText(this, "Successful", Toast.LENGTH_SHORT).show();

        else

            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
    }
}
