package com.fourkkm.citylife.control.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.tencent.open.HttpStatusException;
import com.tencent.open.NetworkUnavailableException;
import com.tencent.tauth.Constants;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.zj.app.BaseActivity;
import com.zj.app.utils.AppUtils;

/**
 * 第三方分享界面(新浪自带，主要处理QQ、腾讯微博)
 * 
 * @author ShanZha
 * 
 */
public class ShareActivity extends BaseActivity implements TextWatcher,
		IUiListener {

	private TextView mTvTitle, mTvLimit;
	private EditText mEtContent;

	private String mContent;
	private ProgressDialogProxy mDialogProxy;
	private int mIndex = -1;
	private Tencent mTencent;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.share);
		mTvTitle = (TextView) this.findViewById(R.id.share_tv_title);
		mTvLimit = (TextView) this.findViewById(R.id.share_tv_limit);
		mEtContent = (EditText) this.findViewById(R.id.share_et_content);

		mEtContent.addTextChangedListener(this);
		mEtContent.setFocusable(true);
		mEtContent.requestFocus();
		mEtContent.setSelection(mContent.length());
		mEtContent.setScrollbarFadingEnabled(true);
		mEtContent.setGravity(Gravity.TOP);
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

		mTencent = Tencent.createInstance(GlobalConfig.Third.TENCENT_QQ_APP_ID,
				this.getApplicationContext());

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

	private void onShareToQQ() {
		final Bundle params = new Bundle();
		params.putInt(Tencent.SHARE_TO_QQ_KEY_TYPE,
				Tencent.SHARE_TO_QQ_TYPE_DEFAULT);
		params.putString(Tencent.SHARE_TO_QQ_TITLE, "分享标题");
		params.putString(Tencent.SHARE_TO_QQ_SUMMARY, mContent);
		params.putString(Tencent.SHARE_TO_QQ_TARGET_URL, GlobalConfig.URL_PIC);
		// params.putString(Tencent.SHARE_TO_QQ_IMAGE_URL,
		// "http://imgcache.qq.com/qzone/space_item/pre/0/66768.gif");
		params.putString(Tencent.SHARE_TO_QQ_APP_NAME, "测试应用222222");
		// 隐藏空间分享（单独）
		params.putInt(Tencent.SHARE_TO_QQ_EXT_INT,
				Tencent.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
		AppUtils.getExecutors(this).submit(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				mTencent.shareToQQ(ShareActivity.this, params,
						ShareActivity.this);

			}
		});
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
				mTencent.shareToQzone(ShareActivity.this, params,
						ShareActivity.this);
			}
		});

	}
	
	private void onShareTencentWeibo(){
		Bundle bundle = new Bundle();
		bundle.putString("format", "json");// 返回的数据格式
		bundle.putString("content", "I love Test");
		mTencent.requestAsync(Constants.GRAPH_ADD_T, bundle,
				Constants.HTTP_POST, new TencentWeiboListener(), null);
	}

	@Override
	public void onCancel() {
		// TODO Auto-generated method stub
		System.out.println(" onCancel ");
		AppUtils.getHandler(this).post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onComplete(JSONObject arg0) {
		// TODO Auto-generated method stub
		System.out.println(" onComplete " + arg0.toString());
		AppUtils.getHandler(this).post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onError(UiError arg0) {
		// TODO Auto-generated method stub
		System.out.println(" onError " + arg0.errorMessage + " code = "
				+ arg0.errorCode);
		AppUtils.getHandler(this).post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private class TencentWeiboListener implements  IRequestListener{

		@Override
		public void onComplete(JSONObject arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onConnectTimeoutException(ConnectTimeoutException arg0,
				Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onIOException(IOException arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onJSONException(JSONException arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMalformedURLException(MalformedURLException arg0,
				Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNetworkUnavailableException(
				NetworkUnavailableException arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSocketTimeoutException(SocketTimeoutException arg0,
				Object arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onUnknowException(Exception arg0, Object arg1) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
