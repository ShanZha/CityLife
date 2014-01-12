package com.fourkkm.citylife.control.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.TextView;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.widget.HelperPagerAdapter;
import com.zj.app.BaseActivity;

/**
 * ÐÂÊÖ°ïÖú
 * 
 * @author ShanZha
 * 
 */
public class HelperActivity extends BaseActivity implements
		OnPageChangeListener {

	private TextView mTvTitle, mTvCount;
	private ViewPager mViewPager;
	private PagerAdapter mPageAdapter;
	private static final int[] mResIds = { R.drawable.more_helper_1,
			R.drawable.more_helper_2, R.drawable.more_helper_3 };
	private int mTotalCount = mResIds.length;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.more_helper);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvCount = (TextView) this.findViewById(R.id.more_helper_tv_count);
		mViewPager = (ViewPager) this.findViewById(R.id.more_helper_viewpager);
		mTvTitle.setText(this.getString(R.string.more_help));
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mPageAdapter = new HelperPagerAdapter(mResIds);
		mViewPager.setOnPageChangeListener(this);
		mViewPager.setAdapter(mPageAdapter);

		mTvCount.setText("1" + "/" + mTotalCount);
	}

	public void onClickBack(View view) {
		this.finish();
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
		mTvCount.setText((arg0 + 1) + "/" + mTotalCount);
	}
}
