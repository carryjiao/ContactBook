package com.carryj.root.contactbook.ui;

import android.app.ProgressDialog;
import android.content.Context;
/**
 * 
 *@Name: MyProgressDialog
 * 
 *@Description: ��Ϣ����
 *
 *@author: ���������桢�޼�
 *
 */
public class MyProgressDialog {
	ProgressDialog progressDialog;

	public MyProgressDialog(Context context) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setCanceledOnTouchOutside(false);
	}

	public void show(String msg) {
		progressDialog.setMessage(msg);
		progressDialog.show();
	}

	public void dismiss() {
		progressDialog.dismiss();
	}

}
