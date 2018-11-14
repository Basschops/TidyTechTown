package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DatabaseTesting extends AppCompatActivity
{
    private Cursor bins;
    private boolean lastBin = false;
    private Cursor centers;
    private Cursor communities;
    private MyDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.db_tests);

        db = new MyDatabase(this);
        bins = db.getBins();
        centers = db.getCenters();
        communities = db.getCommunities();
    }

    public void getToast(String o)
    {
        switch(o){
            case "bin":
                String binInfo = "Lat: "+bins.getString(2)+" Long: "+bins.getString(3);
                Toast.makeText(getApplicationContext(), binInfo, Toast.LENGTH_SHORT).show();
                if (bins.isLast())
                {
                    bins.moveToFirst();
                }else
                {
                    bins.moveToNext();
                }
            case "recycle":
                String recInfo = "Shite";
        }
    }
}
