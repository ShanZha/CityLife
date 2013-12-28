package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TableLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerReview;
import com.andrnes.modoer.ModoerReviewOpt;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.BaseActivity;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

/**
 * 评论添加
 * 
 * @author ShanZha
 * 
 */
public class ReviewAddActivity extends BaseActivity implements
		OnRatingBarChangeListener {

	private static final String TAG = "ReviewAddActivity";
	private static final String SORT_1 = "sort1";
	private static final String SORT_2 = "sort2";
	private static final String SORT_3 = "sort3";
	private static final String SORT_4 = "sort4";
	private static final int REQ_LOGIN_CODE = 1;
	private TextView mTvTitle, mTvSubject, mTvSort1, mTvSort2, mTvSort3,
			mTvSort4, mTvSort1Level, mTvSort2Level, mTvSort3Level,
			mTvSort4Level;
	private EditText mEtPerAver, mEtContent;
	private RatingBar mRatingBarSort1, mRatingBarSort2, mRatingBarSort3,
			mRatingBarSort4;

	private LinearLayout mLlSort, mLlSortLoading, mLlTagsLoading;
	private TableLayout mTlTag;

	private ProgressDialogProxy mDialogProxy;

	private ModoerSubject mSubject;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.review_add);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvSubject = (TextView) this.findViewById(R.id.review_add_tv_title);
		mTvSort1 = (TextView) this.findViewById(R.id.review_add_tv_sort1);
		mTvSort2 = (TextView) this.findViewById(R.id.review_add_tv_sort2);
		mTvSort3 = (TextView) this.findViewById(R.id.review_add_tv_sort3);
		mTvSort4 = (TextView) this.findViewById(R.id.review_add_tv_sort4);
		mTvSort1Level = (TextView) this
				.findViewById(R.id.review_add_tv_sort1_level);
		mTvSort2Level = (TextView) this
				.findViewById(R.id.review_add_tv_sort2_level);
		mTvSort3Level = (TextView) this
				.findViewById(R.id.review_add_tv_sort3_level);
		mTvSort4Level = (TextView) this
				.findViewById(R.id.review_add_tv_sort4_level);

		mRatingBarSort1 = (RatingBar) this
				.findViewById(R.id.review_add_ratingBar_sort1);
		mRatingBarSort2 = (RatingBar) this
				.findViewById(R.id.review_add_ratingBar_sort2);
		mRatingBarSort3 = (RatingBar) this
				.findViewById(R.id.review_add_ratingBar_sort3);
		mRatingBarSort4 = (RatingBar) this
				.findViewById(R.id.review_add_ratingBar_sort4);

		mEtPerAver = (EditText) this
				.findViewById(R.id.review_add_et_per_average);
		mEtContent = (EditText) this.findViewById(R.id.review_add_et_content);

		mLlSort = (LinearLayout) this.findViewById(R.id.review_add_ll_sort);
		mLlSortLoading = (LinearLayout) this
				.findViewById(R.id.review_add_ll_loading_sort);
		mTlTag = (TableLayout) this.findViewById(R.id.review_add_tl_tags);
		mLlTagsLoading = (LinearLayout) this
				.findViewById(R.id.review_add_ll_loading_tags);

		mRatingBarSort1.setOnRatingBarChangeListener(this);
		mRatingBarSort2.setOnRatingBarChangeListener(this);
		mRatingBarSort3.setOnRatingBarChangeListener(this);
		mRatingBarSort4.setOnRatingBarChangeListener(this);

	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		// 如果没有登录，先登录
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE);
			return;
		}
		this.prepare();
	}

	private void prepare() {
		mTvTitle.setText(this.getString(R.string.subject_review));
		mSubject = (ModoerSubject) this.getIntent().getSerializableExtra(
				"ModoerSubject");
		if (null == mSubject) {
			Log.e(TAG, "shan-->ModoerSubject is null");
			return;
		}
		mTvSubject.setText(mSubject.getName());

		this.showSortLoading();
		this.fetchReviewOpt();
		this.showTagsLoading();

		mDialogProxy = new ProgressDialogProxy(this);
	}

	private void showSortLoading() {
		mLlSort.setVisibility(View.GONE);
		mLlSortLoading.setVisibility(View.VISIBLE);
	}

	private void hideSortLoading() {
		mLlSort.setVisibility(View.VISIBLE);
		mLlSortLoading.setVisibility(View.GONE);
	}

	private void showTagsLoading() {
		mTlTag.setVisibility(View.GONE);
		mLlTagsLoading.setVisibility(View.VISIBLE);
	}

	private void hideTagsLoading() {
		mTlTag.setVisibility(View.VISIBLE);
		mLlTagsLoading.setVisibility(View.GONE);
	}

	/**
	 * 获取各类评分
	 */
	private void fetchReviewOpt() {
		String selectCode = "from com.andrnes.modoer.ModoerReviewOpt mro where mro.enable = 1 and mro.gid.id = "
				+ mSubject.getPid().getReviewOptGid().getId();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_REVIEW_OPT);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerReviewOpt(),
				param);
	}

	private void fetctReviewTags() {
		// String selectCode =
		// "from com.andrnes.modoer.ModoerReviewOpt mro where mro.enable = 1 and mro.gid.id = "
		// + mSubject.getPid().getReviewOptGid().getId();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_REVIEW_TAGS);
		// this.getStoreOperation().findAll(selectCode,
		// new HashMap<String, Object>(), true, new ModoerReviewOpt(),
		// new Param(this.hashCode(), GlobalConfig.URL_CONN));
	}

	private boolean validate() {
		int sort1 = mRatingBarSort1.getProgress();
		int sort2 = mRatingBarSort2.getProgress();
		int sort3 = mRatingBarSort3.getProgress();
		int sort4 = mRatingBarSort4.getProgress();
		if (0 == sort1 || 0 == sort2 || 0 == sort3 || 0 == sort4) {
			this.showToast(this.getString(R.string.review_sort_null));
			return false;
		}

		String perAver = mEtPerAver.getText().toString().trim();
		if (TextUtils.isEmpty(perAver)) {
			this.showToast(this.getString(R.string.review_per_aver));
			return false;
		}
		return true;
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickCommit(View view) {
		if (!this.validate()) {
			return;
		}
		ModoerReview review = new ModoerReview();
		review.setIdtype("item_subject");
		review.setContent(mEtContent.getText().toString().trim());
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		review.setUid(member);
		review.setUsername(member.getUsername());
		review.setCityId(((CoreApp) AppUtils.getBaseApp(this)).getCurrArea());
		review.setSubject(mSubject.getName());
		review.setStatus(1);

		int sort1 = mRatingBarSort1.getProgress();
		int sort2 = mRatingBarSort2.getProgress();
		int sort3 = mRatingBarSort3.getProgress();
		int sort4 = mRatingBarSort4.getProgress();
		review.setSort1(sort1);
		review.setSort2(sort2);
		review.setSort3(sort3);
		review.setSort4(sort4);
		review.setPrice(Integer.valueOf(mEtPerAver.getText().toString().trim()));
		int averSort = (sort1 + sort2 + sort3 + sort4) / 4;
		if (averSort >= 3) {// 评价分大于等于3分，则好评
			review.setBest(2);
		} else if (averSort > 1) {// 一般
			review.setBest(0);
		} else {// 差评
			review.setBest(1);
		}
		review.setPosttime((int) (System.currentTimeMillis() / 1000));

		mDialogProxy.showDialog();
		// 标签组？？？格式暂时不知道
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_REVIEW);
		this.getStoreOperation().saveOrUpdate(review, param);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode && REQ_LOGIN_CODE == requestCode) {
			this.prepare();
			return;
		}
		this.finish();
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);

		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan-->result is null");
			this.hideSortLoading();
			return;
		}
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_REVIEW_OPT == operator) {
			// 根据Catology获取评分的各个类型以及名字
			for (int i = 0; i < results.size(); i++) {
				ModoerReviewOpt reviewOpt = (ModoerReviewOpt) results.get(i);
				String flag = reviewOpt.getFlag();
				String name = reviewOpt.getName();
				if (SORT_1.equals(flag)) {
					mTvSort1.setText(name);
				} else if (SORT_2.equals(flag)) {
					mTvSort2.setText(name);
				} else if (SORT_3.equals(flag)) {
					mTvSort3.setText(name);
				} else if (SORT_4.equals(flag)) {
					mTvSort4.setText(name);
				}
			}
			this.hideSortLoading();
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_REVIEW_TAGS == operator) {

			this.hideTagsLoading();
		}

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		mDialogProxy.hideDialog();
		this.showToast(this.getString(R.string.review_success));
		this.finish();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_REVIEW == operator) {
			mDialogProxy.hideDialog();
		} else {
			this.hideSortLoading();
		}
	}

	@Override
	public void onRatingChanged(RatingBar ratingBar, float rating,
			boolean fromUser) {
		// TODO Auto-generated method stub
		switch (ratingBar.getId()) {
		case R.id.review_add_ratingBar_sort1:
			mTvSort1Level.setText(CommonUtil.getStringByScore(this,
					(int) rating));
			break;
		case R.id.review_add_ratingBar_sort2:
			mTvSort2Level.setText(CommonUtil.getStringByScore(this,
					(int) rating));
			break;
		case R.id.review_add_ratingBar_sort3:
			mTvSort3Level.setText(CommonUtil.getStringByScore(this,
					(int) rating));
			break;
		case R.id.review_add_ratingBar_sort4:
			mTvSort4Level.setText(CommonUtil.getStringByScore(this,
					(int) rating));
			break;
		}
	}

}
