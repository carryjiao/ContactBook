package com.carryj.root.contactbook.activity;

import android.content.ContentResolver;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.carryj.root.contactbook.R;
import com.carryj.root.contactbook.SweepBackActivity;
import com.carryj.root.contactbook.ui.RoundProgressBar;

import java.io.File;
import java.io.FileInputStream;
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


    private int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);

    }

    @Override
    protected void initData() {
        total = 0;
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
                break;

            case R.id.btn_backup_local_backup:
                localBackup();
                break;

            case R.id.btn_backup_cloud_recovery:
                break;

            case R.id.btn_backup_local_recovery:
                break;

            default:
                break;
        }

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
}
