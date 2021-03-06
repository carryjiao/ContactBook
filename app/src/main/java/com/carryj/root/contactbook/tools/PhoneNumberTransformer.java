package com.carryj.root.contactbook.tools;

/**
 * Created by root on 17/4/28.
 *
 * 去掉数据库中电话号码里的格式化符号: '-' 和 ' '
 */

public class PhoneNumberTransformer {
    private String strPhoneNumber;

    public PhoneNumberTransformer() {

    }

    public String getStrPhoneNumber() {

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i<strPhoneNumber.length(); i++){
            if(strPhoneNumber.charAt(i) != '-'&&strPhoneNumber.charAt(i) != ' ')
                sb.append(strPhoneNumber.charAt(i));

        }

        return sb.toString();
    }

    public void setStrPhoneNumber(String strPhoneNumber) {
        this.strPhoneNumber = strPhoneNumber;
    }
}
