package com.fourkkm.citylife.control.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.util.AccessTokenKeeper;
import com.zj.app.BaseActivity;

public class SinaWeiboShareActivity extends BaseActivity implements
		IWeiboHandler.Response {

	private IWeiboAPI mWeiboApi;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.sina_weibo_share);

	}

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mWeiboApi = WeiboSDK.createWeiboAPI(this,
				GlobalConfig.APP_KEY_SINA_WEIBO);
		mWeiboApi.responseListener(this.getIntent(), this);

		Oauth2AccessToken mAccessToken = AccessTokenKeeper
				.readAccessToken(this);
		if (mAccessToken.isSessionValid()) {
			String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
					.format(new java.util.Date(mAccessToken.getExpiresTime()));
			System.out.println("����Ч����");
			System.out.println(" accessToken = " + mAccessToken.getToken()
					+ " date= " + date);
		} else {
			System.out.println("������Ч����");
			this.showToast("�ȵ�¼��Ȩ");
			this.finish();
		}

		// Weibo weibo = Weibo.getInstance(GlobalConfig.APP_KEY_SINA_WEIBO,
		// GlobalConfig.REDIRECT_URL_SINA, GlobalConfig.SCOPE_SINA);
		// weibo.anthorize(this, new WeiboAuthListener() {
		//
		// @Override
		// public void onWeiboException(WeiboException arg0) {
		// // TODO Auto-generated method stub
		// System.out.println(" onWeiboException :" + arg0.getMessage());
		// }
		//
		// @Override
		// public void onError(WeiboDialogError arg0) {
		// // TODO Auto-generated method stub
		// System.out.println(" onErro:" + arg0.getMessage());
		// }
		//
		// @Override
		// public void onComplete(Bundle arg0) {
		// // TODO Auto-generated method stub
		// System.out.println(" onComplete:" + arg0.toString());
		// }
		//
		// @Override
		// public void onCancel() {
		// // TODO Auto-generated method stub
		// System.out.println(" onCancel");
		// }
		// });

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mWeiboApi.responseListener(intent, this);
	}

	public void onClickReqApp(View view) {
		mWeiboApi.registerApp();
	}

	public void onClickShare(View view) {
		if (!mWeiboApi.isWeiboAppInstalled()) {
			this.showToast("����û��װ����΢���ͻ��ˣ���ʱ���ܷ���");
			return;
		}
		if (!mWeiboApi.isWeiboAppSupportAPI()) {
			this.showToast("��������΢���ͻ��˲�֧�ַ����ܣ�����������");
			return;
		}
		mWeiboApi.registerApp();
		int supportApi = mWeiboApi.getWeiboAppSupportAPI();
		if (supportApi >= 10351) {// ֧�ֶ�������
			this.showToast("֧��һ�ζ���");
			this.sendShareMultiContent();
		} else {// ֧�ֵ�������
			this.showToast("֧��һ��һ��");
		}
	}

	@Override
	public void onResponse(BaseResponse baseResp) {
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
			this.showToast("����ɹ�");
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
			this.showToast("�û�ȡ��");
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
			this.showToast("����ʧ�ܣ�" + baseResp.errMsg);
			break;
		}
	}

	private void sendShareMultiContent() {
		// Step 1: ��ʼ��΢���ķ�����Ϣ
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.textObject = this.getTextObject();
		weiboMessage.imageObject = this.getImageObj();
		// Step 2: ��ʼ���ӵ�������΢������Ϣ����
		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		// ��transactionΨһ��ʶһ������
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.multiMessage = weiboMessage;
		// Step 3:����������Ϣ��΢��������΢���������
		mWeiboApi.sendRequest(this, req);
	}

	private TextObject getTextObject() {
		TextObject text = new TextObject();
		text.text = "ɽ����App����";
		return text;
	}

	/**
	 * ͼƬ��Ϣ���췽����
	 * 
	 * @return ͼƬ��Ϣ����
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.ic_launcher);
		// BitmapDrawable bitmapDrawable = (BitmapDrawable)
		// mImage.getDrawable();
		// imageObject.setImageObject(bitmapDrawable.getBitmap());
		imageObject.setImageObject(bitmap);
		return imageObject;
	}
}
