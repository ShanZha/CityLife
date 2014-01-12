package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerParty;
import com.andrnes.modoer.ModoerPartyCategory;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.FloatingTimeProxy;
import com.fourkkm.citylife.widget.IFloatingTimeListener;
import com.fourkkm.citylife.widget.SpinnerAdapter;
import com.zj.app.utils.AppUtils;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.AsyncImageView;

/**
 * 聚会添加
 * 
 * @author ShanZha
 * 
 */
public class PartyAddActivity extends BaseAddActivity implements
		IFloatingTimeListener {

	private static final String TAG = "PartyAddActivity";
	/** 常量定义见ModoerParty实体-sex字段 **/
	private static final int SEX_MAN = 2;
	private static final int SEX_WOMAN = 1;
	private static final int SEX_NONE = 0;
	/** 常量定义见ModoerParty实体-applypriceType字段 **/
	private static final String INTEGRATION_PONIT = "point1";
	private static final String INTEGRATION_RMB = "rmb";
	private static final String INTEGRATION_NONE = "none";

	private static final int POS_INTEGRATION_POINT = 0;
	private static final int POS_INTEGRATION_RMB = 1;
	private LinearLayout mLlCategoryLoading;
	private TextView mTvTitle, mTvInitiator, mTvInitiateTime;
	private EditText mEtSubject, mEtSignupEnd, mEtBeginTime, mEtEndTime,
			mEtContact, mEtContactTel, mEtAddrDetail, mEtCost, mEtPlanNum,
			mEtIntegration, mEtDesc;
	private Spinner mSpinnerCategoty, mSpIntegration;
	private RadioGroup mRgSex;
	private SpinnerAdapter mCategoryAdapter, mIntegrationAdapter;
	private List<ModoerPartyCategory> mPartyCategoryList;
	private List<String> mCategoryList, mIntegrationList;

	private FloatingTimeProxy mFloatingTimeProxy;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.party_add);
		mIvThumb = (AsyncImageView) this
				.findViewById(R.id.party_add_iv_thumb_upload);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvInitiator = (TextView) this
				.findViewById(R.id.party_add_tv_initiator);
		mTvInitiateTime = (TextView) this
				.findViewById(R.id.party_add_tv_initiate_time);

		mEtSubject = (EditText) this.findViewById(R.id.party_add_et_title);
		mEtSignupEnd = (EditText) this
				.findViewById(R.id.party_add_et_signup_end);
		mEtBeginTime = (EditText) this
				.findViewById(R.id.party_add_et_begin_time);
		mEtEndTime = (EditText) this.findViewById(R.id.party_add_et_end_time);
		mEtContact = (EditText) this.findViewById(R.id.party_add_et_contact);
		mEtContactTel = (EditText) this
				.findViewById(R.id.party_add_et_contact_tel);
		mEtAddrDetail = (EditText) this
				.findViewById(R.id.party_add_et_addr_detail);
		mEtCost = (EditText) this.findViewById(R.id.party_add_et_cost);
		mEtPlanNum = (EditText) this.findViewById(R.id.party_add_et_plan_num);
		mEtDesc = (EditText) this.findViewById(R.id.party_add_et_desc);
		mEtIntegration = (EditText) this
				.findViewById(R.id.party_add_et_integration);

		mSpinnerCategoty = (Spinner) this
				.findViewById(R.id.party_add_spinner_category);
		mSpIntegration = (Spinner) this
				.findViewById(R.id.party_add_spinner_integration);
		mRgSex = (RadioGroup) this.findViewById(R.id.party_add_radio_group);
		mSpAreaFirst = (Spinner) this
				.findViewById(R.id.party_add_spinner_area_first);
		mSpAreaSecond = (Spinner) this
				.findViewById(R.id.party_add_spinner_area_second);
		mLlArea = (LinearLayout) this.findViewById(R.id.party_add_ll_area);
		mLlAreaLoading = (LinearLayout) this
				.findViewById(R.id.party_add_ll_area_loading);
		mLlCategoryLoading = (LinearLayout) this
				.findViewById(R.id.party_add_ll_category_loading);
		mLlThumbLoading = (LinearLayout) this
				.findViewById(R.id.party_add_ll_thumb_loading);
	}

	@Override
	protected void prepare() {
		super.prepare();
		mTvTitle.setText(this.getString(R.string.party_add));
		mPartyCategoryList = new ArrayList<ModoerPartyCategory>();
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		mTvInitiator.setText(member.getUsername());
		mTvInitiateTime.setText(DateFormatMethod.getCurrentDate());
		mFloatingTimeProxy = new FloatingTimeProxy(this, this);

		this.fetchPartyCategory();
	}

	@Override
	protected void setSpAdapter() {
		// TODO Auto-generated method stub
		super.setSpAdapter();
		mCategoryList = new ArrayList<String>();
		mCategoryAdapter = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, mCategoryList);
		mSpinnerCategoty.setAdapter(mCategoryAdapter);
		mCategoryAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		mIntegrationList = new ArrayList<String>();
		String[] arrays = this.getResources().getStringArray(
				R.array.party_sign_up);
		mIntegrationList.addAll(Arrays.asList(arrays));
		mIntegrationAdapter = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, mIntegrationList);
		mSpIntegration.setAdapter(mIntegrationAdapter);
		mIntegrationAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	}

	private void fetchPartyCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerPartyCategory ";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_PARTY_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerPartyCategory(),
				param);
	}

	private int getSexByCheckId(int checkId) {
		switch (checkId) {
		case R.id.party_add_rbtn_sex_man:
			return SEX_MAN;
		case R.id.party_add_rbtn_sex_woman:
			return SEX_WOMAN;
		case R.id.party_add_rbtn_sex_none:
			return SEX_NONE;
		default:
			return SEX_NONE;
		}
	}

	private String getIntegrationType() {
		int checkPos = mSpIntegration.getSelectedItemPosition();
		switch (checkPos) {
		case POS_INTEGRATION_POINT:
			return INTEGRATION_PONIT;
		case POS_INTEGRATION_RMB:
			return INTEGRATION_RMB;
		}
		return INTEGRATION_NONE;
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickSignupEndTime(View view) {
		if (null == mFloatingTimeProxy) {
			return;
		}
		mFloatingTimeProxy
				.setType(GlobalConfig.FloatingType.TYPE_PARTY_SIGNUP_END_TIME);
		mFloatingTimeProxy.showLocation(view, Gravity.CENTER, 0, 0);
	}

	public void onClickBeginTime(View view) {
		if (null == mFloatingTimeProxy) {
			return;
		}
		mFloatingTimeProxy
				.setType(GlobalConfig.FloatingType.TYPE_PARTY_BEGIN_TIME);
		mFloatingTimeProxy.showLocation(view, Gravity.CENTER, 0, 0);
	}

	public void onClickEndTime(View view) {
		if (null == mFloatingTimeProxy) {
			return;
		}
		mFloatingTimeProxy
				.setType(GlobalConfig.FloatingType.TYPE_PARTY_END_TIME);
		mFloatingTimeProxy.showLocation(view, Gravity.CENTER, 0, 0);
	}

	private boolean validate() {
		long signUpTime = CommonUtil.formatTimeByPHP(mEtSignupEnd.getText()
				.toString().trim());
		long beginTime = CommonUtil.formatTimeByPHP(mEtBeginTime.getText()
				.toString().trim());
		long endTime = CommonUtil.formatTimeByPHP(mEtEndTime.getText()
				.toString().trim());
		if (0 == signUpTime || 0 == beginTime || 0 == endTime) {
			this.showToast(this.getString(R.string.time_format_error));
			return false;
		}
		String planNum = mEtPlanNum.getText().toString().trim();
		if (TextUtils.isEmpty(planNum)) {
			this.showToast(this.getString(R.string.plan_num_not_null));
			return false;
		}
		if (TextUtils.isEmpty(mThumbPath)) {
			this.showToast(this.getString(R.string.thumb_not_null));
			return false;
		}
		return true;
	}

	public void onClickAdd(View view) {// 添加
		if (!this.validate()) {
			return;
		}
		try {
			ModoerParty party = new ModoerParty();
			int categoryPos = mSpinnerCategoty.getSelectedItemPosition();
			party.setCatid(mPartyCategoryList.get(categoryPos));
			party.setCityId(((CoreApp) AppUtils.getBaseApp(this)).getCurrArea());
			int secondPos = mSpAreaSecond.getSelectedItemPosition();
			String secondAreaName = mAreaSecond.get(secondPos);
			ModoerArea aid = mAreaMgr.getSubjectAreaByName(secondAreaName);
			if (null == aid) {// 没有第三级则给第二级
				int areaFirstPos = mSpAreaFirst.getSelectedItemPosition();
				ModoerArea cid = mAreaMgr.getSubjectAreaByName(mAreaFirst
						.get(areaFirstPos));
				party.setAid(cid);
			} else {
				party.setAid(aid);
			}
			party.setUid(mCurrMember);
			party.setUsername(mCurrMember.getUsername());
			int checkId = mRgSex.getCheckedRadioButtonId();
			party.setSex(this.getSexByCheckId(checkId));
			party.setSubject(mEtSubject.getText().toString().trim());
			long signUpTime = CommonUtil.formatTimeByPHP(mEtSignupEnd.getText()
					.toString().trim());
			long beginTime = CommonUtil.formatTimeByPHP(mEtBeginTime.getText()
					.toString().trim());
			long endTime = CommonUtil.formatTimeByPHP(mEtEndTime.getText()
					.toString().trim());
			party.setJoinendtime((int) signUpTime);
			party.setBegintime((int) beginTime);
			party.setEndtime((int) endTime);

			String planNum = mEtPlanNum.getText().toString().trim();
			party.setPlannum(Integer.valueOf(planNum));
			party.setThumb(mThumbPath);
			String cost = mEtCost.getText().toString().trim();
			String integration = mEtIntegration.getText().toString().trim();
			if (!TextUtils.isEmpty(cost)) {
				party.setPrice(Integer.valueOf(cost));
			}
			if (!TextUtils.isEmpty(integration)) {
				party.setApplypriceType(this.getIntegrationType());
				party.setApplyprice(Float.valueOf(integration));
			} else {
				party.setApplypriceType(INTEGRATION_NONE);
			}
			party.setLinkman(mEtContact.getText().toString().trim());
			party.setContact(mEtContactTel.getText().toString().trim());
			party.setAddress(mEtAddrDetail.getText().toString().trim());
			party.setDes(mEtDesc.getText().toString().trim());
			party.setDateline((int) CommonUtil.getCurrTimeByPHP());
			party.setStatus(1);
			this.showWaiting();
			Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
			param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_PARTY);
			this.getStoreOperation().saveOrUpdate(party, param);
		} catch (Exception e) {
			e.printStackTrace();
			this.showToast(this.getString(R.string.commit_tip));
		}

	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		@SuppressWarnings("unchecked")
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan-->results is null");
			return;
		}
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_PARTY_CATEGORY == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerPartyCategory category = (ModoerPartyCategory) results
						.get(i);
				mCategoryList.add(category.getName());
				mPartyCategoryList.add(category);
			}
			this.notifyDataChanged(mCategoryAdapter);
			this.showLoading(mSpinnerCategoty);
			this.hideLoading(mLlCategoryLoading);

		}

	}

	@Override
	public void onCurrTime(int type, String time) {
		// TODO Auto-generated method stub
		switch (type) {
		case GlobalConfig.FloatingType.TYPE_PARTY_SIGNUP_END_TIME:
			if (null != mEtSignupEnd) {
				mEtSignupEnd.setText(time);
			}
			break;
		case GlobalConfig.FloatingType.TYPE_PARTY_BEGIN_TIME:
			if (null != mEtBeginTime) {
				mEtBeginTime.setText(time);
			}
			break;
		case GlobalConfig.FloatingType.TYPE_PARTY_END_TIME:
			if (null != mEtEndTime) {
				mEtEndTime.setText(time);
			}
			break;
		}
	}

}
