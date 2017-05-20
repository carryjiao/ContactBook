package com.carryj.root.contactbook;

import org.apache.log4j.Level;

import com.carryj.root.contactbook.constant_values.UserTable;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import de.mindpipe.android.logging.log4j.LogConfigurator;
/**
 * 
 *@Name: RedAlarmApplication
 * 
 *@Description: 通讯录
 *								存储轻量级数据，如用户名，密码，登录状态等
 *
 *@author: ���������桢�޼�
 *
 */
public class ContactBookApplication extends Application {
	public static Context appContext;
	private String telnum;
	private String psw;
	private boolean isLogin;
	private static final String IS_LOGIN = "IS_LOGIN";
	private SharedPreferences preferences;
	private SharedPreferences.Editor edior;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		appContext = this;
		preferences = getSharedPreferences("data", 0);
		edior = preferences.edit();
		telnum = preferences.getString(UserTable.TELNUM, null);
		psw = preferences.getString(UserTable.PSW, null);
		isLogin = preferences.getBoolean(IS_LOGIN, false);
		initLog4J();
	}

	private void initLog4J() {
		LogConfigurator logConfigurator = new LogConfigurator();
		logConfigurator.setUseFileAppender(false);
		logConfigurator.setRootLevel(Level.DEBUG);
		logConfigurator.configure();
	}

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
		edior.putString(UserTable.TELNUM, telnum).commit();
	}

	public String getPsw() {
		return psw;
	}

	public void setPsw(String psw) {
		this.psw = psw;
		edior.putString(UserTable.PSW, psw).commit();
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
		edior.putBoolean(IS_LOGIN, isLogin).commit();
	}

}
