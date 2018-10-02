package com.cmput301.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmput301.assignment1.fragment.DatePickerFragment;
import com.cmput301.assignment1.fragment.TimePickerFragment;

import java.util.ArrayList;
import java.util.Arrays;

public class EditActivity extends AppCompatActivity {

    private Integer id;
    private String datetime_string;
    private String comment;
    private Integer year, month, day, hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Fetch Intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            datetime_string = extras.getString("datetime_string");
            comment = extras.getString("comment");
        }

        // Save date and time
        ArrayList<Integer> dt = getDateTimeAsInts(this.datetime_string);
        setDateInts(dt.get(0), dt.get(1), dt.get(2));
        setTimeInts(dt.get(3), dt.get(4));

        // Set click listeners
        Button dateButton = findViewById(R.id.dateButton);
        Button timeButton = findViewById(R.id.timeButton);
        dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });

    }

    protected void showTimePickerDialog(View v){
        // https://stackoverflow.com/a/27514754
        TimePickerFragment timeFrag = new TimePickerFragment();
        timeFrag.setDefaults(this, this.hour, this.minute);
        timeFrag.show(getSupportFragmentManager(), "timePicker");
    }

    protected void showDatePickerDialog(View v){
        DatePickerFragment dateFrag = new DatePickerFragment();
        dateFrag.setDefaults(this, this.year, this.month, this.day);
        dateFrag.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDateInts(Integer year, Integer month, Integer day){
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setTimeInts(Integer hour, Integer minute){
        this.hour = hour;
        this.minute = minute;
    }

    private ArrayList<Integer> getDateTimeAsInts(String dateTimeString){
        /* Converts "Year-Month-DayTHour:Minute:Second" to [Year, Month, Day, Hour, Minute, Second]
         *       then binds it to member attributes
         * */

        // Isolate the components
        String[] temp = dateTimeString.split("T");
        String[] date = temp[0].split("-");
        String[] time = temp[1].split(":");

        // Collect them (as strings)
        ArrayList<String> dateTimeStringArray = new ArrayList<>(Arrays.asList(date));
        dateTimeStringArray.addAll(Arrays.asList(time));

        // Convert to integers
        ArrayList<Integer> dateTimeArray = new ArrayList<>();
        for(String e: dateTimeStringArray) dateTimeArray.add(Integer.valueOf(e));

        return dateTimeArray;

    }

}
