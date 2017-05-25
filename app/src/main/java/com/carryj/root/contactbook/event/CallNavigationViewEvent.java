package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 */

public class CallNavigationViewEvent {

    private boolean CallNavigationViewFlag;

    public CallNavigationViewEvent(boolean CallNavigationViewFlag) {
        this.CallNavigationViewFlag = CallNavigationViewFlag;
    }

    public boolean isCallNavigationViewFlag() {
        return CallNavigationViewFlag;
    }

    public void setCallNavigationViewFlag(boolean callNavigationViewFlag) {
        this.CallNavigationViewFlag = callNavigationViewFlag;
    }
}
