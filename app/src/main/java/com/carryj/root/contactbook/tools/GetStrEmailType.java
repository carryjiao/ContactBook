package com.carryj.root.contactbook.tools;

import android.provider.ContactsContract.CommonDataKinds.Email;

/**
 * Created by root on 17/4/24.
 */

public class GetStrEmailType {

    private String strEmailType;

    public GetStrEmailType() {

    }

    public String getStrPhoneType(int emailType) {

        switch (emailType) {

            case Email.TYPE_HOME://1
                strEmailType = new String("个人");
                break;

            case Email.TYPE_WORK://2
                strEmailType = new String("工作");
                break;

            case Email.TYPE_OTHER://3
                strEmailType = new String("其他");
                break;

            case Email.TYPE_MOBILE://4
                strEmailType = new String("手机");
                break;

            default:
                strEmailType = new String("未知");
                break;

        }

        return strEmailType;

    }

    public void setStrEmailType(String strEmailType) {
        this.strEmailType = strEmailType;
    }
}
