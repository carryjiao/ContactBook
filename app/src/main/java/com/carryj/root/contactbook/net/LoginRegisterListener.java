package com.carryj.root.contactbook.net;
/**
 *登录或注册的监听器接口
 *
 */
public interface LoginRegisterListener {
	public void onDetect(int resultCode);

	public void onVerify(int resultCode);

	public void onRegister(int resultCode);

	public void onLogout(int resultCode);

	public void onResetPsw(int resultCode);

}
