package com.tt.t.tidytechtowns;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


    // nb this code was adapted from the code for the Android tutorial Weather app

    public class CommunityScoresFragment extends Fragment {

        private ArrayAdapter<String> mScoresAdapter;

        public CommunityScoresFragment() {
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


            // Create some dummy data for the ListView.
            String[] commScoresArray = {
                    "Dublin - 200000 points",
                    "Cork - 201 points",
                    "Galway - 202 points",
                    "Sligo - 300 points",
                    "Dublin - 200000 points",
                    "Cork - 201 points",
                    "Galway - 202 points",
                    "Sligo - 300 points",
                    "Dublin - 200000 points",
                    "Cork - 201 points",
                    "Galway - 202 points",
                    "Sligo - 300 points",
                    "Dublin - 200000 points",
                    "Cork - 201 points",
                    "Galway - 202 points",
                    "Sligo - 300 points"

            };
            List<String> weekForecast = new ArrayList<String>(
                    Arrays.asList(commScoresArray));


            mScoresAdapter =
                    new ArrayAdapter<String>(
                            getActivity(), // The current context (this activity)
                            R.layout.list_item_scores, // The name of the layout ID.
                            R.id.list_item_scores_textview, // The ID of the textview to populate.
                            weekForecast);


            ListView listView = (ListView) rootView.findViewById(R.id.listview_scores);
            listView.setAdapter(mScoresAdapter);



            return rootView;

        }
































    }
