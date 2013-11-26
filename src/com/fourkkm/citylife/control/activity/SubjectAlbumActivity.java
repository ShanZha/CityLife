package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * œ‡≤·‰Ø¿¿ΩÁ√Ê
 * 
 * @author ShanZha
 * 
 */
public class SubjectAlbumActivity extends BaseFragmentActivity implements
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
		mSubjectId = intent.getIntExtra("subjectId", 0);
		mSubjectName = intent.getStringExtra("subjectName");
		mSubjectThumbCount = intent.getIntExtra("subjectThumbnailCount", 0);

		mTvTitle.setText(mSubjectName);
		mTvCount.setText(mCurrIndex + "/" + mSubjectThumbCount);

		this.showLoading();
		this.fetchPictures(mSubjectId);

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

	private void fetchPictures(int subjectId) {
		String selectCode = "from com.andrnes.modoer.ModoerPictures mp where mp.sid.id = "
				+ subjectId;
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerPictures(),
				param);
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
				String thumb = pictures.getThumb();
				mThumbUrls.add(GlobalConfig.URL_PIC + thumb);
			}
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
