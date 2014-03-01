package com.fourkkm.citylife.itemview;

import java.util.Date;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerReview;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.control.activity.AlbumActivity;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * ∆¿º€ItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerReviewItemView extends RelativeLayout implements ItemView,
		OnClickListener {

	private static final int BEST = 2;
	private static final int BAD = 0;
	private static final int GOOD = 1;
	private TextView mTvUsername, mTvAveragePer, mTvContent, mTvOverallRating,
			mTvTime;
	private RatingBar mRatingBar;
	private FrameLayout mFrThumb;
	private ImageView mIvThumb;
	private TextView mTvThumbCount;
	private Bitmap mBmDefault;

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
		mBmDefault = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.list_thumb);
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
		mFrThumb = (FrameLayout) this
				.findViewById(R.id.review_list_item_fr_thumb);
		mIvThumb = (ImageView) this
				.findViewById(R.id.review_list_item_iv_thumb);
		mTvThumbCount = (TextView) this
				.findViewById(R.id.review_list_item_tv_thumbnail_count);
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
		mTvTime.setText(DateFormatMethod.formatDate(new Date(time),
				"yyyy-MM-dd HH:mm"));
		int best = review.getBest();
		switch (best) {
		case GOOD:// “ª∞„
			mTvOverallRating.setText(mCtx.getString(R.string.overall_rating)
					+ mCtx.getString(R.string.review_middle));
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

		String picJson = review.getPicturesJson();
		if (TextUtils.isEmpty(picJson)) {
			mFrThumb.setVisibility(View.GONE);
		} else {
			mFrThumb.setVisibility(View.VISIBLE);
			try {
				JSONObject pJson = new JSONObject(picJson);
				Iterator<String> it = pJson.keys();
				String url = "";
				if(it.hasNext()){
					String key = it.next();
					JSONObject jObj = pJson.getJSONObject(key);
					String thumb = jObj.getString("thumb");
					url = GlobalConfig.URL_PIC+thumb;
					System.out.println(" thumb = "+jObj.getString("thumb")
							+" pic = "+jObj.getString("picture"));
				}
				
//				JSONArray array = new JSONArray(picJson);
				int count = pJson.length();
				mTvThumbCount.setVisibility(View.VISIBLE);
				mTvThumbCount.setText(count + "");
				// String path = array.getString(0);
//				JSONObject jsonObj = (JSONObject) array.get(0);
//				@SuppressWarnings("unchecked")
//				Iterator<String> it = jsonObj.keys();
//				String url = "";
//				if (it.hasNext()) {
//					String key = it.next();
//					JSONObject jObj = jsonObj.getJSONObject(key);
//					String thumb = jObj.getString("thumb");
//					url = GlobalConfig.URL_PIC + thumb;
//				}
				System.out.println(" url = "+url);
				mFrThumb.setOnClickListener(this);
				mFrThumb.setTag(review);
				AsyncImageLoader.getImageLoad(mCtx).showPic(url, mIvThumb,
						mBmDefault);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		ModoerReview review = (ModoerReview) v.getTag();
		if (null == review) {
			return;
		}
		Intent intent = new Intent(mCtx, AlbumActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.ALBUM_REVIEW);
		intent.putExtra("reviewPicJson", review.getPicturesJson());
		intent.putExtra("reviewSubject", review.getSubject());
		mCtx.startActivity(intent);
	}

}
