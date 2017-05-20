package com.carryj.root.contactbook.activity;

import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.carryj.root.contactbook.ContactBookApplication;
import com.carryj.root.contactbook.MainActivity;
import com.carryj.root.contactbook.SweepBackActivity;
import com.carryj.root.contactbook.constant_values.HTTPCODES;
import com.carryj.root.contactbook.net.LoginRegisterListener;
import com.carryj.root.contactbook.net.LoginRegisterManager;
import com.carryj.root.contactbook.ui.MyProgressDialog;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import com.carryj.root.contactbook.R;
/**
 * 
 *@Name: RegisterActivity
 * 
 *@Description:通过输入用户密码，和短信验证码，进行注册
 *
 *
 *@author: 焦消
 *
 */
public class RegisterActivity extends SweepBackActivity {
	//模式设置：注册模式和重置密码模式
	public static final String REQUEST_MODE = "REQUEST_MODE";
	public static final int REQUEST_REGISTER = 1;
	public static final int REQUEST_SET_PSW = 2;
	public static final int RESULT_REGISTER_OK = 1;
	public static final int RESULT_SET_PSW_OK = 2;
	private int request_mode;
	private TextView tv_title_register_setpsw;
	private EditText et_verifycode;
	private EditText et_password;
	private LinearLayout ll_register_back;
	private TextView tv_login;
	private TextView tv_code_operation;
	private ToggleButton tg_view_psw;
	private Logger logger;
	private String telnum;
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static final String APPKEY = "fd41235f7406";
	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static final String APPSECRET = "2fe552cfe432b5a4e4a450ea0080158e";
	private EventHandler smsHandler;
	private Handler mHandler;
	private LoginRegisterListener mListener;
	private MyProgressDialog progressDialog;
	private String psw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		//////////////// -----------------------��ȡ��֤����ʱע�͵�----------------------/////////////////////
		// SMSSDK.getVerificationCode("86", telnum);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		logger = Logger.getLogger(RegisterActivity.class);
		Intent intent = getIntent();
		//获取从上一个界面传来的用户名
		telnum = intent.getStringExtra(LoginRegisterActivity.TELNUM_EXTRA);
		logger.debug("----------telnum: " + telnum);
		//获取从上一个界面传来的请求模式
		request_mode = intent.getIntExtra(REQUEST_MODE, REQUEST_REGISTER);
		logger.debug("----------request_mode: " + request_mode);

	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		tv_title_register_setpsw = (TextView) findViewById(R.id.tv_title_register_setpsw);
		ll_register_back = (LinearLayout) findViewById(R.id.ll_register_back);
		et_verifycode = (EditText) findViewById(R.id.et_verifycode);
		et_password = (EditText) findViewById(R.id.et_password);
		tv_login = (TextView) findViewById(R.id.tv_register_setpsw);
		tv_code_operation = (TextView) findViewById(R.id.tv_get_code);
		tg_view_psw = (ToggleButton) findViewById(R.id.tg_view_psw);
		//设置重置密码的界面文字
		if (request_mode == REQUEST_SET_PSW) {
			tv_title_register_setpsw.setText("重置登录" + "密码");
			tv_login.setText("重置密码");

		}
		timeCount();
		progressDialog = new MyProgressDialog(this);

	}

	@Override
	protected void initEvents() {
		// TODO Auto-generated method stub
		ll_register_back.setOnClickListener(this);
		tv_login.setOnClickListener(this);
		tv_code_operation.setOnClickListener(this);
		tg_view_psw.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {// 隐藏密码
					et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				} else {//显示密码
					et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

				}
			}
		});
		//短信验证
		/*SMSSDK.initSDK(this, APPKEY, APPSECRET, true);
		smsHandler = new EventHandler() {

			@Override
			public void afterEvent(int event, int result, Object data) {
				// TODO Auto-generated method stub
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				mHandler.sendMessage(msg);

			}

		};
		SMSSDK.registerEventHandler(smsHandler);
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				int event = msg.arg1;
				int result = msg.arg2;
				Object data = msg.obj;
				if (result == SMSSDK.RESULT_COMPLETE) {
					switch (event) {
					// �ύ������֤��ɹ�
					case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
						logger.debug("-----------提交验证码成功");

						break;
					// ��ȡ��֤��ɹ�
					case SMSSDK.EVENT_GET_VERIFICATION_CODE:
						logger.debug("---------获取验证码成功");
						break;

					default:
						break;
					}
				} else {
					((Throwable) data).printStackTrace();
				}
			}
		};*/
		mListener = new LoginRegisterListener() {

			@Override
			public void onVerify(int resultCode) {
				// TODO Auto-generated method stub
				logger.debug("----onverify�� " + resultCode);
				progressDialog.dismiss();
				if (resultCode == HTTPCODES.VERIFY_SUCCESS) {
					ContactBookApplication application = (ContactBookApplication) getApplication();
					application.setTelnum(telnum);
					application.setPsw(psw);
					application.setLogin(true);
					Toast.makeText(RegisterActivity.this, "重置密码登录成功！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(RegisterActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onRegister(int resultCode) {
				// TODO Auto-generated method stub
				logger.debug("----onregister�� " + resultCode);
				progressDialog.dismiss();
				if (resultCode == HTTPCODES.REGISTER_SUCCESS) {
					ContactBookApplication application = (ContactBookApplication) getApplication();
					application.setTelnum(telnum);
					application.setPsw(psw);
					application.setLogin(true);
					Toast.makeText(RegisterActivity.this, "注册登录成功！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
					startActivity(intent);
					setResult(RESULT_REGISTER_OK);
				} else {
					Toast.makeText(RegisterActivity.this, "注册失败!", Toast.LENGTH_SHORT).show();
				}

			}

			@Override
			public void onLogout(int resultCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDetect(int resultCode) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResetPsw(int resultCode) {
				// TODO Auto-generated method stub

				if (resultCode == HTTPCODES.RESET_PSW_SUCCESS) {
					progressDialog.show("重置密码成功！登录中...");
					LoginRegisterManager.getInstance().Login(telnum, psw);
					setResult(RESULT_SET_PSW_OK);
				} else {
					progressDialog.dismiss();
					Toast.makeText(RegisterActivity.this, "重置密码失败！", Toast.LENGTH_SHORT).show();
				}

			}
		};

		LoginRegisterManager.getInstance().registerListener(mListener);
	}

	/**
	 * 
	 *@Name: timeCount()
	 *
	 *@Description:短信验证重发倒计时
	 *
	 */
	private void timeCount() {
		tv_code_operation.setTextColor(Color.GRAY);
		tv_code_operation.setEnabled(false);
		final Timer timer = new Timer();
		new AsyncTask<Void, Integer, Void>() {

			@Override
			protected void onProgressUpdate(Integer... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);
				if (values[0] >= 0) {
					tv_code_operation.setText(values[0] + "s");
				} else {
					tv_code_operation.setTextColor(Color.BLUE);
					tv_code_operation.setText("重发");
					tv_code_operation.setEnabled(true);
				}

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				timer.schedule(new TimerTask() {
					int count = 60;  //重新计时60秒

					@Override
					public void run() {
						// TODO Auto-generated method stub
						count--;
						publishProgress(count);
						if (count < 0) {
							timer.cancel();
						}

					}
				}, 1000, 1000);
				return null;
			}
		}.execute();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tv_register_setpsw:

			String verifyCode = et_verifycode.getText().toString().trim();
			if (verifyCode != null && !verifyCode.equals("")) {
				//////////////// -----------------------�ύ��֤����ʱע�͵�----------------------/////////////////////
				// SMSSDK.submitVerificationCode("86", telnum, verifyCode);
				psw = et_password.getText().toString().trim();
				if (request_mode == REQUEST_REGISTER) {
					if (psw != null && !psw.equals("")) {
						progressDialog.show("注册中请稍等...");
						LoginRegisterManager.getInstance().register(telnum, psw);//向服务器端发送用户注册信息
					} else {
						Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
					}

				} else if (request_mode == REQUEST_SET_PSW) {
					if (psw != null && !psw.equals("")) {
						progressDialog.show("重置中请稍等...");
						logger.debug("------>>resetpsw>>------telnum: "+telnum+";psw: "+psw);
						LoginRegisterManager.getInstance().resetPsw(telnum, psw);//向服务器端发送重置密码信息
					} else {
						Toast.makeText(this, "请输入密码！", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(this, "请先填写验证码！", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.tv_get_code:
			//////////////// -----------------------��ȡ��֤����ʱע�͵�----------------------/////////////////////
			// SMSSDK.getVerificationCode("86", telnum);
			timeCount();
			break;
		case R.id.ll_register_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//SMSSDK.unregisterEventHandler(smsHandler);
		LoginRegisterManager.getInstance().unregisterListener(mListener);
	}

}
