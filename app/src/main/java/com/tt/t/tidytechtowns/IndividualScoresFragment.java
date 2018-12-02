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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// this code was adapted from the code for the Android tutorial Weather app

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

        List<String> outputArray = new ArrayList<String>();
        ArrayList<individual> individuals = new ArrayList<individual>();

        results.moveToFirst();
        while (results.isAfterLast() == false)  {
            individuals.add(new individual(results.getInt(3),results.getString(1)));

            results.moveToNext();
        }

        // Add user to the list
        int userScore = (int) Math.round(db.returnScore());
        if(userScore<0){userScore=0;}
        db.close();
        individual user = new individual(userScore, "You");
        individuals.add(user);

        Collections.sort(individuals, new SortIbyscore());

        for(individual i:individuals){
            outputArray.add(i.toString());
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

// Class for storing individuals and scores
class individual{
    int score;
    String name;

    public individual(int score, String name)
    {
        this.score = score;
        this.name = name;
    }

    public String toString()
    {
        return this.name + ":  " + this.score;
    }
}

// Sorts individuals based on their score
class SortIbyscore implements Comparator<individual>
{
    // Used for sorting in descending order of score
    public int compare(individual a, individual b)
    {
        return b.score - a.score;
    }
}