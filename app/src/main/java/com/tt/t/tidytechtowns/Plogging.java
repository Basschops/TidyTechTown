package com.tt.t.tidytechtowns;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Plogging extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = "daragh";
    private GoogleMap mMap;
    private MyDatabase db2;
    private Cursor markers;
    private ArrayList<Marker> mMarkerArray = new ArrayList<Marker>();
    private ArrayList<Marker> mLitterArray = new ArrayList<Marker>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plogging);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng dublin = new LatLng(53.3498, -6.2603);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dublin, 10));
        //mMap.addMarker(new MarkerOptions().position(dublin).icon(BitmapDescriptorFactory
          //      .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
        loadMarkers();
        //getDirections(o,d);
    }

    public void butLoad(View view){
        loadMarkers();
    }

    public void loadMarkers(){
        //Toast.makeText(getApplicationContext(), "Loading Markers", Toast.LENGTH_SHORT).show();
        db2 = new MyDatabase(this);
        markers = db2.getPloggingInfo();
        LatLng temp;
        String type;
        Marker marker;
        LatLng dub = new LatLng(53.352, -6.259);
        marker = mMap.addMarker(new MarkerOptions().position(dub).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        if (markers==null){            Toast.makeText(getApplicationContext(), "######", Toast.LENGTH_SHORT).show();}


            do {

            temp = new LatLng(markers.getDouble(1), markers.getDouble(2));
            type = markers.getString(3);
            Toast.makeText(getApplicationContext(), "Loading Markers"+type, Toast.LENGTH_SHORT).show();

            if(type=="Litter") {
                marker = mMap.addMarker(new MarkerOptions().position(temp).title(type).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                mLitterArray.add(marker);
            }
            else if(type=="Bin"){
                marker = mMap.addMarker(new MarkerOptions().position(temp).title(type).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_CYAN)));
                mMarkerArray.add(marker);
            }
        } while (markers.moveToNext());
    }
/*
    // Adapted from https://android.jlelse.eu/google-maps-directions-api-5b2e11dee9b0
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setQueryRateLimit(3)
                .setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    LatLng o = new LatLng(53.34, -6.25);
    LatLng d = new LatLng(53.3498, -6.2603);

    private void getDirections(LatLng o, LatLng destination) {
        DateTime now = new DateTime();
        DirectionsResult result = null;
        try {
            result = DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.WALKING).origin(o.toString()).destination(destination.toString())
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        addPolyline(result, mMap);
//Now we can call the await method on the DirectionsApiRequest. This will make a synchronous call to the web service and return us a DirectionsResult object.
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(results.routes[0].legs[0].startLocation.lat,results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].startAddress).snippet(getEndLocationTitle(results)));
    }

    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }


    private String getEndLocationTitle(DirectionsResult results){
        return  "Time :"+ results.routes[0].legs[0].duration.humanReadable + " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }

    /*
    // Adapted from https://github.com/akexorcist/Android-GoogleDirectionLibrary/blob/master/app/src/main/java/com/akexorcist/googledirection/sample/WaypointsDirectionActivity.java
    public void requestDirection(){
        GoogleDirectionConfiguration.getInstance().setLogEnabled(true);
        GoogleDirection.withServerKey(serverKey)
                .from(park)
                .and(shopping)
                .and(dinner)
                .to(gallery)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }*/
}
