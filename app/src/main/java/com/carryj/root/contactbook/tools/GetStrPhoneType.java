package com.carryj.root.contactbook.tools;

import android.provider.ContactsContract.CommonDataKinds.Phone;

/**
 * Created by root on 17/4/24.
 */

public class GetStrPhoneType {

    private String strPhoneType;

    public GetStrPhoneType() {

    }

    public String getStrPhoneType(int phoneType) {

        switch (phoneType) {

            case Phone.TYPE_HOME://1
                strPhoneType = new String("住宅");
                break;

            case Phone.TYPE_MOBILE://2
                strPhoneType = new String("手机");
                break;

            case Phone.TYPE_WORK://3
                strPhoneType = new String("单位");
                break;

            case Phone.TYPE_FAX_WORK://4
                strPhoneType = new String("单位传真");
                break;

            case Phone.TYPE_FAX_HOME://5
                strPhoneType = new String("住宅传真");
                break;

            case Phone.TYPE_PAGER://6
                strPhoneType = new String("寻呼机");
                break;

            case Phone.TYPE_OTHER://7
                strPhoneType = new String("其他");
                break;

            case Phone.TYPE_CALLBACK://8
                strPhoneType = new String("回拨号码");
                break;

            case Phone.TYPE_CAR://9
                strPhoneType = new String("车载电话");
                break;

            case Phone.TYPE_COMPANY_MAIN://10
                strPhoneType = new String("公司总机");
                break;

            case Phone.TYPE_ISDN://11
                strPhoneType = new String("ISDN");
                break;

            case Phone.TYPE_MAIN://12
                strPhoneType = new String("总机");
                break;

            case Phone.TYPE_OTHER_FAX://13
                strPhoneType = new String("其他传真");
                break;

            case Phone.TYPE_RADIO://14
                strPhoneType = new String("无线装置");
                break;

            case Phone.TYPE_TELEX://15
                strPhoneType = new String("电报");
                break;

            case Phone.TYPE_TTY_TDD://16
                strPhoneType = new String("TTY TDD");
                break;

            case Phone.TYPE_WORK_MOBILE://17
                strPhoneType = new String("单位手机");
                break;

            case Phone.TYPE_WORK_PAGER://18
                strPhoneType = new String("单位寻呼机");
                break;

            case Phone.TYPE_ASSISTANT://19
                strPhoneType = new String("助理");
                break;

            case Phone.TYPE_MMS://20
                strPhoneType = new String("彩信");
                break;

            default:
                strPhoneType = new String("未知");
                break;

        }

        return strPhoneType;

    }

    public void setStrPhoneType(String strPhoneType) {
        this.strPhoneType = strPhoneType;
    }
}
