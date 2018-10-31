package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

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
    }


    public void startScores(View v) {
        Intent intent = new Intent(Welcome.this, ScoresActivity.class);
        startActivity(intent);
    }

    public void startCarbonFootPrintCalculator(View v) {
        Intent intent = new Intent(Welcome.this, CarbonFootprint.class);
        startActivity(intent);
    }





    public void startEventCalendar(View v) {
        Intent intent = new Intent(Welcome.this, EventActivity.class);
        startActivity(intent);
    }

}












