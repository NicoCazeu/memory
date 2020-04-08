package com.softcaze.memory.model;

public class LifeLevel extends Level {
    private int numberLife;

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
}
