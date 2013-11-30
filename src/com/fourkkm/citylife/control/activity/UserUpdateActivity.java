package com.fourkkm.citylife.control.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * 用户信息修改页面
 * 
 * @author ShanZha
 * 
 */
public class UserUpdateActivity extends BaseActivity {

	private TextView mTvTitle;
	private EditText mEtUsername, mEtOldPswd, mEtNewPswd, mEtNewPswdSure,
			mEtDesc;
	private ModoerMembers mMember;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.user_update);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtUsername = (EditText) this
				.findViewById(R.id.user_update_et_username);
		mEtOldPswd = (EditText) this.findViewById(R.id.user_update_et_old_pswd);
		mEtNewPswd = (EditText) this.findViewById(R.id.user_update_et_new_pswd);
		mEtNewPswdSure = (EditText) this
				.findViewById(R.id.user_update_et_new_pswd_sure);
		mEtDesc = (EditText) this.findViewById(R.id.user_update_et_desc);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mTvTitle.setText(this.getString(R.string.user_update_title));
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickAvator(View view) {// 头像

	}

	public void onClickCommit(View view) {// 提交
		String oldPswd = mEtOldPswd.getText().toString().trim();
		if (!oldPswd.equals(mMember.getPassword())) {
			this.showToast(this.getString(R.string.user_update_old_pswd_error));
			return;
		}
		String newPswd = mEtNewPswd.getText().toString().trim();
		String newPswdSure = mEtNewPswdSure.getText().toString().trim();
	}

	private boolean isChanged() {
		String username = mEtUsername.getText().toString().trim();

		String desc = mEtDesc.getText().toString().trim();
		return !username.equals(mMember.getUsername());
	}
}
