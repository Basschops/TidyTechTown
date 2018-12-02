package com.tt.t.tidytechtowns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class reportNearbyDialog extends AppCompatDialogFragment{

        mapDialogListener mListener;
        private static String type;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            // Verify that the host activity implements the callback interface
            try {
                // Instantiate the NoticeDialogListener so we can send events to the host
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
                            mListener.reportNearbyNegativeClick(reportNearbyDialog.this);
                        }
                    })
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            mListener.reportNearbyPositiveClick(reportNearbyDialog.this, type);
                        }
                    });
            return builder.create();
        }

    // Overloaded to change string type
    public void show(FragmentManager manager, String tag, String type) {
        super.show(manager, tag);
        this.type = type;
    }

    public interface mapDialogListener{
            void reportNearbyPositiveClick(reportNearbyDialog dialog, String type);
            void reportNearbyNegativeClick(reportNearbyDialog dialog);
        }
    }


