package com.fourkkm.citylife.control.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * 会员中心界面
 * 
 * @author ShanZha
 * 
 */
public class UserCenterActivity extends BaseActivity {

	private ImageView mIvAvator;
	private TextView mTvTitle, mTvUsernmae, mTvNickname, mTvDesc;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();

		this.setContentView(R.layout.user_center);
		mIvAvator = (ImageView) this.findViewById(R.id.user_center_iv_avator);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvUsernmae = (TextView) this
				.findViewById(R.id.user_center_tv_username);
		mTvNickname = (TextView) this
				.findViewById(R.id.user_center_tv_nickname);
		mTvDesc = (TextView) this.findViewById(R.id.user_center_tv_desc);

		mTvTitle.setText(this.getString(R.string.member_center));
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickAvator(View view) {// 头像

	}

	public void onClickMyParty(View view) {// 我的聚会活动

	}

	public void onClickMyLane(View view) {// 我的唐人巷

	}

	public void onClickMyReview(View view) {// 我的点评

	}

	public void onClickMyShop(View view) {// 我的店铺

	}

	public void onClickMyCollection(View view) {// 我的收藏

	}

	public void onClickMyQuestion(View view) {// 我的问题

	}

	public void onClickMyAnswer(View view) {// 我的回答

	}

	public void onClickMySms(View view) {// 我的短信

	}

	public void onClickUpdateInfo(View view) {// 修改个人信息

	}

}
