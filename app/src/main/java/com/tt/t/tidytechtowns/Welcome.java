package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class Welcome extends AppCompatActivity {


    private Cursor bins;
    private boolean lastBin = false;
    private Cursor centers;
    private Cursor communities;
    private MyDatabase db;


    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);



        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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
                    case R.id.carbonfootprint: startCarbonFootPrintCalculator(nv);
                        break;
                    case R.id.plogging: startPlogging(nv);
                        break;

                    default:
                        return true;
                }

                return true;

            }
        });






    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }





    public void startScores(View v) {
        Intent intent = new Intent(Welcome.this, ScoresActivity.class);
        startActivity(intent);
    }



    public void startEventCalendar(View v) {
        Intent intent = new Intent(Welcome.this, EventActivity.class);
        startActivity(intent);
    }

    public void startCarbon(View v) {
        Intent intent = new Intent(Welcome.this, Carbon.class);
        startActivity(intent);
    }


    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }


    public void startCarbonFootPrintCalculator(View v) {
        Intent intent = new Intent(Welcome.this, CarbonFootprint.class);
        startActivity(intent);
    }

    public void startPlogging(View v) {
        Intent intent = new Intent(Welcome.this, Plogging.class);
        startActivity(intent);
    }





}












