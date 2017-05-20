package com.carryj.root.contactbook.tools;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.carryj.root.contactbook.ContactBookApplication;


import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
/**
 * 
 *@Name: HttpUtils
 * 
 *@Description: �ӷ������˻�ȡ����
 *
 *@author: ���������桢�޼�
 *
 */
public class HttpUtils {

	private static HttpUtils httpUtils = new HttpUtils();

	private HttpUtils() {
		// TODO Auto-generated constructor stub
	}

	public static HttpUtils getInstance() {
		return httpUtils;
	}

	/**
	 * 
	 *@Name: isNetworkConnected()
	 *
	 *@Description: �ж��ֻ��Ƿ�����
	 *
	 */
	public boolean isNetworkConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) ContactBookApplication.appContext
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isAvailable()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 *@Name: getContent()
	 *
	 *@Description: ����http�������ݣ��ӷ���˻�ȡ����
	 *
	 */
	public String getContent(String urlPath) throws ConnectException {
		String result = "";
		InputStream in = getInputStream(urlPath);
		ByteArrayOutputStream baos = null;
		if (in != null) {
			baos = new ByteArrayOutputStream();
			byte data[] = new byte[1024];
			int len;
			try {
				while ((len = in.read(data)) != -1) {
					baos.write(data, 0, len);
				}
				result = new String(baos.toByteArray()).trim();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			if (baos != null) {
				baos.close();
			}
			if (in != null) {
				in.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 *@Name: getInputStream()
	 *
	 *@Description: ��ȡ������
	 *
	 */
	public static InputStream getInputStream(String urlPath) throws ConnectException {
		InputStream in = null;
		try {
			URL url = new URL(urlPath);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setReadTimeout(5000);
			conn.setDoInput(true);
			if (conn.getResponseCode() == 200) {
				in = conn.getInputStream();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectException e) {
			// TODO: handle exception
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return in;
	}

}
