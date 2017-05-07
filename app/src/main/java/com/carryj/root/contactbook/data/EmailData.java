package com.carryj.root.contactbook.data;

import java.io.Serializable;

/**
 * Created by root on 17/5/6.
 */

public class EmailData implements Serializable {

    private String email;
    private String emailType;
    private String _id;

    public EmailData(){

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
