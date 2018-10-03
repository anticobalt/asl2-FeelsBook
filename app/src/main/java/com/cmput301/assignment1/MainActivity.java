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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Log> logs = new ArrayList<>();
    private LogsAdapter adapter;
    private ListView logsView;
    private ArrayList<ImageView> emojis = new ArrayList<>();
    private final String SAVEDATA = "data.sav";
    private final int EDIT_LOG_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user data, if it exists
        loadFileData();

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

    private void loadFileData() {
    /* Try to open save file and bind it's contents to Log ArrayList */

        try {

            // Read file
            FileInputStream fis = openFileInput(SAVEDATA);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            Gson gson = new Gson();
            Type LiteLogList = new TypeToken<ArrayList<LiteLog>>(){}.getType();
            ArrayList<LiteLog> liteLogs = gson.fromJson(reader, LiteLogList);

            // Dissect the liteLogs and create new Logs out of them
            for (LiteLog liteLog: liteLogs){
                Date d = liteLog.datetime;
                String e = liteLog.emotionName;
                String c = liteLog.comment;
                Log actualLog = new Log(e, d, c);
                logs.add(actualLog);
            }

        } catch (FileNotFoundException e){
            // if user has nothing already saved, or something else goes wrong
            logs = new ArrayList<>();
            e.printStackTrace();
        }

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
                HashMap<String, Integer> datetimes = getDateTimeAsIntHash(log.getDateTimeAsString());
                intent.putExtra("id", log.getId());
                intent.putExtra("datetimes", datetimes);
                intent.putExtra("comment", log.getComment());
                startActivityForResult(intent, EDIT_LOG_REQUEST);
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
                //      then create Log instance with that Emotion. Logs are prepended.
                // Doing it this way avoids having to create unique listener for every emoji
                switch(emoji.getId()){
                    case R.id.loveView: logs.add(0, new Log("love", new Date(), comment)); break;
                    case R.id.fearView: logs.add(0, new Log("fear", new Date(), comment)); break;
                    case R.id.angerView: logs.add(0, new Log("anger", new Date(), comment)); break;
                    case R.id.surpriseView: logs.add(0, new Log("surprise", new Date(), comment)); break;
                    case R.id.joyView: logs.add(0, new Log("joy", new Date(), comment)); break;
                    case R.id.sadView: logs.add(0, new Log("sadness", new Date(), comment)); break;
                }

                adapter.notifyDataSetChanged();

                // save changes
                saveFileData();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* Called when a called activity returns control to this activity
        * */
        Integer RESULT_REQUEST_DELETION = 2;
        Bundle extras;

        // make sure an Intent was actually returned by called Activity
        // null foreshadows requestCode == RESULT_CANCELLED
        if (data != null) {
             extras = data.getExtras();
        } else {
            return;
        }

        if (requestCode == EDIT_LOG_REQUEST ) {
            if (resultCode == RESULT_OK) {

                Integer id = extras.getInt("id");
                String comment = extras.getString("comment");
                Log log = findLogByID(id);

                // Extra is HashMap despite compiler's complaints
                // https://stackoverflow.com/a/262416
                HashMap<String, Integer> dt = (HashMap<String, Integer>) extras.getSerializable("datetimes");

                if (log == null) {
                    // this should never happen
                    Toast toast = Toast.makeText(getApplicationContext(), "Fatal save error!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // Convert integers to date
                    Calendar c = Calendar.getInstance();
                    c.set(dt.get("year"), dt.get("month"), dt.get("day"), dt.get("hour"), dt.get("minute"));
                    // Set date and comment
                    log.updateDatetime(c.getTime());
                    log.updateComment(comment);
                }

            } else if (resultCode == RESULT_CANCELED) {
                // Don't do anything for now if called activity quits without doing anything
            } else if (resultCode == RESULT_REQUEST_DELETION){
                Integer id = extras.getInt("id");
                Integer status = deleteLogAtID(id);
                if (status == -1){
                    // Couldn't find log; shouldn't ever happen
                    Toast toast = Toast.makeText(getApplicationContext(), "Fatal error! Deletion failed.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
    }

    private Log findLogByID(Integer id) {
        // Linear search because it's quick and easy to implement
        for(Log log : this.logs){
            if (log.getId().equals(id)){
                return log;
            }
        }
        return null;
    }

    private Integer deleteLogAtID(Integer id) {
        for(int i = 0; i < this.logs.size(); i++){
            if (logs.get(i).getId().equals(id)){
                logs.remove(i);
                return 0;
            }
        }
        return -1;
    }

    private void saveFileData() {

        try {

            // Prep work
            FileOutputStream fos = openFileOutput(SAVEDATA, 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();

            // Create array of LiteLogs to allow for serialization
            ArrayList<LiteLog> liteLogs = new ArrayList<>();
            for (Log log: logs){
                LiteLog litelog = new LiteLog(log.getDatetime(), log.getComment(), log.getEmotionName());
                liteLogs.add(litelog);
            }

            // convert to Json and write
            gson.toJson(liteLogs, writer);
            writer.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashMap<String, Integer> getDateTimeAsIntHash(String dateTimeString){
        /* Converts "Year-Month-DayTHour:Minute:Second" to [Year, Month, Day, Hour, Minute, Second]
         *       in hash-table format (accessed by name e.g. "year"), then binds it to member attributes
         * */

        ArrayList<String> names = new ArrayList<>(Arrays.asList("year", "month", "day", "hour", "minute"));
        ArrayList<String> dateTimeStringArray = new ArrayList<>();

        // Isolate the components
        String[] temp = dateTimeString.split("T");
        String[] date = temp[0].split("-");
        String[] time = temp[1].split(":");

        // Collect them (as strings)
        dateTimeStringArray.addAll(Arrays.asList(date));
        dateTimeStringArray.addAll(Arrays.asList(time));

        // Convert to integers
        HashMap<String, Integer> dateTimeHash = new HashMap<>();
        for (int i = 0; i < names.size(); i++) {
            dateTimeHash.put(names.get(i), Integer.valueOf(dateTimeStringArray.get(i)));
        }

        return dateTimeHash;

    }

}
