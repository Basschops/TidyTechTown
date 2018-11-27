package com.tt.t.tidytechtowns;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class Resgistration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgistration);
    }

    public void GeneratingRandomString(View view) {
        EditText communityName = findViewById(R.id.ComName);
        EditText AddLine1 = findViewById(R.id.AddressLine1);
        EditText AddLine2 = findViewById(R.id.AddressLine2);
        EditText fName = findViewById(R.id.regFirstName);
        TextView sName = findViewById(R.id.regSecondName);
        String Add1 = AddLine1.getText().toString();
        String comm = communityName.getText().toString();
        String Add2 = AddLine2.getText().toString();
        String firstN = fName.getText().toString();
        String secName = sName.getText().toString();

        if (comm.matches("") || Add1.matches("") || firstN.matches("") || secName.matches("") || Add2.matches("")) {
            Toast.makeText(this, "Error, please ensure all inputs have been provided", Toast.LENGTH_SHORT).show();
            return;
        }

        // Code to randomly generate code for new community
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        Intent i = new Intent(this, showRandomCode.class);
        i.putExtra("code", generatedString);
        i.putExtra("comm", comm);
        i.putExtra("firstN", firstN);
        startActivity(i);
    }
}
