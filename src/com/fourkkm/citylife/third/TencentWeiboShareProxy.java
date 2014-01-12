package com.fourkkm.citylife.third;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.third.IThirdShareListener;

/**
 * 腾讯微博分享
 * 
 * @author ShanZha
 * 
 */
public class TencentWeiboShareProxy {

	private static final String TAG = "TencentWeiboApi";
	private static TencentWeiboShareProxy mTencenApi = new TencentWeiboShareProxy();

	private TencentWeiboShareProxy() {
	}

	public static TencentWeiboShareProxy getTencentApi() {
		return mTencenApi;
	}

	public void addWeibo(IThirdShareListener listener, String accessToken,
			String openId, String content, String clientIp) {
		StringBuilder sbUrl = new StringBuilder();
		sbUrl.append(GlobalConfig.Third.TENCENT_URL_API_ADD);
		sbUrl.append("?format=json");// 必填，json或xml
		sbUrl.append("&content=" + content);
		sbUrl.append("&compatibleflag=0");
		sbUrl.append(this.getPublicParam(accessToken, openId, clientIp));
		sbUrl.append("&");
		String url = sbUrl.toString();
		TencentWeiboTask task = new TencentWeiboTask(listener,
				GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO);
		task.execute(url);

	}

	private String getPublicParam(String accessToken, String openId,
			String clientIp) {
		StringBuilder sb = new StringBuilder();
		sb.append("&oauth_consumer_key="
				+ GlobalConfig.Third.TENCENT_WEIBO_APP_KEY);
		sb.append("&access_token=" + accessToken);
		sb.append("&openid=" + openId);
		// 用户ip（必须正确填写用户侧真实ip，不能为内网ip及以127或255开头的ip，以分析用户所在地）
		sb.append("&clientip=" + clientIp);
		sb.append("&oauth_version=2.a");// 不能更改
		sb.append("&scope=all");
		return sb.toString();
	}

	private String doPost(String url) {
		HttpsURLConnection con = null;
		try {
			_FakeX509TrustManager.allowAllSSL();
			URL postUrl = new URL(url);
			con = (HttpsURLConnection) postUrl.openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			con.setConnectTimeout(30 * 1000);
			con.setReadTimeout(30 * 1000);
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			con.connect();
			int resposeCode = con.getResponseCode();
			Log.i(TAG, "shan-->Tencent weibo responseCode = " + resposeCode);
			if (200 == resposeCode) {
				String result = null;
				result = getResultByStream(con.getInputStream());
				return result;
			} else {
				return resposeCode + "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		} finally {
			if (null != con) {
				con.disconnect();
				con = null;
			}
		}
	}

	private String getResultByStream(InputStream is) {
		byte[] buffer = new byte[4 * 1024];
		int len = -1;
		try {
			StringBuilder sb = new StringBuilder();
			while ((len = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, len));
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (null != is) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
			}

		}
		return "";
	}

	private class TencentWeiboTask extends AsyncTask<String, Integer, String> {

		private IThirdShareListener mListener;
		private int mShareType = -1;

		public TencentWeiboTask(IThirdShareListener listener, int shareType) {
			this.mListener = listener;
			this.mShareType = shareType;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String url = params[0];
			String result = doPost(url);
			Log.i(TAG, "shan-->Tencent weibo share result: " + result);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			try {
				if (TextUtils.isEmpty(result)) {
					if (null != mListener) {
						mListener.onShareFail(mShareType, "result is null");
					}
				}
				JSONObject json = new JSONObject(result);
				Log.i(TAG, "shan-->Tencent weibo share: " + json.toString());
				String errorCode = json.getString("errcode");
				String msg = json.getString("msg");
				if (errorCode != null && "0".equals(errorCode)) {
					if (null != mListener) {
						mListener.onShareSuccess(mShareType, null);
					}
				} else {
					if (null != mListener) {
						mListener.onShareFail(mShareType, errorCode);
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (null != mListener) {
					mListener.onShareFail(mShareType, e.getMessage());
				}
			}

		}

	}

}
