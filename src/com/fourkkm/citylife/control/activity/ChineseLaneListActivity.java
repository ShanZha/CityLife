package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerFenlei;
import com.andrnes.modoer.ModoerFenleiCategory;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerChinaLaneItemView;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * Ã∆»ÀœÔ¡–±Ì
 * 
 * @author ShanZha
 * 
 */
public class ChineseLaneListActivity extends BaseListActivity {

	private LinearLayout mLlTopCheck;
	private ProgressBar mProBarTopCheck;
	private TextView mTvRental, mTvTrade;
	private List<ModoerFenlei> mChinaLaneList;
	private List<ModoerFenleiCategory> mChinaLaneCategoryList;

	private ModoerFenleiCategory mCurrCategory;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.china_lane_list);

		mListView = (ListView) this.findViewById(R.id.china_lane_list_listview);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.china_lane_list_ll_top_check);
		mProBarTopCheck = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mTvRental = (TextView) this
				.findViewById(R.id.china_lane_list_tv_rental);
		mTvTrade = (TextView) this.findViewById(R.id.china_lane_list_tv_trade);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mChinaLaneList = new ArrayList<ModoerFenlei>();
		mChinaLaneCategoryList = new ArrayList<ModoerFenleiCategory>();
		mAdapter = new ItemSingleAdapter<ModoerChinaLaneItemView, ModoerFenlei>(
				mChinaLaneList, this);
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
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerSubject(), param);
	}

	private String buildSelectCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerFenlei mf where mf.status = 1");
		if (null != mCurrCategory) {
//			sb.append(" and mf."+);
		}
		return sb.toString();
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE_CATEGORY:
			List<Object> results = (List<Object>) out.getResult();
			if (null != results) {
				for (int i = 0; i < results.size(); i++) {
					ModoerFenleiCategory category = (ModoerFenleiCategory) results
							.get(i);
					mChinaLaneCategoryList.add(category);
				}
			}
			this.hideLoadingCategory();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_CHINA_LANE:

			break;
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
	}
}
