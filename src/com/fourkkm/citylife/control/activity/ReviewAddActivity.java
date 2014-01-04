package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerPictures;
import com.andrnes.modoer.ModoerReview;
import com.andrnes.modoer.ModoerReviewOpt;
import com.andrnes.modoer.ModoerSubject;
import com.andrnes.modoer.ModoerTag;
import com.andrnes.modoer.ModoerTaggroup;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ReviewTagItemView;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 评论添加
 * 
 * @author ShanZha
 * 
 */
public class ReviewAddActivity extends BaseUploadPicActivity implements
		OnRatingBarChangeListener {

	private static final String TAG = "ReviewAddActivity";
	private static final String SORT_1 = "sort1";
	private static final String SORT_2 = "sort2";
	private static final String SORT_3 = "sort3";
	private static final String SORT_4 = "sort4";
	private TextView mTvTitle, mTvSubject, mTvSort1, mTvSort2, mTvSort3,
			mTvSort4, mTvSort1Level, mTvSort2Level, mTvSort3Level,
			mTvSort4Level;
	private EditText mEtPerAver, mEtContent;
	private RatingBar mRatingBarSort1, mRatingBarSort2, mRatingBarSort3,
			mRatingBarSort4;

	private LinearLayout mLlSort, mLlSortLoading, mLlTagsLoading;
	private GridView mGvTag;
	private BaseAdapter mAdapterTags;
	private List<ModoerTag> mTagList;

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
		mGvTag = (GridView) this.findViewById(R.id.review_add_gv_tags);
		mGvPics = (GridView) this.findViewById(R.id.review_add_gv_pics);
		mLlTagsLoading = (LinearLayout) this
				.findViewById(R.id.review_add_ll_loading_tags);

		mRatingBarSort1.setOnRatingBarChangeListener(this);
		mRatingBarSort2.setOnRatingBarChangeListener(this);
		mRatingBarSort3.setOnRatingBarChangeListener(this);
		mRatingBarSort4.setOnRatingBarChangeListener(this);

	}

	@Override
	protected void prepare() {
		mIsNeedUploadPics = true;
		super.prepare();
		mTagList = new ArrayList<ModoerTag>();
		mAdapterTags = new ItemSingleAdapter<ReviewTagItemView, ModoerTag>(
				mTagList, this);
		mGvTag.setAdapter(mAdapterTags);
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
		this.fetctReviewTags();

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
		mGvTag.setVisibility(View.GONE);
		mLlTagsLoading.setVisibility(View.VISIBLE);
	}

	private void hideTagsLoading() {
		mGvTag.setVisibility(View.VISIBLE);
		mLlTagsLoading.setVisibility(View.GONE);
	}

	private void showWaitting() {
		if (null != mDialogProxy) {
			mDialogProxy.showDialog();
		}
	}

	private void hideWaitting() {
		if (null != mDialogProxy) {
			mDialogProxy.hideDialog();
		}
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
		String selectCode = "from com.andrnes.modoer.ModoerTaggroup mt where mt.id in("
				+ this.getTagGroupIdString() + ")";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_REVIEW_TAGS);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerTaggroup(),
				param);
	}

	private String getTagGroupIdString() {
		StringBuilder sb = new StringBuilder();
		try {
			String configJson = mSubject.getPid().getConfigJson();
			JSONObject json = new JSONObject(configJson);
			JSONArray array = json.getJSONArray("taggroup");
			for (int i = 0; i < array.length(); i++) {
				String taggroupId = array.getString(i);
				sb.append(taggroupId);
				if (i != array.length() - 1) {
					sb.append(",");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
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
		if (!this.isUploadFinished()) {
			this.showToast(this.getString(R.string.subject_upload_pic_unfinish));
			return;
		}
		ModoerReview review = new ModoerReview();
		review.setIdtype("item_subject");
		review.setContent(mEtContent.getText().toString().trim());
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		review.setUid(member);
		review.setUsername(member.getUsername());
		review.setSid(mSubject);
		review.setPcatid(mSubject.getPid());
		review.setCityId(((CoreApp) AppUtils.getBaseApp(this)).getCurrArea());
		review.setSubject(mSubject.getName());
		review.setStatus(1);
		String tagJson = this.buildCheckedTaggroup();
		if (!TextUtils.isEmpty(tagJson)) {
			review.setTaggroup(tagJson);
			review.setTaggroupJson(tagJson);
		}
		String picJson = this.buildUploadPic();
		if (!TextUtils.isEmpty(picJson)) {
			review.setPictures(picJson);
			review.setPicturesJson(picJson);
		}

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

		this.showWaitting();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_REVIEW);
		this.getStoreOperation().saveOrUpdate(review, param);

	}

	/**
	 * 构建选中标签json
	 * 
	 * @return
	 */
	private String buildCheckedTaggroup() {
		// 以ModoerTaggroup的id，即parentId分组
		Map<Integer, List<String>> map = new HashMap<Integer, List<String>>();
		for (int i = 0; i < mTagList.size(); i++) {
			ModoerTag tag = mTagList.get(i);
			int parentId = tag.getParentId();
			String name = tag.getName();
			if (!tag.isChecked()) {
				continue;
			}
			if (map.containsKey(parentId)) {
				List<String> tagList = map.get(parentId);
				if (null != tagList) {
					tagList.add(name);
				}
			} else {
				List<String> tagList = new ArrayList<String>();
				tagList.add(name);
				map.put(parentId, tagList);
			}
		}
		if (map.size() < 1) {
			return "";
		}
		try {
			Set<Integer> set = map.keySet();
			Iterator<Integer> it = set.iterator();
			JSONObject root = new JSONObject();
			while (it.hasNext()) {
				int parentId = it.next();
				JSONArray array = new JSONArray();
				List<String> values = map.get(parentId);
				for (String tag : values) {
					array.put(tag);
				}
				root.put("" + parentId, array);
			}
			return root.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 构建图片Json
	 * 
	 * @return
	 */
	public String buildUploadPic() {
		if (this.getPicCount() > 0) {
			// 排除第一项,仅保存大图路径
			JSONArray array = new JSONArray();
			for (int i = 1; i < mPicList.size(); i++) {
				ModoerPictures pic = mPicList.get(i);
				String big = pic.getFilename();
				array.put(big);
			}
			return array.toString();
		}
		return "";
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
			for (int i = 0; i < results.size(); i++) {
				ModoerTaggroup group = (ModoerTaggroup) results.get(i);
				String options = group.getOptions();
				String[] tags = options.split(",");
				for (String tag : tags) {
					ModoerTag mTag = new ModoerTag();
					mTag.setParentId(group.getId());
					mTag.setName(tag);
					mTagList.add(mTag);
				}
			}
			this.notifyDataChanged(mAdapterTags);
			this.hideTagsLoading();
		}

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		this.hideWaitting();
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerMembers.class.getName());
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerReview.class.getName());
		this.showToast(this.getString(R.string.review_success));
		this.finish();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_REVIEW == operator) {
			this.hideWaitting();
			this.showToast(this.getString(R.string.review_fail));
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
			mTvSort1Level.setText(CommonUtil.getStringByScore(this, rating));
			break;
		case R.id.review_add_ratingBar_sort2:
			mTvSort2Level.setText(CommonUtil.getStringByScore(this, rating));
			break;
		case R.id.review_add_ratingBar_sort3:
			mTvSort3Level.setText(CommonUtil.getStringByScore(this, rating));
			break;
		case R.id.review_add_ratingBar_sort4:
			mTvSort4Level.setText(CommonUtil.getStringByScore(this, rating));
			break;
		}
	}

}
