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
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Im;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.SweepBackActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ShowORCodeActivity extends SweepBackActivity {

    private String URL;
    private ImageView imageView;
    private Bitmap ORCode;
    private Handler handler;

    private LinearLayout ll_show_orcode_back;
    private TextView tv_show_orcode_share;

    private static boolean FLAG = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_orcode);
    }

    @Override
    protected void initData() {


        URL = getIntent().getStringExtra("ORCODE");
        Log.d("==========URL=","*******************************"+URL+"*******************************");

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
                    ORCode = getBitmap("http://qr.liantu.com/api.php?text="+URL);
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
                new Thread(){
                    @Override
                    public void run()
                    {
                        ORCode = getBitmap("http://qr.liantu.com/api.php?text="+URL);
                        Message msg = new Message();
                        // 把bm存入消息中,发送到主线程
                        msg.obj = ORCode;
                        handler.sendMessage(msg);
                    }
                }.start();
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

        ll_show_orcode_back = (LinearLayout) findViewById(R.id.ll_show_orcode_back);
        tv_show_orcode_share = (TextView) findViewById(R.id.tv_show_orcode_share);
        handler = new Handler() {
            public void handleMessage(Message msg) {

                imageView = (ImageView) findViewById(R.id.im_show_orcode_orcode);
                imageView.setImageBitmap((Bitmap) msg.obj);
            }
        };

    }

    @Override
    protected void initEvents() {
        ll_show_orcode_back.setOnClickListener(this);
        tv_show_orcode_share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_show_orcode_back:
                this.finish();
                break;
            case R.id.tv_show_orcode_share:

            default:
                break;
        }

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
                FLAG = true;
                return bitmap;
            }else {
                Log.d("***************","获取图片失败****************************");
                FLAG = false;
            }


        }catch (IOException e){
            FLAG = false;
            Log.d("***************","getIOException****************************");
            e.printStackTrace();
        }finally {
            conn.disconnect();//断开连接
        }
        return null;
    }


}
