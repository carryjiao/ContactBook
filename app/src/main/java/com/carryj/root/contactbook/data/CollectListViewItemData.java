package com.carryj.root.contactbook.data;

import com.carryj.root.contactbook.tools.GetStrPhoneType;

import java.io.Serializable;

/**
 * Created by root on 17/4/27.
 */

public class CollectListViewItemData implements Serializable {

    private String name;
    private String strPhoneNumber;
    private String lookUp;
    private int rawContactID;
    private int contactID;
    private int phoneType;


    public CollectListViewItemData() {

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

    public int getID() {
        return rawContactID;
    }

    public void setRawContactID(int rawContactID) {
        this.rawContactID = rawContactID;
    }

    public String getContactID() {
        return contactID+"";
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getStrPhoneNumber() {
        return strPhoneNumber;
    }

    public void setStrPhoneNumber(String strPhoneNumber) {
        this.strPhoneNumber = strPhoneNumber;
    }

    public String getPhoneType() {
        String numberType = new GetStrPhoneType().getStrPhoneType(phoneType);
        return numberType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public String getLookUp() {
        return lookUp;
    }

    public void setLookUp(String lookUp) {
        this.lookUp = lookUp;
    }
}
