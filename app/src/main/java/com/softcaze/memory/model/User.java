package com.softcaze.memory.model;

/**
 * Created by Nicolas on 27/01/2020.
 */

public class User {
    private static User instance = new User();
    private Coin coin;
    private Bonus bonus;

    public User() {
    }

    public static User getInstance() {
        return instance;
    }

    public Coin getCoin() {
        return coin;
    }

    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public Bonus getBonus() {
        return bonus;
    }

    public void setBonus(Bonus bonus) {
        this.bonus = bonus;
    }
}
