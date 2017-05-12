package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 */

public class CollectEvent {

    private boolean collectFlag;

    public CollectEvent(boolean collectFlag) {
        this.collectFlag = collectFlag;
    }

    public boolean isCollectFlag() {
        return collectFlag;
    }

    public void setCollectFlag(boolean collectFlag) {
        this.collectFlag = collectFlag;
    }
}
