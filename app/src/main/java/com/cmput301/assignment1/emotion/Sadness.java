package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Sadness extends Emotion {

    private static final String name = "sadness";
    private Integer emoji_reference;
    private Integer spawner_reference;

    public Sadness(){
        this.emoji_reference = R.drawable.sad;
        this.spawner_reference = R.id.sadView;
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
        return Sadness.name;
    }
}
