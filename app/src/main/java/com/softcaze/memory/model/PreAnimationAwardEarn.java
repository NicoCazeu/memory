package com.softcaze.memory.model;

import android.widget.ImageView;

/**
 * Created by Nicolas on 27/01/2020.
 */

public class PreAnimationAwardEarn {
    private double coefA;
    private double coefB;
    private Position from;
    private Position to;
    private ImageView award;

    public double getCoefA() {
        return coefA;
    }

    public void setCoefA(double coefA) {
        this.coefA = coefA;
    }

    public double getCoefB() {
        return coefB;
    }

    public void setCoefB(double coefB) {
        this.coefB = coefB;
    }

    public Position getFrom() {
        return from;
    }

    public void setFrom(Position from) {
        this.from = from;
    }

    public Position getTo() {
        return to;
    }

    public void setTo(Position to) {
        this.to = to;
    }

    public ImageView getAward() {
        return award;
    }

    public void setAward(ImageView award) {
        this.award = award;
    }
}
