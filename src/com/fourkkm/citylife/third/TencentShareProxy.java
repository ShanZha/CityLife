package com.fourkkm.citylife.third;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zj.app.utils.AppUtils;

public class TencentShareProxy {

	private Tencent mTencent;
	private Activity mCtx;

	public TencentShareProxy(Activity ctx, Tencent tencent) {
		this.mCtx = ctx;
		this.mTencent = tencent;
	}

	public void onLogin(String scope, QQAuthListener authListener) {
		if (null != mTencent) {
			mTencent.login(mCtx, scope, authListener);
		}
	}

	public void setAuthInfo(String accessToken, String openId, String expireTime) {
		if (null != mTencent) {
			mTencent.setAccessToken(accessToken, expireTime);
			mTencent.setOpenId(openId);
		}
	}

	public boolean isSessionValid() {
		if (null == mTencent) {
			return false;
		}
		return mTencent.isSessionValid();
	}

	public boolean isReady() {
		if (null == mTencent) {
			return false;
		}
		return mTencent.ready(mCtx);
	}

	public void onShareToQQ(Bundle params, IThirdShareListener listener) {
		AppUtils.getExecutors(mCtx).submit(
				new TencentShareTask(params,
						GlobalConfig.IntentKey.INDEX_TENCENT_QQ, listener));
	}

	public void onShareToQZone(Bundle params, IThirdShareListener listener) {
		AppUtils.getExecutors(mCtx).submit(
				new TencentShareTask(params,
						GlobalConfig.IntentKey.INDEX_TENCENT_QZONE, listener));
	}

	private class BaseUIListener implements IUiListener {

		private int mShareIndex = -1;
		private IThirdShareListener mListener;

		public BaseUIListener(int shareIndex, IThirdShareListener listener) {
			this.mShareIndex = shareIndex;
			this.mListener = listener;
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			AppUtils.getHandler(mCtx).post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (null != mListener) {
						mListener.onShareCancel(mShareIndex);
					}
				}
			});
		}

		@Override
		public void onComplete(JSONObject arg0) {
			// TODO Auto-generated method stub
			AppUtils.getHandler(mCtx).post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (null != mListener) {
						mListener.onShareSuccess(mShareIndex, null);
					}
				}
			});
		}

		@Override
		public void onError(final UiError arg0) {
			// TODO Auto-generated method stub
			AppUtils.getHandler(mCtx).post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (null != mListener) {
						mListener.onShareFail(mShareIndex, arg0.errorMessage);
					}
				}
			});
		}

	}

	private class TencentShareTask implements Runnable {

		private Bundle mParams;
		private int mShareIndex = -1;
		private IThirdShareListener mListener;

		public TencentShareTask(Bundle params, int shareIndex,
				IThirdShareListener listener) {
			this.mParams = params;
			this.mShareIndex = shareIndex;
			this.mListener = listener;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {

				switch (mShareIndex) {
				case GlobalConfig.IntentKey.INDEX_TENCENT_QQ:
					mTencent.shareToQQ(mCtx, mParams, new BaseUIListener(
							mShareIndex, mListener));
					break;
				case GlobalConfig.IntentKey.INDEX_TENCENT_QZONE:
					mTencent.shareToQzone(mCtx, mParams, new BaseUIListener(
							mShareIndex, mListener));
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(" fhasldfhasjkf: " + e.getMessage());
				if (e.getMessage().contains("com.tencent.mobileqq")) {
					System.out.println(" ²»Ö§³Ö");
				}
			}

		}

	}

}
