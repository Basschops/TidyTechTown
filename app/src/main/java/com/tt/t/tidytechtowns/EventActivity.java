package com.tt.t.tidytechtowns;

// NB the code in this module is adapted from the android calendar
// library at https://github.com/SundeepK/CompactCalendarView

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
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


    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    Date d = new Date();

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




        Event event = new Event(Color.YELLOW, 1540944000000L, "Oct 31 event");
        Event event2 = new Event(Color.YELLOW, 1541116800000L, "Nov 2 event");
        Event event3 = new Event(Color.YELLOW, 1541289600000L, "Nov 4 event");


        calendar.addEvent(event, true);
        calendar.addEvent(event2, true);
        calendar.addEvent(event3, true);







        // define a listener to receive callbacks when certain events happen.
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                Log.i("Tag", "Value " + dateClicked);
                System.out.println("date " + dateClicked);




                if (dateClicked.toString().compareTo("Wed Oct 31 00:00:00 GMT+00:00 2018") == 0) {
                    Toast.makeText(context, "Eco awards", Toast.LENGTH_SHORT).show();
                }

                if (dateClicked.toString().compareTo("Fri Nov 02 00:00:00 GMT+00:00 2018") == 0) {
                    Toast.makeText(context, "Recycle your toaster day", Toast.LENGTH_SHORT).show();
                }

                if (dateClicked.toString().compareTo("Sun Nov 04 00:00:00 GMT+00:00 2018") == 0) {
                    Toast.makeText(context, "Reuse your dishwater event", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Nothing on today", Toast.LENGTH_SHORT).show();

                }

            }

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


        Intent email = new Intent(Intent.ACTION_SEND);
        email.setData(Uri.parse("mailto:"));
        //email.setType("text/plain");
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sheena.davitt@ucdconnect.ie"});
        email.putExtra(Intent.EXTRA_SUBJECT, "I wish to attend your event");
        email.putExtra(Intent.EXTRA_TEXT, "I will be attending");
        try {
            startActivity(Intent.createChooser(email, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(EventActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

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
