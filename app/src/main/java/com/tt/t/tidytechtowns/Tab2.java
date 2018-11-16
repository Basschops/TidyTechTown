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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2, container, false);

        Button b = (Button) rootView.findViewById(R.id.calculate);
        b.setOnClickListener(this);

        TextView plses = (TextView) rootView.findViewById(R.id.pls);
        Resources resources = getResources();
        String tt = resources.getString(R.string.total, 0.0);
        plses.setText(tt);

        return rootView;
    }

    @Override
    public void onClick(View v) {

        Spinner flight_t = (Spinner) getView().findViewById(R.id.flight_type);
        String text = flight_t.getSelectedItem().toString();

        if (text.equals("Domestic")) {

            float f = (float) 0.008;
            calculate_Cf(f);
        }

        else if (text.equals("Short Hall")){

            float f = (float) 0.08;
            calculate_Cf(f);

        }

        else if (text.equals("Long Hall")){

            float f = (float) 0.88;
            calculate_Cf(f);

        }


        EditText trips = (EditText) getView().findViewById(R.id.trips);
        Spinner vehicle_t = (Spinner) getView().findViewById(R.id.vehicle_type);
        Spinner fuel_t = (Spinner) getView().findViewById(R.id.fuel_type);
        EditText milage = (EditText) getView().findViewById(R.id.milage);
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
            String tt = resources.getString(R.string.total, number);
            plses.setText(tt);
        } else {

            String tt = resources.getString(R.string.total, num);
            plses.setText(tt);
            number = num;

        }

    }

}
