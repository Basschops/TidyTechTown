package com.tt.t.tidytechtowns;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Carbon extends AppCompatActivity
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
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(t.onOptionsItemSelected(item))
            return true;

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    //Deleted Placeholder fragment class.

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


        @Override
        public int getCount() {
            // Show 4 total pages.
            return 2;
        }
    }

    //need this to stop the app from crashing when it its turned sideways.

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    public void startScores(View v) {
        Intent intent = new Intent(Carbon.this, ScoresActivity.class);
        startActivity(intent);
    }



    public void startEventCalendar(View v) {
        Intent intent = new Intent(Carbon.this, EventActivity.class);
        startActivity(intent);
    }

    public void startCarbon(View v) {
        Intent intent = new Intent(Carbon.this, Carbon.class);
        startActivity(intent);
    }

    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }



    public void startPlogging(View v) {
        Intent intent = new Intent(Carbon.this, Plogging.class);
        startActivity(intent);
    }

} // end outer class
