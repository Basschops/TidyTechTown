package com.tt.t.tidytechtowns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class reportDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        //CharSequence[] array = {"Litter", "Dumping","Graffiti", "Chemical spill"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("What are you reporting?")
                .setSingleChoiceItems(R.array.report, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // This method will be invoked when a button in the dialog is clicked.
                    }
                });
        return builder.create();
    }
}
