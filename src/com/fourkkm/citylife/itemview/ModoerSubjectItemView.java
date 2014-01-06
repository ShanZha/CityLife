package com.fourkkm.citylife.itemview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerAttList;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.widget.AsyncImageView;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

public class ModoerSubjectItemView extends RelativeLayout implements ItemView {

	private Context mCtx;
	private ImageView mIvShow, mIvAttr1, mIvAttr2;
	private TextView mTvShopName;
	private TextView mTvArea;
	private TextView mTvAveragePer;
	private RatingBar mRatingBar;
	private TextView mTvDistance;
	private Bitmap mBmDefault;

	public ModoerSubjectItemView(Context context) {
		this(context, null);
	}

	public ModoerSubjectItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ModoerSubjectItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mCtx = context;
		mBmDefault = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.list_thumb);
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub
		mIvShow = (ImageView) this.findViewById(R.id.subject_list_item_iv_show);
		mTvShopName = (TextView) this
				.findViewById(R.id.subject_list_item_tv_shop_name);
		mTvArea = (TextView) this.findViewById(R.id.subject_list_item_tv_area);
		mTvAveragePer = (TextView) this
				.findViewById(R.id.subject_list_item_tv_average_per);
		mTvDistance = (TextView) this
				.findViewById(R.id.subject_list_item_tv_distance);
		mRatingBar = (RatingBar) this
				.findViewById(R.id.subject_list_item_rating_bar);
		mIvAttr1 = (ImageView) this
				.findViewById(R.id.subject_list_item_iv_attr1);
		mIvAttr2 = (ImageView) this
				.findViewById(R.id.subject_list_item_iv_attr2);

	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerSubject subject = (ModoerSubject) item;
		mTvShopName.setText(subject.getName());
		ModoerArea area = subject.getAid();
		if (null != area && !TextUtils.isEmpty(area.getName())) {
			mTvArea.setText(area.getName());
		}
		mTvAveragePer.setText(mCtx.getString(R.string.average_per)
				+ subject.getAvgprice());
		double lng = ((CoreApp) AppUtils.getBaseApp(mCtx)).mCurrLng;
		double lat = ((CoreApp) AppUtils.getBaseApp(mCtx)).mCurrLat;
		double lngSubject = subject.getMapLng();
		double latSubject = subject.getMapLat();
		if (0 != lngSubject && 0 != latSubject) {
			int distance = (int) CommonUtil.getDistance(lng, lat, lngSubject,
					latSubject);
			mTvDistance.setText(distance + "m");
		} else {
			mTvDistance.setVisibility(View.GONE);
		}
		mRatingBar.setProgress((int) subject.getAvgsort());

		String thumb = subject.getThumb();
		if (!TextUtils.isEmpty(thumb)) {
			String url = GlobalConfig.URL_PIC + subject.getThumb();
			AsyncImageLoader.getImageLoad(mCtx).showPic(url, mIvShow,
					mBmDefault);
		} else {
			mIvShow.setImageBitmap(mBmDefault);
		}

		ModoerAttList attr1 = subject.getCShopatts();
		if (null != attr1 && attr1.getIcon() != null) {
			AsyncImageLoader.getImageLoad(mCtx)
					.showPic(GlobalConfig.URL_ATTR_PIC + attr1.getIcon(),
							mIvAttr1, null);
		} else {
			mIvAttr1.setImageBitmap(null);
		}
		ModoerAttList attr2 = subject.getCShopatts2();
		if (null != attr2 && attr2.getIcon() != null) {
			AsyncImageLoader.getImageLoad(mCtx)
					.showPic(GlobalConfig.URL_ATTR_PIC + attr2.getIcon(),
							mIvAttr2, null);
		} else {
			mIvAttr2.setImageBitmap(null);
		}

	}

}
