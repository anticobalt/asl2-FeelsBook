package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Surprise extends Emotion {

    private static final String name = "surprise";
    private static Integer counter = 0;
    private Integer emoji_reference;
    private Integer spawner_reference;

    public Surprise(){
        this.emoji_reference = R.drawable.surprise;
        this.spawner_reference = R.id.surpriseView;
        Surprise.counter++;
    }

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public Integer getCount() {
        return Surprise.counter;
    }

    @Override
    public Integer getSpawnerReference() {
        return this.spawner_reference;
    }

    @Override
    public String getEmotionName() {
        return Surprise.name;
    }
}

