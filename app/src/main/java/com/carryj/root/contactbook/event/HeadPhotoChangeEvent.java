package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 */

public class HeadPhotoChangeEvent {

    private boolean headPhotoChangeFlag;

    private String headPhotoChange;

    /*public HeadPhotoChangeEvent(boolean headPhotoChangeFlag) {
        this.headPhotoChangeFlag = headPhotoChangeFlag;
    }*/

    public HeadPhotoChangeEvent(String headPhotoChange) {
        this.headPhotoChange = headPhotoChange;
    }
    public boolean isHeadPhotoChangeFlag() {
        return headPhotoChangeFlag;
    }

    public void setHeadPhotoChangeFlag(boolean headPhotoChangeFlag) {
        this.headPhotoChangeFlag = headPhotoChangeFlag;
    }

    public String getHeadPhotoChange() {
        return headPhotoChange;
    }

    public void setHeadPhotoChange(String headPhotoChange) {
        this.headPhotoChange = headPhotoChange;
    }
}
