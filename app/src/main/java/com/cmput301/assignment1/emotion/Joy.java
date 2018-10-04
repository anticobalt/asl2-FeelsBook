package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Joy extends Emotion {

    private static final String name = "joy";
    private static Integer emoji_reference = R.drawable.joy;

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public String getEmotionName() {
        return Joy.name;
    }
}
