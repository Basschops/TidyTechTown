package com.tt.t.tidytechtowns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

// If user is within 20m of another similar report, this dialog confirms action
public class proximityDialog extends AppCompatDialogFragment {

    // Interface for passing data
    mapDialogListener mListener;

    // Adapted from https://developer.android.com/guide/topics/ui/dialogs
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            mListener = (mapDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    // Adapted from https://developer.android.com/guide/topics/ui/dialogs
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.map_dialog, null);

        builder.setView(view).setTitle("Already one in this area!")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mListener.proximityNegativeClick(proximityDialog.this);
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mListener.proximityPositiveClick(proximityDialog.this);
                    }
                });
        return builder.create();
    }

    public interface mapDialogListener{
        void proximityPositiveClick(proximityDialog dialog);
        void proximityNegativeClick(proximityDialog dialog);
    }
}
