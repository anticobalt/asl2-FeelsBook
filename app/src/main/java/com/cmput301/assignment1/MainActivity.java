/* Adapter implementation modified from Joshua Charles Campbell's work at
* [https://github.com/joshua2ua/lonelyTwitter]
* */

package com.cmput301.assignment1;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Log> logs = new ArrayList<>();
    private LogsAdapter adapter;
    private ListView logsView;
    private ArrayList<ImageView> emojis = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ListView that holds logs
        logsView = findViewById(R.id.logsListView);

        // Connect the log array with the logs view
        adapter = new LogsAdapter(this, R.layout.log, logs);
        logsView.setAdapter(adapter);

        // Collect emoji ImageViews
        emojis.add((ImageView) findViewById(R.id.loveView));
        emojis.add((ImageView) findViewById(R.id.angerView));
        emojis.add((ImageView) findViewById(R.id.fearView));
        emojis.add((ImageView) findViewById(R.id.surpriseView));
        emojis.add((ImageView) findViewById(R.id.joyView));
        emojis.add((ImageView) findViewById(R.id.sadView));

        // Set click listener for each emoji
        for (ImageView emoji: emojis){
            setEmojiListener(emoji, adapter);
        }

        // Set click listener for logsView
        setLogsListener(logsView);

    }

    private void setLogsListener(ListView logsView) {
        // Set click listener, which transfers Log data from MainActivity to EditActivity
        //      via an Intent.
        // See https://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener
        //      and user914425's answer on https://stackoverflow.com/a/7325248
        logsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log log = (Log) parent.getItemAtPosition(position);
                Intent intent = new Intent(MainActivity.this, EditActivity.class);

                // Intents can only be packaged with standard-typed variables,
                //      so Logs will have to be dissected, then passed to activity
                intent.putExtra("id", log.getId());
                intent.putExtra("emotion_name", log.getEmotionName());
                intent.putExtra("date_string", log.getDateAsString());
                intent.putExtra("comment", log.getComment());
                startActivity(intent);
            }
        });
    }

    // Lack of `final` throws compile error
    private void setEmojiListener(final ImageView emoji, final LogsAdapter adapter) {
        emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                // fetch comment from textbox
                TextInputEditText textbox = findViewById(R.id.textboxView);
                String comment = textbox.getText().toString();

                // clear it
                textbox.setText("");

                // For each emoji, manually determine associated Emotion
                //      then create Log instance with that Emotion.
                // Doing it this way avoids having to create unique listener for every emoji
                switch(emoji.getId()){
                    case R.id.loveView: logs.add(new Log("love", new Date(), comment)); break;
                    case R.id.fearView: logs.add(new Log("fear", new Date(), comment)); break;
                    case R.id.angerView: logs.add(new Log("anger", new Date(), comment)); break;
                    case R.id.surpriseView: logs.add(new Log("surprise", new Date(), comment)); break;
                    case R.id.joyView: logs.add(new Log("joy", new Date(), comment)); break;
                    case R.id.sadView: logs.add(new Log("sadness", new Date(), comment)); break;
                }

                adapter.notifyDataSetChanged();

            }
        });
    }


}
