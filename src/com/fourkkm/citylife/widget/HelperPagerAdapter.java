package com.fourkkm.citylife.widget;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

/**
 * ÐÂÊÖ°ïÖúµÄPagerAdapter
 * 
 * @author ShanZha
 * 
 */
public class HelperPagerAdapter extends PagerAdapter {

	private int[] mResIdList;

	public HelperPagerAdapter(int[] resids) {
		this.mResIdList = resids;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mResIdList == null ? 0 : mResIdList.length;
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		ImageView view = new ImageView(container.getContext());
		view.setImageResource(mResIdList[position]);
		// Now just add PhotoView to ViewPager and return it
		container.addView(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		// TODO Auto-generated method stub
		return view == object;
	}

}
