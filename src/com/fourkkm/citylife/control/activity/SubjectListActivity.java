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
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.fourkkm.citylife.widget.LocationProxy;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * �����б����
 * 
 * @author ShanZha
 * 
 */
public class SubjectListActivity extends BaseListActivity implements
		IFloatingItemClick, OnDismissListener {

	private static final String TAG = "SubjectListActivity";
	private LinearLayout mLlTopCheck, mLlTopCheckLoading;
	private RelativeLayout mRlFloatingFirst, mRlFloatingSecond,
			mRlFloatingThird;
	private TextView mTvTitle, mTvNearOrSearch, mTvCategory, mTvSort;
	private List<ModoerSubject> mSubjectList;
	private List<String> mSortDatas, mNearDatas;
	/** �ϼ����ݹ��������Id **/
	private int mCategoryId = 0;
	/** ��ǰ�����Ϊ��ʱ����������� **/
	private ModoerCategory mCurrCategory;
	/** ��ǰ��������/��������-����������ȫ��������ʱΪ�գ�����ȫ�ǡ�ʱ���� **/
	private ModoerArea mCurrArea;
	/** ��ǰ���� **/
	private ModoerArea mCurrCountry;
	/** ��ǰ����Ĭ�����κ����� **/
	private int mCurrSort = SUBJECT_SORT_DEFAULT;
	/** ��ǰ����--����ʱ���ã�Ĭ��1000 **/
	private int mCurrDistance = 1000;

	/** ����Ư������ **/
	private FloatingOneMenuProxy mFloatingSortProxy;
	private FloatingOneMenuProxy mFloatingNearProxy;
	private FloatingTwoMenuProxy mFloatingSearchCityProxy;
	private FloatingSubjectMenuProxy mFloatingCategoryProxy;
	/** ��ȫ��/���� **/
	// private boolean mIsSearchCity = false;
	private int mOperator = -1;

	private AreaManager mAreaMgr;
	private SubjectCategoryManager mCategoryMgr;

	/** ��ǰƯ��view���� **/
	private int mCurrFloatingType = -1;

	private LocationProxy mLocation;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.subject_list);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mLlTopCheckLoading = (LinearLayout) this
				.findViewById(R.id.subject_list_ll_top_loading);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
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
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mLocation = new LocationProxy(this, this.getSupportFragmentManager());
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
	 * ��ȡ����Category
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
	 * ��ȡĳ�����µ���������
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
	 * ��ȡ�����б�
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
	 * ��ȡ�ղ�
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
		// ��ȫ��
		if (GlobalConfig.FloatingType.TYPE_AREA == type) {
			// ���⴦���£���ǰΪȫ��������ѡ��ҲΪȫ������ʱ
			if (null == mCurrArea
					&& GlobalConfig.FloatingStr.STR_ALL_AREA.equals(key)) {
				return;
			}
			// ȫ������ʱ
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
			// ����
			int distance = Integer.valueOf(mNearDatas.get(pos));
			if (mCurrDistance == distance) {
				return;
			}
			mCurrDistance = distance;
			mTvNearOrSearch.setText(mCurrDistance + "");
		} else if (GlobalConfig.FloatingType.TYPE_SUBJECT_SORT == type) {// ����
			if (this.isCurrSort(pos)) {
				return;
			}
			mCurrSort = pos;
			mTvSort.setText(key);
		} else if (GlobalConfig.FloatingType.TYPE_CATEGORY == type) {// ���
			// ���⴦���£���ǰΪȫ�����ѡ��ҲΪȫ�����ʱ
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
		// ���¼�������
		this.onLoadMore();
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

	public void onClickRight(View view) {// ���
	// mLocation.fetchCurrLocation();
		this.startActivity(new Intent(this, SubjectAddActivity.class));
	}

	public void onClickFloatingFirst(View view) {// ����/����
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

	public void onClickFloatingSecond(View view) {// ����
		if (null != mFloatingCategoryProxy) {
			mFloatingCategoryProxy.showAsDropDown(view);
			mFloatingCategoryProxy.resetByCategory(mCurrCategory);
		}
		mCurrFloatingType = GlobalConfig.FloatingType.TYPE_CATEGORY;
		mRlFloatingSecond
				.setBackgroundResource(R.drawable.floating_item_selected_bg);
	}

	public void onClickFloatingThird(View view) {// ����
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
			sb.append(" where ms.status = 1 and ms.pid.enabled = 1 and ms.pid.id = "
					+ mCurrCategory.getId());
			sb.append(" and");
		} else {
			sb.append(" where");
		}
		if (null != mCurrArea) {
			sb.append(" ms.aid.id = " + mCurrArea.getId());
		} else {// ���ơ����Ҽ���
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
	 * �������ı�ʱ ������
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
		if (GlobalConfig.Operator.OPERATION_FINDALL_SUJECT_CATEGORY == operator) {// Subject���
			for (int i = 0; i < results.size(); i++) {
				ModoerCategory category = (ModoerCategory) results.get(i);
				// ���ó�ʼ���
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
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT_AREA == operator) {// Subject����
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

		} else if (GlobalConfig.Operator.OPERATION_FINDALL_FAVORITE == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerFavorites favorite = (ModoerFavorites) results.get(i);
				ModoerSubject subject = favorite.getSid();
				if (null != subject && subject.getId() != 0) {
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

	/** ����-�Ƽ��� **/
	private static final int SUBJECT_SORT_DEFAULT = 0;
	/** ����-�Ƽ��� **/
	private static final int SUBJECT_SORT_RECOMMEND = 1;
	/** ����-�Ǽ�ʱ�� **/
	private static final int SUBJECT_SORT_SIGNUP_TIME = 2;
	/** ����-�������� **/
	private static final int SUBJECT_SORT_REVIEW_COUNT = 3;
	/** ����-ϲ���̶� **/
	private static final int SUBJECT_SORT_LIKE_DEGREE = 4;
	/** ����-����� **/
	private static final int SUBJECT_SORT_VIEWS = 5;
	/** ����-ͼƬ���� **/
	private static final int SUBJECT_SORT_PIC_COUNT = 6;
	/** ����-�ۺϵ��� **/
	private static final int SUBJECT_SORT_COMPOSITE_REVIEW = 7;
	/** ����-�˾����� **/
	private static final int SUBJECT_SORT_COST_PER = 8;
}
