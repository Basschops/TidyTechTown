package com.tt.t.tidytechtowns;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// nb this code was adapted from the code for the Android tutorial Weather app

public class IndividualScoresFragment extends Fragment {

    private ArrayAdapter<String> mScoresAdapter;
    private MyDatabase db;
    private Cursor results;

    public IndividualScoresFragment() {
    }

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        //Allow the fragement to access menu items.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu items for use in the action bar
        inflater.inflate(R.menu.scoresfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_scores, container, false);

        TextView helloTextView = rootView.findViewById(R.id.textView3);
        helloTextView.setText(R.string.yourRank);

        db = new MyDatabase(getActivity());
        results = db.getIndScores();
        int number = results.getCount();

        List<String> outputArray = new ArrayList<String>();

        results.moveToFirst();
        while (results.isAfterLast() == false)  {

            String output = "";
            String id = results.getString(0);
            String name = results.getString(1);
            String community = results.getString(2);
            int score = results.getInt(3);
            String stringscore = Integer.toString(score);

            output +=  name + ":  ";
            output +=  stringscore;
            outputArray.add(output);
            results.moveToNext();
        }

        mScoresAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.list_item_scores, // The name of the layout ID.
                        R.id.list_item_scores_textview, // The ID of the textview to populate.
                        outputArray);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_scores);
        listView.setAdapter(mScoresAdapter);

        return rootView;
    }
}


