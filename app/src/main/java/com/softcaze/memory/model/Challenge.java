package com.softcaze.memory.model;

/**
 * Created by Nicolas on 30/06/2019.
 */

public class Challenge {
    private String challengeLabel;
    private AwardChallengeType awardChallengeType;
    private int countAward;
    private boolean unlockChallenge;
    private boolean getAward;

    public Challenge(String challengeLabel, AwardChallengeType awardChallengeType, boolean unlockChallenge, boolean getAward, int countAward) {
        this.challengeLabel = challengeLabel;
        this.awardChallengeType = awardChallengeType;
        this.unlockChallenge = unlockChallenge;
        this.getAward = getAward;
        this.countAward = countAward;
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
