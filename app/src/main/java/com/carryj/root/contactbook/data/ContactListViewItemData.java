package com.carryj.root.contactbook.data;

import java.io.Serializable;

/**
 * Created by root on 17/4/27.
 */

public class ContactListViewItemData implements Serializable {

    private String name;
    private int rawContactID;

    public ContactListViewItemData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRawContactID() {
        return rawContactID+"";
    }

    public void setRawContactID(int rawContactID) {
        this.rawContactID = rawContactID;
    }
}
