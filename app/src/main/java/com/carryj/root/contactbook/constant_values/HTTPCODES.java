package com.carryj.root.contactbook.constant_values;
/**
 * 
 *@Name: HTTPCODES
 * 
 *@Description: �ͻ��˺ͷ������˽��н���ʱ�������ʾ��Ϣ��
 *
 *@author: ���������桢�޼�
 *
 */
public class HTTPCODES {

	//��¼ע�Ჿ��
	public static final int REGISTER_FAILED = 100;
	public static final int REGISTER_SUCCESS = 101;
	public static final int DETECT_UNREGISTER = 102;
	public static final int DETECT_REGISTERED = 103;

	//�˺Ź�����
	public static final int VERIFY_FAILED = 110;
	public static final int VERIFY_SUCCESS = 111;
	public static final int LOGOUT_SUCCESS = 120;
	public static final int LOGOUT_FAILED = 121;
	public static final int RESET_PSW_FAILED = 130;
	public static final int RESET_PSW_SUCCESS = 131;
	public static final int UPDATE_MONEY_FAILED = 140;
	public static final int UPDATE_MONEY_SUCCESS = 141;

	//�����𴲼ƻ�����
	public static final int JOIN_FAILED = 200;
	public static final int JOIN_SUCCESS = 201;
	public static final int JOIN_TIME_RIGHT = 202;
	public static final int JOIN_TOO_EARLY = 203;
	public static final int JOIN_TOO_LATE = 204;
	public static final int JOINED = 205;
	public static final int JOIN_NONE = 206;

	//ǩ������
	public static final int SIGN_FAILED = 210;
	public static final int SIGN_SUCCESS = 211;
	public static final int SIGN_TIME_RIGHT = 212;
	public static final int SIGN_TOO_EARLY = 213;
	public static final int SIGN_TOO_LATE = 214;
	public static final int SIGN_NEVER_JOINED = 215;
	public static final int SIGNED = 216;

	//��¼ע�᳣���ֶ�
	public static final String ACTION = "action";
	public static final String ACTION_DETECT = "detect";
	public static final String ACTION_VERIFY = "verify";
	public static final String ACTION_REGISTER = "register";
	public static final String ACTION_LOGOUT = "logout";
	public static final String ACTION_SET_PSW = "setpsw";
	public static final String ACTION_SIGN = "sign";
	public static final String ACTION_JOIN = "join";
	public static final String ACTION_DETECT_JOIN = "detect_join";
	
	//����ģʽ����
	public static final String STATE_MYRECORD = "1";
	public static final String STATE_MYREDENVELOPE = "2";

}
