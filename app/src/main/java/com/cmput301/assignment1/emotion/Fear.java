package com.cmput301.assignment1.emotion;

import com.cmput301.assignment1.R;

public class Fear extends Emotion {

    private static final String name = "fear";
    private Integer emoji_reference;
    private Integer spawner_reference;

    public Fear(){
        this.emoji_reference = R.drawable.fear;
        this.spawner_reference = R.id.fearView;
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
        return Fear.name;
    }
}
