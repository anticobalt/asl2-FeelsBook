package com.cmput301.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class EditActivity extends AppCompatActivity {

    private Integer id;
    private String emotion_name;
    private String date_string;
    private String comment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Fetch Intent extras
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            id = extras.getInt("id");
            emotion_name = extras.getString("emotion_name");
            date_string = extras.getString("date_string");
            comment = extras.getString("comment");
        }
    }

}
