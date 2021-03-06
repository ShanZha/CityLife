package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerPictures;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.widget.AlbumPagerAdapter;
import com.zj.app.BaseFragmentActivity;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.HackyViewPager;

/**
 * 相册浏览界面
 * 
 * @author ShanZha
 * 
 */
public class AlbumActivity extends BaseFragmentActivity implements
		OnPageChangeListener {

	private TextView mTvTitle, mTvCount;
	private ViewPager mViewPager;
	private ProgressBar mProBar;
	private PagerAdapter mPagerAdapter;
	private List<String> mThumbUrls = null;
	private int mSubjectId = 0;
	private int mSubjectThumbCount = 0;
	private int mCurrIndex = 1;
	private String mSubjectName;

	private int mOperator = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();

		this.setContentView(R.layout.subject_album);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvCount = (TextView) this.findViewById(R.id.subject_album_tv_count);
		mViewPager = (HackyViewPager) this
				.findViewById(R.id.subject_album_view_pager);
		mProBar = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);

		mViewPager.setOnPageChangeListener(this);

	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mThumbUrls = new ArrayList<String>();

		Intent intent = this.getIntent();
		mOperator = intent.getIntExtra("operator", -1);
		if (GlobalConfig.IntentKey.ALBUM_SUBJECT == mOperator) {
			mSubjectId = intent.getIntExtra("subjectId", 0);
			mSubjectName = intent.getStringExtra("subjectName");
			mSubjectThumbCount = intent.getIntExtra("subjectThumbnailCount", 0);

			mTvTitle.setText(mSubjectName + "-相册");
			mTvCount.setText(mCurrIndex + "/" + mSubjectThumbCount);

			this.showLoading();
			this.fetchPictures(mSubjectId);
		} else if (GlobalConfig.IntentKey.ALBUM_REVIEW == mOperator) {
			this.hideLoading();
			String subject = intent.getStringExtra("reviewSubject");
			mTvTitle.setText(subject + "-点评相册");
			String picJson = intent.getStringExtra("reviewPicJson");
			this.prepareReviewPicsByJson(picJson);
			this.notifyDataChanged();
		} else if (GlobalConfig.IntentKey.ALBUM_CHINA_LANE == mOperator) {
			this.hideLoading();
			String subject = intent.getStringExtra("chinalaneSubject");
			mTvTitle.setText(subject + "-相册");
			String picJson = intent.getStringExtra("picJson");
			this.prepareChinaLanePicsByJson(picJson);
			this.notifyDataChanged();
		}

	}

	private void showLoading() {
		mProBar.setVisibility(View.VISIBLE);
		mViewPager.setVisibility(View.GONE);
	}

	private void hideLoading() {
		mProBar.setVisibility(View.GONE);
		mViewPager.setVisibility(View.VISIBLE);
	}

	private void notifyDataChanged() {
		if (null == mPagerAdapter) {
			mPagerAdapter = new AlbumPagerAdapter(mThumbUrls);
			mViewPager.setAdapter(mPagerAdapter);
		} else {
			mPagerAdapter.notifyDataSetChanged();
		}
	}

	private void prepareReviewPicsByJson(String picJson) {
		try {
//			JSONArray array = new JSONArray(picJson);
//			int length = array.length();
//			for (int i = 0; i < length; i++) {
//				// String temp = array.getString(i);
//				// String url = GlobalConfig.URL_UPLOAD + temp;
//				// mThumbUrls.add(url);
//				JSONObject jsonObj = (JSONObject) array.get(i);
//				String temp = jsonObj.getString("picture");
//				String url = GlobalConfig.URL_PIC + temp;
//				mThumbUrls.add(url);
//			}
			JSONObject pJson = new JSONObject(picJson);
			Iterator<String> it = pJson.keys();
			while(it.hasNext()){
				String key = it.next();
				JSONObject jObj = pJson.getJSONObject(key);
				String temp = jObj.getString("picture");
				String url = GlobalConfig.URL_PIC + temp;
				mThumbUrls.add(url);
			}
			int length = pJson.length();
			mSubjectThumbCount = length;
			mTvCount.setText(mCurrIndex + "/" + length);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void prepareChinaLanePicsByJson(String picJson) {
		try {
			JSONObject json = new JSONObject(picJson);
			@SuppressWarnings("unchecked")
			Iterator<String> it = json.keys();
			while (it.hasNext()) {
				String value = json.getString(it.next());
				String url = GlobalConfig.URL_PIC + value;
				mThumbUrls.add(url);
			}
			mSubjectThumbCount = mThumbUrls.size();
			mTvCount.setText(mCurrIndex + "/" + mSubjectThumbCount);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fetchPictures(int subjectId) {
		String selectCode = "from com.andrnes.modoer.ModoerPictures mp where mp.sid.id = "
				+ subjectId;
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerPictures(), param);
	}

	public void onClickBack(View view) {
		this.finish();
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null != results) {
			for (int i = 0; i < results.size(); i++) {
				ModoerPictures pictures = (ModoerPictures) results.get(i);
				String bigPic = pictures.getFilename();
				mThumbUrls.add(GlobalConfig.URL_PIC + bigPic);
			}
			mSubjectThumbCount = mThumbUrls.size();
			this.hideLoading();
			this.notifyDataChanged();
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.hideLoading();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mTvCount.setText((arg0 + 1) + "/" + mSubjectThumbCount);
	}
}
