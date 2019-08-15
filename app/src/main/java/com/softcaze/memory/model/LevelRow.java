package com.softcaze.memory.model;

/**
 * Created by Nicolas on 26/06/2019.
 */

public class LevelRow {
    private Level level1;
    private Level level2;
    private Level level3;

    public LevelRow() {
        ;
    }
    public LevelRow(Level level1, Level level2, Level level3) {
        this.level1 = level1;
        this.level2 = level2;
        this.level3 = level3;
    }

    public Level getLevel1() {
        return level1;
    }

    public void setLevel1(Level level1) {
        this.level1 = level1;
    }

    public Level getLevel2() {
        return level2;
    }

    public void setLevel2(Level level2) {
        this.level2 = level2;
    }

    public Level getLevel3() {
        return level3;
    }

    public void setLevel3(Level level3) {
        this.level3 = level3;
    }

    @Override
    public String toString() {
        return "LevelRow{" +
                "level1=" + level1 +
                ", level2=" + level2 +
                ", level3=" + level3 +
                '}';
    }
}
