package com.softcaze.memory.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nicolas on 26/06/2019.
 */

public abstract class Level {
    private int id;
    private LevelState state;
    private int countCard;
    private CardTheme theme;

    public Level() {
        ;
    }

    public Level(int id, LevelState state, int countCard, CardTheme theme) {
        this.id = id;
        this.state = state;
        this.countCard = countCard;
        this.theme = theme;
    }

    public Level(int id, int nbrStar, LevelState state, int countCard, CardTheme theme) {
        this.id = id;
        this.state = state;
        this.countCard = countCard;
        this.theme = theme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LevelState getState() {
        return state;
    }

    public void setState(LevelState state) {
        this.state = state;
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

    @Override
    public String toString() {
        return "Level{" +
                "id=" + id +
                ", state=" + state +
                ", countCard=" + countCard +
                ", theme=" + theme +
                '}';
    }
}
