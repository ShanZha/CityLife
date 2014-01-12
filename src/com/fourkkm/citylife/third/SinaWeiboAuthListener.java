package com.fourkkm.citylife.third;

import android.os.Bundle;

import com.fourkkm.citylife.constant.GlobalConfig;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

/**
 * ÐÂÀËÎ¢²©¼øÈ¨¼àÌý
 * 
 * @author ShanZha
 * 
 */
public class SinaWeiboAuthListener extends BaseAuthListener implements
		WeiboAuthListener {

	public SinaWeiboAuthListener(int type, IThirdAuthListener listener) {
		super(type, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onComplete(Bundle values) {
		String uid = values.getString("uid");
		String accessToken = values.getString("access_token");
		String expireTime = values.getString("expires_in");
		Bundle bundle = new Bundle();
		bundle.putString(GlobalConfig.Third.KEY_ACCESS_TOKEN, accessToken);
		bundle.putString(GlobalConfig.Third.KEY_UID, uid);
		bundle.putLong(GlobalConfig.Third.KEY_EXPIRE_TIME,
				Long.parseLong(expireTime));
		this.notifyAuthSuccess(bundle);
	}

	@Override
	public void onWeiboException(WeiboException e) {
		this.notifyAuthFail(null == e ? "" : e.getMessage());
	}

	@Override
	public void onError(WeiboDialogError e) {
		this.notifyAuthFail(null == e ? "" : e.getMessage());
	}

	@Override
	public void onCancel() {
		this.notifyAuthCancel();
	}

}
