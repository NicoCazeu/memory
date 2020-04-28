package com.softcaze.memory.model;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.softcaze.memory.R;

/**
 * Created by Nicolas on 30/06/2019.
 */

public enum AwardChallengeType {
    COIN(R.drawable.coin),
    EYE_BONUS(R.drawable.eyes_bonus);

    public static final int COIN_DATABASE = 1;
    public static final int EYE_BONUS_DATABASE = 2;

    private AwardChallengeType(int resId) {
        this.resId = resId;
    }

    private int resId;

    public int getResId() {
        return resId;
    }
}
