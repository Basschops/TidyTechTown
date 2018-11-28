package com.tt.t.tidytechtowns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;

import java.util.ArrayList;

// Dialog box that checks what type of event to report
public class reportDialog extends AppCompatDialogFragment {

    public interface reportDialogListener {
        void reportPositiveClick(reportDialog dialog, String response);
        void reportNegativeClick(reportDialog dialog);
    }

    // Use this instance of the interface to deliver action events
    reportDialogListener mListener;

    // Adapted from https://developer.android.com/guide/topics/ui/dialogs
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (reportDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement reportDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final ArrayList response = new ArrayList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.reoprtTitle)
                .setSingleChoiceItems(R.array.report, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // This method will be invoked when a button in the dialog is clicked.
                        String[] array = getResources().getStringArray(R.array.report);
                        response.add(0, array[i]);
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Protect against case if none selected.
                if(response.isEmpty()){ mListener.reportPositiveClick(reportDialog.this, null);
                return;}
                String r = (String) response.get(0);
                mListener.reportPositiveClick(reportDialog.this, r);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.reportNegativeClick(reportDialog.this);
            }
        });

        return builder.create();
    }
}
