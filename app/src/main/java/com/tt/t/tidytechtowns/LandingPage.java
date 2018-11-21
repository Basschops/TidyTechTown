package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LandingPage extends AppCompatActivity {

    private MyDatabase mdb;// = new MyDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Check if details have already been entered in database
        mdb = new MyDatabase(this);
        boolean loaded = mdb.loggedIn();
        if(loaded){
            goToHome();
        }
    }


    public void login(View view) {


        EditText password = (EditText)findViewById(R.id.uniqueCode);

        if (password.getText().toString().equals("abc123")) {

            mdb.saveLogIn(password.getText().toString());
            Intent intent = new Intent(LandingPage.this, Welcome.class);
            startActivity(intent);
        } else {

            TextView error = findViewById(R.id.errorMessage);
            error.setVisibility(View.VISIBLE);

        }
        }

    public void SignUp(View view) {
        Intent reg = new Intent(LandingPage.this, Resgistration.class);
        startActivity(reg);
    }

    public void goToHome() {
        Intent reg = new Intent(LandingPage.this, Welcome.class);
        startActivity(reg);
    }
}


