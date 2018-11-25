package com.tt.t.tidytechtowns;

// NB the code in this module is adapted from the android calendar
// library at https://github.com/SundeepK/CompactCalendarView

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;


import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;


public class EventActivity extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;


    private ArrayAdapter<String> mScoresAdapter;

    private MyDatabase db;
    private Cursor eventCursor;

    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    Date d = new Date();

    private String eventClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);



        final CompactCalendarView calendar;
        final ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(false);

        actionbar.setTitle(dateFormatMonth.format(d));

        calendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        calendar.setUseThreeLetterAbbreviation(true);

        db = new MyDatabase(this);
        eventCursor = db.getEvents();

        List<String> outputArray = new ArrayList<String>();


        eventCursor.moveToFirst();
        while (eventCursor.isAfterLast() == false)  {

            String anEvent = eventCursor.getString(1);
            long date = eventCursor.getLong(2);


            Event e1 = new Event(Color.YELLOW, date, anEvent);
            calendar.addEvent(e1, true);


            eventCursor.moveToNext();
        };





        // define a listener to receive callbacks when certain events happen.
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                long millisecond = dateClicked.getTime();
                boolean nothingOn = true;

                List <Event> monthEvents = calendar.getEventsForMonth(millisecond);


                for (int i = 0; i <= monthEvents.size()-1; i++) {

                    Event ev = monthEvents.get(i);
                    Long thisdate = ev.getTimeInMillis();

                    if (millisecond == thisdate) {

                        String thisevent = ev.getData().toString();
                        Toast.makeText(context, thisevent, Toast.LENGTH_SHORT).show();
                        nothingOn = false;
                        eventClicked = thisevent;
                    }



                }

                if (nothingOn) {

                    Toast.makeText(context, "Nothing on today", Toast.LENGTH_SHORT).show();
                }




            } // end onDayClick

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionbar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });


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

                    default:
                        return true;
                }

                return true;

            }
        });



    } // end onCreate


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }



    public void notifyAttending(View v) {

        if (eventClicked!=null) {

            Intent email = new Intent(Intent.ACTION_SEND);
            email.setData(Uri.parse("mailto:"));
            //email.setType("text/plain");
            email.setType("message/rfc822");
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sheena.davitt@ucdconnect.ie"});
            email.putExtra(Intent.EXTRA_SUBJECT, "I wish to attend the " + eventClicked);
            email.putExtra(Intent.EXTRA_TEXT, "Dear organiser, I will be attending " + eventClicked);
            try {
                startActivity(Intent.createChooser(email, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(EventActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

            eventClicked=null;
        }

        else Toast.makeText(EventActivity.this, "You have not selected an event", Toast.LENGTH_SHORT).show();
    }



    public void startScores(View v) {
        Intent intent = new Intent(EventActivity.this, ScoresActivity.class);
        startActivity(intent);
    }



    public void startEventCalendar(View v) {
        Intent intent = new Intent(EventActivity.this, EventActivity.class);
        startActivity(intent);
    }

    public void startCarbon(View v) {
        Intent intent = new Intent(EventActivity.this, Carbon.class);
        startActivity(intent);
    }


    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }

}// end class
