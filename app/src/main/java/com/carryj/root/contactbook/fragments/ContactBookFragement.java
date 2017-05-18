package com.carryj.root.contactbook.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.carryj.root.contactbook.AddContactActivity;
import com.carryj.root.contactbook.ContactPersonalShowActivity;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.adapter.ContactBookAdapter;
import com.carryj.root.contactbook.data.ContactListViewItemData;
import com.carryj.root.contactbook.data.PhoneNumberData;
import com.carryj.root.contactbook.event.NumberChangeEvent;
import com.carryj.root.contactbook.tools.ContactBookSearch;
import com.carryj.root.contactbook.tools.GetStrPhoneType;
import com.carryj.root.contactbook.tools.PhoneNumberTransformer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * Created by root on 17/4/10.
 */

public class ContactBookFragement extends Fragment implements OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;
    public static final String CONTACT_SHOW = "CONTACT_SHOW";
    public static final String SELECTOR = "SELECTOR";
    private static final String FROM_CONTACT_BOOK_FRAGEMENT_ADD = "FROM_CONTACT_BOOK_FRAGEMENT_ADD";

    public static final String FROM_CONTACT_BOOK_FRAGMENT = "FROM_CONTACT_BOOK_FRAGMENT";
    public static final String NAME = "NAME";
    public static final String LOOKUP = "LOOKUP";


    private TextView tv_contact_book_add;
    private EditText et_contact_book_search;
    private TextView tv_contact_book_search;
    private ImageView iv_contact_book_search;
    private ImageView iv_contact_book_box;
    private ContactBookAdapter adapter;
    private SwipeMenuListView listView;
    private View view;


    /**
     * 获取库Contacts表字段
     **/
    private static final String[] CONTACTS_PROJECTION = new String[]{
            Contacts.DISPLAY_NAME, Contacts.LOOKUP_KEY};



    private ArrayList<ContactListViewItemData> mData = new ArrayList<ContactListViewItemData>();
    private ArrayList<ContactListViewItemData> searchResultData = new ArrayList<ContactListViewItemData>();
    private ArrayList<ContactListViewItemData> allContactData = new ArrayList<ContactListViewItemData>();


    public ContactBookFragement() {


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact_book, null);
        initView(view);
        return view;
    }

    private void initData() {
        //异步加载联系人数据
        new AsyncTask<Void, Void, ArrayList<ContactListViewItemData>>() {
            @Override
            protected ArrayList<ContactListViewItemData> doInBackground(Void... params) {

                ArrayList<ContactListViewItemData> datas = new ArrayList<ContactListViewItemData>();
                //获取 READ_CONTACTS 权限

                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {

                    datas = getPhoneContacts();

                }
                return datas;
            }


            @Override
            protected void onPostExecute(ArrayList<ContactListViewItemData> datas) {
                mData.addAll(datas);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void initView(View view) {

        tv_contact_book_add = (TextView) view.findViewById(R.id.tv_contact_book_add);
        et_contact_book_search = (EditText) view.findViewById(R.id.et_contact_book_search);
        tv_contact_book_search = (TextView) view.findViewById(R.id.tv_contact_book_search);
        iv_contact_book_search = (ImageView) view.findViewById(R.id.iv_contact_book_search);
        iv_contact_book_box = (ImageView) view.findViewById(R.id.iv_contact_book_box);
        listView = (SwipeMenuListView) view.findViewById(R.id.contact_book_listview);
        adapter = new ContactBookAdapter(getContext(), mData);
        listView.setAdapter(adapter);

        final SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

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
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //*************************************************************************************************************
                        // delete   先查后删
                        String lookUp = mData.get(position).getLookUp();
                        Cursor cursor = getContext().getContentResolver().query(Phone.CONTENT_URI,
                                new String[]{Phone.RAW_CONTACT_ID}, Phone.LOOKUP_KEY + "=?",
                                new String[]{lookUp}, null);
                        if (cursor != null) {
                            while (cursor.moveToNext()) {
                                int rawContactID = cursor.getInt(0);
                                getContext().getContentResolver().delete(
                                        ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                                                rawContactID), null, null);
                                //*************************************************************************************************************

                            }
                            cursor.close();
                        }

                        mData.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        // open
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = mData.get(position).getName();
                String lookUp = mData.get(position).getLookUp();
                Intent intent = new Intent(ContactBookFragement.this.getContext(), ContactPersonalShowActivity.class);
                intent.putExtra(SELECTOR, FROM_CONTACT_BOOK_FRAGMENT);
                intent.putExtra(NAME, name);
                intent.putExtra(LOOKUP, lookUp);
                startActivity(intent);
            }
        });


        tv_contact_book_add.setOnClickListener(this);
        iv_contact_book_box.setOnClickListener(this);
        et_contact_book_search.addTextChangedListener(new MyTextWatcher());

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_contact_book_add:
                Intent intent = new Intent(ContactBookFragement.this.getContext(), AddContactActivity.class);
                intent.putExtra(SELECTOR, FROM_CONTACT_BOOK_FRAGEMENT_ADD);
                startActivity(intent);

                break;
            case R.id.iv_contact_book_box:

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

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);//取消订阅事件
        super.onDestroy();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //提示刷新联系人数据
                EventBus.getDefault().post(new NumberChangeEvent(true));

            } else {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 得到手机通讯录联系人信息
     **/
    private ArrayList<ContactListViewItemData> getPhoneContacts() {
        ArrayList<ContactListViewItemData> contactInfo = new ArrayList<ContactListViewItemData>();

        ContentResolver resolver = getContext().getContentResolver();

        Cursor contactsCursor = resolver.query(Contacts.CONTENT_URI, CONTACTS_PROJECTION,
                null, null, Contacts.SORT_KEY_PRIMARY + " ASC");

        if (contactsCursor != null) {


            while (contactsCursor.moveToNext()) {

                //得到联系人名称
                String contactName = contactsCursor.getString(0);
                //获取lookup
                String lookUp = contactsCursor.getString(1);

                ContactListViewItemData data = new ContactListViewItemData();

                data.setName(contactName);
                data.setLookUp(lookUp);

                contactInfo.add(data);


            }

            contactsCursor.close();
        }

        return contactInfo;
    }


    private class MyTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            tv_contact_book_search.setVisibility(View.GONE);
            iv_contact_book_search.setVisibility(View.GONE);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s != null && s.length() > 0) {
                //调用模糊匹配类进行查找
                searchResultData = new ContactBookSearch().searchContact(getContext(), s);
                mData.clear();
                mData.addAll(searchResultData);
                adapter.notifyDataSetChanged();
            } else {
                tv_contact_book_search.setVisibility(View.VISIBLE);
                iv_contact_book_search.setVisibility(View.VISIBLE);
                allContactData = getPhoneContacts();
                mData.clear();
                mData.addAll(allContactData);
                adapter.notifyDataSetChanged();
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    //处理数据更新事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDealNumberChangeEvent(NumberChangeEvent numberChangeEvent) {
        if (numberChangeEvent.isNumberChangeFlag()) {
            ArrayList<ContactListViewItemData> contactDatas = getPhoneContacts();
            mData.clear();
            mData.addAll(contactDatas);
            adapter.notifyDataSetChanged();
        }
    }
}
