package com.fourkkm.citylife.itemview;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.widget.adapter.ItemSingleAdapter;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.item.ItemSingle;
import com.zj.support.widget.itemview.ItemView;

public class ModoerSubjectItemView extends RelativeLayout implements ItemView {

	private Context mCtx;
	private ImageView mIvShow, mIvAttr1, mIvAttr2, mIvAttr3, mIvAttr4,
			mIvAttr5;
	private TextView mTvShopName;
	private TextView mTvArea;
	private TextView mTvAveragePer;
	private RatingBar mRatingBar;
	private TextView mTvDistance;
	private TextView mTvCatidName;
	private Bitmap mBmDefault;
	private ItemSingleAdapter<ItemView, ItemSingle> mAdapter;
	private boolean mIsNear = false;

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
		mTvArea = (TextView) this.findViewById(R.id.subject_list_item_tv_area1);
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
		mIvAttr3 = (ImageView) this
				.findViewById(R.id.subject_list_item_iv_attr3);
		mIvAttr4 = (ImageView) this
				.findViewById(R.id.subject_list_item_iv_attr4);
		mIvAttr5 = (ImageView) this
				.findViewById(R.id.subject_list_item_iv_attr5);
		mTvCatidName = (TextView) this
				.findViewById(R.id.subject_list_item_tv_catid_name);

	}

	@SuppressWarnings("unchecked")
	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub
		mAdapter = (ItemSingleAdapter<ItemView, ItemSingle>) obj;
		try {
			mIsNear = (Boolean) mAdapter.getParams("isNear");
		} catch (Exception e) {
		}
	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerSubject subject = (ModoerSubject) item;
		mTvShopName.setText(subject.getName());
		ModoerArea area = subject.getAid();
		if (null != area && !TextUtils.isEmpty(area.getName())) {
			mTvArea.setText(area.getName());
		}else{
			mTvArea.setVisibility(View.GONE);
		}
		mTvAveragePer.setText(mCtx.getString(R.string.average_per)
				+ subject.getAvgprice());
		if (null != subject.getCatid()) {
			mTvCatidName.setText(subject.getCatid().getName());
		}else{
			mTvCatidName.setVisibility(View.GONE);
		}
		if (mIsNear) {
			double lng = ((CoreApp) AppUtils.getBaseApp(mCtx)).mCurrLng;
			double lat = ((CoreApp) AppUtils.getBaseApp(mCtx)).mCurrLat;
			double lngSubject = subject.getMapLng();
			double latSubject = subject.getMapLat();
			if (0 != lngSubject && 0 != latSubject) {
				double distance = CommonUtil.getDistance(lng, lat, lngSubject,
						latSubject);
				mTvDistance.setText(CommonUtil.formatUnitM(distance));
			} else {
				mTvDistance.setVisibility(View.GONE);
			}
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

		List<String> attrList = CommonUtil.getSubjectAttrIconList(
				subject.getCShopattsReplace(), subject.getCShopatts2Replace());
		this.setAttrIcons(attrList);

	}

	private void setAttrIcons(List<String> attrList) {
		if (null == attrList || attrList.size() == 0) {
			mIvAttr1.setVisibility(View.GONE);
			mIvAttr2.setVisibility(View.GONE);
			mIvAttr3.setVisibility(View.GONE);
			mIvAttr4.setVisibility(View.GONE);
			mIvAttr5.setVisibility(View.GONE);
			return;
		}
		for (int i = 0; i < attrList.size(); i++) {
			String icon = attrList.get(i);
			String url = GlobalConfig.URL_ATTR_PIC + icon;
			switch (i) {
			case 0:
				mIvAttr1.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(mCtx)
						.showPic(url, mIvAttr1, null);
				break;
			case 1:
				mIvAttr2.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(mCtx)
						.showPic(url, mIvAttr2, null);
				break;
			case 2:
				mIvAttr3.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(mCtx)
						.showPic(url, mIvAttr3, null);
				break;
			case 3:
				mIvAttr4.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(mCtx)
						.showPic(url, mIvAttr4, null);
				break;
			case 4:
				mIvAttr5.setVisibility(View.VISIBLE);
				AsyncImageLoader.getImageLoad(mCtx)
						.showPic(url, mIvAttr5, null);
				break;
			}
		}
	}

}
