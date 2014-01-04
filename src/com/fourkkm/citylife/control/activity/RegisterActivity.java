package com.fourkkm.citylife.control.activity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerUsergroups;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.MD5;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.BaseActivity;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

/**
 * ×¢²á½çÃæ
 * 
 * @author ShanZha
 * 
 */
public class RegisterActivity extends BaseActivity {

	private static final String TAG = "RegisterActivity";
	private TextView mTvTitle;
	private EditText mEtUsername, mEtEmail, mEtPswd, mEtPswdSure;
	private CheckBox mCbProtocal;

	private ModoerMembers mMember;
	private ModoerUsergroups mUserGroup;
	private ProgressDialogProxy mDialogProxy;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.register);

		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtUsername = (EditText) this.findViewById(R.id.register_et_username);
		mEtEmail = (EditText) this.findViewById(R.id.register_et_email);
		mEtPswd = (EditText) this.findViewById(R.id.register_et_pswd);
		mEtPswdSure = (EditText) this.findViewById(R.id.register_et_pswd_sure);
		mCbProtocal = (CheckBox) this
				.findViewById(R.id.register_check_box_protocal);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mTvTitle.setText(this.getString(R.string.register_user));

		mDialogProxy = new ProgressDialogProxy(this);
		mDialogProxy.setMessage(this.getText(R.string.register_ing));
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	public void onClickRegister(View view) {// ×¢²á
		if (!this.validate()) {
			return;
		}
		mDialogProxy.showDialog();
		this.fetchUserGroup();
	}

	private void fetchUserGroup() {
		String selectCode = "from com.andrnes.modoer.ModoerUsergroups mug where mug.grouptype=:groupType order by point";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("groupType", "member");

		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode, paramMap, false,
				new ModoerUsergroups(), param);
	}

	private void onRegister() {
		String username = mEtUsername.getText().toString().trim();
		String email = mEtEmail.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();

		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		if (null == mMember) {
			mMember = new ModoerMembers();
		}
		mMember.setUsername(username);
		mMember.setEmail(email);
		mMember.setPassword(MD5.encryptMd5(pswd));
		mMember.setRegdate((int) (System.currentTimeMillis() / 1000));
		mMember.setRmb(new BigDecimal(0));
		mMember.setNewmsg("0");
		mMember.setPoint(0);
		mMember.setPoint1(0);
		mMember.setPoint2(0);
		mMember.setPoint3(0);
		mMember.setPoint4(0);
		mMember.setPoint5(0);
		mMember.setPoint6(0);
		mMember.setGroupid(mUserGroup);
		this.getStoreOperation().saveOrUpdate(mMember, param);
	}

	private boolean validate() {
		if (!mCbProtocal.isChecked()) {
			this.showToast(this
					.getString(R.string.register_use_protocal_unchecked));
			return false;
		}
		String pswd = mEtPswd.getText().toString().trim();
		String pswdSure = mEtPswdSure.getText().toString().trim();
		if (TextUtils.isEmpty(pswd) || TextUtils.isEmpty(pswdSure)
				|| !pswd.equals(pswdSure)) {
			this.showToast(this.getString(R.string.register_pswd_not_correct));
			return false;
		}
		return true;
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan--->results is null");
			return;
		}
		if (results.size() > 0) {
			mUserGroup = (ModoerUsergroups) results.get(0);
		}
		if (null == mUserGroup) {
			mDialogProxy.hideDialog();
			this.showToast(this.getString(R.string.register_fail));
		} else {
			this.onRegister();
		}

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		((CoreApp) AppUtils.getBaseApp(this)).setCurrMember(mMember);
		SharedPreferenceUtil.getSharedPrefercence().put(
				this.getApplicationContext(),
				GlobalConfig.SharePre.KEY_USERNAME, mMember.getUsername());
		SharedPreferenceUtil.getSharedPrefercence().put(
				this.getApplicationContext(), GlobalConfig.SharePre.KEY_PSWD,
				mEtPswd.getText().toString().trim());
		mDialogProxy.hideDialog();
		this.showToast(this.getString(R.string.register_success));
		this.setResult(RESULT_OK);
		this.finish();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		mDialogProxy.hideDialog();
	}
}
