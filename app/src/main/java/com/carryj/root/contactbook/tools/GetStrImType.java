package com.carryj.root.contactbook.tools;

import android.provider.ContactsContract.CommonDataKinds.Im;

/**
 * Created by root on 17/4/24.
 */

public class GetStrImType {

    private String strImType;

    public GetStrImType() {

    }

    public String getStrImType(int imType) {

        switch (imType) {

            case Im.PROTOCOL_MSN://1
                strImType = new String("MSN");
                break;

            case Im.PROTOCOL_YAHOO://2
                strImType = new String("雅虎");
                break;

            case Im.PROTOCOL_SKYPE://3
                strImType = new String("Skype");
                break;

            case Im.PROTOCOL_QQ://4
                strImType = new String("QQ");
                break;

            case Im.PROTOCOL_GOOGLE_TALK://5
                strImType = new String("环聊");
                break;

            case Im.PROTOCOL_ICQ://6
                strImType = new String("ICQ");
                break;

            case Im.PROTOCOL_JABBER://7
                strImType = new String("Jabber");
                break;

            case Im.PROTOCOL_NETMEETING://8
                strImType = new String("Windows Live");
                break;

            default:
                strImType = new String("未知");
                break;

        }

        return strImType;

    }

    public void setStrImType(String strImType) {
        this.strImType = strImType;
    }
}
