package com.tt.t.tidytechtowns;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ScoresActivity extends AppCompatActivity {

    private ListView mDrawerList2;
    private ArrayAdapter<String> mAdapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        mDrawerList2 = (ListView)findViewById(R.id.navList);
        addDrawerItems();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addDrawerItems() {
        String[] osArray = { "Carbon calculator", "Join", "My rankings", "Town rankings", "Maps"};
        mAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList2.setAdapter(mAdapter2);
    }


} // end class
