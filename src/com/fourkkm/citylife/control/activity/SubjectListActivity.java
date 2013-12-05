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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerCategory;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.AreaManager;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.SubjectCategoryManager;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerSubjectItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.FloatingSubjectMenuProxy;
import com.fourkkm.citylife.widget.FloatingTwoMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
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
		IFloatingItemClick {

	private static final String TAG = "SubjectListActivity";
	private ProgressBar mProBarTopCheck;
	private LinearLayout mLlTopCheck;
	private TextView mTvTitle, mTvNearOrSearch, mTvCategory, mTvSort;
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

	/** 排序漂浮代理 **/
	private FloatingOneMenuProxy mFloatingSortProxy;
	private FloatingOneMenuProxy mFloatingNearProxy;
	private FloatingTwoMenuProxy mFloatingSearchCityProxy;
	private FloatingSubjectMenuProxy mFloatingCategoryProxy;
	/** 搜全城/附近 **/
	private boolean mIsSearchCity = false;

	private AreaManager mAreaMgr;
	private SubjectCategoryManager mCategoryMgr;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.subject_list);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mProBarTopCheck = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.subject_list_listview);
		mTvNearOrSearch = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_second);
		mTvSort = (TextView) this.findViewById(R.id.floating_layout_tv_third);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mAreaMgr = new AreaManager();
		mCategoryMgr = new SubjectCategoryManager();
		mCurrCountry = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();

		Intent intent = this.getIntent();
		mIsSearchCity = intent.getBooleanExtra("isSearchCity", false);
		if (!mIsSearchCity) {
			mCategoryId = intent.getIntExtra("category", 0);
			mFloatingNearProxy = new FloatingOneMenuProxy(this,
					GlobalConfig.FloatingType.TYPE_SUBJECT_DISTANCE);
			mFloatingNearProxy.setFloatingItemClickListener(this);
			this.addNearData();

			mTvTitle.setText(this.getString(R.string.nearby));
		} else {
			mFloatingSearchCityProxy = new FloatingTwoMenuProxy(this,
					GlobalConfig.FloatingType.TYPE_SUBJECT_SEARCH_CITY);
			mFloatingSearchCityProxy.setFloatingItemClickListener(this);
			mTvTitle.setText(this.getString(R.string.search_city));
		}

		mFloatingSortProxy = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_SUBJECT_SORT);
		mFloatingCategoryProxy = new FloatingSubjectMenuProxy(this,
				mCategoryMgr, GlobalConfig.FloatingType.TYPE_SUBJECT_CATEGORY);
		mFloatingCategoryProxy.setFloatingtItemListener(this);
		mFloatingSortProxy.setFloatingItemClickListener(this);
		this.addSortData();

		mSubjectList = new ArrayList<ModoerSubject>();
		mAdapter = new ItemSingleAdapter<ModoerSubjectItemView, ModoerSubject>(
				mSubjectList, this);

		this.prepareFloatingText();
		this.showLoadingCategory();
		if (mIsSearchCity) {// 搜全城
			this.fetchArea();
		} else {
			this.fetchCategory();
		}
	}

	private void prepareFloatingText() {
		mTvSort.setText(GlobalConfig.FloatingStr.STR_DEFAULT_SORT);
		if (mIsSearchCity) {
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
		mProBarTopCheck.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mProBarTopCheck.setVisibility(View.GONE);
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
		if (GlobalConfig.FloatingType.TYPE_SUBJECT_SEARCH_CITY == type) {
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
			this.showToast("暂未实现");
			return;
		} else if (GlobalConfig.FloatingType.TYPE_SUBJECT_SORT == type) {// 排序
			if (this.isCurrSort(pos)) {
				return;
			}
			mCurrSort = pos;
			mTvSort.setText(key);
		} else if (GlobalConfig.FloatingType.TYPE_SUBJECT_CATEGORY == type) {// 类别
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

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRight(View view) {// 添加

	}

	public void onClickFloatingFirst(View view) {// 距离
		if (mIsSearchCity) {
			if (null != mFloatingSearchCityProxy) {
				mFloatingSearchCityProxy.showAsDropDown(view);
			}
		} else {
			if (null != mFloatingNearProxy) {
				mFloatingNearProxy.showAsDropDown(view);
			}
		}
	}

	public void onClickFloatingSecond(View view) {// 类型
		if (null != mFloatingCategoryProxy) {
			mFloatingCategoryProxy.showAsDropDown(view);
		}
	}

	public void onClickFloatingThird(View view) {// 排序
		if (null == mFloatingSortProxy) {
			return;
		}
		mFloatingSortProxy.showAsDropDown(view);
	}

	private String buildSelectCode() {// 暂时未加上距离多少米的限制
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerSubject ms");
		if (null != mCurrCategory) {
			sb.append(" where ms.status = 1 and ms.pid.enabled = 1 and ms.pid.id = "
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
		switch (mCurrSort) {
		case SUBJECT_SORT_DEFAULT:
			// Do nothing
			break;
		case SUBJECT_SORT_RECOMMEND:
			sb.append(" order by ms.finer DESC");
			break;
		case SUBJECT_SORT_SIGNUP_TIME:
			sb.append(" order by ms.addtime DESC");
			break;
		case SUBJECT_SORT_REVIEW_COUNT:
			sb.append(" order by ms.reviews DESC");
			break;
		case SUBJECT_SORT_LIKE_DEGREE:
			sb.append(" order by ms.best DESC");
			break;
		case SUBJECT_SORT_VIEWS:
			sb.append(" order by ms.pageviews DESC");
			break;
		case SUBJECT_SORT_PIC_COUNT:
			sb.append(" order by ms.pictures DESC");
			break;
		case SUBJECT_SORT_COMPOSITE_REVIEW:
			sb.append(" order by ms.avgsort DESC");
			break;
		case SUBJECT_SORT_COST_PER:
			sb.append(" order by ms.avgprice DESC");
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
		this.fetchSuject();
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
				mSubjectList.add(subject);
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
