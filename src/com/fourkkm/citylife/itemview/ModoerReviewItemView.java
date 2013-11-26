package com.fourkkm.citylife.itemview;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerReview;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.control.activity.BaseListActivity;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * ∆¿º€ItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerReviewItemView extends RelativeLayout implements ItemView {

	private static final int BEST = 2;
	private static final int BAD = 1;
	private static final int GOOD = 0;
	private TextView mTvUsername, mTvAveragePer, mTvContent, mTvOverallRating,
			mTvTime;
	private RatingBar mRatingBar;

	private Context mCtx;

	public ModoerReviewItemView(Context context) {
		this(context, null);
	}

	public ModoerReviewItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ModoerReviewItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mCtx = context;
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub

		mTvUsername = (TextView) this
				.findViewById(R.id.review_list_item_tv_username);
		mTvAveragePer = (TextView) this
				.findViewById(R.id.review_list_item_tv_average_per);
		mTvContent = (TextView) this
				.findViewById(R.id.review_list_item_tv_content);
		mTvOverallRating = (TextView) this
				.findViewById(R.id.review_list_item_tv_overall_rating);
		mTvTime = (TextView) this.findViewById(R.id.review_list_item_tv_time);

		mRatingBar = (RatingBar) this
				.findViewById(R.id.review_list_item_ratingBar);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerReview review = (ModoerReview) item;
		mTvUsername.setText(review.getUsername());
		mTvAveragePer.setText(mCtx.getString(R.string.average_per)
				+ review.getPrice());
		mTvContent.setText(review.getContent());

		long time = review.getPosttime() * 1000L;
		mTvTime.setText(DateFormatMethod.formatDate(new Date(time)));
		int best = review.getBest();
		switch (best) {
		case GOOD:// “ª∞„
			mTvOverallRating.setText(mCtx.getString(R.string.overall_rating)
					+ mCtx.getString(R.string.review_good));
			break;
		case BAD:// ≤Ó∆¿
			mTvOverallRating.setText(mCtx.getString(R.string.overall_rating)
					+ mCtx.getString(R.string.review_bad));
			break;
		case BEST:// ∫√∆¿
			mTvOverallRating.setText(mCtx.getString(R.string.overall_rating)
					+ mCtx.getString(R.string.review_best));
			break;
		}
		int averScore = (review.getSort1() + review.getSort2()
				+ review.getSort3() + review.getSort4()) / 4;
		mRatingBar.setProgress(averScore);

		if (((BaseListActivity) mCtx).isShouldLoadData(pos)) {
			((BaseListActivity) mCtx).notifyLoadStart();
		}
	}

}
