package com.fourkkm.citylife.itemview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;

import com.andrnes.modoer.ModoerTag;
import com.fourkkm.citylife.R;
import com.zj.support.widget.item.BaseItem;
import com.zj.support.widget.itemview.ItemView;

/**
 * 
 * ∆¿¬€±Í«©ItemView
 * 
 * @author ShanZha
 * 
 */
public class ReviewTagItemView extends LinearLayout implements ItemView,
		OnCheckedChangeListener {

	private CheckBox mCbTag;

	public ReviewTagItemView(Context context) {
		this(context, null);
	}

	public ReviewTagItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void prepareViews() {
		// TODO Auto-generated method stub
		mCbTag = (CheckBox) this.findViewById(R.id.review_tag_cb_tag);
	}

	@Override
	public void prepareItemProperty(Object obj) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setItem(BaseItem item, int pos) {
		// TODO Auto-generated method stub
		ModoerTag tag = (ModoerTag) item;
		mCbTag.setText(tag.getName());
		mCbTag.setTag(tag);
		mCbTag.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		ModoerTag tag = (ModoerTag) buttonView.getTag();
		tag.setChecked(isChecked);
	}

}
