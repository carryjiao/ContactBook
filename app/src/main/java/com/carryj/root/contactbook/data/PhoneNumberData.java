package com.carryj.root.contactbook.data;

import java.io.Serializable;

/**
 * Created by root on 17/5/6.
 */

public class PhoneNumberData implements Serializable {

    private String number;
    private String numberType;
    private String _id;

    public PhoneNumberData(){

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
