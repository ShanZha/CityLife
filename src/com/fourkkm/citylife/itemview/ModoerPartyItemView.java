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

import com.andrnes.modoer.ModoerParty;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * 聚会ItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerPartyItemView extends RelativeLayout implements ItemView {

	private ImageView mIvThumb;
	private TextView mTvSubject, mTvAddress, mTvTime, mTvDesc, mTvSignUpCount;
	private Context mCtx;
	private Bitmap mBmDefault;

	public ModoerPartyItemView(Context context) {
		this(context, null);
	}

	public ModoerPartyItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ModoerPartyItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mCtx = context;
		mBmDefault = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.list_thumb);
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub
		mIvThumb = (ImageView) this
				.findViewById(R.id.party_list_item_iv_thunmb);
		mTvSubject = (TextView) this
				.findViewById(R.id.party_list_item_tv_subject);
		mTvAddress = (TextView) this
				.findViewById(R.id.party_list_item_tv_address);
		mTvTime = (TextView) this.findViewById(R.id.party_list_item_tv_time);
		mTvDesc = (TextView) this.findViewById(R.id.party_list_item_tv_desc);
		mTvSignUpCount = (TextView) this
				.findViewById(R.id.party_list_item_tv_sign_up_count);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerParty party = (ModoerParty) item;
		if (TextUtils.isEmpty(party.getThumb())) {
			mIvThumb.setImageBitmap(mBmDefault);
		} else {
			String url = GlobalConfig.URL_PIC + party.getThumb();
			AsyncImageLoader.getImageLoad(mCtx).showPic(url, mIvThumb);

		}
		mTvSubject.setText(party.getSubject());
		mTvAddress.setText(mCtx.getString(R.string.party_address_detail)
				+ party.getAddress());
		mTvTime.setText(mCtx.getString(R.string.party_time_detail)
				+ CommonUtil.getTimeByPHP(party.getBegintime(), "MM-dd hh:mm")
				+ "~"
				+ CommonUtil.getTimeByPHP(party.getEndtime(), "MM-dd hh:mm"));
		// mTvDesc.setText(mCtx.getString(R.string.party_desc_detail) +
		// party.getDes());
		int applyNum = party.getApplynum();
		if (applyNum > 0) {
			mTvSignUpCount.setText("已报名" + applyNum + "人");
		} else {
			mTvSignUpCount.setVisibility(View.GONE);
		}

	}

}
