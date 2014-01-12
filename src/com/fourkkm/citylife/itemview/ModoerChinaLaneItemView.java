package com.fourkkm.citylife.itemview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerFenlei;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * Ã∆»ÀœÔItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerChinaLaneItemView extends RelativeLayout implements ItemView {

	private Context mCtx;
	private ImageView mIvShow;
	private TextView mTvSubject;
	private TextView mTvContent;
	private TextView mTvArea;
	private Bitmap mBmDefault;

	public ModoerChinaLaneItemView(Context context) {
		this(context, null);
	}

	public ModoerChinaLaneItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ModoerChinaLaneItemView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		this.mCtx = context;
		mBmDefault = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.list_thumb);
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub
		mIvShow = (ImageView) this
				.findViewById(R.id.china_lane_list_item_iv_thunmb);
		mTvSubject = (TextView) this
				.findViewById(R.id.china_lane_list_item_tv_subject);
		mTvContent = (TextView) this
				.findViewById(R.id.china_lane_list_item_tv_content);
		mTvArea = (TextView) this
				.findViewById(R.id.china_lane_list_item_tv_area);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerFenlei lane = (ModoerFenlei) item;
		mTvSubject.setText(lane.getSubject());
		String content = lane.getContent();
		mTvContent.setText(CommonUtil.formatHtml2(content));
		ModoerArea aid = lane.getAid();
		if (null != aid && aid.getId() != 0) {
			mTvArea.setText(mCtx.getString(R.string.china_lane_area)
					+ aid.getName());
		} else {
			mTvArea.setVisibility(View.GONE);
		}

		String thumb = lane.getThumb();
		if (!TextUtils.isEmpty(thumb)) {
			String url = GlobalConfig.URL_PIC + lane.getThumb();
			AsyncImageLoader.getImageLoad(mCtx).showPic(url, mIvShow,
					mBmDefault);
		} else {
			mIvShow.setImageBitmap(mBmDefault);
		}

	}

}
