package com.fourkkm.citylife.itemview;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskCategory;
import com.andrnes.modoer.ModoerAsks;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.control.activity.BaseListActivity;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * ÎÊ´ðItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerAskItemView extends RelativeLayout implements ItemView {

	private TextView mTvCategory, mTvSubject, mTvTime;
	private Context mCtx;

	public ModoerAskItemView(Context context) {
		this(context, null);
	}

	public ModoerAskItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ModoerAskItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mCtx = context;
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub
		mTvCategory = (TextView) this
				.findViewById(R.id.ask_list_item_tv_category);
		mTvSubject = (TextView) this
				.findViewById(R.id.ask_list_item_tv_subject);
		mTvTime = (TextView) this.findViewById(R.id.ask_list_item_tv_time);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerAsks ask = (ModoerAsks) item;
		ModoerAskCategory category = ask.getCatid();
		if (null != category && category.getId() != 0) {
			mTvCategory.setText("[" + category.getName() + "]");
		}
		mTvSubject.setText(ask.getSubject());
		mTvTime.setText(DateFormatMethod.formatDate(new Date(
				ask.getDateline() * 1000L), "yyyy-MM-dd"));

		if (((BaseListActivity) mCtx).isShouldLoadData(pos)) {
			((BaseListActivity) mCtx).notifyLoadStart();
		}
	}

}
