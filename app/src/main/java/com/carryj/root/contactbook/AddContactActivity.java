package com.carryj.root.contactbook;

import android.content.ContentUris;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Data;
import android.view.View;



public class AddContactActivity extends SweepBackActivity {

    private String SELECTOR;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
    }

    @Override
    protected void initData() {



    }



    @Override
    protected void initView() {



    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onClick(View v) {

    }

    private boolean insert(String name, String phoneNumber, String email, String im_qq) {

        try
        {
            ContentValues values = new ContentValues();

            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (name.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
                values.put(StructuredName.GIVEN_NAME, name);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入电话数据
            if (phoneNumber.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                values.put(Phone.NUMBER, phoneNumber);
                values.put(Phone.TYPE, Phone.TYPE_MOBILE);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入Email数据
            if (email.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
                values.put(Email.DATA, email);
                values.put(Email.TYPE, Email.TYPE_WORK);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入QQ数据
            if (im_qq.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
                values.put(Im.DATA, im_qq);
                values.put(Im.PROTOCOL, Im.PROTOCOL_QQ);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }
        }

        catch (Exception e)
        {
            return false;
        }

        return true;

    }


}
