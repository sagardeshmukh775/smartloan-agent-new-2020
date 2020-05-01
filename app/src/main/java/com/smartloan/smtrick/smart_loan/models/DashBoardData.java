package com.smartloan.smtrick.smart_loan.models;

import java.io.Serializable;

public class DashBoardData implements Serializable {
    private String imagePath;
    private String description;
    private boolean isActive;
    private long index;

    public DashBoardData() {
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }
}
