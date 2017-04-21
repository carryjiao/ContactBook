package com.carryj.root.contactbook.fragments;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.adapter.ContactBookAdapter;

import java.util.ArrayList;

/**
 * Created by root on 17/4/10.
 */

public class ContactBookFragement extends Fragment implements OnClickListener {


    private TextView tv_contact_book_add;
    private ImageView iv_contact_book_box;
    private ContactBookAdapter adapter;
    private ListView listView;
    private ArrayList<String> nameListData = new ArrayList<String>();

    /**获取库Phon表字段**/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME };

    /**联系人显示名称**/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;


    public ContactBookFragement() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_book,null);
        //getPhoneContacts();
        nameListData.add(0,"焦消");
        nameListData.add(1,"王舒");
        nameListData.add(2,"周身高");
        initView(view);
        return view;
    }

    private void initView(View view) {
        tv_contact_book_add = (TextView) view.findViewById(R.id.tv_contact_book_add);
        iv_contact_book_box = (ImageView) view.findViewById(R.id.iv_contact_book_box);
        listView = (ListView) view.findViewById(R.id.contact_book_listview);
        adapter = new ContactBookAdapter(getContext(), nameListData);
        listView.setAdapter(adapter);

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

    /**得到手机通讯录联系人信息**/
    private void getPhoneContacts() {
        ContentResolver resolver = getContext().getContentResolver();

        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                nameListData.add(contactName);
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
                nameListData.add(contactName);
            }

            phoneCursor.close();
        }
    }
}
