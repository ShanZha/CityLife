package com.fourkkm.citylife.widget;

import java.util.List;

import com.fourkkm.citylife.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FloatingAdapter extends BaseAdapter {

	public static final String INVALIDA_NAME = "";
	private Context mCtx;
	private int level = -1;
	private List<String> mDataList;
	public String mCurrName = INVALIDA_NAME;

	public FloatingAdapter(Context context, List<String> dataList, int level) {
		mCtx = context;
		this.mDataList = dataList;
		this.level = level;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return null == mDataList ? 0 : mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null == mDataList ? null : mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mCtx).inflate(
					R.layout.floating_two_menu_item, null);
			holder.mTvName = (TextView) convertView
					.findViewById(R.id.floating_two_menu_item_tv_tips);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String name = mDataList.get(position);

		if (mCurrName.equals(name)) {
			if (level % 2 == 0) {// 偶数，则灰
				convertView
						.setBackgroundResource(R.color.floating_list_bg_gray);
			} else {// 奇数，则白
				convertView
						.setBackgroundResource(R.color.floating_list_bg_white);
			}
		} else {
			convertView.setBackgroundResource(0);
		}

		holder.mTvName.setText(name);
		return convertView;
	}

	private static class ViewHolder {
		TextView mTvName;
	}
}
