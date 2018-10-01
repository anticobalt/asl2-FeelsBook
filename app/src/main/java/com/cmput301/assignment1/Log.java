package com.cmput301.assignment1;

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
    private Date date;
    private String comment;
    private String emotionName;

    public Log(String emotionName, Date date, String comment){

        this.id = ++runningID;

        switch (emotionName){
            case "anger": this.emotion = new Anger(); break;
            case "fear": this.emotion = new Fear(); break;
            case "joy": this.emotion = new Joy(); break;
            case "love": this.emotion = new Love(); break;
            case "sadness": this.emotion = new Sadness(); break;
            case "surprise": this.emotion = new Surprise(); break;
        }

        this.date = date;
        this.comment = comment;
        this.emotionName = emotionName;

    }

    public static Integer getRunningID(){
        return Log.runningID;
    }

    public Integer getId() {
        return this.id;
    }

    public Date getDate(){
        return this.date;
    }

    public String getComment(){
        return this.comment;
    }

    public Integer getEmojiReference(){
        return this.emotion.getEmojiReference();
    }

    @Override
    public int compareTo(@NonNull Log o) {
        // return 1 if more than o, -1 if less than, 0 if equal
        Date other_date = o.getDate();
        if (this.date.after(other_date)){
            return 1;
        } else if (this.date.before(other_date)){
            return -1;
        } else {
            return 0;
        }
    }

    public String getDateAsString() {
        /* Returns date formatted in ISO 8601
        Modified from Joachim Sauer's answer on https://stackoverflow.com/a/3914498
        */
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return date_format.format(this.date);
    }

    public String getEmotionName() {
        return this.emotionName;
    }

}

class LiteLog {
    // A subclass that avoids abstract class Emotion,
    //      allowing for serialization via Gson.
    // Class is basically just a data container and sort of a one-trick pony
    //      so making getters/setters seems like overkill

    public Date date;
    public String comment;
    public String emotionName;

    LiteLog(Date date, String comment, String emotionName){
        this.comment = comment;
        this.date = date;
        this.emotionName = emotionName;
    }
}