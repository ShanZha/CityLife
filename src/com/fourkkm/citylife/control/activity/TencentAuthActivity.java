package com.fourkkm.citylife.control.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;

/**
 * 腾讯微博鉴权页面(其他鉴权无需自己写界面)
 * 
 * @author ShanZha
 * 
 */
public class TencentAuthActivity extends Activity {

	private String mUrl;
	private WebView mWebView;
	private ProgressBar mProBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.tencent_auth);
		mWebView = (WebView) this.findViewById(R.id.tencent_auth_webview);
		mProBar = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);

		this.showLoadingWebview();
		int state = (int) Math.random() * 1000 + 111;
		mUrl = "https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id="
				+ GlobalConfig.Third.TENCENT_WEIBO_APP_KEY
				+ "&response_type=token&redirect_uri="
				+ GlobalConfig.Third.TENCENT_WEIBO_REDIRECT_URL + "&state="
				+ state;

		WebSettings webSettings = mWebView.getSettings();
		mWebView.setVerticalScrollBarEnabled(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(false);
		mWebView.loadUrl(mUrl);
		mWebView.setWebViewClient(new CustomWebViewClient());

	}

	private void showLoadingWebview() {
		mProBar.setVisibility(View.VISIBLE);
		mWebView.setVisibility(View.GONE);
	}

	private void hideLoadingWebview() {
		mProBar.setVisibility(View.GONE);
		mWebView.setVisibility(View.VISIBLE);
	}

	private class CustomWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			view.requestFocus();
			super.onPageFinished(view, url);
			if (url.indexOf("access_token") != -1) {
				jumpResultParser(url);
			}
			hideLoadingWebview();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			if (url.indexOf("access_token") != -1) {
				jumpResultParser(url);
			}
			return false;
		}
	}

	/**
	 * 
	 * 获取授权后的返回地址，并对其进行解析
	 */
	public void jumpResultParser(String result) {

		String resultParam = result.split("#")[1];
		String params[] = resultParam.split("&");
		String accessToken = params[0].split("=")[1];
		String expiresIn = params[1].split("=")[1];
		String openid = params[2].split("=")[1];
		String openkey = params[3].split("=")[1];
		String refreshToken = params[4].split("=")[1];
		String state = params[5].split("=")[1];
		String name = params[6].split("=")[1];
		String nick = params[7].split("=")[1];
		System.out.println(" name = " + name + " nick = " + nick
				+ " openKey = " + openkey + " openId = " + openid
				+ " expiresIn= " + expiresIn);
		Context context = this.getApplicationContext();
		if (accessToken != null && !"".equals(accessToken)) {
			// Util.saveSharePersistent(context, "ACCESS_TOKEN", accessToken);
			// Util.saveSharePersistent(context, "EXPIRES_IN",
			// Long.valueOf(expiresIn));//
			// accesstoken过期时间，以返回的时间的准，单位为秒，注意过期时提醒用户重新授权
			// Util.saveSharePersistent(context, "OPEN_ID", openid);
			// Util.saveSharePersistent(context, "OPEN_KEY", openkey);
			// Util.saveSharePersistent(context, "REFRESH_TOKEN", refreshToken);
			// Util.saveSharePersistent(context, "NAME", name);
			// Util.saveSharePersistent(context, "NICK", nick);
			// Util.saveSharePersistent(context, "CLIENT_ID",
			// GlobalConfig.TENCENT_APP_KEY);
			// Util.saveSharePersistent(context, "AUTHORIZETIME",
			// String.valueOf(System.currentTimeMillis() / 1000l));
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString(GlobalConfig.Third.KEY_ACCESS_TOKEN, accessToken);
			bundle.putString(GlobalConfig.Third.KEY_UID, openid);
			bundle.putLong(GlobalConfig.Third.KEY_EXPIRE_TIME,
					Long.parseLong(expiresIn));
			bundle.putString(GlobalConfig.Third.KEY_NICK_NAME, nick);
			intent.putExtra("values", bundle);
			this.setResult(RESULT_OK, intent);
			this.finish();
			// isShow = true;
		}
	}
}
