package com.tt.t.tidytechtowns;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    public interface t2dbListener {
        double dbaccess2();
        void writeTravelScore(double home);
    }
    t2dbListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (Tab2.t2dbListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement reportDialogListener");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Use the same key you used before to retrieve the data.
        View rootView = inflater.inflate(R.layout.tab2, container, false);
        TextView plses = (TextView) rootView.findViewById(R.id.pls1);
        Resources resources = getResources();

        int cc_score = (int) Math.round(mListener.dbaccess2());
        if (cc_score == 0.0f) {
            String tt = "Enter details";
            plses.setText(tt);
        }
        else {
            String tt = resources.getString(R.string.total, cc_score);
            plses.setText(tt);
        }

        Button b = (Button) rootView.findViewById(R.id.calculate);
        b.setOnClickListener(this);

        return rootView;
    }



    @Override
    public void onClick(View v) {
        double carbon_number = 0;

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
            carbon_number+=flightDetails;
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
            carbon_number+=Vehicle;
        }

        //FOR PUBLIC TRANSPORT
        //https://www.theguardian.com/environment/datablog/2009/sep/02/carbon-emissions-per-transport-type

        //Bus Travel
        EditText bus = (EditText) getView().findViewById(R.id.bus);

        if (!(bus.getText().toString().matches(""))) {
            int bus_travel = Integer.parseInt(bus.getText().toString());
            int bus_c02 = bus_travel / 10000;
            carbon_number+=bus_c02;
        }
        //Rail Travel
        EditText rail = (EditText) getView().findViewById(R.id.rail);
        if (!(rail.getText().toString().matches(""))) {
            int rail_travel = Integer.parseInt(rail.getText().toString());
            int rail_c02 = rail_travel / 10000;
            carbon_number+=rail_c02;
        }
        //Luas Travel
        EditText luas = (EditText) getView().findViewById(R.id.luas);
        if (!(luas.getText().toString().matches(""))) {
            int luas_travel = Integer.parseInt(luas.getText().toString());
            int luas_c02 = luas_travel / 10000;
            carbon_number+=luas_c02;
        }

        // Return new value in text view if greater than zero
        if(carbon_number>0) {
            TextView plses = (TextView) getView().findViewById(R.id.pls1);
            Resources resources = getResources();
            mListener.writeTravelScore(carbon_number);
            int total_cc_score = (int) Math.round(mListener.dbaccess2());
            tt = resources.getString(R.string.total, total_cc_score);
            plses.setText(tt);
        }
    }
}

