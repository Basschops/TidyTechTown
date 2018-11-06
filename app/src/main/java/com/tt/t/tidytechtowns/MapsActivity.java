package com.tt.t.tidytechtowns;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
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
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, proximityDialog.mapDialogListener, reportDialog.reportDialogListener
, reportNearbyDialog.mapDialogListener {

    private GoogleMap mMap;
    private boolean showing = false;
    //private Marker bin;
    private static LatLng currentLocation;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 5000; /* 5 sec */
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private ArrayList<Marker> mLitterArray = new ArrayList<Marker>();
    private ArrayList<Marker> mDumpingArray = new ArrayList<Marker>();
    private ArrayList<Marker> mSpillArray = new ArrayList<Marker>();
    private ArrayList<Marker> mGraffitiArray = new ArrayList<Marker>();


    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private boolean gpsAcquired = false;


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
        /*Button binShow = (Button) findViewById(R.id.binBtn);
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
        }); */


        startLocationUpdates();
    }

    public void showBins(View view){
        Toast.makeText(getApplicationContext(), "Show/hide"+showing, Toast.LENGTH_SHORT).show();
        Button button = (Button) findViewById(R.id.binBtn);
        if(showing) {
            for (Marker marker : mMarkerArray) {
                marker.setVisible(false);
                button.setText("Show bins");
                showing = false;
            }
        }
        else{
            for(Marker marker: mMarkerArray){
                marker.setVisible(true);
                button.setText("Hide bins");
                showing = true;
            }
        }
    }

    // Report issue button function
    public void reportIssue(View view) {
        // If no GPS try to change settings
        if (!gpsAcquired) {
            startLocationUpdates();
        }
        if (currentLocation == null) {
            Toast.makeText(getApplicationContext(), "Acquiring location ... Please try again", Toast.LENGTH_SHORT).show();
            return;
        } else {
            openReportDialog();
                }
        }

    public void addBinMarker(View view){
        // If no GPS try to change settings
        if (!gpsAcquired) {
            startLocationUpdates();
        }
        if (currentLocation == null) {
            Toast.makeText(getApplicationContext(), "Acquiring locaiton ... Please try again", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            //LatLng you = new LatLng(currentLocation.latitude, currentLocation.longitude);
            // Check if location is by existing marker
            boolean near;
            near = checkProximity(currentLocation, mMarkerArray);
            if(near) {
                openBinDialog();
            }
            else{addMarker(currentLocation);}
           // if (proximity) {
            //    addMarker(you);
           // }
        }
    }

    public boolean checkProximity(LatLng position, ArrayList<Marker> markerList){
         boolean near = false;
        for (Marker marker : markerList) {

            Location loc1 = new Location("");
            loc1.setLatitude(marker.getPosition().latitude);
            loc1.setLongitude(marker.getPosition().longitude);

            Location loc2 = new Location("");
            loc2.setLatitude(position.latitude);
            loc2.setLongitude(position.longitude);

            if (loc1.distanceTo(loc2) < 50) {
                near = true;
                // If close to one that is enough to break
                break;
            }
        }
        return near;
    }

    /**
     * Add marker to map at location latlon
     * @param latlon
     */
    public void addMarker(LatLng latlon) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latlon).title("Yep, you!").icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMarkerArray.add(marker);
        Toast.makeText(getApplicationContext(), " "+ mMarkerArray.size() , Toast.LENGTH_SHORT).show();

    }


    // opens dialog box if location is near an existing marker
    public void openBinDialog() {
        proximityDialog box = new proximityDialog();
        box.show(getSupportFragmentManager(), "Proximity check");
    }

    // uses interface to dialog box positive button
    @Override
    public void proximityPositiveClick(proximityDialog dialog) {
        // Need to figure out how to put the function in here.
        // Variable to switch function.....?
        //LatLng you = new LatLng(currentLocation.latitude, currentLocation.longitude);
        addMarker(currentLocation);
    }

    // uses interface to dialog box positive button
    @Override
    public void proximityNegativeClick(proximityDialog dialog) {
        return;
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

        // Working code to access location preferences
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                gpsAcquired = true;
            }
        });


        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MapsActivity.this,
                                REQUEST_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        gpsAcquired = false;
                    }
                }
            }
        });
        // end of code

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    // Change stored location when location changes
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    // set up google map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // This could be a variable dependent on the relevant town.
        LatLng dublin = new LatLng(53.3498, -6.2603);
        //mMap.addMarker(new MarkerOptions().position(dublin).title("This is Dublin!"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dublin, 10));

    }

    // Function that stores new location on update
    public void onLocationChanged(Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    // Show dialog for reporting problems in the area
    public void openReportDialog() {
        // Create an instance of the dialog fragment and show it
        reportDialog dialog = new reportDialog();
        dialog.show(getSupportFragmentManager(), "reportDialog");
    }

    // Show method was overloaded to pass through type of report information
    public void openReportNearbyDialog(String type) {
        // Create an instance of the dialog fragment and show it
        reportNearbyDialog dialog = new reportNearbyDialog();
        dialog.show(getSupportFragmentManager(), "reportNearbyDialog", type);
    }

    // uses interface to dialog box positive button
    @Override
    public void reportNearbyPositiveClick(reportNearbyDialog dialog, String type) {
        // Need to figure out how to put the function in here.
        // Variable to switch function.....?
        //LatLng you = new LatLng(currentLocation.latitude, currentLocation.longitude);
        addReportMarker(type, currentLocation);
    }

    // uses interface to dialog box positive button
    @Override
    public void reportNearbyNegativeClick(reportNearbyDialog dialog) { return;    }


    @Override
    public void reportPositiveClick(reportDialog dialog, String type) {
        Toast.makeText(getApplicationContext(), "Processed " + type, Toast.LENGTH_SHORT).show();
        //LatLng you = new LatLng(currentLocation.latitude, currentLocation.longitude);
        boolean near=false;
        switch (type){
            case "Litter": near = checkProximity(currentLocation, mLitterArray); break;
            case "Dumping": near = checkProximity(currentLocation, mDumpingArray); break;
            case "Graffiti": near = checkProximity(currentLocation, mGraffitiArray); break;
            case "Chemical spill": near = checkProximity(currentLocation, mSpillArray); break;
        }
        if (near) {
            openReportNearbyDialog(type);
        } else { addReportMarker(type, currentLocation);}
    }


    public void addReportMarker(String type, LatLng you){
        Marker marker;
        switch (type){
            case "Litter":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                mLitterArray.add(marker);
            break;
            case "Dumping":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mDumpingArray.add(marker);

            break;
            case "Graffiti":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mGraffitiArray.add(marker);
            break;
            case "Chemical spill":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                mSpillArray.add(marker);

            break;
        }
    }

    @Override
    public void reportNegativeClick(reportDialog dialog) {
        return;
    }



}
