package com.softcaze.memory.model;

public class Credit {
    private int resImg;
    private String creator;

    public Credit(int resImg, String creator) {
        this.resImg = resImg;
        this.creator = creator;
    }

    public int getResImg() {
        return resImg;
    }

    public void setResImg(int resImg) {
        this.resImg = resImg;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
