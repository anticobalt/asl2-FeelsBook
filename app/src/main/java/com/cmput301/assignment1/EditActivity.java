package com.cmput301.assignment1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cmput301.assignment1.fragment.DatePickerFragment;
import com.cmput301.assignment1.fragment.TimePickerFragment;

import java.util.HashMap;

public class EditActivity extends AppCompatActivity {

    private Integer id;
    private String comment;
    private HashMap<String, Integer> datetimes;
    private final Integer RESULT_REQUEST_DELETION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize buttons
        Button dateButton = findViewById(R.id.dateButton);
        Button timeButton = findViewById(R.id.timeButton);
        Button saveButton = findViewById(R.id.saveButton);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button cancelButton = findViewById(R.id.cancelButton);

        // Fetch Intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            // Extra is HashMap despite compiler's complaints
            // https://stackoverflow.com/a/262416
            datetimes = (HashMap<String, Integer>) extras.getSerializable("datetimes");

            id = extras.getInt("id");
            comment = extras.getString("comment");

        }

        // Set click listeners
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
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return control to MainActivity, and send everything back
                Intent intent = new Intent();
                intent.putExtra("id", EditActivity.this.id);
                intent.putExtra("comment", EditActivity.this.comment);
                intent.putExtra("datetimes", EditActivity.this.datetimes);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return control to MainActivity, and signal deletion
                Intent intent = new Intent();
                intent.putExtra("id", EditActivity.this.id);
                setResult(RESULT_REQUEST_DELETION, intent);
                finish();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Just return
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    protected void showTimePickerDialog(View v){
        // https://stackoverflow.com/a/27514754
        TimePickerFragment timeFrag = new TimePickerFragment();
        timeFrag.setDefaults(this, this.datetimes.get("hour"), this.datetimes.get("minute"));
        timeFrag.show(getSupportFragmentManager(), "timePicker");
    }

    protected void showDatePickerDialog(View v){
        DatePickerFragment dateFrag = new DatePickerFragment();
        dateFrag.setDefaults(this, this.datetimes.get("year"), this.datetimes.get("month"), this.datetimes.get("day"));
        dateFrag.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDateInts(Integer year, Integer month, Integer day){
        this.datetimes.put("year", year);
        this.datetimes.put("month", month);
        this.datetimes.put("day", day);
    }

    public void setTimeInts(Integer hour, Integer minute){
        this.datetimes.put("hour", hour);
        this.datetimes.put("minute", minute);
    }

}
