package com.softcaze.memory.model;

/**
 * Created by Nicolas on 30/01/2020.
 */

public class Card extends Position {
    protected int width;
    protected int height;

    public Card(int w, int h, int x, int y) {
        super(x, y);
        this.width = w;
        this.height = h;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
