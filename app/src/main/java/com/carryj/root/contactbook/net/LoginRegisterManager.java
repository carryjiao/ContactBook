package com.carryj.root.contactbook.net;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.carryj.root.contactbook.constant_values.HTTPCODES;
import com.carryj.root.contactbook.constant_values.ServletPaths;
import com.carryj.root.contactbook.constant_values.UserTable;
import com.carryj.root.contactbook.tools.HttpUtils;

import android.os.AsyncTask;
/**
 * 
 *@Name: LoginRegisterManager
 * 
 *@Description: ���ڶԡ���¼���͡�ע�ᡱ����ģ��
 *								�ͷ�����������ݽ������й���
 *
 *@author: ���������桢�޼�
 *
 */
public class LoginRegisterManager {
	private List<LoginRegisterListener> listeners = new ArrayList<LoginRegisterListener>();
	private static LoginRegisterManager manager = new LoginRegisterManager();
	private Logger logger;

	private LoginRegisterManager() {
		logger = Logger.getLogger(LoginRegisterManager.class);
	}

	public static LoginRegisterManager getInstance() {
		return manager;
	}

	public void registerListener(LoginRegisterListener listener) {
		listeners.add(listener);
	}

	public void unregisterListener(LoginRegisterListener listener) {
		listeners.remove(listener);
	}

	public void detect(String telnum) {
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result = null;
				StringBuilder url = new StringBuilder(ServletPaths.ServerServletAddress);
				url.append(ServletPaths.LoginRegisterServlet).append("?").append(HTTPCODES.ACTION).append("=")
						.append(HTTPCODES.ACTION_DETECT).append("&").append(UserTable.TELNUM).append("=")
						.append(params[0]);
				try {
					result = HttpUtils.getInstance().getContent(url.toString());
					
					System.out.println(url.toString()+"--------0404-----result:"+result);
					logger.debug("detect resultCode:" + result);
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result != null && !result.equals("")) {
					int resultCode = Integer.parseInt(result);
					for (LoginRegisterListener listener : listeners) {
						listener.onDetect(resultCode);
					}
				}

			}

		}.execute(telnum);

	}

	/**
	 * 
	 *@Name:  Login()
	 *
	 *@Description: ��¼
	 *
	 */
	public void Login(String telnum, String psw) {
		String parms[] = new String[2];
		parms[0] = telnum;
		parms[1] = psw;
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result = null;
				StringBuilder url = new StringBuilder(ServletPaths.ServerServletAddress);
				url.append(ServletPaths.LoginRegisterServlet).append("?").append(HTTPCODES.ACTION).append("=")
						.append(HTTPCODES.ACTION_VERIFY).append("&").append(UserTable.TELNUM).append("=")
						.append(params[0]).append("&").append(UserTable.PSW).append("=").append(params[1]);
				try {
					result = HttpUtils.getInstance().getContent(url.toString());
					logger.debug("verify:" + params[0] + "--" + params[1] + ";resultCode:" + result);
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result != null && !result.equals("")) {
					int resultCode = Integer.parseInt(result);
					for (LoginRegisterListener listener : listeners) {
						listener.onVerify(resultCode);
					}
				}
			}

		}.execute(parms);
	}

	/**
	 * 
	 *@Name: register()
	 *
	 *@Description: ע��
	 *
	 */
	public void register(String telnum, String psw) {
		String parms[] = new String[2];
		parms[0] = telnum;
		parms[1] = psw;
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result = null;
				StringBuilder url = new StringBuilder(ServletPaths.ServerServletAddress);
				url.append(ServletPaths.LoginRegisterServlet).append("?").append(HTTPCODES.ACTION).append("=")
						.append(HTTPCODES.ACTION_REGISTER).append("&").append(UserTable.TELNUM).append("=")
						.append(params[0]).append("&").append(UserTable.PSW).append("=").append(params[1]);
				try {
					result = HttpUtils.getInstance().getContent(url.toString());
					logger.debug("register:" + params[0] + "--" + params[1] + ";resultCode:" + result);
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result != null && !result.equals("")) {
					int resultCode = Integer.parseInt(result);
					for (LoginRegisterListener listener : listeners) {
						listener.onRegister(resultCode);
					}
				}
			}

		}.execute(parms);
	}

	/**
	 * 
	 *@Name: logout()
	 *
	 *@Description: �˳���¼
	 *
	 */
	public void logout(String telnum, String psw) {
		String parms[] = new String[2];
		parms[0] = telnum;
		parms[1] = psw;
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result = null;
				StringBuilder url = new StringBuilder(ServletPaths.ServerServletAddress);
				url.append(ServletPaths.LoginRegisterServlet).append("?").append(HTTPCODES.ACTION).append("=")
						.append(HTTPCODES.ACTION_LOGOUT).append("&").append(UserTable.TELNUM).append("=")
						.append(params[0]).append("&").append(UserTable.PSW).append("=").append(params[1]);
				try {
					result = HttpUtils.getInstance().getContent(url.toString());
					logger.debug("logout:" + params[0] + "--" + params[1] + ";resultCode:" + result);
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result != null && !result.equals("")) {
					int resultCode = Integer.parseInt(result);
					for (LoginRegisterListener listener : listeners) {
						listener.onLogout(resultCode);
					}
				}
			}

		}.execute(parms);

	}

	/**
	 * 
	 *@Name: resetPsw()
	 *
	 *@Description: ��������
	 *
	 */
	public void resetPsw(String telnum, String newpsw) {
		String parms[] = new String[2];
		parms[0] = telnum;
		parms[1] = newpsw;
		new AsyncTask<String, Void, String>() {

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				String result = null;
				StringBuilder url = new StringBuilder(ServletPaths.ServerServletAddress);
				url.append(ServletPaths.LoginRegisterServlet).append("?").append(HTTPCODES.ACTION).append("=")
						.append(HTTPCODES.ACTION_SET_PSW).append("&").append(UserTable.TELNUM).append("=")
						.append(params[0]).append("&").append(UserTable.PSW).append("=").append(params[1]);
				try {
					result = HttpUtils.getInstance().getContent(url.toString());
					logger.debug("resetpsw:" + params[0] + "--" + params[1] + ";resultCode:" + result);
				} catch (ConnectException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (result != null && !result.equals("")) {
					int resultCode = Integer.parseInt(result);
					for (LoginRegisterListener listener : listeners) {
						listener.onResetPsw(resultCode);
					}
				}
			}

		}.execute(parms);

	}
}
