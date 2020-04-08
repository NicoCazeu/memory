package com.softcaze.memory.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 26/06/2019.
 */

public class Level {
    private int id;
    private int starNumber;
    private LevelState state;
    private LevelScore score;
    private int countCard;
    private CardTheme theme;
    private List<Integer> scoreLimit;

    public Level() {
        ;
    }

    public Level(int id, int starNumber, LevelState state, LevelScore score, int countCard, CardTheme theme) {
        this.id = id;
        this.starNumber = starNumber;
        this.state = state;
        this.score = score;
        this.countCard = countCard;
        this.theme = theme;
    }

    public Level(int id, int nbrStar, LevelState state, int countCard, CardTheme theme) {
        this.id = id;
        this.starNumber = nbrStar;
        this.state = state;
        this.countCard = countCard;
        this.theme = theme;
        this.score = new LevelScore();
        this.scoreLimit = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStarNumber() {
        return starNumber;
    }

    public void setStarNumber(int starNumber) {
        this.starNumber = starNumber;
    }

    public LevelState getState() {
        return state;
    }

    public void setState(LevelState state) {
        this.state = state;
    }

    public LevelScore getScore() {
        return score;
    }

    public void setScore(LevelScore score) {
        this.score = score;
    }

    public CardTheme getTheme() {
        return theme;
    }

    public void setTheme(CardTheme theme) {
        this.theme = theme;
    }

    public int getCountCard() {
        return countCard;
    }

    public void setCountCard(int countCard) {
        this.countCard = countCard;
    }

    public List<Integer> getScoreLimit() {
        return scoreLimit;
    }

    public void setScoreLimit(List<Integer> scoreLimit) {
        this.scoreLimit = scoreLimit;
    }

    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", starNumber=" + starNumber +
                ", state=" + state +
                ", score=" + score +
                ", countCard=" + countCard +
                ", theme=" + theme +
                ", scoreLimit=" + scoreLimit +
                '}';
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
        LevelScore score = new LevelScore(0,0);
        this.setScore(score);
    }
}
