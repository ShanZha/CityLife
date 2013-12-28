package com.fourkkm.citylife.third;

import org.json.JSONObject;

import com.fourkkm.citylife.constant.GlobalConfig;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.HttpManager;

/**
 * 获取新浪微博用户信息任务
 * 
 * @author ShanZha
 * 
 */
public class SinaWeiboUserInfoTask implements Runnable {

	private int mType = -1;
	private String mToken, mUid;
	private IThirdUserInfoListener mThirdListener;

	public SinaWeiboUserInfoTask(String access_token, String uid,
			IThirdUserInfoListener listener, int type) {
		this.mToken = access_token;
		this.mUid = uid;
		this.mThirdListener = listener;
		this.mType = type;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		WeiboParameters params = new WeiboParameters();
		params.add("access_token", mToken);
		params.add("uid", mUid);
		try {
			String str = HttpManager.openUrl(
					GlobalConfig.Third.SINA_WEIBO_GET_USERINFO_URL, "GET",
					params, "");
			JSONObject json = new JSONObject(str);
			String name = json.getString("name");
			if (null != mThirdListener) {
				mThirdListener.onThirdUserInfoSuccess(mType, name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (null != mThirdListener) {
				mThirdListener.onThirdUserInfoFail(mType, e.getMessage());
			}
		}
	}
}
