package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.fourkkm.citylife.SinaWeiboShareProxy;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.FloatingTranslucentProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.sina.weibo.sdk.WeiboSDK;
import com.sina.weibo.sdk.api.BaseResponse;
import com.sina.weibo.sdk.api.IWeiboAPI;
import com.sina.weibo.sdk.api.IWeiboHandler;
import com.zj.app.BaseActivity;
import com.zj.app.utils.AppUtils;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.observer.model.Param;

/**
 * 商店类别详情界面
 * 
 * @author ShanZha
 * 
 */
public class SubjectDetailActivity extends BaseActivity implements
		IFloatingItemClick, IWeiboHandler.Response {

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

	/** 分享选择代理 **/
	private FloatingTranslucentProxy mFloatingShareProxy;
	private List<String> mShareList;

	// private IWeiboAPI mSinaWeiboApi;
	private SinaWeiboShareProxy mSinaShareProxy;

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
		this.prepareShare();
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

	private void prepareShare() {
		mShareList = new ArrayList<String>();
		String[] arrays = this.getResources().getStringArray(
				R.array.share_array);
		mShareList.addAll(Arrays.asList(arrays));
		mFloatingShareProxy = new FloatingTranslucentProxy(this, 0);
		mFloatingShareProxy.setFloatingItemClickListener(this);
		mFloatingShareProxy.setDatas(mShareList);

		IWeiboAPI mSinaWeiboApi = WeiboSDK.createWeiboAPI(this,
				GlobalConfig.SINA_WEIBO_APP_KEY);
		mSinaShareProxy = new SinaWeiboShareProxy(this, mSinaWeiboApi);
	}

	private String buildShareContent() {
		StringBuffer sb = new StringBuffer();
		sb.append(mSubject.getName());
		sb.append("\n");
		String address = mSubject.getAddress();
		if (!TextUtils.isEmpty(address)) {
			sb.append(this.getString(R.string.subject_addr));
			sb.append(address);
			sb.append("\n");
		}
		String tel = mSubject.getTel();
		if (!TextUtils.isEmpty(tel)) {
			sb.append(this.getString(R.string.subject_tel));
			sb.append(tel);
		}
		return sb.toString();
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
					REQ_LOGIN_CODE_COLLECTION);
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
		if (null != mFloatingShareProxy) {
			mFloatingShareProxy.showLocation(view, Gravity.CENTER, 0, 0);
		}
	}

	public void onClickReview(View view) {// 评价

	}

	public void onClickUploadPic(View view) {// 上传图片

	}

	public void onClickError(View view) {// 报错

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		mSinaShareProxy.setResponseListener(intent, this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		if (REQ_LOGIN_CODE_COLLECTION == requestCode) {
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

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		this.showToast("pos = " + pos + " name = " + key);
		switch (pos) {
		case POS_SINA_WEIBO:
			if (!mSinaShareProxy.isInstall()) {
				this.showToast(this.getString(R.string.share_sina_uninstall));
				return;
			}
			if (!mSinaShareProxy.isAppSupport()) {
				this.showToast(this
						.getString(R.string.share_sina_version_mismatch));
				return;
			}
			if (((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
				if (mSinaShareProxy.isSessionValid()) {
					mSinaShareProxy.share(this.buildShareContent());
				} else {

				}
			} else {
				this.startActivityForResult(new Intent(this,
						LoginActivity.class), REQ_LOGIN_CODE_SINA_WEIBO);
			}
			break;
		case POS_TENCENT_WEIBO:
			this.onShareTencent(GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO);
			break;
		case POS_TENCENT_QZONE:
			this.onShareTencent(GlobalConfig.IntentKey.INDEX_TENCENT_QZONE);
			break;
		case POS_TENCENT_QQ:
			this.onShareTencent(GlobalConfig.IntentKey.INDEX_TENCENT_QQ);
			break;
		case POS_SMS:
			try {
				Uri smsToUri = Uri.parse("smsto:");
				Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
				sendIntent.putExtra("sms_body", this.buildShareContent());
				sendIntent.setType("vnd.android-dir/mms-sms");
				this.startActivityForResult(sendIntent, REQ_LOGIN_CODE_SMS);
			} catch (Exception e) {
				this.showToast(this.getString(R.string.share_sms_unsupport));
			}
			break;
		case POS_EMAIL:
			try {
				Intent data = new Intent(Intent.ACTION_SENDTO);
				data.setData(Uri.parse("mailto:"));
				data.putExtra(Intent.EXTRA_SUBJECT,
						this.getString(R.string.share_email_subject));
				data.putExtra(Intent.EXTRA_TEXT, this.buildShareContent());
				this.startActivityForResult(data, REQ_LOGIN_CODE_EMAIL);
			} catch (Exception e) {
				this.showToast(this.getString(R.string.share_email_unsupport));
			}
			break;
		}
	}

	@Override
	public void onResponse(BaseResponse baseResp) {// 新浪微博分享回调
		// TODO Auto-generated method stub
		switch (baseResp.errCode) {
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_OK:
			this.showToast(this.getString(R.string.share_success));
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_CANCEL:
			this.showToast(this.getString(R.string.share_cancel));
			break;
		case com.sina.weibo.sdk.constant.Constants.ErrorCode.ERR_FAIL:
			this.showToast(this.getString(R.string.share_fail)
					+ baseResp.errMsg);
			break;
		}
	}

	private void onShareTencent(int index) {
		Intent intent = new Intent(this, ShareActivity.class);
		intent.putExtra("shareIndex", index);
		intent.putExtra("shareContent", this.buildShareContent());
		this.startActivity(intent);
	}

	/** 收藏 **/
	private static final int REQ_LOGIN_CODE_COLLECTION = 1;
	/** 新浪微博 **/
	private static final int REQ_LOGIN_CODE_SINA_WEIBO = 2;
	private static final int REQ_LOGIN_CODE_SMS = 3;
	private static final int REQ_LOGIN_CODE_EMAIL = 4;
	private static final int POS_SINA_WEIBO = 0;
	private static final int POS_TENCENT_WEIBO = 1;
	private static final int POS_TENCENT_QZONE = 2;
	private static final int POS_TENCENT_QQ = 3;
	private static final int POS_SMS = 4;
	private static final int POS_EMAIL = 5;
	private static final String SORT_1 = "sort1";
	private static final String SORT_2 = "sort2";
	private static final String SORT_3 = "sort3";
	private static final String SORT_4 = "sort4";

}
