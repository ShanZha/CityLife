package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerFenlei;
import com.andrnes.modoer.ModoerFenleiCategory;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerChinaLaneItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingTwoMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 唐人巷列表
 * 
 * @author ShanZha
 * 
 */
public class ChineseLaneListActivity extends BaseListActivity implements
		IFloatingItemClick {

	private static final String TAG = "ChineseLaneListActivity";
	private LinearLayout mLlTopCheck;
	private ProgressBar mProBarTopCheck;
	private TextView mTvTitle;
	/** 查询条件：分类/地区 **/
	private TextView mTvCategory, mTvArea;
	private List<ModoerFenlei> mChinaLaneList;
	private List<ModoerFenleiCategory> mChinaLaneCategoryList;

	/** 当前类别，当其为空时，全部类别 **/
	private ModoerFenleiCategory mCurrCategory;
	/** 全部分类字符 **/
	private String mStrAllCategory = "";

	private FloatingTwoMenuProxy mFloatingCategory;

	// private FloatingOneMenuProxy mFloatingArea;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.china_lane_list);

		mListView = (PullUpDownListView) this
				.findViewById(R.id.china_lane_list_listview);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mProBarTopCheck = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_drop_back_tv_title);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvArea = (TextView) this.findViewById(R.id.floating_layout_tv_second);
		this.findViewById(R.id.floating_layout_ll_third).setVisibility(
				View.GONE);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mTvTitle.setText(this.getString(R.string.chinese_lane));
		mStrAllCategory = this.getString(R.string.floating_category_all);
		mChinaLaneList = new ArrayList<ModoerFenlei>();
		mChinaLaneCategoryList = new ArrayList<ModoerFenleiCategory>();
		mAdapter = new ItemSingleAdapter<ModoerChinaLaneItemView, ModoerFenlei>(
				mChinaLaneList, this);
		mListView.setAdapter(mAdapter);

		mFloatingCategory = new FloatingTwoMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_CHINA_LANE_CATEGORY);
		// mFloatingArea = new FloatingOneMenuProxy(this,
		// GlobalConfig.FloatingType.TYPE_CHINA_LANE_AREA);
		mFloatingCategory.setFloatingItemClickListener(this);
		// mFloatingArea.setFloatingItemClickListener(this);

		this.showLoadingCategory();
		this.fetchCategory();
	}

	private void showLoadingCategory() {
		mProBarTopCheck.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mProBarTopCheck.setVisibility(View.GONE);
		mLlTopCheck.setVisibility(View.VISIBLE);
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
		if (null != mCurrCategory) {
			sb.append(" and mf.catid.id = " + mCurrCategory.getId());
		}
		// 地区的条件暂时未加上？？？
		return sb.toString();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickFloatingFirst(View view) {// 类别菜单栏
		// this.showToast("第一个菜单栏还没实现");
		if (null != mFloatingCategory) {
			mFloatingCategory.showAsDropDown(view);
		}
	}

	public void onClickFloatingSecond(View view) {// 地区菜单栏
		this.showToast("地区菜单栏还没实现");
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
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE_CATEGORY:
			// 所有类别成功
			for (int i = 0; i < results.size(); i++) {
				ModoerFenleiCategory category = (ModoerFenleiCategory) results
						.get(i);
				mChinaLaneCategoryList.add(category);
			}
			mFloatingCategory.setDatas(this.buildLaneCategoryRelation());
			mTvCategory.setText(mStrAllCategory);
			this.hideLoadingCategory();
			// 类别查询成功之后，加载数据
			this.onFirstLoadSetting();
			this.onLoadMore();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE:
			// 唐人巷
			for (int i = 0; i < results.size(); i++) {
				ModoerFenlei lane = (ModoerFenlei) results.get(i);
				mChinaLaneList.add(lane);
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
		}
	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		if (GlobalConfig.FloatingType.TYPE_CHINA_LANE_CATEGORY == type) {// 类别
			if (this.isCurrCategory(key)) {
				return;
			}
			// 选择“全部类别”
			if (mStrAllCategory.equals(key)) {
				mCurrCategory = null;
			} else {
				ModoerFenleiCategory category = this.getLaneCategoryByName(key);
				mCurrCategory = category;
			}
			mTvCategory.setText(key);
		} else if (GlobalConfig.FloatingType.TYPE_CHINA_LANE_AREA == type) {// 地区

		}
		this.reset();
		this.notifyDataChanged();
		this.onFirstLoadSetting();
		// 重新加载数据
		this.onLoadMore();
	}

	/**
	 * 选择的类别跟当前类别是否一致
	 * 
	 * @param name
	 * @return
	 */
	private boolean isCurrCategory(String name) {
		if (null == mCurrCategory) {
			return false;
		}
		String currName = mCurrCategory.getName();
		return name.equals(currName);
	}

	/**
	 * 是否有父级类别
	 * 
	 * @param category
	 * @return
	 */
	private boolean hasParent(ModoerFenleiCategory category) {
		if (null == category) {
			return false;
		}
		return category.getPid() != null && category.getPid().getId() != 0;
	}

	/**
	 * 建立唐人巷类别父子名称关系
	 * 
	 * @return
	 */
	private Map<String, List<String>> buildLaneCategoryRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mChinaLaneCategoryList) {
			return map;
		}
		for (int i = 0; i < mChinaLaneCategoryList.size(); i++) {
			ModoerFenleiCategory category = mChinaLaneCategoryList.get(i);
			if (this.hasParent(category)) {
				continue;
			}
			map.put(category.getName(),
					this.getLaneCategoryChild(category.getId()));
		}
		// 最后加入“全部类别”选项，默认也是此项
		List<String> allCategoryList = new ArrayList<String>();
		allCategoryList.add(mStrAllCategory);
		map.put(mStrAllCategory, allCategoryList);
		return map;
	}

	/**
	 * 获取某类别的所有子Name列表
	 * 
	 * @param parentId
	 * @return
	 */
	private List<String> getLaneCategoryChild(int parentId) {
		List<String> childNames = new ArrayList<String>();
		if (null == mChinaLaneCategoryList) {
			return childNames;
		}
		for (int i = 0; i < mChinaLaneCategoryList.size(); i++) {
			ModoerFenleiCategory category = mChinaLaneCategoryList.get(i);
			if (!this.hasParent(category)) {
				continue;
			}
			if (parentId == category.getPid().getId()) {
				childNames.add(category.getName());
			}
		}
		return childNames;
	}

	/**
	 * 根据分类名字找到对应的类别
	 * 
	 * @param name
	 * @return
	 */
	private ModoerFenleiCategory getLaneCategoryByName(String name) {
		if (null == mChinaLaneCategoryList) {
			return null;
		}
		for (int i = 0; i < mChinaLaneCategoryList.size(); i++) {
			ModoerFenleiCategory category = mChinaLaneCategoryList.get(i);
			if (name.equals(category.getName())) {
				return category;
			}
		}
		return null;
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
