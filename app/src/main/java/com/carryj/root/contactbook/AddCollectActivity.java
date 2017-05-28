package com.carryj.root.contactbook;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.carryj.root.contactbook.adapter.ContactBookAdapter;
import com.carryj.root.contactbook.data.ContactListViewItemData;
import com.carryj.root.contactbook.event.CollectEvent;
import com.carryj.root.contactbook.tools.UserHeadPhotoManager;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class AddCollectActivity extends SweepBackActivity {



    private RelativeLayout contact_book_view;
    private RelativeLayout contact_book_title_bar;
    private TextView tv_contact_book_add;
    private RoundedImageView head_photo;
    private ContactBookAdapter adapter;
    private SwipeMenuListView listView;


    private Button cancel;
    private Button confirm;


    /**获取库Phone表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            Contacts.DISPLAY_NAME, Contacts.NAME_RAW_CONTACT_ID, Contacts._ID};

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**联系人的ID**/
    private static final int PHONES_RAW_CONTACT_ID_INDEX = 1;

    private static final int PHONES_CONTACT_ID_INDEX = 2;


    private ArrayList<ContactListViewItemData> mData = new ArrayList<ContactListViewItemData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_book);
    }

    @Override
    protected void initData() {

        //异步加载联系人数据
        new AsyncTask<Void, Void, ArrayList<ContactListViewItemData>>() {
            @Override
            protected ArrayList<ContactListViewItemData> doInBackground(Void... params) {
                ArrayList<ContactListViewItemData> datas = getPhoneContacts();
                return datas;
            }

            @Override
            protected void onPostExecute(ArrayList<ContactListViewItemData> datas) {
                mData.addAll(datas);
                adapter.notifyDataSetChanged();
            }
        }.execute();


    }

    @Override
    protected void initView() {

        contact_book_view = (RelativeLayout) findViewById(R.id.contact_book_view);
        contact_book_title_bar = (RelativeLayout) findViewById(R.id.contact_book_title_bar);
        tv_contact_book_add = (TextView) findViewById(R.id.tv_contact_book_add);
        head_photo = (RoundedImageView) findViewById(R.id.head_photo);
        listView = (SwipeMenuListView) findViewById(R.id.contact_book_listview);


        head_photo.setVisibility(View.GONE);
        tv_contact_book_add.setText("取消");
        tv_contact_book_add.setTextSize(20);

        adapter = new ContactBookAdapter(this, mData);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int index = position;
                View popupWindowView = getLayoutInflater().inflate(R.layout.add_collect_popupwindow, null);
                cancel = (Button) popupWindowView.findViewById(R.id.btn_add_collect_popupwindow_cancel);
                confirm = (Button) popupWindowView.findViewById(R.id.btn_add_collect_popupwindow_confirm);

                final PopupWindow popupWindow = new PopupWindow(popupWindowView,
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                //设置背景透明
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 0.7f; //0.0-1.0
                getWindow().setAttributes(lp);

                popupWindow.setAnimationStyle(R.style.popup_window_anim);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.update();
                popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);

                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        WindowManager.LayoutParams lp = getWindow().getAttributes();
                        lp.alpha = 1f; //0.0-1.0
                        getWindow().setAttributes(lp);
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCollect(mData.get(index).getContactID());
                        EventBus.getDefault().post(new CollectEvent(true));
                        popupWindow.dismiss();
                    }
                });


            }
        });




    }

    @Override
    protected void initEvents() {
        tv_contact_book_add.setOnClickListener(this);
        head_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_contact_book_add:
                this.finish();
                break;
            default:
                break;
        }

    }


    /**得到手机通讯录联系人信息**/
    private ArrayList<ContactListViewItemData> getPhoneContacts() {
        ArrayList<ContactListViewItemData> contactInfo = new ArrayList<ContactListViewItemData>();

        ContentResolver resolver = getContentResolver();

        Cursor phoneCursor = resolver.query(Contacts.CONTENT_URI,
                PHONES_PROJECTION, Contacts.STARRED+"=?", new String[]{"0"},
                Contacts.SORT_KEY_PRIMARY+" ASC");

        if (phoneCursor != null) {


            //*************************************************************************************



            while (phoneCursor.moveToNext()) {

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                int rawContactid = phoneCursor.getInt(PHONES_RAW_CONTACT_ID_INDEX);

                int contactID = phoneCursor.getInt(PHONES_CONTACT_ID_INDEX);

                ContactListViewItemData data = new ContactListViewItemData();

                data.setName(contactName);
                data.setRawContactID(rawContactid);
                data.setContactID(contactID);

                contactInfo.add(data);


            }

            phoneCursor.close();
        }

        return contactInfo;
    }

    private void addCollect(String contactID) {
        ContentValues values = new ContentValues();
        values.put(Phone.STARRED,1);
        getContentResolver().update(RawContacts.CONTENT_URI,
                values, RawContacts.CONTACT_ID+"=?",new String[]{contactID});

        Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
        tv_contact_book_add.setText("返回");
    }

    //设置背景透明度
    public void setBackgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);

    }
}
