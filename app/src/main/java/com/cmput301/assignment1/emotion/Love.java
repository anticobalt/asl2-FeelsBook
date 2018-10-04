package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Love extends Emotion {

    private static final String name = "love";
    private static Integer emoji_reference = R.drawable.love;

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public String getEmotionName() {
        return Love.name;
    }
}
