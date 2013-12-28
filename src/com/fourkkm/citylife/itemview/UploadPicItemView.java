package com.fourkkm.citylife.itemview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.andrnes.modoer.ModoerPictures;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.control.activity.BaseAddActivity;
import com.fourkkm.citylife.control.activity.BaseUploadPicActivity;
import com.zj.app.utils.CommonUtil;
import com.zj.support.image.ScaleImageProcessor;
import com.zj.support.widget.AsyncImageView;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * 
 * 
 * @author ShanZha
 * 
 */
public class UploadPicItemView extends FrameLayout implements ItemView,
		OnClickListener {

	private AsyncImageView mIvThumb;
	private LinearLayout mLlLoading;
	private Bitmap mBmAdd, mBmDefault;
	private int mThumbWidth = 0, mThumbHeight = 0;

	public UploadPicItemView(Context context) {
		this(context, null);
	}

	public UploadPicItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UploadPicItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		int screenWidth = CommonUtil.getScreenWidth(getContext());
		int margin = this.getResources().getDimensionPixelSize(
				R.dimen.margin_10);
		int padding = this.getResources().getDimensionPixelSize(
				R.dimen.margin_5);
		int horizontalSpacing = this.getResources().getDimensionPixelSize(
				R.dimen.margin_10);
		mThumbWidth = (screenWidth - margin * 2 - horizontalSpacing * 2 + padding * 2) / 3;
		mThumbHeight = (mThumbWidth * 2) / 3;

		Bitmap bm1 = BitmapFactory.decodeResource(getResources(),
				R.drawable.add_thumb);
		mBmAdd = ThumbnailUtils
				.extractThumbnail(bm1, mThumbWidth, mThumbHeight);

		Bitmap bm2 = BitmapFactory.decodeResource(getResources(),
				R.drawable.list_thumb);
		mBmDefault = ThumbnailUtils.extractThumbnail(bm2, mThumbWidth,
				mThumbHeight);
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub
		mIvThumb = (AsyncImageView) this
				.findViewById(R.id.upload_pic_item_iv_thumb);
		mLlLoading = (LinearLayout) this
				.findViewById(R.id.upload_pic_item_ll_loading);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerPictures picture = (ModoerPictures) item;
		ScaleImageProcessor scale = new ScaleImageProcessor(mThumbWidth,
				mThumbHeight);
		mIvThumb.setImageProcessor(scale);
		mIvThumb.setTag(pos);
		mIvThumb.setOnClickListener(this);
		int status = picture.getStatus();
		switch (status) {
		case GlobalConfig.STATUS_UPLOAD_START:
			mLlLoading.setVisibility(View.VISIBLE);
			break;
		case GlobalConfig.STATUS_UPLOAD_FAIL:
		case GlobalConfig.STATUS_UPLOAD_NONE:
			mLlLoading.setVisibility(View.GONE);
			break;
		case GlobalConfig.STATUS_UPLOAD_SUCCESS:
			mLlLoading.setVisibility(View.GONE);
			break;
		}
		if (pos == 0) {// 第一项是添加图片入口
			mIvThumb.setImageBitmap(mBmAdd);
		} else {
			String url = picture.getUrl();
			if (!TextUtils.isEmpty(url)) {// 暂时将url设置为本地路劲
				mIvThumb.setUrl(url);
			} else {
				mIvThumb.setImageBitmap(mBmDefault);
			}
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int pos = (Integer) v.getTag();
		Toast.makeText(getContext(), "Pos = " + pos, Toast.LENGTH_SHORT).show();
		((BaseUploadPicActivity) getContext()).onClickUploadPic(pos);
	}

}
