package com.winsafe.jiangmenzhibao.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;

import java.util.List;

public class NetworkHelper {
	private NetworkHelper() {}

	/**
	 * 判断当前网络是否可用
	 * @return 0-No network;1-Network is connecting;2-Network has connected
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		if (mConnectivityManager != null) {
			State mMobile = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
			State mWifi = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
			if (mMobile == State.CONNECTING || mWifi == State.CONNECTING) {
				return false;
			}
			if (mMobile == State.CONNECTED || mWifi == State.CONNECTED) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断GPS是否可用
	 * @param context 上下文
	 * @return 可用：true;不可用：false
	 */
	public static boolean isGPSAvailable(Context context) {
		boolean result = false;

		LocationManager mLocationManager = ((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
		List<String> mProviders = mLocationManager.getProviders(true);
		if (mProviders != null && mProviders.size() > 0) {
			result = true;
		}

		return result;
	}
}
