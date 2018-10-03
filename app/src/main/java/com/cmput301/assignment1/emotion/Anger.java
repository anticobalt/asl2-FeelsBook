package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Anger extends Emotion {

    private static final String name = "anger";
    private static Integer counter = 0;
    private Integer emoji_reference;
    private Integer spawner_reference;

    public Anger(){
        this.emoji_reference = R.drawable.anger;
        this.spawner_reference =  R.id.angerView;
        Anger.counter++;
    }

    @Override
    public Integer getEmojiReference() {
        return this.emoji_reference;
    }

    @Override
    public Integer getCount() {
        return Anger.counter;
    }

    @Override
    public Integer getSpawnerReference() {
        return this.spawner_reference;
    }

    @Override
    public String getEmotionName() {
        return Anger.name;
    }
}
