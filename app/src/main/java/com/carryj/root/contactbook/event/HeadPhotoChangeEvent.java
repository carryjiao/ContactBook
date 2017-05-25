package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 */

public class HeadPhotoChangeEvent {

    private boolean headPhotoChangeFlag;

    public HeadPhotoChangeEvent(boolean headPhotoChangeFlag) {
        this.headPhotoChangeFlag = headPhotoChangeFlag;
    }

    public boolean isHeadPhotoChangeFlag() {
        return headPhotoChangeFlag;
    }

    public void setHeadPhotoChangeFlag(boolean headPhotoChangeFlag) {
        this.headPhotoChangeFlag = headPhotoChangeFlag;
    }
}
