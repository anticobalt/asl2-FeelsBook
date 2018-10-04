package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Love extends Emotion {

    private static final String name = "love";
    private Integer emoji_reference;
    private Integer spawner_reference;

    public Love(){
        this.emoji_reference = R.drawable.love;
        this.spawner_reference = R.id.loveView;
    }

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public Integer getSpawnerReference() {
        return this.spawner_reference;
    }

    @Override
    public String getEmotionName() {
        return Love.name;
    }
}
