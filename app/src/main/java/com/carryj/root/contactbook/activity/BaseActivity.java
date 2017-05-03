package com.carryj.root.contactbook.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 
 *@Name: BaseActivity
 * 
 *@Description: �Զ����Activity�࣬��������Activity���̳У�
 *								�̳�ʱ��ʵ�֣�
 *								initData();  ���ݳ�ʼ������
 *								initView();  ���ֳ�ʼ������
 *								initEvents(); �¼���ʼ������
 *
 *@author: ���������桢�޼�
 *
 */
public abstract class BaseActivity extends Activity implements OnClickListener {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setContentView(int layoutResID) {
		// TODO Auto-generated method stub
		super.setContentView(layoutResID);
		initData();
		initView();
		initEvents();
	}

	@Override
	public void setContentView(View view) {
		// TODO Auto-generated method stub
		super.setContentView(view);
		initData();
		initView();
		initEvents();
	}

	protected abstract void initData();

	protected abstract void initView();

	protected abstract void initEvents();

}
