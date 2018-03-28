package com.example.asus.onlinecanteen.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment implements OnTimeSetListener {

    private TimeResultHandler handler;

    public interface TimeResultHandler {
        void onTimeSet(int hourOfDay, int minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getContext(), this, 0, 0, true);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(handler != null) {
            handler.onTimeSet(hourOfDay, minute);
        }
    }

    public void setHandler(TimeResultHandler handler) {
        this.handler = handler;
    }
}
