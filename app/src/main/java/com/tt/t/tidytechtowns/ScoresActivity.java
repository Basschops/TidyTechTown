package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class ScoresActivity extends AppCompatActivity {

    private ActionBarDrawerToggle t;
    private NavigationView nv;

    // initialises activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        // create DrawerLayout object for navigation menu
        // variables needed for navigation drawer
        final DrawerLayout dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        nv = (NavigationView) findViewById(R.id.nv);

        // set options for navigation drawer - each item will trigger an activity if selected
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                switch (id) {
                    case R.id.ratings: dl.closeDrawers();
                        break;
                    case R.id.map: startMaps(nv);
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

        // Retrieves user score from database
        TextView uScore = findViewById(R.id.userScore);
        MyDatabase db = new MyDatabase(this);
        int score = (int) Math.round(db.returnScore());
        uScore.setText(Integer.toString(score));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // respond to items chosen in navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks.
        int id = item.getItemId();

        if (id == R.id.action_settings) { return true;  }

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // start event activity
    public void startEventCalendar(View v) {
        Intent intent = new Intent(ScoresActivity.this, EventActivity.class);
        startActivity(intent);
    }

    // start Carbon activity
    public void startCarbon(View v) {
        Intent intent = new Intent(ScoresActivity.this, Carbon.class);
        startActivity(intent);
    }

    // start maps activity
    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }

    // start plogging activity
    public void startPlogging(View v) {
        Intent intent = new Intent(ScoresActivity.this, Plogging.class);
        startActivity(intent);
    }

    // Return to login
    public void startLogin(View v) {
        Intent intent = new Intent(ScoresActivity.this, LandingPage.class);
        startActivity(intent);
    }

} // end class
