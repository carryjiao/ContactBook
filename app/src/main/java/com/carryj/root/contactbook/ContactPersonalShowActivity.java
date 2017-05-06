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

import com.carryj.root.contactbook.activity.ShowORCodeActivity;
import com.carryj.root.contactbook.adapter.ContactPersonalShowEmailAdapter;
import com.carryj.root.contactbook.adapter.ContactPersonalShowImAdapter;
import com.carryj.root.contactbook.adapter.ContactPersonalShowNumberAdapter;
import com.carryj.root.contactbook.data.ContactListViewItemData;
import com.carryj.root.contactbook.data.EmailData;
import com.carryj.root.contactbook.data.ImData;
import com.carryj.root.contactbook.data.PhoneNumberData;
import com.carryj.root.contactbook.fragments.ContactBookFragement;
import com.carryj.root.contactbook.tools.GetStrPhoneType;
import com.carryj.root.contactbook.tools.PhoneNumberTransformer;
import com.carryj.root.contactbook.ui.DividerItemDecoration;

import java.util.ArrayList;

public class ContactPersonalShowActivity extends SweepBackActivity {


    public static final String SELECTOR = "SELECTOR";
    public static final String FROM_COLLECT_FRAGMENT = "FROM_COLLECT_FRAGMENT";
    public static final String FROM_CONTACT_BOOK_FRAGMENT = "FROM_CONTACT_BOOK_FRAGMENT";
    public static final String NAME = "NAME";
    public static final String LOOKUP = "LOOKUP";

    private ContactListViewItemData data;

    private String selector;
    private String lookUp;
    private String name;
    private String remark;

    private String backStr;

    private LinearLayout ll_contact_personal_show_back;
    private LinearLayout ll_contact_personal_show_email_block;
    private LinearLayout ll_contact_personal_show_im_block;
    private LinearLayout ll_contact_personal_show_remark_block;

    private TextView tv_back_str;
    private TextView tv_contact_personal_show_edit;
    private TextView tv_contact_personal_show_name;
    private TextView tv_contact_personal_show_remark;

    private ImageView im_contact_personal_show_icon;

    private RecyclerView rv_contact_personal_show_number;
    private RecyclerView rv_contact_personal_show_emial;
    private RecyclerView rv_contact_personal_show_im;


    private ContactPersonalShowNumberAdapter numberAdapter;
    private ContactPersonalShowEmailAdapter emailAdapter;
    private ContactPersonalShowImAdapter imAdapter;


