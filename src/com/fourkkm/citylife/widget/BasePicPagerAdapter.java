package com.fourkkm.citylife.widget;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView.ScaleType;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.control.activity.MainActivity;
import com.zj.app.utils.CommonUtil;
import com.zj.support.widget.AsyncImageView;

/**
 * »ù±¾µÄÍ¼Æ¬ÇÐ»»Adapter
 * 
 * @author ShanZha
 * 
 */
public class BasePicPagerAdapter extends PagerAdapter implements
		OnClickListener {

	private List<String> mUrls;
	private int mThumbnailHeight = 0, mThumbnailWidth = 0;
	private Context mCtx;

	public BasePicPagerAdapter(List<String> urls, Context ctx) {
		this.mUrls = urls;
		this.mCtx = ctx;
		mThumbnailHeight = CommonUtil.getScreenHeight(mCtx) / 3;
		mThumbnailWidth = CommonUtil.getScreenWidth(mCtx);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUrls == null ? 0 : mUrls.size();
	}

	@Override
	public View instantiateItem(ViewGroup container, int position) {
		AsyncImageView iv = new AsyncImageView(mCtx);
		String url = mUrls.get(position);
		// iv.setImageProcessor(new ScaleImageProcessor(mThumbnailWidth,
		// mThumbnailHeight));
		iv.setDefaultImageResource(R.drawable.nopic);
		iv.setScaleType(ScaleType.FIT_XY);
		iv.setUrl(url);
		ViewGroup.LayoutParams params = container.getLayoutParams();
		params.width = LayoutParams.MATCH_PARENT;
		params.height = mThumbnailHeight;
		container.setLayoutParams(params);
		// Now just add AsyncImageView to ViewPager and return it
		container.addView(iv, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		iv.setTag(position);
		iv.setOnClickListener(this);
		return iv;
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
		((MainActivity) mCtx).onBcastrItemClick(pos);
	}

}
