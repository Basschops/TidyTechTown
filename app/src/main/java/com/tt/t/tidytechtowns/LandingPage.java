package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LandingPage extends AppCompatActivity {

    private MyDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        // Check if details have already been entered in database
        // If so proceed to home screen
        mdb = new MyDatabase(this);
        boolean loaded = mdb.loggedIn();
        if(loaded){
            goToHome();
        }
    }

    // Receive log in details
    public void login(View view) {
        EditText password = (EditText)findViewById(R.id.uniqueCode);

        // Check login is valid. If so save to DB
        if (password.getText().toString().equals("abc123")) {
            mdb.saveLogIn(password.getText().toString());
            Intent intent = new Intent(LandingPage.this, Welcome.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), R.string.loginError,
                    Toast.LENGTH_SHORT).show();
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


