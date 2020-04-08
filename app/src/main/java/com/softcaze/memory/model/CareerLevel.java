package com.softcaze.memory.model;

import java.util.ArrayList;
import java.util.List;

public class CareerLevel extends Level {
    private List<Integer> scoreLimit;
    private int numberStar;
    private int touchUsed;


    public CareerLevel() {
        this.scoreLimit = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CareerLevel{" +
                "scoreLimit=" + scoreLimit +
                ", numberStar=" + numberStar +
                ", touchUsed=" + touchUsed +
                '}';
    }

    @Override
    public List<Integer> getScoreLimit() {
        return scoreLimit;
    }

    @Override
    public void setScoreLimit(List<Integer> scoreLimit) {
        this.scoreLimit = scoreLimit;
    }

    public int getNumberStar() {
        return numberStar;
    }

    public void setNumberStar(int numberStar) {
        this.numberStar = numberStar;
    }

    public int getTouchUsed() {
        return touchUsed;
    }

    public void setTouchUsed(int touchUsed) {
        this.touchUsed = touchUsed;
    }
}
