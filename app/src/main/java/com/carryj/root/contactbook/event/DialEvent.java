package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 * 用于拨号后通知RecordFragment刷新数据
 */

public class DialEvent {

    private boolean dialFlag;
    private boolean afterDialFlag;

    public DialEvent(boolean dialFlag, boolean afterDialFlag) {
        this.dialFlag = dialFlag;
        this.afterDialFlag = afterDialFlag;
    }

    public boolean isDialFlag() {
        return dialFlag;
    }

    public void setDialFlag(boolean dialFlag) {
        this.dialFlag = dialFlag;
    }

    public boolean isAfterDialFlag() {
        return afterDialFlag;
    }

    public void setAfterDialFlag(boolean afterDialFlag) {
        this.afterDialFlag = afterDialFlag;
    }
}
