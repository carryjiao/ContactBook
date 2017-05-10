package com.carryj.root.contactbook.fragments;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.carryj.root.contactbook.AddCollectActivity;
import com.carryj.root.contactbook.CollectPersonalShowActivity;
import com.carryj.root.contactbook.ContactPersonalShowActivity;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.adapter.CollectAdapter;
import com.carryj.root.contactbook.data.CollectListViewItemData;
import com.carryj.root.contactbook.data.ContactListViewItemData;


import java.util.ArrayList;


/**
 * Created by root on 17/4/10.
 */

public class CollectFragement extends Fragment implements OnClickListener {

    public static final String CONTACT_PERSONAL_SHOW = "CONTACT_PERSONAL_SHOW";

    public static final String SELECTOR = "SELECTOR";
    public static final String FROM_COLLECT_FRAGMENT = "FROM_COLLECT_FRAGMENT";
    public static final String NAME = "NAME";
    public static final String LOOKUP = "LOOKUP";

    private TextView tv_collect_add;
    private ImageView iv_collect_box;
    private CollectAdapter adapter;
    private SwipeMenuListView listView;

    /**获取库contact表字段**/
    private static final String[] Collect_PROJECTION = new String[] {
            Phone.LOOKUP_KEY,
            Phone.DISPLAY_NAME,
            Phone.TYPE,
            Phone.RAW_CONTACT_ID,
            Phone.CONTACT_ID,
            Phone.NUMBER};


    private static final int COLLECT_LOOKUP_INDEX = 0;

    /**姓名**/
    private static final int COLLECT_DISPLAY_NAME_INDEX = 1;

    /**号码类型**/
    private static final int COLLECT_TYPE_INDEX = 2;

    private static final int COLLECT_RAW_CONTACT_ID_INDEX = 3;

    private static final int COLLECT_CONTACT_ID_INDEX = 4;

    private static final int COLLECT_NUMBER_ID_INDEX = 5;

    private ArrayList<CollectListViewItemData> mData = new ArrayList<CollectListViewItemData>();
    private ArrayList<CollectListViewItemData> collectData = new ArrayList<CollectListViewItemData>();


    public CollectFragement() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initData();
        View view = inflater.inflate(R.layout.fragment_collect,null);
        initView(view);
        return view;
    }

    private void initData() {
        //异步加载联系人数据
        new AsyncTask<Void, Void, ArrayList<CollectListViewItemData>>() {
            @Override
            protected ArrayList<CollectListViewItemData> doInBackground(Void... params) {
                ArrayList<CollectListViewItemData> datas = getCollectData();
                return datas;
            }

            @Override
            protected void onPostExecute(ArrayList<CollectListViewItemData> datas) {
                mData.addAll(datas);
                adapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void initView(View view) {
        tv_collect_add = (TextView) view.findViewById(R.id.tv_collect_add);
        iv_collect_box = (ImageView) view.findViewById(R.id.iv_collect_box);
        listView = (SwipeMenuListView) view.findViewById(R.id.collect_listview);
        adapter = new CollectAdapter(getContext(), mData);
        listView.setAdapter(adapter);

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
        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        String name = mData.get(position).getName();
                        String lookUp = mData.get(position).getLookUp();
                        Intent intent = new Intent(CollectFragement.this.getContext(), ContactPersonalShowActivity.class);
                        intent.putExtra(SELECTOR, FROM_COLLECT_FRAGMENT);
                        intent.putExtra(NAME, name);
                        intent.putExtra(LOOKUP,lookUp);
                        startActivity(intent);
                        break;
                    case 1:

                        // delete
                        deleteCollect(mData.get(position).getContactID());
                        collectData = getCollectData();
                        mData.clear();
                        mData.addAll(collectData);
                        adapter.notifyDataSetChanged();
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

                callPhone(mData.get(position).getStrPhoneNumber());

            }
        });

        initEvent();



    }

    private void initEvent(){
        tv_collect_add.setOnClickListener(this);
        iv_collect_box.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_collect_add:
                Intent intent = new Intent(this.getContext(), AddCollectActivity.class);
                startActivity(intent);
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

    public void callPhone(String telNum)
    {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + telNum);
        intent.setData(data);
        startActivity(intent);
    }

    private ArrayList<CollectListViewItemData> getCollectData() {

        ArrayList<CollectListViewItemData> collectInfo = new ArrayList<CollectListViewItemData>();

        ContentResolver resolver = getContext().getContentResolver();

        try {

            Cursor collectCursor = resolver.query(Phone.CONTENT_URI, Collect_PROJECTION,
                    Phone.STARRED+"=?", new String[]{"1"},  Phone.RAW_CONTACT_ID+" ASC");

            if (collectCursor != null) {
                while (collectCursor.moveToNext()) {

                    String lookUp = collectCursor.getString(COLLECT_LOOKUP_INDEX);

                    //得到联系人名称
                    String cachedName = collectCursor.getString(COLLECT_DISPLAY_NAME_INDEX);

                    //得到号码类型
                    int numberType = collectCursor.getInt(COLLECT_TYPE_INDEX);

                    int rawContactID = collectCursor.getInt(COLLECT_RAW_CONTACT_ID_INDEX);

                    int contactID = collectCursor.getInt(COLLECT_CONTACT_ID_INDEX);

                    String number = collectCursor.getString(COLLECT_NUMBER_ID_INDEX);

                    CollectListViewItemData itemData = new CollectListViewItemData();
                    itemData.setLookUp(lookUp);
                    itemData.setName(cachedName);
                    itemData.setPhoneType(numberType);
                    itemData.setRawContactID(rawContactID);
                    itemData.setContactID(contactID);
                    itemData.setStrPhoneNumber(number);

                    collectInfo.add(itemData);

                }

                collectCursor.close();
            }

        }catch (SecurityException e) {

        }finally {

        }

        return collectInfo;

    }

    private void deleteCollect(String contactID) {
        ContentValues values = new ContentValues();
        values.put(Phone.STARRED,0);
        getContext().getContentResolver().update(ContactsContract.RawContacts.CONTENT_URI,
                values, ContactsContract.RawContacts.CONTACT_ID+"=?",new String[]{contactID});
    }
}
