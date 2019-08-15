package com.softcaze.memory.model;

import com.softcaze.memory.R;

/**
 * Created by Nicolas on 09/07/2019.
 */

public enum CardType {
    // FRUIT
    GRAPES(R.drawable.fruits_grapes, 1, CardTheme.FRUTT),
    APPLE(R.drawable.fruits_apple, 2, CardTheme.FRUTT),
    BANANA(R.drawable.fruits_banana, 3, CardTheme.FRUTT),
    CHERRIE(R.drawable.fruits_cherries, 4, CardTheme.FRUTT),
    ORANGE(R.drawable.fruits_orange, 5, CardTheme.FRUTT),
    PINEAPPLE(R.drawable.fruits_pineapple, 6, CardTheme.FRUTT),
    STRAWBERRY(R.drawable.fruits_strawberry, 7, CardTheme.FRUTT),
    WATERMELON(R.drawable.fruits_watermelon, 8, CardTheme.FRUTT),

    // FLAG COUNTRY
    AUSTRALIA(R.drawable.flag_australia, 101, CardTheme.FLAG),
    BRAZIL(R.drawable.flag_brazil, 102, CardTheme.FLAG),
    CANADA(R.drawable.flag_canada, 103, CardTheme.FLAG),
    CHINA(R.drawable.flag_china, 104, CardTheme.FLAG),
    FRANCE(R.drawable.flag_france, 105, CardTheme.FLAG),
    GERMANY(R.drawable.flag_germany, 106, CardTheme.FLAG),
    INDIA(R.drawable.flag_india, 107, CardTheme.FLAG),
    ITALY(R.drawable.flag_italy, 108, CardTheme.FLAG),
    JAPAN(R.drawable.flag_japan, 109, CardTheme.FLAG),
    PORTUGAL(R.drawable.flag_portugal, 110, CardTheme.FLAG),
    RUSSIA(R.drawable.flag_russia, 111, CardTheme.FLAG),
    SOUTH_KOREA(R.drawable.flag_south_korea, 112, CardTheme.FLAG),
    SPAIN(R.drawable.flag_spain, 113, CardTheme.FLAG),
    THAILAND(R.drawable.flag_thailand, 114, CardTheme.FLAG),
    UNITED_KINGDOM(R.drawable.flag_united_kingdom, 115, CardTheme.FLAG),
    UNITED_STATES(R.drawable.flag_united_states, 116, CardTheme.FLAG);

    private int drawable;
    private int value;
    private CardTheme theme;

    private CardType(int drawable, int value, CardTheme theme) {
        this.drawable = drawable;
        this.value = value;
        this.theme = theme;
    }

    public int getDrawable() {
        return this.drawable;
    }

    public int getValue() {
        return this.value;
    }

    public CardTheme getTheme() {
        return this.theme;
    }
}
