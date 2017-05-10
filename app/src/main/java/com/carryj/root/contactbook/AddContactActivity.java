package com.carryj.root.contactbook;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.Data;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carryj.root.contactbook.adapter.AddContactEmailAdapter;
import com.carryj.root.contactbook.adapter.AddContactImAdapter;
import com.carryj.root.contactbook.adapter.AddContactNumberAdapter;
import com.carryj.root.contactbook.data.EmailData;
import com.carryj.root.contactbook.data.ImData;
import com.carryj.root.contactbook.data.PhoneNumberData;
import com.carryj.root.contactbook.event.NumberChangeEvent;
import com.carryj.root.contactbook.ui.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class AddContactActivity extends SweepBackActivity {

    private String SELECTOR;
    private static final String FROM_CONTACT_BOOK_FRAGEMENT_ADD = "FROM_CONTACT_BOOK_FRAGEMENT_ADD";
    private static final String FROM_DIAL_FRAGEMENT_ADD = "FROM_DIAL_FRAGEMENT_ADD";
    private static final String FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW = "FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW";
    private static final String FROM_CONTACT_PERSONAL_SHOW_ACTIVITY_EDIT = "FROM_CONTACT_PERSONAL_SHOW_ACTIVITY_EDIT";

    private static final int REQUEST_CODE = 1;
    private static final int RESULT_CODE = 100;

    private String lookUp;
    private String phoneNumber;
    private String name;
    private String company;
    private String remark;

    private long rawContactId;

    private TextView tv_add_contact_done;
    private TextView tv_add_contact_back_str;

    private ImageView im_add_contact_icon;

    private EditText et_add_contact_surname;
    private EditText et_add_contact_given_name;
    private EditText et_add_contact_company;
    private EditText et_add_contact_remark;

    private RecyclerView add_contact_number_recyclerview;
    private RecyclerView add_contact_email_recyclerview;
    private RecyclerView add_contact_im_recyclerview;

    private LinearLayout ll_add_contact_back;
    private LinearLayout ll_add_contact_number_add;
    private LinearLayout ll_add_contact_email_add;
    private LinearLayout ll_add_contact_im_add;

    private  ArrayList<PhoneNumberData> myNumberData = new ArrayList<PhoneNumberData>();
    private ArrayList<EmailData> myEmailData = new ArrayList<EmailData>();
    private ArrayList<ImData> myImData = new ArrayList<ImData>();

    private AddContactNumberAdapter numberAdapter;
    private AddContactEmailAdapter emailAdapter;
    private AddContactImAdapter imAdapter;

    private boolean insertFlag = false;
    private boolean updateFlag = false;
    private boolean nameFlag;
    private boolean remarkFlag;
    private boolean companyFlag;

    private ContentValues values;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
    }

    @Override
    protected void initData() {

        SELECTOR = getIntent().getStringExtra("SELECTOR");
        if(SELECTOR.equals(FROM_CONTACT_BOOK_FRAGEMENT_ADD)){
            insertFlag = true;
            updateFlag = false;

        }else if(SELECTOR.equals(FROM_DIAL_FRAGEMENT_ADD) ||SELECTOR.equals(FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW)) {
            if(SELECTOR.equals(FROM_DIAL_FRAGEMENT_ADD)) {
                insertFlag = true;
                updateFlag = false;
            }else {
                insertFlag = false;
                updateFlag = true;
            }

            phoneNumber = getIntent().getStringExtra("NUMBER");
            PhoneNumberData numberItemData = new PhoneNumberData();
            numberItemData.setNumber(phoneNumber);
            myNumberData.add(numberItemData);
        }else if(SELECTOR.equals(FROM_CONTACT_PERSONAL_SHOW_ACTIVITY_EDIT)) {
            insertFlag = false;
            updateFlag = true;

            name = getIntent().getStringExtra("NAME");
            nameFlag = getIntent().getBooleanExtra("NAMEFLAG",false);

            company = getIntent().getStringExtra("COMPANY");
            companyFlag = getIntent().getBooleanExtra("COMPANYFLAG", false);

            remark = getIntent().getStringExtra("REMARK");
            remarkFlag = getIntent().getBooleanExtra("REMARKFLAG", false);

            lookUp = getIntent().getStringExtra("LOOKUP");
            myNumberData = (ArrayList<PhoneNumberData>) getIntent().getSerializableExtra("NUMBERDATA");
            myEmailData = (ArrayList<EmailData>) getIntent().getSerializableExtra("EMAILDATA");
            myImData = (ArrayList<ImData>) getIntent().getSerializableExtra("IMDATA");
        }

        values = new ContentValues();



    }



    @Override
    protected void initView() {

        ll_add_contact_back = (LinearLayout) findViewById(R.id.ll_add_contact_back);
        tv_add_contact_back_str = (TextView) findViewById(R.id.tv_add_contact_back_str);
        tv_add_contact_done = (TextView) findViewById(R.id.tv_add_contact_done);
        im_add_contact_icon = (ImageView) findViewById(R.id.im_add_contact_icon);
        et_add_contact_surname = (EditText) findViewById(R.id.et_add_contact_surname);
        et_add_contact_given_name = (EditText) findViewById(R.id.et_add_contact_given_name);
        et_add_contact_company = (EditText) findViewById(R.id.et_add_contact_company);
        et_add_contact_remark = (EditText) findViewById(R.id.et_add_contact_remark);

        add_contact_number_recyclerview = (RecyclerView) findViewById(R.id.add_contact_number_recyclerview);
        ll_add_contact_number_add = (LinearLayout) findViewById(R.id.ll_add_contact_number_add);

        add_contact_email_recyclerview = (RecyclerView) findViewById(R.id.add_contact_email_recyclerview);
        ll_add_contact_email_add = (LinearLayout) findViewById(R.id.ll_add_contact_email_add);

        add_contact_im_recyclerview = (RecyclerView) findViewById(R.id.add_contact_im_recyclerview);
        ll_add_contact_im_add = (LinearLayout) findViewById(R.id.ll_add_contact_im_add);

        //返回处显示的字符串
        if(SELECTOR.equals(FROM_DIAL_FRAGEMENT_ADD)){
            tv_add_contact_back_str.setText("拨号界面");
        }else if(SELECTOR.equals(FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW)) {
            tv_add_contact_back_str.setText("通话记录");
        }

        /*个人信息模块*/
        if(updateFlag) {
            if(nameFlag) {
                et_add_contact_surname.setText(name);

            }
            et_add_contact_company.setText(company);
            et_add_contact_remark.setText(remark);
        }


        /*电话号码模块*/
        numberAdapter = new AddContactNumberAdapter(this, myNumberData, updateFlag);
        add_contact_number_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        add_contact_number_recyclerview.setAdapter(numberAdapter);
        add_contact_number_recyclerview.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        /*电子邮箱模块*/
        emailAdapter = new AddContactEmailAdapter(this, myEmailData, updateFlag);
        add_contact_email_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        add_contact_email_recyclerview.setAdapter(emailAdapter);
        add_contact_email_recyclerview.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        /*即时聊天模块*/
        imAdapter = new AddContactImAdapter(this, myImData, updateFlag);
        add_contact_im_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        add_contact_im_recyclerview.setAdapter(imAdapter);
        add_contact_im_recyclerview.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));





    }

    @Override
    protected void initEvents() {
        ll_add_contact_back.setOnClickListener(this);
        tv_add_contact_done.setOnClickListener(this);
        ll_add_contact_number_add.setOnClickListener(this);
        ll_add_contact_email_add.setOnClickListener(this);
        ll_add_contact_im_add.setOnClickListener(this);


        /*电话号码模块*/
        //删除按钮监听器
        numberAdapter.setOnItemListener(new AddContactNumberAdapter.OnItemListener() {
            @Override
            public void onClick(int position) {
                if(myNumberData != null && myNumberData.get(position) != null) {
                    String strNumber = myNumberData.get(position).getNumber();
                    if(strNumber != null && strNumber.length()>0) {
                        String _id = myNumberData.get(position).get_id();
                        getContentResolver().delete(Data.CONTENT_URI,
                                Data._ID+"=?", new String[]{_id});
                    }
                }
                numberAdapter.deleteNumberData(position);

            }
        });

        //电话号码类型监听器
        numberAdapter.setOnItemSpinnerListener(new AddContactNumberAdapter.OnItemSpinnerListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id, int listposition) {
                int type = position+1;
                myNumberData.get(listposition).setNumberType(type+"");//设置电话号码类型
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent, int listposition) {

            }
        });

        //电话号码监听器
        numberAdapter.myAddTextChangeListener(new AddContactNumberAdapter.TextChangeListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after, int listposition) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count, int listposition) {

            }

            @Override
            public void afterTextChanged(Editable s, int listposition) {
                myNumberData.get(listposition).setNumber(s.toString());//设置电话号码
            }
        });


        /*电子邮箱模块*/
        //删除按钮监听
        emailAdapter.setOnItemListener(new AddContactEmailAdapter.OnItemListener() {
            @Override
            public void onClick(int position) {
                if(myEmailData != null && myEmailData.get(position) != null) {
                    String email = myEmailData.get(position).getEmail();
                    if(email != null && email.length()>0) {
                        String _id = myEmailData.get(position).get_id();
                        getContentResolver().delete(Data.CONTENT_URI,
                                Data._ID+"=?", new String[]{_id});
                    }
                }
                emailAdapter.deleteEmailData(position);

            }
        });

        //电子邮箱类型监听器
        emailAdapter.setOnItemSpinnerListener(new AddContactEmailAdapter.OnItemSpinnerListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id, int listposition) {
                int type = position+1;
                myEmailData.get(listposition).setEmailType(type+"");//设置电子邮箱类型
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent, int listposition) {

            }
        });

        //电子邮箱号码监听器
        emailAdapter.myAddTextChangeListener(new AddContactEmailAdapter.TextChangeListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after, int listposition) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count, int listposition) {

            }

            @Override
            public void afterTextChanged(Editable s, int listposition) {
                myEmailData.get(listposition).setEmail(s.toString());//设置电子邮箱号
            }
        });

        /*即时通信模块*/
        //删除按钮监听
        imAdapter.setOnItemListener(new AddContactImAdapter.OnItemListener() {
            @Override
            public void onClick(int position) {
                if(myImData != null && myImData.get(position) != null) {
                    String im = myImData.get(position).getIm();
                    if(im != null && im.length()>0) {
                        String _id = myImData.get(position).get_id();
                        getContentResolver().delete(Data.CONTENT_URI,
                                Data._ID+"=?", new String[]{_id});
                    }
                }
                imAdapter.deleteImData(position);

            }
        });

        //即时通信类型监听器
        imAdapter.setOnItemSpinnerListener(new AddContactImAdapter.OnItemSpinnerListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id, int listposition) {
                int type = position+1;
                myImData.get(listposition).setImType(type+"");//设置电子邮箱类型
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent, int listposition) {

            }
        });

        //即时通信号码监听器
        imAdapter.myAddTextChangeListener(new AddContactImAdapter.TextChangeListener() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after, int listposition) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count, int listposition) {

            }

            @Override
            public void afterTextChanged(Editable s, int listposition) {
                myImData.get(listposition).setIm(s.toString());//设置电子邮箱号
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_add_contact_back:
                this.finish();
                break;
            case R.id.tv_add_contact_done:
                //获取名字
                StringBuilder sb = new StringBuilder();
                sb.append(et_add_contact_surname.getText().toString());
                sb.append(et_add_contact_given_name.getText().toString());
                name = sb.toString();

                //获取备注
                remark = et_add_contact_remark.getText().toString();

                //获取公司名称
                company = et_add_contact_company.getText().toString();


                if(insertFlag){
                    insert(name, company, myNumberData, myEmailData, myImData, remark);
                    Toast.makeText(this,"添加成功",Toast.LENGTH_SHORT).show();
                }
                if(updateFlag){
                    update(name, company, myNumberData, myEmailData, myImData, remark);
                    Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("NUMBER",myNumberData);
                    bundle.putSerializable("EMAIL",myEmailData);
                    bundle.putSerializable("IM",myImData);
                    intent.putExtras(bundle);
                    intent.putExtra("NAME",name);
                    intent.putExtra("COMPANY",company);
                    intent.putExtra("REMARK",remark);
                    this.setResult(RESULT_CODE,intent);
                }

                EventBus.getDefault().post(new NumberChangeEvent(true));//通知ContactBook刷新数据
                this.finish();
                break;
            case R.id.ll_add_contact_number_add:
                numberAdapter.addNumberData(numberAdapter.getItemCount());
                break;
            case R.id.ll_add_contact_email_add:
                emailAdapter.addEmailData(emailAdapter.getItemCount());
                break;
            case R.id.ll_add_contact_im_add:
                imAdapter.addImData(imAdapter.getItemCount());
                break;
            default:
                break;
        }

    }

    private boolean insert(String name, String company, ArrayList<PhoneNumberData> numberDatas,
                           ArrayList<EmailData> emailDatas, ArrayList<ImData> imDatas, String remark) {

        try
        {
            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
            rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (name.length()>0)
            {
                insertName(name);
            }

            // 向data表插入公司数据
            if (company.length()>0)
            {
                insertCompany(company);
            }

            // 向data表插入电话数据
            for(PhoneNumberData numberItemData:numberDatas) {
                insertPhoneNumber(numberItemData);
            }

            // 向data表插入Email数据
            for(EmailData emailItemData:emailDatas) {
                insertEmailData(emailItemData);
            }

            // 向data表插入IM数据
            for(ImData imItemData:imDatas) {
                insertImData(imItemData);
            }

            // 向data表插入备注数据
            if (remark.length()>0)
            {
                insertNote(remark);
            }

        }

        catch (Exception e)
        {
            return false;
        }
        return true;
    }

    private boolean update(String name, String company, ArrayList<PhoneNumberData> numberDatas,
                           ArrayList<EmailData> emailDatas,
                           ArrayList<ImData> imDatas, String remark) {

        try {

            /*最新思路:每条记录都有一个ID,让每个data保存自己的ID*/

            //查询需要修改的联系人的rawContactId
            Cursor cursor = getContentResolver().query(Data.CONTENT_URI,
                    new String[]{Data.RAW_CONTACT_ID},
                    Data.LOOKUP_KEY+"=?", new String[]{lookUp}, null);
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    rawContactId = cursor.getLong(0);
                }
                cursor.close();
            }

            // 在data表中更新姓名数据
            if (name.length()>0)
            {
                if(nameFlag) {
                    values.clear();
                    values.put("data1", name);
                    getContentResolver().update(Data.CONTENT_URI, values,
                            Data.RAW_CONTACT_ID+"=? AND "+Data.MIMETYPE+"=?",
                            new String[]{rawContactId+"", StructuredName.CONTENT_ITEM_TYPE});

                }else {

                    insertName(name);

                }

            }/*else {
                getContentResolver().delete(Data.CONTENT_URI,
                        Data.RAW_CONTACT_ID+"=? AND "+Data.MIMETYPE+"=?",
                        new String[]{rawContactId+"", StructuredName.CONTENT_ITEM_TYPE});
                Log.d("update_nameFlag","===============deleteName");
            }*/

            // 在data表中更新公司数据
            if (company.length()>0)
            {
                if(companyFlag) {

                    values.clear();
                    values.put(Organization.COMPANY, company);
                    getContentResolver().update(Data.CONTENT_URI, values,
                            Data.RAW_CONTACT_ID+"=? AND "+Data.MIMETYPE+"=?",
                            new String[]{rawContactId+"", Organization.CONTENT_ITEM_TYPE});
                }else {

                    insertCompany(company);

                }

            }else {

                getContentResolver().delete(Data.CONTENT_URI,
                        Data.RAW_CONTACT_ID+"=? AND "+Data.MIMETYPE+"=?",
                        new String[]{rawContactId+"", Organization.CONTENT_ITEM_TYPE});

            }

            // 在data表中更新备注数据
            if (remark.length()>0)
            {
                if (remarkFlag) {
                    values.clear();
                    values.put(Note.NOTE, remark);
                    getContentResolver().update(Data.CONTENT_URI, values,
                            Data.RAW_CONTACT_ID+"=? AND "+Data.MIMETYPE+"=?",
                            new String[]{rawContactId+"", Note.CONTENT_ITEM_TYPE});
                 }else {
                    insertNote(remark);
                }

            }else {
                getContentResolver().delete(Data.CONTENT_URI,
                        Data.RAW_CONTACT_ID+"=? AND "+Data.MIMETYPE+"=?",
                        new String[]{rawContactId+"", Note.CONTENT_ITEM_TYPE});
            }


            // 在data表中更新电话数据
            for(PhoneNumberData numberItemData:numberDatas) {
                if(numberItemData.get_id() != null){ //不是新增数据
                    values.clear();
                    values.put(Phone.NUMBER, numberItemData.getNumber());
                    values.put(Phone.TYPE, numberItemData.getNumberType());
                    getContentResolver().update(Data.CONTENT_URI, values,
                            Data._ID+"=?", new String[]{numberItemData.get_id()});

                }else {//新增数据
                    insertPhoneNumber(numberItemData);

                }


            }

            // 在data表中更新Email数据
            for(EmailData emailItemData:emailDatas) {
                if(emailItemData.get_id() != null) {
                    values.clear();
                    values.put(Email.DATA, emailItemData.getEmail());
                    values.put(Email.TYPE, emailItemData.getEmailType());
                    getContentResolver().update(Data.CONTENT_URI, values,
                            Data._ID+"=?", new String[]{emailItemData.get_id()});

                }else {
                    insertEmailData(emailItemData);

                }



            }

            // 在data表中更新IM数据
            for(ImData imItemData:imDatas) {
                if(imItemData.get_id() != null) {
                    values.clear();
                    values.put(Im.DATA, imItemData.getIm());
                    values.put(Im.PROTOCOL, imItemData.getImType());
                    getContentResolver().update(Data.CONTENT_URI, values,
                            Data._ID+"=?", new String[]{imItemData.get_id()});

                }else {
                    insertImData(imItemData);

                }

            }

        }catch (Exception e){
            return false;
        }
        return true;
    }

    private void insertName(String name) {
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
        values.put(StructuredName.DISPLAY_NAME, name);
        getContentResolver().insert(Data.CONTENT_URI, values);
    }

    private void insertCompany(String company) {
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
        values.put(Organization.COMPANY, company);
        getContentResolver().insert(Data.CONTENT_URI, values);
    }

    private void insertNote(String note) {
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE);
        values.put(Note.NOTE, note);
        getContentResolver().insert(Data.CONTENT_URI, values);
    }

    private void insertPhoneNumber(PhoneNumberData numberItemData) {
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
        values.put(Phone.NUMBER, numberItemData.getNumber());
        values.put(Phone.TYPE, numberItemData.getNumberType());
        getContentResolver().insert(Data.CONTENT_URI, values);
    }

    private void insertEmailData(EmailData emailItemData) {
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
        values.put(Email.DATA, emailItemData.getEmail());
        values.put(Email.TYPE, emailItemData.getEmailType());
        getContentResolver().insert(Data.CONTENT_URI, values);
    }

    private void insertImData(ImData imItemData) {
        values.clear();
        values.put(Data.RAW_CONTACT_ID, rawContactId);
        values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
        values.put(Im.DATA, imItemData.getIm());
        values.put(Im.PROTOCOL, imItemData.getImType());
        getContentResolver().insert(Data.CONTENT_URI, values);
    }


}
