package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerCategory;
import com.andrnes.modoer.ModoerFavorites;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.AreaManager;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.SubjectCategoryManager;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerSubjectItemView;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.FloatingSubjectMenuProxy;
import com.fourkkm.citylife.widget.FloatingTwoMenuProxy;
import com.fourkkm.citylife.widget.IAddressListener;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.fourkkm.citylife.widget.ILocationConnListener;
import com.fourkkm.citylife.widget.LocationProxy;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 分类列表界面
 * 
 * @author ShanZha
 * 
 */
public class SubjectListActivity extends BaseListActivity implements
		IFloatingItemClick, OnDismissListener, ILocationConnListener,
		IAddressListener {

	private static final String TAG = "SubjectListActivity";
	private LinearLayout mLlTopCheck, mLlTopCheckLoading, mLlLocationLoading;
	private RelativeLayout mRlFloatingFirst, mRlFloatingSecond,
			mRlFloatingThird;
	private ImageButton mBtnAdd, mBtnLocationRefresh;
	private TextView mTvTitle, mTvNearOrSearch, mTvCategory, mTvSort,
			mTvLocation;
	private List<ModoerSubject> mSubjectList;
	private List<String> mSortDatas, mNearDatas;
	/** 上级传递过来的类别Id **/
	private int mCategoryId = 0;
	/** 当前类别，其为空时，即所有类别 **/
	private ModoerCategory mCurrCategory;
	/** 当前地区（县/地区级）-“附近”或“全部地区”时为空，“搜全城”时有用 **/
	private ModoerArea mCurrArea;
	/** 当前国家 **/
	private ModoerArea mCurrCountry;
	/** 当前排序，默认无任何排序 **/
	private int mCurrSort = SUBJECT_SORT_DEFAULT;
	/** 当前距离--附近时有用，默认1000 **/
	private int mCurrDistance = 1000;

	/** 排序漂浮代理 **/
	private FloatingOneMenuProxy mFloatingSortProxy;
	private FloatingOneMenuProxy mFloatingNearProxy;
	private FloatingTwoMenuProxy mFloatingSearchCityProxy;
	private FloatingSubjectMenuProxy mFloatingCategoryProxy;
	/** 搜全城/附近 **/
	// private boolean mIsSearchCity = false;
	private int mOperator = -1;

	private AreaManager mAreaMgr;
	private SubjectCategoryManager mCategoryMgr;

	/** 当前漂浮view类型 **/
	private int mCurrFloatingType = -1;

	private LocationProxy mLocation;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.subject_list);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mBtnAdd = (ImageButton) this
				.findViewById(R.id.titlebar_back_right_btn_operator);
		mLlTopCheckLoading = (LinearLayout) this
				.findViewById(R.id.subject_list_ll_top_loading);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mLlLocationLoading = (LinearLayout) this
				.findViewById(R.id.subject_list_ll_location_loading);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.subject_list_listview);
		mRlFloatingFirst = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_first);
		mRlFloatingSecond = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_second);
		mRlFloatingThird = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_third);
		mTvNearOrSearch = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_second);
		mTvSort = (TextView) this.findViewById(R.id.floating_layout_tv_third);
		mTvLocation = (TextView) this
				.findViewById(R.id.subject_list_tv_location);
		mBtnLocationRefresh = (ImageButton) this
				.findViewById(R.id.subject_list_ibtn_refresh_location);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mLocation = new LocationProxy(this, this.getSupportFragmentManager(),
				this);
		mAreaMgr = new AreaManager();
		mCategoryMgr = new SubjectCategoryManager();
		mCurrCountry = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();
		mSubjectList = new ArrayList<ModoerSubject>();
		mAdapter = new ItemSingleAdapter<ModoerSubjectItemView, ModoerSubject>(
				mSubjectList, this);

		Intent intent = this.getIntent();
		mOperator = intent.getIntExtra("operator", -1);
		switch (mOperator) {
		case GlobalConfig.IntentKey.SUBJECT_SEACH_CITY:
			this.prepareFloating();
			mFloatingSearchCityProxy = new FloatingTwoMenuProxy(this,
					GlobalConfig.FloatingType.TYPE_AREA);
			mFloatingSearchCityProxy.setFloatingItemClickListener(this);
			mFloatingSearchCityProxy.setFloatingDismissListener(this);
			mTvTitle.setText(this.getString(R.string.search_city));
			this.showLoadingCategory();
			this.fetchArea();
			break;
		case GlobalConfig.IntentKey.SUBJECT_ME:
			mLlTopCheck.setVisibility(View.GONE);
			mLlTopCheckLoading.setVisibility(View.GONE);
			mTvTitle.setText(this.getString(R.string.user_my_shop));
			this.onFirstLoadSetting();
			this.onLoadMore();
			break;
		case GlobalConfig.IntentKey.SUBJECT_FAVORITE:
			mBtnAdd.setVisibility(View.GONE);
			mLlTopCheck.setVisibility(View.GONE);
			mLlTopCheckLoading.setVisibility(View.GONE);
			mTvTitle.setText(this.getString(R.string.user_my_collection));
			mListView.setAdapter(mAdapter);
			this.onFirstLoadSetting();
			this.onLoadMore();
			break;
		default:
			mCategoryId = intent.getIntExtra("category", 0);
			this.prepareFloating();
			mFloatingNearProxy = new FloatingOneMenuProxy(this,
					GlobalConfig.FloatingType.TYPE_SUBJECT_DISTANCE);
			mFloatingNearProxy.setFloatingItemClickListener(this);
			mFloatingNearProxy.setFloatingDismissListener(this);
			this.addNearData();

			mTvTitle.setText(this.getString(R.string.nearby));
			this.showLoadingCategory();
			this.fetchCategory();
			break;
		}

	}

	@Override
	protected void prepareResume() {
		// TODO Auto-generated method stub
		super.prepareResume();
		if (null != mLocation) {
			mLocation.connect();
		}
	}

	@Override
	protected void preparePause() {
		// TODO Auto-generated method stub
		super.preparePause();
		if (null != mLocation) {
			mLocation.disconnect();
		}
	}

	private void prepareFloating() {

		mFloatingSortProxy = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_SUBJECT_SORT);
		mFloatingCategoryProxy = new FloatingSubjectMenuProxy(this,
				mCategoryMgr, GlobalConfig.FloatingType.TYPE_CATEGORY);
		mFloatingCategoryProxy.setFloatingtItemListener(this);
		mFloatingCategoryProxy.setFloatingDismissListener(this);
		mFloatingSortProxy.setFloatingItemClickListener(this);
		mFloatingSortProxy.setFloatingDismissListener(this);
		this.addSortData();

		mTvSort.setText(GlobalConfig.FloatingStr.STR_DEFAULT_SORT);
		if (GlobalConfig.IntentKey.SUBJECT_SEACH_CITY == mOperator) {
			mTvNearOrSearch.setText(GlobalConfig.FloatingStr.STR_ALL_AREA);
			mTvCategory.setText(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
			return;
		} else {
			mTvNearOrSearch.setText(this
					.getString(R.string.subject_distance_1000));
		}
		switch (mCategoryId) {
		case GlobalConfig.CATEGORY_FOOD:
			mTvCategory.setText(this.getString(R.string.main_food));
			break;
		case GlobalConfig.CATEGORY_RECREATION:
			mTvCategory.setText(this.getString(R.string.main_recreation));
			break;
		case GlobalConfig.CATEGORY_SHOPPING:
			mTvCategory.setText(this.getString(R.string.main_shopping));
			break;
		case GlobalConfig.CATEGORY_TRAVEL:
			mTvCategory.setText(this.getString(R.string.main_travel));
			break;
		}
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
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT_AREA);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		this.getStoreOperation().findAll(sb.toString(), paramsMap, true,
				new ModoerArea(), param);
	}

	/**
	 * 获取主题列表
	 */
	private void fetchSuject() {
		String selectCode = this.buildSelectCode();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerSubject(), param);
	}

	/**
	 * 获取收藏
	 */
	private void fetchFavorite() {
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		if (null == member) {
			return;
		}
		String selectCode = "from com.andrnes.modoer.ModoerFavorites mf where mf.uid.id = "
				+ member.getId();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_FAVORITE);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerFavorites(), param);
	}

	private boolean isCurrArea(int areaId) {
		if (null == mCurrArea) {
			return false;
		}
		return areaId == mCurrArea.getId();
	}

	private boolean isCurrSort(int pos) {
		return pos == mCurrSort;
	}

	private boolean isCurrCategory(int categoryId) {
		if (null == mCurrCategory) {
			return false;
		}
		return categoryId == mCurrCategory.getId();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent(this, SubjectDetailActivity.class);
		ModoerSubject subject = (ModoerSubject) parent.getAdapter().getItem(
				position);
		intent.putExtra("ModoerSubject", subject);
		this.startActivity(intent);
	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		// 搜全城
		if (GlobalConfig.FloatingType.TYPE_AREA == type) {
			// 特殊处理下，当前为全部地区，选中也为全部地区时
			if (null == mCurrArea
					&& GlobalConfig.FloatingStr.STR_ALL_AREA.equals(key)) {
				return;
			}
			// 全部地区时
			if (GlobalConfig.FloatingStr.STR_ALL_AREA.equals(key)) {
				mCurrArea = null;
				mTvNearOrSearch.setText(key);
			} else {
				ModoerArea area = mAreaMgr.getSubjectAreaByName(key);
				if (this.isCurrArea(area.getId())) {
					return;
				}
				mCurrArea = area;
				mTvNearOrSearch.setText(key);
			}
		} else if (GlobalConfig.FloatingType.TYPE_SUBJECT_DISTANCE == type) {
			// 附近
			int distance = Integer.valueOf(mNearDatas.get(pos));
			if (mCurrDistance == distance) {
				return;
			}
			mCurrDistance = distance;
			mTvNearOrSearch.setText(mCurrDistance + "");
		} else if (GlobalConfig.FloatingType.TYPE_SUBJECT_SORT == type) {// 排序
			if (this.isCurrSort(pos)) {
				return;
			}
			mCurrSort = pos;
			mTvSort.setText(key);
		} else if (GlobalConfig.FloatingType.TYPE_CATEGORY == type) {// 类别
			// 特殊处理下，当前为全部类别，选中也为全部类别时
			if (null == mCurrCategory
					&& GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(key)) {
				return;
			}
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(key)) {
				mCurrCategory = null;
				mTvCategory.setText(key);
			} else {
				ModoerCategory category = mCategoryMgr.getCategoryByName(key);
				if (null != category) {
					if (this.isCurrCategory(category.getId())) {
						return;
					}
					mCurrCategory = category;
					mTvCategory.setText(key);
				}
			}
		}
		this.reset();
		this.notifyDataChanged();
		this.onFirstLoadSetting();
		// 重新加载数据
		this.onLoadMore();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (null == mLocation) {
			return;
		}
		Location location = mLocation.fetchCurrLocation();
		mLocation.fetchAddress(this);
		if (null == location) {
			Log.i(TAG, "shan-->onConnected: location is null");
		} else {
			((CoreApp) AppUtils.getBaseApp(this)).mCurrLat = location
					.getLatitude();
			((CoreApp) AppUtils.getBaseApp(this)).mCurrLng = location
					.getLongitude();
			Log.i(TAG, "shan-->onConnected: " + "(" + location.getLatitude()
					+ "," + location.getLongitude() + ")");
		}
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub
		Log.i(TAG, "shan-->onDisconnect");
	}

	@Override
	public void onConnectFail(String error) {
		// TODO Auto-generated method stub
		Log.e(TAG, "shan-->onConnectFail:" + error);
		mLlLocationLoading.setVisibility(View.GONE);
		mTvLocation.setVisibility(View.VISIBLE);
		mTvLocation.setText(this.getString(R.string.location_conn_fail));
	}

	@Override
	public void onAddressSuccess(String addr) {
		// TODO Auto-generated method stub
		mTvLocation.setVisibility(View.VISIBLE);
		mLlLocationLoading.setVisibility(View.GONE);
		mBtnLocationRefresh.setVisibility(View.VISIBLE);
		mTvLocation.setText(addr);
	}

	@Override
	public void onAddressError(String error) {
		// TODO Auto-generated method stub
		mTvLocation.setVisibility(View.VISIBLE);
		mLlLocationLoading.setVisibility(View.GONE);
		mBtnLocationRefresh.setVisibility(View.GONE);
		mTvLocation.setText(error);
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		switch (mCurrFloatingType) {
		case GlobalConfig.FloatingType.TYPE_SUBJECT_DISTANCE:
		case GlobalConfig.FloatingType.TYPE_AREA:
			mRlFloatingFirst.setBackgroundResource(0);
			break;
		case GlobalConfig.FloatingType.TYPE_CATEGORY:
			mRlFloatingSecond.setBackgroundResource(0);
			break;
		case GlobalConfig.FloatingType.TYPE_SUBJECT_SORT:
			mRlFloatingThird.setBackgroundResource(0);
			break;
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRight(View view) {// 添加
		// mLocation.fetchCurrLocation();
		this.startActivity(new Intent(this, SubjectAddActivity.class));
	}

	public void onClickLocationRefresh(View view) {
		if (null != mLocation) {
			mTvLocation.setVisibility(View.GONE);
			mLlLocationLoading.setVisibility(View.VISIBLE);
			mBtnLocationRefresh.setVisibility(View.GONE);
			Location location = mLocation.fetchCurrLocation();
			if (null != location) {
				((CoreApp) AppUtils.getBaseApp(this)).mCurrLat = location
						.getLatitude();
				((CoreApp) AppUtils.getBaseApp(this)).mCurrLng = location
						.getLongitude();
				this.notifyDataChanged();
			}
			mLocation.fetchAddress(this);
		}
	}

	public void onClickFloatingFirst(View view) {// 距离/地区
		if (GlobalConfig.IntentKey.SUBJECT_SEACH_CITY == mOperator) {
			if (null != mFloatingSearchCityProxy) {
				mFloatingSearchCityProxy.showAsDropDown(view);
				if (null == mCurrArea) {
					mFloatingSearchCityProxy
							.resetDataByKey(GlobalConfig.FloatingStr.STR_ALL_AREA);
				} else {
					mFloatingSearchCityProxy
							.resetDataByKey(mCurrArea.getName());
				}
			}
		} else {
			if (null != mFloatingNearProxy) {
				mFloatingNearProxy.showAsDropDown(view);
			}
			mFloatingNearProxy.resetSelectItemBg(mTvNearOrSearch.getText()
					.toString().trim());
		}
		mCurrFloatingType = GlobalConfig.FloatingType.TYPE_SUBJECT_DISTANCE;
		mRlFloatingFirst
				.setBackgroundResource(R.drawable.floating_item_selected_bg);
	}

	public void onClickFloatingSecond(View view) {// 类型
		if (null != mFloatingCategoryProxy) {
			mFloatingCategoryProxy.showAsDropDown(view);
			mFloatingCategoryProxy.resetByCategory(mCurrCategory);
		}
		mCurrFloatingType = GlobalConfig.FloatingType.TYPE_CATEGORY;
		mRlFloatingSecond
				.setBackgroundResource(R.drawable.floating_item_selected_bg);
	}

	public void onClickFloatingThird(View view) {// 排序
		if (null == mFloatingSortProxy) {
			return;
		}
		mFloatingSortProxy.showAsDropDown(view);
		mFloatingSortProxy.resetSelectItemBg(mTvSort.getText().toString()
				.trim());
		mCurrFloatingType = GlobalConfig.FloatingType.TYPE_SUBJECT_SORT;
		mRlFloatingThird
				.setBackgroundResource(R.drawable.floating_item_selected_bg);
	}

	private String buildSelectCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerSubject ms");
		if (GlobalConfig.IntentKey.SUBJECT_ME == mOperator) {
			ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
					.getCurrMember();
			if (null == member) {
				return sb.toString();
			}
			sb.append(" where ms.cuid.id = " + member.getId());
			return sb.toString();
		}
		if (null != mCurrCategory) {
			sb.append(" where ms.status = 1 and ms.pid.enabled = 1 and ms.catid.id = "
					+ mCurrCategory.getId());
			sb.append(" and");
		} else {
			sb.append(" where");
		}
		if (null != mCurrArea) {
			sb.append(" ms.aid.id = " + mCurrArea.getId());
		} else {// 限制“国家级”
			sb.append(" ms.cityId.id = " + mCurrCountry.getId());
		}

		if (GlobalConfig.IntentKey.SUBJECT_SEACH_CITY != mOperator) {
			double[] rounds = CommonUtil.getAround(
					((CoreApp) AppUtils.getBaseApp(this)).mCurrLat,
					((CoreApp) AppUtils.getBaseApp(this)).mCurrLng,
					mCurrDistance);
			double minLat = rounds[0];
			double minLng = rounds[1];
			double maxLat = rounds[2];
			double maxLng = rounds[3];
			sb.append(" and  ms.mapLng >= " + minLng);
			sb.append(" and  ms.mapLng <= " + maxLng);
			sb.append(" and ms.mapLat >= " + minLat);
			sb.append(" and ms.mapLat <= " + maxLat);
		}
		switch (mCurrSort) {
		case SUBJECT_SORT_DEFAULT:// 默认按照添加时间排序
			sb.append(" order by ms.addtime DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_RECOMMEND:
			sb.append(" order by ms.finer DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_SIGNUP_TIME:
			sb.append(" order by ms.addtime DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_REVIEW_COUNT:
			sb.append(" order by ms.reviews DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_LIKE_DEGREE:
			sb.append(" order by ms.best DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_VIEWS:
			sb.append(" order by ms.pageviews DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_PIC_COUNT:
			sb.append(" order by ms.pictures DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_COMPOSITE_REVIEW:
			sb.append(" order by ms.avgsort DESC,ms.id DESC");
			break;
		case SUBJECT_SORT_COST_PER:
			sb.append(" order by ms.avgprice DESC,ms.id DESC");
			break;
		}
		return sb.toString();
	}

	private void addSortData() {
		mSortDatas = new ArrayList<String>();
		mSortDatas.add(GlobalConfig.FloatingStr.STR_DEFAULT_SORT);
		mSortDatas.add(this.getString(R.string.subject_sort_recommend));
		mSortDatas.add(this.getString(R.string.subject_sort_signup_time));
		mSortDatas.add(this.getString(R.string.subject_sort_review_count));
		mSortDatas.add(this.getString(R.string.subject_sort_like_degree));
		mSortDatas.add(this.getString(R.string.subject_sort_views_count));
		mSortDatas.add(this.getString(R.string.subject_sort_pic_count));
		mSortDatas.add(this.getString(R.string.subject_sort_composite_review));
		mSortDatas.add(this.getString(R.string.subject_sort_cost_per));
		mFloatingSortProxy.setDatas(mSortDatas);
	}

	private void addNearData() {
		mNearDatas = new ArrayList<String>();
		mNearDatas.add(this.getString(R.string.subject_distance_500));
		mNearDatas.add(this.getString(R.string.subject_distance_1000));
		mNearDatas.add(this.getString(R.string.subject_distance_2000));
		mFloatingNearProxy.setDatas(mNearDatas);
	}

	/**
	 * 当条件改变时 ，重置
	 */
	private void reset() {
		mPage = 0;
		if (null != mSubjectList) {
			mSubjectList.clear();
		}
		this.setHaveMore(true);
		this.setEnbaleLoad(true);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		super.onLoadMore();
		if (GlobalConfig.IntentKey.SUBJECT_FAVORITE == mOperator) {
			this.fetchFavorite();
		} else {
			this.fetchSuject();
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
				// 设置初始类别
				if (mCategoryId == category.getId()) {
					mCurrCategory = category;
				}
				mCategoryMgr.add(category);
			}
			mFloatingCategoryProxy.prepareDatas();
			mListView.setAdapter(mAdapter);
			this.hideLoadingCategory();
			this.onFirstLoadSetting();
			this.onLoadMore();
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT_AREA == operator) {// Subject地区
			for (int i = 0; i < results.size(); i++) {
				ModoerArea area = (ModoerArea) results.get(i);
				mAreaMgr.add(area);
			}
			mFloatingSearchCityProxy.setDatas(mAreaMgr.buildAreaRelation());
			this.fetchCategory();
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT == operator) {// Subject
			for (int i = 0; i < results.size(); i++) {
				ModoerSubject subject = (ModoerSubject) results.get(i);
				String title = CommonUtil.transferHtmlToJava(subject.getName());
				subject.setName(title);
				mSubjectList.add(subject);
			}
			this.pretreatmentResults(results);
			this.notifyLoadOver();

		} else if (GlobalConfig.Operator.OPERATION_FINDALL_FAVORITE == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerFavorites favorite = (ModoerFavorites) results.get(i);
				ModoerSubject subject = favorite.getSid();
				if (null != subject && subject.getId() != 0) {
					String title = CommonUtil.transferHtmlToJava(subject
							.getName());
					subject.setName(title);
					mSubjectList.add(subject);
				}
			}
			this.pretreatmentResults(results);
			this.notifyLoadOver();
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT:
		case GlobalConfig.Operator.OPERATION_FINDALL_FAVORITE:
			this.notifyLoadOver();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_SUJECT_CATEGORY:
		case GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT_AREA:
			this.hideLoadingCategory();
			break;
		default:
			break;
		}
	}

	/** 排序-推荐度 **/
	private static final int SUBJECT_SORT_DEFAULT = 0;
	/** 排序-推荐度 **/
	private static final int SUBJECT_SORT_RECOMMEND = 1;
	/** 排序-登记时间 **/
	private static final int SUBJECT_SORT_SIGNUP_TIME = 2;
	/** 排序-点评数量 **/
	private static final int SUBJECT_SORT_REVIEW_COUNT = 3;
	/** 排序-喜欢程度 **/
	private static final int SUBJECT_SORT_LIKE_DEGREE = 4;
	/** 排序-浏览量 **/
	private static final int SUBJECT_SORT_VIEWS = 5;
	/** 排序-图片数量 **/
	private static final int SUBJECT_SORT_PIC_COUNT = 6;
	/** 排序-综合点评 **/
	private static final int SUBJECT_SORT_COMPOSITE_REVIEW = 7;
	/** 排序-人均消费 **/
	private static final int SUBJECT_SORT_COST_PER = 8;

}
