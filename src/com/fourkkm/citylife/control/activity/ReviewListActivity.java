package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerReview;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerReviewItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * ∆¿¬€¡–±Ì
 * 
 * @author ShanZha
 * 
 */
public class ReviewListActivity extends BaseListActivity {

	private ModoerSubject mSubject;
	private TextView mTvTitle;
	private List<ModoerReview> mReviewLists;
	private int mOperator = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.review_list);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.review_list_listview);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);

		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mReviewLists = new ArrayList<ModoerReview>();
		mAdapter = new ItemSingleAdapter<ModoerReviewItemView, ModoerReview>(
				mReviewLists, this);
		mListView.setAdapter(mAdapter);
		Intent intent = this.getIntent();
		mOperator = intent.getIntExtra("operator", -1);
		if (GlobalConfig.IntentKey.PARTY_ME == mOperator) {
			mTvTitle.setText(this.getString(R.string.user_my_review));
		} else {
			mTvTitle.setText(this.getString(R.string.review));
			mSubject = (ModoerSubject) this.getIntent().getSerializableExtra(
					"ModoerSubject");
		}
		this.onFirstLoadSetting();
		this.onLoadMore();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickReview(View view) {
		Intent intent = new Intent(this, ReviewAddActivity.class);
		intent.putExtra("ModoerSubject", mSubject);
		this.startActivity(intent);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		super.onLoadMore();

		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerReview mr where mr.status = 1");
		if (GlobalConfig.IntentKey.REVIEW_ME == mOperator) {
			ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
					.getCurrMember();
			if (null != member) {
				sb.append(" and mr.uid.id = " + member.getId());
			}
		} else {
			sb.append(" and mr.sid.id = " + mSubject.getId());
		}
		sb.append(" order by mr.posttime DESC,mr.id DESC");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		this.getStoreOperation().findAll(sb.toString(), paramsMap, true,
				new ModoerReview(),
				new Param(this.hashCode(), GlobalConfig.URL_CONN));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		// ModoerReview review = (ModoerReview) parent.getAdapter().getItem(
		// position);
		// Intent intent = new Intent(this, ReviewDetailActivity.class);
		// intent.putExtra("review", review);
		// this.startActivity(intent);
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
		this.pretreatmentResults(results);
		this.notifyLoadOver();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.notifyLoadOver();
	}
}
