package com.carryj.root.contactbook.tools;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

import com.carryj.root.contactbook.data.ContactListViewItemData;
import com.carryj.root.contactbook.data.PhoneNumberData;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 17/5/2.
 */

public class ContactBookSearch {

    /*按号码-拼音模糊搜索*/

    public static ArrayList<ContactListViewItemData> searchContact(Context context,
            CharSequence str) {

        ArrayList<ContactListViewItemData> allContactData = new ArrayList<ContactListViewItemData>();
        ArrayList<ContactListViewItemData> resultData = new ArrayList<ContactListViewItemData>();

        Cursor phoneCursor = context.getContentResolver().query(Phone.CONTENT_URI,
                new String[]{Phone.DISPLAY_NAME, Phone.NUMBER, Phone.LOOKUP_KEY},
                null,null,Phone.RAW_CONTACT_ID+" ASC");
        if(phoneCursor!=null){
            while (phoneCursor.moveToNext()){
                String name = phoneCursor.getString(0);
                String number = phoneCursor.getString(1);
                String lookUp = phoneCursor.getString(2);
                PhoneNumberTransformer pntf = new PhoneNumberTransformer();
                pntf.setStrPhoneNumber(number);
                number = pntf.getStrPhoneNumber();
                ContactListViewItemData itemData = new ContactListViewItemData();
                itemData.setName(name);
                itemData.setNumber(number);
                itemData.setLookUp(lookUp);
                allContactData.add(itemData);
            }
        }

        //以电话号码方式查询
        if (str.toString().startsWith("0") || str.toString().startsWith("1")
                || str.toString().startsWith("+")) {

            for(ContactListViewItemData itemData : allContactData) {
                if(itemData.getName() != null && itemData.getNumber() != null) {
                    if(itemData.getName().contains(str)||itemData.getNumber().contains(str)) {
                        resultData.add(itemData);
                    }
                }

            }
            return resultData;

        }

        ChineseSpelling finder = ChineseSpelling.getInstance();
        String result = new String("");

        for(ContactListViewItemData itemData : allContactData) {

            // 先将输入的字符串转换为拼音
            finder.setResource(str.toString());
            result = finder.getSpelling();

            if (contains(itemData, result)) {//是拼音
                resultData.add(itemData);
            }else if (itemData.getNumber().contains(str)) {//是数字

                resultData.add(itemData);
            }

        }
        return resultData;


    }



    /**
     * 根据拼音搜索
     */
    private static boolean contains(ContactListViewItemData itemData, String search) {
        if (TextUtils.isEmpty(itemData.getName())
                && TextUtils.isEmpty(itemData.getNumber())) {
            return false;
        }

        boolean flag = false;

        // 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
        if (search.length() < 6) {
            String firstLetters = FirstLetterUtil.getFirstLetter(itemData.getName());
            // 不区分大小写
            Pattern firstLetterMatcher = Pattern.compile(search,
                    Pattern.CASE_INSENSITIVE);
            flag = firstLetterMatcher.matcher(firstLetters).find();
        }

        if (!flag) { // 如果简拼已经找到了，就不使用全拼了
            // 全拼匹配
            ChineseSpelling finder = ChineseSpelling.getInstance();
            finder.setResource(itemData.getName());
            // 不区分大小写
            Pattern pattern2 = Pattern
                    .compile(search, Pattern.CASE_INSENSITIVE);
            Matcher matcher2 = pattern2.matcher(finder.getSpelling());
            flag = matcher2.find();
        }

        return flag;
    }

}
