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
 * 登录界面
 * 
 * @author ShanZha
 * 
 */
public class LoginActivity extends BaseActivity {

	// private Weibo mWeiboSina;

	private LoginButton mBtnSinaWeibo;

	/** 登陆认证对应的listener */
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
	// + "access_token 仍在有效期内,无需再次登录: \naccess_token:"
	// + mAccessToken.getToken() + "\n有效期：" + date);
	// } else {
	// System.out.println("shan-->" + "使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，"
	// + "目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
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
	 * 登入按钮的监听器，接收授权结果。
	 */
	private class AuthListener implements WeiboAuthListener {

		@Override
		public void onComplete(Bundle values) {// sdk自动保存到SharePrefrence
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
	// * 微博认证授权回调类。 1. SSO登陆时，需要在{@link #onActivityResult}
	// * 中调用mSsoHandler.authorizeCallBack后， 该回调才会被执行。 2. 非SSO登陆时，当授权后，就会被执行。
	// * 当授权成功后，请保存该access_token、expires_in等信息到SharedPreferences中。
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
	// System.out.println("shan-->" + "认证成功: \r\n access_token: "
	// + token + "\r\n" + "expires_in: " + expires_in
	// + "\r\n有效期：" + date);
	// // mText.setText("认证成功: \r\n access_token: " + token + "\r\n" +
	// // "expires_in: "
	// // + expires_in + "\r\n有效期：" + date);
	//
	// AccessTokenKeeper.keepAccessToken(LoginActivity.this,
	// mAccessToken);
	// Toast.makeText(getApplicationContext(), "认证成功",
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
