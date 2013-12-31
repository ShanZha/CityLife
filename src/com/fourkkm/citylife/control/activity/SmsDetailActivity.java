package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.andrnes.modoer.ModoerPmsgs;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;

/**
 * 短信详情界面
 * 
 * @author ShanZha
 * 
 */
public class SmsDetailActivity extends BaseActivity {

	private static final String TAG = "SmsDetailActivity";
	private TextView mTvTitle, mTvSubject, mTvContent, mTvTime;
	private ModoerPmsgs mSms;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.sms_detail);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvSubject = (TextView) this.findViewById(R.id.sms_detail_tv_subject);
		mTvContent = (TextView) this.findViewById(R.id.sms_detail_tv_content);
		mTvTime = (TextView) this.findViewById(R.id.sms_detail_tv_time);

	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mTvTitle.setText(this.getString(R.string.sms_detail));

		Intent intent = this.getIntent();
		mSms = (ModoerPmsgs) intent.getSerializableExtra("sms");
		if (null == mSms) {
			Log.e(TAG, "shan-->sms is null");
		}
		mTvSubject.setText(mSms.getSubject());
		mTvContent.setText(mSms.getContent());
		mTvTime.setText(CommonUtil.getTimeByPHP(mSms.getPosttime()));
	}

	public void onClickBack(View view) {
		this.finish();
	}

}
