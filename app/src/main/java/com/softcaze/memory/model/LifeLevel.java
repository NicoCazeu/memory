package com.softcaze.memory.model;

public class LifeLevel extends Level {
    private int numberLife;
    private boolean hasAdVideoReward;

    public LifeLevel(int id, LevelState state, int countCard, CardTheme theme, int numberLife) {
        super(id, state, countCard, theme);
        this.numberLife = numberLife;
    }

    public int getNumberLife() {
        return numberLife;
    }

    public void setNumberLife(int numberLife) {
        this.numberLife = numberLife;
    }

    public boolean isHasAdVideoReward() {
        return hasAdVideoReward;
    }

    public void setHasAdVideoReward(boolean hasAdVideoReward) {
        this.hasAdVideoReward = hasAdVideoReward;
    }
}
