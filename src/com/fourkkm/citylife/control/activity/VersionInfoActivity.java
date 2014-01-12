package com.fourkkm.citylife.control.activity;

import android.view.View;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * °æ±¾ËµÃ÷
 * 
 * @author ShanZha
 * 
 */
public class VersionInfoActivity extends BaseActivity {

	private TextView mTvTitle;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.more_version_info);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvTitle.setText(this.getString(R.string.more_version));
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
	}

	public void onClickBack(View view) {
		this.finish();
	}
}
