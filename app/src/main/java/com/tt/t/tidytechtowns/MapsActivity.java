package com.tt.t.tidytechtowns;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
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

//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//
//            }
//
//            @Override
//            public void onStatusChanged(String s, int i, Bundle bundle) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String s) {
//
//            }
//
//            // If gps is turned off, open settings to turn it on
//            @Override
//            public void onProviderDisabled(String s) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//            }
//        };
//        //could check for version here. If <=23 will not need to ask for gps
//        // if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.INTERNET
//            }, 10);
//            return;
//        }
//        //else {
//        locationManager.requestLocationUpdates("gps", 10000, 20, locationListener);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode) {
//            case 10:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                    locationManager.requestLocationUpdates("gps", 10000, 20, locationListener);
//                return;
//        }
//    }

            //locationManager.requestLocationUpdates("gps", 10000, 20, locationListener);


            Button binShow = (Button) findViewById(R.id.binBtn);
            binShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!showing) {
                        LatLng bin1 = new LatLng(53, -6);
                        bin = mMap.addMarker(new MarkerOptions().position(bin1).title("This is a BIN!").draggable(true));
                        Button button = (Button)findViewById(R.id.binBtn);
                        button.setText("Hide bins");
                        showing = true;
                    }
                    else{
                        bin.remove();
                        Button button = (Button)findViewById(R.id.binBtn);
                        button.setText("Show bins");
                        showing = false;
                    }
                }
            });

//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//
//                        "Lat : " + latLng.latitude + " , "
//                                + "Long : " + latLng.longitude,
//                        Toast.LENGTH_LONG).show();
//
//            }
//        });

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
