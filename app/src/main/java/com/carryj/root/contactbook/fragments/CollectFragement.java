package com.carryj.root.contactbook.fragments;


import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.carryj.root.contactbook.CollectPersonalShowActivity;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.adapter.CollectAdapter;
import com.carryj.root.contactbook.data.CollectListViewItemData;


import java.util.ArrayList;


/**
 * Created by root on 17/4/10.
 */

public class CollectFragement extends Fragment implements OnClickListener {

    public static final String CONTACT_PERSONAL_SHOW = "CONTACT_PERSONAL_SHOW";

    private TextView tv_collect_add;
    private ImageView iv_collect_box;
    private CollectAdapter adapter;
    private SwipeMenuListView listView;

    /**获取库contact表字段**/
    private static final String[] Collect_PROJECTION = new String[] {
            Phone.NUMBER,
            Phone.DISPLAY_NAME,
            Phone.TYPE,
            Phone.RAW_CONTACT_ID,
            Phone.CONTACT_ID};

    /**电话号码**/
    private static final int COLLECT_NUMBER_INDEX = 0;

    /**姓名**/
    private static final int COLLECT_DISPLAY_NAME_INDEX = 1;

    /**号码类型**/
    private static final int COLLECT_TYPE_INDEX = 2;

    private static final int COLLECT_RAW_CONTACT_ID_INDEX = 3;

    private static final int COLLECT_CONTACT_ID_INDEX = 4;

    private ArrayList<CollectListViewItemData> mData = new ArrayList<CollectListViewItemData>();


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
        mData = getCollectData();
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
                        Intent intent = new Intent(CollectFragement.this.getContext(), CollectPersonalShowActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(CONTACT_PERSONAL_SHOW, mData.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:

                        // delete
                        mData.remove(position);
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
                    Phone.STARRED+"=?", new String[]{"1"}, null);

            if (collectCursor != null) {
                while (collectCursor.moveToNext()) {

                    //得到电话号码
                    String strNumber = collectCursor.getString(COLLECT_NUMBER_INDEX);

                    //得到联系人名称
                    String cachedName = collectCursor.getString(COLLECT_DISPLAY_NAME_INDEX);

                    //得到号码类型
                    int numberType = collectCursor.getInt(COLLECT_TYPE_INDEX);

                    int rawContactID = collectCursor.getInt(COLLECT_RAW_CONTACT_ID_INDEX);

                    int contactID = collectCursor.getInt(COLLECT_CONTACT_ID_INDEX);

                    CollectListViewItemData itemData = new CollectListViewItemData();
                    itemData.setStrPhoneNumber(strNumber);
                    itemData.setName(cachedName);
                    itemData.setPhoneType(numberType);
                    itemData.setRawContactID(rawContactID);
                    itemData.setContactID(contactID);

                    collectInfo.add(itemData);

                }

                collectCursor.close();
            }

        }catch (SecurityException e) {

        }finally {

        }

        return collectInfo;

    }
}
