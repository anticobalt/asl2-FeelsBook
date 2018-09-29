package com.cmput301.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Log> logs = new ArrayList<>();
    private ArrayAdapter<Log> adapter;
    private ListView logsView;

    private ImageView mLoveEmoji;
    private ImageView mAngerEmoji;
    private ImageView mFearEmoji;
    private ImageView mSurpriseEmoji;
    private ImageView mJoyEmoji;
    private ImageView mSadEmoji;
    private ArrayList<ImageView> emojis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ListView that holds logs
        logsView = findViewById(R.id.logsListView);

        // Collect emoji ImageViews
        mLoveEmoji = findViewById(R.id.loveView);
        mAngerEmoji = findViewById(R.id.angerView);
        mFearEmoji = findViewById(R.id.fearView);
        mSurpriseEmoji = findViewById(R.id.surpriseView);
        mJoyEmoji = findViewById(R.id.joyView);
        mSadEmoji = findViewById(R.id.sadView);
        emojis.add(mLoveEmoji);
        emojis.add(mAngerEmoji);
        emojis.add(mFearEmoji);
        emojis.add(mSurpriseEmoji);
        emojis.add(mJoyEmoji);
        emojis.add(mSadEmoji);

        // Set click listener for each emoji
        // Lack of `final` throws compile error
        for (final ImageView emoji: emojis){
            emoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    // for each emoji, manually determine associated Emotion
                    //      then create Log instance with that Emotion
                    switch(emoji.getId()){
                        case R.id.loveView: logs.add(new Log("love", new Date(), "")); break;
                        case R.id.fearView: logs.add(new Log("fear", new Date(), "")); break;
                        case R.id.angerView: logs.add(new Log("anger", new Date(), "")); break;
                        case R.id.surpriseView: logs.add(new Log("surprise", new Date(), "")); break;
                        case R.id.joyView: logs.add(new Log("joy", new Date(), "")); break;
                        case R.id.sadView: logs.add(new Log("sadness", new Date(), "")); break;
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the log array with the logs view
        adapter = new ArrayAdapter<>(this, R.layout.log, logs);
        logsView.setAdapter(adapter);
    }
}
