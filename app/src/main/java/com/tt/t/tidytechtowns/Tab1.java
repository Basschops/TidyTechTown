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

import static java.lang.Float.parseFloat;

public class Tab1 extends Fragment implements View.OnClickListener {

    public static float number;
    public static float previous_val;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1, container, false);

        Button b = (Button) rootView.findViewById(R.id.button5);
        b.setOnClickListener(this);

        TextView plses = (TextView) rootView.findViewById(R.id.pls);
        Resources resources = getResources();

        String tt = resources.getString(R.string.total, 0.0);
        plses.setText(tt);

        return rootView;

    }


    @Override
    public void onClick(View v) {

        //tab 1 values
        EditText elec = (EditText)getView().findViewById(R.id.electricity);
        EditText heat = (EditText)getView().findViewById(R.id.heating);
        EditText coal = (EditText)getView().findViewById(R.id.coal);
        //tab 2 values
//        Spinner flight_t = (Spinner)getView().findViewById(R.id.flight_type);
//        EditText trips = (EditText)getView().findViewById(R.id.trips);
//        Spinner vehicle_t = (Spinner)getView().findViewById(R.id.vehicle_type);
//        Spinner fuel_t = (Spinner)getView().findViewById(R.id.fuel_type);
//        EditText milage = (EditText)getView().findViewById(R.id.milage);
//        EditText bus = (EditText)getView().findViewById(R.id.bus);
//        EditText rail = (EditText)getView().findViewById(R.id.rail);
//        EditText luas = (EditText)getView().findViewById(R.id.luas);
//        //tab 3 values
//        EditText phone = (EditText)getView().findViewById(R.id.phone);
//        EditText food = (EditText)getView().findViewById(R.id.food);
//        EditText rec = (EditText)getView().findViewById(R.id.recreational);


        int heating = Integer.parseInt(heat.getText().toString());
        Float h = parseFloat(String.valueOf(heating));
        int electricity = Integer.parseInt(elec.getText().toString());
        Float e = parseFloat(String.valueOf(electricity));
        int coaleen = Integer.parseInt(coal.getText().toString());
        Float c = parseFloat(String.valueOf(coaleen));
        calculate_Cf(h);
        calculate_Cf(e);
        calculate_Cf(c);

//        int trip = Integer.parseInt(trips.getText().toString());
//        Float t = parseFloat(String.valueOf(trip));
//        int milages = Integer.parseInt(milage.getText().toString());
//        Float m = parseFloat(String.valueOf(milages));
//        int buses = Integer.parseInt(bus.getText().toString());
//        Float b = parseFloat(String.valueOf(buses));
//        int rails = Integer.parseInt(rail.getText().toString());
//        Float r = parseFloat(String.valueOf(rails));
//        int luass = Integer.parseInt(luas.getText().toString());
//        Float l = parseFloat(String.valueOf(luass));
//        int phones = Integer.parseInt(phone.getText().toString());
//        Float p = parseFloat(String.valueOf(phones));
//        int foods = Integer.parseInt(food.getText().toString());
//        Float f = parseFloat(String.valueOf(foods));
//        int recs = Integer.parseInt(rec.getText().toString());
//        Float re = parseFloat(String.valueOf(recs));



    }

        public void calculate_Cf(float num) {
//        //do some calculations
            TextView plses = (TextView) getView().findViewById(R.id.pls);
            Resources resources = getResources();



            if (number != 0.0f){

                number = number + num;
                String tt = resources.getString(R.string.total, number);
                plses.setText(tt);

            }
            else {

                String tt = resources.getString(R.string.total, num/1000);
                plses.setText(tt);
                number = num;



            }

   }

}


