package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * �������
 * 
 * @author ShanZha
 * 
 */
public class MoreActivity extends BaseActivity {

	private TextView mTvTitle;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.more);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvTitle.setText(this.getString(R.string.more));
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickAddSubject(View view) {// ��ӵ���
		Intent intent = new Intent(this, SubjectAddActivity.class);
		this.startActivity(intent);
	}

	public void onClickVersion(View view) {// �汾˵��
		Intent intent = new Intent(this, VersionInfoActivity.class);
		this.startActivity(intent);
	}

	public void onClickHelp(View view) {// ���ְ���
		Intent intent = new Intent(this, HelperActivity.class);
		this.startActivity(intent);
	}

	public void onClickAbout(View view) {// ��������
		Intent intent = new Intent(this, AboutUsActivity.class);
		this.startActivity(intent);
	}

}
