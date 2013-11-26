package com.fourkkm.citylife.control.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.AccessTokenKeeper;
import com.weibo.sdk.view.LoginButton;
import com.zj.app.BaseActivity;

/**
 * ��¼����
 * 
 * @author ShanZha
 * 
 */
public class LoginActivity extends BaseActivity {

	// private Weibo mWeiboSina;

	private LoginButton mBtnSinaWeibo;

	/** ��½��֤��Ӧ��listener */
	private AuthListener mLoginListener = new AuthListener();

	// private Oauth2AccessToken mAccessToken;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.login);
		mBtnSinaWeibo = (LoginButton) this
				.findViewById(R.id.login_btn_sina_weibo);

		// mWeiboSina = Weibo.getInstance(GlobalConfig.APP_KEY_SINA_WEIBO,
		// GlobalConfig.REDIRECT_URL_SINA, GlobalConfig.SCOPE_SINA);
		// RequestToken requestToken = mWeiboSina.getRequestToken(this,
		// GlobalConfig.APP_KEY_SINA_WEIBO, GlobalConfig.APP_SECRET_SINA_WEIBO,
		// AuthorizeActivity.URL_ACTIVITY_CALLBACK);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mBtnSinaWeibo.setCurrentActivity(this);
		mBtnSinaWeibo.setAuthListener(mLoginListener);
	}

	// public void onClickSinaWeibo(View view) {
	// mAccessToken = AccessTokenKeeper.readAccessToken(this);
	// if (mAccessToken.isSessionValid()) {
	// String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
	// .format(new java.util.Date(mAccessToken.getExpiresTime()));
	// System.out.println("shan-->"
	// + "access_token ������Ч����,�����ٴε�¼: \naccess_token:"
	// + mAccessToken.getToken() + "\n��Ч�ڣ�" + date);
	// } else {
	// System.out.println("shan-->" + "ʹ��SSO��¼ǰ�������ֻ����Ƿ��Ѿ���װ����΢���ͻ��ˣ�"
	// + "Ŀǰ��3.0.0������΢���ͻ��˰汾֧��SSO�����δ��װ�����Զ�תΪOauth2.0������֤");
	// }
	// mWeiboSina.anthorize(this, new AuthDialogListener());
	// }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode) {
			System.out.println(" onActivityResult = " + data.toString());
			SsoHandler ssoHandler = mBtnSinaWeibo.getSsoHandler();
			if (null != ssoHandler) {
				ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			}
		}
	}

	/**
	 * ���밴ť�ļ�������������Ȩ�����
	 */
	private class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {// sdk�Զ����浽SharePrefrence
			String code = values.getString("code");
			String accessToken = values.getString("access_token");
			String expiresIn = values.getString("expires_in");
			System.out.println("onComplete() accessToken= " + accessToken
					+ " expiresIn = " + expiresIn);
			if (TextUtils.isEmpty(accessToken) || TextUtils.isEmpty(expiresIn)) {
				getAccessTokenByCode(code);
			} else {
				saveLoginInfo(accessToken, expiresIn);
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			if (e != null) {
				System.out.println("shan-->onWeiboException");
				// Toast.makeText(DemoLoginButton.this, e.getMessage(),
				// Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onError(WeiboDialogError e) {
			System.out.println(" onError " + e.getMessage());
		}

		@Override
		public void onCancel() {
			System.out.println(" onCancel");
		}
	}

	private void getAccessTokenByCode(final String code) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(
							"https://api.weibo.com/oauth2/access_token");
					URLConnection connection = url.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					OutputStreamWriter out = new OutputStreamWriter(
							connection.getOutputStream(), "utf-8");
					out.write("client_id=" + GlobalConfig.APP_KEY_SINA_WEIBO
							+ "&client_secret="
							+ GlobalConfig.APP_SECRET_SINA_WEIBO
							+ "&grant_type=authorization_code" + "&code="
							+ code + "&redirect_uri="
							+ GlobalConfig.REDIRECT_URL_SINA);
					out.flush();
					out.close();
					String sCurrentLine;
					String sTotalString;
					sCurrentLine = "";
					sTotalString = "";
					InputStream l_urlStream;
					l_urlStream = connection.getInputStream();
					BufferedReader l_reader = new BufferedReader(
							new InputStreamReader(l_urlStream));
					while ((sCurrentLine = l_reader.readLine()) != null) {
						sTotalString += sCurrentLine;
					}

					JSONObject o = new JSONObject(sTotalString);
					System.out.println(" " + sTotalString);
					System.out.println("access Token ="
							+ o.getString("access_token") + " expires_in = "
							+ o.getString("expires_in"));
					String accessToken = o.getString("access_token");
					String expiresIn = o.getString("expires_in");
					saveLoginInfo(accessToken, expiresIn);
					// AccessTokenKeeper.keepAccessToken(MainActivity.this,
					// mAccessToken);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	private void saveLoginInfo(String accessToken, String expiresIn) {
		Oauth2AccessToken token = new Oauth2AccessToken(accessToken, expiresIn);
		AccessTokenKeeper.keepAccessToken(LoginActivity.this, token);
	}

	public void onClickSinaWeiboShare(View view) {
		Intent intent = new Intent(this, SinaWeiboShareActivity.class);
		this.startActivity(intent);
	}

	public void onClickBtnOauth(View view) {

	}
	// /**
	// * ΢����֤��Ȩ�ص��ࡣ 1. SSO��½ʱ����Ҫ��{@link #onActivityResult}
	// * �е���mSsoHandler.authorizeCallBack�� �ûص��Żᱻִ�С� 2. ��SSO��½ʱ������Ȩ�󣬾ͻᱻִ�С�
	// * ����Ȩ�ɹ����뱣���access_token��expires_in����Ϣ��SharedPreferences�С�
	// */
	// class AuthDialogListener implements WeiboAuthListener {
	//
	// @Override
	// public void onComplete(Bundle values) {
	//
	// String token = values.getString("access_token");
	// String expires_in = values.getString("expires_in");
	// mAccessToken = new Oauth2AccessToken(token, expires_in);
	// if (mAccessToken.isSessionValid()) {
	// String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
	// .format(new java.util.Date(mAccessToken
	// .getExpiresTime()));
	// System.out.println("shan-->" + "��֤�ɹ�: \r\n access_token: "
	// + token + "\r\n" + "expires_in: " + expires_in
	// + "\r\n��Ч�ڣ�" + date);
	// // mText.setText("��֤�ɹ�: \r\n access_token: " + token + "\r\n" +
	// // "expires_in: "
	// // + expires_in + "\r\n��Ч�ڣ�" + date);
	//
	// AccessTokenKeeper.keepAccessToken(LoginActivity.this,
	// mAccessToken);
	// Toast.makeText(getApplicationContext(), "��֤�ɹ�",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	//
	// @Override
	// public void onError(WeiboDialogError e) {
	// Toast.makeText(getApplicationContext(),
	// "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
	// }
	//
	// @Override
	// public void onCancel() {
	// Toast.makeText(getApplicationContext(), "Auth cancel",
	// Toast.LENGTH_LONG).show();
	// }
	//
	// @Override
	// public void onWeiboException(WeiboException e) {
	// Toast.makeText(getApplicationContext(),
	// "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG)
	// .show();
	// }
	// }

}
