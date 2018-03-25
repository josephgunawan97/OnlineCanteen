package com.example.asus.onlinecanteen.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class RegistrationCancellationDialogFragment extends DialogFragment {

    private static final String TAG = RegistrationCancellationDialogFragment.class.getSimpleName();

    private CancellationHandler handler;

    public interface CancellationHandler {
        void cancelRegistration();
        void resumeRegistration();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Are you sure to cancel registration?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Resume registration");
                if(handler != null) {
                    handler.resumeRegistration();
                    dismiss();
                }
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, "Cancel registration");
                if(handler == null) {
                    getActivity().finish();
                }
                else {
                    handler.cancelRegistration();
                }
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof CancellationHandler) {
            handler = (CancellationHandler) context;
        }
    }
}
