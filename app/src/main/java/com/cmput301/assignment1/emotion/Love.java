package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Love extends Emotion {

    private static final String name = "love";
    private static Integer counter = 0;
    private Integer emoji_reference;

    public Love(){
        this.emoji_reference = R.drawable.love;
        Love.counter++;
    }

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public Integer getCount() {
        return Love.counter;
    }
}
