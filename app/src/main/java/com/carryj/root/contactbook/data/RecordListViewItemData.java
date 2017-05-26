package com.carryj.root.contactbook.data;


import android.provider.CallLog;

import com.carryj.root.contactbook.tools.GetStrPhoneType;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 17/4/22.
 */

public class RecordListViewItemData implements Serializable{


    private String strNumber;
    private String strCachedName;
    private int contactType;
    private int phoneType;
    private Date date;
    private Long duration;
    private int _id;
    private String contactID;


    public RecordListViewItemData() {

    }

    public String getStrNumber() {
        return strNumber;
    }

    public void setStrNumber(String strNumber) {
        this.strNumber = strNumber;
    }

    public String getStrCachedName() {
        return strCachedName;
    }

    public void setStrCachedName(String strCachedName) {
        this.strCachedName = strCachedName;
    }

    public int getcontactType() {
        return contactType;
    }

    public String getStrContactType() {

        String str = new String();

        if (contactType == CallLog.Calls.INCOMING_TYPE) {
            str = new String("呼入电话");
        } else if (contactType == CallLog.Calls.OUTGOING_TYPE) {
            str = new String("呼出电话");
        } else if (contactType == CallLog.Calls.MISSED_TYPE) {
            str = new String("未接来电");
        }
        return str;
    }

    public void setcontactType(int contactType) {
        this.contactType = contactType;
    }

    public String getPhoneType() {
        String numberType = new GetStrPhoneType().getStrPhoneType(phoneType);
        return numberType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public String getDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Long diff = currentDate.getTime() - date.getTime();
        if(diff <= 120*1000) {
            return "刚刚";
        }else if(diff < 60*60*1000) {
            return diff/(60*1000)+"分钟前";
        }else if(diff < 12*60*60*1000) {
            return df.format(date);
        }else if(diff < 3*24*60*60*1000) {
            df = new SimpleDateFormat("EEEE");
            return df.format(date);
        }else {
            return getDisplayDate();
        }

    }

    public String getDisplayDate() {

        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        return df.format(date);

    }

    public String getTime() {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        return df.format(date);

    }



    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {//以秒钟为单位

        StringBuilder sb = new StringBuilder();

        if (duration == 0) {
            sb.append("0 秒钟");
        } else if (duration > 0 && duration < 60) {
            sb.append(duration);
            sb.append(" 秒钟");

        } else if (duration > 60 && duration < 3600) {

            long min = duration / 60;
            long sec = duration % 60;
            sb.append(min);
            sb.append("分");
            sb.append(sec);
            sb.append("秒");
        } else if (duration > 3600) {
            long hour = duration / 3600;
            long min = duration % 3600 / 60;
            long sec = duration % 3600 % 60;
            sb.append(hour);
            sb.append("小时");
            sb.append(min);
            sb.append("分钟");
        }

        return sb.toString();
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }
}
