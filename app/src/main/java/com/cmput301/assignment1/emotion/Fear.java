package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Fear extends Emotion {

    private static final String name = "fear";
    private static Integer emoji_reference = R.drawable.fear;

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public String getEmotionName() {
        return Fear.name;
    }
}
