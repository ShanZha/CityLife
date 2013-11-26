package com.fourkkm.citylife.itemview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andrnes.modoer.ModoerBcastr;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.utils.CommonUtil;
import com.zj.support.image.ScaleImageProcessor;
import com.zj.support.widget.AsyncImageView;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * 首页广告图片轮播的ItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerBcastrItemView extends LinearLayout implements ItemView {

	private AsyncImageView mIvShow;
	private Context mCtx;
	// private Bitmap mDefaultBm;
	private int mThumbnailHeight = 0, mThumbnailWidth = 0;
	private boolean mIsFirst = false;

	public ModoerBcastrItemView(Context context) {
		this(context, null);
	}

	public ModoerBcastrItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mCtx = context;
		mThumbnailHeight = CommonUtil.getScreenHeight(mCtx) / 3;
		mThumbnailWidth = CommonUtil.getScreenHeight(mCtx);
		// mDefaultBm = BitmapFactory.decodeResource(this.getResources(),
		// R.drawable.nopic);
		// mDefaultBm = ThumbnailUtils.extractThumbnail(mDefaultBm,
		// mThumbnailWidth, mThumbnailHeight);
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub
		mIvShow = (AsyncImageView) this
				.findViewById(R.id.main_gallery_item_iv_show);
		if (!mIsFirst) {
			mIsFirst = true;
			ViewGroup.LayoutParams params = mIvShow.getLayoutParams();
			params.width = mThumbnailWidth;
			params.height = mThumbnailHeight;
			mIvShow.setLayoutParams(params);
		}
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerBcastr bcastr = (ModoerBcastr) item;
		// mIvShow.setDefaultImageBitmap(mDefaultBm);
		mIvShow.setImageProcessor(new ScaleImageProcessor(mThumbnailWidth,
				mThumbnailHeight));
		mIvShow.setUrl(GlobalConfig.URL_PIC + bcastr.getLink());
	}

}
