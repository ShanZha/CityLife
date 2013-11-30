package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * µÇÂ¼½çÃæ
 * 
 * @author ShanZha
 * 
 */
public class LoginActivity extends BaseActivity {

	private static final int REGISTER_REQ_CODE = 1;
	private TextView mTvTitle;
	private EditText mEtUsername, mEtPswd;
	private CheckBox mCbPswd, mCbAutoLogin;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.login);

		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtUsername = (EditText) this.findViewById(R.id.login_et_username);
		mEtPswd = (EditText) this.findViewById(R.id.login_et_pswd);
		mCbPswd = (CheckBox) this.findViewById(R.id.login_check_box_pswd);
		mCbAutoLogin = (CheckBox) this
				.findViewById(R.id.login_check_box_auto_login);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mTvTitle.setText(this.getString(R.string.login_user));
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRegister(View view) {// ×¢²á
		this.startActivityForResult(new Intent(this, RegisterActivity.class),
				REGISTER_REQ_CODE);
	}

	public void onClickLogin(View view) {// µÇÂ¼

	}

	public void onClickQQ(View view) {// qqµÇÂ¼

	}

	public void onClickTencentWeibo(View view) {// ÌÚÑ¶Î¢²©µÇÂ¼

	}

	public void onClickSinaWeibo(View view) {// ÐÂÀËÎ¢²©µÇÂ¼

	}

	public void onClickTaoBao(View view) {// ÌÔ±¦µÇÂ¼

	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(RESULT_OK == resultCode&&REGISTER_REQ_CODE == requestCode){
			
		}
	}
}
