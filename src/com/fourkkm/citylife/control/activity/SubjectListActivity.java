package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerCategory;
import com.andrnes.modoer.ModoerReviewOptGroup;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerSubjectItemView;
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

	private ProgressBar mProBarTopCheck;
	private RelativeLayout mRlTopCheck;
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
		mRlTopCheck = (RelativeLayout) this
				.findViewById(R.id.subject_list_rl_top_check);
		mListView = (ListView) this.findViewById(R.id.subject_list_listview);
		mTvDistance = (TextView) this
				.findViewById(R.id.subject_list_tv_distance);
		mTvType = (TextView) this.findViewById(R.id.subject_list_tv_type);
		mTvSort = (TextView) this.findViewById(R.id.subject_list_tv_sort);
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
		mRlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mProBarTopCheck.setVisibility(View.GONE);
		mRlTopCheck.setVisibility(View.VISIBLE);
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

	public void onClickDistance(View view) {// 距离

	}

	public void onClickType(View view) {// 类型

	}

	public void onClickSort(View view) {// 排序
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
	public void notifyLoadStart() {
		// TODO Auto-generated method stub
		super.notifyLoadStart();

		// ModoerArea currArea =
		// ((CoreApp)AppUtils.getBaseApp(this)).getCurrArea();
		// if(null!=currArea){
		//
		// }
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
				// 设置初始类别
				if (mCategoryId == category.getId()) {
					mCurrCategory = category;
				}
				mCategoryList.add(category);
			}
			this.hideLoadingCategory();
			this.notifyLoadStart();
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT == operator) {
			List<Object> results = (List<Object>) out.getResult();
			if (null == results) {
				return;
			}
			for (int i = 0; i < results.size(); i++) {
				ModoerSubject subject = (ModoerSubject) results.get(i);
				ModoerCategory pid = subject.getPid();
				if (null != pid) {
					ModoerReviewOptGroup gid = pid.getReviewOptGid();
					if (null != gid) {
						int id = gid.getId();
					}
				}
				// System.out.println(" reviewOptGid --id =  "+subject.getPid().getReviewOptGid().getId());
				mSubjectList.add(subject);
			}
			this.notifyLoadOver();
			mCurrSize = mSubjectList.size();

		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		// int operator = out.getOperator();
		// switch (operator) {
		// case GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT:
		// this.notifyLoadOver();
		// break;
		// default:
		// break;
		// }
	}
}
