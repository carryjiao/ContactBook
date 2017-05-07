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
import android.text.TextWatcher;
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
import com.carryj.root.contactbook.ui.DividerItemDecoration;

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
    private boolean updataFlag = false;

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
            updataFlag = false;

        }else if(SELECTOR.equals(FROM_DIAL_FRAGEMENT_ADD) ||SELECTOR.equals(FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW)) {
            insertFlag = true;
            updataFlag = false;
            phoneNumber = getIntent().getStringExtra("NUMBER");
            PhoneNumberData numberItemData = new PhoneNumberData();
            numberItemData.setNumber(phoneNumber);
            myNumberData.add(numberItemData);
        }else if(SELECTOR.equals(FROM_CONTACT_PERSONAL_SHOW_ACTIVITY_EDIT)) {
            insertFlag = false;
            updataFlag = true;
            name = getIntent().getStringExtra("NAME");
            company = getIntent().getStringExtra("COMPANY");
            remark = getIntent().getStringExtra("REMARK");
            lookUp = getIntent().getStringExtra("LOOKUP");
            myNumberData = (ArrayList<PhoneNumberData>) getIntent().getSerializableExtra("NUMBERDATA");
            myEmailData = (ArrayList<EmailData>) getIntent().getSerializableExtra("EMAILDATA");
            myImData = (ArrayList<ImData>) getIntent().getSerializableExtra("IMDATA");
        }

        values = new ContentValues();

        if(insertFlag) {
            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
            rawContactId = ContentUris.parseId(rawContactUri);

        }else if(updataFlag) {//查询需要修改的联系人的rawContactId
            Cursor cursor = getContentResolver().query(Data.CONTENT_URI,
                    new String[]{Data.RAW_CONTACT_ID},
                    Data.LOOKUP_KEY+"=?", new String[]{lookUp}, null);
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    rawContactId = cursor.getLong(0);
                }
                cursor.close();
            }
        }

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
        if(updataFlag) {
            et_add_contact_surname.setText(name);
            et_add_contact_company.setText(company);
            et_add_contact_remark.setText(remark);
        }


        /*电话号码模块*/
        numberAdapter = new AddContactNumberAdapter(this, myNumberData, updataFlag);
        add_contact_number_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        add_contact_number_recyclerview.setAdapter(numberAdapter);
        add_contact_number_recyclerview.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        /*电子邮箱模块*/
        emailAdapter = new AddContactEmailAdapter(this, myEmailData,updataFlag);
        add_contact_email_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        add_contact_email_recyclerview.setAdapter(emailAdapter);
        add_contact_email_recyclerview.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));

        /*及时通信模块*/
        imAdapter = new AddContactImAdapter(this, myImData,updataFlag);
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

        et_add_contact_remark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                remark = s.toString();
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
                if(updataFlag){
                    updata(name, company, myNumberData, myEmailData, myImData, remark);
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

            // 向data表插入姓名数据
            if (name.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
                values.put(StructuredName.GIVEN_NAME, name);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入公司数据
            if (company.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
                values.put(Organization.COMPANY, company);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入电话数据
            for(PhoneNumberData numberItemData:numberDatas) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                values.put(Phone.NUMBER, numberItemData.getNumber());
                values.put(Phone.TYPE, numberItemData.getNumberType());
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入Email数据
            for(EmailData emailItemData:emailDatas) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
                values.put(Email.DATA, emailItemData.getEmail());
                values.put(Email.TYPE, emailItemData.getEmailType());
                getContentResolver().insert(Data.CONTENT_URI, values);


            }

            // 向data表插入QQ数据
            for(ImData imItemData:imDatas) {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
                values.put(Im.DATA, imItemData.getIm());
                values.put(Im.PROTOCOL, imItemData.getImType());
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入备注数据
            if (remark.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Note.CONTENT_ITEM_TYPE);
                values.put(Note.NOTE, remark);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

        }

        catch (Exception e)
        {
            return false;
        }

        return true;

    }

    private boolean updata(String name, String company, ArrayList<PhoneNumberData> numberDatas,
                           ArrayList<EmailData> emailDatas,
                           ArrayList<ImData> imDatas, String remark) {

        try {
            //先删除后插入
            getContentResolver().delete(Data.CONTENT_URI,
                    Data.RAW_CONTACT_ID+"=?",new String[]{rawContactId+""});

            getContentResolver().insert(RawContacts.CONTENT_URI, values);

            insert(name,company,numberDatas,emailDatas,imDatas,remark);

        }catch (Exception e){
            return false;
        }
        return true;
    }


}