    private ArrayList<PhoneNumberData> numberDatas = new ArrayList<PhoneNumberData>();
    private ArrayList<EmailData> emailDatas = new ArrayList<EmailData>();
    private ArrayList<ImData> imDatas = new ArrayList<ImData>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_personal_show);
    }

    @Override
    protected void initData() {

        selector = getIntent().getStringExtra(SELECTOR);
        if(selector.equals(FROM_COLLECT_FRAGMENT)){
            backStr = "个人收藏";
        }else if(selector.equals(FROM_CONTACT_BOOK_FRAGMENT)) {
            backStr = "所有联系人";
        }

        name = getIntent().getStringExtra(NAME);
        lookUp = getIntent().getStringExtra(LOOKUP);

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
                PhoneNumberTransformer pntf = new PhoneNumberTransformer();
                pntf.setStrPhoneNumber(number);
                number = pntf.getStrPhoneNumber();
                numberData.setNumberType(numberType);
                numberData.setNumber(number);
                numberDatas.add(numberData);

            }
            phoneCursor.close();

        }



        //获取E-Mail
        Cursor emailCursor = resolver.query(CommonDataKinds.Email.CONTENT_URI,
                new String[]{CommonDataKinds.Email.TYPE, CommonDataKinds.Email.DATA},
                CommonDataKinds.Email.LOOKUP_KEY + "=?",
                new String[]{lookUp}, null);

        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                EmailData emailData = new EmailData();
                //得到email
                String emailType = emailCursor.getString(0);
                String email = emailCursor.getString(1);
                emailData.setEmailType(emailType);
                emailData.setEmail(email);

                emailDatas.add(emailData);

            }
            emailCursor.close();
        }

        //获取IM
        Cursor imCursor = resolver.query(ContactsContract.Data.CONTENT_URI,
                new String[]{CommonDataKinds.Im.TYPE, CommonDataKinds.Im.DATA},
                ContactsContract.Data.LOOKUP_KEY + "=?",
                new String[]{lookUp}, null);

        if (imCursor != null) {
            while (imCursor.moveToNext()) {
                ImData imData = new ImData();
                //得到email
                String imType = imCursor.getString(0);
                String im = imCursor.getString(1);
                imData.setImType(imType);
                imData.setIm(im);
                imDatas.add(imData);

            }
            imCursor.close();
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
        tv_back_str = (TextView) findViewById(R.id.tv_back_str);
        tv_contact_personal_show_edit = (TextView) findViewById(R.id.tv_contact_personal_show_edit);
        im_contact_personal_show_icon = (ImageView) findViewById(R.id.im_contact_personal_show_icon);
        tv_contact_personal_show_name = (TextView) findViewById(R.id.tv_contact_personal_show_name);
        rv_contact_personal_show_number = (RecyclerView) findViewById(R.id.rv_contact_personal_show_number);

        ll_contact_personal_show_email_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_email_block);
        rv_contact_personal_show_emial = (RecyclerView) findViewById(R.id.rv_contact_personal_show_emial);

        ll_contact_personal_show_im_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_im_block);
        rv_contact_personal_show_im = (RecyclerView) findViewById(R.id.rv_contact_personal_show_im);

        ll_contact_personal_show_remark_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_remark_block);
        tv_contact_personal_show_remark = (TextView) findViewById(R.id.tv_contact_personal_show_remark);

        tv_back_str.setText(backStr);
        tv_contact_personal_show_name.setText(name);

        numberAdapter = new ContactPersonalShowNumberAdapter(this,numberDatas);
        rv_contact_personal_show_number.setLayoutManager(new LinearLayoutManager(this));
        rv_contact_personal_show_number.setAdapter(numberAdapter);
        rv_contact_personal_show_number.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));


        if(emailDatas == null){

            ll_contact_personal_show_email_block.setVisibility(View.GONE);

        }
        else {

            emailAdapter = new ContactPersonalShowEmailAdapter(this,emailDatas);
            rv_contact_personal_show_emial.setLayoutManager(new LinearLayoutManager(this));
            rv_contact_personal_show_emial.setAdapter(emailAdapter);
            rv_contact_personal_show_emial.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));

        }


        if(imDatas == null){

            ll_contact_personal_show_im_block.setVisibility(View.GONE);

        }
        else {

            imAdapter = new ContactPersonalShowImAdapter(this,imDatas);
            rv_contact_personal_show_im.setLayoutManager(new LinearLayoutManager(this));
            rv_contact_personal_show_im.setAdapter(imAdapter);
            rv_contact_personal_show_im.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));

        }

        if(remark == null)
            ll_contact_personal_show_remark_block.setVisibility(View.GONE);
        else
            tv_contact_personal_show_remark.setText(remark);



    }

    @Override
    protected void initEvents() {
        ll_contact_personal_show_back.setOnClickListener(this);
        tv_contact_personal_show_edit.setOnClickListener(this);

        //拨号
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

        //点击跳转到二维码界面
        numberAdapter.setOnItemClicktoORCodeListenner(new ContactPersonalShowNumberAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String number = numberDatas.get(position).getNumber();
                Intent intent = new Intent(ContactPersonalShowActivity.this.getApplicationContext(), ShowORCodeActivity.class);
                intent.putExtra("ORCODE", number);
                startActivity(intent);
            }
        });

        //长按将电子邮箱号码复制到系统粘贴板上
        emailAdapter.setOnItemLongClickListener(new ContactPersonalShowEmailAdapter.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View v, int position) {
                String email = emailDatas.get(position).getEmail();
                ClipboardManager clip = (ClipboardManager)getApplicationContext().
                        getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(email);
                Toast.makeText(getApplicationContext(), "已复制到粘贴板",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //点击跳转到二维码界面
        emailAdapter.setOnItemClickListener(new ContactPersonalShowEmailAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String email = emailDatas.get(position).getEmail();
                Intent intent = new Intent(ContactPersonalShowActivity.this.getApplicationContext(), ShowORCodeActivity.class);
                intent.putExtra("ORCODE", email);
                startActivity(intent);
            }
        });

        //长按将即时聊天号码复制到系统粘贴板上
        imAdapter.setOnItemLongClickListener(new ContactPersonalShowImAdapter.OnItemLongClickListener() {
            @Override
            public boolean onLongClick(View v, int position) {
                String im = imDatas.get(position).getIm();
                ClipboardManager clip = (ClipboardManager)getApplicationContext().
                        getSystemService(Context.CLIPBOARD_SERVICE);
                clip.setText(im);
                Toast.makeText(getApplicationContext(), "已复制到粘贴板",Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        //点击跳转到二维码界面
        imAdapter.setOnItemClickListener(new ContactPersonalShowImAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String im = imDatas.get(position).getIm();
                Intent intent = new Intent(ContactPersonalShowActivity.this.getApplicationContext(), ShowORCodeActivity.class);
                intent.putExtra("ORCODE", im);
                startActivity(intent);
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
