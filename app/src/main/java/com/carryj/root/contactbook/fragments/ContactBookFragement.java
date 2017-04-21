package com.carryj.root.contactbook.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.adapter.ContactBookAdapter;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by root on 17/4/10.
 */

public class ContactBookFragement extends Fragment implements OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 2;


    private TextView tv_contact_book_add;
    private ImageView iv_contact_book_box;
    private ContactBookAdapter adapter;
    private SwipeMenuListView listView;

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Photo.PHOTO_ID,Phone.CONTACT_ID};

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;

    /**头像ID**/
    private static final int PHONES_PHOTO_ID_INDEX = 1;

    /**联系人的ID**/
    private static final int PHONES_CONTACT_ID_INDEX = 2;



    /**联系人名称**/
    private ArrayList<String> mContactsName = new ArrayList<String>();

    /**联系人头像**/
    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();

    /**联系人ID**/
    private ArrayList<Long> mContactsID = new ArrayList<Long>();


    public ContactBookFragement() {
        /*nameListData.add("焦消");
        nameListData.add("王舒");
        nameListData.add("周身高");
        nameListData.add("焦消");
        nameListData.add("王舒");
        nameListData.add("周身高");
        nameListData.add("焦消");
        nameListData.add("王舒");
        nameListData.add("周身高");
        nameListData.add("焦消");
        nameListData.add("王舒");
        nameListData.add("周身高");
        nameListData.add("焦消");
        nameListData.add("王舒");
        nameListData.add("周身高");*/

        testReadContact();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_book,null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_contact_book_add = (TextView) view.findViewById(R.id.tv_contact_book_add);
        iv_contact_book_box = (ImageView) view.findViewById(R.id.iv_contact_book_box);
        listView = (SwipeMenuListView) view.findViewById(R.id.contact_book_listview);
        adapter = new ContactBookAdapter(getContext(), mContactsName, mContactsPhonto, mContactsID);
        listView.setAdapter(adapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                /*// create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(getContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(dp2px(90));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);*/

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

                        // delete
                        mContactsName.remove(position);
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


        tv_contact_book_add.setOnClickListener(this);
        iv_contact_book_box.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {

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
        } else
        {
            getPhoneContacts();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getPhoneContacts();
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
    private void getPhoneContacts() {
        ContentResolver resolver = getContext().getContentResolver();

        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);

                //得到联系人头像Bitamp
                Bitmap contactPhoto;

                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if(photoid > 0 ) {
                    Uri photoUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, photoUri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                }else {
                    contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
                }

                mContactsName.add(contactName);
                mContactsPhonto.add(contactPhoto);
                mContactsID.add(contactid);


            }

            phoneCursor.close();
        }
    }


    /**得到手机SIM卡联系人人信息**/
    private void getSIMContacts() {
        ContentResolver resolver = getContext().getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                mContactsName.add(contactName);

            }

            phoneCursor.close();
        }
    }
}
