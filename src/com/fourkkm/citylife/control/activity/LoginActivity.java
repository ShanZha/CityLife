package com.fourkkm.citylife.control.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.taobao.top.android.TopAndroidClient;
import com.taobao.top.android.auth.AccessToken;
import com.taobao.top.android.auth.AuthActivity;
import com.taobao.top.android.auth.AuthError;
import com.taobao.top.android.auth.AuthException;
import com.taobao.top.android.auth.AuthorizeListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.util.AccessTokenKeeper;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.http.StoreOperation;
import com.zj.app.utils.AppUtils;
import com.zj.app.utils.DateFormatMethod;
import com.zj.app.utils.EncryptionUtil;
import com.zj.support.observer.Observer;
import com.zj.support.observer.inter.ICallback;
import com.zj.support.observer.model.Param;

/**
 * µÇÂ¼½çÃæ
 * 
 * @author ShanZha
 * 
 */
public class LoginActivity extends AuthActivity implements ICallback {

	private static final int REGISTER_REQ_CODE = 1;
	private static final int TENCENT_WEIBO_REQ_CODE = 2;
	private TextView mTvTitle;
	private EditText mEtUsername, mEtPswd;
	private CheckBox mCbPswd, mCbAutoLogin;

	private ProgressDialogProxy mDialogProxy;

