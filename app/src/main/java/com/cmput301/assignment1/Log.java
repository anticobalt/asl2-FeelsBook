package com.cmput301.assignment1;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.cmput301.assignment1.emotion.Anger;
import com.cmput301.assignment1.emotion.Emotion;
import com.cmput301.assignment1.emotion.Fear;
import com.cmput301.assignment1.emotion.Joy;
import com.cmput301.assignment1.emotion.Love;
import com.cmput301.assignment1.emotion.Sadness;
import com.cmput301.assignment1.emotion.Surprise;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Log implements Comparable<Log>{
    /* Logs have unique ID to allow for identification across activities,
    *       as they themselves cannot be passed through Intents.
    * */

    private static Integer runningID = 0;
    private Integer id;
    private Emotion emotion;
    private Date datetime;
    private String comment;

    public Log(String emotionName, Date datetime, String comment){

        this.id = ++runningID;

        switch (emotionName){
            case "anger": this.emotion = new Anger(); break;
            case "fear": this.emotion = new Fear(); break;
            case "joy": this.emotion = new Joy(); break;
            case "love": this.emotion = new Love(); break;
            case "sadness": this.emotion = new Sadness(); break;
            case "surprise": this.emotion = new Surprise(); break;
        }

        this.datetime = datetime;
        this.comment = comment;

    }

    @Override
    public int compareTo(@NonNull Log o) {
        // return 1 if more than o, -1 if less than, 0 if equal
        Date other_date = o.getDatetime();
        if (this.datetime.after(other_date)){
            return 1;
        } else if (this.datetime.before(other_date)){
            return -1;
        } else {
            return 0;
        }
    }

    public Integer getId() {
        return this.id;
    }

    public Date getDatetime(){
        return this.datetime;
    }

    public String getComment(){
        return this.comment;
    }

    public Emotion getEmotion() {
        return this.emotion;
    }

    public String getDateTimeAsString() {
        /* Returns datetime formatted in ISO 8601
        */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return date_format.format(this.datetime);
    }

    public void updateDatetime(Date datetime){
        this.datetime = datetime;
    }

    public void updateComment(String comment){
        this.comment = comment;
    }

}

class LiteLog {
    // A subclass that avoids abstract class Emotion,
    //      allowing for serialization via Gson.
    // Class is basically just a data container and sort of a one-trick pony
    //      so making getters/setters seems like overkill

    public Date datetime;
    public String comment;
    public String emotionName;

    LiteLog(Date datetime, String comment, String emotionName){
        this.comment = comment;
        this.datetime = datetime;
        this.emotionName = emotionName;
    }
}