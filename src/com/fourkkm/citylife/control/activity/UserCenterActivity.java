package com.fourkkm.citylife.control.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * ��Ա���Ľ���
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

	public void onClickAvator(View view) {// ͷ��

	}

	public void onClickMyParty(View view) {// �ҵľۻ�

	}

	public void onClickMyLane(View view) {// �ҵ�������

	}

	public void onClickMyReview(View view) {// �ҵĵ���

	}

	public void onClickMyShop(View view) {// �ҵĵ���

	}

	public void onClickMyCollection(View view) {// �ҵ��ղ�

	}

	public void onClickMyQuestion(View view) {// �ҵ�����

	}

	public void onClickMyAnswer(View view) {// �ҵĻش�

	}

	public void onClickMySms(View view) {// �ҵĶ���

	}

	public void onClickUpdateInfo(View view) {// �޸ĸ�����Ϣ

	}

}
