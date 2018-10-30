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
import android.widget.RelativeLayout;
import android.widget.Toast;

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




        Event event = new Event(Color.YELLOW, 1540944000000L, "Event event");
        Event event2 = new Event(Color.YELLOW, 1541116800000L, "Event event");

        Event event3 = new Event(Color.YELLOW, 1541289600000L, "Event event");


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


    }

}// end class
