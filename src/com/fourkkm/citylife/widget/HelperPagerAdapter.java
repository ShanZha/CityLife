package com.fourkkm.citylife.widget;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.fourkkm.citylife.control.activity.HelperActivity;

/**
 * ÐÂÊÖ°ïÖúµÄPagerAdapter
 * 
 * @author ShanZha
 * 
 */
public class HelperPagerAdapter extends PagerAdapter implements OnClickListener {

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
		view.setScaleType(ScaleType.FIT_XY);
		view.setImageResource(mResIdList[position]);
		view.setTag(position);
		view.setOnClickListener(this);
		// Now just add PhotoView to ViewPager and return it
		container.addView(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer) v.getTag();
		((HelperActivity) v.getContext()).onClickHelperIv(pos);
	}

}
