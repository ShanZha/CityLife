package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAlbum;
import com.andrnes.modoer.ModoerFavorites;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerPictures;
import com.andrnes.modoer.ModoerReview;
import com.andrnes.modoer.ModoerReviewOpt;
import com.andrnes.modoer.ModoerSubject;
import com.andrnes.modoer.ModoerSubjectlog;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.FloatingTranslucentProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.fourkkm.citylife.wxapi.WXEntryActivity;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.observer.model.Param;

/**
 * 商店类别详情界面
 * 
 * @author ShanZha
 * 
 */
public class SubjectDetailActivity extends BaseUploadPicActivity implements
		IFloatingItemClick {

	private static final String TAG = "SubjectDetailActivity";
	private ModoerSubject mSubject;
	private RelativeLayout mRlAddress, mRlTel, mRlDesc;
	private TextView mTvTitle, mTvShopName, mTvThumbnailCount, mTvAveragePer,
			mTvAddress, mTvTel, mTvDesc, mTvReviewCount, mTvReviewUserName,
			mTvReviewAveragePer, mTvReviewContent, mTvCompositeScore;
	/** 4项评分 **/
	private TextView mTvSort1, mTvSort2, mTvSort3, mTvSort4;
	private ImageView mIvThumbnail, mIvDividerTel, mIvDividerDesc, mIvAttr1,
			mIvAttr2, mIvAttr3, mIvAttr4, mIvAttr5;
	private RatingBar mRatingBar;
	private RatingBar mRatingBarReview;

	private ProgressBar mProBarReview;
	private RelativeLayout mRlReview;

	private RelativeLayout mRlReviewOpt;
	private ProgressBar mProBarReviewOpt;

	private LinearLayout mLlCollectLoading, mLlUpload;

	private ImageButton mBtnCollection;

	/** 分享、报错代理 **/
	private FloatingTranslucentProxy mFloatingShareProxy, mFloatingErrorProxy;
	private List<String> mShareList, mErrorList;

	// private IWeiboAPI mSinaWeiboApi;

	private ProgressDialogProxy mDialogProxy;
	private ModoerAlbum mAlbum;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.subject_detail);

		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mLlCollectLoading = (LinearLayout) this
				.findViewById(R.id.titlebar_back_right_ll_probar);
		mBtnCollection = (ImageButton) this
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
		mTvDesc = (TextView) this
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
		mIvAttr1 = (ImageView) this
				.findViewById(R.id.subject_detail_item_iv_attr1);
		mIvAttr2 = (ImageView) this
				.findViewById(R.id.subject_detail_item_iv_attr2);
		mIvAttr3 = (ImageView) this
				.findViewById(R.id.subject_detail_item_iv_attr3);
		mIvAttr4 = (ImageView) this
				.findViewById(R.id.subject_detail_item_iv_attr4);
		mIvAttr5 = (ImageView) this
				.findViewById(R.id.subject_detail_item_iv_attr5);
		mIvDividerTel = (ImageView) this
				.findViewById(R.id.subject_detail_iv_divider_tel);
		mIvDividerDesc = (ImageView) this
				.findViewById(R.id.subject_detail_iv_divider_desc);

		mRlAddress = (RelativeLayout) this
				.findViewById(R.id.subject_detail_rl_address);
		mRlTel = (RelativeLayout) this.findViewById(R.id.subject_detail_rl_tel);
		mRlDesc = (RelativeLayout) this
				.findViewById(R.id.subject_detail_rl_impression);

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

		mLlUpload = (LinearLayout) this
				.findViewById(R.id.subject_detail_ll_upload);
		mGvPics = (GridView) this.findViewById(R.id.floating_upload_gv_pics);

	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		mIsAutoLogin = false;
		mIsNeedUploadPics = true;
		super.prepareDatas();
		this.prepareShare();
		this.prepareError();
		mTvTitle.setText(this.getString(R.string.subject_detail));
		mBtnCollection
				.setBackgroundResource(R.drawable.subject_detail_collection_selector);
		mDialogProxy = new ProgressDialogProxy(this);
		mSubject = (ModoerSubject) this.getIntent().getSerializableExtra(
				"ModoerSubject");
		if (null == mSubject) {
			Log.e(TAG, "shan-->mSubject is null");
			return;
		}

		mTvShopName.setText(mSubject.getName());
		mTvAveragePer.setText(this.getString(R.string.average_per)
				+ mSubject.getAvgprice());
		String addr = mSubject.getAddress();
		String tel = mSubject.getTel();
		String desc = mSubject.getDescription();
		this.setTxtValue(mRlAddress, mTvAddress, addr);
		this.setTxtValue(mRlTel, mTvTel, tel);
		this.setTxtValue(mRlDesc, mTvDesc, desc);
		if (TextUtils.isEmpty(desc)) {
			mIvDividerDesc.setVisibility(View.GONE);
		}
		if ((TextUtils.isEmpty(tel) || "0".equals(tel))
				|| TextUtils.isEmpty(addr) || "0".equals(addr)) {
			mIvDividerTel.setVisibility(View.GONE);
		}

		List<String> attrList = CommonUtil
				.getSubjectAttrIconList(mSubject.getCShopattsReplace(),
						mSubject.getCShopatts2Replace());
		this.setAttrIcons(attrList);

		mTvReviewCount.setText(this.getString(R.string.review) + "("
				+ mSubject.getReviews() + ")");
		mTvCompositeScore.setText(this.getString(R.string.composite_score)
				+ mSubject.getAvgsort());
		mRatingBar.setProgress((int) mSubject.getAvgsort());
		String thumbnailUrl = GlobalConfig.URL_PIC + mSubject.getThumb();
		Bitmap mBmDefault = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.list_thumb);
		AsyncImageLoader.getImageLoad(this).showPic(thumbnailUrl, mIvThumbnail,
				mBmDefault);
		int picNum = mSubject.getPictures();
		if (picNum > 0) {
			mTvThumbnailCount.setText(picNum + "");
			mTvThumbnailCount.setVisibility(View.VISIBLE);
		} else {
			mTvThumbnailCount.setVisibility(View.GONE);
		}
		this.showReviewLoading();
		this.fetchOneBestReview(mSubject.getId());

		this.showReviewOptLoading();
		this.fetchReviewOpt();

	}

	/**
	 * 获取各类评分
	 */
	private void fetchReviewOpt() {
		try {
			String selectCode = "from com.andrnes.modoer.ModoerReviewOpt mro where mro.enable = 1 and mro.gid.id = "
					+ mSubject.getPid().getReviewOptGid().getId();
			this.getStoreOperation().findAll(selectCode,
					new HashMap<String, Object>(), true, new ModoerReviewOpt(),
					new Param(this.hashCode(), GlobalConfig.URL_CONN));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取一条此主题的好评论
	 * 
	 * @param subjectId
	 */
	private void fetchOneBestReview(int subjectId) {
		String selectCode = "from com.andrnes.modoer.ModoerReview mr where mr.sid.id = "
				+ subjectId + " and mr.status = 1 and mr.best = 2";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FIND_REVIEW_BY_SUBJECT);
		this.getStoreOperation().find(selectCode,
				new HashMap<String, Object>(), true, new ModoerReview(), param);
	}

	/**
	 * 获取此主题相册
	 * 
	 * @param subjectId
	 */
	private void fetchModoerAlbum(int subjectId) {
		String selectCode = "from com.andrnes.modoer.ModoerAlbum ma where ma.sid.id = "
				+ subjectId;
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FIND_ALBUM_BY_SUBJECT);
		this.getStoreOperation().find(selectCode,
				new HashMap<String, Object>(), true, new ModoerAlbum(), param);
	}

	private void prepareShare() {
		mShareList = new ArrayList<String>();
		String[] arrays = this.getResources().getStringArray(
				R.array.share_array);
		mShareList.addAll(Arrays.asList(arrays));
		mFloatingShareProxy = new FloatingTranslucentProxy(this,
				GlobalConfig.FloatingType.TYPE_SHARE);
		mFloatingShareProxy.setFloatingItemClickListener(this);
		mFloatingShareProxy.setDatas(mShareList);

	}

	private void prepareError() {
		mErrorList = new ArrayList<String>();
		String[] arrays = this.getResources().getStringArray(
				R.array.error_array);
		mErrorList.addAll(Arrays.asList(arrays));
		mFloatingErrorProxy = new FloatingTranslucentProxy(this,
				GlobalConfig.FloatingType.TYPE_ERROR);
		mFloatingErrorProxy.setFloatingItemClickListener(this);
		mFloatingErrorProxy.setDatas(mErrorList);
	}

	private void setTxtValue(View parent, TextView tv, String content) {
		if (TextUtils.isEmpty(content) || content.equals("0")) {
			parent.setVisibility(View.GONE);
		} else {
			tv.setText(content);
		}
	}

	private void setAttrIcons(List<String> attrList) {
		if (null == attrList || attrList.size() == 0) {
			mIvAttr1.setVisibility(View.GONE);
			mIvAttr2.setVisibility(View.GONE);
			mIvAttr3.setVisibility(View.GONE);
			mIvAttr4.setVisibility(View.GONE);
			mIvAttr5.setVisibility(View.GONE);
			return;
		}
		for (int i = 0; i < attrList.size(); i++) {
			String icon = attrList.get(i);
			String url = GlobalConfig.URL_ATTR_PIC + icon;
			switch (i) {
			case 0:
				mIvAttr1.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(this)
						.showPic(url, mIvAttr1, null);
				break;
			case 1:
				mIvAttr2.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(this)
						.showPic(url, mIvAttr2, null);
				break;
			case 2:
				mIvAttr3.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(this)
						.showPic(url, mIvAttr3, null);
				break;
			case 3:
				mIvAttr4.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(this)
						.showPic(url, mIvAttr4, null);
				break;
			case 4:
				mIvAttr5.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(this)
						.showPic(url, mIvAttr5, null);
				break;
			}
		}
	}

	private String buildShareContent(int index) {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getString(R.string.subject_share_tip));
		sb.append(mSubject.getName());
		if (GlobalConfig.IntentKey.INDEX_TENCENT_QQ == index) {
			return sb.toString();
		}
		sb.append("\n");
		String address = mSubject.getAddress();
		if (!TextUtils.isEmpty(address) && !"0".equals(address)) {
			sb.append(this.getString(R.string.subject_addr_detail));
			sb.append(address);
			sb.append("\n");
		}
		String tel = mSubject.getTel();
		if (!TextUtils.isEmpty(tel) && !"0".equals(tel)) {
			sb.append(this.getString(R.string.subject_tel_detail));
			sb.append(tel);
			sb.append("\n");
		}
		sb.append(GlobalConfig.URL_PIC);
		sb.append("item-");
		sb.append(mSubject.getId());
		sb.append(".html");
		return sb.toString();
	}

	/**
	 * 形如http://www.40000km.com.cn/item-subject.id.html
	 * 
	 * @param subjectId
	 */
	private String buildSubjectUrlById(int subjectId) {
		StringBuilder sb = new StringBuilder();

		return sb.toString();
	}

	private void showLoadingCollection() {
		mLlCollectLoading.setVisibility(View.VISIBLE);
		mBtnCollection.setVisibility(View.GONE);
	}

	private void hideLoadingCollection() {
		mLlCollectLoading.setVisibility(View.GONE);
		mBtnCollection.setVisibility(View.VISIBLE);
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
		this.showLoadingCollection();
		// this.showToast(this.getString(R.string.subject_collection_start));
		ModoerFavorites favorite = new ModoerFavorites();
		favorite.setAddtime((int) CommonUtil.getCurrTimeByPHP());
		favorite.setSid(mSubject);
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		favorite.setUid(member);
		favorite.setUsername(member.getUsername());
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SUBJECT_CONNLECTION);
		this.getStoreOperation().saveOrUpdate(favorite, param);
	}

	private void onSaveModoerAlbum() {
		if (null == mAlbum) {
			return;
		}
		// Step 3：保存ModoerPictures(排除第一项)
		List<Object> objs = new ArrayList<Object>();
		for (int i = 1; i < mPicList.size(); i++) {
			ModoerPictures pic = mPicList.get(i);
			pic.setAlbumid(mAlbum);
			pic.setSid(mSubject);
			pic.setStatus(1);
			pic.setUrl("");
			objs.add(pic);
		}
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_PICS);
		this.getStoreOperation().saveOrUpdateArray(objs, param);
	}

	public void onClickBack(View view) {// 返回
		this.finish();
	}

	public void onClickRight(View view) {// 收藏
		if (this.isUploading()) {
			return;
		}
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE_COLLECTION);
			return;
		}
		this.onCollection();
	}

	public void onClickThumbnail(View view) {// 缩略图
		if (this.isUploading()) {
			return;
		}
		int picNum = mSubject.getPictures();
		if (picNum < 1) {
			return;
		}
		Intent intent = new Intent(this, AlbumActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.ALBUM_SUBJECT);
		intent.putExtra("subjectId", mSubject.getId());
		intent.putExtra("subjectName", mSubject.getName());
		intent.putExtra("subjectThumbnailCount", mSubject.getPictures());
		this.startActivity(intent);
	}

	public void onClickAddress(View view) {// 地址
		if (this.isUploading()) {
			return;
		}
		Intent intent = new Intent(this, SubjectMapMarkerActivity.class);
		intent.putExtra("ModoerSubject", mSubject);
		this.startActivity(intent);
	}

	public void onClickTel(View view) {// 电话
		if (this.isUploading()) {
			return;
		}
		try {
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
					+ mSubject.getTel()));
			this.startActivity(intent);
		} catch (Exception e) {
			this.showToast(this.getString(R.string.share_tel_unsupport));
		}
	}

	public void onClickReviewContent(View view) {// 评价内容
		if (this.isUploading()) {
			return;
		}
		Intent intent = new Intent(this, ReviewListActivity.class);
		intent.putExtra("ModoerSubject", mSubject);
		this.startActivity(intent);
	}

	public void onClickShareTo(View view) {// 分享到
		if (this.isUploading()) {
			return;
		}
		if (null != mFloatingShareProxy) {
			mFloatingShareProxy.showLocation(view, Gravity.CENTER, 0, 0);
		}
	}

	public void onClickReview(View view) {// 评价
		if (this.isUploading()) {
			return;
		}
		Intent intent = new Intent(this, ReviewAddActivity.class);
		intent.putExtra("ModoerSubject", mSubject);
		this.startActivity(intent);
	}

	public void onClickUploadPic(View view) {// 上传图片
		if (this.isUploading()) {
			return;
		}
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE_UPLOAD);
			return;
		} else {
			this.prepare();
		}
		this.onUploadShow();
	}

	public void onClickError(View view) {// 报错
		if (this.isUploading()) {
			return;
		}
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE_ERROR);
			return;
		}
		if (null != mFloatingErrorProxy) {
			mFloatingErrorProxy.showLocation(view, Gravity.CENTER, 0, 0);
		}
	}

	public void onClickClose(View view) {
		this.onUploadHide();
	}

	public void onClickAdd(View view) {
		if (!this.isUploadFinished()) {
			this.showToast(this.getString(R.string.subject_upload_pic_unfinish));
			return;
		}
		if (this.getPicCount() > 0) {
			if (null != mDialogProxy) {
				mDialogProxy.showDialog();
			}
			if (null == mAlbum) {
				if (null != mSubject) {
					this.fetchModoerAlbum(mSubject.getId());
				}
			} else {
				this.onSaveModoerAlbum();
			}
		}
	}

	private void onUploadShow() {
		mLlUpload.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.ani_common_bottombar_in));
		mLlUpload.setVisibility(View.VISIBLE);
	}

	private void onUploadHide() {
		mLlUpload.startAnimation(AnimationUtils.loadAnimation(this,
				R.anim.ani_common_bottombar_out));
		mLlUpload.setVisibility(View.GONE);
	}

	private boolean isUploading() {
		return mLlUpload.getVisibility() == View.VISIBLE;
	}

	private void onShare(int pos) {
		switch (pos) {
		case POS_SINA_WEIBO:
			this.share(GlobalConfig.IntentKey.INDEX_SINA_WEIBO);
			break;
		case POS_TENCENT_WEIBO:
			this.share(GlobalConfig.IntentKey.INDEX_TENCENT_WEIBO);
			break;
		case POS_TENCENT_QZONE:
			this.share(GlobalConfig.IntentKey.INDEX_TENCENT_QZONE);
			break;
		case POS_TENCENT_QQ:
			this.share(GlobalConfig.IntentKey.INDEX_TENCENT_QQ);
			break;
		case POS_WEIXIN_FRIENDS:
			this.shareWeixin(GlobalConfig.IntentKey.INDEX_WEIXIN_FRIENDS);
			break;
		case POS_WEIXIN_TIMELINE:
			this.shareWeixin(GlobalConfig.IntentKey.INDEX_WEIXIN_TIMELINE);
			break;
		case POS_SMS:
			try {
				Uri smsToUri = Uri.parse("smsto:");
				Intent sendIntent = new Intent(Intent.ACTION_VIEW, smsToUri);
				sendIntent.putExtra("sms_body", this.buildShareContent(-1));
				sendIntent.setType("vnd.android-dir/mms-sms");
				this.startActivity(sendIntent);
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
				data.putExtra(Intent.EXTRA_TEXT, this.buildShareContent(-1));
				this.startActivity(data);
			} catch (Exception e) {
				this.showToast(this.getString(R.string.share_email_unsupport));
			}
			break;
		}
	}

	private void share(int index) {
		Intent intent = new Intent(this, ShareActivity.class);
		intent.putExtra("shareIndex", index);
		intent.putExtra("subjectId", (null == mSubject ? 0 : mSubject.getId()));
		intent.putExtra("shareContent", this.buildShareContent(index));
		this.startActivity(intent);
	}

	private void shareWeixin(int index) {
		Intent intent = new Intent(this, WXEntryActivity.class);
		intent.putExtra("shareIndex", index);
		intent.putExtra("shareContent", this.buildShareContent(index));
		this.startActivity(intent);
	}

	private void onError(int pos, String info) {
		if (pos == POS_LOCATION_ERROR) {// 地图报错
			Intent intent = new Intent(this, MapMarkerActivity.class);
			intent.putExtra("operator", GlobalConfig.IntentKey.MAP_POINT_ERROR);
			double[] locArray = new double[2];
			locArray[0] = mSubject.getMapLat();
			locArray[1] = mSubject.getMapLng();
			intent.putExtra("latlng", locArray);
			this.startActivityForResult(intent, REQ_LOGIN_CODE_ERROR_MAP);
		} else {
			this.onSaveError(info);
		}
	}

	/**
	 * 报错
	 * 
	 * @param info
	 */
	private void onSaveError(String info) {
		ModoerSubjectlog log = new ModoerSubjectlog();
		log.setSid(mSubject);
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		log.setUid(member);
		log.setUsername(member.getUsername());
		log.setPosttime((int) CommonUtil.getCurrTimeByPHP());
		log.setUpcontent(info);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_ERROR);
		this.getStoreOperation().saveOrUpdate(log, param);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case REQ_LOGIN_CODE_COLLECTION:
			this.onCollection();
			break;
		case REQ_LOGIN_CODE_UPLOAD:
			this.prepare();
			this.onUploadShow();
			break;
		case REQ_LOGIN_CODE_ERROR:
			if (null != mFloatingErrorProxy) {
				mFloatingErrorProxy.showLocation(mRlAddress, Gravity.CENTER, 0,
						0);
			}
			break;
		case REQ_LOGIN_CODE_ERROR_MAP:
			double lat = data.getDoubleExtra("lat", 0);
			double lng = data.getDoubleExtra("lng", 0);
			this.onSaveError(lng + "," + lat);
			break;
		case REQ_LOGIN_CODE_SINA_WEIBO:
			this.onShare(POS_SINA_WEIBO);
			break;
		case REQ_LOGIN_CODE_QQ:
			this.onShare(POS_TENCENT_QQ);
			break;
		case REQ_LOGIN_CODE_QZONE:
			this.onShare(POS_TENCENT_QZONE);
			break;
		case REQ_LOGIN_CODE_TENCEN_WEIBO:
			this.onShare(POS_TENCENT_WEIBO);
			break;
		case REQ_LOGIN_CODE_WEIXIN_FRIENDS:
			this.onShare(POS_WEIXIN_FRIENDS);
			break;
		case REQ_LOGIN_CODE_WEIXIN_TIMELINE:
			this.onShare(POS_WEIXIN_TIMELINE);
			break;
		}

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SUBJECT_CONNLECTION == operator) {
			SqliteUtil.getInstance(this.getApplicationContext())
					.deleteByClassName(ModoerFavorites.class.getName());
			this.showToast(this.getString(R.string.subject_collection_success));
			this.hideLoadingCollection();
		} else if (GlobalConfig.Operator.OPERATION_SAVE_ERROR == operator) {
			this.showToast(this.getString(R.string.subject_error_success));
		}

	}

	@Override
	public void onSuccessSaveOrUpdateArray(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdateArray(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_PICS == operator) {
			if (null != mDialogProxy) {
				mDialogProxy.hideDialog();
			}
			this.showToast(this.getString(R.string.commit_success));
			this.onUploadHide();
		}
	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFind(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FIND_ALBUM_BY_SUBJECT == operator) {
			mAlbum = (ModoerAlbum) out.getResult();
			this.onSaveModoerAlbum();
		} else if (GlobalConfig.Operator.OPERATION_FIND_REVIEW_BY_SUBJECT == operator) {
			ModoerReview review = (ModoerReview) out.getResult();
			if (null != review) {
				mTvReviewAveragePer.setText(this
						.getString(R.string.average_per) + review.getPrice());

				mTvReviewContent.setText(review.getContent());
				ModoerMembers member = review.getUid();
				if (null != member && member.getId() != 0) {
					mTvReviewUserName.setText(member.getUsername());
				}
				int average = (review.getSort1() + review.getSort2()
						+ review.getSort3() + review.getSort4()) / 4;
				mRatingBarReview.setProgress(average);
				this.hideReviewLoading();
			} else {
				mRlReview.setVisibility(View.GONE);
				mProBarReview.setVisibility(View.GONE);
			}
		}

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
			// this.showToast(this.getString(R.string.subject_collection_fail));
			this.hideLoadingCollection();
		} else if (GlobalConfig.Operator.OPERATION_FIND_ALBUM_BY_SUBJECT == operator) {
			if (null != mDialogProxy) {
				mDialogProxy.hideDialog();
			}
			this.showToast(this.getString(R.string.commit_fail));
		} else if (GlobalConfig.Operator.OPERATION_SAVE_PICS == operator) {
			if (null != mDialogProxy) {
				mDialogProxy.hideDialog();
			}
			this.showToast(this.getString(R.string.commit_fail));
		} else if (GlobalConfig.Operator.OPERATION_SAVE_ERROR == operator) {
			this.showToast(this.getString(R.string.subject_error_fail));
		}
	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		if (GlobalConfig.FloatingType.TYPE_ERROR == type) {
			this.onError(pos, key);
		} else if (GlobalConfig.FloatingType.TYPE_SHARE == type) {
			if (POS_SMS == pos || POS_EMAIL == pos) {
				this.onShare(pos);
			} else {
				if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
					Intent intent = new Intent(this, LoginActivity.class);
					switch (pos) {
					case POS_SINA_WEIBO:
						this.startActivityForResult(intent,
								REQ_LOGIN_CODE_SINA_WEIBO);
						break;
					case POS_TENCENT_QQ:
						this.startActivityForResult(intent, REQ_LOGIN_CODE_QQ);
						break;
					case POS_TENCENT_QZONE:
						this.startActivityForResult(intent,
								REQ_LOGIN_CODE_QZONE);
						break;
					case POS_TENCENT_WEIBO:
						this.startActivityForResult(intent,
								REQ_LOGIN_CODE_TENCEN_WEIBO);
						break;
					case POS_WEIXIN_FRIENDS:
						this.startActivityForResult(intent,
								REQ_LOGIN_CODE_WEIXIN_FRIENDS);
						break;
					case POS_WEIXIN_TIMELINE:
						this.startActivityForResult(intent,
								REQ_LOGIN_CODE_WEIXIN_TIMELINE);
						break;
					}
				} else {
					this.onShare(pos);
				}
			}
		} else {
			super.onFloatingItemClick(pos, key, type);
		}

	}

	/** 收藏 **/
	private static final int REQ_LOGIN_CODE_COLLECTION = 4;
	/** 上传 **/
	private static final int REQ_LOGIN_CODE_UPLOAD = 5;
	/** 报错 **/
	private static final int REQ_LOGIN_CODE_ERROR = 6;
	/** 地图选点报错 **/
	private static final int REQ_LOGIN_CODE_ERROR_MAP = 7;
	/** 新浪微博 **/
	private static final int REQ_LOGIN_CODE_SINA_WEIBO = 8;
	/** QQ **/
	private static final int REQ_LOGIN_CODE_QQ = 9;
	/** QZone **/
	private static final int REQ_LOGIN_CODE_QZONE = 10;
	/** 腾讯微博 **/
	private static final int REQ_LOGIN_CODE_TENCEN_WEIBO = 11;
	/** 微信好友 **/
	private static final int REQ_LOGIN_CODE_WEIXIN_FRIENDS = 12;
	/** 微信朋友圈 **/
	private static final int REQ_LOGIN_CODE_WEIXIN_TIMELINE = 13;
	/** Share POS(Begin) **/
	private static final int POS_SINA_WEIBO = 0;
	private static final int POS_TENCENT_WEIBO = 1;
	private static final int POS_TENCENT_QZONE = 2;
	private static final int POS_TENCENT_QQ = 3;
	private static final int POS_WEIXIN_FRIENDS = 4;
	private static final int POS_WEIXIN_TIMELINE = 5;
	private static final int POS_SMS = 6;
	private static final int POS_EMAIL = 7;
	/** Share POS(End) **/
	/** Error POS(Begin) **/
	private static final int POS_SUBJECT_CLOSED = 0;
	private static final int POS_LOCATION_ERROR = 1;
	private static final int POS_SUBJECT_INFO_ERROR = 2;
	private static final int POS_SUBJECT_REPEAT = 3;
	/** Error POS(End) **/
	private static final String SORT_1 = "sort1";
	private static final String SORT_2 = "sort2";
	private static final String SORT_3 = "sort3";
	private static final String SORT_4 = "sort4";

}
