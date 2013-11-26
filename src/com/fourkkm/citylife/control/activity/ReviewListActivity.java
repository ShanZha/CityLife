package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.andrnes.modoer.ModoerReview;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerReviewItemView;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * ∆¿¬€¡–±Ì
 * 
 * @author ShanZha
 * 
 */
public class ReviewListActivity extends BaseListActivity {

	private TextView mTvTitle;
	private int mSujectId = 0;
	private List<ModoerReview> mReviewLists;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.review_list);
		mListView = (ListView) this.findViewById(R.id.review_list_listview);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvTitle.setText(this.getString(R.string.review));
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mReviewLists = new ArrayList<ModoerReview>();
		Intent intent = this.getIntent();
		mSujectId = intent.getIntExtra("subjectId", 0);
		mAdapter = new ItemSingleAdapter<ModoerReviewItemView, ModoerReview>(
				mReviewLists, this);
		mListView.setAdapter(mAdapter);
		this.notifyLoadStart();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	@Override
	public void notifyLoadStart() {
		// TODO Auto-generated method stub
		super.notifyLoadStart();

		String selectCode = "from com.andrnes.modoer.ModoerReview mr where mr.status = 1 and mr.sid.id = "
				+ mSujectId;
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerReview(),
				new Param(this.hashCode(), GlobalConfig.URL_CONN));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		ModoerReview review = (ModoerReview) parent.getAdapter().getItem(
				position);
		Intent intent = new Intent(this, ReviewDetailActivity.class);
		intent.putExtra("review", review);
		this.startActivity(intent);
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null != results) {
			for (int i = 0; i < results.size(); i++) {
				ModoerReview review = (ModoerReview) results.get(i);
				mReviewLists.add(review);
			}
		}
		this.notifyLoadOver();
		mCurrSize = mReviewLists.size();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.notifyLoadOver();
	}
}
