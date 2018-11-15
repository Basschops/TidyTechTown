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

        TextView plses = (TextView) getView().findViewById(R.id.pls);
        Resources resources = getResources();

        EditText elec = (EditText) getView().findViewById(R.id.electricity);
        int electricity = Integer.parseInt(elec.getText().toString());
        Float x = parseFloat(String.valueOf(electricity));
        String tt = resources.getString(R.string.total, x);
        plses.setText(tt);

    }

}


