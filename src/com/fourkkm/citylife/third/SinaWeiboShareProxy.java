package com.fourkkm.citylife.third;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler.Response;
import com.sina.weibo.sdk.api.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.util.AccessTokenKeeper;

/**
 * ����΢������Ĵ���
 * 
 * @author ShanZha
 * 
 */
public class SinaWeiboShareProxy {

	private IWeiboAPI mSinaWeiboApi;
	private Activity mCtx;

	public SinaWeiboShareProxy(Activity ctx, IWeiboAPI mWeiboApi) {
		this.mCtx = ctx;
		this.mSinaWeiboApi = mWeiboApi;
	}

	public void setResponseListener(Intent intent, Response response) {
		if (null != mSinaWeiboApi) {
			mSinaWeiboApi.responseListener(intent, response);
		}
	}

	public void registerApp() {
		if (null != mSinaWeiboApi) {
			mSinaWeiboApi.registerApp();
		}
	}

//	public boolean isSessionValid(String accessToken, String expireTime) {
//		Oauth2AccessToken mAccessToken = new Oauth2AccessToken(accessToken,
//				expireTime);
//		return mAccessToken.isSessionValid();
//	}

	public boolean isInstall() {
		if (null == mSinaWeiboApi) {
			return false;
		}
		return mSinaWeiboApi.isWeiboAppInstalled();
	}

	public boolean isAppSupport() {
		if (null == mSinaWeiboApi) {
			return false;
		}
		return mSinaWeiboApi.isWeiboAppSupportAPI();
	}

	/**
	 * ��ʱ�������һ�������ı�
	 * 
	 * @param content
	 */
	public void share(String content) {
		if (null == mSinaWeiboApi) {
			return;
		}
		mSinaWeiboApi.registerApp();
		int supportApi = mSinaWeiboApi.getWeiboAppSupportAPI();
		if (supportApi >= 10351) {// ֧�ֶ�������
			this.sendSinaMultiContent(mSinaWeiboApi, content);
		} else {// ֧�ֵ�������
			this.sendSinaSingleContent(mSinaWeiboApi, content);
		}
	}

	private void sendSinaMultiContent(IWeiboAPI mWeiboApi, String content) {
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.textObject = this.getTextObject(content);
		// weiboMessage.imageObject = this.getImageObj();
		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		// ��transactionΨһ��ʶһ������
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.multiMessage = weiboMessage;
		// ����������Ϣ��΢��������΢���������
		mWeiboApi.sendRequest(mCtx, req);
	}

	private void sendSinaSingleContent(IWeiboAPI mWeiboApi, String content) {
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = this.getTextObject(content);
		SendMessageToWeiboRequest req = new SendMessageToWeiboRequest();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = weiboMessage;
		// ����������Ϣ��΢��������΢���������
		mWeiboApi.sendRequest(mCtx, req);
	}

	private TextObject getTextObject(String content) {
		TextObject text = new TextObject();
		text.text = content;
		return text;
	}
}
