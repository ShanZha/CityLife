package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.BaseActivity;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.utils.AppUtils;
import com.zj.app.utils.EncryptionUtil;
import com.zj.support.observer.model.Param;

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

	private ProgressDialogProxy mDialogProxy;

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
		mDialogProxy = new ProgressDialogProxy(this);
		mDialogProxy.setMessage(this.getText(R.string.login_ing));

		boolean isRemeberPswd = SharedPreferenceUtil.getSharedPrefercence()
				.getBoolean(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_REMBER_PSWD);
		boolean isAutoLogin = SharedPreferenceUtil.getSharedPrefercence()
				.getBoolean(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_AUTO_LOGIN);
		mCbPswd.setChecked(isRemeberPswd);
		mCbAutoLogin.setChecked(isAutoLogin);
		String username = SharedPreferenceUtil.getSharedPrefercence()
				.getString(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_USERNAME);
		mEtUsername.setText(username);
		if (isRemeberPswd) {
			String pswd = SharedPreferenceUtil.getSharedPrefercence()
					.getString(this.getApplicationContext(),
							GlobalConfig.SharePre.KEY_PSWD);
			mEtPswd.setText(pswd);
		}
	}

	/**
	 * ÃÜÂëÊÇ·ñÕýÈ·
	 * 
	 * @param inputPswd
	 * @param dbPswd
	 * @return
	 */
	private boolean isPswdCorrect(String inputPswd, String dbPswd) {
		String encrytPswd = EncryptionUtil.encryptionString(inputPswd);
		return encrytPswd.equals(dbPswd);
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	public void onClickRegister(View view) {// ×¢²á
		this.startActivityForResult(new Intent(this, RegisterActivity.class),
				REGISTER_REQ_CODE);
	}

	public void onClickLogin(View view) {// µÇÂ¼
		String username = mEtUsername.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			this.showToast(this.getString(R.string.register_username_not_null));
			return;
		}
		if (TextUtils.isEmpty(pswd)) {
			this.showToast(this.getString(R.string.login_pswd_null));
			return;
		}
		mDialogProxy.showDialog();
		String selectCode = "from com.andrnes.modoer.ModoerMembers mm where mm.username = :username";
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("username", username);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().find(selectCode, paramsMap, true,
				new ModoerMembers(), param);
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
		if (RESULT_OK == resultCode && REGISTER_REQ_CODE == requestCode) {
			this.setResult(RESULT_OK);
			this.finish();
		}
	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFind(out);
		Object result = out.getResult();
		if (null == result) {
			mDialogProxy.hideDialog();
			this.showToast(this.getString(R.string.login_info_incorrect));
			return;
		}
		ModoerMembers member = (ModoerMembers) result;
		if (this.isPswdCorrect(mEtPswd.getText().toString().trim(),
				member.getPassword())) {
			((CoreApp) AppUtils.getBaseApp(this)).setCurrMember(member);
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_USERNAME, member.getUsername());
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_PSWD,
					mEtPswd.getText().toString().trim());
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_IS_REMBER_PSWD,
					mCbPswd.isChecked());
			SharedPreferenceUtil.getSharedPrefercence().put(
					this.getApplicationContext(),
					GlobalConfig.SharePre.KEY_IS_AUTO_LOGIN,
					mCbAutoLogin.isChecked());
			this.setResult(RESULT_OK);
			this.finish();
		} else {
			this.showToast(this.getString(R.string.login_info_incorrect));
		}
		mDialogProxy.hideDialog();

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		mDialogProxy.hideDialog();
		this.showToast(this.getString(R.string.login_fail));
	}
}
