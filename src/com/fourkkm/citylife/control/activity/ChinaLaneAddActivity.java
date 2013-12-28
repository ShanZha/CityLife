package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerFenlei;
import com.andrnes.modoer.ModoerFenleiCategory;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.ChinaLaneCategoryManager;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.utils.AppUtils;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.AsyncImageView;

/**
 * 唐人巷添加
 * 
 * @author ShanZha
 * 
 */
public class ChinaLaneAddActivity extends BaseAddActivity {

	private static final String TAG = "ChinaLaneAddActivity";
	private LinearLayout mLlCategoryLoading, mLlCategory;
	private TextView mTvTitle, mTvInitiator, mTvInitiateTime;
	private EditText mEtSubject, mEtExpireTime, mEtContact, mEtContactTel,
			mEtEmail, mEtAddr, mEtIm, mEtDesc;
	private Spinner mSpCategotyFirst, mSpCategotySecond;
	private ArrayAdapter<String> mCategoryAdapterFirst, mCategoryAdapterSecond;
	private List<String> mCategoryListFirst, mCategoryListSecond;

	private ChinaLaneCategoryManager mLaneCategoryMgr;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.china_lane_add);
		mIvThumb = (AsyncImageView) this
				.findViewById(R.id.china_lane_add_iv_thumb_upload);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvInitiator = (TextView) this
				.findViewById(R.id.china_lane_add_tv_initiator);
		mTvInitiateTime = (TextView) this
				.findViewById(R.id.china_lane_add_tv_initiate_time);

		mEtSubject = (EditText) this.findViewById(R.id.china_lane_add_et_title);
		mEtExpireTime = (EditText) this
				.findViewById(R.id.china_lane_add_et_expire_time);
		mEtContact = (EditText) this
				.findViewById(R.id.china_lane_add_et_contact);
		mEtContactTel = (EditText) this
				.findViewById(R.id.china_lane_add_et_contact_tel);
		mEtEmail = (EditText) this.findViewById(R.id.china_lane_add_et_email);
		mEtIm = (EditText) this.findViewById(R.id.china_lane_add_et_im);
		mEtAddr = (EditText) this.findViewById(R.id.china_lane_add_et_addr);
		mEtDesc = (EditText) this.findViewById(R.id.china_lane_add_et_desc);

		mLlThumbLoading = (LinearLayout)this.findViewById(R.id.china_lane_add_ll_thumb_loading);
		mLlCategory = (LinearLayout) this
				.findViewById(R.id.china_lane_add_ll_category);
		mLlCategoryLoading = (LinearLayout) this
				.findViewById(R.id.china_lane_add_ll_category_loading);
		mLlArea = (LinearLayout) this.findViewById(R.id.china_lane_add_ll_area);
		mLlAreaLoading = (LinearLayout) this
				.findViewById(R.id.china_lane_add_ll_area_loading);
		mSpCategotyFirst = (Spinner) this
				.findViewById(R.id.china_lane_add_spinner_category_first);
		mSpCategotySecond = (Spinner) this
				.findViewById(R.id.china_lane_add_spinner_category_second);
		mSpAreaFirst = (Spinner) this
				.findViewById(R.id.china_lane_add_spinner_area_first);
		mSpAreaSecond = (Spinner) this
				.findViewById(R.id.china_lane_add_spinner_area_second);
	}

	@Override
	protected void prepare() {
		super.prepare();
		mLaneCategoryMgr = new ChinaLaneCategoryManager();
		mTvTitle.setText(this.getString(R.string.china_lane_add));
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		mTvInitiator.setText(member.getUsername());
		mTvInitiateTime.setText(DateFormatMethod.getCurrentDate());

		this.fetchFenleiCategory();

	}

	@Override
	protected void setSpAdapter() {
		// TODO Auto-generated method stub
		super.setSpAdapter();

		mCategoryListFirst = new ArrayList<String>();
		mCategoryAdapterFirst = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mCategoryListFirst);
		mSpCategotyFirst.setAdapter(mCategoryAdapterFirst);
		mCategoryAdapterFirst
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpCategotyFirst
				.setOnItemSelectedListener(mCategoryItemSelectedListener);

		mCategoryListSecond = new ArrayList<String>();
		mCategoryAdapterSecond = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mCategoryListSecond);
		mSpCategotySecond.setAdapter(mCategoryAdapterSecond);
		mCategoryAdapterSecond
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

	}

	private void fetchFenleiCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerFenleiCategory ";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true,
				new ModoerFenleiCategory(), param);
	}

	/**
	 * 根据类别的父类姓名重置子类列表
	 * 
	 * @param parentName
	 */
	private void resetCategoryChildList(String parentName) {
		try {
			ModoerFenleiCategory parent = mLaneCategoryMgr
					.getLaneCategoryByName(parentName);
			List<String> secondList = mLaneCategoryMgr
					.getLaneCategoryChild(parent.getId());
			mCategoryListSecond.clear();
			mCategoryListSecond.addAll(secondList);
			secondList = null;
			this.notifyDataChanged(mCategoryAdapterSecond);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean validate() {
		long expireTime = CommonUtil.formatTimeByPHP(mEtExpireTime.getText()
				.toString().trim());
		if (0 == expireTime) {
			this.showToast(this.getString(R.string.time_format_error));
			return false;
		}
		return true;
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickAdd(View view) {
		if (!this.validate()) {
			return;
		}
		try {
			ModoerFenlei lane = new ModoerFenlei();
			int categoryPos = mSpCategotySecond.getSelectedItemPosition();
			lane.setCatid(mLaneCategoryMgr
					.getLaneCategoryByName(mCategoryListSecond.get(categoryPos)));
			lane.setCityId(((CoreApp) AppUtils.getBaseApp(this)).getCurrArea());
			int secondPos = mSpAreaSecond.getSelectedItemPosition();
			String secondAreaName = mAreaSecond.get(secondPos);
			lane.setAid(mAreaMgr.getSubjectAreaByName(secondAreaName));
			lane.setUid(mCurrMember);
			lane.setUsername(mCurrMember.getUsername());
			lane.setSubject(mEtSubject.getText().toString().trim());
			lane.setAddress(mEtAddr.getText().toString().trim());
			long endTime = CommonUtil.formatTimeByPHP(mEtExpireTime.getText()
					.toString().trim());
			lane.setEndtime((int) endTime);
			lane.setLinkman(mEtContact.getText().toString().trim());
			lane.setContact(mEtContactTel.getText().toString().trim());
			lane.setEmail(mEtEmail.getText().toString().trim());
			lane.setIm(mEtIm.getText().toString().trim());
			lane.setContent(mEtDesc.getText().toString().trim());
			lane.setThumb(mThumbPath);
			lane.setStatus(1);
			lane.setDateline((int) CommonUtil.getCurrTimeByPHP());

			this.showWaiting();
			Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
			param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_CHINA_LANE);
			this.getStoreOperation().saveOrUpdate(lane, param);
		} catch (Exception e) {
			e.printStackTrace();
			this.showToast(this.getString(R.string.commit_tip));
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
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE_CATEGORY:
			// 所有类别成功
			for (int i = 0; i < results.size(); i++) {
				ModoerFenleiCategory category = (ModoerFenleiCategory) results
						.get(i);
				mLaneCategoryMgr.add(category);
				if (mLaneCategoryMgr.hasParent(category)) {
					mCategoryListSecond.add(category.getName());
				} else {
					mCategoryListFirst.add(category.getName());
				}
			}
			this.notifyDataChanged(mCategoryAdapterFirst);
			this.notifyDataChanged(mCategoryAdapterSecond);
			this.showLoading(mLlCategory);
			this.hideLoading(mLlCategoryLoading);
			break;
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE_CATEGORY == operator) {
			Log.e(TAG, "shan-->ChinaLane category fetch fails");
		}
	}

	/**
	 * 类别一级列表监听
	 */
	private OnItemSelectedListener mCategoryItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			resetCategoryChildList(mCategoryListFirst.get(position));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};
}
