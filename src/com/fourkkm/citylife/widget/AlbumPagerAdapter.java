package com.fourkkm.citylife.widget;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.zj.support.widget.PhotoView;

/**
 * 相册查看的PagerAdapter
 * 
 * @author ShanZha
 * 
 */
public class AlbumPagerAdapter extends PagerAdapter {

	private List<String> mUrls;

	public AlbumPagerAdapter(List<String> urls) {
		this.mUrls = urls;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUrls == null ? 0 : mUrls.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		PhotoView photoView = new PhotoView(container.getContext());
		String url = mUrls.get(position);
		photoView.setUrl(url);
		// Now just add PhotoView to ViewPager and return it
		container.addView(photoView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		return photoView;
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
