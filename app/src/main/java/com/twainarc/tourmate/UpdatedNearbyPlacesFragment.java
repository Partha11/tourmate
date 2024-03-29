package com.twainarc.tourmate;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twainarc.tourmate.model.ApiClient;
import com.twainarc.tourmate.model.ApiInterface;
import com.twainarc.tourmate.model.GetNearbyPlacesData;
import com.twainarc.tourmate.model.Nearby;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdatedNearbyPlacesFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private Context mContext;

    private MapView mapView;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 5000;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private Marker mCurrLocationMarker;
    private LocationRequest mLocationRequest;

    public UpdatedNearbyPlacesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {

        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();

        mMap.clear();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_updated_nearby_places, container, false);

        mapView = view.findViewById(R.id.nearbyMap);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }

        else {

            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    private void build_retrofit_and_get_response(String type) {

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);

        //Call<Nearby> call = service.getNearbyPlaces(type, latitude + "," + longitude, PROXIMITY_RADIUS);
        Call<Nearby> call = service.searchPlaces(latitude + "," + longitude, type,
                "true", "10006", ApiClient.GOOGLE_PLACE_API_KEY);
        call.enqueue(new Callback<Nearby>() {

            @Override
            public void onResponse(Call<Nearby> call, Response<Nearby> response) {

                try {

                    mMap.clear();
                    Nearby nearbyPlace = response.body();
                    Log.e("Info", nearbyPlace.getStatus());

                    if (nearbyPlace.getStatus() != null) {

                        if (nearbyPlace.getStatus().equals("OK")) {

                            // This loop will go through all the results and add marker on each location.
                            for (int i = 0; i < response.body().getResults().size(); i++) {

                                Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                                Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                                String placeName = response.body().getResults().get(i).getName();
                                String vicinity = response.body().getResults().get(i).getVicinity();

                                MarkerOptions markerOptions = new MarkerOptions();
                                LatLng latLng = new LatLng(lat, lng);

                                markerOptions.position(latLng);
                                markerOptions.title(placeName + " : " + vicinity);
                                Marker m = mMap.addMarker(markerOptions);
                                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(20));
                            }
                        }

                        else {

                            Toast.makeText(mContext, "No matches found near you", Toast.LENGTH_SHORT).show();
                        }
                    }

                    else if (response.code() != 200) {

                        Toast.makeText(mContext, "Error " + response.code() + " found.", Toast.LENGTH_SHORT).show();
                    }
                }

                catch (Exception e) {

                    Log.d("onResponse", e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Nearby> call, Throwable t) {

                Log.d("onFailure", t.toString());
            }
        });
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");

        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        mCurrLocationMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        Log.d("onLocationChanged", "Exit");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(mContext, "permission denied", Toast.LENGTH_LONG).show();
                }

                return;
            }
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext = context;
    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + ApiClient.GOOGLE_PLACE_API_KEY);
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

/*    @Override
    public void onMenuItemClick(FloatingActionButton miniFab, @Nullable TextView label, int itemId) {


        if (label.getText().toString().trim().equals("Cafe")) {

            build_retrofit_and_get_response("cafe");
        }

        else if (label.getText().toString().trim().equals("Restaurant")) {

            build_retrofit_and_get_response("restaurant");
        }

        else if (label.getText().toString().trim().equals("ATM")) {

            build_retrofit_and_get_response("atm");
        }

        else if (label.getText().toString().trim().equals("Bank")) {

            build_retrofit_and_get_response("bank");
        }

        else if (label.getText().toString().trim().equals("Mosque")) {

            build_retrofit_and_get_response("mosque");
        }

        else if (label.getText().toString().trim().equals("Hospital")) {

            build_retrofit_and_get_response("hospital");
        }
    }*/

    private void updateMap(String placeName) {

        Log.d("onClick", "Button is Clicked");
        mMap.clear();
        String url = getUrl(latitude, longitude, placeName);
        Object[] DataTransfer = new Object[2];
        DataTransfer[0] = mMap;
        DataTransfer[1] = url;
        Log.d("onClick", url);
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        getNearbyPlacesData.execute(DataTransfer);
        Toast.makeText(mContext,"Nearby", Toast.LENGTH_LONG).show();
    }
}
