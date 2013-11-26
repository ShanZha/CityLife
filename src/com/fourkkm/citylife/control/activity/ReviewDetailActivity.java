package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerReview;
import com.andrnes.modoer.ModoerReviewOpt;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;
import com.zj.support.observer.model.Param;

/**
 * 评论详情界面
 * 
 * @author ShanZha
 * 
 */
public class ReviewDetailActivity extends BaseActivity {

	private static final String SORT_1 = "sort1";
	private static final String SORT_2 = "sort2";
	private static final String SORT_3 = "sort3";
	private static final String SORT_4 = "sort4";
	private TextView mTvTitle, mTvUsername, mTvCompositeLevel, mTvSort1,
			mTvSort1Level, mTvSort2, mTvSort2Level, mTvSort3, mTvSort3Level,
			mTvSort4, mTvSort4Level, mTvAvgSort, mTvContent;
	private RatingBar mRatingBarComposite, mRatingBar1, mRatingBar2,
			mRatingBar3, mRatingBar4;
	private ModoerReview mReview;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.review_detail);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvUsername = (TextView) this
				.findViewById(R.id.review_detail_tv_username);
		mTvCompositeLevel = (TextView) this
				.findViewById(R.id.review_detail_tv_composite_level);
		mTvSort1 = (TextView) this.findViewById(R.id.review_detail_tv_sort1);
		mTvSort1Level = (TextView) this
				.findViewById(R.id.review_detail_tv_sort1_level);
		mTvSort2 = (TextView) this.findViewById(R.id.review_detail_tv_sort2);
		mTvSort2Level = (TextView) this
				.findViewById(R.id.review_detail_tv_sort2_level);
		mTvSort3 = (TextView) this.findViewById(R.id.review_detail_tv_sort3);
		mTvSort3Level = (TextView) this
				.findViewById(R.id.review_detail_tv_sort3_level);
		mTvSort4 = (TextView) this.findViewById(R.id.review_detail_tv_sort4);
		mTvSort4Level = (TextView) this
				.findViewById(R.id.review_detail_tv_sort4_level);
		mTvAvgSort = (TextView) this
				.findViewById(R.id.review_detail_tv_avgsort);
		mTvContent = (TextView) this
				.findViewById(R.id.review_detail_tv_content);

		mRatingBarComposite = (RatingBar) this
				.findViewById(R.id.review_detail_rating_bar_composite);
		mRatingBar1 = (RatingBar) this
				.findViewById(R.id.review_detail_rating_bar_sort1);
		mRatingBar2 = (RatingBar) this
				.findViewById(R.id.review_detail_rating_bar_sort2);
		mRatingBar3 = (RatingBar) this
				.findViewById(R.id.review_detail_rating_bar_sort3);
		mRatingBar4 = (RatingBar) this
				.findViewById(R.id.review_detail_rating_bar_sort4);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		Intent intent = this.getIntent();
		mReview = (ModoerReview) intent.getSerializableExtra("review");
		mTvTitle.setText(this.getString(R.string.review_detail));
		ModoerMembers member = mReview.getUid();
		if (null != member && member.getId() != 0) {
			mTvUsername.setText(member.getUsername() + ":");
		}
		mTvAvgSort.setText(this.getString(R.string.average_per)
				+ mReview.getPrice());
		mTvContent.setText(mReview.getContent());
		this.fetchReviewOpt();
	}

	/**
	 * 获取各类评分
	 */
	private void fetchReviewOpt() {
		ModoerSubject subject = mReview.getSid();
		if (null != subject) {
			String selectCode = "from com.andrnes.modoer.ModoerReviewOpt mro where mro.enable = 1 and mro.gid.id = "
					+ subject.getPid().getReviewOptGid().getId();
			this.getStoreOperation().findAll(selectCode,
					new HashMap<String, Object>(), true, new ModoerReviewOpt(),
					new Param(this.hashCode(), GlobalConfig.URL_CONN));
		}
	}

	private void setSortText(ModoerReviewOpt reviewOpt) {
		if (null == reviewOpt) {
			return;
		}
		String flag = reviewOpt.getFlag();
		int sort1 = mReview.getSort1();
		int sort2 = mReview.getSort2();
		int sort3 = mReview.getSort3();
		int sort4 = mReview.getSort4();
		if (SORT_1.equals(flag)) {
			mTvSort1.setText(reviewOpt.getName());
			mRatingBar1.setProgress(sort1);
			mTvSort1Level.setText(CommonUtil.getStringByScore(
					this.getApplicationContext(), sort1));
		} else if (SORT_2.equals(flag)) {
			mTvSort2.setText(reviewOpt.getName());
			mRatingBar2.setProgress(sort2);
			mTvSort2Level.setText(CommonUtil.getStringByScore(
					this.getApplicationContext(), sort2));
		} else if (SORT_3.equals(flag)) {
			mTvSort3.setText(reviewOpt.getName());
			mRatingBar3.setProgress(sort3);
			mTvSort3Level.setText(CommonUtil.getStringByScore(
					this.getApplicationContext(), sort3));
		} else if (SORT_4.equals(flag)) {
			mTvSort4.setText(reviewOpt.getName());
			mRatingBar4.setProgress(sort4);
			mTvSort4Level.setText(CommonUtil.getStringByScore(
					this.getApplicationContext(), sort4));
		}

		int compositeScore = (sort1 + sort2 + sort3 + sort4) / 4;
		mRatingBarComposite.setProgress(compositeScore);
		mTvCompositeLevel.setText(CommonUtil.getStringByScore(
				this.getApplicationContext(), compositeScore));

	}

	public void onClickBack(View view) {
		this.finish();
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null != results) {// 根据Catology获取评分的各个类型以及名字
			for (int i = 0; i < results.size(); i++) {
				ModoerReviewOpt reviewOpt = (ModoerReviewOpt) results.get(i);
				this.setSortText(reviewOpt);
			}
			// this.hideReviewOptLoading();
		}
	}

}
