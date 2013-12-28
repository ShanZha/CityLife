package com.fourkkm.citylife.control.activity;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;

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
 * 保存会员基类
 * 
 * @author ShanZha
 * 
 */
public class BaseSaveMemberActivity extends BaseActivity {

	private static final String TAG = "BaseSaveMemberActivity";
	private ModoerMembers mMember;
	private ModoerUsergroups mUserGroup;
	protected ProgressDialogProxy mDialogProxy;
	protected String mUserName = "", mUserPswd = "", mEmail = "";

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mDialogProxy = new ProgressDialogProxy(this);
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	protected void fetchUserGroup() {
		String selectCode = "from com.andrnes.modoer.ModoerUsergroups mug where mug.grouptype=:groupType order by point";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("groupType", "member");

		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode, paramMap, false,
				new ModoerUsergroups(), param);
	}

	private void onRegister() {
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_MEMBER);
		if (null == mMember) {
			mMember = new ModoerMembers();
		}
		mMember.setUsername(mUserName);
		mMember.setEmail(mEmail);
		mMember.setPassword(MD5.encryptMd5(mUserPswd));
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
				mUserPswd);
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
		this.showToast(this.getString(R.string.register_fail));
	}
}
