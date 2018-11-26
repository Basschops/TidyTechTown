package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tt.t.tidytechtowns.weatherModels.Weather;

import org.json.JSONException;

import static java.lang.String.format;

public class Welcome extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private TextView cityText;
    private TextView desc;
    private TextView temp;
    private TextView tempMin;
    private TextView tempMax;
    private TextView direct;
    private TextView speed;
    private TextView progress;
    private TextView appTemp;

    private ProgressBar humid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);





        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        nv = findViewById(R.id.nv);
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

        String city = "Dublin,ie";

        cityText = findViewById(R.id.cityText);
        desc = findViewById(R.id.desc);
        temp = findViewById(R.id.temp);
        tempMin = findViewById(R.id.tempMin);
        tempMax = findViewById(R.id.tempMax);
        direct = findViewById(R.id.direct);
        speed = findViewById(R.id.speed);
        humid = findViewById(R.id.progressBar);
        progress = findViewById(R.id.progress);
        appTemp = findViewById(R.id.appTemp);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(city);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }





    public void startScores(View v) {
        Intent intent = new Intent(Welcome.this, ScoresActivity.class);
        startActivity(intent);
    }



    public void startEventCalendar(View v) {
        Intent intent = new Intent(Welcome.this, EventActivity.class);
        startActivity(intent);
    }

    public void startCarbon(View v) {
        Intent intent = new Intent(Welcome.this, Carbon.class);
        startActivity(intent);
    }


    public void startMaps(View v) {
        Intent i = new Intent(getBaseContext(), MapsActivity.class);
        startActivity(i);
    }
    public void startPlogging(View v) {
        Intent intent = new Intent(Welcome.this, Plogging.class);
        startActivity(intent);
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = new WeatherHTTPClient().getWeatherData(params[0]);

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                String icon = weather.currentCondition.getIcon();
                weather.iconData = new WeatherHTTPClient().getImage(icon);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }



        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

//            if (weather.iconData != null && weather.iconData.length > 0) {
//                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
//                imgView.setImageBitmap(img);
//            }

            cityText.setText(format("%s,%s", weather.location.getCity(), weather.location.getCountry()));
            desc.setText(format("%s(%s)", weather.currentCondition.getCondition(), weather.currentCondition.getDescr()));
            temp.setText(format("%d\u00b0", Math.round(weather.temperature.getTemp() - 273.15)));
            tempMin.setText(format("%d\u00b0", Math.round(weather.temperature.getMinTemp() - 273.15)));
            tempMax.setText(format("%d\u00b0", Math.round(weather.temperature.getMaxTemp() - 273.15)));
            speed.setText(format("%1.2f m/s", weather.wind.getSpeed()));
            float humidity = weather.currentCondition.getHumidity();
            humid.setProgress(Integer.parseInt(format("%d", Math.round(humidity))));
            progress.setText(format("%1.0f%%", humidity));
            if (humidity >= 55f){
                progress.setTextColor(Color.parseColor("#FFFFFF"));
            }
            float windAngle = weather.wind.getDeg();
            if (windAngle > 337.5f || windAngle <= 22.5f){
                direct.setText("North");
            } else if (windAngle > 22.5f || windAngle <= 67.5f){
                direct.setText("North East");
            } else if (windAngle > 67.5f || windAngle <= 112.5f){
                direct.setText("East");
            } else if (windAngle > 112.5f || windAngle <= 157.5f){
                direct.setText("South East");
            } else if (windAngle > 157.5f || windAngle <= 202.5f){
                direct.setText("South");
            } else if (windAngle > 202.5f || windAngle <= 247.5){
                direct.setText("South West");
            } else if (windAngle > 247.5f || windAngle <= 292.5f){
                direct.setText("West");
            } else if (windAngle > 292.5f || windAngle <= 337.5f){
                direct.setText("North West");
            }

//            float tempC = weather.temperature.getTemp() - 273.15f;
//            float wSpeed = weather.wind.getSpeed();
//            float appTempC = tempC;
//            if (tempC <= 10.5d){
//                appTempC = (float) (13.12f + 0.6215f*tempC - 11.37f*(Math.pow(wSpeed, 0.16f))+0.3965f*tempC*(Math.pow(wSpeed, 0.16f)));
//            } else if (tempC >= 26.66d){
//                float tempF = tempC*1.8f + 32;
//                float appTempF = (float) (42.38 + (2.05*tempF) + (10.14*humidity) - (0.22*tempF*humidity)
//                        - (6.83*Math.pow(10, -3)*Math.pow(tempF, 2))
//                        - (5.48*Math.pow(10, -2)*Math.pow(humidity, 2))
//                        + (1.23*Math.pow(10d, -3)*Math.pow(tempF, 2)*humidity)
//                        + (8.53*Math.pow(10d, -4)*tempF*Math.pow(humidity, 2))
//                        - (1.99*Math.pow(10d, -6)*Math.pow(tempF, 2)*Math.pow(humidity, 2)));
//                appTempC = 0.5556f*(appTempF-32f);
//            }
//            appTemp.setText(format("%1.2f\u00b0", appTempC));
        }
    }

}












