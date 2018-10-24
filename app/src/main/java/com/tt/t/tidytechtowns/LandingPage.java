package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

public class LandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

    }


    public void login(View view) {


        EditText password = (EditText)findViewById(R.id.uniqueCode);

        if (password.getText().toString().equals("abc123")) {

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
    }


