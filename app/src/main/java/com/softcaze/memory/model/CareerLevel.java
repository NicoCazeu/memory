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

    public CareerLevel(int id, int starNumber, LevelState state, int countCard, CardTheme theme) {
        super(id, state, countCard, theme);
        this.numberStar = starNumber;
        scoreLimit = new ArrayList<>();
    }

    public String toString() {
        return "CareerLevel{" +
                "scoreLimit=" + scoreLimit +
                ", numberStar=" + numberStar +
                ", touchUsed=" + touchUsed +
                '}';
    }

    public List<Integer> getScoreLimit() {
        return scoreLimit;
    }

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

    public Integer getScoreNumberStar(Integer score) {
        if(score <= 0 ) {
            return 0;
        }

        int numberStar = 0;

        for(int i = scoreLimit.size()-1; i >= 0; i--) {
            if(score <= scoreLimit.get(i)) {
                numberStar = i+1;
                break;
            }
        }
        return numberStar;
    }

    public void resetScore() {
        this.touchUsed = 0;
    }
}
