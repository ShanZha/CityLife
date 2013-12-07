package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerFavorites;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerReview;
import com.andrnes.modoer.ModoerReviewOpt;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;
import com.zj.app.constant.Config;
import com.zj.app.utils.AppUtils;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.observer.model.Param;

/**
 * 商店类别详情界面
 * 
 * @author ShanZha
 * 
 */
public class SubjectDetailActivity extends BaseActivity {

	private static final int REQ_LOGIN_CODE = 1;
	private static final String SORT_1 = "sort1";
	private static final String SORT_2 = "sort2";
	private static final String SORT_3 = "sort3";
	private static final String SORT_4 = "sort4";
	private ModoerSubject mSubject;
	private RelativeLayout mRlAddress, mRlTel;
	private TextView mTvTitle, mTvShopName, mTvThumbnailCount, mTvAveragePer,
			mTvTaste, mTvAddress, mTvTel, mTvImpression, mTvReviewCount,
			mTvReviewUserName, mTvReviewAveragePer, mTvReviewContent,
			mTvCompositeScore;
	/** 4项评分 **/
	private TextView mTvSort1, mTvSort2, mTvSort3, mTvSort4;
	private ImageView mIvThumbnail;
	private RatingBar mRatingBar;
	private RatingBar mRatingBarReview;

	private ProgressBar mProBarReview;
	private RelativeLayout mRlReview;

	private RelativeLayout mRlReviewOpt;
	private ProgressBar mProBarReviewOpt;

