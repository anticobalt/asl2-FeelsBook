package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Joy extends Emotion {

    private static final String name = "joy";
    private static Integer counter = 0;
    private Integer emoji_reference;

    public Joy(){
        this.emoji_reference = R.drawable.joy;
        Joy.counter++;
    }

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public Integer getCount() {
        return Joy.counter;
    }
}
