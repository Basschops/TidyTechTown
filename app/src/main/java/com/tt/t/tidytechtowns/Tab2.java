package com.tt.t.tidytechtowns;
import android.content.res.Resources;
import android.support.annotation.Nullable;
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

    public static float number2;
    public static float f;
    public static float ftype;
    public static float vtype;
    public static String tt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Use the same key you used before to retrieve the data.
        View rootView = inflater.inflate(R.layout.tab2, container, false);
        TextView textview = rootView.findViewById(R.id.communicationaaa);
        TextView plses = (TextView) rootView.findViewById(R.id.pls);
        Resources resources = getResources();


        if (number2 == 0.0f) {

            String tt = resources.getString(R.string.total, 0.0);
            plses.setText(tt);

        }
        else {

            String tt = resources.getString(R.string.total, number2);
            plses.setText(tt);
        }

        Button b = (Button) rootView.findViewById(R.id.calculate);
        b.setOnClickListener(this);

        return rootView;
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

        //making sure that there is a flight type select.
        if (!(trips.getText().toString().matches("")) && !(text.equals("Select")))  {

            int trip = Integer.parseInt(trips.getText().toString());
            float flightDetails = trip * f;
            calculate_Cf(flightDetails);
        }


        //calculations based on http://ecoscore.be/en/info/ecoscore/co2
        // and also http://www.irishevowners.ie/useful-info/how-green-are-electric-cars/

        Spinner vehicle_t = (Spinner) getView().findViewById(R.id.vehicle_type);
        String veh = vehicle_t.getSelectedItem().toString();

        //checks if a vehicle type has been selected, if not. Sets the bad boy to zero.


        if (veh.equals("Car")) {

            vtype = (float) 6.0 ;//kgs
        }

        else if (veh.equals("Van")){

            vtype = (float) 7.5; //kgs

        }

        else if (veh.equals("Jeep")){

            vtype = (float) 9.0;

        }

        else if (veh.equals("Motorbike")){

            vtype = (float) 7.0;

        }


        Spinner fuel_t = (Spinner) getView().findViewById(R.id.fuel_type);
        String ft = vehicle_t.getSelectedItem().toString();

        //checks if a fuel type has been selected, if not. Sets the bad boy to zero.

        if (ft.equals("Select")) {

            ftype = (float) 0;

        }

        if (ft.equals("Diesel")) {

            ftype = (float) 2.640 ;//kgs
        }

        else if (ft.equals("Petrol")){

            ftype = (float) 2.392; //kgs

        }

        else if (ft.equals("Hybrid")){

            ftype = (float) 0.330;

        }

        else if (ft.equals("Electric")){

            ftype = (float) 0.055;

        }

        EditText milage = (EditText) getView().findViewById(R.id.milage);

        if (!(milage.getText().toString().matches(""))&& !(veh.equals("Select")) && !(ft.equals("Select"))) {

            int kms = Integer.parseInt(milage.getText().toString());

            //FORMULA - (milaeg x consumption / 100) x fuel type.
            //https://comcar.co.uk/emissions/footprint/

            float Vehicle = (kms * vtype / 100) * ftype;

            calculate_Cf(Vehicle);

        }

        //FOR PUBLIC TRANSPORT
        //https://www.theguardian.com/environment/datablog/2009/sep/02/carbon-emissions-per-transport-type

        //Bus Travel
        EditText bus = (EditText) getView().findViewById(R.id.bus);

        if (!(bus.getText().toString().matches(""))) {
            int bus_travel = Integer.parseInt(bus.getText().toString());
            int bus_c02 = bus_travel / 10000;
            calculate_Cf(bus_c02);

        }

        //Rail Travel
        EditText rail = (EditText) getView().findViewById(R.id.rail);

        if (!(rail.getText().toString().matches(""))) {
            int rail_travel = Integer.parseInt(rail.getText().toString());
            int rail_c02 = rail_travel / 10000;
            calculate_Cf(rail_c02);
        }

        //Luas Travel
        EditText luas = (EditText) getView().findViewById(R.id.luas);

        if (!(luas.getText().toString().matches(""))) {

            int luas_travel = Integer.parseInt(luas.getText().toString());
            int luas_c02 = luas_travel / 10000;
            calculate_Cf(luas_c02);
        }
    }

    public void calculate_Cf(float num) {
//        //do some calculations

        TextView plses = (TextView) getView().findViewById(R.id.pls);
        Resources resources = getResources();

        if (number2 != 0.0f) {

            number2 = number2 + num;
             tt = resources.getString(R.string.total, number2);
            plses.setText(tt);

        } else {

            tt = resources.getString(R.string.total, num);
            plses.setText(tt);
            number2 = num;

        }

    }


}

