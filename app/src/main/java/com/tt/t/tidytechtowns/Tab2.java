package com.tt.t.tidytechtowns;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Tab2  extends Fragment implements View.OnClickListener {

    public static float number;
    public static float f;
    public static String plses;
    public static String tt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);

        TextView plses = (TextView) rootView.findViewById(R.id.pls);
        Resources resources = getResources();

        if (number == 0.0f) {

            String tt = resources.getString(R.string.total, 0.0);
            plses.setText(tt);

        }
        else {

            String tt = resources.getString(R.string.total, number);
            plses.setText(tt);
        }

        Button b = (Button) rootView.findViewById(R.id.calculate);
        b.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);
        outState.putFloat("num", number);

    }

    @Override
    public void onClick(View v) {

        //dealing with values from the spinners first and they can be used with other values.
        Spinner flight_t = (Spinner) getView().findViewById(R.id.flight_type);
        String text = flight_t.getSelectedItem().toString();

        if (text.equals("Domestic")) {

           f = (float) 0.008;

        }

        else if (text.equals("Short Hall")){

         f = (float) 0.08;


        }

        else if (text.equals("Long Hall")){

            f = (float) 0.88;

        }

        //calculating flight information!

        EditText trips = (EditText) getView().findViewById(R.id.trips);
        int trip = Integer.parseInt(trips.getText().toString());
        float flightDetails = trip * f;
        calculate_Cf(flightDetails);

        //calculations based on http://ecoscore.be/en/info/ecoscore/co2
        // and also http://www.irishevowners.ie/useful-info/how-green-are-electric-cars/

        Spinner vehicle_t = (Spinner) getView().findViewById(R.id.vehicle_type);
        String veh = vehicle_t.getSelectedItem().toString();



        Spinner fuel_t = (Spinner) getView().findViewById(R.id.fuel_type);
        String ft = vehicle_t.getSelectedItem().toString();

        if (ft.equals("Diesel")) {

            f = (float) 2640;
        }

        else if (ft.equals("Petrol")){

            f = (float) 2392;

        }

        else if (ft.equals("Hybrid")){

            f = (float) 330;

        }

        else if (ft.equals("Electric")){

            f = (float) 55;

        }

        EditText milage = (EditText) getView().findViewById(R.id.milage);

        //FORMULA - LITRE PER 1KM X GRAMMES PER GALLON AND DIVIDED BY 100KM.

        //FOR PUBLIC TRANSPORT

        //https://www.theguardian.com/environment/datablog/2009/sep/02/carbon-emissions-per-transport-type

        EditText bus = (EditText) getView().findViewById(R.id.bus);
        EditText rail = (EditText) getView().findViewById(R.id.rail);
        EditText luas = (EditText) getView().findViewById(R.id.luas);
//
//        TextView plses = (TextView) getView().findViewById(R.id.pls);
//        Resources resources = getResources();
//        String tt = resources.getString(R.string.total, 0.1);
//        plses.setText(tt);
    }

    public void calculate_Cf(float num) {
//        //do some calculations

        TextView plses = (TextView) getView().findViewById(R.id.pls);
        Resources resources = getResources();

        if (number != 0.0f) {

            number = number + num;
             tt = resources.getString(R.string.total, number);
            plses.setText(tt);

        } else {

             tt = resources.getString(R.string.total, num);
            plses.setText(tt);
            number = num;

        }

    }



}
