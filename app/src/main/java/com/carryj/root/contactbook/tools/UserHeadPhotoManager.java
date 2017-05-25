package com.carryj.root.contactbook.tools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.carryj.root.contactbook.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by root on 17/5/25.
 */

public class UserHeadPhotoManager {


    public String telnum;
    public Bitmap bitmap;

    public UserHeadPhotoManager(String telnum) {
        this.telnum = telnum;
    }

    public String getTelnum() {
        return telnum;
    }

    public void setTelnum(String telnum) {
        this.telnum = telnum;
    }

    public Bitmap getBitmap() {
        // 文件夹地址
        String tempPath = "ContactBook/"+telnum;

        String filePath = tempPath + "/head.jpg";

        FileUtils fileutils = new FileUtils();

        // 判断sd卡上的文件夹是否存在
        if (!fileutils.isFileExist(tempPath)) {
            fileutils.createSDDir(tempPath);
        }

        bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getPath()
                + "/" + filePath);// 从SD卡中找头像，转换成Bitmap
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public void getHeadPhoto(final Activity activity, final int galleryRequestCode, final int cameraRequestCode) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(activity, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);

        tv_select_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentGallery = new Intent(Intent.ACTION_PICK, null);
                intentGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activity.startActivityForResult(intentGallery, galleryRequestCode);
                dialog.dismiss();
            }
        });

        tv_select_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory().getPath(),
                                "/ContactBook/"+telnum+"/head.jpg")));
                activity.startActivityForResult(intentCamera, cameraRequestCode);// 采用ForResult打开
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.show();
    }


    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri, Activity activity, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
// aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, requestCode);
    }

    public void setPicToSDCard(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        String fileName = Environment.getExternalStorageDirectory().getPath()+
                "/ContactBook/"+telnum+"/head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
// 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void refreshHeadPhoto(RoundedImageView headPhoto) {
        Bitmap bitmap = getBitmap();
        headPhoto.setImageBitmap(bitmap);
    }
}
