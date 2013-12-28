package com.fourkkm.citylife.third;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.IRequestListener;

public class BaseQQRequestListener implements IRequestListener {

	private int mType = -1;
	private IThirdUserInfoListener mListener;

	public BaseQQRequestListener(int type, IThirdUserInfoListener listener) {
		this.mType = type;
		this.mListener = listener;
	}

	private void notifyFail(String e) {
		if (null != mListener) {
			mListener.onThirdUserInfoFail(mType, e);
		}
	}

	@Override
	public void onComplete(JSONObject arg0, Object arg1) {
		// TODO Auto-generated method stub
		try {
			String nick = arg0.getString("nick");
			if (null != mListener) {
				mListener.onThirdUserInfoSuccess(mType, nick);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.notifyFail(e.getMessage());
		}
	}

	@Override
	public void onConnectTimeoutException(ConnectTimeoutException arg0,
			Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

	@Override
	public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

	@Override
	public void onIOException(IOException arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

	@Override
	public void onJSONException(JSONException arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

	@Override
	public void onMalformedURLException(MalformedURLException arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

	@Override
	public void onNetworkUnavailableException(NetworkUnavailableException arg0,
			Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

	@Override
	public void onSocketTimeoutException(SocketTimeoutException arg0,
			Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

	@Override
	public void onUnknowException(Exception arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (null != arg0) {
			this.notifyFail(arg0.getMessage());
		}
	}

}
