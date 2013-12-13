package com.fourkkm.citylife;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.fourkkm.citylife.constant.GlobalConfig;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zj.app.utils.AppUtils;

public class TencentShareProxy implements Handler.Callback {

	private Tencent mTencent;
	private Activity mCtx;
	private Handler mHanlder;

	public TencentShareProxy(Activity ctx, Tencent tencent) {
		this.mCtx = ctx;
		this.mTencent = tencent;
	}

	public void onShareToQQ(final Bundle params) {
		AppUtils.getExecutors(mCtx).submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mTencent.shareToQQ(mCtx, params, new BaseUIListener(
						GlobalConfig.IntentKey.INDEX_TENCENT_QQ));

			}
		});
	}

	private class BaseUIListener implements IUiListener {

		private int mShareIndex = -1;

		public BaseUIListener(int shareIndex) {
			this.mShareIndex = shareIndex;
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(JSONObject arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		return false;
	}

	private static final int MSG_SHARE_CANCEL = 0;
	private static final int MSG_SHARE_COMPLETE = 1;
	private static final int MSG_SHARE_ERROR = 2;
	private static final int MSG_SHARE_REAUTH = 3;
}
