package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerFenlei;
import com.andrnes.modoer.ModoerFenleiCategory;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerPictures;
import com.fourkkm.citylife.ChinaLaneCategoryManager;
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
 * 唐人巷添加
 * 
 * @author ShanZha
 * 
 */
public class ChinaLaneAddActivity extends BaseAddActivity implements
		IFloatingTimeListener {

	private static final String TAG = "ChinaLaneAddActivity";
	private LinearLayout mLlCategoryLoading, mLlCategory;
	private TextView mTvTitle, mTvInitiator, mTvInitiateTime;
	private EditText mEtSubject, mEtExpireTime, mEtContact, mEtContactTel,
			mEtEmail, mEtAddr, mEtIm, mEtDesc;
	private Spinner mSpCategotyFirst, mSpCategotySecond;
	private ArrayAdapter<String> mCategoryAdapterFirst, mCategoryAdapterSecond;
	private List<String> mCategoryListFirst, mCategoryListSecond;

	private ChinaLaneCategoryManager mLaneCategoryMgr;
	private FloatingTimeProxy mFloatingTimeProxy;

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

		mLlThumbLoading = (LinearLayout) this
				.findViewById(R.id.china_lane_add_ll_thumb_loading);
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
		mGvPics = (GridView) this.findViewById(R.id.china_lane_add_gv_pics);
	}

	@Override
	protected void prepare() {
		mIsNeedUploadPics = true;
		mUploadStrType = "fenlei";
		super.prepare();
		mLaneCategoryMgr = new ChinaLaneCategoryManager();
		mTvTitle.setText(this.getString(R.string.china_lane_add));
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		mTvInitiator.setText(member.getUsername());
		mTvInitiateTime.setText(DateFormatMethod.getCurrentDate());
		mFloatingTimeProxy = new FloatingTimeProxy(this, this);
		this.fetchFenleiCategory();

	}

	@Override
	protected void setSpAdapter() {
		// TODO Auto-generated method stub
		super.setSpAdapter();

		mCategoryListFirst = new ArrayList<String>();
		mCategoryAdapterFirst = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, mCategoryListFirst);
		mSpCategotyFirst.setAdapter(mCategoryAdapterFirst);
		mCategoryAdapterFirst
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpCategotyFirst
				.setOnItemSelectedListener(mCategoryItemSelectedListener);

		mCategoryListSecond = new ArrayList<String>();
		mCategoryAdapterSecond = new SpinnerAdapter(this,
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

	/**
	 * 构建图片Json
	 * 
	 * @return
	 */
	protected String buildUploadPic() {
		if (this.getPicCount() > 0) {
			// 排除第一项,仅保存大图路径
			JSONObject json = new JSONObject();
			for (int i = 1; i < mPicList.size(); i++) {
				ModoerPictures pic = mPicList.get(i);
				String big = pic.getFilename();
				String title = pic.getTitle();
				try {
					json.put(title, big);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return json.toString();
		}
		return "";
	}

	private boolean validate() {
		long expireTime = CommonUtil.formatTimeByPHP(mEtExpireTime.getText()
				.toString().trim());
		if (0 == expireTime) {
			this.showToast(this.getString(R.string.time_format_error));
			return false;
		}
		if (TextUtils.isEmpty(mThumbPath)) {
			this.showToast(this.getString(R.string.thumb_not_null));
			return false;
		}
		return true;
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickExpireTime(View view) {
		if (null == mFloatingTimeProxy) {
			return;
		}
		mFloatingTimeProxy
				.setType(GlobalConfig.FloatingType.TYPE_PARTY_BEGIN_TIME);
		mFloatingTimeProxy.showLocation(view, Gravity.CENTER, 0, 0);
	}

	public void onClickAdd(View view) {
		if (!this.validate()) {
			return;
		}
		if (!this.isUploadFinished()) {
			this.showToast(this.getString(R.string.subject_upload_pic_unfinish));
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
			ModoerArea aid = mAreaMgr.getSubjectAreaByName(secondAreaName);
			if (null == aid) {// 没有第三级则给第二级
				int areaFirstPos = mSpAreaFirst.getSelectedItemPosition();
				ModoerArea cid = mAreaMgr.getSubjectAreaByName(mAreaFirst
						.get(areaFirstPos));
				lane.setAid(cid);
			} else {
				lane.setAid(aid);
			}
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

			String picJson = this.buildUploadPic();
			if (!TextUtils.isEmpty(picJson)) {
				lane.setPictures(picJson);
				lane.setPicturesJson(picJson);
//				lane.setPicNum(this.getPicCount());
			}

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

	@Override
	public void onCurrTime(int type, String time) {
		// TODO Auto-generated method stub
		if (null != mEtExpireTime) {
			mEtExpireTime.setText(time);
		}
	}
}
