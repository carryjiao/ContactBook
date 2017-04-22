package com.carryj.root.contactbook.data;

import android.provider.CallLog;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 17/4/22.
 */

public class RecordListViewItemData {


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

    public void setcontactType(int contactType) {
        this.contactType = contactType;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public String getDate() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat df = new SimpleDateFormat("yy/MM/dd");
        int diffMonth = currentDate.getMonth() - date.getMonth();
        int diffDay = currentDate.getDay() - date.getDay();

        if(diffMonth > 0){
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
        }
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
