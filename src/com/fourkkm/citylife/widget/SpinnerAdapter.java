package com.fourkkm.citylife.widget;

import java.util.List;

import com.fourkkm.citylife.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SpinnerAdapter extends ArrayAdapter<String> {

	private Context mCtx;
	private int mResId;
	private List<String> mDatas;

	public SpinnerAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.mCtx = context;
		this.mResId = textViewResourceId;
		this.mDatas = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (null == convertView) {
			convertView = LayoutInflater.from(mCtx).inflate(mResId, parent,
					false);
		}
		TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
		tv.setText(mDatas.get(position));
		tv.setTextColor(mCtx.getResources()
				.getColor(R.color.spinner_text_color));
		tv.setTextSize(14);
		return super.getView(position, convertView, parent);
	}

}
