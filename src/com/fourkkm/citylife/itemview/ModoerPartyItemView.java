package com.fourkkm.citylife.itemview;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerParty;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.control.activity.BaseListActivity;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * æ€ª·ItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerPartyItemView extends RelativeLayout implements ItemView {

	private ImageView mIvThumb;
	private TextView mTvSubject, mTvAddress, mTvTime, mTvDesc, mTvSignUpCount;
	private Context mCtx;

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
		String url = GlobalConfig.URL_PIC + party.getThumb();
		AsyncImageLoader.getImageLoad(mCtx).showPic(url, mIvThumb);
		mTvSubject.setText(party.getSubject());
		mTvAddress.setText(mCtx.getString(R.string.party_address)
				+ party.getAddress());
		long beginTime = party.getBegintime() * 1000L;
		long endTime = party.getEndtime() * 1000L;
		mTvTime.setText(mCtx.getString(R.string.party_time)
				+ DateFormatMethod.formatDate(new Date(beginTime),
						"MM-dd hh:mm") + "~"
				+ DateFormatMethod.formatDate(new Date(endTime), "MM-dd hh:mm"));
		mTvDesc.setText(mCtx.getString(R.string.party_desc) + party.getDes());
		mTvSignUpCount.setText(party.getApplynum()+"");

		if (((BaseListActivity) mCtx).isShouldLoadData(pos)) {
			((BaseListActivity) mCtx).notifyLoadStart();
		}
	}

}
