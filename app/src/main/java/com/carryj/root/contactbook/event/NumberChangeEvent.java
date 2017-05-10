package com.carryj.root.contactbook.event;

/**
 * Created by root on 17/5/10.
 */

public class NumberChangeEvent {

    private boolean numberChangeFlag;

    public NumberChangeEvent(boolean numberChangeFlag) {
        this.numberChangeFlag = numberChangeFlag;
    }

    public boolean isNumberChangeFlag() {
        return numberChangeFlag;
    }

    public void setNumberChangeFlag(boolean numberChangeFlag) {
        this.numberChangeFlag = numberChangeFlag;
    }
}
