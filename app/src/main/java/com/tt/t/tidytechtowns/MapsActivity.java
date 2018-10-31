package com.tt.t.tidytechtowns;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.Map;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private boolean showing = false;
    private Marker bin;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng currentLocation;
    private Button gpsbutton;


    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button binShow = (Button) findViewById(R.id.binBtn);
        binShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change set visibility in stead to avoid global variables
                if (!showing) {
                    LatLng bin1 = new LatLng(53, -6);
                    bin = mMap.addMarker(new MarkerOptions().position(bin1).title("This is a BIN!").draggable(true));
                    Button button = (Button) findViewById(R.id.binBtn);
                    button.setText("Hide bins");
                    showing = true;
                } else {
                    bin.remove();
                    Button button = (Button) findViewById(R.id.binBtn);
                    button.setText("Show bins");
                    showing = false;
                }
            }
        });


        startLocationUpdates();

        gpsbutton = (Button) findViewById(R.id.GPS);
        gpsbutton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {

                 if (currentLocation==null){
                     Toast.makeText(getApplicationContext(), "Null", Toast.LENGTH_SHORT).show();

                 }
                 else {
                     String msg1 = "LL" + Double.toString(currentLocation.latitude);
                     Toast.makeText(getApplicationContext(), msg1, Toast.LENGTH_SHORT).show();
                 }

             }
         });
    }


    // Location request adapted from https://github.com/codepath/android_guides/wiki/Retrieving-Location-with-LocationServices-API
    protected void startLocationUpdates() {

        Toast.makeText(getApplicationContext(), "updates", Toast.LENGTH_SHORT).show();


        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback(){

            @Override
            public void onLocationResult (LocationResult locationResult){


            onLocationChanged(locationResult.getLastLocation());
            String msg = "string";
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            }
        },
        Looper.myLooper());
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dublin = new LatLng(53.3498, -6.2603);
        mMap.addMarker(new MarkerOptions().position(dublin).title("This is Dublin!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dublin, 10));

    }

    public void showBin() {
        LatLng bin1 = new LatLng(53, 6);
        mMap.addMarker(new MarkerOptions().position(bin1).title("This is a BIN!"));
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

}

























/*
    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            switch (requestCode) {
                case 10:
                    configureButton();
                    break;
                default:
                    break;
            }
        }

        public void addNewMarker(LatLng location) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(location).title("Yep, you!").icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            mMarkerArray.add(marker);
        }

        void configureButton() {
            // first check for permissions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);

                return;
            }
            // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
            gpsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg1 = "LL"+Double.toString(currentLocation.latitude);
                    Toast.makeText(getApplicationContext(), msg1 , Toast.LENGTH_SHORT).show();

                }
            });
        }



       /* public void onLocationChanged(Location location) {
            // New location has now been determined
            String msg = "Updated Location: " +
                    Double.toString(location.getLatitude()) + "," +
                    Double.toString(location.getLongitude());
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            // You can now create a LatLng Object for use with maps
            currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
        }

    }*/
