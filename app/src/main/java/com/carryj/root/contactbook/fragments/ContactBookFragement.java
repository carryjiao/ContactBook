package com.carryj.root.contactbook.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract;
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
import com.carryj.root.contactbook.tools.ContactBookSearch;
import com.carryj.root.contactbook.tools.GetStrPhoneType;
import com.carryj.root.contactbook.tools.PhoneNumberTransformer;

import java.util.ArrayList;

/**
 * Created by root on 17/4/10.
 */

public class ContactBookFragement extends Fragment implements OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;
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


    private static final String[] CONTACTS_PROJECTION = new String[] {
            Contacts.DISPLAY_NAME, Contacts.LOOKUP_KEY};

    /**获取库Phone表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Phone.RAW_CONTACT_ID, Phone.CONTACT_ID};


    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**联系人的ID**/
    private static final int PHONES_RAW_CONTACT_ID_INDEX = 1;

    private static final int PHONES_CONTACT_ID_INDEX = 2;


    private ArrayList<ContactListViewItemData> mData = new ArrayList<ContactListViewItemData>();
    private ArrayList<ContactListViewItemData> searchResultData = new ArrayList<ContactListViewItemData>();
    private ArrayList<ContactListViewItemData> allContactData = new ArrayList<ContactListViewItemData>();


    public ContactBookFragement() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_book,null);
        testReadContact();
        initView(view);
        return view;
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
                                new String[]{Phone.RAW_CONTACT_ID},Phone.LOOKUP_KEY+"=?",
                                new String[]{lookUp},null);
                        if(cursor!=null){
                            while (cursor.moveToNext()){
                                int rawContactID = cursor.getInt(0);
                                getContext().getContentResolver().delete(
                                        ContentUris.withAppendedId(ContactsContract.RawContacts.CONTENT_URI,
                                                rawContactID), null, null);
                                //*************************************************************************************************************

                            }
                            cursor.close();
                        }

                        allContactData = getPhoneContacts();
                        mData.clear();
                        mData.addAll(allContactData);
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
                intent.putExtra(SELECTOR,FROM_CONTACT_BOOK_FRAGMENT);
                intent.putExtra(NAME,name);
                intent.putExtra(LOOKUP,lookUp);
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
                intent.putExtra(SELECTOR,FROM_CONTACT_BOOK_FRAGEMENT_ADD);
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

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }


    //获取 READ_CONTACTS 权限
    public void testReadContact() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_CONTACTS},
                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {

            mData = getPhoneContacts();

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                mData = getPhoneContacts();

            } else
            {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**得到手机通讯录联系人信息**/
    private ArrayList<ContactListViewItemData> getPhoneContacts() {
        ArrayList<ContactListViewItemData> contactInfo = new ArrayList<ContactListViewItemData>();

        ContentResolver resolver = getContext().getContentResolver();

        Cursor contactsCursor = resolver.query(Contacts.CONTENT_URI, CONTACTS_PROJECTION,
                null, null, Contacts.NAME_RAW_CONTACT_ID+" ASC");

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
                searchResultData = new ContactBookSearch().searchContact(getContext(), s);
                mData.clear();
                mData.addAll(searchResultData);
                adapter.notifyDataSetChanged();
            }else {
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
}
