package com.tt.t.tidytechtowns;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.android.ui.IconGenerator;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

public class Plogging extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private final String TAG = "daragh";
    private GoogleMap mMap;
    private MyDatabase db2;
    private Cursor markers;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private ArrayList<Marker> mLitterArray = new ArrayList<Marker>();

    private ArrayList<Marker> exclude = new ArrayList<Marker>();

    private Polyline polyline;
    private boolean polyShowing = false;
    private Marker plogLabel;
    private boolean snackbarShown = false;
    private boolean showing = false;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plogging);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        startLocationUpdates();

        AlertDialog alertDialog = new AlertDialog.Builder(Plogging.this).create();
        alertDialog.setTitle("What is Ploging?");
        alertDialog.setMessage("Plogging is... \n\nPicking up \nLitter while\nJogging.\n\nClick 'Go Plogging' to find a route that has reported litter.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.ratings: startScores(nv);
                        break;
                    case R.id.map: startMaps(nv);
                        break;
                    case R.id.events: startEventCalendar(nv);
                        break;
                    case R.id.carbon: startCarbon(nv);
                        break;
                    case R.id.plogging: dl.closeDrawers();
                        break;

                    default:
                        return true;
                }
                return true;

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);

        LatLng dublin = new LatLng(53.3498, -6.2603);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dublin, 10));

        loadMarkers();
    }

    /**
     * Connected to show/hide bins button to show bins on map.
     */
    public void showBinsP(View view) {
        Button button = (Button) findViewById(R.id.showBinButPlog);
        if (showing) {
            hideMarkers(mMarkerArray);
            button.setText("Show bins");
            showing = false;
        } else {
            showMarkers(mMarkerArray);
            button.setText("Hide bins");
            showing = true;
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

    public void loadMarkers(){
        db2 = new MyDatabase(this);
        markers = db2.getPloggingInfo();
        LatLng temp;
        String type;
        Marker marker;

        // If no data display toast
        if (markers==null){
            Toast.makeText(getApplicationContext(), "No markers to display", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            do {
                temp = new LatLng(markers.getDouble(1), markers.getDouble(2));
                type = markers.getString(3);
                switch (type) {
                    case "Litter":
                        marker = mMap.addMarker(new MarkerOptions().position(temp).title(type).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                        mLitterArray.add(marker);
                        break;
                    case "Bin":
                        marker = mMap.addMarker(new MarkerOptions().position(temp).title(type).icon(BitmapDescriptorFactory
                                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).visible(false));
                        mMarkerArray.add(marker);
                        break;
                }
            } while (markers.moveToNext());
        }
    }

    // Connected to 'plogging' button. Shows route or hides it if already visible.
    public void refreshPlogging(View view) throws ApiException, IOException, InterruptedException {
        // Check location permissions
        checkLocationPermission();
        Button button = (Button) findViewById(R.id.plogBut);
        if(polyShowing){
            polyline.remove();
            plogLabel.remove();
            polyShowing = false;
            button.setText("Go Plogging!");
        }
        else {
            button.setText("Hide route");
            getDirections(currentLocation);

            if(!snackbarShown) {
                View contextView = findViewById(R.id.contextP);
                final Snackbar snackBar = Snackbar.make(contextView,
                        "Click on litter markers to add or remove them from the route",
                        Snackbar.LENGTH_INDEFINITE);
                snackBar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackBar.dismiss();
                    }
                });
                snackBar.show();
                snackbarShown = true;
            }
        }
    }

    // Adapted from https://android.jlelse.eu/google-maps-directions-api-5b2e11dee9b0
    // Gets direction between markers on the map
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    // Get directions between all litter waypoints and back to user location
    private void getDirections(LatLng o) throws ApiException, IOException, InterruptedException {

        ArrayList<Marker> include = new ArrayList<Marker>(mLitterArray);
        for(Marker z: mLitterArray){
            if(exclude.contains(z)){
                include.remove(z);
            }
        }
        // If location is null, only show route for markers
        if(o==null){
            o = include.get(0).getPosition();
            Toast.makeText(getApplicationContext(), "User location not detected. Try again.",
                    Toast.LENGTH_SHORT).show();
        }
        String origin = o.latitude+", "+o.longitude;

        // Ensure there are litter points to include in the route
        if(mLitterArray.isEmpty()){
            Toast.makeText(getApplicationContext(), "There are no litter spots recorded",
                    Toast.LENGTH_SHORT).show();
        }
        else {
            int i=0;
            String[] waypoints = new String[include.size()];
            for (Marker x : include) {
                LatLng y = x.getPosition();
                waypoints[i]=y.latitude + ", " + y.longitude;
                i++;
            }

            DirectionsResult result = null;

            result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.WALKING).origin(origin)
                    .destination(origin)
                    .optimizeWaypoints(true)
                    .waypoints(waypoints)
                    .await();
            double distance=0;
            double time=0;
            for(int j=0; j<result.routes[0].legs.length;j++){
                distance+= result.routes[0].legs[0].distance.inMeters;
                time += result.routes[0].legs[0].duration.inSeconds;
            }
            distance= ((double) Math.round(distance/100))/10; //convert to km with one d.p.
            int timeI = (int) time/60; // convert to minutes
            // Convert time to human readable
            String timeS;
            if (timeI>60){timeS = Integer.toString(timeI/60)+"hrs"+Integer.toString(timeI%60)+"min";}
            else {timeS = Integer.toString(timeI/60)+"min";}

            String details =  " Distance: " + distance+ "km\n " +
                    "Walking time: "+timeS;;
            IconGenerator iconFactory = new IconGenerator(this);
            plogLabel = mMap.addMarker(new MarkerOptions().position(o).zIndex(1)
                    .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(details)))
                    .anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV()));
            addPolyline(result, mMap);
            //new LatLng(result.routes[0].legs[0]
             //       .endLocation.lat, result.routes[0].legs[0].endLocation.lng
        }
//Now we can call the await method on the DirectionsApiRequest. This will make a synchronous call to the web service and return us a DirectionsResult object.
    }

    // Draw route on map with info marker.
    private void addPolyline(DirectionsResult results, GoogleMap mMap) {


        //Toast.makeText(getApplicationContext(), details, Toast.LENGTH_SHORT).show();
        // Display route
        if(results.routes.length >0 ){
            List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
            polyline = mMap.addPolyline(new PolylineOptions().color(Color.GREEN).addAll(decodedPath));
        }
        else{
            Toast.makeText(getApplicationContext(), "Cannot get directions right now",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        // Display litter markers
        showMarkers(mLitterArray);

        // Display distance of route

        polyShowing = true;
    }

    ///// EXPERIMENTAL CODE

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Only do this if plogging is on
        if (polyShowing) {
            if (exclude.contains(marker)) {
                exclude.remove(marker);
            }
            else {
                exclude.add(marker);
            }
            polyline.remove();
            plogLabel.remove();
            try {

                getDirections(currentLocation);
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }






    private static LatLng currentLocation;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 5000;  /* 5 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 secs */
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

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
                        resolvable.startResolutionForResult(Plogging.this,
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


    // NAVIGATION FUNCTIONS
    public void startScores(View v) {
        Intent intent = new Intent(getBaseContext(), ScoresActivity.class);
        startActivity(intent);
    }
    public void startEventCalendar(View v) {
        Intent intent = new Intent(getBaseContext(), EventActivity.class);
        startActivity(intent);
    }
    public void startCarbon(View v) {
        Intent intent = new Intent(getBaseContext(), Carbon.class);
        startActivity(intent);
    }
    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }


}


