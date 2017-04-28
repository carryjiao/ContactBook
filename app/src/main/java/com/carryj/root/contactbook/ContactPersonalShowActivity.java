package com.carryj.root.contactbook;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryj.root.contactbook.data.ContactListViewItemData;
import com.carryj.root.contactbook.fragments.ContactBookFragement;
import com.carryj.root.contactbook.tools.GetStrPhoneType;

public class ContactPersonalShowActivity extends SweepBackActivity {

    private String[] PERSON_INFOMATION_PROJECTION = new String[]{
            CommonDataKinds.Phone.TYPE,
            CommonDataKinds.Phone.NUMBER
    };

    private static int PHONE_TYPE_INDEX = 0;
    private static int PHONE_NUMBER_INDEX = 1;




    private ContactListViewItemData data;
    private String rawContactID;
    private String name;
    private String number;
    private String numberType;
    private String email;

    private LinearLayout ll_contact_personal_show_back;

    private TextView tv_contact_personal_show_edit;
    private TextView tv_contact_personal_show_name;
    private TextView tv_contact_personal_show_number_type;
    private TextView tv_contact_personal_show_number;
    private TextView tv_contact_personal_show_email;
    private TextView tv_contact_personal_show_remark;

    private ImageView im_contact_personal_show_icon;
    private ImageView im_contact_personal_show_call_icon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_personal_show);
    }

    @Override
    protected void initData() {

        data = (ContactListViewItemData) getIntent().getSerializableExtra(ContactBookFragement.CONTACT_SHOW);
        rawContactID = data.getRawContactID();
        name = data.getName();

        ContentResolver resolver = getContentResolver();

        Cursor phoneCursor = resolver.query(CommonDataKinds.Phone.CONTENT_URI, PERSON_INFOMATION_PROJECTION,
                ContactsContract.Contacts.Data.RAW_CONTACT_ID + "=?", new String[]{rawContactID}, null);


        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //获取电话号码
                number = phoneCursor.getString(PHONE_NUMBER_INDEX);

                //获取号码类型
                numberType = new GetStrPhoneType().getStrPhoneType(phoneCursor.getInt(PHONE_TYPE_INDEX));

            }

        }
        phoneCursor.close();



        //获取E-Mail
        /*Cursor emailCursor = resolver.query(CommonDataKinds.Phone.CONTENT_URI,
                new String[]{CommonDataKinds.Phone.DATA1},
                ContactsContract.Contacts.Data.RAW_CONTACT_ID + "=?"+" and "+ContactsContract.Contacts.Data.MIMETYPE + "=?",
                new String[]{rawContactID,"1"}, null);*/

        Cursor emailCursor = resolver.query(CommonDataKinds.Phone.CONTENT_URI,
                new String[]{CommonDataKinds.Phone.DATA1},
                ContactsContract.Contacts.Data.RAW_CONTACT_ID + "=?",
                new String[]{rawContactID}, null);
        if(emailCursor == null)
            System.out.print("========================================== NULL ======================================");

        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                //得到email
                email = emailCursor.getString(0);

            }
        }
        emailCursor.close();
    }

    @Override
    protected void initView() {

        ll_contact_personal_show_back = (LinearLayout) findViewById(R.id.ll_contact_personal_show_back);
        tv_contact_personal_show_edit = (TextView) findViewById(R.id.tv_contact_personal_show_edit);
        im_contact_personal_show_icon = (ImageView) findViewById(R.id.im_contact_personal_show_icon);
        tv_contact_personal_show_name = (TextView) findViewById(R.id.tv_contact_personal_show_name);
        tv_contact_personal_show_number_type = (TextView) findViewById(R.id.tv_contact_personal_show_number_type);
        tv_contact_personal_show_number = (TextView) findViewById(R.id.tv_contact_personal_show_number);
        im_contact_personal_show_call_icon = (ImageView) findViewById(R.id.im_contact_personal_show_call_icon);
        tv_contact_personal_show_email = (TextView) findViewById(R.id.tv_contact_personal_show_email);
        tv_contact_personal_show_remark = (TextView) findViewById(R.id.tv_contact_personal_show_remark);

        tv_contact_personal_show_name.setText(name);
        tv_contact_personal_show_number_type.setText(numberType);
        tv_contact_personal_show_number.setText(number);

        if(email != null)
            tv_contact_personal_show_email.setText(email);
        else
            tv_contact_personal_show_email.setText("");


    }

    @Override
    protected void initEvents() {
        ll_contact_personal_show_back.setOnClickListener(this);
        tv_contact_personal_show_edit.setOnClickListener(this);
        im_contact_personal_show_call_icon.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_contact_personal_show_back:
                this.finish();
                break;
            case R.id.tv_contact_personal_show_edit:
                break;
            case R.id.im_contact_personal_show_call_icon:
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number);
                    intent.setData(data);
                    startActivity(intent);
                }catch (SecurityException e){

                }finally {

                }
        }

    }
}
