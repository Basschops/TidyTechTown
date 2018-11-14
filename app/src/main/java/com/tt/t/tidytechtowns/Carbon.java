package com.tt.t.tidytechtowns;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Carbon extends AppCompatActivity {

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

                case 2:
                    Tab3 tab3 = new Tab3();
                    return tab3;

                    default:
                        return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    public void calculate_Cf(View v) {

        //tab 1 values
        EditText elec = (EditText)findViewById(R.id.electricity);
        EditText heat = (EditText)findViewById(R.id.heating);
        EditText coal = (EditText)findViewById(R.id.coal);
        //tab 2 values
        Spinner flight_t = (Spinner)findViewById(R.id.flight_type);
        EditText trips = (EditText)findViewById(R.id.trips);
        Spinner vehicle_t = (Spinner)findViewById(R.id.vehicle_type);
        Spinner fuel_t = (Spinner)findViewById(R.id.fuel_type);
        EditText milage = (EditText)findViewById(R.id.milage);
        EditText bus = (EditText)findViewById(R.id.bus);
        EditText rail = (EditText)findViewById(R.id.rail);
        EditText luas = (EditText)findViewById(R.id.luas);
        //tab 3 values
        EditText phone = (EditText)findViewById(R.id.phone);
        EditText food = (EditText)findViewById(R.id.food);
        EditText rec = (EditText)findViewById(R.id.recreational);

        //Getting Numeric Values for Carbon Calculation
        int electricity = Integer.parseInt(elec.getText().toString());
        int heating = Integer.parseInt(heat.getText().toString());
        int coaleen = Integer.parseInt(coal.getText().toString());
        int trip = Integer.parseInt(trips.getText().toString());
        int milages = Integer.parseInt(milage.getText().toString());
        int buses = Integer.parseInt(bus.getText().toString());
        int rails = Integer.parseInt(rail.getText().toString());
        int luass = Integer.parseInt(luas.getText().toString());
        int phones = Integer.parseInt(phone.getText().toString());
        int foods = Integer.parseInt(food.getText().toString());
        int recs = Integer.parseInt(rec.getText().toString());

        //Getting the values from the spinners.
        int type_of_flight = Integer.parseInt(flight_t.getSelectedItem().toString());
        int type_of_vehicle = Integer.parseInt(vehicle_t.getSelectedItem().toString());
        int type_of_fuel = Integer.parseInt(fuel_t.getSelectedItem().toString());



        //do some calculations




    }
}
