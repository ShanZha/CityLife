package com.fourkkm.citylife.third;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

import com.fourkkm.citylife.constant.GlobalConfig;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

/**
 * QQ¼øÈ¨¼àÌý
 * 
 * @author ShanZha
 * 
 */
public class QQAuthListener extends BaseAuthListener implements IUiListener {

	public QQAuthListener(int type, IThirdAuthListener listener) {
		super(type, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onComplete(JSONObject arg0) {
		// TODO Auto-generated method stub
		try {
			String accessToken = arg0.getString("access_token");
			String openid = arg0.getString("openid");
			String expires_in = arg0.getString("expires_in");
			Bundle bundle = new Bundle();
			bundle.putString(GlobalConfig.Third.KEY_ACCESS_TOKEN, accessToken);
			bundle.putString(GlobalConfig.Third.KEY_UID, openid);
			bundle.putLong(GlobalConfig.Third.KEY_EXPIRE_TIME,
					Long.parseLong(expires_in));
			this.notifyAuthSuccess(bundle);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.notifyAuthFail(e.getMessage());
		}
	}

	@Override
	public void onError(UiError arg0) {
		// TODO Auto-generated method stub
		this.notifyAuthFail(arg0.errorMessage);
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		this.notifyAuthCancel();
	}

}
