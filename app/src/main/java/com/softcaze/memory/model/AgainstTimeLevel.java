package com.softcaze.memory.model;

public class AgainstTimeLevel extends Level {
    private long timeScore;

    public AgainstTimeLevel(int id, LevelState state, int countCard, CardTheme theme, long timeScore) {
        super(id, state, countCard, theme);
        this.timeScore = timeScore;
    }

    public long getTimeScore() {
        return timeScore;
    }

    public void setTimeScore(long timeScore) {
        this.timeScore = timeScore;
    }
}
