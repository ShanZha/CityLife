package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerParty;
import com.andrnes.modoer.ModoerPartyApply;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.BaseActivity;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

/**
 * 聚会报名
 * 
 * @author ShanZha
 * 
 */
public class PartyApplyActivity extends BaseActivity {

	private static final String TAG = "PartyApplyActivity";
	private static final int REQ_LOGIN_CODE = 1;
	/** 常量定义见ModoerParty实体-sex字段 **/
	private static final int SEX_MAN = 2;
	private static final int SEX_WOMAN = 1;
	private static final int SEX_NONE = 0;
	private TextView mTvTitle, mTvSubject;
	private EditText mEtContact, mEtContactTel, mEtRemark;
	private RadioGroup mRgSex;
	private ProgressDialogProxy mDialogProxy;
	private ModoerParty mParty;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.party_apply);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvSubject = (TextView) this.findViewById(R.id.party_apply_tv_subject);
		mEtContact = (EditText) this.findViewById(R.id.party_apply_et_contact);
		mEtContactTel = (EditText) this
				.findViewById(R.id.party_apply_et_contact_tel);
		mEtRemark = (EditText) this.findViewById(R.id.party_apply_et_remark);

		mRgSex = (RadioGroup) this.findViewById(R.id.party_apply_radio_group);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		// 如果没有登录，先登录
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE);
			return;
		}
		this.prepare();
	}

	private void prepare() {
		mDialogProxy = new ProgressDialogProxy(this);
		mTvTitle.setText(this.getString(R.string.party_sign_up_me));
		Intent intent = this.getIntent();
		mParty = (ModoerParty) intent.getSerializableExtra("party");
		if (null == mParty) {
			Log.e(TAG, "shan-->party is null");
			return;
		}
		mTvSubject.setText(mParty.getSubject());
	}

	private int getSexByCheckId(int checkId) {
		switch (checkId) {
		case R.id.party_apply_rbtn_sex_man:
			return SEX_MAN;
		case R.id.party_apply_rbtn_sex_woman:
			return SEX_WOMAN;
		}
		return SEX_NONE;
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	public void onClickAdd(View view) {// 提交
		try {
			ModoerPartyApply apply = new ModoerPartyApply();
			apply.setPartyid(mParty);
			ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
					.getCurrMember();
			apply.setUid(member);
			apply.setUsername(member.getUsername());
			apply.setLinkman(mEtContact.getText().toString().trim());
			apply.setContact(mEtContactTel.getText().toString().trim());
			apply.setComment(mEtRemark.getText().toString().trim());
			apply.setDateline((int) CommonUtil.getCurrTimeByPHP());
			apply.setSex(this.getSexByCheckId(mRgSex.getCheckedRadioButtonId()));
			apply.setStatus(1);

			mDialogProxy.showDialog();
			this.getStoreOperation().saveOrUpdate(apply,
					new Param(this.hashCode(), GlobalConfig.URL_CONN));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (REQ_LOGIN_CODE == requestCode) {
			if (RESULT_OK == resultCode) {
				this.prepare();
			} else {
				this.finish();
			}
		}
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		this.showToast(this.getString(R.string.party_apply_success));
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerParty.class.getName());
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerMembers.class.getName());
		mDialogProxy.hideDialog();
		Intent intent = new Intent();
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		if (null != member) {
			intent.putExtra("signUpName", member.getUsername());
		}
		this.setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		mDialogProxy.hideDialog();
		this.showToast(this.getString(R.string.party_apply_fail));
	}
}
