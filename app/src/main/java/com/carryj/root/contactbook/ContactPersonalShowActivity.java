package com.carryj.root.contactbook;

import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Note;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.carryj.root.contactbook.event.DialEvent;
import com.carryj.root.contactbook.tools.GetStrEmailType;
import com.carryj.root.contactbook.tools.GetStrImType;
import com.carryj.root.contactbook.tools.GetStrPhoneType;
import com.carryj.root.contactbook.tools.PhoneNumberTransformer;
import com.carryj.root.contactbook.ui.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class ContactPersonalShowActivity extends SweepBackActivity {


    public static final String SELECTOR = "SELECTOR";
    public static final String FROM_COLLECT_FRAGMENT = "FROM_COLLECT_FRAGMENT";
    public static final String FROM_CONTACT_BOOK_FRAGMENT = "FROM_CONTACT_BOOK_FRAGMENT";
    public static final String NAME = "NAME";
    public static final String LOOKUP = "LOOKUP";
    private static final String FROM_CONTACT_PERSONAL_SHOW_ACTIVITY_EDIT = "FROM_CONTACT_PERSONAL_SHOW_ACTIVITY_EDIT";

    private static final int REQUEST_CODE = 1;
    private static final int RESULT_CODE = 100;

    private ContactListViewItemData data;

    private String selector;
    private String lookUp;
    private String name;
    private String remark;
    private String company;

    private String backStr;

    private LinearLayout ll_contact_personal_show_back;
    private LinearLayout ll_contact_personal_show_number_block;
    private LinearLayout ll_contact_personal_show_email_block;
    private LinearLayout ll_contact_personal_show_im_block;
    private LinearLayout ll_contact_personal_show_remark_block;
    private LinearLayout ll_contact_personal_show_company_block;
    private LinearLayout ll_contact_personal_show_orcode;
    private LinearLayout ll_contact_personal_show_delete;

    private TextView tv_back_str;
    private TextView tv_contact_personal_show_edit;
    private TextView tv_contact_personal_show_name;
    private TextView tv_contact_personal_show_remark;
    private TextView tv_contact_personal_show_company;

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

        lookUp = getIntent().getStringExtra(LOOKUP);

        ContentResolver resolver = getContentResolver();

        //查询联系人姓名
        Cursor nameCursor = resolver.query(Phone.CONTENT_URI, new String[]{Phone.DISPLAY_NAME},
                Phone.LOOKUP_KEY+"=?",new String[]{lookUp},null);
        if (nameCursor != null) {
            while (nameCursor.moveToNext()) {
                name = nameCursor.getString(0);
            }
        }

        //查询联系人电话号码
        Cursor phoneCursor = resolver.query(Data.CONTENT_URI,
                new String[]{Phone.TYPE, Phone.NUMBER, Data._ID},
                Data.LOOKUP_KEY + "=? AND "+Data.MIMETYPE+"=?",
                new String[]{lookUp,Phone.CONTENT_ITEM_TYPE}, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                PhoneNumberData numberData = new PhoneNumberData();

                //获取号码类型
                String numberType = new GetStrPhoneType().getStrPhoneType(phoneCursor.getInt(0));
                String number = phoneCursor.getString(1);
                String _id = phoneCursor.getString(2);
                PhoneNumberTransformer pntf = new PhoneNumberTransformer();
                pntf.setStrPhoneNumber(number);
                number = pntf.getStrPhoneNumber();
                numberData.setNumberType(numberType);
                numberData.setNumber(number);
                numberData.set_id(_id);
                numberDatas.add(numberData);

            }
            phoneCursor.close();

        }



        //获取E-Mail
        Cursor emailCursor = resolver.query(Data.CONTENT_URI,
                new String[]{Email.TYPE, Email.DATA, Data._ID},
                Data.LOOKUP_KEY + "=? AND "+Data.MIMETYPE+"=?",
                new String[]{lookUp,Email.CONTENT_ITEM_TYPE}, null);

        if (emailCursor != null) {
            while (emailCursor.moveToNext()) {
                EmailData emailData = new EmailData();
                //得到email
                int type = emailCursor.getInt(0);
                String email = emailCursor.getString(1);
                String _id = emailCursor.getString(2);
                String emailType = new GetStrEmailType().getStrEmailType(type);
                emailData.setEmailType(emailType);
                emailData.setEmail(email);
                emailData.set_id(_id);
                emailDatas.add(emailData);

            }
            emailCursor.close();
        }

        //获取IM
        Cursor imCursor = resolver.query(Data.CONTENT_URI,
                new String[]{Im.PROTOCOL, Im.DATA, Data._ID},
                Data.LOOKUP_KEY + "=? AND "+ Data.MIMETYPE+"=?",
                new String[]{lookUp, Im.CONTENT_ITEM_TYPE}, null);

        if (imCursor != null) {
            while (imCursor.moveToNext()) {
                ImData imData = new ImData();
                //得到email
                int type = imCursor.getInt(0);
                String im = imCursor.getString(1);
                String _id = imCursor.getString(2);
                Log.d("type = ", type+"");
                String imType = new GetStrImType().getStrImType(type);
                Log.d("imType = ",imType);
                imData.setImType(imType);
                imData.setIm(im);
                imData.set_id(_id);
                imDatas.add(imData);

            }
            imCursor.close();
        }

        //获取备注
        Cursor remarkCursor = resolver.query(Data.CONTENT_URI, null,
                Data.LOOKUP_KEY + " = ? AND " + Data.MIMETYPE + " = ?",
                new String[]{lookUp, Note.CONTENT_ITEM_TYPE}, null);

        if (remarkCursor != null) {
            while (remarkCursor.moveToNext()) {
                //得到备注
                remark = remarkCursor.getString(remarkCursor.getColumnIndex(Note.NOTE));

            }
            remarkCursor.close();
        }

        //获取公司
        Cursor companyCursor = resolver.query(Data.CONTENT_URI, null,
                Data.LOOKUP_KEY + " = ? AND " + Data.MIMETYPE + " = ?",
                new String[]{lookUp, Organization.CONTENT_ITEM_TYPE}, null);
        if(companyCursor != null) {
            while (companyCursor.moveToNext()) {

                company = companyCursor.getString(companyCursor.getColumnIndex(Organization.COMPANY));
            }
            companyCursor.close();
        }

    }

    @Override
    protected void initView() {

        ll_contact_personal_show_back = (LinearLayout) findViewById(R.id.ll_contact_personal_show_back);
        tv_back_str = (TextView) findViewById(R.id.tv_back_str);
        tv_contact_personal_show_edit = (TextView) findViewById(R.id.tv_contact_personal_show_edit);
        im_contact_personal_show_icon = (ImageView) findViewById(R.id.im_contact_personal_show_icon);
        tv_contact_personal_show_name = (TextView) findViewById(R.id.tv_contact_personal_show_name);
        ll_contact_personal_show_number_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_number_block);
        rv_contact_personal_show_number = (RecyclerView) findViewById(R.id.rv_contact_personal_show_number);
        ll_contact_personal_show_orcode = (LinearLayout) findViewById(R.id.ll_contact_personal_show_orcode);
        ll_contact_personal_show_delete = (LinearLayout) findViewById(R.id.ll_contact_personal_show_delete);

        ll_contact_personal_show_email_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_email_block);
        rv_contact_personal_show_emial = (RecyclerView) findViewById(R.id.rv_contact_personal_show_emial);

        ll_contact_personal_show_im_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_im_block);
        rv_contact_personal_show_im = (RecyclerView) findViewById(R.id.rv_contact_personal_show_im);

        ll_contact_personal_show_remark_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_remark_block);
        tv_contact_personal_show_remark = (TextView) findViewById(R.id.tv_contact_personal_show_remark);

        ll_contact_personal_show_company_block = (LinearLayout) findViewById(R.id.ll_contact_personal_show_company_block);
        tv_contact_personal_show_company = (TextView) findViewById(R.id.tv_contact_personal_show_company);

        tv_back_str.setText(backStr);
        tv_contact_personal_show_name.setText(name);

        numberAdapter = new ContactPersonalShowNumberAdapter(this,numberDatas);
        emailAdapter = new ContactPersonalShowEmailAdapter(this,emailDatas);
        imAdapter = new ContactPersonalShowImAdapter(this,imDatas);

        if(numberDatas.size()==0) {

            ll_contact_personal_show_number_block.setVisibility(View.GONE);

        }else {

            rv_contact_personal_show_number.setLayoutManager(new LinearLayoutManager(this));
            rv_contact_personal_show_number.setAdapter(numberAdapter);
            rv_contact_personal_show_number.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));

        }


        if(emailDatas.size()==0){

            ll_contact_personal_show_email_block.setVisibility(View.GONE);

        }
        else {

            rv_contact_personal_show_emial.setLayoutManager(new LinearLayoutManager(this));
            rv_contact_personal_show_emial.setAdapter(emailAdapter);
            rv_contact_personal_show_emial.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));

        }


        if(imDatas.size()==0){

            ll_contact_personal_show_im_block.setVisibility(View.GONE);

        }
        else {

            rv_contact_personal_show_im.setLayoutManager(new LinearLayoutManager(this));
            rv_contact_personal_show_im.setAdapter(imAdapter);
            rv_contact_personal_show_im.addItemDecoration(new DividerItemDecoration(this,
                    DividerItemDecoration.VERTICAL_LIST));

        }

        if(remark != null && remark.length()>0)
            tv_contact_personal_show_remark.setText(remark);
        else
            ll_contact_personal_show_remark_block.setVisibility(View.GONE);



        if(company != null && company.length()>0)
            tv_contact_personal_show_company.setText(company);
        else
            ll_contact_personal_show_company_block.setVisibility(View.GONE);


    }

    @Override
    protected void initEvents() {
        ll_contact_personal_show_back.setOnClickListener(this);
        tv_contact_personal_show_edit.setOnClickListener(this);
        ll_contact_personal_show_orcode.setOnClickListener(this);
        ll_contact_personal_show_delete.setOnClickListener(this);

        //拨号
        numberAdapter.setOnItemClickListener(new ContactPersonalShowNumberAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                try {
                    EventBus.getDefault().post(new DialEvent(false,true));
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

        /*//点击跳转到二维码界面
        numberAdapter.setOnItemClicktoORCodeListenner(new ContactPersonalShowNumberAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String number = numberDatas.get(position).getNumber();
                Intent intent = new Intent(ContactPersonalShowActivity.this.getApplicationContext(), ShowORCodeActivity.class);
                intent.putExtra("ORCODE", number);
                startActivity(intent);
            }
        });*/

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
                Intent intent = new Intent(ContactPersonalShowActivity.this.getApplicationContext(),
                        AddContactActivity.class);
                intent.putExtra(SELECTOR, FROM_CONTACT_PERSONAL_SHOW_ACTIVITY_EDIT);
                intent.putExtra("NAME",name);
                intent.putExtra("COMPANY",company);
                intent.putExtra("REMARK",remark);
                intent.putExtra("LOOKUP",lookUp);
                Bundle bundle = new Bundle();
                bundle.putSerializable("NUMBERDATA", numberDatas);
                bundle.putSerializable("EMAILDATA", emailDatas);
                bundle.putSerializable("IMDATA", imDatas);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.ll_contact_personal_show_orcode:
                turnToORCode();
                break;
            case R.id.ll_contact_personal_show_delete:
                Cursor cursor = getContentResolver().query(Phone.CONTENT_URI,
                        new String[]{Phone.RAW_CONTACT_ID},Phone.LOOKUP_KEY+"=?",
                        new String[]{lookUp},null);
                if(cursor!=null){
                    while (cursor.moveToNext()){
                        int rawContactID = cursor.getInt(0);
                        getContentResolver().delete(
                                ContentUris.withAppendedId(
                                ContactsContract.RawContacts.CONTENT_URI,
                                        rawContactID), null, null);
                        //*************************************************************************************************************

                    }
                    cursor.close();
                }
                this.finish();
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
            //更新数据
            name = data.getStringExtra("NAME");
            tv_contact_personal_show_name.setText(name);

            remark = data.getStringExtra("REMARK");
            if(remark != null && remark.length()>0) {
                ll_contact_personal_show_remark_block.setVisibility(View.VISIBLE);
                tv_contact_personal_show_remark.setText(remark);
            }else {
                ll_contact_personal_show_remark_block.setVisibility(View.GONE);
            }

            company = data.getStringExtra("COMPANY");
            if(company != null && company.length()>0) {
                ll_contact_personal_show_company_block.setVisibility(View.VISIBLE);
                tv_contact_personal_show_company.setText(company);
            }else {
                ll_contact_personal_show_company_block.setVisibility(View.GONE);
            }



            ArrayList<PhoneNumberData> resultNumberDatas = (ArrayList<PhoneNumberData>) data.getSerializableExtra("NUMBER");
            ArrayList<EmailData> resultEmailDatas = (ArrayList<EmailData>) data.getSerializableExtra("EMAIL");
            ArrayList<ImData> resultImDatas = (ArrayList<ImData>) data.getSerializableExtra("IM");
            //类型数据处理
            if(resultNumberDatas.size()>0) {
                ll_contact_personal_show_number_block.setVisibility(View.VISIBLE);
                for(PhoneNumberData resultNumberData:resultNumberDatas){
                    int intType = Integer.parseInt(resultNumberData.getNumberType());
                    String strType = new GetStrPhoneType().getStrPhoneType(intType);
                    resultNumberData.setNumberType(strType);
                }
            }else {
                ll_contact_personal_show_number_block.setVisibility(View.GONE);
            }

            if(resultEmailDatas.size()>0) {
                ll_contact_personal_show_email_block.setVisibility(View.VISIBLE);
                for (EmailData resultEmailData:resultEmailDatas){
                    int intType = Integer.parseInt(resultEmailData.getEmailType());
                    String strType = new GetStrEmailType().getStrEmailType(intType);
                    resultEmailData.setEmailType(strType);
                }
            }else {
                ll_contact_personal_show_email_block.setVisibility(View.GONE);
            }

            if (resultImDatas.size()>0) {
                ll_contact_personal_show_im_block.setVisibility(View.VISIBLE);
                for (ImData resultImData:resultImDatas){
                    int intType = Integer.parseInt(resultImData.getImType());
                    String strType = new GetStrImType().getStrImType(intType);
                    resultImData.setImType(strType);
                }
            }else {
                ll_contact_personal_show_im_block.setVisibility(View.GONE);
            }

            if(numberDatas.size()>0) {//原本有数据
                numberDatas.clear();
                numberDatas.addAll(resultNumberDatas);
                numberAdapter.notifyDataSetChanged();
            }else {//原本没有数据
                for (PhoneNumberData resultNumberData:resultNumberDatas) {
                    numberDatas.add(resultNumberData);
                    numberAdapter.notifyItemInserted(numberAdapter.getItemCount());
                }
            }


            if(emailDatas.size()>0) {
                emailDatas.clear();
                emailDatas.addAll(resultEmailDatas);
                emailAdapter.notifyDataSetChanged();
            }else {
                for (EmailData resultEmailData:resultEmailDatas) {
                    emailDatas.add(resultEmailData);
                    emailAdapter.notifyItemInserted(emailAdapter.getItemCount());
                }
            }



            if(imDatas.size()>0) {
                imDatas.clear();
                imDatas.addAll(resultImDatas);
                imAdapter.notifyDataSetChanged();
            }else {
                for (ImData resultImData:resultImDatas) {
                    imDatas.add(resultImData);
                    imAdapter.notifyItemInserted(imAdapter.getItemCount());
                }
            }








        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void turnToORCode() {

        Intent orCodeIntent = new Intent(ContactPersonalShowActivity.this.getApplicationContext(),
                ShowORCodeActivity.class);
        StringBuilder sb = new StringBuilder();

        /*for(PhoneNumberData numberData:numberDatas) {
            sb.append(numberData.getNumber());
            sb.append(" ");
        }*/
        sb.append(numberDatas.get(0).getNumber());

        orCodeIntent.putExtra("ORCODE", sb.toString());
        Log.d("===========ORCODE=",sb.toString());
        startActivity(orCodeIntent);

    }
}
