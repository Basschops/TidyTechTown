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
import android.widget.TextView;

// Calculates carbon footprint due to life at home
public class Tab1 extends Fragment implements View.OnClickListener {

    public interface t1dbListener {
        double dbaccess();
        void writeHomeScore(double home);
    }

    // Interface listener to pass info from fragment to database
    t1dbListener mListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (t1dbListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement reportDialogListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1, container, false);

        TextView plses = rootView.findViewById(R.id.pls1);
        Resources resources = getResources();
        int cc_score = (int) Math.round(mListener.dbaccess());
        if (cc_score == 0.0f) {
            String tt = "Enter details";
            plses.setText(tt);
        }
        else {
            String tt = resources.getString(R.string.total, cc_score);
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

        double carbon_number = 0;

        if (!(elec.getText().toString().matches(""))){
           int electricity = Integer.parseInt(elec.getText().toString());
            //calculations coming from carbon calculator.com
           float e = (float) ((electricity * 0.442 )/1000);
           carbon_number+=e;
        }

        if (!(heat.getText().toString().matches(""))) {

            int heating = Integer.parseInt(heat.getText().toString());
            //calculations coming from carbon calculator.com
            float h = (float) (heating/ 315.0);
            carbon_number+=h;
        }

        if (!(coal.getText().toString().equals(""))){
            int coaleen = Integer.parseInt(coal.getText().toString());
            float c = (float) (coaleen * 2.88166667);
            carbon_number+=c;
        }

        //based on calculations from https://www.carbonfootprint.com/calculator.aspx
        if (!(phone.getText().toString().matches("")) ){
            int phone_amt = Integer.parseInt(phone.getText().toString());
            float p_amt = (float)(phone_amt / 1666.00);
            carbon_number+=p_amt;
        }

        if (!(food.getText().toString().matches(""))) {
            int food_amt = Integer.parseInt(food.getText().toString());
            float amt = (float) (food_amt / 1831.00);
            carbon_number+=amt;
        }

        if (!(rec.getText().toString().matches(""))){
            int rec_amt = Integer.parseInt(rec.getText().toString());
            float re_amt = (float)(rec_amt /3571.00);
            carbon_number+=re_amt;
        }

        // Return new value in text view if greater than zero
        if(carbon_number>0) {
            mListener.writeHomeScore(carbon_number);
            Resources resources = getResources();
            TextView plses = getView().findViewById(R.id.pls1);

            int total_cc_score = (int) Math.round(mListener.dbaccess());
            String tt = resources.getString(R.string.total, total_cc_score);
            plses.setText(tt);
        }
    }
}


