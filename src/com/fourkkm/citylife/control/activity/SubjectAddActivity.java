package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAlbum;
import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerCategory;
import com.andrnes.modoer.ModoerPictures;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.SubjectCategoryManager;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.widget.SpinnerAdapter;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.AsyncImageView;

/**
 * 商铺添加
 * 
 * @author ShanZha
 * 
 */
public class SubjectAddActivity extends BaseAddActivity {

	private static final String TAG = "SubjectAddActivity";
	public static final int REQ_CODE_MAP_POINT = 4;
	private TextView mTvTitle;
	private LinearLayout mLlCategory, mLlCategoryLoading;
	private EditText mEtName, mEtSubname, mEtTel, mEtDesc, mEtAddr;

	private Spinner mSpCategoryFirst, mSpCategorySecond;
	private ArrayAdapter<String> mAdapterCategoryFirst, mAdapterCategorySecond;
	private List<String> mCategoryFirstList, mCategorySecondList;

	private SubjectCategoryManager mCategoryMgr;
	/** 地图选点坐标 **/
	private double mLat = 0, mLng = 0;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.subject_add);
		mIvThumb = (AsyncImageView) this
				.findViewById(R.id.subject_add_iv_upload);
		mLlCategory = (LinearLayout) this
				.findViewById(R.id.subject_add_ll_category);
		mLlArea = (LinearLayout) this.findViewById(R.id.subject_add_ll_area);
		mLlCategoryLoading = (LinearLayout) this
				.findViewById(R.id.subject_add_ll_category_loading);
		mLlAreaLoading = (LinearLayout) this
				.findViewById(R.id.subject_add_ll_area_loading);
		mLlThumbLoading = (LinearLayout) this
				.findViewById(R.id.subject_add_ll_thumb_loading);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtName = (EditText) this.findViewById(R.id.subject_add_et_name);
		mEtSubname = (EditText) this.findViewById(R.id.subject_add_et_subname);
		mEtTel = (EditText) this.findViewById(R.id.subject_add_et_tel);
		mEtAddr = (EditText) this
				.findViewById(R.id.subject_add_et_detail_address);
		mEtDesc = (EditText) this.findViewById(R.id.subject_add_et_desc);

		mSpCategoryFirst = (Spinner) this
				.findViewById(R.id.subject_add_spinner_category_first);
		mSpCategorySecond = (Spinner) this
				.findViewById(R.id.subject_add_spinner_category_second);
		mSpAreaFirst = (Spinner) this
				.findViewById(R.id.subject_add_spinner_area_first);
		mSpAreaSecond = (Spinner) this
				.findViewById(R.id.subject_add_spinner_area_second);
		mGvPics = (GridView) this.findViewById(R.id.subject_add_gv_pics);

		mBtnCommit = (Button) this.findViewById(R.id.subject_add_btn_commit);
	}

	@Override
	protected void prepare() {
		mIsNeedUploadPics = true;
		super.prepare();

		mTvTitle.setText(this.getString(R.string.more_add_subject));
		mCategoryMgr = new SubjectCategoryManager();

		this.fetchCategory();
	}

	@Override
	protected void setSpAdapter() {
		super.setSpAdapter();
		mCategoryFirstList = new ArrayList<String>();
		mCategorySecondList = new ArrayList<String>();
		mAdapterCategoryFirst = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, mCategoryFirstList);
		mSpCategoryFirst.setAdapter(mAdapterCategoryFirst);
		mAdapterCategoryFirst
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpCategoryFirst
				.setOnItemSelectedListener(mCategoryItemSelectedListener);

		// mAdapterCategorySecond = new ArrayAdapter<String>(this,
		// android.R.layout.simple_spinner_item, mCategorySecondList);
		mAdapterCategorySecond = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, mCategorySecondList);
		mSpCategorySecond.setAdapter(mAdapterCategorySecond);
		mAdapterCategorySecond
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	/**
	 * 根据类别的父类姓名重置子类列表
	 * 
	 * @param parentName
	 */
	private void resetCategoryChildList(String parentName) {
		try {
			ModoerCategory parent = mCategoryMgr.getCategoryByName(parentName);
			List<String> secondList = mCategoryMgr.getNamesByParentId(parent
					.getId());
			mCategorySecondList.clear();
			mCategorySecondList.addAll(secondList);
			secondList = null;
			this.notifyDataChanged(mAdapterCategorySecond);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取所有Category
	 */
	private void fetchCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerCategory mc where mc.enabled = 1";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_SUJECT_CATEGORY);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerCategory(), param);
	}

	private boolean validate() {
		if (mLat == 0 || mLng == 0) {
			this.showToast(this.getString(R.string.subject_map_point_null));
			return false;
		}
		return true;
	}

	private ModoerSubject buildSubject() throws Exception {
		try {
			ModoerSubject mSubject = new ModoerSubject();
			mSubject.setAddress(mEtAddr.getText().toString().trim());
			mSubject.setDescription(mEtDesc.getText().toString().trim());
			mSubject.setName(mEtName.getText().toString().trim());
			mSubject.setSubname(mEtSubname.getText().toString().trim());
			mSubject.setTel(mEtTel.getText().toString().trim());
			mSubject.setThumb(mThumbPath);
			mSubject.setAddtime((int) com.fourkkm.citylife.util.CommonUtil
					.getCurrTimeByPHP());
			mSubject.setCuid(mCurrMember);
			mSubject.setCreator(mCurrMember.getUsername());
			mSubject.setOwner(mCurrMember.getUsername());
			mSubject.setMapLat(mLat);
			mSubject.setMapLng(mLng);

			int categorFirstPos = mSpCategoryFirst.getSelectedItemPosition();
			ModoerCategory pid = mCategoryMgr
					.getCategoryByName(mCategoryFirstList.get(categorFirstPos));
			mSubject.setPid(pid);
			int categorSecondPos = mSpCategorySecond.getSelectedItemPosition();
			ModoerCategory catid = mCategoryMgr
					.getCategoryByName(mCategorySecondList
							.get(categorSecondPos));
			mSubject.setCatid(catid);
			// int areaFirstPos = mSpAreaFirst.getSelectedItemPosition();
			// ModoerArea cityId = mAreaMgr.getSubjectAreaByName(mAreaFirst
			// .get(areaFirstPos));
			mSubject.setCityId(mCurrCountry);
			int areaSecondPos = mSpAreaSecond.getSelectedItemPosition();
			ModoerArea aid = mAreaMgr.getSubjectAreaByName(mAreaSecond
					.get(areaSecondPos));
			mSubject.setAid(aid);
			mSubject.setPictures(this.getPicCount());
			mSubject.setStatus(1);
			return mSubject;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickMapPoint(View view) {// 地图选点
		Intent intent = new Intent(this, MapMarkerActivity.class);
		this.startActivityForResult(intent, REQ_CODE_MAP_POINT);
	}

	public void onClickAdd(View view) {// 添加
		if (!this.validate()) {
			return;
		}
		if (!this.isUploadFinished()) {
			this.showToast(this.getString(R.string.subject_upload_pic_unfinish));
			return;
		}
		this.changedBtnState(mBtnCommit, false);
		this.setBtnText(mBtnCommit, this.getString(R.string.add_ing));
		try {
			List<Object> objs = new ArrayList<Object>();

			// Step 1：保存ModoerSubject
			ModoerSubject mSubject = this.buildSubject();
			// Step 2：保存ModoerAlbum
			ModoerAlbum mAlbum = new ModoerAlbum();
			mAlbum.setSid(mSubject);
			mAlbum.setCityId(mCurrCountry);
			mAlbum.setThumb(mThumbPath);
			mAlbum.setLastupdate((int) com.fourkkm.citylife.util.CommonUtil
					.getCurrTimeByPHP());
			mAlbum.setName(mSubject.getName() + "默认相册");
			mAlbum.setNum(this.getPicCount());

			objs.add(mSubject);
			objs.add(mAlbum);
			if (this.getPicCount() > 0) {
				// Step 3：保存ModoerPictures(排除第一项)
				for (int i = 1; i < mPicList.size(); i++) {
					ModoerPictures pic = mPicList.get(i);
					pic.setAlbumid(mAlbum);
					pic.setSid(mSubject);
					pic.setStatus(1);
					pic.setUrl("");
					objs.add(pic);
				}
			}

			this.showWaiting();
			Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
			param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_SUBJECT);
			this.getStoreOperation().saveOrUpdateArray(objs, param);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		if (REQ_CODE_MAP_POINT == requestCode) {
			if (null != data) {
				mLat = data.getDoubleExtra("lat", 0);
				mLng = data.getDoubleExtra("lng", 0);
			}
		}

	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		int operator = out.getOperator();
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan-->results is null");
			return;
		}
		if (GlobalConfig.Operator.OPERATION_FINDALL_SUJECT_CATEGORY == operator) {// Subject类别
			for (int i = 0; i < results.size(); i++) {
				ModoerCategory category = (ModoerCategory) results.get(i);
				mCategoryMgr.add(category);
			}
			try {// Step 1：设置类别一级列表
				List<String> firstList = mCategoryMgr.getFirstLevelNames();
				mCategoryFirstList.addAll(firstList);
				firstList = null;
				this.notifyDataChanged(mAdapterCategoryFirst);
				// Step 2：设置类别二级列表(默认显示第一级的第一个类别的所有子类别)
				this.resetCategoryChildList(mCategoryFirstList.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			mLlCategory.setVisibility(View.VISIBLE);
			mLlCategoryLoading.setVisibility(View.GONE);
		}
	}

	@Override
	public void onSuccessSaveOrUpdateArray(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdateArray(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_SUBJECT == operator) {
			this.onSaveSuccess();
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_SUJECT_CATEGORY:
			Log.e(TAG, "shan-->subject category fetch fails");
			break;
		case GlobalConfig.Operator.OPERATION_SAVE_SUBJECT:
			this.changedBtnState(mBtnCommit, true);
			this.setBtnText(mBtnCommit, this.getString(R.string.commit));
			this.hideWaiting();
			this.showToast(this.getString(R.string.commit_fail));
			break;
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
			resetCategoryChildList(mCategoryFirstList.get(position));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

}
