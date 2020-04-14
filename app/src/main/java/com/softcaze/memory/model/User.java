package com.softcaze.memory.model;

/**
 * Created by Nicolas on 27/01/2020.
 */

public class User {
    private static User instance = new User();
    private Coin coin;
    private Bonus bonus;
    private boolean needCareerTuto;
    private boolean needAgainstTimeTuto;
    private boolean needSurvivalTuto;
    private boolean needSuddenDeathTuto;

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

    public boolean isNeedCareerTuto() {
        return needCareerTuto;
    }

    public void setNeedCareerTuto(boolean needCareerTuto) {
        this.needCareerTuto = needCareerTuto;
    }

    public boolean isNeedAgainstTimeTuto() {
        return needAgainstTimeTuto;
    }

    public void setNeedAgainstTimeTuto(boolean needAgainstTimeTuto) {
        this.needAgainstTimeTuto = needAgainstTimeTuto;
    }

    public boolean isNeedSurvivalTuto() {
        return needSurvivalTuto;
    }

    public void setNeedSurvivalTuto(boolean needSurvivalTuto) {
        this.needSurvivalTuto = needSurvivalTuto;
    }

    public boolean isNeedSuddenDeathTuto() {
        return needSuddenDeathTuto;
    }

    public void setNeedSuddenDeathTuto(boolean needSuddenDeathTuto) {
        this.needSuddenDeathTuto = needSuddenDeathTuto;
    }
}
