package com.carryj.root.contactbook;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carryj.root.contactbook.adapter.ContactPersonalShowNumberAdapter;
import com.carryj.root.contactbook.data.ContactListViewItemData;
import com.carryj.root.contactbook.data.PhoneNumberData;
import com.carryj.root.contactbook.fragments.ContactBookFragement;
import com.carryj.root.contactbook.tools.GetStrPhoneType;
import com.carryj.root.contactbook.ui.DividerItemDecoration;

import java.util.ArrayList;

public class ContactPersonalShowActivity extends SweepBackActivity {


    private ContactListViewItemData data;

    private String lookUp;
    private String name;
    private String email;
    private String remark;

    private ArrayList<PhoneNumberData> numberDatas = new ArrayList<PhoneNumberData>();

    private LinearLayout ll_contact_personal_show_back;
    private LinearLayout ll_contact_personal_show_email_block;
    private LinearLayout ll_contact_personal_show_remark_block;

    private TextView tv_contact_personal_show_edit;
    private TextView tv_contact_personal_show_name;
    private TextView tv_contact_personal_show_email;
    private TextView tv_contact_personal_show_remark;

    private ImageView im_contact_personal_show_icon;

    private RecyclerView rv_contact_personal_show_number;
    private ContactPersonalShowNumberAdapter numberAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_personal_show);
    }

    @Override
    protected void initData() {

        data = (ContactListViewItemData) getIntent().getSerializableExtra(ContactBookFragement.CONTACT_SHOW);
        lookUp = data.getLookUp();
        name = data.getName();

        ContentResolver resolver = getContentResolver();

        Cursor phoneCursor = resolver.query(CommonDataKinds.Phone.CONTENT_URI,
                new String[]{CommonDataKinds.Phone.TYPE, CommonDataKinds.Phone.NUMBER},
                CommonDataKinds.Phone.LOOKUP_KEY + "=?",
                new String[]{lookUp}, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                PhoneNumberData numberData = new PhoneNumberData();

                //获取号码类型
                String numberType = new GetStrPhoneType().getStrPhoneType(phoneCursor.getInt(0));
                String number = phoneCursor.getString(1);
                numberData.setNumberType(numberType);
                numberData.setNumber(number);
                numberDatas.add(numberData);

            }
            phoneCursor.close();

        }



        //获取E-Mail
        Cursor emailCursor = resolver.query(CommonDataKinds.Email.CONTENT_URI,
                null, CommonDataKinds.Email.LOOKUP_KEY + "=?",
                new String[]{lookUp}, null);

        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                //得到email
                email = emailCursor.getString(emailCursor.getColumnIndex(CommonDataKinds.Email.DATA));

            }
            emailCursor.close();
        }

        //获取备注
        Cursor remarkCursor = resolver.query(ContactsContract.Data.CONTENT_URI, null,
                ContactsContract.Data.LOOKUP_KEY + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                new String[]{lookUp, CommonDataKinds.Note.CONTENT_ITEM_TYPE}, null);

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
        rv_contact_personal_show_number = (RecyclerView) findViewById(R.id.rv_contact_personal_show_number);

        ll_contact_personal_show_email_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_email_block);
        tv_contact_personal_show_email = (TextView) findViewById(R.id.tv_contact_personal_show_email);

        ll_contact_personal_show_remark_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_remark_block);
        tv_contact_personal_show_remark = (TextView) findViewById(R.id.tv_contact_personal_show_remark);

        tv_contact_personal_show_name.setText(name);

        numberAdapter = new ContactPersonalShowNumberAdapter(this,numberDatas);
        rv_contact_personal_show_number.setLayoutManager(new LinearLayoutManager(this));
        rv_contact_personal_show_number.setAdapter(numberAdapter);
        rv_contact_personal_show_number.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));


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

        numberAdapter.setOnItemClickListener(new ContactPersonalShowNumberAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                try {
                    String number = numberDatas.get(position).getNumber();
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    Uri data = Uri.parse("tel:" + number);
                    intent.setData(data);
                    startActivity(intent);
                }catch (SecurityException e){

                }finally {

                }

            }
        });

        //长按将号码复制到系统粘贴板上
        numberAdapter.setOnItemLongClickListener(new ContactPersonalShowNumberAdapter.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View v, int position) {
                String number = numberDatas.get(position).getNumber();
                ClipboardManager clip = (ClipboardManager)getApplicationContext().
                        getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(number);
                Toast.makeText(getApplicationContext(), "已复制到粘贴板",Toast.LENGTH_SHORT).show();

                return false;
            }
        });

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
            default:
                break;
        }

    }
}
