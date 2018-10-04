/* Adapter implementation modified from Joshua Charles Campbell's work at
* [https://github.com/joshua2ua/lonelyTwitter]
* */

package com.cmput301.assignment1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cmput301.assignment1.emotion.Anger;
import com.cmput301.assignment1.emotion.Emotion;
import com.cmput301.assignment1.emotion.Fear;
import com.cmput301.assignment1.emotion.Joy;
import com.cmput301.assignment1.emotion.Love;
import com.cmput301.assignment1.emotion.Sadness;
import com.cmput301.assignment1.emotion.Surprise;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

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
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    // Log variables
    private ArrayList<Log> logs = new ArrayList<>();
    private LogsAdapter adapter;
    private ListView logsView;

    // Emotion/emoji variables
    private ArrayList<ImageView> emojis = new ArrayList<>();
    private HashMap<String, Integer> emotionCounts = new HashMap<>();
    private ArrayList<TextView> emotionCountViews = new ArrayList<>();

    // Constants
    private final String LOGS_FILE = "logs.sav";
    private final int EDIT_LOG_REQUEST = 1;
    private ArrayList<Class<? extends Emotion>> emotions = new ArrayList<>(Arrays.asList(
            Anger.class, Fear.class, Joy.class, Love.class,
            Sadness.class, Surprise.class));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove the action bar; no use for now
        // https://stackoverflow.com/a/27712413
        getSupportActionBar().hide();

        // Initialize emotionCounts
        // https://stackoverflow.com/a/1181976
        for (Class emotion : emotions){
            String name = emotion.getName();
            emotionCounts.put(name.substring(name.lastIndexOf(".") + 1), 0);
        }

        // Get user data, if it exists; requires emotionCounts
        loadFileData();
        updateEmotionCountViews();

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

        // Collect emotion count TextViews
        emotionCountViews.add((TextView) findViewById(R.id.loveCount));
        emotionCountViews.add((TextView) findViewById(R.id.angerCount));
        emotionCountViews.add((TextView) findViewById(R.id.fearCount));
        emotionCountViews.add((TextView) findViewById(R.id.surpriseCount));
        emotionCountViews.add((TextView) findViewById(R.id.joyCount));
        emotionCountViews.add((TextView) findViewById(R.id.sadCount));

        // Set click listener for each emoji
        for (ImageView emoji: emojis){
            setEmojiListener(emoji, adapter);
        }

        // Set click listener for logsView
        setLogsListener(logsView);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /* Occurs when a called activity returns control to this activity
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

            String save_message = "Log saved!";
            String delete_message = "Log deleted!";
            String cancel_message = "Editing canceled: no changes made.";
            String save_error_message = "Fatal save error!";
            String delete_error_message = "Fatal error! Deletion failed.";

            if (resultCode == RESULT_OK) {

                Integer id = extras.getInt("id");
                String comment = extras.getString("comment");
                Log log = findLogByID(id);

                // Extra is HashMap despite compiler's complaints
                // https://stackoverflow.com/a/262416
                HashMap<String, Integer> dt = (HashMap<String, Integer>) extras.getSerializable("datetimes");

                if (log == null) {
                    // this should never happen
                    Toast toast = Toast.makeText(getApplicationContext(), save_error_message, Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    // Convert integers to date
                    Calendar c = Calendar.getInstance();
                    c.set(dt.get("year"), dt.get("month"), dt.get("day"), dt.get("hour"), dt.get("minute"));
                    // Set date and comment
                    log.updateDatetime(c.getTime());
                    log.updateComment(comment);
                }

                // Sort array, notify adapter, and save
                Collections.sort(this.logs);
                Collections.reverse(this.logs);
                this.adapter.notifyDataSetChanged();
                saveFileData();

                // Notify user
                Toast toast = Toast.makeText(getApplicationContext(), save_message, Toast.LENGTH_SHORT);
                toast.show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast toast = Toast.makeText(getApplicationContext(), cancel_message, Toast.LENGTH_SHORT);
                toast.show();

            } else if (resultCode == RESULT_REQUEST_DELETION){

                Integer id = extras.getInt("id");
                Integer status = deleteLogAtID(id);
                if (status == -1){
                    // Couldn't find log; shouldn't ever happen
                    Toast toast = Toast.makeText(getApplicationContext(), delete_error_message, Toast.LENGTH_SHORT);
                    toast.show();
                }

                // Notify adapter and save
                this.adapter.notifyDataSetChanged();
                saveFileData();

                // Notify user
                Toast toast = Toast.makeText(getApplicationContext(), delete_message, Toast.LENGTH_SHORT);
                toast.show();

            }
        }
    }

    private void updateEmotionCountViews() {
        // for every count view, find associated class' name, then use as hashmap key
        //      to update view's text
        for (TextView countView : this.emotionCountViews){
            String associated_class_name = countView.getTag().toString();
            for (Class emotion : emotions){
                String name = emotion.getName();
                String key = name.substring(name.lastIndexOf(".") + 1);
                if (associated_class_name.equals(key)){
                    countView.setText(emotionCounts.get(key).toString());
                }
            }
        }
    }

    private void loadFileData() {
    /* Try to open save file and bind it's contents to Log ArrayList */

        try {

            // Read logs file
            FileInputStream fis = openFileInput(LOGS_FILE);
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
                this.logs.add(actualLog);
            }

            // Get emotion counts by iterating over logs array
            for (Log log : this.logs){
                String name = log.getEmotion().getEmotionName().toUpperCase();
                this.emotionCounts.put(name, this.emotionCounts.get(name) + 1); // count += 1
            }


        } catch (FileNotFoundException e){
            // if user has nothing already saved, or something else goes wrong
            logs = new ArrayList<>();
            e.printStackTrace();
        }

    }

    private void saveFileData() {

        try {
            // Prep work
            FileOutputStream fos = openFileOutput(LOGS_FILE, 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter writer = new BufferedWriter(osw);
            Gson gson = new Gson();

            // Create array of LiteLogs to allow for serialization
            ArrayList<LiteLog> liteLogs = new ArrayList<>();
            for (Log log: logs){
                LiteLog litelog = new LiteLog(log.getDatetime(), log.getComment(),
                        log.getEmotion().getEmotionName().toUpperCase());
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

                String success_message = "Log added!";

                // fetch comment from textbox
                TextInputEditText textbox = findViewById(R.id.textboxView);
                String comment = textbox.getText().toString();

                // clear it
                textbox.setText("");

                // find the emotion name
                String name = null;
                for (Class emotion : emotions){
                    name = emotion.getName();
                    name = name.substring(name.lastIndexOf(".") + 1);
                    if (name.equals(emoji.getTag().toString())){
                        break;
                    }
                }
                // create log with emotion name, update everything, save
                if (name != null) {
                    MainActivity.this.logs.add(0, new Log(name, new Date(), comment));

                    // count += 1
                    MainActivity.this.emotionCounts.put(name, MainActivity.this.emotionCounts.get(name) + 1);
                    updateEmotionCountViews();

                    adapter.notifyDataSetChanged();

                    // save changes and notify
                    saveFileData();
                    Toast toast = Toast.makeText(getApplicationContext(), success_message, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
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
        /* Returns 0 on success, -1 on failure */

        for(int i = 0; i < this.logs.size(); i++){
            if (logs.get(i).getId().equals(id)){

                // Decrement running count of emotion
                String name = logs.get(i).getEmotion().getEmotionName().toUpperCase();
                this.emotionCounts.put(name, this.emotionCounts.get(name) - 1);

                // Update views and remove from array
                updateEmotionCountViews();
                logs.remove(i);

                return 0;

            }
        }
        return -1;
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
