package com.softcaze.memory.model;

public class HeaderItem extends ListItem {
    private int resId;

    public HeaderItem(int resId) {
        this.resId = resId;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
