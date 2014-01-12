package com.fourkkm.citylife.control.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMemberPassport;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.third.IThirdAuthListener;
import com.fourkkm.citylife.third.IThirdShareListener;
import com.fourkkm.citylife.third.IThirdUserInfoListener;
import com.fourkkm.citylife.third.QQAuthListener;
import com.fourkkm.citylife.third.SinaWeiboAuthListener;
import com.fourkkm.citylife.third.SinaWeiboShareProxy;
import com.fourkkm.citylife.third.TencentShareProxy;
import com.fourkkm.citylife.third.TencentWeiboShareProxy;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.tencent.sdkutil.Util;
import com.tencent.tauth.Tencent;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.util.AccessTokenKeeper;
import com.zj.app.BaseActivity;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

/**
 * 第三方分享界面(新浪自带，主要处理QQ、腾讯微博)
 * 
 * @author ShanZha
 * 
 */
public class ShareActivity extends BaseActivity implements TextWatcher,
		IThirdAuthListener, IThirdUserInfoListener, IThirdShareListener,
		IWeiboHandler.Response {

	private static final String TAG = "ShareActivity";
	private static final int TENCENT_WEIBO_REQ_CODE = 1;
	private static final int TYPE_SINA_WEIBO = 1;
	private static final int TYPE_QQ = 2;
	private static final int TYPE_TENCENT_WEIBO = 3;
	private static final int TYPE_TAOBAO = 4;
	private TextView mTvTitle, mTvLimit;
	private EditText mEtContent;
	private Button mBtnShare;

	private String mContent;
	private ProgressDialogProxy mDialogProxy;
	private int mIndex = -1;
	// private Tencent mTencent;
	private ModoerMembers mMember;
	private ModoerMemberPassport mMemberPassport;
	private int mRetryCount = 0;
	private SinaWeiboShareProxy mSinaShareProxy;
	private TencentShareProxy mTencentProxy;
	private Weibo mSinaWeibo;

	private int mMaxLength = 0;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.share);
		mTvTitle = (TextView) this.findViewById(R.id.share_tv_title);
		mTvLimit = (TextView) this.findViewById(R.id.share_tv_limit);
		mBtnShare = (Button) this.findViewById(R.id.share_btn_share);
		mEtContent = (EditText) this.findViewById(R.id.share_et_content);

	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mDialogProxy = new ProgressDialogProxy(this);
		Intent intent = this.getIntent();
		mIndex = intent.getIntExtra("shareIndex", -1);
		mContent = intent.getStringExtra("shareContent");
		mEtContent.setText(mContent);
		mEtContent.addTextChangedListener(this);
		mEtContent.setFocusable(true);
		mEtContent.requestFocus();
		mEtContent.setSelection(mContent.length());
		mEtContent.setScrollbarFadingEnabled(true);
		mEtContent.setGravity(Gravity.TOP);
		// try {
		// String num = (mContentLength - mContent.getBytes("gbk").length / 2)
		// + "";
		// mTvLimit.setText("还可输入" + num + "字");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		IWeiboAPI mSinaWeiboApi = WeiboSDK.createWeiboAPI(this,
				GlobalConfig.Third.SINA_WEIBO_APP_KEY);
		mSinaShareProxy = new SinaWeiboShareProxy(this, mSinaWeiboApi);
		mSinaWeibo = Weibo.getInstance(GlobalConfig.Third.SINA_WEIBO_APP_KEY,
				GlobalConfig.Third.SINA_WEIBO_REDIRECT_URL,
				GlobalConfig.Third.SINA_WEIBO_SCOPE);
		mSinaShareProxy.setResponseListener(this.getIntent(), this);
		mSinaShareProxy.registerApp();
		Tencent tencent = Tencent.createInstance(
				GlobalConfig.Third.TENCENT_QQ_APP_ID,
				this.getApplicationContext());
		mTencentProxy = new TencentShareProxy(this, tencent);

		if (!this.isShareOK()) {
			this.finish();
			return;
		}

		this.setShareBtnEnable(false);
		this.showWaitting();
		mMember = ((CoreApp) AppUtils.getBaseApp(this)).getCurrMember();
		this.fetchMemberByThirdInfo(this.getPsnameByIndex());

	}

	private boolean isShareOK() {
		switch (mIndex) {
		case GlobalConfig.IntentKey.INDEX_SINA_WEIBO:
			mTvTitle.setText(this.getString(R.string.share_sina_weibo));
			mMaxLength = 140;
			if (!mSinaShareProxy.isInstall()) {
				this.showToast(this.getString(R.string.share_sina_uninstall));
				return false;
			}
			if (!mSinaShareProxy.isAppSupport()) {
				this.showToast(this
						.getString(R.string.share_sina_version_mismatch));
				return false;
			}
			break;
		case GlobalConfig.IntentKey.INDEX_TENCENT_QQ:
			if (!com.tencent.sdkutil.Util.checkMobileQQ(this)) {
				this.showToast(this
						.getString(R.string.share_qq_uninstall_or_mismatch));
				return false;
			}
			mTvTitle.setText(this.getString(R.string.share_qq));
			mMaxLength = 40;
			break;
		case GlobalConfig.IntentKey.INDEX_TENCENT_QZONE:
			if (!com.tencent.sdkutil.Util.checkMobileQQ(this)) {
				this.showToast(this
						.getString(R.string.share_qq_uninstall_or_mismatch));
				return false;
			}
			mMaxLength = 600;
			mTvTitle.setText(this.getString(R.string.share_qzone));
			break;
		case GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO:
			mTvTitle.setText(this.getString(R.string.share_tencent_weibo));
			break;
		}
		return true;
	}

	private void setShareBtnEnable(boolean enable) {
		mBtnShare.setEnabled(enable);
		mBtnShare.setClickable(enable);
		mBtnShare.setFocusable(enable);
		if (enable) {
			mBtnShare.setTextColor(this.getResources().getColor(
					android.R.color.white));
		} else {
			mBtnShare.setTextColor(this.getResources().getColor(
					R.color.spinner_text_color));
		}
	}

	/**
	 * 根据平台名称
	 * 
	 * @param psname
	 */
	private void fetchMemberByThirdInfo(String psname) {
		String selectCode = "from com.andrnes.modoer.ModoerMemberPassport mmp where mmp.psname = :psname and mmp.uid.id = "
				+ mMember.getId();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("psname", psname);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FIND_MEMBERPASSPORT);
		this.getStoreOperation().find(selectCode, paramsMap, true,
				new ModoerMemberPassport(), param);
	}

	private String getPsnameByIndex() {
		switch (mIndex) {
		case GlobalConfig.IntentKey.INDEX_TENCENT_QQ:
		case GlobalConfig.IntentKey.INDEX_TENCENT_QZONE:
			return GlobalConfig.Third.PSNAME_TENCENT_QQ;
		case GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO:
			return GlobalConfig.Third.PSNAME_TENCENT_WEIBO;
		case GlobalConfig.IntentKey.INDEX_SINA_WEIBO:
			return GlobalConfig.Third.PSNAME_SINA_WEIBO;
		}
		return "";
	}

	private void showWaitting() {
		if (null != mDialogProxy) {
			mDialogProxy.showDialog();
		}
	}

	private void hideWaitting() {
		if (null != mDialogProxy) {
			mDialogProxy.hideDialog();
		}
	}

	/**
	 * 第三方鉴权完毕时，构建ModoerMemberPassport
	 * 
	 * @param psname
	 * @param accessToken
	 * @param uid
	 * @param expireTime
	 */
	private void buildMemberPassport(String psname, String accessToken,
			String uid, long expireTime) {
		mMemberPassport = new ModoerMemberPassport();
		mMemberPassport.setPsname(psname);
		mMemberPassport.setAccessToken(accessToken);
		mMemberPassport.setExpired(expireTime);
		mMemberPassport.setPsuid(uid);
		mMemberPassport.setIsbind(1);
		mMemberPassport.setUid(mMember);
	}

	private void onSaveOrUpdatePassport() {
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_MEMBER);
		this.getStoreOperation().saveOrUpdate(mMemberPassport, param);
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickShare(View view) {
		switch (mIndex) {
		case GlobalConfig.IntentKey.INDEX_TENCENT_QQ:
			this.onShareToQQ();
			break;
		case GlobalConfig.IntentKey.INDEX_TENCENT_QZONE:
			this.onShareQZone();
			break;
		case GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO:
			this.onShareTencentWeibo();
			break;
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mSinaShareProxy.setResponseListener(intent, this);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		// try {
		// mContent = s.toString();
		// String num = (mContentLength - mContent.getBytes("gbk").length / 2)
		// + "";
		// mTvLimit.setText("还可输入" + num + "字");
		// } catch (UnsupportedEncodingException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFind(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FIND_MEMBERPASSPORT:
			Object result = out.getResult();
			if (null == result) {// 还没注册过
				if (GlobalConfig.IntentKey.INDEX_TENCENT_QQ == mIndex
						|| GlobalConfig.IntentKey.INDEX_TENCENT_QZONE == mIndex) {
					mTencentProxy.onLogin(GlobalConfig.Third.TENCENT_QQ_SCOPE,
							new QQAuthListener(TYPE_QQ, this));
				} else if (GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO == mIndex) {
					this.startActivityForResult(new Intent(this,
							TencentAuthActivity.class), TENCENT_WEIBO_REQ_CODE);
				} else if (GlobalConfig.IntentKey.INDEX_SINA_WEIBO == mIndex) {
					mSinaWeibo.anthorize(this, new SinaWeiboAuthListener(
							TYPE_SINA_WEIBO, this));
				}
			} else {
				mMemberPassport = (ModoerMemberPassport) result;
				this.hideWaitting();
				if (GlobalConfig.IntentKey.INDEX_TENCENT_QQ == mIndex
						|| GlobalConfig.IntentKey.INDEX_TENCENT_QZONE == mIndex) {
					long expireTime = mMemberPassport.getExpired() * 1000;
					mTencentProxy.setAuthInfo(mMemberPassport.getAccessToken(),
							mMemberPassport.getPsuid(),
							String.valueOf(expireTime));
					if (mTencentProxy.isSessionValid()) {
						this.setShareBtnEnable(true);
						return;
					}
					mTencentProxy.onLogin(GlobalConfig.Third.TENCENT_QQ_SCOPE,
							new QQAuthListener(TYPE_QQ, this));
				} else if (GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO == mIndex) {
					this.startActivityForResult(new Intent(this,
							TencentAuthActivity.class), TENCENT_WEIBO_REQ_CODE);
				} else if (GlobalConfig.IntentKey.INDEX_SINA_WEIBO == mIndex) {
					Oauth2AccessToken mAccessToken = AccessTokenKeeper
							.readAccessToken(this);
					if (mAccessToken.isSessionValid()) {
						this.setShareBtnEnable(true);
						this.onShareSinaWeibo();
					} else {
						mSinaWeibo.anthorize(this, new SinaWeiboAuthListener(
								TYPE_SINA_WEIBO, this));
					}

				}

			}
			break;
		}
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_MEMBER == operator) {
			mMemberPassport = (ModoerMemberPassport) out.getResult();
			this.hideWaitting();
			switch (mIndex) {
			case GlobalConfig.IntentKey.INDEX_SINA_WEIBO:
				Oauth2AccessToken mAccessToken = AccessTokenKeeper
						.readAccessToken(this);
				if (mAccessToken.isSessionValid()) {
					this.setShareBtnEnable(true);
					this.onShareSinaWeibo();
				} else {
					Log.e(TAG, "shan-->session is invalid");
				}
				break;
			case GlobalConfig.IntentKey.INDEX_TENCENT_QQ:
			case GlobalConfig.IntentKey.INDEX_TENCENT_QZONE:
			case GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO:
				this.setShareBtnEnable(true);
				break;
			}

		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FIND_MEMBERPASSPORT == operator) {
			if (mRetryCount < 3) {
				this.fetchMemberByThirdInfo(this.getPsnameByIndex());
				mRetryCount++;
			} else {
				Log.e(TAG, "shan--> find ModoerPassport fails");
				this.hideWaitting();
			}
		} else if (GlobalConfig.Operator.OPERATION_SAVE_MEMBER == operator) {
			Log.e(TAG, "shan--> save ModoerPassport fails");
			this.hideWaitting();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		if (TENCENT_WEIBO_REQ_CODE == requestCode) {// 腾讯微博登录成功
			if (null != data) {
				this.onThirdAuthSuccess(TYPE_TENCENT_WEIBO,
						data.getBundleExtra("values"));
			}
		}
	}

	private void onShareSinaWeibo() {
		if (null != mSinaShareProxy) {
			mSinaShareProxy.registerApp();
			mSinaShareProxy.share(mEtContent.getText().toString().trim());
		}
	}

	private void onShareToQQ() {
		if (mTencentProxy.isSessionValid() && mTencentProxy.isReady()) {
			Bundle params = new Bundle();
			params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE,
					Tencent.SHARE_TO_QQ_TYPE_DEFAULT);
			params.putString(Tencent.SHARE_TO_QQ_TITLE,
					this.getString(R.string.share));
			params.putString(Tencent.SHARE_TO_QQ_SUMMARY, mContent);
			params.putString(Tencent.SHARE_TO_QQ_TARGET_URL,
					GlobalConfig.URL_PIC);
			// params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL,
			// "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
			params.putString(Tencent.SHARE_TO_QQ_APP_NAME,
					this.getString(R.string.app_name));
			// 隐藏空间分享（单独）
			params.putInt(Tencent.SHARE_TO_QQ_EXT_INT,
					Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
			mTencentProxy.onShareToQQ(params, this);

		} else {
			Log.e(TAG, "shan-->Tencent session invalid or not ready");
		}

	}

	private void onShareQZone() {
		if (mTencentProxy.isSessionValid() && mTencentProxy.isReady()) {
			Bundle params = new Bundle();
			params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE,
					Tencent.SHARE_TO_QQ_TYPE_DEFAULT);
			params.putString(Tencent.SHARE_TO_QQ_TITLE,
					this.getString(R.string.share));
			params.putString(Tencent.SHARE_TO_QQ_SUMMARY, mContent);
			params.putString(Tencent.SHARE_TO_QQ_TARGET_URL,
					GlobalConfig.URL_PIC);
			// 显示空间分享
			params.putInt(Tencent.SHARE_TO_QQ_EXT_INT,
					Tencent.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
			// params.putStringArrayList(Tencent.SHARE_TO_QQ_IMAGE_URL,
			// "图片链接ArrayList");
			mTencentProxy.onShareToQQ(params, this);
		} else {
			Log.e(TAG, "shan-->Tencent session invalid or not ready");
		}

	}

	private void onShareTencentWeibo() {
		if (null != mMemberPassport) {
			this.showWaitting();
			TencentWeiboShareProxy.getTencentApi().addWeibo(this,
					mMemberPassport.getAccessToken(),
					mMemberPassport.getPsuid(), mContent,
					CommonUtil.getLocalIPAddress(this));
		}
	}

	@Override
	public void onThirdAuthSuccess(int type, Bundle bundle) {
		// TODO Auto-generated method stub
		String accessToken = bundle
				.getString(GlobalConfig.Third.KEY_ACCESS_TOKEN);
		String uid = bundle.getString(GlobalConfig.Third.KEY_UID);
		long expireTime = bundle.getLong(GlobalConfig.Third.KEY_EXPIRE_TIME);
		Log.e(TAG, "shan-->onThirdAuthSuccess: " + accessToken + " ,uid: "
				+ uid + " ,expireTime: " + expireTime);
		switch (type) {
		case TYPE_QQ:
			expireTime = (System.currentTimeMillis() + expireTime * 1000) / 1000;
			mTencentProxy.setAuthInfo(accessToken, uid,
					String.valueOf(expireTime));
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_TENCENT_QQ,
					accessToken, uid, expireTime);
			break;
		case TYPE_TENCENT_WEIBO:
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_TENCENT_WEIBO,
					accessToken, uid, expireTime);
			break;
		case TYPE_SINA_WEIBO:
			Oauth2AccessToken token = new Oauth2AccessToken(accessToken,
					String.valueOf(expireTime));
			AccessTokenKeeper.keepAccessToken(ShareActivity.this, token);
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_SINA_WEIBO,
					accessToken, uid, expireTime);
			break;
		}
		// Step 2:保存MemberPassport
		this.onSaveOrUpdatePassport();
	}

	@Override
	public void onThirdAuthFail(int type, String e) {
		// TODO Auto-generated method stub
		Log.e(TAG, "shan-->onThirdAuthFail(): " + e);
		this.hideWaitting();
		this.finish();
	}

	@Override
	public void onThirdAuthCancel(int type) {
		// TODO Auto-generated method stub
		Log.e(TAG, "shan-->onThirdAuthCancel()");
		this.hideWaitting();
		this.finish();
	}

	@Override
	public void onThirdUserInfoSuccess(int type, String nickname) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onThirdUserInfoFail(int type, String error) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShareSuccess(int type, Bundle bundle) {
		// TODO Auto-generated method stub
		this.showToast(this.getString(R.string.share_success));
		if (GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO == type) {
			this.hideWaitting();
		}
		this.finish();
	}

	@Override
	public void onShareFail(int type, String error) {
		// TODO Auto-generated method stub
		if (GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO == type) {
			this.hideWaitting();
		}
		this.showToast(this.getString(R.string.share_fail));
	}

	@Override
	public void onShareCancel(int type) {
		// TODO Auto-generated method stub
		// this.showToast(this.getString(R.string.share_cancel));
		this.finish();
	}

	@Override
	public void onResponse(BaseResponse baseResp) {// 新浪微博分享回调
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
			this.showToast(this.getString(R.string.share_success));
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
			this.showToast(this.getString(R.string.share_cancel));
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
			this.showToast(this.getString(R.string.share_fail)
					+ baseResp.errMsg);
			break;
		}
		this.hideWaitting();
		this.finish();
	}
}
