package com.carryj.root.contactbook.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carryj.root.contactbook.ContactBookApplication;
import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.SweepBackActivity;
import com.carryj.root.contactbook.constant_values.HTTPCODES;
import com.carryj.root.contactbook.constant_values.ServletPaths;
import com.carryj.root.contactbook.event.NumberChangeEvent;
import com.carryj.root.contactbook.ui.RoundProgressBar;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BackupActivity extends SweepBackActivity {

    private LinearLayout ll_backup_back;

    private RoundProgressBar progressBar;

    private TextView tv_progress_str;

    private Button btn_backup_cloud_backup;
    private Button btn_backup_local_backup;
    private Button btn_backup_cloud_recovery;
    private Button btn_backup_local_recovery;

    private boolean localRecoveryFlag;
    private int total;
    private String telnum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

    }

    @Override
    protected void initData() {
        total = 0;
        localRecoveryFlag = false;
        ContactBookApplication applicationLogin = (ContactBookApplication) getApplication();
        telnum = applicationLogin.getTelnum();
    }

    @Override
    protected void initView() {
        ll_backup_back = (LinearLayout) findViewById(R.id.ll_backup_back);
        progressBar = (RoundProgressBar) findViewById(R.id.backup_progressBar);
        tv_progress_str = (TextView) findViewById(R.id.tv_progress_str);

        btn_backup_cloud_backup = (Button) findViewById(R.id.btn_backup_cloud_backup);
        btn_backup_local_backup = (Button) findViewById(R.id.btn_backup_local_backup);
        btn_backup_cloud_recovery = (Button) findViewById(R.id.btn_backup_cloud_recovery);
        btn_backup_local_recovery = (Button) findViewById(R.id.btn_backup_local_recovery);

        progressBar.setProgress(0);
        progressBar.setCricleProgressColor(0xFF1AE642);
        progressBar.setCricleColor(0xFF111100);
        progressBar.setTextIsDisplayable(false);

        tv_progress_str.setText("0");

    }

    @Override
    protected void initEvents() {
        ll_backup_back.setOnClickListener(this);
        btn_backup_cloud_backup.setOnClickListener(this);
        btn_backup_local_backup.setOnClickListener(this);
        btn_backup_cloud_recovery.setOnClickListener(this);
        btn_backup_local_recovery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_backup_back:
                finish();
                break;

            case R.id.btn_backup_cloud_backup:
                uploadFile();
                break;

            case R.id.btn_backup_local_backup:
                localBackup();
                break;

            case R.id.btn_backup_cloud_recovery:
                break;

            case R.id.btn_backup_local_recovery:
                localRecovery();
                break;

            default:
                break;
        }

    }


    @Override
    public void finish() {
        if(localRecoveryFlag) {
            EventBus.getDefault().post(new NumberChangeEvent(true));
        }
        super.finish();
    }

    private void localBackup() {
        new AsyncTask<Void, Integer, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {

                try {

                    //String path = Environment.getExternalStorageDirectory().getPath() + "/contacts.vcf";

                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "/contacts.vcf");

                    ContentResolver resolver = BackupActivity.this.getContentResolver();
                    Cursor cur = resolver.query(Contacts.CONTENT_URI, null, null, null, null);
                    int index = cur.getColumnIndex(Contacts.LOOKUP_KEY);
                    total = cur.getCount()-1;


                    FileOutputStream fout = new FileOutputStream(file);

                    byte[] data = new byte[1024 * 1];

                    if(cur != null) {
                        while (cur.moveToNext()) {
                            String lookupKey = cur.getString(index);

                            Uri uri = Uri.withAppendedPath(Contacts.CONTENT_VCARD_URI, lookupKey);

                            AssetFileDescriptor fd = resolver.openAssetFileDescriptor(uri, "r");
                            FileInputStream fin = fd.createInputStream();
                            int len = -1;
                            while ((len = fin.read(data)) != -1) {
                                fout.write(data, 0, len);
                                publishProgress(cur.getPosition());
                            }
                            fin.close();
                        }
                        fout.close();
                        cur.close();
                        return true;
                    }else {
                        Log.d("===============", "cur == null=======================================");
                        return false;
                    }


                }catch (IOException e) {
                    Log.d("===============", "IOException======================================="+e.getMessage());
                    return false;
                }

            }

            @Override
            protected void onProgressUpdate(Integer... values) {

                progressBar.setMax(total);
                Log.d("===============", "progressBar===  total===================================="+total);
                progressBar.setProgress(values[0]);
                Log.d("===============", "progressBar======================================="+values[0]);
                int progress = 100*values[0]/total;
                tv_progress_str.setText(progress+"");

            }

            @Override
            protected void onPostExecute(Boolean b) {
                if (b) {
                    Toast.makeText(BackupActivity.this, "备份成功", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(BackupActivity.this, "备份失败", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    private void localRecovery() {

        new AsyncTask<Void, Integer, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {

                try {
                    ContentResolver resolver = BackupActivity.this.getContentResolver();
                    Cursor cur = resolver.query(RawContacts.CONTENT_URI, null, null, null, null);
                    int index = cur.getColumnIndex(RawContacts._ID);
                    total = cur.getCount()-1;

                    if(cur != null) {
                        while (cur.moveToNext()) {
                            String rawContactID = cur.getString(index);
                            publishProgress(cur.getPosition());
                            resolver.delete(RawContacts.CONTENT_URI, RawContacts._ID+"=?", new String[]{rawContactID});
                            resolver.delete(Data.CONTENT_URI, RawContacts._ID+"=?", new String[]{rawContactID});
                        }
                        cur.close();
                        return true;
                    }else {
                        Log.d("===============", "cur == null=======================================");
                        return false;
                    }

                }catch (Exception e) {
                    return false;
                }

            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                progressBar.setMax(total);
                Log.d("===============", "progressBar===  total===================================="+total);
                progressBar.setProgress(values[0]);
                Log.d("===============", "progressBar======================================="+values[0]);
                int progress = 100*values[0]/total;
                tv_progress_str.setText(progress+"");
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(BackupActivity.this, "恢复预处理成功,正在进行联系人恢复", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setPackage("com.android.contacts");
                    Uri uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath(), "/contacts.vcf"));
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "text/x-vcard");
                    startActivity(intent);
                    localRecoveryFlag = true;

                }else {
                    Toast.makeText(BackupActivity.this, "恢复预处理失败,联系人恢复中断", Toast.LENGTH_SHORT).show();
                }

            }
        }.execute();


    }



    public void uploadFile()
    {

        String url = ServletPaths.ServerServletAddress+ServletPaths.UploadFileServlet;

        String filePath = Environment.getExternalStorageDirectory().getPath()
                + "/contacts.vcf";

        AsyncHttpClient httpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        try
        {
            param.put("telnum", telnum);
            param.put("file", new File(filePath));

            httpClient.post(url, param, new AsyncHttpResponseHandler()
            {
                @Override
                public void onStart()
                {
                    super.onStart();

                    Toast.makeText(BackupActivity.this, "正在上传...", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onSuccess(String arg0)
                {
                    super.onSuccess(arg0);

                    Log.i("ck", "success>" + arg0);

                    if(arg0.equals("success"))
                    {
                        Toast.makeText(BackupActivity.this, "上传成功!", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(BackupActivity.this, "服务器写入数据错误", Toast.LENGTH_SHORT).show();
                    }


                }

                @Override
                public void onFailure(Throwable arg0, String arg1)
                {
                    super.onFailure(arg0, arg1);

                    Toast.makeText(BackupActivity.this, "上传失败", Toast.LENGTH_LONG).show();
                }
            });

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(BackupActivity.this, "上传文件不存在！", Toast.LENGTH_LONG).show();
        }
    }

}
