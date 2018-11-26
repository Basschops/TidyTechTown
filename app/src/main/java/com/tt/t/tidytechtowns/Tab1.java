package com.tt.t.tidytechtowns;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Tab1 extends Fragment implements View.OnClickListener {

    public static float number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1, container, false);

        TextView plses = rootView.findViewById(R.id.pls);
        Resources resources = getResources();

        if (number == 0.0f) {
            String tt = resources.getString(R.string.total, 0.0);
            plses.setText(tt);
        }
        else {
            String tt = resources.getString(R.string.total, number);
            plses.setText(tt);
        }

        Button b = rootView.findViewById(R.id.carbon1Btn);
        b.setOnClickListener(this);

        return rootView;
    }

    // Function for calculate button
    @Override
    public void onClick(View v) {

        //tab 1 values
        EditText elec = getView().findViewById(R.id.electricity);
        EditText heat = getView().findViewById(R.id.heating);
        EditText coal = getView().findViewById(R.id.coal);
        EditText phone = getView().findViewById(R.id.phone);
        EditText food = getView().findViewById(R.id.food);
        EditText rec = getView().findViewById(R.id.recreational);

        // Booleans to check if values have been entered/changed
        boolean electiricity_used = false;
        boolean heating_used = false;
        boolean coal_used = false;
        boolean phone_used = false;
        boolean food_used = false;
        boolean rec_used = false;

        if (!(elec.getText().toString().matches("")) && !electiricity_used){
           int electricity = Integer.parseInt(elec.getText().toString());
            //calculations coming from carbon calculator.com
           float e = (float) ((electricity * .442 )/1000);
           calculate_Cf(e);
           electiricity_used = true;
        }

        if (!(heat.getText().toString().matches("")) && !heating_used) {

            int heating = Integer.parseInt(heat.getText().toString());
            //calculations coming from carbon calculator.com
            float h = (float) (heating/ 315.0);
            calculate_Cf(h);
            heating_used = true;
        }

        if (!(coal.getText().toString().equals("")) && !coal_used){
            int coaleen = Integer.parseInt(coal.getText().toString());
           float c = (float) (coaleen * 2.88166667);
            calculate_Cf(c);
            coal_used = true;
        }

        //based on calculations from https://www.carbonfootprint.com/calculator.aspx
        if (!(phone.getText().toString().matches("")) && !phone_used){

            int phone_amt = Integer.parseInt(phone.getText().toString());
            float p_amt = (float)(phone_amt / 1666.00);

            calculate_Cf(p_amt);
        }

        if (!(food.getText().toString().matches("")) && !food_used) {

            int food_amt = Integer.parseInt(food.getText().toString());
            float amt = (float) (food_amt / 1831.00);
            calculate_Cf(amt);
            phone_used = true;

        }

        if (!(rec.getText().toString().matches("")) && !rec_used){
            int rec_amt = Integer.parseInt(rec.getText().toString());
            float re_amt = (float)(rec_amt /3571.00);
            calculate_Cf(re_amt);
            rec_used = true;
        }

    }

    // Function that refreshes results
    public void calculate_Cf(float num) {
        TextView plses = getView().findViewById(R.id.pls);
        Resources resources = getResources();

        if (number != 0.0f){
            number = number + num;
            String tt = resources.getString(R.string.total, number);
            plses.setText(tt);
        }
        else {
            String tt = resources.getString(R.string.total, num);
            plses.setText(tt);
        }
   }
}


