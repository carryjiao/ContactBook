package com.carryj.root.contactbook.data;

/**
 * Created by root on 17/5/4.
 */

public class AddContactNumberData {

    private String phoneNumbertype;
    private String phoneNumber;

    public AddContactNumberData() {

    }

    public String getPhoneNumberType() {
        return phoneNumbertype;
    }

    public void setPhoneNumberType(String phoneNumbertype) {
        this.phoneNumbertype = phoneNumbertype;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
