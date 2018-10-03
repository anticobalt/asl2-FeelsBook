package com.cmput301.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
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

    // Declare buttons and views
    private Button dateButton;
    private Button timeButton;
    private Button saveButton;
    private Button deleteButton;
    private Button cancelButton;
    private TextInputEditText commentInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.dateButton = findViewById(R.id.dateButton);
        this.timeButton = findViewById(R.id.timeButton);
        this.saveButton = findViewById(R.id.saveButton);
        this.deleteButton = findViewById(R.id.deleteButton);
        this.cancelButton = findViewById(R.id.cancelButton);
        this.commentInput = findViewById(R.id.commentEditView);

        // Fetch Intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){

            // Extra is HashMap despite compiler's complaints
            // https://stackoverflow.com/a/262416
            datetimes = (HashMap<String, Integer>) extras.getSerializable("datetimes");

            // DatePickerDialog's month attribute is 0-11 instead of 1-12
            // Example: https://developer.android.com/reference/android/app/DatePickerDialog
            datetimes.put("month", datetimes.get("month") - 1);

            id = extras.getInt("id");
            comment = extras.getString("comment");

        }

        // Display comment
        commentInput.setText(this.comment);

        // Set click listeners
        setClickListeners();

    }

    private void setClickListeners() {
        this.dateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
        this.timeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v);
            }
        });
        this.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return control to MainActivity, and send everything back
                EditActivity.this.comment = EditActivity.this.commentInput.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("id", EditActivity.this.id);
                intent.putExtra("comment", EditActivity.this.comment);
                intent.putExtra("datetimes", EditActivity.this.datetimes);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        this.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Return control to MainActivity, and signal deletion
                Intent intent = new Intent();
                intent.putExtra("id", EditActivity.this.id);
                setResult(RESULT_REQUEST_DELETION, intent);
                finish();
            }
        });
        this.cancelButton.setOnClickListener(new View.OnClickListener() {
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
