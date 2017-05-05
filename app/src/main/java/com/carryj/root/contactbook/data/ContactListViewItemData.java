package com.carryj.root.contactbook.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by root on 17/4/27.
 */

public class ContactListViewItemData implements Serializable {

    private String name;
    private int rawContactID;
    private int contactID;
    private String number;
    private ArrayList<String> numbers;

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ArrayList<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;
    }
}
