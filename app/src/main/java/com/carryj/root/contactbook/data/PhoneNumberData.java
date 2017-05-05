package com.carryj.root.contactbook.data;

import java.io.Serializable;

/**
 * Created by root on 17/5/6.
 */

public class PhoneNumberData implements Serializable {

    private String number;
    private String numberType;

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
}
