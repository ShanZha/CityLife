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

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerParty;
import com.andrnes.modoer.ModoerPartyCategory;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerPartyItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 聚会列表
 * 
 * @author ShanZha
 * 
 */
public class PartyListActivity extends BaseListActivity implements
		IFloatingItemClick, OnDismissListener {

	private static final String TAG = "PartyListActivity";
	/** 全部状态 **/
	private static final int PARTY_STATE_ALL = 0;
	/** 报名中 **/
	private static final int PARTY_STATE_SIGNNING = 1;
	/** 进行中 **/
	private static final int PARTY_STATE_ONGOING = 2;
	/** 已结束 **/
	private static final int PARTY_STATE_OVER = 3;
	/** 最新发起 **/
	private static final int PARTY_MOST_NEW = 0;
	/** 最旺人气 **/
	private static final int PARTY_MOST_POPULAR = 1;
	// private ProgressBar mProBarTopCheck;
	private LinearLayout mLlTopCheck, mLlTopCheckLoading;
	private RelativeLayout mRlFloatingFirst, mRlFloatingSecond,
			mRlFloatingThird;
	private TextView mTvTitle, mTvCategory, mTvState, mTvMost;

	private List<ModoerParty> mPartyList;
	private List<ModoerPartyCategory> mPartyCategoryList;
	/** 当前类别 **/
	private ModoerPartyCategory mCurrCategory;
	private int mCurrState = PARTY_STATE_ALL;
	private int mCurrMost = PARTY_MOST_NEW;
	/** 三个漂浮视图代理 **/
	private FloatingOneMenuProxy mFloatingCategory, mFloatingState,
			mFloatingMost;
	private List<String> mStateList, mMostList;

	private int mOperator = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.party_list);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mLlTopCheckLoading = (LinearLayout) this
				.findViewById(R.id.party_list_ll_top_loading);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvState = (TextView) this.findViewById(R.id.floating_layout_tv_second);
		mTvMost = (TextView) this.findViewById(R.id.floating_layout_tv_third);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.party_list_listview);

		mRlFloatingFirst = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_first);
		mRlFloatingSecond = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_second);
		mRlFloatingThird = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_third);

		mTvTitle.setText(this.getString(R.string.party));
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mPartyList = new ArrayList<ModoerParty>();
		mAdapter = new ItemSingleAdapter<ModoerPartyItemView, ModoerParty>(
				mPartyList, this);
		Intent intent = this.getIntent();
		mOperator = intent.getIntExtra("operator", -1);
		if (GlobalConfig.IntentKey.PARTY_ME == mOperator) {
			mListView.setAdapter(mAdapter);
			mLlTopCheck.setVisibility(View.GONE);
			mLlTopCheckLoading.setVisibility(View.GONE);
			mTvTitle.setText(this.getString(R.string.user_my_party));
			this.onFirstLoadSetting();
			this.onLoadMore();
		} else {
			this.prepareFloatingDatas();
			this.showLoadingCategory();
			this.fetchPartyCategory();
		}
	}

	private void prepareFloatingDatas() {
		mPartyCategoryList = new ArrayList<ModoerPartyCategory>();
		mStateList = new ArrayList<String>();
		mMostList = new ArrayList<String>();

		mFloatingCategory = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_CATEGORY);
		mFloatingState = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_PARTY_STATE);
		mFloatingMost = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_PARTY_MOST);

		mFloatingCategory.setFloatingItemClickListener(this);
		mFloatingState.setFloatingItemClickListener(this);
		mFloatingMost.setFloatingItemClickListener(this);

		mFloatingCategory.setFloatingDismissListener(this);
		mFloatingState.setFloatingDismissListener(this);
		mFloatingMost.setFloatingDismissListener(this);

		mStateList.add(this.getString(R.string.party_state_all));
		mStateList.add(this.getString(R.string.party_state_signning));
		mStateList.add(this.getString(R.string.party_state_ongoing));
		mStateList.add(this.getString(R.string.party_state_over));
		mFloatingState.setDatas(mStateList);

		mMostList.add(this.getString(R.string.party_most_new));
		mMostList.add(this.getString(R.string.party_most_popular));
		mFloatingMost.setDatas(mMostList);
	}

	/**
	 * 网络请求之后才知道类别信息
	 */
	private void prepareFloadingCategoryDatas() {
		if (null == mPartyCategoryList) {
			return;
		}
		List<String> categoryNameList = new ArrayList<String>();
		// 加上“所有类别”
		categoryNameList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		for (int i = 0; i < mPartyCategoryList.size(); i++) {
			String name = mPartyCategoryList.get(i).getName();
			categoryNameList.add(name);
		}
		mFloatingCategory.setDatas(categoryNameList);
	}

	private void showLoadingCategory() {
		mLlTopCheckLoading.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mLlTopCheckLoading.setVisibility(View.GONE);
		mLlTopCheck.setVisibility(View.VISIBLE);
	}

	private void fetchPartyCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerPartyCategory ";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_PARTY_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerPartyCategory(),
				param);
	}

	private void fetchPartys() {
		String selectCode = this.buildFetchPartys();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_PARTY);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerSubject(), param);
	}

	/**
	 * 根据当前条件组建查询语句
	 * 
	 * @return
	 */
	private String buildFetchPartys() {
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerParty mp where mp.status = 1 ");
		if (GlobalConfig.IntentKey.PARTY_ME == mOperator) {
			ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
					.getCurrMember();
			if (null == member) {
				return sb.toString();
			}
			sb.append(" and mp.uid.id = " + member.getId());
			return sb.toString();
		}
		// Step 1:类别限制
		if (null != mCurrCategory) {
			sb.append(" and mp.catid.id = ");
			sb.append(mCurrCategory.getId() + "");
		}
		// Step 2:状态限制
		switch (mCurrState) {
		case PARTY_STATE_ALL:
			break;
		case PARTY_STATE_SIGNNING:
			long currTime = System.currentTimeMillis() / 1000;
			sb.append(" and mp.joinendtime > " + currTime);
			break;
		case PARTY_STATE_ONGOING:
			long currTime2 = System.currentTimeMillis() / 1000;
			sb.append(" and mp.begintime < " + currTime2);
			sb.append(" and mp.endtime > " + currTime2);
			break;
		case PARTY_STATE_OVER:
			long currTime3 = System.currentTimeMillis() / 1000;
			sb.append(" and mp.endtime < " + currTime3);
			break;
		}
		// Step 3:最之限制
		if (PARTY_MOST_NEW == mCurrMost) {
			sb.append(" order by dateline DESC");
		} else if (PARTY_MOST_POPULAR == mCurrMost) {
			sb.append(" order by pageview DESC");
		}

		return sb.toString();
	}

	/**
	 * 根据当前的类别、状态等设置Text
	 */
	private void setTextByCurrValue() {
		switch (mCurrState) {
		case PARTY_STATE_ALL:
			mTvState.setText(this.getString(R.string.party_state_all));
			break;
		case PARTY_STATE_SIGNNING:
			mTvState.setText(this.getString(R.string.party_state_signning));
			break;
		case PARTY_STATE_ONGOING:
			mTvState.setText(this.getString(R.string.party_state_ongoing));
			break;
		case PARTY_STATE_OVER:
			mTvState.setText(this.getString(R.string.party_state_over));
			break;
		}

		if (PARTY_MOST_NEW == mCurrMost) {
			mTvMost.setText(this.getString(R.string.party_most_new));
		} else if (PARTY_MOST_POPULAR == mCurrMost) {
			mTvMost.setText(this.getString(R.string.party_most_popular));
		}

		if (null != mCurrCategory) {
			mTvCategory.setText(mCurrCategory.getName());
		}

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

	private ModoerPartyCategory getPartyCategoryByName(String name) {
		if (null == mPartyCategoryList) {
			return null;
		}
		for (int i = 0; i < mPartyCategoryList.size(); i++) {
			ModoerPartyCategory category = mPartyCategoryList.get(i);
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
		if (null != mPartyList) {
			mPartyList.clear();
		}
		this.setHaveMore(true);
		this.setEnbaleLoad(true);
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRight(View view) {// 添加
		Intent intent = new Intent(this, PartyAddActivity.class);
		this.startActivity(intent);
	}

	public void onClickFloatingFirst(View view) {// 类别
		if (null != mFloatingCategory) {
			mFloatingCategory.showAsDropDown(view);
			mFloatingCategory.resetSelectItemBg(mTvCategory.getText()
					.toString().trim());
			mRlFloatingFirst
					.setBackgroundResource(R.drawable.floating_item_selected_bg);
		}
	}

	public void onClickFloatingSecond(View view) {// 状态
		if (null != mFloatingState) {
			mFloatingState.showAsDropDown(view);
			mFloatingState.resetSelectItemBg(mTvState.getText().toString()
					.trim());
			mRlFloatingSecond
					.setBackgroundResource(R.drawable.floating_item_selected_bg);
		}
	}

	public void onClickFloatingThird(View view) {// 最之系列
		if (null != mFloatingMost) {
			mFloatingMost.showAsDropDown(view);
			mFloatingMost
					.resetSelectItemBg(mTvMost.getText().toString().trim());
			mRlFloatingThird
					.setBackgroundResource(R.drawable.floating_item_selected_bg);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		ModoerParty party = (ModoerParty) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this, PartyDetailActivity.class);
		intent.putExtra("party", party);
		this.startActivity(intent);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		super.onLoadMore();

		this.fetchPartys();
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
		if (GlobalConfig.Operator.OPERATION_FINDALL_PARTY_CATEGORY == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerPartyCategory category = (ModoerPartyCategory) results
						.get(i);
				mPartyCategoryList.add(category);
			}
			mListView.setAdapter(mAdapter);
			mTvCategory.setText(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
			this.setTextByCurrValue();
			this.prepareFloadingCategoryDatas();
			this.hideLoadingCategory();
			this.onFirstLoadSetting();
			this.onLoadMore();
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_PARTY == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerParty party = (ModoerParty) results.get(i);
				mPartyList.add(party);
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
		if (GlobalConfig.Operator.OPERATION_FINDALL_PARTY == operator) {
			this.notifyLoadOver();
		} else {
			this.hideLoadingCategory();
		}
	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		// 是否需要重置并请求数据
		boolean isReset = true;
		switch (type) {
		case GlobalConfig.FloatingType.TYPE_CATEGORY:
			if (this.isCurrCategory(key)) {
				isReset = false;
				return;
			}
			ModoerPartyCategory category = this.getPartyCategoryByName(key);
			mCurrCategory = category;
			break;
		case GlobalConfig.FloatingType.TYPE_PARTY_STATE:
			if (pos == mCurrState) {
				isReset = false;
				return;
			}
			mCurrState = pos;
			break;
		case GlobalConfig.FloatingType.TYPE_PARTY_MOST:
			if (pos == mCurrMost) {
				isReset = false;
				return;
			}
			mCurrMost = pos;
			break;
		}
		if (isReset) {
			mTvCategory.setText(key);
			this.setTextByCurrValue();
			this.reset();
			this.notifyDataChanged();
			this.onFirstLoadSetting();
			this.onLoadMore();
		}
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		mRlFloatingFirst.setBackgroundResource(0);
		mRlFloatingSecond.setBackgroundResource(0);
		mRlFloatingThird.setBackgroundResource(0);
	}
}
