package com.softcaze.memory.model;

/**
 * Created by Nicolas on 27/01/2020.
 */

public class Award extends Position {
    private int amount;
    private AwardChallengeType awardChallengeType;

    public Award() {
        super();
    }

    public Award(int amount) {
        this.amount = amount;
    }

    public Award(int x, int y) {
        super(x, y);
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public AwardChallengeType getAwardChallengeType() {
        return awardChallengeType;
    }

    public void setAwardChallengeType(AwardChallengeType awardChallengeType) {
        this.awardChallengeType = awardChallengeType;
    }
}
