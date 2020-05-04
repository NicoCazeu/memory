package com.softcaze.memory.model;

import android.app.Activity;

import com.softcaze.memory.R;

/**
 * Created by Nicolas on 30/06/2019.
 */

public class Challenge extends ListItem {
    private int id;
    private String challengeLabel;
    private AwardChallengeType awardChallengeType;
    private int countAward;
    private boolean unlockChallenge;
    private boolean getAward;
    private GameMode mode;
    private ChallengeType challengeType;
    private int valueToReach;

    public Challenge(int id, AwardChallengeType awardChallengeType, boolean unlockChallenge, boolean getAward, int countAward, GameMode mode, ChallengeType type, int value, Activity activity) {
        this.id = id;
        this.awardChallengeType = awardChallengeType;
        this.unlockChallenge = unlockChallenge;
        this.getAward = getAward;
        this.countAward = countAward;
        this.mode = mode;
        this.challengeType = type;
        this.valueToReach = value;
        this.challengeLabel = getStringByChallengeType(this.challengeType, this.valueToReach, activity);
    }

    private String getStringByChallengeType(ChallengeType type, Integer value, Activity activity) {
        String label = "";

        if(type.equals(ChallengeType.GLOBAL_STAR)) {
            label = activity.getString(R.string.collect_stars).replace("{0}", value + "");
        } else if(type.equals(ChallengeType.END_LEVEL)) {
            label = activity.getString(R.string.end_level).replace("{0}", value + "");
        } else if(type.equals(ChallengeType.END_LEVEL_ONE_LIFE)) {
            label = activity.getString(R.string.end_level_without_fail).replace("{0}", value + "");
        } else if(type.equals(ChallengeType.END_LEVEL_THREE_STARS)) {
            label = activity.getString(R.string.end_level_with_three_stars).replace("{0}", value + "");
        } else if(type.equals(ChallengeType.END_LEVEL_WITHOUT_BONUS)) {
            label = activity.getString(R.string.end_level_without_bonus).replace("{0}", value + "");
        }

        return label;
    }

    public String getChallengeLabel() {
        return challengeLabel;
    }

    public void setChallengeLabel(String challengeLabel) {
        this.challengeLabel = challengeLabel;
    }

    public int getCountAward() {
        return countAward;
    }

    public void setCountAward(int countAward) {
        this.countAward = countAward;
    }

    public AwardChallengeType getAwardChallengeType() {
        return awardChallengeType;
    }

    public void setAwardChallengeType(AwardChallengeType awardChallengeType) {
        this.awardChallengeType = awardChallengeType;
    }

    public boolean isUnlockChallenge() {
        return unlockChallenge;
    }

    public void setUnlockChallenge(boolean unlockChallenge) {
        this.unlockChallenge = unlockChallenge;
    }

    public boolean isGetAward() {
        return getAward;
    }

    public void setGetAward(boolean getAward) {
        this.getAward = getAward;
    }

    public GameMode getMode() {
        return mode;
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public ChallengeType getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(ChallengeType challengeType) {
        this.challengeType = challengeType;
    }

    public int getValueToReach() {
        return valueToReach;
    }

    public void setValueToReach(int valueToReach) {
        this.valueToReach = valueToReach;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Challenge{" +
                "challengeLabel='" + challengeLabel + '\'' +
                ", awardChallengeType=" + awardChallengeType +
                ", countAward=" + countAward +
                ", unlockChallenge=" + unlockChallenge +
                ", getAward=" + getAward +
                '}';
    }
}
