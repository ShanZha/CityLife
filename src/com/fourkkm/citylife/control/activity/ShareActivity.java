package com.fourkkm.citylife.control.activity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import com.fourkkm.citylife.third.IThirdUserInfoListener;
import com.fourkkm.citylife.third.QQAuthListener;
import com.fourkkm.citylife.third.SinaWeiboAuthListener;
import com.fourkkm.citylife.third.SinaWeiboShareProxy;
import com.fourkkm.citylife.third.TencentShareProxy;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.tencent.tauth.Tencent;
import com.weibo.sdk.android.Weibo;
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
		IThirdAuthListener, IThirdUserInfoListener, IWeiboHandler.Response {

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
//	private Tencent mTencent;
	private ModoerMembers mMember;
	private ModoerMemberPassport mMemberPassport;
	private int mRetryCount = 0;
	private SinaWeiboShareProxy mSinaShareProxy;
	private TencentShareProxy mTencentProxy;
	private Weibo mSinaWeibo;

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

		IWeiboAPI mSinaWeiboApi = WeiboSDK.createWeiboAPI(this,
				GlobalConfig.Third.SINA_WEIBO_APP_KEY);
		mSinaShareProxy = new SinaWeiboShareProxy(this, mSinaWeiboApi);
		mSinaWeibo = Weibo.getInstance(GlobalConfig.Third.SINA_WEIBO_APP_KEY,
				GlobalConfig.Third.SINA_WEIBO_REDIRECT_URL,
				GlobalConfig.Third.SINA_WEIBO_SCOPE);
		Tencent tencent = Tencent.createInstance(GlobalConfig.Third.TENCENT_QQ_APP_ID,
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
		case GlobalConfig.IntentKey.INDEX_TENCENT_QZONE:
			// mTencent.
			break;
		case GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO:

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
		try {
			mContent = s.toString();
			String num = (140 - mContent.getBytes("gbk").length / 2) + "";
			mTvLimit.setText("还可输入" + num + "字");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
				if (GlobalConfig.IntentKey.INDEX_TENCENT_QQ == mIndex) {
					mTencentProxy.onLogin( GlobalConfig.Third.TENCENT_QQ_SCOPE,
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
				if (GlobalConfig.IntentKey.INDEX_TENCENT_QQ == mIndex) {
					if (mTencentProxy.isSessionValid()) {
						this.setShareBtnEnable(true);
						return;
					}
					mTencentProxy.onLogin( GlobalConfig.Third.TENCENT_QQ_SCOPE,
							new QQAuthListener(TYPE_QQ, this));
				} else if (GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO == mIndex) {
					// this.startActivityForResult(new Intent(this,
					// TencentAuthActivity.class), TENCENT_WEIBO_REQ_CODE);
				} else if (GlobalConfig.IntentKey.INDEX_SINA_WEIBO == mIndex) {
					if (mSinaShareProxy.isSessionValid(
							mMemberPassport.getAccessToken(),
							mMemberPassport.getExpired() + "")) {
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
				if (mSinaShareProxy.isSessionValid(
						mMemberPassport.getAccessToken(),
						mMemberPassport.getExpired() + "")) {
					this.setShareBtnEnable(true);
					this.onShareSinaWeibo();
				}
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

	private void onShareSinaWeibo() {
		if (null != mSinaShareProxy) {
			mSinaShareProxy.share(mEtContent.getText().toString().trim());
		}
	}

	private void onShareToQQ() {
		final Bundle params = new Bundle();
		params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE,
				Tencent.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(Tencent.SHARE_TO_QQ_TITLE, this.getString(R.string.share));
		params.putString(Tencent.SHARE_TO_QQ_SUMMARY, mContent);
		params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, GlobalConfig.URL_PIC);
		// params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL,
		// "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
		params.putString(Tencent.SHARE_TO_QQ_APP_NAME, this.getString(R.string.app_name));
		// 隐藏空间分享（单独）
		params.putInt(Tencent.SHARE_TO_QQ_EXT_INT,
				Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
//		 mTencentProxy.onShareToQQ();
	}

	private void onShareQZone() {
		final Bundle params = new Bundle();
		params.putString(Tencent.SHARE_TO_QQ_TITLE, "分享标题");
		params.putString(Tencent.SHARE_TO_QQ_SUMMARY, mContent);
		params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, GlobalConfig.URL_PIC);
		// params.putStringArrayList(Tencent.SHARE_TO_QQ_IMAGE_URL,
		// "图片链接ArrayList");
		AppUtils.getExecutors(this).submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// mTencent.shareToQzone(ShareActivity.this, params,
				// ShareActivity.this);
			}
		});

	}

	private void onShareTencentWeibo() {
		Bundle bundle = new Bundle();
		bundle.putString("format", "json");// 返回的数据格式
		bundle.putString("content", "I love Test");
		// mTencent.requestAsync(Constants.GRAPH_ADD_T, bundle,
		// Constants.HTTP_POST, new TencentWeiboListener(), null);
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
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_TENCENT_QQ,
					accessToken, uid, expireTime);
			break;
		case TYPE_TENCENT_WEIBO:
			this.buildMemberPassport(GlobalConfig.Third.PSNAME_TENCENT_WEIBO,
					accessToken, uid, expireTime);
			break;
		case TYPE_SINA_WEIBO:
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
		this.finish();
	}

	// @Override
	// public void onCancel() {
	// // TODO Auto-generated method stub
	// System.out.println(" onCancel ");
	// AppUtils.getHandler(this).post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	//
	// @Override
	// public void onComplete(JSONObject arg0) {
	// // TODO Auto-generated method stub
	// System.out.println(" onComplete " + arg0.toString());
	// AppUtils.getHandler(this).post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	//
	// @Override
	// public void onError(UiError arg0) {
	// // TODO Auto-generated method stub
	// System.out.println(" onError " + arg0.errorMessage + " code = "
	// + arg0.errorCode);
	// AppUtils.getHandler(this).post(new Runnable() {
	//
	// @Override
	// public void run() {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }

}
