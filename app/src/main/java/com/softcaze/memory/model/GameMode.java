package com.softcaze.memory.model;

import android.content.Context;
import android.content.res.Resources;

import com.softcaze.memory.R;

/**
 * Created by Nicolas on 30/06/2019.
 */

public enum GameMode {
    CAREER(R.string.career),
    AGAINST_TIME(R.string.race_against_time),
    SUDDEN_DEATH(R.string.sudden_death),
    SURVIVAL(R.string.survival);

    public static final int AGAINST_TIME_DATABASE = 1;
    public static final int SURVIVAL_DATABASE = 2;
    public static final int SUDDEN_DEATH_DATABASE = 3;

    private GameMode(int resId) {
        this.resId = resId;
    }

    private int resId;

    public int getResId() {
        return resId;
    }

    public String toString(Context context) {
        String text = context.getApplicationContext().getString(getResId());
        return text;

    }
}
