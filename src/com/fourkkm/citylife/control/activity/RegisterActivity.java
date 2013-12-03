package com.fourkkm.citylife.control.activity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerUsergroups;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;
import com.zj.support.observer.model.Param;

/**
 * 注册界面
 * 
 * @author ShanZha
 * 
 */
public class RegisterActivity extends BaseActivity {

	private static final String TAG = "RegisterActivity";
	private TextView mTvTitle;
	private EditText mEtUsername, mEtEmail, mEtPswd, mEtPswdSure;
	private CheckBox mCbProtocal;

	private ModoerUsergroups mUserGroup;
	private ProgressDialog mDialog;

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

		mDialog = new ProgressDialog(this);
		mDialog.setMessage("正在注册...");
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRegister(View view) {// 注册
		if (!this.validate()) {
			return;
		}
		this.showDialog();
		this.fetchUserGroup();
	}

	private void fetchUserGroup() {
		String selectCode = "from com.andrnes.modoer.ModoerUsergroups mug where mug.grouptype = 'member' order by point";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), false, new ModoerUsergroups(),
				param);
	}

	private void onRegister() {
		String username = mEtUsername.getText().toString().trim();
		String email = mEtEmail.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();

		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		ModoerMembers member = new ModoerMembers();
		member.setUsername(username);
		member.setEmail(email);
		member.setPassword(pswd);
		member.setRegdate((int) (System.currentTimeMillis() / 1000));
		member.setRmb(new BigDecimal(0));
		member.setNewmsg("");
		member.setPoint(0);
		member.setPoint1(0);
		member.setPoint2(0);
		member.setPoint3(0);
		member.setPoint4(0);
		member.setPoint5(0);
		member.setPoint6(0);
		member.setGroupid(mUserGroup);
		this.getStoreOperation().saveOrUpdate(member, param);
	}

	private void showDialog() {
		if (null != mDialog) {
			mDialog.show();
		}
	}

	private void hideDialog() {
		if (null != mDialog) {
			mDialog.dismiss();
		}
	}

	private boolean validate() {
		if (!mCbProtocal.isChecked()) {
			this.showToast(this
					.getString(R.string.register_use_protocal_unchecked));
			return false;
		}
		String username = mEtUsername.getText().toString().trim();
		String email = mEtEmail.getText().toString().trim();
		String pswd = mEtPswd.getText().toString().trim();
		String pswdSure = mEtPswdSure.getText().toString().trim();
		if (TextUtils.isEmpty(username)) {
			this.showToast(this.getString(R.string.register_username_not_null));
			return false;
		}
		if (TextUtils.isEmpty(email)) {
			this.showToast(this.getString(R.string.register_email_not_null));
			return false;
		}
		if (!CommonUtil.isEmail(email)) {
			this.showToast(this.getString(R.string.register_email_invalide));
			return false;
		}
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
			this.onRegister();
		} else {
			this.hideDialog();
			this.showToast(this.getString(R.string.register_fail));
		}
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		System.out.println(" onSuccessSaveOrUpdate ="+out.getResult().toString());
		this.hideDialog();
		this.showToast(this.getString(R.string.register_success));
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.hideDialog();
		this.showToast(this.getString(R.string.register_fail));
	}
}
