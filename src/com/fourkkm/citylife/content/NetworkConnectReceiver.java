package com.fourkkm.citylife.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;

import com.fourkkm.citylife.CoreApp;
import com.zj.app.utils.AppUtils;

/**
 * ÍøÂç×´Ì¬¼àÌý
 * 
 * @author ShanZha
 * 
 */
public class NetworkConnectReceiver extends BroadcastReceiver {

	private static final String TAG = "NetworkConnectReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			NetworkInfo info = intent
					.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			State state = info.getState();
			if (state == State.CONNECTED) {
				Log.i(TAG, "shan-->onReceive() connected");
				((CoreApp) AppUtils.getBaseApp(context)).startPushInfoService();
			} else if (state == State.DISCONNECTED) {
				Log.i(TAG, "shan-->onReceive() unconnected");
				((CoreApp) AppUtils.getBaseApp(context)).stopPushInfoService();
			}
		}
	}

}
