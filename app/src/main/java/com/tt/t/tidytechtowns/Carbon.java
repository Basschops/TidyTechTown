package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Carbon extends AppCompatActivity implements Tab1.t1dbListener, Tab2.t2dbListener
{

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // variables needed for navigation drawer
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    // initialises activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // create DrawerLayout object for navigation menu
        dl = (DrawerLayout) findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        dl.addDrawerListener(t);
        t.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // set options for navigation drawer - each item will trigger an activity if selected
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
                    case R.id.carbon: dl.closeDrawers();
                        break;
                    case R.id.plogging: startPlogging(nv);
                        break;
                    case R.id.logIn: startLogin(nv);
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });
    }

    // DB access through interface for Tab1
    @Override
    public double dbaccess(){
        MyDatabase db = new MyDatabase(this);
        Cursor cursor = db.getCarbonScore();
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return 0;
        }
        else {
            return cursor.getDouble(0);
        }
    }

    // DB access through interface for Tab2
    @Override
    public double dbaccess2(){
        MyDatabase db = new MyDatabase(this);
        Cursor cursor = db.getCarbonScore();
        cursor.moveToFirst();
        if (cursor.getCount() == 0) {
            return 0;
        }
        else {
            return cursor.getDouble(0);
        }
    }

    // Write home carbon score from Tab1
    @Override
    public void writeHomeScore(double home){
        MyDatabase db = new MyDatabase(this);
        db.writeHomeCarbon(home);
        db.close();
    }

    // Write travel carbon score from Tab2
    @Override
    public void writeTravelScore(double travel){
        MyDatabase db = new MyDatabase(this);
        db.writeTravelCarbon(travel);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // respond to items chosen in navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position){
                case 0:
                    Tab1 tab1 = new Tab1();
                    return tab1;
                case 1:
                    Tab2 tab2 = new Tab2();
                    return tab2;
                default:
                    return null;
            }
        }

        // Show 2 total pages.
        @Override
        public int getCount() {
            return 2;
        }
    }

    // Required stop the app from crashing when it its turned sideways.
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Navigation functions

    // start Scores activity
    public void startScores(View v) {
        Intent intent = new Intent(Carbon.this, ScoresActivity.class);
        startActivity(intent);
    }

    // start Event activity
    public void startEventCalendar(View v) {
        Intent intent = new Intent(Carbon.this, EventActivity.class);
        startActivity(intent);
    }

    // start maps activity
    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }

    // start plogging activity
    public void startPlogging(View v) {
        Intent intent = new Intent(Carbon.this, Plogging.class);
        startActivity(intent);
    }

    // Return to login
    public void startLogin(View v) {
        Intent intent = new Intent(Carbon.this, LandingPage.class);
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

} // end activity
