package com.fourkkm.citylife.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerPmsgs;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * Õ¾ÄÚ¶ÌÐÅItemView
 * 
 * @author ShanZha
 * 
 */
public class ModoerSmsItemView extends RelativeLayout implements ItemView {

	private TextView mTvSubject, mTvContent, mTvTime;

	public ModoerSmsItemView(Context context) {
		this(context, null);
	}

	public ModoerSmsItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ModoerSmsItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub

		mTvSubject = (TextView) this
				.findViewById(R.id.sms_list_item_tv_subject);
		mTvContent = (TextView) this
				.findViewById(R.id.sms_list_item_tv_content);
		mTvTime = (TextView) this.findViewById(R.id.sms_list_item_tv_time);

	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerPmsgs sms = (ModoerPmsgs) item;
		mTvSubject.setText(sms.getSubject());
		mTvContent.setText(sms.getContent());
		mTvTime.setText(CommonUtil.getTimeByPHP(sms.getPosttime(),
				CommonUtil.FORMAT_MINUTE));

	}

}
