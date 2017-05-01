package com.carryj.root.contactbook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.carryj.root.contactbook.adapter.ContactBookAdapter;
import com.carryj.root.contactbook.data.ContactListViewItemData;
import java.util.ArrayList;


public class AddCollectActivity extends SweepBackActivity {



    private TextView tv_contact_book_add;
    private ImageView iv_contact_book_box;
    private ContactBookAdapter adapter;
    private SwipeMenuListView listView;

    /**获取库Phone表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Phone.RAW_CONTACT_ID, Phone.CONTACT_ID};

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

        mData = getPhoneContacts();

    }

    @Override
    protected void initView() {

        tv_contact_book_add = (TextView) findViewById(R.id.tv_contact_book_add);
        iv_contact_book_box = (ImageView) findViewById(R.id.iv_contact_book_box);
        listView = (SwipeMenuListView) findViewById(R.id.contact_book_listview);

        tv_contact_book_add.setText("取消");
        tv_contact_book_add.setTextSize(20);

        adapter = new ContactBookAdapter(this, mData);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addCollect(mData.get(position).getContactID());
            }
        });


        tv_contact_book_add.setOnClickListener(this);
        iv_contact_book_box.setOnClickListener(this);

    }

    @Override
    protected void initEvents() {

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

        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                PHONES_PROJECTION, Phone.STARRED+"=?", new String[]{"0"},
                Phone.RAW_CONTACT_ID+" ASC");

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
        Toast.makeText(this, "收藏成功", Toast.LENGTH_LONG).show();
        tv_contact_book_add.setText("返回");
    }
}