	private Weibo mSinaWeibo;
	private Tencent mTencent;
	private TopAndroidClient mTaobao = TopAndroidClient
			.getAndroidClientByAppKey(GlobalConfig.TAOBAO_APP_KEY);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Observer.getObserver().addCallback(this.hashCode(), this);
		AppUtils.getBaseApp(this).addActivity(this);
		this.prepareViews();
		this.prepareDatas();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Observer.getObserver().deleteCallback(this.hashCode(), this);
		AppUtils.getBaseApp(this).popActivity(this);
	}

	protected void prepareViews() {
		this.setContentView(R.layout.login);

		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtUsername = (EditText) this.findViewById(R.id.login_et_username);
		mEtPswd = (EditText) this.findViewById(R.id.login_et_pswd);
		mCbPswd = (CheckBox) this.findViewById(R.id.login_check_box_pswd);
		mCbAutoLogin = (CheckBox) this
				.findViewById(R.id.login_check_box_auto_login);
	}

	protected void prepareDatas() {
		mTvTitle.setText(this.getString(R.string.login_user));
		mDialogProxy = new ProgressDialogProxy(this);
		mDialogProxy.setMessage(this.getText(R.string.login_ing));

		boolean isRemeberPswd = SharedPreferenceUtil.getSharedPrefercence()
				.getBoolean(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_REMBER_PSWD);
		boolean isAutoLogin = SharedPreferenceUtil.getSharedPrefercence()
				.getBoolean(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_AUTO_LOGIN);
		mCbPswd.setChecked(isRemeberPswd);
		mCbAutoLogin.setChecked(isAutoLogin);
		String username = SharedPreferenceUtil.getSharedPrefercence()
				.getString(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_USERNAME);
		mEtUsername.setText(username);
		if (isRemeberPswd) {
			String pswd = SharedPreferenceUtil.getSharedPrefercence()
					.getString(this.getApplicationContext(),
							GlobalConfig.SharePre.KEY_PSWD);
			mEtPswd.setText(pswd);
		}

		mSinaWeibo = Weibo.getInstance(GlobalConfig.SINA_WEIBO_APP_KEY,
				GlobalConfig.SINA_WEIBO_REDIRECT_URL,
				GlobalConfig.SINA_WEIBO_SCOPE);
		mTencent = Tencent.createInstance(GlobalConfig.TENCENT_QQ_APP_ID,
				this.getApplicationContext());
	}

	private void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	/**
	 * ÃÜÂëÊÇ·ñÕýÈ·
	 * 
	 * @param inputPswd
	 * @param dbPswd
	 * @return
	 */
	private boolean isPswdCorrect(String inputPswd, String dbPswd) {
		String encrytPswd = EncryptionUtil.encryptionString(inputPswd);
		return encrytPswd.equals(dbPswd);
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	public void onClickRegister(View view) {// ×¢²á
		this.startActivityForResult(new Intent(this, RegisterActivity.class),
				REGISTER_REQ_CODE);
	}

	public void onClickLogin(View view) {// µÇÂ¼
		String username = mEtUsername.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			this.showToast(this.getString(R.string.register_username_not_null));
			return;
		}
		if (TextUtils.isEmpty(pswd)) {
			this.showToast(this.getString(R.string.login_pswd_null));
			return;
		}
		mDialogProxy.showDialog();
		String selectCode = "from com.andrnes.modoer.ModoerMembers mm where mm.username = :username";
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("username", username);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		StoreOperation.getInstance(this).find(selectCode, paramsMap, true,
				new ModoerMembers(), param);
	}

	public void onClickQQ(View view) {// qqµÇÂ¼
		mTencent.login(this, GlobalConfig.TENCENT_QQ_SCOPE,
				new TencentUIListener());
	}

	public void onClickTencentWeibo(View view) {// ÌÚÑ¶Î¢²©µÇÂ¼
		this.startActivityForResult(
				new Intent(this, TencentAuthActivity.class),
				TENCENT_WEIBO_REQ_CODE);
	}

	public void onClickSinaWeibo(View view) {// ÐÂÀËÎ¢²©µÇÂ¼
		mSinaWeibo.anthorize(this, new SinaAuthListener());
	}

	public void onClickTaoBao(View view) {// ÌÔ±¦µÇÂ¼
		mTaobao.authorize(this);
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}

		if (REGISTER_REQ_CODE == requestCode) {// ×¢²á³É¹¦
			this.setResult(RESULT_OK);
			this.finish();
		} else if (TENCENT_WEIBO_REQ_CODE == requestCode) {// ÌÚÑ¶Î¢²©µÇÂ¼³É¹¦
			// Do nothing
		}
	}

	/**
	 * ÐÂÀË¼øÈ¨¼àÌý
	 */
	private class SinaAuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {// sdk×Ô¶¯±£´æµ½SharePrefrence
			String uid = values.getString("uid");
			String accessToken = values.getString("access_token");
			String expiresIn = values.getString("expires_in");
			System.out.println("onComplete() accessToken= " + accessToken
					+ " expiresIn = " + expiresIn + " uid = " + uid);
			long expires = Long.valueOf(expiresIn);
			System.out.println(" expireTime = "
					+ DateFormatMethod.formatDate(new Date(expires)));
			showToast("ÐÂÀËÎ¢²©µÇÂ¼³É¹¦");
			// ±£´æAccessToken
			Oauth2AccessToken token = new Oauth2AccessToken(accessToken,
					expiresIn);
			AccessTokenKeeper.keepAccessToken(LoginActivity.this, token);
		}

		@Override
		public void onWeiboException(WeiboException e) {
			if (e != null) {
				System.out.println("shan-->onWeiboException");
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			showToast("ÐÂÀËÎ¢²©µÇÂ¼³ö´í");
			System.out.println(" onError " + e.getMessage());
		}

		@Override
		public void onCancel() {
			showToast("ÐÂÀËÎ¢²©µÇÂ¼È¡Ïû");
			System.out.println(" onCancel");
		}
	}

	/**
	 * ÌÚÑ¶QQµÇÂ¼¼àÌý
	 * 
	 * @author ShanZha
	 * 
	 */
	private class TencentUIListener implements IUiListener {

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub
			System.out.println(" Tencent--oncancel()");
			showToast("ÌÚÑ¶µÇÂ¼È¡Ïû");
		}

		@Override
		public void onComplete(JSONObject arg0) {
			// TODO Auto-generated method stub
			showToast("ÌÚÑ¶µÇÂ¼³É¹¦");
			System.out.println("onComplete(): " + arg0.toString());
			try {
				String accessToken = arg0.getString("access_token");
				String openid = arg0.getString("openid");
				String expires_in = arg0.getString("expires_in");
				// Util.saveSharePersistent(getApplicationContext(),
				// "TENCENT_ACCESS_TOKEN", accessToken);
				// Util.saveSharePersistent(getApplicationContext(),
				// "TENCENT_OPEN_ID", openid);
				// Util.saveSharePersistent(getApplicationContext(),
				// "TENCENT_EXPIRES_IN", expires_in);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub
			showToast("ÌÚÑ¶µÇÂ¼Ê§°Ü:" + arg0.errorMessage);
			System.out.println(" Tencent--onerror()-->" + arg0.toString());
		}

	}

	/**
	 * ÌÔ±¦¼øÈ¨¼àÌý
	 * 
	 * @author ShanZha
	 * 
	 */
	private class TaobaoAuthListener implements AuthorizeListener {

		@Override
		public void onComplete(AccessToken accessToken) {
			// TODO Auto-generated method stub
			showToast("ÌÔ±¦ÊÚÈ¨³É¹¦");
			System.out.println("Taobao-onCompelete: " + accessToken.toString());
			String id = accessToken.getAdditionalInformation().get(
					AccessToken.KEY_SUB_TAOBAO_USER_ID);
			if (id == null) {
				id = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_TAOBAO_USER_ID);
			}
			long userId = Long.parseLong(id);
			String nick = accessToken.getAdditionalInformation().get(
					AccessToken.KEY_SUB_TAOBAO_USER_NICK);
			if (nick == null) {
				nick = accessToken.getAdditionalInformation().get(
						AccessToken.KEY_TAOBAO_USER_NICK);
			}
			String r2_expires = accessToken.getAdditionalInformation().get(
					AccessToken.KEY_R2_EXPIRES_IN);
			Date start = accessToken.getStartDate();
			Date end = new Date(start.getTime() + Long.parseLong(r2_expires)
					* 1000L);
			System.out.println(" onCompelete() nick = " + nick
					+ " r2_expires = " + r2_expires + " id " + id
					+ " userId = " + userId);
		}

		@Override
		public void onError(AuthError e) {
			// TODO Auto-generated method stub
			Log.e("LoginActivity", "TaoBao-onError" + e.getErrorDescription());
			showToast("ÌÔ±¦ÊÚÈ¨³ö´í");
		}

		@Override
		public void onAuthException(AuthException e) {
			// TODO Auto-generated method stub
			Log.e("LoginActivity", "TaoBao-onAuthException: " + e.getMessage());
			showToast("ÌÔ±¦ÊÚÈ¨Ê§°Ü");
		}

	}

	@Override
	protected TopAndroidClient getTopAndroidClient() {
		// TODO Auto-generated method stub
		return mTaobao;
	}

	@Override
	protected AuthorizeListener getAuthorizeListener() {
		// TODO Auto-generated method stub
		return new TaobaoAuthListener();
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessSaveOrUpdateArray(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessDelete(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessShowDomain(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessUploadFile(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessExecuteQuery(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub
		Object result = out.getResult();
		if (null == result) {
			mDialogProxy.hideDialog();
			this.showToast(this.getString(R.string.login_info_incorrect));
			return;
		}
		ModoerMembers member = (ModoerMembers) result;
		if (this.isPswdCorrect(mEtPswd.getText().toString().trim(),
				member.getPassword())) {
			((CoreApp) AppUtils.getBaseApp(this)).setCurrMember(member);
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_USERNAME, member.getUsername());
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_PSWD,
					mEtPswd.getText().toString().trim());
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_IS_REMBER_PSWD,
					mCbPswd.isChecked());
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_IS_AUTO_LOGIN,
					mCbAutoLogin.isChecked());
			this.setResult(RESULT_OK);
			this.finish();
		} else {
			this.showToast(this.getString(R.string.login_info_incorrect));
		}
		mDialogProxy.hideDialog();

	}

	@Override
	public void onFails(Param out) {
		mDialogProxy.hideDialog();
		this.showToast(this.getString(R.string.login_fail));
	}

}
