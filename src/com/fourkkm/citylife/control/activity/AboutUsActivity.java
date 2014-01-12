package com.fourkkm.citylife.control.activity;

import android.view.View;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * 关于我们
 * 
 * @author ShanZha
 * 
 */
public class AboutUsActivity extends BaseActivity {

	private TextView mTvTitle;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.more_about_us);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvTitle.setText(this.getString(R.string.more_about));
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
