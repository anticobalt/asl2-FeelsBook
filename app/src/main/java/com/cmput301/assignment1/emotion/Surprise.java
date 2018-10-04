package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Surprise extends Emotion {

    private static final String name = "surprise";
    private Integer emoji_reference = R.drawable.surprise;

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public String getEmotionName() {
        return Surprise.name;
    }
}

