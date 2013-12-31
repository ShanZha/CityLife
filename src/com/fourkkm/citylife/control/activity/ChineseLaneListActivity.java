package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerFenlei;
import com.andrnes.modoer.ModoerFenleiCategory;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.AreaManager;
import com.fourkkm.citylife.ChinaLaneCategoryManager;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerChinaLaneItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingTwoMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 唐人巷列表
 * 
 * @author ShanZha
 * 
 */
public class ChineseLaneListActivity extends BaseListActivity implements
		IFloatingItemClick, OnDismissListener {

	private static final String TAG = "ChineseLaneListActivity";
	private LinearLayout mLlTopCheck, mLlTopCheckLoading;
	private TextView mTvTitle;
	private RelativeLayout mRlFloatingFirst, mRlFloatingSecond;
	/** 查询条件：地区/分类 **/
	private TextView mTvArea, mTvCategory;
	private List<ModoerFenlei> mChinaLaneList;
	// private List<ModoerFenleiCategory> mChinaLaneCategoryList;

	/** 当前类别，当其为空时，全部类别 **/
	private ModoerFenleiCategory mCurrCategory;
	/** 当前地区（县/地区级）-“全部地区”时为空，默认为空 **/
	private ModoerArea mCurrArea;
	/** 当前国家 **/
	private ModoerArea mCurrCountry;

	private AreaManager mAreaMgr;
	private ChinaLaneCategoryManager mLaneCategoryMgr;

	private FloatingTwoMenuProxy mFloatingCategory;
	private FloatingTwoMenuProxy mFloatingArea;
	private int mOperator = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.china_lane_list);

		mListView = (PullUpDownListView) this
				.findViewById(R.id.china_lane_list_listview);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mLlTopCheckLoading = (LinearLayout) this
				.findViewById(R.id.china_lane_list_ll_top_loading);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mTvArea = (TextView) this.findViewById(R.id.floating_layout_tv_first);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_second);
		mRlFloatingFirst = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_first);
		mRlFloatingSecond = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_second);
		this.findViewById(R.id.floating_layout_rl_third).setVisibility(
				View.GONE);
		mTvTitle.setText(this.getString(R.string.chinese_lane));
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mChinaLaneList = new ArrayList<ModoerFenlei>();
		mAdapter = new ItemSingleAdapter<ModoerChinaLaneItemView, ModoerFenlei>(
				mChinaLaneList, this);
		Intent intent = this.getIntent();
		mOperator = intent.getIntExtra("operator", -1);
		if (GlobalConfig.IntentKey.PARTY_ME == mOperator) {
			mListView.setAdapter(mAdapter);
			mLlTopCheck.setVisibility(View.GONE);
			mLlTopCheckLoading.setVisibility(View.GONE);
			mTvTitle.setText(this.getString(R.string.user_my_china_lane));
			this.onFirstLoadSetting();
			this.onLoadMore();
			return;
		}
		mAreaMgr = new AreaManager();
		mLaneCategoryMgr = new ChinaLaneCategoryManager();
		mCurrCountry = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();
		mTvTitle.setText(this.getString(R.string.chinese_lane));

		mFloatingCategory = new FloatingTwoMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_CATEGORY);
		mFloatingArea = new FloatingTwoMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_AREA);
		mFloatingCategory.setFloatingItemClickListener(this);
		mFloatingCategory.setFloatingDismissListener(this);
		mFloatingArea.setFloatingItemClickListener(this);
		mFloatingArea.setFloatingDismissListener(this);

		this.showLoadingCategory();
		this.fetchArea();
	}

	private void showLoadingCategory() {
		mLlTopCheckLoading.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mLlTopCheckLoading.setVisibility(View.GONE);
		mLlTopCheck.setVisibility(View.VISIBLE);
	}

	/**
	 * 获取某国家下的所有区域
	 */
	private void fetchArea() {
		if (null == mCurrCountry) {
			Log.e(TAG, "shan-->mCurrCountry is null");
			return;
		}
		int countryId = mCurrCountry.getId();
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerArea ma where (ma.pid.id = ");
		sb.append(countryId);
		sb.append(" and ma.enabled = 1) or");
		sb.append(" (ma.pid.pid.id = ");
		sb.append(countryId);
		sb.append(" and ma.pid.enabled = 1)");
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_CHINA_AREA);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		this.getStoreOperation().findAll(sb.toString(), paramsMap, true,
				new ModoerArea(), param);
	}

	private void fetchCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerFenleiCategory";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true,
				new ModoerFenleiCategory(), param);
	}

	private void fetchChinaLane() {
		String selectCode = this.buildSelectCode();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerFenlei(), param);
	}

	private String buildSelectCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerFenlei mf where mf.status = 1");
		if (GlobalConfig.IntentKey.CHINA_LANE_ME == mOperator) {
			ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
					.getCurrMember();
			if (null == member) {
				return sb.toString();
			}
			sb.append(" and mf.uid.id = " + member.getId());
			return sb.toString();
		}
		if (null != mCurrCategory) {
			sb.append(" and mf.catid.id = " + mCurrCategory.getId());
		}
		if (null != mCurrArea) {
			sb.append(" and mf.aid.id = " + mCurrArea.getId());
		} else {// 限制“国家级”
			sb.append(" and mf.cityId.id = " + mCurrCountry.getId());
		}
		return sb.toString();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRight(View view) {// 添加
		this.startActivity(new Intent(this, ChinaLaneAddActivity.class));
	}

	public void onClickFloatingFirst(View view) {// 地区菜单栏
		if (null != mFloatingArea) {
			mFloatingArea.showAsDropDown(view);
			if (null == mCurrArea) {
				mFloatingArea
						.resetDataByKey(GlobalConfig.FloatingStr.STR_ALL_AREA);
			} else {
				mFloatingArea.resetDataByKey(mCurrArea.getName());
			}
			mRlFloatingFirst
					.setBackgroundResource(R.drawable.floating_item_selected_bg);
		}

	}

	public void onClickFloatingSecond(View view) {// 类别菜单栏
		if (null != mFloatingCategory) {
			mFloatingCategory.showAsDropDown(view);
			if (null == mCurrCategory) {
				mFloatingCategory
						.resetDataByKey(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
			} else {
				mFloatingCategory.resetDataByKey(mCurrCategory.getName());
			}
			mRlFloatingSecond
					.setBackgroundResource(R.drawable.floating_item_selected_bg);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		ModoerFenlei lane = (ModoerFenlei) parent.getAdapter()
				.getItem(position);
		Intent intent = new Intent(this, ChineseLaneDetailActivity.class);
		intent.putExtra("ModoerFeilei", lane);
		this.startActivity(intent);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		super.onLoadMore();
		this.fetchChinaLane();
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
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_AREA:
			for (int i = 0; i < results.size(); i++) {
				ModoerArea area = (ModoerArea) results.get(i);
				mAreaMgr.add(area);
			}
			mFloatingArea.setDatas(mAreaMgr.buildAreaRelation());
			mTvArea.setText(GlobalConfig.FloatingStr.STR_ALL_AREA);
			this.fetchCategory();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE_CATEGORY:
			// 所有类别成功
			for (int i = 0; i < results.size(); i++) {
				ModoerFenleiCategory category = (ModoerFenleiCategory) results
						.get(i);
				mLaneCategoryMgr.add(category);
			}
			mFloatingCategory.setDatas(mLaneCategoryMgr
					.buildLaneCategoryRelation());
			mTvCategory.setText(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
			mListView.setAdapter(mAdapter);
			this.hideLoadingCategory();
			// 类别查询成功之后，加载数据
			this.onFirstLoadSetting();
			this.onLoadMore();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE:
			// 唐人巷
			for (int i = 0; i < results.size(); i++) {
				ModoerFenlei lane = (ModoerFenlei) results.get(i);
				if (null != lane) {
					mChinaLaneList.add(lane);
				}
			}
			this.pretreatmentResults(results);
			this.notifyLoadOver();
			break;
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		// 查询唐人巷失败，重置
		if (GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE == operator) {
			this.notifyLoadOver();
		} else {
			this.hideLoadingCategory();
		}
	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		if (GlobalConfig.FloatingType.TYPE_CATEGORY == type) {// 类别
			if (this.isCurrCategory(key)) {
				return;
			}
			// 选择“全部类别”
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(key)) {
				mCurrCategory = null;
			} else {
				ModoerFenleiCategory category = mLaneCategoryMgr
						.getLaneCategoryByName(key);
				mCurrCategory = category;
			}
			mTvCategory.setText(key);
		} else if (GlobalConfig.FloatingType.TYPE_AREA == type) {// 地区
			// 特殊处理下，当前为全部地区，选中也为全部地区时
			if (null == mCurrArea
					&& GlobalConfig.FloatingStr.STR_ALL_AREA.equals(key)) {
				return;
			}
			// 全部地区时
			if (GlobalConfig.FloatingStr.STR_ALL_AREA.equals(key)) {
				mCurrArea = null;
			} else {
				ModoerArea area = mAreaMgr.getSubjectAreaByName(key);
				if (this.isCurrArea(area.getId())) {
					return;
				}
				mCurrArea = area;
			}
			mTvArea.setText(key);
		}
		this.reset();
		this.notifyDataChanged();
		this.onFirstLoadSetting();
		// 重新加载数据
		this.onLoadMore();
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		mRlFloatingFirst.setBackgroundResource(0);
		mRlFloatingSecond.setBackgroundResource(0);
	}

	/**
	 * 选择的类别跟当前类别是否一致
	 * 
	 * @param name
	 * @return
	 */
	private boolean isCurrCategory(String name) {
		if (null == mCurrCategory) {
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(name)) {
				return true;
			}
			return false;
		}
		String currName = mCurrCategory.getName();
		return name.equals(currName);
	}

	private boolean isCurrArea(int areaId) {
		if (null == mCurrArea) {
			return false;
		}
		return areaId == mCurrArea.getId();
	}

	/**
	 * 当条件改变时 ，重置
	 */
	private void reset() {
		mPage = 0;
		if (null != mChinaLaneList) {
			mChinaLaneList.clear();
		}
		this.setHaveMore(true);
		this.setEnbaleLoad(true);
	}

}
