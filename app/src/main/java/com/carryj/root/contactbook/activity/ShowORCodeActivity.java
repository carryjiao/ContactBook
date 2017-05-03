package com.carryj.root.contactbook.activity;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.SweepBackActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ShowORCodeActivity extends SweepBackActivity {

    private String SELECTOR;
    private ImageView imageView;
    private Bitmap ORCode;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_orcode);
    }

    @Override
    protected void initData() {



        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, 99);
        } else
        {
            new Thread(){
                @Override
                public void run()
                {
                    ORCode = getBitmap("http://qr.liantu.com/api.php?text=焦消13251356557");
                    Message msg = new Message();
                    // 把bm存入消息中,发送到主线程
                    msg.obj = ORCode;
                    handler.sendMessage(msg);
                }
            }.start();

        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == 99)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                ORCode = getBitmap("http://qr.liantu.com/api.php?text=焦消13251356557");
            } else
            {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initView() {

        handler = new Handler() {
            public void handleMessage(Message msg) {

                imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };

    }

    @Override
    protected void initEvents() {

    }

    @Override
    public void onClick(View v) {

    }

    private boolean insert(String name, String phoneNumber, String email, String im_qq) {

        try
        {
            ContentValues values = new ContentValues();

            // 下面的操作会根据RawContacts表中已有的rawContactId使用情况自动生成新联系人的rawContactId
            Uri rawContactUri = getContentResolver().insert(RawContacts.CONTENT_URI, values);
            long rawContactId = ContentUris.parseId(rawContactUri);

            // 向data表插入姓名数据
            if (name.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
                values.put(StructuredName.GIVEN_NAME, name);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入电话数据
            if (phoneNumber.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
                values.put(Phone.NUMBER, phoneNumber);
                values.put(Phone.TYPE, Phone.TYPE_MOBILE);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入Email数据
            if (email.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
                values.put(Email.DATA, email);
                values.put(Email.TYPE, Email.TYPE_WORK);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }

            // 向data表插入QQ数据
            if (im_qq.length()>0)
            {
                values.clear();
                values.put(Data.RAW_CONTACT_ID, rawContactId);
                values.put(Data.MIMETYPE, Im.CONTENT_ITEM_TYPE);
                values.put(Im.DATA, im_qq);
                values.put(Im.PROTOCOL, Im.PROTOCOL_QQ);
                getContentResolver().insert(Data.CONTENT_URI, values);
            }
        }

        catch (Exception e)
        {
            return false;
        }

        return true;

    }


    public static Bitmap getBitmap(String path) {
        HttpURLConnection conn = null;
        try {

            URL url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }else
                System.out.println("失败");

        }catch (IOException e){
            e.printStackTrace();
        }finally {
            conn.disconnect();//断开连接
        }
        return null;
    }
}
