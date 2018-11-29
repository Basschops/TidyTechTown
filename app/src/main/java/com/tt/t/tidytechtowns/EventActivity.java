package com.tt.t.tidytechtowns;

// NB the code in this module is adapted from the android calendar
// library at https://github.com/SundeepK/CompactCalendarView

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class EventActivity extends AppCompatActivity {

    // variables needed for navigation drawer
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private MyDatabase db;
    private Cursor eventCursor;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    Date d = new Date();
    private String eventClicked;



    // initialises activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // create calendar
        final CompactCalendarView calendar;

        // create actionbar
        final ActionBar actionbar = getSupportActionBar();

        // enable 'up' navigation and put month name in actionbar
        actionbar.setDisplayHomeAsUpEnabled(false);
        actionbar.setTitle(dateFormatMonth.format(d));

        // finish creating calendar object
        calendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        calendar.setUseThreeLetterAbbreviation(true);

        // create db connection
        db = new MyDatabase(this);

        // get events from db table 'events'
        eventCursor = db.getEvents();

        List<String> outputArray = new ArrayList<String>();

        // iterate over results from call to 'events' table
        // and create an event object for each event. Then
        // add each object to calendar
        eventCursor.moveToFirst();
        while (eventCursor.isAfterLast() == false)  {

            String anEvent = eventCursor.getString(1);
            long date = eventCursor.getLong(2);

            Event e1 = new Event(Color.YELLOW, date, anEvent);
            calendar.addEvent(e1, true);
            eventCursor.moveToNext();
        }

        // define a listener to receive callbacks when certain events happen.
        // In this case, when user clicks on day, get date and check if it matches
        // any events in db for that month. If so, display event info in toast, if not
        // toast says 'nothing on today'
        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();

                long millisecond = dateClicked.getTime();
                boolean nothingOn = true;

                // list of events for selected month
                List <Event> monthEvents = calendar.getEventsForMonth(millisecond);

                // check if day selected matches date of any event in month events list
                for (int i = 0; i <= monthEvents.size()-1; i++) {

                    Event ev = monthEvents.get(i);
                    Long thisdate = ev.getTimeInMillis();

                    if (millisecond == thisdate) {
                        // print event description in toast
                        String thisevent = ev.getData().toString();
                        // Get date in readable format
                        String[] date = dateClicked.toString().split(" ");

                        AlertDialog alertDialog = new AlertDialog.Builder(EventActivity.this).create();
                        alertDialog.setTitle(date[0]+" "+date[1]+" "+date[2]);
                        alertDialog.setMessage(thisevent);
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                        nothingOn = false;
                        eventClicked = thisevent;
                    }
                }

                if (nothingOn) {
                    Toast.makeText(context, "Nothing on today", Toast.LENGTH_SHORT).show();
                }
            } // end onDayClick

            // highlight first day of month when user moves to new month
            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                actionbar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });

        // create DrawerLayout object for navigation menu
        dl = (DrawerLayout) findViewById(R.id.activity_main);
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
    } // end onCreate

    // respond to items chosen in navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    // create email activity - will generate email to organiser with header/subject/body filled out
    // name of event held on day selected will appear in email title and body.
    public void notifyAttending(View v) {
        if (eventClicked!=null) {
            // create new email and fill out fields
            Intent email = new Intent(Intent.ACTION_SEND);
            email.setData(Uri.parse("mailto:"));
            email.setType("message/rfc822");
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{"sheena.davitt@ucdconnect.ie"});
            email.putExtra(Intent.EXTRA_SUBJECT, "I wish to attend the " + eventClicked);
            email.putExtra(Intent.EXTRA_TEXT, "Dear organiser, I will be attending " + eventClicked);

            try {
                startActivity(Intent.createChooser(email, "Send mail..."));
            }
            catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(EventActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

            eventClicked=null;
        }
        // if user has not actually chosen event, display toast to notify them of this
        else Toast.makeText(EventActivity.this, "You have not selected an event", Toast.LENGTH_SHORT).show();
    }


    // start Scores activity
    public void startScores(View v) {
        Intent intent = new Intent(EventActivity.this, ScoresActivity.class);
        startActivity(intent);
    }

    // start Event activity
    public void startEventCalendar(View v) {
        Intent intent = new Intent(EventActivity.this, EventActivity.class);
        startActivity(intent);
    }

    // start Carbon activity
    public void startCarbon(View v) {
        Intent intent = new Intent(EventActivity.this, Carbon.class);
        startActivity(intent);
    }

    // start Maps activity
    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }

    // start plogging activity
    public void startPlogging(View v) {
        Intent intent = new Intent(EventActivity.this, Plogging.class);
        startActivity(intent);
    }

}// end class
