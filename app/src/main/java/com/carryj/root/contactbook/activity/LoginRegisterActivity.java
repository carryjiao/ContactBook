package com.carryj.root.contactbook.activity;

import org.apache.log4j.Logger;

import com.carryj.root.contactbook.MainActivity;
import com.carryj.root.contactbook.SweepBackActivity;
import com.carryj.root.contactbook.constant_values.HTTPCODES;
import com.carryj.root.contactbook.net.LoginRegisterListener;
import com.carryj.root.contactbook.net.LoginRegisterManager;
import com.carryj.root.contactbook.ui.MyProgressDialog;
import com.carryj.root.contactbook.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class LoginRegisterActivity extends SweepBackActivity {
	private LinearLayout ll_login_register_back;
	private EditText et_phonenum;
	private TextView tv_next_step;
	private Logger logger;
	public static final String TELNUM_EXTRA = "TELNUM_EXTRA";
	private boolean isRegister = true;
	private LoginRegisterListener mListener;
	private MyProgressDialog progressDialog;
	private String telnum;
	private TextView tv_in;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		logger = Logger.getLogger(LoginRegisterActivity.class);
		setContentView(R.layout.activity_login_register);

	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ll_login_register_back = (LinearLayout) findViewById(R.id.ll_login_register_back);
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		tv_next_step = (TextView) findViewById(R.id.tv_next_step);
		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telnum = telephonyManager.getLine1Number();//��ȡ��������
		logger.debug("-------phoneNum: " + telnum);
		et_phonenum.setText(telnum);
		progressDialog = new MyProgressDialog(this);
		tv_in = (TextView)findViewById(R.id.tv_in);
		tv_in.setOnClickListener(this);

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		tv_next_step.setOnClickListener(this);
		ll_login_register_back.setOnClickListener(this);
		mListener = new LoginRegisterListener() {

			@Override
			public void onVerify(int resultCode) {
				// TODO Auto-generated method stub
				progressDialog.dismiss();

			}

			@Override
			public void onRegister(int resultCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDetect(int resultCode) {
				// TODO Auto-generated method stub
				logger.debug("onDetect resultCode: " + resultCode);
				progressDialog.dismiss();
				/**
				 * �жϴӷ������˷��صĲ�ѯ���
				 */
				if (resultCode == HTTPCODES.DETECT_REGISTERED) {//��ע��
					Intent intent = new Intent(LoginRegisterActivity.this, LoginActivity.class);
					intent.putExtra(TELNUM_EXTRA, telnum);
					startActivityForResult(intent, 0);//��ת����¼����

				} else {//δע��
					Intent intent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
					intent.putExtra(TELNUM_EXTRA, telnum);
					intent.putExtra(RegisterActivity.REQUEST_MODE, RegisterActivity.REQUEST_REGISTER);
					startActivityForResult(intent, 0);//��ת��ע�����
				}

			}

			@Override
			public void onLogout(int resultCode) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onResetPsw(int resultCode) {
				// TODO Auto-generated method stub
				
			}
		};
		LoginRegisterManager.getInstance().registerListener(mListener);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_in: //��ίδ����ʱ�����
			Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
			break;
		case R.id.tv_next_step: //��һ��
			telnum = et_phonenum.getText().toString().trim();
			if (telnum != null && !telnum.equals("")) {
				if (telnum.length() == 11) {
					progressDialog.show("ʶ����...");
					LoginRegisterManager.getInstance().detect(telnum);//��������˲�ѯ���û��Ƿ���ע��

				} else {
					Toast.makeText(this, "�ֻ�����д����", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "������д�ֻ��ţ�", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.ll_login_register_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == LoginActivity.RESULT_LOGIN_OK || resultCode == RegisterActivity.RESULT_REGISTER_OK) {
			this.finish();//�ɹ���¼��ע��󣬽���Activity
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoginRegisterManager.getInstance().unregisterListener(mListener);//����ʱȡ���������İ�
	}

}
