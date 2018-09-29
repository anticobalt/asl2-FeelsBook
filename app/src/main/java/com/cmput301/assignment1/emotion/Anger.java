package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Anger extends Emotion {

    private static final String name = "anger";
    private static Integer counter = 0;
    private Integer emoji_reference;

    public Anger(){
        this.emoji_reference = R.drawable.anger;
        Anger.counter++;
    }

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public Integer getCount() {
        return Anger.counter;
    }
}
