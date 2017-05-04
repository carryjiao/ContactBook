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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryj.root.contactbook.adapter.AddContactNumberAdapter;
import com.carryj.root.contactbook.data.AddContactEmailData;
import com.carryj.root.contactbook.data.AddContactImData;
import com.carryj.root.contactbook.data.AddContactNumberData;
import com.carryj.root.contactbook.ui.DividerItemDecoration;

import java.util.ArrayList;


public class AddContactActivity extends SweepBackActivity {

    private String SELECTOR;
    private static final String FROM_CONTACT_BOOK_FRAGEMENT_ADD = "FROM_CONTACT_BOOK_FRAGEMENT_ADD";
    private static final String FROM_DIAL_FRAGEMENT_ADD = "FROM_DIAL_FRAGEMENT_ADD";
    private static final String FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW = "FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW";

    private String phoneNumber;

    private TextView tv_add_contact_done;

    private ImageView im_add_contact_icon;

    private EditText et_add_contact_surname;
    private EditText et_add_contact_given_name;
    private EditText et_add_contact_company;

    private RecyclerView add_contact_number_recyclerview;
    private RecyclerView add_contact_email_recyclerview;
    private RecyclerView add_contact_im_recyclerview;

    private LinearLayout ll_add_contact_number_add;
    private LinearLayout ll_add_contact_email_add;
    private LinearLayout ll_add_contact_im_add;

    private  ArrayList<AddContactNumberData> myNumberData = new ArrayList<AddContactNumberData>();
    private  ArrayList<AddContactNumberData> myNumberChangeData = new ArrayList<AddContactNumberData>();

    private ArrayList<AddContactEmailData> myEmailData = new ArrayList<AddContactEmailData>();
    private ArrayList<AddContactEmailData> myEmailChangeData = new ArrayList<AddContactEmailData>();

    private ArrayList<AddContactImData> myImData = new ArrayList<AddContactImData>();
    private ArrayList<AddContactImData> myImChangeData = new ArrayList<AddContactImData>();

    private AddContactNumberAdapter numberAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
    }

    @Override
    protected void initData() {

        SELECTOR = getIntent().getStringExtra("SELECTOR");
        if(SELECTOR == FROM_CONTACT_BOOK_FRAGEMENT_ADD){
            phoneNumber = "";
        }else if(SELECTOR == FROM_DIAL_FRAGEMENT_ADD||SELECTOR == FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW) {
            phoneNumber = getIntent().getStringExtra("NUMBER");
        }else {
            phoneNumber = "";
        }

        AddContactNumberData numberItemData = new AddContactNumberData();
        numberItemData.setPhoneNumber(phoneNumber);
        myNumberData.add(numberItemData);



    }



    @Override
    protected void initView() {

        tv_add_contact_done = (TextView) findViewById(R.id.tv_add_contact_done);
        im_add_contact_icon = (ImageView) findViewById(R.id.im_add_contact_icon);
        et_add_contact_surname = (EditText) findViewById(R.id.et_add_contact_surname);
        et_add_contact_given_name = (EditText) findViewById(R.id.et_add_contact_given_name);
        et_add_contact_company = (EditText) findViewById(R.id.et_add_contact_company);

        add_contact_number_recyclerview = (RecyclerView) findViewById(R.id.add_contact_number_recyclerview);
        ll_add_contact_number_add = (LinearLayout) findViewById(R.id.ll_add_contact_number_add);

        add_contact_email_recyclerview = (RecyclerView) findViewById(R.id.add_contact_email_recyclerview);
        ll_add_contact_email_add = (LinearLayout) findViewById(R.id.ll_add_contact_email_add);

        add_contact_im_recyclerview = (RecyclerView) findViewById(R.id.add_contact_im_recyclerview);
        ll_add_contact_im_add = (LinearLayout) findViewById(R.id.ll_add_contact_im_add);

        numberAdapter = new AddContactNumberAdapter(this, myNumberData);
        add_contact_number_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        add_contact_number_recyclerview.setAdapter(numberAdapter);
        add_contact_number_recyclerview.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));



    }

    @Override
    protected void initEvents() {
        tv_add_contact_done.setOnClickListener(this);
        ll_add_contact_number_add.setOnClickListener(this);
        ll_add_contact_email_add.setOnClickListener(this);
        ll_add_contact_im_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_add_contact_done:
                break;
            case R.id.ll_add_contact_number_add:
                numberAdapter.addNumberData(1);
                break;
            case R.id.ll_add_contact_email_add:
                break;
            case R.id.ll_add_contact_im_add:
                break;
            default:
                break;
        }

    }

    private boolean insert(String name, ArrayList<AddContactNumberData> numberDatas,
                           ArrayList<AddContactEmailData> emailDatas,
                           ArrayList<AddContactImData> imDatas) {

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
            for(AddContactNumberData numberItemData:numberDatas) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                values.put(Phone.NUMBER, numberItemData.getPhoneNumber());
                values.put(Phone.TYPE, numberItemData.getPhoneNumberType());
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入Email数据
            for(AddContactEmailData emailItemData:emailDatas) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
                values.put(Email.DATA, emailItemData.getEmail());
                values.put(Email.TYPE, emailItemData.getEmailType());
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入QQ数据
            for(AddContactImData imItemData:imDatas) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
                values.put(Im.DATA, imItemData.getIm());
                values.put(Im.PROTOCOL, imItemData.getImType());
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