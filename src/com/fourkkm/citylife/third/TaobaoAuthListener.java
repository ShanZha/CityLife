package com.fourkkm.citylife.third;

import java.util.Date;

import android.os.Bundle;

import com.fourkkm.citylife.constant.GlobalConfig;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;

/**
 * ÌÔ±¦¼øÈ¨¼àÌý
 * 
 * @author ShanZha
 * 
 */
public class TaobaoAuthListener extends BaseAuthListener implements
		AuthorizeListener {

	public TaobaoAuthListener(int type, IThirdAuthListener listener) {
		super(type, listener);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onComplete(AccessToken accessToken) {
		// TODO Auto-generated method stub
		String uid = accessToken.getAdditionalInformation().get(
				AccessToken.KEY_SUB_TAOBAO_USER_ID);
		String nick = accessToken.getAdditionalInformation().get(
				AccessToken.KEY_SUB_TAOBAO_USER_NICK);
		String r2_expires = accessToken.getAdditionalInformation().get(
				AccessToken.KEY_R2_EXPIRES_IN);
		Date start = accessToken.getStartDate();
		long expireTime = start.getTime() + Long.parseLong(r2_expires) * 1000L;
		String access_token = accessToken.getValue();

		Bundle bundle = new Bundle();
		bundle.putString(GlobalConfig.Third.KEY_ACCESS_TOKEN, access_token);
		bundle.putString(GlobalConfig.Third.KEY_UID, uid);
		bundle.putLong(GlobalConfig.Third.KEY_EXPIRE_TIME, expireTime);
		bundle.putString(GlobalConfig.Third.KEY_NICK_NAME, nick);
		this.notifyAuthSuccess(bundle);
	}

	@Override
	public void onError(AuthError e) {
		// TODO Auto-generated method stub
		this.notifyAuthFail(null == e ? "" : e.getErrorDescription());
	}

	@Override
	public void onAuthException(AuthException e) {
		// TODO Auto-generated method stub
		this.notifyAuthFail(null == e ? "" : e.getMessage());
	}

}
