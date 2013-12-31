package com.fourkkm.citylife.widget;

import android.os.Bundle;

public interface ILocationConnListener {

	public void onConnected(Bundle data);

	public void onDisconnect();

	public void onConnectFail(String error);

}
