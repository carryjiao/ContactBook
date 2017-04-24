package com.carryj.root.contactbook;


import android.graphics.Color;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.carryj.root.contactbook.data.RecordListViewItemData;
import com.carryj.root.contactbook.fragments.RecordFragement;

public class RecordItemInDetailActivity extends SweepBackActivity {

    private LinearLayout ll_record_item_in_detail_back;
    private LinearLayout ll_record_item_in_detail_contact_add;
    private LinearLayout ll_record_item_in_detail_contact_delete;

    private ImageView im_record_item_in_detail_icon;
    private ImageView im_record_item_in_detail_call_icon;

    private TextView tv_record_item_in_detail_name;
    private TextView tv_record_item_in_detail_date;
    private TextView tv_record_item_in_detail_time;
    private TextView tv_record_item_in_detail_catact_type;
    private TextView tv_record_item_in_detail_duration;
    private TextView tv_record_item_in_detail_number_type;
    private TextView tv_record_item_in_detail_number;

    private RecordListViewItemData data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_item_in_detail);

    }

    @Override
    protected void initData() {

        data = (RecordListViewItemData) getIntent().getSerializableExtra(RecordFragement.RECORD_IN_DETAIL);

    }

    @Override
    protected void initView() {
        ll_record_item_in_detail_back = (LinearLayout) findViewById(R.id.ll_record_item_in_detail_back);
        ll_record_item_in_detail_contact_add = (LinearLayout) findViewById(R.id.ll_record_item_in_detail_contact_add);
        ll_record_item_in_detail_contact_delete = (LinearLayout) findViewById(R.id.ll_record_item_in_detail_contact_delete);

        im_record_item_in_detail_icon = (ImageView) findViewById(R.id.im_record_item_in_detail_icon);
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
        }else {
            tv_record_item_in_detail_name.setText(data.getStrNumber());
        }

        //设置通话日期
        //tv_record_item_in_detail_date.setText("");

        //设置通话时间
        //tv_record_item_in_detail_time.setText();

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
            default:
                break;
        }

    }
}