	private ImageButton mBtn;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.subject_detail);

		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mBtn = (ImageButton) this
				.findViewById(R.id.titlebar_back_right_btn_operator);
		mTvShopName = (TextView) this
				.findViewById(R.id.subject_detail_tv_shop_name);
		mTvThumbnailCount = (TextView) this
				.findViewById(R.id.thumb_detail_tv_thumbnail_count);
		mTvCompositeScore = (TextView) this
				.findViewById(R.id.subject_detail_composite_score);
		mTvAveragePer = (TextView) this
				.findViewById(R.id.subject_detail_tv_average);
		mTvReviewAveragePer = (TextView) this
				.findViewById(R.id.subject_detail_tv_review_average_per);
		mTvAddress = (TextView) this
				.findViewById(R.id.subject_detail_tv_address);
		mTvTel = (TextView) this.findViewById(R.id.subject_detail_tv_tel);
		mTvImpression = (TextView) this
				.findViewById(R.id.subject_detail_tv_impression);
		mTvReviewCount = (TextView) this
				.findViewById(R.id.subject_detail_tv_review_counts);
		mTvReviewUserName = (TextView) this
				.findViewById(R.id.subject_detail_tv_review_username);
		mTvReviewContent = (TextView) this
				.findViewById(R.id.subject_detail_tv_review_content);

		mTvSort1 = (TextView) this.findViewById(R.id.subject_detail_tv_sort1);
		mTvSort2 = (TextView) this.findViewById(R.id.subject_detail_tv_sort2);
		mTvSort3 = (TextView) this.findViewById(R.id.subject_detail_tv_sort3);
		mTvSort4 = (TextView) this.findViewById(R.id.subject_detail_tv_sort4);

		mIvThumbnail = (ImageView) this
				.findViewById(R.id.thumb_detail_iv_thumb);

		mRlAddress = (RelativeLayout) this
				.findViewById(R.id.subject_detail_rl_address);
		mRlTel = (RelativeLayout) this.findViewById(R.id.subject_detail_rl_tel);

		mRatingBar = (RatingBar) this
				.findViewById(R.id.subject_detail_ratingBar);
		mRatingBarReview = (RatingBar) this
				.findViewById(R.id.subject_detail_review_ratingBar);

		mProBarReview = (ProgressBar) this
				.findViewById(R.id.subject_detail_proBar_review);
		mRlReview = (RelativeLayout) this
				.findViewById(R.id.subject_detail_rl_review);

		mRlReviewOpt = (RelativeLayout) this
				.findViewById(R.id.subject_detail_ll_review_opt);
		mProBarReviewOpt = (ProgressBar) this
				.findViewById(R.id.subject_detail_proBar_review_opt);

		mTvThumbnailCount.setVisibility(View.VISIBLE);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mTvTitle.setText(this.getString(R.string.subject_detail));
		mBtn.setBackgroundResource(R.drawable.subject_detail_collection_selector);

		mSubject = (ModoerSubject) this.getIntent().getSerializableExtra(
				"ModoerSubject");
		if (null != mSubject) {
			mTvShopName.setText(mSubject.getName());
			mTvThumbnailCount.setText("" + mSubject.getPictures());
			mTvAveragePer.setText(this.getString(R.string.average_per)
					+ mSubject.getAvgprice());
			String address = mSubject.getAddress();
			if (TextUtils.isEmpty(address)) {
				mRlAddress.setVisibility(View.GONE);
			} else {
				mTvAddress.setText(address);
			}
			String tel = mSubject.getTel();
			if (TextUtils.isEmpty(tel)) {
				mRlTel.setVisibility(View.GONE);
			} else {
				mTvTel.setText(tel);
			}

			mTvReviewCount.setText(this.getString(R.string.review) + "("
					+ mSubject.getReviews() + ")");
			mTvCompositeScore.setText(this.getString(R.string.composite_score)
					+ mSubject.getAvgsort().intValue());
			mRatingBar.setProgress(mSubject.getAvgsort().intValue());
			String thumbnailUrl = GlobalConfig.URL_PIC + mSubject.getThumb();
			AsyncImageLoader.getImageLoad(this).showPic(thumbnailUrl,
					mIvThumbnail);

			this.showReviewLoading();
			this.fetchOneBestReview(mSubject.getId());

			this.showReviewOptLoading();
			this.fetchReviewOpt();
		}

	}

	/**
	 * 获取各类评分
	 */
	private void fetchReviewOpt() {
		String selectCode = "from com.andrnes.modoer.ModoerReviewOpt mro where mro.enable = 1 and mro.gid.id = "
				+ mSubject.getPid().getReviewOptGid().getId();
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerReviewOpt(),
				new Param(this.hashCode(), GlobalConfig.URL_CONN));
	}

	/**
	 * 获取一条此主题的好评论
	 * 
	 * @param subjectId
	 */
	private void fetchOneBestReview(int subjectId) {
		String selectCode = "from com.andrnes.modoer.ModoerReview mr where mr.sid.id = "
				+ subjectId + " and mr.status = 1 and mr.best = 2";
		this.getStoreOperation().find(selectCode,
				new HashMap<String, Object>(), true, new ModoerReview(),
				new Param(this.hashCode(), GlobalConfig.URL_CONN));
	}

	private void showReviewLoading() {
		mRlReview.setVisibility(View.GONE);
		mProBarReview.setVisibility(View.VISIBLE);
	}

	private void hideReviewLoading() {
		mRlReview.setVisibility(View.VISIBLE);
		mProBarReview.setVisibility(View.GONE);
	}

	private void showReviewOptLoading() {
		mRlReviewOpt.setVisibility(View.GONE);
		mProBarReviewOpt.setVisibility(View.VISIBLE);
	}

	private void hideReviewOptLoading() {
		mRlReviewOpt.setVisibility(View.VISIBLE);
		mProBarReviewOpt.setVisibility(View.GONE);
	}

	/**
	 * 收藏
	 */
	private void onCollection() {
		this.showToast(this.getString(R.string.subject_collection_start));
		ModoerFavorites favorite = new ModoerFavorites();
		favorite.setAddtime(CommonUtil.getCurrTimeByPHP());
		favorite.setSid(mSubject);
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		favorite.setUid(member);
		favorite.setUsername(member.getUsername());
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SUBJECT_CONNLECTION);
		this.getStoreOperation().saveOrUpdate(favorite, param);
	}

	public void onClickBack(View view) {// 返回
		this.finish();
	}

	public void onClickRight(View view) {// 收藏
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE);
			return;
		}
		this.onCollection();
	}

	public void onClickThumbnail(View view) {// 缩略图
		Intent intent = new Intent(this, SubjectAlbumActivity.class);
		intent.putExtra("subjectId", mSubject.getId());
		intent.putExtra("subjectName", mSubject.getName());
		intent.putExtra("subjectThumbnailCount", mSubject.getPictures());
		this.startActivity(intent);
	}

	public void onClickAddress(View view) {// 地址
		Intent intent = new Intent(this, SubjectMapMarkerActivity.class);
		intent.putExtra("ModoerSubject", mSubject);
		this.startActivity(intent);
	}

	public void onClickTel(View view) {// 电话

	}

	public void onClickReviewContent(View view) {// 评价内容
		Intent intent = new Intent(this, ReviewListActivity.class);
		intent.putExtra("subjectId", mSubject.getId());
		this.startActivity(intent);
	}

	public void onClickShareTo(View view) {// 分享到

	}

	public void onClickReview(View view) {// 评价

	}

	public void onClickUploadPic(View view) {// 上传图片

	}

	public void onClickError(View view) {// 报错

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode && REQ_LOGIN_CODE == requestCode) {
			this.onCollection();
		}
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		this.showToast(this.getString(R.string.subject_collection_success));
	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFind(out);

		ModoerReview review = (ModoerReview) out.getResult();
		if (null != review) {
			mTvReviewAveragePer.setText(this.getString(R.string.average_per)
					+ review.getPrice());

			mTvReviewContent.setText(review.getContent());
			ModoerMembers member = review.getUid();
			if (null != member && member.getId() != 0) {
				mTvReviewUserName.setText(member.getUsername());
			}
			int average = (review.getSort1() + review.getSort2()
					+ review.getSort3() + review.getSort4()) / 4;
			mRatingBarReview.setProgress(average);
		}
		this.hideReviewLoading();
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);

		List<Object> results = (List<Object>) out.getResult();
		if (null != results) {// 根据Catology获取评分的各个类型以及名字
			for (int i = 0; i < results.size(); i++) {
				ModoerReviewOpt reviewOpt = (ModoerReviewOpt) results.get(i);
				String flag = reviewOpt.getFlag();
				if (SORT_1.equals(flag)) {
					mTvSort1.setText(reviewOpt.getName() + "："
							+ mSubject.getSort1());
				} else if (SORT_2.equals(flag)) {
					mTvSort2.setText(reviewOpt.getName() + "："
							+ mSubject.getSort2());
				} else if (SORT_3.equals(flag)) {
					mTvSort3.setText(reviewOpt.getName() + "："
							+ mSubject.getSort3());
				} else if (SORT_4.equals(flag)) {
					mTvSort4.setText(reviewOpt.getName() + "："
							+ mSubject.getSort4());
				}
			}
			this.hideReviewOptLoading();
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		// this.hideReviewLoading();
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SUBJECT_CONNLECTION == operator) {
			this.showToast(this.getString(R.string.subject_collection_fail));
		}
	}
}
