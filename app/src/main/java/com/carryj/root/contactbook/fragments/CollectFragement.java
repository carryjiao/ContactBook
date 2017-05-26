package com.carryj.root.contactbook.fragments;


import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.carryj.root.contactbook.AddCollectActivity;
import com.carryj.root.contactbook.ContactBookApplication;
import com.carryj.root.contactbook.ContactPersonalShowActivity;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.adapter.CollectAdapter;
import com.carryj.root.contactbook.data.CollectListViewItemData;
import com.carryj.root.contactbook.event.CallNavigationViewEvent;
import com.carryj.root.contactbook.event.CollectEvent;
import com.carryj.root.contactbook.event.DialEvent;
import com.carryj.root.contactbook.event.HeadPhotoChangeEvent;
import com.carryj.root.contactbook.tools.UserHeadPhotoManager;
import com.makeramen.roundedimageview.RoundedImageView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InputStream;
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

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1001;

    private TextView tv_collect_add;
    private RoundedImageView head_photo;
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

    private UserHeadPhotoManager userHeadPhotoManager;
    private Bitmap bitmap;


    public CollectFragement() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_collect,null);
        initView(view);
        return view;
    }

    private void initData() {
        //异步加载收藏联系人数据
        new AsyncTask<Void, Void, ArrayList<CollectListViewItemData>>() {
            @Override
            protected ArrayList<CollectListViewItemData> doInBackground(Void... params) {

                ArrayList<CollectListViewItemData> datas = new ArrayList<CollectListViewItemData>();
                //获取 READ_CONTACTS 权限

                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.READ_CONTACTS)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_CONTACTS},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                } else {

                    datas = getCollectData();

                }
                return datas;
            }


            @Override
            protected void onPostExecute(ArrayList<CollectListViewItemData> datas) {
                mData.addAll(datas);
                adapter.notifyDataSetChanged();
            }
        }.execute();
        ContactBookApplication application = ContactBookApplication.getInstance();
        String telnum = application.getTelnum();
        Log.d("CollectFragment","===========================telnum = "+telnum);
        userHeadPhotoManager = new UserHeadPhotoManager(telnum);
        bitmap = userHeadPhotoManager.getBitmap();

    }

    private void initView(View view) {
        tv_collect_add = (TextView) view.findViewById(R.id.tv_collect_add);
        head_photo = (RoundedImageView) view.findViewById(R.id.head_photo);
        listView = (SwipeMenuListView) view.findViewById(R.id.collect_listview);

        if(bitmap != null) {
            head_photo.setImageBitmap(bitmap);
        }else {
            Log.d("ContactBookFragment","===========================bitmap = null");
        }
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

                EventBus.getDefault().post(new DialEvent(false,true));//设置通话记录数据更新
                callPhone(mData.get(position).getStrPhoneNumber());

            }
        });

        initEvent();



    }

    private void initEvent(){
        tv_collect_add.setOnClickListener(this);
        head_photo.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_collect_add:
                Intent intent = new Intent(this.getContext(), AddCollectActivity.class);
                startActivity(intent);
                break;
            case R.id.head_photo:
                EventBus.getDefault().post(new CallNavigationViewEvent(true));
                break;
            default:
                break;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //提示刷新收藏联系人数据
                EventBus.getDefault().post(new CollectEvent(true));

            } else {
                // Permission Denied
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
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
                    Phone.STARRED+"=?", new String[]{"1"},  Phone.SORT_KEY_PRIMARY+" ASC");

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
                    Bitmap photo = null;


                    //获取头像
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactID);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri, true);
                    if (input != null) {
                        photo = BitmapFactory.decodeStream(input);
                    }

                    CollectListViewItemData itemData = new CollectListViewItemData();
                    itemData.setLookUp(lookUp);
                    itemData.setName(cachedName);
                    itemData.setPhoneType(numberType);
                    itemData.setRawContactID(rawContactID);
                    itemData.setContactID(contactID);
                    itemData.setStrPhoneNumber(number);

                    if(photo != null) {
                        itemData.setBitmap(photo);//设置头像
                    }

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



    //处理数据更新事件
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onHeadPhotoChangeEvent(HeadPhotoChangeEvent headPhotoChangeEvent) {
        if (headPhotoChangeEvent.getHeadPhotoChange().equals("change")) {
            userHeadPhotoManager.refreshHeadPhoto(head_photo);
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectChangeEvent(CollectEvent collectEvent) {
        if (collectEvent.isCollectFlag()) {
            ArrayList<CollectListViewItemData> collectDatas = getCollectData();
            mData.clear();
            mData.addAll(collectDatas);
            adapter.notifyDataSetChanged();
        }
    }


}
