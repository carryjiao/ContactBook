package com.carryj.root.contactbook.activity;

import org.apache.log4j.Logger;

import com.carryj.root.contactbook.ContactBookApplication;
import com.carryj.root.contactbook.MainActivity;
import com.carryj.root.contactbook.SweepBackActivity;
import com.carryj.root.contactbook.constant_values.HTTPCODES;
import com.carryj.root.contactbook.net.LoginRegisterListener;
import com.carryj.root.contactbook.net.LoginRegisterManager;
import com.carryj.root.contactbook.ui.MyProgressDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.carryj.root.contactbook.R;


/**
 * 
 *@Name: LoginActivity
 * 
 *@Description: λ�ã���¼��ע�ᡪ>��¼
 *								ͨ�������û����룬��¼�˻������Ӧ��
 *
 *@author: ���������桢�޼�
 *
 */
public class LoginActivity extends SweepBackActivity {
	public static final int RESULT_LOGIN_OK = 1;
	private LinearLayout ll_login_back;
	private EditText et_password;
	private TextView tv_forget_psw;
	private TextView tv_login;

	private Logger logger;
	private String telnum;
	private LoginRegisterListener mListener;
	private MyProgressDialog progressDialog;
	private String psw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		logger = Logger.getLogger(LoginActivity.class);
		setContentView(R.layout.activity_login);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		//��ȡ�ӵ�¼��ע�����������û���
		telnum = getIntent().getStringExtra(LoginRegisterActivity.TELNUM_EXTRA);
		logger.debug("-------telnum: " + telnum);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ll_login_back = (LinearLayout) findViewById(R.id.ll_login_back);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_forget_psw = (TextView) findViewById(R.id.tv_forget_psw);
		tv_login = (TextView) findViewById(R.id.tv_login);

		progressDialog = new MyProgressDialog(this);
	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		ll_login_back.setOnClickListener(this);
		tv_forget_psw.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		mListener = new LoginRegisterListener() {

			/**
			 * ��¼ʱ���û�����������֤
			 */
			@Override
			public void onVerify(int resultCode) {
				// TODO Auto-generated method stub
				progressDialog.dismiss();
				logger.debug("onverify --------" + resultCode);
				if (resultCode == HTTPCODES.VERIFY_SUCCESS) {
					ContactBookApplication application = (ContactBookApplication) getApplication();
					application.setTelnum(telnum);
					application.setPsw(psw);
					application.setLogin(true);//���õ�¼״̬Ϊ���ѵ�¼

					logger.debug("--->>>---->>>telnum: " + telnum + ";psw: " + psw + ";gettelnum():"
							+ application.getTelnum() + ";getpsw():" + application.getPsw());

					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					LoginActivity.this.startActivity(intent);//��¼�ɹ�����ת��������
					LoginActivity.this.setResult(RESULT_LOGIN_OK);
					LoginActivity.this.finish();
				} else {
					//�û���Ϣ��֤ʧ�ܺ���ʾ�������
					Toast.makeText(LoginActivity.this, "�������", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onRegister(int resultCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDetect(int resultCode) {
				// TODO Auto-generated method stub

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
		case R.id.tv_login:
			progressDialog.show("��¼��...");//�����¼��ť�󣬵�����ʾ��¼��
			psw = et_password.getText().toString().trim();
			if (psw != null && !psw.equals("")) {
				LoginRegisterManager.getInstance().Login(telnum, psw);//��������˷��͵�¼����
			} else {
				//����⵽����Ϊ��ʱ�������û���������
				Toast.makeText(this, "���������룡", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.ll_login_back:
			this.finish();
			break;
		case R.id.tv_forget_psw://��������
			Intent intent = new Intent(this, RegisterActivity.class);
			intent.putExtra(LoginRegisterActivity.TELNUM_EXTRA, telnum);//�����û�������
			//���ڽ��渴�ã�����ģʽΪ��������ģʽ
			intent.putExtra(RegisterActivity.REQUEST_MODE, RegisterActivity.REQUEST_SET_PSW);
			startActivity(intent);//��ת�������������
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RegisterActivity.RESULT_SET_PSW_OK) {
			this.finish();//�������óɹ��󣬽�����Activity
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoginRegisterManager.getInstance().unregisterListener(mListener);
	}
}
