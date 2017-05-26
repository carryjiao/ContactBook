package com.carryj.root.contactbook;



import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryj.root.contactbook.data.RecordListViewItemData;
import com.carryj.root.contactbook.fragments.RecordFragement;
import com.carryj.root.contactbook.tools.UserHeadPhotoManager;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.InputStream;

public class RecordItemInDetailActivity extends SweepBackActivity {


    public static final String SELECTOR = "SELECTOR";
    public static final String NUMBER = "NUMBER";
    private static final String FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW = "FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW";

    private LinearLayout ll_record_item_in_detail_back;
    private LinearLayout ll_record_item_in_detail_contact_add;
    private LinearLayout ll_record_item_in_detail_contact_delete;

    private RoundedImageView im_record_item_in_detail_icon;
    private ImageView im_record_item_in_detail_call_icon;

    private TextView tv_record_item_in_detail_name;
    private TextView tv_record_item_in_detail_date;
    private TextView tv_record_item_in_detail_time;
    private TextView tv_record_item_in_detail_catact_type;
    private TextView tv_record_item_in_detail_duration;
    private TextView tv_record_item_in_detail_number_type;
    private TextView tv_record_item_in_detail_number;

    private RecordListViewItemData data;
    private UserHeadPhotoManager userHeadPhotoManager;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_item_in_detail);

    }

    @Override
    protected void initData() {

        data = (RecordListViewItemData) getIntent().getSerializableExtra(RecordFragement.RECORD_IN_DETAIL);

        new AsyncTask<Void, Void, Bitmap>(){//异步加载联系人头像
            @Override
            protected Bitmap doInBackground(Void... params) {
                String contactID = data.getContactID();
                if(contactID == null) {//根据姓名查询contactID
                    String name = data.getStrCachedName();
                    Cursor cursor = getContentResolver().query(Contacts.CONTENT_URI, new String[]{Contacts._ID},
                            Contacts.DISPLAY_NAME + "=?", new String[]{name}, null);
                    if (cursor != null) {
                        while (cursor.moveToNext()) {
                            contactID = cursor.getString(0);
                        }
                    }
                }

                //获取头像
                Uri uri = ContentUris.withAppendedId(Contacts.CONTENT_URI, Long.parseLong(contactID));
                InputStream input = Contacts.openContactPhotoInputStream(getContentResolver(), uri, true);
                if (input != null) {
                    bitmap = BitmapFactory.decodeStream(input);
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                im_record_item_in_detail_icon.setImageBitmap(bitmap);
                super.onPostExecute(bitmap);
            }
        }.execute();


    }

    @Override
    protected void initView() {
        ll_record_item_in_detail_back = (LinearLayout) findViewById(R.id.ll_record_item_in_detail_back);
        ll_record_item_in_detail_contact_add = (LinearLayout) findViewById(R.id.ll_record_item_in_detail_contact_add);
        ll_record_item_in_detail_contact_delete = (LinearLayout) findViewById(R.id.ll_record_item_in_detail_contact_delete);

        im_record_item_in_detail_icon = (RoundedImageView) findViewById(R.id.im_record_item_in_detail_icon);
        im_record_item_in_detail_call_icon = (ImageView) findViewById(R.id.im_record_item_in_detail_call_icon);

        tv_record_item_in_detail_name = (TextView) findViewById(R.id.tv_record_item_in_detail_name);
        tv_record_item_in_detail_date = (TextView) findViewById(R.id.tv_record_item_in_detail_date);
        tv_record_item_in_detail_time = (TextView) findViewById(R.id.tv_record_item_in_detail_time);
        tv_record_item_in_detail_catact_type = (TextView) findViewById(R.id.tv_record_item_in_detail_catact_type);
        tv_record_item_in_detail_duration = (TextView) findViewById(R.id.tv_record_item_in_detail_duration);
        tv_record_item_in_detail_number_type = (TextView) findViewById(R.id.tv_record_item_in_detail_number_type);
        tv_record_item_in_detail_number = (TextView) findViewById(R.id.tv_record_item_in_detail_number);

        //设置头像
        //im_record_item_in_detail_icon.setImageResource();

        //设置姓名
        if(data.getStrCachedName() != null) {
            tv_record_item_in_detail_name.setText(data.getStrCachedName());
            ll_record_item_in_detail_contact_add.setVisibility(View.GONE);
        }else {
            tv_record_item_in_detail_name.setText(data.getStrNumber());
        }

        //设置通话日期
        tv_record_item_in_detail_date.setText(data.getDisplayDate());

        //设置通话时间
        tv_record_item_in_detail_time.setText(data.getTime());

        //设置通话类型
        String str = new String();

        if (data.getcontactType() == CallLog.Calls.INCOMING_TYPE) {
            str = new String("呼入电话");
        } else if (data.getcontactType() == CallLog.Calls.OUTGOING_TYPE) {
            str = new String("呼出电话");
        } else if (data.getcontactType() == CallLog.Calls.MISSED_TYPE) {
            str = new String("未接来电");
            tv_record_item_in_detail_number.setTextColor(Color.parseColor("#EE0000"));
        }
        tv_record_item_in_detail_catact_type.setText(str);

        //设置通话时长
        tv_record_item_in_detail_duration.setText(data.getDuration());

        //设置号码类型
        tv_record_item_in_detail_number_type.setText(data.getPhoneType());

        //设置电话号码
        tv_record_item_in_detail_number.setText(data.getStrNumber());



    }

    @Override
    protected void initEvents() {

        ll_record_item_in_detail_back.setOnClickListener(this);
        ll_record_item_in_detail_contact_add.setOnClickListener(this);
        ll_record_item_in_detail_contact_delete.setOnClickListener(this);
        im_record_item_in_detail_call_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.ll_record_item_in_detail_back:
                this.finish();
                break;
            case R.id.im_record_item_in_detail_call_icon:
                callPhone(data.getStrNumber());
                break;
            case R.id.ll_record_item_in_detail_contact_delete:
                break;
            case R.id.ll_record_item_in_detail_contact_add:

                String number = tv_record_item_in_detail_number.getText().toString();
                Intent intent = new Intent(RecordItemInDetailActivity.this, AddContactActivity.class);
                intent.putExtra(SELECTOR,FROM_RECORD_ITEM_IN_DETAIL_ACTIVITY_NEW);
                intent.putExtra(NUMBER,number);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    public void callPhone(String telNum)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + telNum);
        intent.setData(data);
        try {
            startActivity(intent);
        }catch (SecurityException e){

        }finally {

        }

    }
}
