package com.fourkkm.citylife.wxapi;

import android.content.Intent;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.control.activity.ShareActivity;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.platformtools.Log;
import com.zj.app.BaseActivity;

/**
 * 微信分享入口
 * 
 * @author ShanZha
 * 
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	private static final String TAG = "WXEntryActivity";
	private static final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
	private IWXAPI mWxApi;
	private int mIndex = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		Intent intent = this.getIntent();
		mIndex = intent.getIntExtra("shareIndex", -1);
		mWxApi = WXAPIFactory.createWXAPI(this,
				GlobalConfig.Third.WEIXIN_APP_ID, false);
		if (!mWxApi.isWXAppInstalled()) {
			this.showToast(this.getString(R.string.share_weixin_uninstall));
			this.finish();
			return;
		}
		if (!mWxApi.isWXAppSupportAPI()) {
			this.showToast(this.getString(R.string.share_weixin_unsupport));
			this.finish();
			return;
		}
		if (GlobalConfig.IntentKey.INDEX_WEIXIN_TIMELINE == mIndex) {
			int wxSdkVersion = mWxApi.getWXAppSupportAPI();
			if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
				this.showToast(this
						.getString(R.string.share_weixin_unsupport_timeline));
				this.finish();
			}
		}
		mWxApi.registerApp(GlobalConfig.Third.WEIXIN_APP_ID);
		mWxApi.handleIntent(getIntent(), this);

		if (mIndex == -1) {
			return;
		}
		Intent temp = new Intent(this, ShareActivity.class);
		temp.putExtra("shareIndex", mIndex);
		temp.putExtra("shareContent", intent.getStringExtra("shareContent"));
		this.startActivity(temp);
		this.finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
		mWxApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResp(BaseResp resp) {
		// TODO Auto-generated method stub
		Log.i(TAG, "shan-->Weixin Share = " + (null == resp ? "" : resp.errStr));
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = this.getString(R.string.share_success);
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = this.getString(R.string.share_cancel);
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = this.getString(R.string.share_fail);
			break;
		default:
			result = this.getString(R.string.share_fail);
			break;
		}
		this.showToast(result);
		this.finish();
	}
}
