package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerParty;
import com.andrnes.modoer.ModoerPartyCategory;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.BaseActivity;
import com.zj.app.utils.AppUtils;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.observer.model.Param;

/**
 * æ€ª·ÃÌº”
 * 
 * @author ShanZha
 * 
 */
public class PartyAddActivity extends BaseActivity {

	private static final String TAG = "PartyAddActivity";
	private static final int REQ_LOGIN_CODE = 1;
	private TextView mTvTitle, mTvInitiator, mTvInitiateTime;
	private EditText mEtSubject, mEtSignupEnd, mEtBeginTime, mEtEndTime,
			mEtContact, mEtContactTel, mEtLocationArea, mEtAddrDetail, mEtCost,
			mEtPlanNum, mEtDesc;

	private Spinner mSpinnerCategoty, mSpinnerSex;
	private ArrayAdapter<String> mCategoryAdapter, mSexAdapter;
	private List<ModoerPartyCategory> mPartyCategoryList;

	private ProgressDialog mDialog;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.party_add);
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
		mEtLocationArea = (EditText) this
				.findViewById(R.id.party_add_et_location_area);
		mEtAddrDetail = (EditText) this
				.findViewById(R.id.party_add_et_addr_detail);
		mEtCost = (EditText) this.findViewById(R.id.party_add_et_cost);
		mEtPlanNum = (EditText) this.findViewById(R.id.party_add_et_plan_num);
		mEtDesc = (EditText) this.findViewById(R.id.party_add_et_desc);

		mSpinnerCategoty = (Spinner) this
				.findViewById(R.id.party_add_spinner_category);
		mSpinnerSex = (Spinner) this.findViewById(R.id.party_add_spinner_sex);

		mDialog = new ProgressDialog(this);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE);
			return;
		}
		this.prepare();
	}

	private void prepare() {
		mTvTitle.setText(this.getString(R.string.party_add));

		mPartyCategoryList = new ArrayList<ModoerPartyCategory>();
		mSexAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, this.getResources()
						.getStringArray(R.array.sex_array));
		mSpinnerSex.setAdapter(mSexAdapter);

		this.prepareDataByMember();
		this.fetchPartyCategory();
	}

	private void fetchPartyCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerPartyCategory ";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_PARTY_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerPartyCategory(),
				param);
	}

	private void prepareDataByMember() {
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		mTvInitiator.setText(member.getUsername());
		mTvInitiateTime.setText(DateFormatMethod.getCurrentDate());

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
		String subject = mEtSubject.getText().toString().trim();
		if (TextUtils.isEmpty(subject)) {
			return false;
		}
		return false;
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickThumb(View view) {// Àı¬‘Õº

	}

	public void onClickAdd(View view) {// ÃÌº”
		if (!this.validate()) {
			return;
		}
		this.showDialog();
		ModoerParty party = new ModoerParty();
		party.setApplypriceType("rmb");
		int categoryPos = mSpinnerCategoty.getSelectedItemPosition();
		party.setCatid(mPartyCategoryList.get(categoryPos));
		int sexPos = mSpinnerSex.getSelectedItemPosition();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode && REQ_LOGIN_CODE == requestCode) {
			this.prepare();
		} else {
			this.finish();
		}
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan-->results is null");
			return;
		}
		String[] categoryNames = new String[results.size()];
		for (int i = 0; i < results.size(); i++) {
			ModoerPartyCategory category = (ModoerPartyCategory) results.get(i);
			categoryNames[i] = category.getName();
			mPartyCategoryList.add(category);
		}
		mCategoryAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categoryNames);
		mSpinnerCategoty.setAdapter(mCategoryAdapter);

	}
}
