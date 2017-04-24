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
        //System.out.println("+++++++++++++++++++============================= "+diff+" +++++++++++++++++++=============================");
        if(diff <= 60*1000) {
            return "刚刚";
        }else if(diff < 60*60*1000) {
            return diff+"分钟前";
        }else if(diff < 12*60*60*1000) {
            return df.format(date);
        }else if(diff < 7*24*60*60*1000) {
            df = new SimpleDateFormat("E");
            return df.format(date);
        }else {
            df = new SimpleDateFormat("yy/MM/dd");
            return df.format(date);
        }

        /*if(diffMonth > 0){
            return df.format(date);
        }else if(diffDay>7) {
            return df.format(date);
        }else if(1<diffDay) {
            df = new SimpleDateFormat("E");
            return df.format(date);
        }else if(diffDay == 1){
            return "昨天";
        }else {
            df = new SimpleDateFormat("HH:mm");
            return df.format(date);
        }*/
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDuration() {

        StringBuilder sb = new StringBuilder();

        if (duration == 0) {
            sb.append("00:00");
        } else if (duration > 0 && duration < 60) {
            sb.append("00:");
            if (duration < 10) {
                sb.append("0");
            }
            sb.append(duration);

        } else if (duration > 60 && duration < 3600) {

            long min = duration / 60;
            long sec = duration % 60;
            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        } else if (duration > 3600) {
            long hour = duration / 3600;
            long min = duration % 3600 / 60;
            long sec = duration % 3600 % 60;
            if (hour < 10) {
                sb.append("0");
            }
            sb.append(hour);
            sb.append(":");

            if (min < 10) {
                sb.append("0");
            }
            sb.append(min);
            sb.append(":");

            if (sec < 10) {
                sb.append("0");
            }
            sb.append(sec);
        }

        return sb.toString();
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
