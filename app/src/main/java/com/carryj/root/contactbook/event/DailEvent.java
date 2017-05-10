package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 * 用于拨号后通知RecordFragment刷新数据
 */

public class DailEvent {

    private boolean dailFlag;

    public DailEvent(boolean dailFlag) {
        this.dailFlag = dailFlag;
    }

    public boolean isDailFlag() {
        return dailFlag;
    }

    public void setDailFlag(boolean dailFlag) {
        this.dailFlag = dailFlag;
    }
}
