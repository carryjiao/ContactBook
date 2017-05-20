package com.carryj.root.contactbook.net;
/**
 * 
 *@Name: LoginRegisterListener
 * 
 *@Description: ��¼��ע��ļ������ӿ�
 *
 *@author: ���������桢�޼�
 *
 */
public interface LoginRegisterListener {
	public void onDetect(int resultCode);

	public void onVerify(int resultCode);

	public void onRegister(int resultCode);

	public void onLogout(int resultCode);

	public void onResetPsw(int resultCode);

}
