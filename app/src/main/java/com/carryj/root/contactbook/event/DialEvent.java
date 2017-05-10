package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 * 用于拨号后通知RecordFragment刷新数据
 */

public class DialEvent {

    private boolean dialFlag;

    public DialEvent(boolean dialFlag) {
        this.dialFlag = dialFlag;
    }

    public boolean isDialFlag() {
        return dialFlag;
    }

    public void setDialFlag(boolean dialFlag) {
        this.dialFlag = dialFlag;
    }
}
