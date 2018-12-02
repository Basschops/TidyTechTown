package com.tt.t.tidytechtowns;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
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

public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback,
        proximityDialog.mapDialogListener, reportDialog.reportDialogListener,
        reportNearbyDialog.mapDialogListener, showHideReports.reportShowHideDialogListener
    {

    private GoogleMap mMap;
    private boolean showingBin = true;
    private boolean showingRec = false;
    private boolean showingLitt = false;
    private boolean showingDump = false;
    private boolean showingGraf = false;
    private boolean showingSpill = false;
    private static LatLng currentLocation;
    private LocationRequest mLocationRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private long UPDATE_INTERVAL = 5000;  /* 5 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 secs */
    // Arrays to store map markers
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private ArrayList<Marker> mLitterArray = new ArrayList<Marker>();
    private ArrayList<Marker> mDumpingArray = new ArrayList<Marker>();
    private ArrayList<Marker> mSpillArray = new ArrayList<Marker>();
    private ArrayList<Marker> mGraffitiArray = new ArrayList<Marker>();
    private ArrayList<Marker> mRecyclingArray = new ArrayList<Marker>();

    private MyDatabase db;
    private Cursor markers;

    // variables needed for navigation drawer
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    // initialises activity
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        startLocationUpdates();

        // create DrawerLayout object for navigation menu
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        // set options for navigation drawer - each item will trigger an activity if selected
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.ratings: startScores(nv);
                        break;
                    case R.id.map: dl.closeDrawers();
                        break;
                    case R.id.events: startEventCalendar(nv);
                        break;
                    case R.id.carbon: startCarbon(nv);
                        break;
                    case R.id.plogging: startPlogging(nv);
                        break;
                    case R.id.logIn: startLogin(nv);
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    // Set up google map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // This could be a variable dependent on the relevant town.
        LatLng dublin = new LatLng(53.3498, -6.2603);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dublin, 10));
        loadMarkers();
    }


    // Load all markers from database memory and store in appropriate arrays
    public void loadMarkers(){
        db = new MyDatabase(this);
        markers = db.getBins();
        LatLng temp;
        String type, name;
        Marker marker;

        do {
            temp = new LatLng(markers.getDouble(1), markers.getDouble(2));
            type = markers.getString(3);

            switch (type) {
                case "Bin":
                    marker = mMap.addMarker(new MarkerOptions().position(temp).title(type)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                    .HUE_GREEN)));
                    mMarkerArray.add(marker);
                    break;
               case "Litter":
                    marker = mMap.addMarker(new MarkerOptions().position(temp).title(type)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                    .HUE_CYAN)).visible(false));
                    mLitterArray.add(marker);
                    break;
                case "Dumping":
                    marker = mMap.addMarker(new MarkerOptions().position(temp).title(type)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                    .HUE_AZURE)).visible(false));
                    mDumpingArray.add(marker);
                    break;
                case "Graffiti":
                    marker = mMap.addMarker(new MarkerOptions().position(temp).title(type)
                            .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)).visible(false));
                    mGraffitiArray.add(marker);
                    break;
                case "Chemical spill":
                    marker = mMap.addMarker(new MarkerOptions().position(temp).title(type)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                                    .HUE_VIOLET)).visible(false));
                    mSpillArray.add(marker);
                    break;
                }

        } while (markers.moveToNext());
        // Load recycling centres
        markers = db.getCenters();
        do {
            temp = new LatLng(markers.getDouble(1), markers.getDouble(2));
            name = markers.getString(3);
            marker = mMap.addMarker(new MarkerOptions().position(temp).title(name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory
                            .HUE_MAGENTA)).visible(false));
            mRecyclingArray.add(marker);
        } while (markers.moveToNext());
        db.close();
    }

        public void onCheckboxClicked(View view) {
            // Is the view now checked?
            boolean checked = ((CheckBox) view).isChecked();


            // Check which checkbox was clicked
            switch(view.getId()) {
                case R.id.chkBin:
                    if (checked) {
                        if (!showingBin) {
                            showMarkers(mMarkerArray);
                            showingBin = true;
                            Toast.makeText(getApplicationContext(), "Bins",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (showingBin) {
                            hideMarkers(mMarkerArray);
                            showingBin = false;
                        }
                    }
                    break;
                case R.id.chkRec:
                    if (checked) {
                        if (!showingRec) {
                            showMarkers(mRecyclingArray);
                            showingRec = true;
                            Toast.makeText(getApplicationContext(), "Recycling centres",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (showingRec) {
                            hideMarkers(mRecyclingArray);
                            showingRec = false;
                        }
                    }
                    break;
                case R.id.chkLitter:
                    if (checked) {
                        if (!showingLitt) {
                            showMarkers(mLitterArray);
                            showingLitt = true;
                            Toast.makeText(getApplicationContext(), "Litter",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (showingLitt) {
                            hideMarkers(mLitterArray);
                            showingLitt = false;
                        }
                    }
                    break;
                case R.id.chkDump:
                    if (checked) {
                        if (!showingDump) {
                            showMarkers(mDumpingArray);
                            showingDump = true;
                            Toast.makeText(getApplicationContext(), "Illegal dumping",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (showingDump) {
                            hideMarkers(mDumpingArray);
                            showingDump = false;
                        }
                    }
                    break;
                case R.id.chkGraf:
                    if (checked) {
                        if (!showingGraf) {
                            showMarkers(mGraffitiArray);
                            showingGraf = true;
                            Toast.makeText(getApplicationContext(), "Graffiti",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (showingGraf) {
                            hideMarkers(mGraffitiArray);
                            showingGraf = false;
                        }
                    }
                    break;
                case R.id.chkChem:
                    if (checked) {
                        if (!showingSpill) {
                            showMarkers(mSpillArray);
                            showingSpill = true;
                            Toast.makeText(getApplicationContext(), "Chemical spill",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        if (showingSpill) {
                            hideMarkers(mSpillArray);
                            showingSpill = false;
                        }
                    }
                    break;
            }
        }


    //Connected to report issue button. Reports issue and adds marker to map at user location
    public void reportIssue(View view) {
        if (currentLocation == null) {
            Toast.makeText(getApplicationContext(), "Acquiring location ... Please try again",
                    Toast.LENGTH_SHORT).show();
            startLocationUpdates();
        }
        // If location OK open dialog with user
        else {
            openReportDialog();
        }
    }


     //Adds bin marker to map at user location
    public void addBinMarker(View view) {
        // If no location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
        }
        if (currentLocation == null) {
            Toast.makeText(getApplicationContext(), "Acquiring location ... Please try again",
                    Toast.LENGTH_SHORT).show();
            startLocationUpdates();
        }
        else {
            // Check if location is by existing marker
            boolean near;
            near = checkProximity(currentLocation, mMarkerArray);
            if (near) {
                openBinDialog();
            }
            else {
                addMarker(currentLocation);
                db.writeDatabase(currentLocation.latitude, currentLocation.longitude, "Bin");
                db.close();
            }
        }
    }

    // Makes entire array of markers visible
    public void showMarkers(ArrayList<Marker> markerArray) {
        for (Marker marker : markerArray) {
            marker.setVisible(true);
        }
    }

    // Makes entire array of markers hidden
    public void hideMarkers(ArrayList<Marker> markerArray) {
        for (Marker marker : markerArray) {
            marker.setVisible(false);
        }
    }

    /**
     *  Checks for other items in the proximity of the user
     * @param position of user
     * @param markerList list of markers of that type
     * @return true if within proximity
     */
    public boolean checkProximity(LatLng position, ArrayList<Marker> markerList) {
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
                // If close to one that is enough to break loop
                break;
            }
        }
        return near;
    }

    /**
     * Add bin marker to map at location latlon
     * @param latlon location of user
     */
    public void addMarker(LatLng latlon) {
        Marker marker = mMap.addMarker(new MarkerOptions().position(latlon).title("Bin")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMarkerArray.add(marker);
        db.writeDatabase(currentLocation.latitude, currentLocation.longitude, "Bin");
        db.addScore("Bin");
        db.close();
    }

    // opens dialog box if location is near an existing marker
    public void openBinDialog() {
        proximityDialog box = new proximityDialog();
        box.show(getSupportFragmentManager(), "Proximity check");
    }

    // uses interface to dialog box positive button after clicking 'add bin'
    @Override
    public void proximityPositiveClick(proximityDialog dialog) {
        addMarker(currentLocation);
    }

    // uses interface to dialog box negative button after clicking 'add bin'
    @Override
    public void proximityNegativeClick(proximityDialog dialog) {
    }

    // Location request adapted from
    // https://github.com/codepath/android_guides/wiki/Retrieving-Location-with-LocationServices-API
    @SuppressLint("MissingPermission")
    protected void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
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

                    }
                }
            }
        });

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest,
                new LocationCallback() {
                    // Store new location when location changes
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    // Check that permission to use location is enabled. If not this will open settings.
    public void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(getApplicationContext(), "Requires location permission...",
                    Toast.LENGTH_SHORT).show();

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
        return;
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

    // If user clicks OK to report dialog
    @Override
    public void reportPositiveClick(reportDialog dialog, String type) {
        if (type == null) {
            return;
        }
        boolean near = false;
        switch (type) {
            case "Litter":
                near = checkProximity(currentLocation, mLitterArray);
                break;
            case "Dumping":
                near = checkProximity(currentLocation, mDumpingArray);
                break;
            case "Graffiti":
                near = checkProximity(currentLocation, mGraffitiArray);
                break;
            case "Chemical spill":
                near = checkProximity(currentLocation, mSpillArray);
                break;
        }
        // If in proximity to similar marker check with user
        if (near) {
            openReportNearbyDialog(type);
        }
        else {
            addReportMarker(type, currentLocation);
        }
    }

    // If user clicks 'cancel' in report dialog box
    @Override
    public void reportNegativeClick(reportDialog dialog) {
        return;
    }

    // If there is a similar report nearby this dialog will prompt user
    // Show method was overloaded to pass through type of report information
    public void openReportNearbyDialog(String type) {
        // Create an instance of the dialog fragment and show it
        reportNearbyDialog dialog = new reportNearbyDialog();
        dialog.show(getSupportFragmentManager(), "reportNearbyDialog", type);
    }

    // uses interface to positive button of openReportNearbyDialog
    @Override
    public void reportNearbyPositiveClick(reportNearbyDialog dialog, String type) {
        // Need to figure out how to put the function in here.
        addReportMarker(type, currentLocation);
    }

    // uses interface to negative button of openReportNearbyDialog
    @Override
    public void reportNearbyNegativeClick(reportNearbyDialog dialog) {
    }

    /**
     * Adds marker depending on selection in report dialog
     * Also records in database for scoring points
     * @param type of marker (report)
     * @param you position of user
     */
    public void addReportMarker(String type, LatLng you) {
        Marker marker;
        switch (type) {
            case "Litter":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type)
                        .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                mLitterArray.add(marker);
                db.writeDatabase(currentLocation.latitude, currentLocation.longitude, type);
                db.addScore(type);
                break;
            case "Dumping":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type)
                        .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                mDumpingArray.add(marker);
                db.writeDatabase(currentLocation.latitude, currentLocation.longitude, type);
                db.addScore(type);
                break;
            case "Graffiti":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type)
                        .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                mGraffitiArray.add(marker);
                db.writeDatabase(currentLocation.latitude, currentLocation.longitude, type);
                db.addScore(type);
                break;
            case "Chemical spill":
                marker = mMap.addMarker(new MarkerOptions().position(you).title(type)
                        .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
                mSpillArray.add(marker);
                db.writeDatabase(currentLocation.latitude, currentLocation.longitude, type);
                db.addScore(type);
                break;
        }
    }

    // Connected to 'show/hide report' button.
    // Opens dialog for user to select to show specific information
    public void openShowReportDialog(View view) {
        showHideReports box = new showHideReports();
        box.show(getSupportFragmentManager(), "Proximity check");
    }

    // When positive button of showHideDialog is selected only show the items that were selected
    @Override
    public void reportShowHidePositive(showHideReports dialog, ArrayList<Integer> selected) {
        String[] reportTypes = getResources().getStringArray(R.array.report);

        // Integers identify each selection. If integer is present show, if not hide.
        for (int i = 0; i < 4; i += 1) {
            if (selected.contains(i)) {
                switch (reportTypes[i]) {
                    case "Litter":
                        showMarkers(mLitterArray);
                        break;
                    case "Dumping":
                        showMarkers(mDumpingArray);
                        break;
                    case "Graffiti":
                        showMarkers(mGraffitiArray);
                        break;
                    case "Chemical spill":
                        showMarkers(mSpillArray);
                        break;
                }
            } else {
                switch (reportTypes[i]) {
                    case "Litter":
                        hideMarkers(mLitterArray);
                        break;
                    case "Dumping":
                        hideMarkers(mDumpingArray);
                        break;
                    case "Graffiti":
                        hideMarkers(mGraffitiArray);
                        break;
                    case "Chemical spill":
                        hideMarkers(mSpillArray);
                        break;
                }
            }
        }

    }

    // If 'cancel' is pressed in showHideDialog
    @Override
    public void reportShowHideNegative(showHideReports dialog) {
        return;
    }

    // NAVIGATION FUNCTIONS

    // start Scores activity
    public void startScores(View v) {
        Intent intent = new Intent(MapsActivity.this, ScoresActivity.class);
        startActivity(intent);
    }

    // start Event activity
    public void startEventCalendar(View v) {
        Intent intent = new Intent(MapsActivity.this, EventActivity.class);
        startActivity(intent);
    }

    // start carbon activity
    public void startCarbon(View v) {
        Intent intent = new Intent(MapsActivity.this, Carbon.class);
        startActivity(intent);
    }

    // start plogging activity
    public void startPlogging(View v) {
        Intent intent = new Intent(MapsActivity.this, Plogging.class);
        startActivity(intent);
    }

    // Return to login
    public void startLogin(View v) {
        Intent intent = new Intent(MapsActivity.this, LandingPage.class);
        startActivity(intent);
    }

    // Hides nav bar if back button pressed
    @Override
    public void onBackPressed() {
        if (this.dl.isDrawerOpen(nv)) {
            this.dl.closeDrawer(nv);
        } else {
            super.onBackPressed();
        }
    }

}