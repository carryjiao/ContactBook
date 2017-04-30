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
import com.carryj.root.contactbook.tools.PhoneNumberTransformer;

public class ContactPersonalShowActivity extends SweepBackActivity {

    private String[] PERSON_INFOMATION_PROJECTION = new String[]{
            CommonDataKinds.Phone.TYPE,
            CommonDataKinds.Phone.NUMBER
    };

    private static int PHONE_TYPE_INDEX = 0;
    private static int PHONE_NUMBER_INDEX = 1;




    private ContactListViewItemData data;

    private String rawContactID;
    private String contactID;
    private String name;
    private String number;
    private String numberType;
    private String email;
    private String remark;

    private LinearLayout ll_contact_personal_show_back;
    private LinearLayout ll_contact_personal_show_email_block;
    private LinearLayout ll_contact_personal_show_remark_block;

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
        contactID = data.getContactID();
        name = data.getName();

        ContentResolver resolver = getContentResolver();

        Cursor phoneCursor = resolver.query(CommonDataKinds.Phone.CONTENT_URI, PERSON_INFOMATION_PROJECTION,
                CommonDataKinds.Phone.CONTACT_ID + "=?", new String[]{contactID}, null);


        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                //获取电话号码
                number = phoneCursor.getString(PHONE_NUMBER_INDEX);

                //获取号码类型
                numberType = new GetStrPhoneType().getStrPhoneType(phoneCursor.getInt(PHONE_TYPE_INDEX));

            }
            phoneCursor.close();

        }


        PhoneNumberTransformer pntf = new PhoneNumberTransformer();
        pntf.setStrPhoneNumber(number);
        number = pntf.getStrPhoneNumber();



        //获取E-Mail
        Cursor emailCursor = resolver.query(CommonDataKinds.Email.CONTENT_URI,
                null, CommonDataKinds.Email.CONTACT_ID + "=?",
                new String[]{contactID}, null);

        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                //得到email
                email = emailCursor.getString(emailCursor.getColumnIndex(CommonDataKinds.Email.DATA));

            }
            emailCursor.close();
        }

        //获取备注
        Cursor remarkCursor = resolver.query(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                new String[]{contactID, CommonDataKinds.Note.CONTENT_ITEM_TYPE}, null);

        if (remarkCursor != null) {
            while (remarkCursor.moveToNext()) {
                //得到备注
                remark = remarkCursor.getString(remarkCursor.getColumnIndex(CommonDataKinds.Note.NOTE));

            }
            remarkCursor.close();
        }

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

        ll_contact_personal_show_email_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_email_block);
        tv_contact_personal_show_email = (TextView) findViewById(R.id.tv_contact_personal_show_email);

        ll_contact_personal_show_remark_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_remark_block);
        tv_contact_personal_show_remark = (TextView) findViewById(R.id.tv_contact_personal_show_remark);

        tv_contact_personal_show_name.setText(name);
        tv_contact_personal_show_number_type.setText(numberType);
        tv_contact_personal_show_number.setText(number);

        if(email == null)
            ll_contact_personal_show_email_block.setVisibility(View.GONE);
        else
            tv_contact_personal_show_email.setText(email);

        if(remark == null)
            ll_contact_personal_show_remark_block.setVisibility(View.GONE);
        else
            tv_contact_personal_show_remark.setText(remark);



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
