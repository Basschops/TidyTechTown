package com.tt.t.tidytechtowns;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
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
    private long FASTEST_INTERVAL = 5000; /* 5 sec */


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

        // Set up function with button to display bins on map
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

                 final boolean[] proximity = {true};
                 if (currentLocation==null){
                     Toast.makeText(getApplicationContext(), "Acquiring locaiton ... Please try again", Toast.LENGTH_SHORT).show();

                 }
                 else {
                     final LatLng you = new LatLng(currentLocation.latitude, currentLocation.longitude);
                     markerDialogFragment box = new markerDialogFragment();
                     // Check if location is by existing marker
                     for (Marker marker : mMarkerArray) {

                         Location loc1 = new Location("");
                         loc1.setLatitude(marker.getPosition().latitude);
                         loc1.setLongitude(marker.getPosition().longitude);

                         Location loc2 = new Location("");
                         loc2.setLatitude(you.latitude);
                         loc2.setLongitude(you.longitude);

                         if (loc1.distanceTo(loc2) < 20) {
                             Toast toast = Toast.makeText(getApplicationContext(), "You are too near.", Toast.LENGTH_SHORT);
                             toast.show();
                             // Code to check if user wants to place another here.
//                            box.show();
                            openDialog();


                            break;


                         }
                     }
                     if (proximity[0]) {
                         String msg1 = currentLocation.longitude + " " + currentLocation.latitude;
                         Toast.makeText(getApplicationContext(), msg1, Toast.LENGTH_SHORT).show();
                         Marker marker = mMap.addMarker(new MarkerOptions().position(you).title("Yep, you!").icon(BitmapDescriptorFactory
                                 .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                         mMarkerArray.add(marker);
                     }
                 }

             }
         });
    }

    public void openDialog(){
        proximityDialog box = new proximityDialog();
        box.show(getSupportFragmentManager(), "Proximity check");
    }


    // Location request adapted from https://github.com/codepath/android_guides/wiki/Retrieving-Location-with-LocationServices-API
    protected void startLocationUpdates() {
        // Test message to see if location is updating
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
            // Change stored location when location changes
            @Override
            public void onLocationResult (LocationResult locationResult){
            onLocationChanged(locationResult.getLastLocation());
            }
        },
        Looper.myLooper());
    }

    // Adapted from https://developer.android.com/guide/topics/ui/dialogs

    public static class markerDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstantState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Close to other bin so you want to proceed")
                    .

                            setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //proximity[0] = true;
                                    //Toast.makeText(this, "You pressed OK", Toast.LENGTH_SHORT).show();
                                    //addNewMarker(you);
                                }
                            })
                    .

                            setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //proximity[0] = false;
                                }
                            });

            return builder.create();

        }

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
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

}
