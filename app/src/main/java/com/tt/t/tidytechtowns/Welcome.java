package com.tt.t.tidytechtowns;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Welcome extends AppCompatActivity {

    private Cursor bins;
    private boolean lastBin = false;
    private Cursor centers;
    private Cursor communities;
    private MyDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button button=(Button)findViewById(R.id.mapEnterBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(), MapsActivity.class);
                startActivity(i);
            }
        });
        db = new MyDatabase(this);
        bins = db.getBins();
    }


    public void startScores(View v) {
        Intent intent = new Intent(Welcome.this, ScoresActivity.class);
        startActivity(intent);
    }


    public void startEventCalendar(View v) {
        Intent intent = new Intent(Welcome.this, EventActivity.class);
        startActivity(intent);
    }

    public void binButton(View v)
    {
        String binInfo = "Lat: "+bins.getString(2)+" Long: "+bins.getString(3);
        Toast.makeText(getApplicationContext(), binInfo, Toast.LENGTH_SHORT).show();
        if (bins.isLast())
        {
            bins.moveToFirst();
        }else
        {
            bins.moveToNext();
        }


    }

    public void recycleButton(View v)
    {
        centers = db.getCenters();
    }

    public void communityButton(View v)
    {
        communities = db.getCommunities();

    }
}












