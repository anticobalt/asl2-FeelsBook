package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Sadness extends Emotion {

    private static final String name = "sadness";
    private static Integer counter = 0;
    private Integer emoji_reference;

    public Sadness(){
        this.emoji_reference = R.drawable.sad;
        Sadness.counter++;
    }

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public Integer getCount() {
        return Sadness.counter;
    }
}
