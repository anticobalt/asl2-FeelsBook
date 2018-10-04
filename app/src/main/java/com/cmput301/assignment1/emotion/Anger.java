package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Anger extends Emotion {

    private static final String name = "anger";
    private final Integer emoji_reference = R.drawable.anger;

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public String getEmotionName() {
        return Anger.name;
    }
}
