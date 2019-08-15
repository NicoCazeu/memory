package com.softcaze.memory.model;

/**
 * Created by Nicolas on 26/06/2019.
 */

public class LevelScore {
    private int touchUsed;
    private long timeUsed;

    public LevelScore() {
        ;
    }
    public LevelScore(int touchUsed, long timeUsed) {
        this.touchUsed = touchUsed;
        this.timeUsed = timeUsed;
    }

    public int getTouchUsed() {
        return touchUsed;
    }

    public void setTouchUsed(int touchUsed) {
        this.touchUsed = touchUsed;
    }

    public long getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(long timeUsed) {
        this.timeUsed = timeUsed;
    }

    @Override
    public String toString() {
        return "LevelScore{" +
                "touchUsed=" + touchUsed +
                ", timeUsed=" + timeUsed +
                '}';
    }
}
