package com.cmput301.assignment1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ImageView mLoveEmoji;
    private ImageView mAngerEmoji;
    private ImageView mFearEmoji;
    private ImageView mSurpriseEmoji;
    private ImageView mJoyEmoji;
    private ImageView mSadEmoji;
    private List<ImageView> emojis = new ArrayList<>();

    private RecyclerView mLogsView;
    private RecyclerView.Adapter mLogsAdapter;
    private RecyclerView.LayoutManager mLogsManager;
    private List<View> logs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mLogsView = findViewById(R.id.recyclerView);
        mLogsManager = new LinearLayoutManager(this);
        mLogsView.setLayoutManager(mLogsManager);
        mLogsAdapter = new LogsAdapter(this, logs);
        mLogsView.setAdapter(mLogsAdapter);

        for (ImageView emoji: emojis){
            emoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    CharSequence text = "A click was detected!";
                    // Inside anonymous method, so use `this` context won't work
                    // Derived from https://developer.android.com/guide/topics/ui/notifiers/toasts
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
