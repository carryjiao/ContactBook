package com.carryj.root.contactbook.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.RecordItemInDetailActivity;
import com.carryj.root.contactbook.adapter.RecordAdapter;
import com.carryj.root.contactbook.data.RecordListViewItemData;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by root on 17/4/10.
 */

public class RecordFragement extends Fragment implements OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CALL_LOG = 3;

    public static final String RECORD_IN_DETAIL = "RECORD_IN_DETAIL";

    /**获取库Call表字段**/
    private static final String[] CALL_LOG_PROJECTION = new String[] {
            CallLog.Calls.NUMBER, CallLog.Calls.CACHED_NAME, CallLog.Calls.TYPE,
            CallLog.Calls.DATE, CallLog.Calls.DURATION,
            CallLog.Calls.CACHED_NUMBER_TYPE, CallLog.Calls._ID};

    /**电话号码**/
    private static final int CALLS_NUMBER_INDEX = 0;

    /**姓名**/
    private static final int CALLS_CACHED_NAME_INDEX = 1;

    /**通话类型**/
    private static final int CALLS_TYPE_INDEX = 2;

    /**通话日期**/
    private static final int CALLS_DATE_INDEX = 3;

    /**通话时长**/
    private static final int CALLS_DURATION_INDEX = 4;

    /**号码类型**/
    private static final int CALLS_CACHED_NUMBER_TYPE_INDEX = 5;

    private static final int CALLS_ID_INDEX = 6;




    private TextView tv_record_all;
    private TextView tv_record_missed_call;
    private ImageView iv_record_box;
    private SwipeMenuListView recordListView;
    private RecordAdapter adapter;

    private ArrayList<RecordListViewItemData> mData = new ArrayList<RecordListViewItemData>();



    public RecordFragement() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_record,null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        tv_record_all = (TextView) view.findViewById(R.id.tv_record_all);
        tv_record_missed_call = (TextView) view.findViewById(R.id.tv_record_missed_call);
        iv_record_box = (ImageView) view.findViewById(R.id.iv_record_box);

        recordListView = (SwipeMenuListView) view.findViewById(R.id.record_listview);
        adapter = new RecordAdapter(getContext(), mData);
        recordListView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getContext());
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9,
                        0xC9, 0xCE)));
                openItem.setWidth(dp2px(60));
                openItem.setTitle("打开");
                openItem.setTitleSize(18);
                openItem.setTitleColor(Color.WHITE);
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(60));
                /*// set a icon
                deleteItem.setIcon(R.drawable.ic_delete);*/
                // set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        recordListView.setMenuCreator(creator);

        recordListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        Intent intent = new Intent(RecordFragement.this.getContext(), RecordItemInDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(RECORD_IN_DETAIL, mData.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:

                        // delete
                        deleteRecord(mData.get(position).get_id());
                        mData.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        recordListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        recordListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                callPhone(mData.get(position).getStrNumber());

            }
        });



        tv_record_all.setOnClickListener(this);
        tv_record_missed_call.setOnClickListener(this);
        iv_record_box.setOnClickListener(this);


    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_record_all:
                tv_record_all.setTextColor(Color.parseColor("#E5E5E5"));
                tv_record_missed_call.setTextColor(Color.parseColor("#030303"));
                mData = getRecordData(null,null);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Changed!", Toast.LENGTH_LONG).show();
                break;
            case R.id.tv_record_missed_call:
                tv_record_all.setTextColor(Color.parseColor("#030303"));
                tv_record_missed_call.setTextColor(Color.parseColor("#E5E5E5"));
                String selection = new String(CallLog.Calls.TYPE+"=?");
                String[] selectionArgs = new String[]{CallLog.Calls.MISSED_TYPE+""};
                mData = getRecordData(selection,selectionArgs);
                adapter.notifyDataSetChanged();
                Toast.makeText(getContext(), "Changed!", Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    //获取 READ_CALL_LOG 权限
    public void initData() {

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    MY_PERMISSIONS_REQUEST_READ_CALL_LOG);
        } else {

            mData = getRecordData(null,null);


        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CALL_LOG)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                mData = getRecordData(null,null);

            } else
            {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**得到手机通话记录**/
    private ArrayList<RecordListViewItemData> getRecordData(String selection, String[] selectionArgs) {

        ArrayList<RecordListViewItemData> recordInfo = new ArrayList<RecordListViewItemData>();

        ContentResolver resolver = getContext().getContentResolver();

        try {

            Cursor callLogCursor = resolver.query(CallLog.Calls.CONTENT_URI, CALL_LOG_PROJECTION,
                    selection, selectionArgs, CallLog.Calls.DEFAULT_SORT_ORDER);

            if (callLogCursor != null) {
                while (callLogCursor.moveToNext()) {

                    //得到电话号码
                    String strNumber = callLogCursor.getString(CALLS_NUMBER_INDEX);

                    //得到联系人名称
                    String cachedName = callLogCursor.getString(CALLS_CACHED_NAME_INDEX);

                    //得到通话类型
                    int contactType = callLogCursor.getInt(CALLS_TYPE_INDEX);

                    //得到通话时间
                    Date date = new Date(Long.parseLong(callLogCursor.getString(CALLS_DATE_INDEX)));

                    //得到通话时长
                    Long duration = callLogCursor.getLong(CALLS_DURATION_INDEX);

                    //得到号码类型
                    int numberType = callLogCursor.getInt(CALLS_CACHED_NUMBER_TYPE_INDEX);

                    int _id = callLogCursor.getInt(CALLS_ID_INDEX);

                    RecordListViewItemData itemData = new RecordListViewItemData();
                    itemData.setStrNumber(strNumber);
                    itemData.setStrCachedName(cachedName);
                    itemData.setcontactType(contactType);
                    itemData.setDate(date);
                    itemData.setDuration(duration);
                    itemData.setPhoneType(numberType);
                    itemData.set_id(_id);

                    recordInfo.add(itemData);

                }

                callLogCursor.close();
            }

        }catch (SecurityException e) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CALL_LOG},
                    MY_PERMISSIONS_REQUEST_READ_CALL_LOG);

        }finally {

        }

        return recordInfo;

    }



    public void callPhone(String telNum)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + telNum);
        intent.setData(data);
        startActivity(intent);
    }

    private void deleteRecord(int id) {
        try {
            getContext().getContentResolver().delete(CallLog.Calls.CONTENT_URI,
                    CallLog.Calls._ID+"=?", new String[]{id+""});

        }catch (SecurityException e){

        }
    }
}
