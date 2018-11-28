package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

    private ImageView splash;


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
        splash = findViewById(R.id.imageView);

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
            String condition = weather.currentCondition.getCondition();
            switch (condition){
                case "Rain":
                case "Drizzle":
                case "Thunderstorm":
                    splash.setImageResource(R.drawable.rain);
                    break;
                case "Snow":
                    splash.setImageResource(R.drawable.snow);
                    break;
                case "Clouds":
                    splash.setImageResource(R.drawable.clouds);
                    break;
                case "Clear":
                    splash.setImageResource(R.drawable.sun);
                    break;
                default:
                    splash.setImageResource(R.drawable.clouds);
                    break;
            }

        }
    }

}












