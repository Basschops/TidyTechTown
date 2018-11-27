package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class showRandomCode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_random_code);

        TextView show_code = (TextView) findViewById(R.id.showCode);
        TextView show_name = (TextView) findViewById(R.id.registratName);
        TextView show_Comm_name = (TextView) findViewById(R.id.communityName);

        show_code.setText(getUniqueCode());
        show_name.setText(getName());
        show_Comm_name.setText(getCommunityName());
    }

    protected String getUniqueCode(){
        Intent i = getIntent();
        String unique = i.getStringExtra("code");
        return unique;
    }

    protected String getName() {
        Intent i = getIntent();
        String name  = i.getStringExtra("firstN");
        return name;
    }

    protected String getCommunityName() {
        Intent i = getIntent();
        String name  = i.getStringExtra("comm");
        return name;
    }
}
