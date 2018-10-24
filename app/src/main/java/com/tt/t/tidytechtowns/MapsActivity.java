package com.tt.t.tidytechtowns;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

    public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

        private GoogleMap mMap;
        private boolean showing = false;
        private Marker bin;
        private LocationManager locationManager;
        private LocationListener locationListener;
        private LatLng currentLocation;
        private Button gpsbutton;

        public void addBin(LatLng latlng) {

        }

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
            binShow.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick (View view){
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

            gpsbutton = (Button) findViewById(R.id.GPS);
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    currentLocation = new LatLng(location.getLongitude(), location.getLatitude());
                    Toast toast = Toast.makeText(getApplicationContext(), "You there"+currentLocation.latitude, Toast.LENGTH_SHORT);
                    toast.show();
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                // If gps is turned off, open settings to turn it on
                @Override
                public void onProviderDisabled(String s) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            };

            configureButton();
        }

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

        void configureButton() {
            // first check for permissions
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                            , 10);
                }
                return;
            }
            // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
            gpsbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates("gps", 5000, 0, locationListener);
                    String msg;
                    if (currentLocation!=null){
                        msg = currentLocation.longitude + " " + currentLocation.latitude;
                        LatLng you = new LatLng(currentLocation.longitude, currentLocation.latitude);
                        mMap.addMarker(new MarkerOptions().position(you).title("Yep, you!"));
                    }
                    else
                        {msg = "Not available";}
                    Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
        }







        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera. In this case,
         * we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to install
         * it inside the SupportMapFragment. This method will only be triggered once the user has
         * installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng dublin = new LatLng(53.3498, -6.2603);
            mMap.addMarker(new MarkerOptions().position(dublin).title("This is Dublin!"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dublin, 10));
        }

        public void showBin(){
            LatLng bin1 = new LatLng(53, 6);
            mMap.addMarker(new MarkerOptions().position(bin1).title("This is a BIN!"));
        }



    }
