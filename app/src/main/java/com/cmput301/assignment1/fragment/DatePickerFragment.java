package com.cmput301.assignment1.fragment;

import android.app.Dialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.cmput301.assignment1.EditActivity;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private EditActivity parent;
    private Integer year;
    private Integer month;
    private Integer day;

    public DatePickerFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new DatePickerDialog(getActivity(), this, this.year, this.month, this.day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // Save changes immediately
        this.parent.setDateInts(year, month, dayOfMonth);
    }

    public void setDefaults(EditActivity parent, Integer year, Integer month, Integer day){
        /* Sets the initial calender value, and reference to parent to allow for saving
         * */
        this.parent = parent;
        this.year = year;
        this.month = month;
        this.day = day;
    }


}
