/* Written with reference to official Android guides at
*       [https://developer.android.com/guide/topics/ui/controls/pickers]
* */

package com.cmput301.assignment1.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import com.cmput301.assignment1.EditActivity;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private EditActivity parent;
    private Integer hour;
    private Integer minute;

    public TimePickerFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new TimePickerDialog(getActivity(), this, this.hour, this.minute, false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Save changes immediately
        this.parent.setTimeInts(hourOfDay, minute);
    }

    public void setDefaults(EditActivity parent, Integer hour, Integer minute){
        /* Sets the initial clock value, and reference to parent to allow for saving
        * */
        this.parent = parent;
        this.hour = hour;
        this.minute = minute;
    }
}
