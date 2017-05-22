package com.carryj.root.contactbook.activity;

import org.apache.log4j.Logger;

import com.carryj.root.contactbook.ContactBookApplication;
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



/*登录或注册时，输入用户名(电话号码)界面复用
 *								通过查询服务器端该用户是否已注册，若已注册则跳转至输入登录时输入密码的界面
 *								若未注册，则跳转至用户注册时输入密码和短信验证码的界面
 */


public class LoginRegisterActivity extends SweepBackActivity {
	private LinearLayout ll_login_register_back;
	private EditText et_phonenum;
	private TextView tv_next_step;
	private Logger logger;
	public static final String TELNUM_EXTRA = "TELNUM_EXTRA";

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
		ContactBookApplication application = (ContactBookApplication) getApplication();
		telnum = application.getTelnum();

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ll_login_register_back = (LinearLayout) findViewById(R.id.ll_login_register_back);
		et_phonenum = (EditText) findViewById(R.id.et_phonenum);
		tv_next_step = (TextView) findViewById(R.id.tv_next_step);
		/*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telnum = telephonyManager.getLine1Number();//获取本机号码*/
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
				 * 判断从服务器端返回的查询结果
				 */
				if (resultCode == HTTPCODES.DETECT_REGISTERED) {//已注册
					Intent intent = new Intent(LoginRegisterActivity.this, LoginActivity.class);
					intent.putExtra(TELNUM_EXTRA, telnum);
					startActivityForResult(intent, 0);//跳转至登录界面

				} else if(resultCode == 104) {
					Toast.makeText(LoginRegisterActivity.this, "服务器数据库连接错误",Toast.LENGTH_LONG).show();
				} else {//未注册
					Intent intent = new Intent(LoginRegisterActivity.this, RegisterActivity.class);
					intent.putExtra(TELNUM_EXTRA, telnum);
					intent.putExtra(RegisterActivity.REQUEST_MODE, RegisterActivity.REQUEST_REGISTER);
					startActivityForResult(intent, 0);//跳转至注册界面
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
		case R.id.tv_in:
			Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
			break;
		case R.id.tv_next_step:
			telnum = et_phonenum.getText().toString().trim();
			if (telnum != null && !telnum.equals("")) {
				if (telnum.length() == 11) {
					progressDialog.show("识别中...");
					LoginRegisterManager.getInstance().detect(telnum);//向服务器端查询该用户是否已注册

				} else {
					Toast.makeText(this, "手机号填写有误！", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(this, "请先填写手机号！", Toast.LENGTH_SHORT).show();
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
			this.finish();//成功登录或注册后，结束Activity
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		LoginRegisterManager.getInstance().unregisterListener(mListener);//����ʱȡ���������İ�
	}

}
