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

import com.andrnes.modoer.ModoerCategory;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerSubjectItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
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
	private TextView mTvDistance, mTvType, mTvSort;
	private List<ModoerSubject> mSubjectList;
	private List<String> mSortDatas;
	private List<ModoerCategory> mCategoryList;
	/** 上级传递过来的类别Id **/
	private int mCategoryId = 0;
	/** 当前类别 **/
	private ModoerCategory mCurrCategory;
	/** 排序漂浮代理 **/
	private FloatingOneMenuProxy mFloatingSortProxy;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.subject_list);
		mProBarTopCheck = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.subject_list_listview);
		mTvDistance = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvType = (TextView) this.findViewById(R.id.floating_layout_tv_second);
		mTvSort = (TextView) this.findViewById(R.id.floating_layout_tv_third);
		super.prepareViews();

	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		Intent intent = this.getIntent();
		mCategoryId = intent.getIntExtra("category", 0);

		mFloatingSortProxy = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_SUBJECT_SORT);
		mFloatingSortProxy.setFloatingItemClickListener(this);
		this.addTestSortData();

		mSubjectList = new ArrayList<ModoerSubject>();
		mCategoryList = new ArrayList<ModoerCategory>();
		mAdapter = new ItemSingleAdapter<ModoerSubjectItemView, ModoerSubject>(
				mSubjectList, this);
		mListView.setAdapter(mAdapter);

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

	/**
	 * 获取所有Category
	 */
	private void fetchCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerCategory mc where mc.enabled = 1";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_SUJECT_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerCategory(),
				param);
	}

	private void fetchSuject() {
		if (null == mCurrCategory) {
			Log.e(TAG, "shan-->mCurrCategory is null");
			return;
		}
		// ???暂时仅按照分类搜索
		String selectCode = "from com.andrnes.modoer.ModoerSubject ms where ms.status = 1 and ms.pid.enabled = 1 and ms.pid.id = "
				+ mCurrCategory.getId();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerSubject(), param);
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
		// this.showToast(mSortDatas.get(pos));
		this.showToast(key);
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickFloatingFirst(View view) {// 距离
		this.showToast("还没处理");
	}

	public void onClickFloatingSecond(View view) {// 类型
		this.showToast("暂未实现");
	}

	public void onClickFloatingThird(View view) {// 排序
		if (null == mFloatingSortProxy) {
			return;
		}
		mFloatingSortProxy.showAsDropDown(view);
	}

	private void addTestSortData() {
		mSortDatas = new ArrayList<String>();
		mSortDatas.add("默认排序");
		mSortDatas.add("距离最近");
		mSortDatas.add("按人气排序");
		mSortDatas.add("按总体评价排序");
		mSortDatas.add("按服务排序");
		mSortDatas.add("费用从高到底");
		mSortDatas.add("费用从低到高");

		mFloatingSortProxy.setDatas(mSortDatas);
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
		if (GlobalConfig.Operator.OPERATION_FINDALL_SUJECT_CATEGORY == operator) {
			List<Object> results = (List<Object>) out.getResult();
			if (null == results) {
				return;
			}
			for (int i = 0; i < results.size(); i++) {
				ModoerCategory category = (ModoerCategory) results.get(i);
				System.out.println("name= " + category.getName() + " 目标Id = "
						+ mCategoryId + " id = " + category.getId());
				// 设置初始类别
				if (mCategoryId == category.getId()) {
					mCurrCategory = category;
				}
				mCategoryList.add(category);
			}
			this.hideLoadingCategory();
			this.onFirstLoadSetting();
			this.onLoadMore();
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT == operator) {
			List<Object> results = (List<Object>) out.getResult();
			if (null == results) {
				return;
			}
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
		default:
			break;
		}
	}
}
