package com.fourkkm.citylife.widget;

import java.util.ArrayList;
import java.util.List;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

public class FloatingAreaMenuProxy implements OnItemClickListener,
		OnClickListener {
	
	private Context mCtx;
	private ListView mListViewParent;
	private ListView mListViewChild;
	private BaseAdapter mParentAdapter;
	private BaseAdapter mChildAdapter;
	private PopupWindow mPopWindow;
	private List<String> mParentDatas, mChildDatas;
	private int mType = -1;
	private String mStrAll = "";
	
	public FloatingAreaMenuProxy(Context context, int type) {
		this.mCtx = context;
		this.mType = type;
		this.prepare();
	}

	private void prepare() {
		View view = LayoutInflater.from(mCtx).inflate(
				R.layout.floating_two_menu, null);
		mListViewParent = (ListView) view
				.findViewById(R.id.floating_two_menu_listview_parent);
		view.setOnClickListener(this);
		// mListView.setOnItemClickListener(this);
		mListViewChild = (ListView) view
				.findViewById(R.id.floating_two_menu_listview_child);
		mListViewParent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
//				String parentKey = getParentKey(position);
//				mCurrParent = parentKey;
//				resetChildDatas(parentKey);
//				((FloatingAdapter) mParentAdapter).mCurrName = parentKey;
//				((FloatingAdapter) mChildAdapter).mCurrName = FloatingAdapter.INVALIDA_NAME;
//				notifyParentDataChanged();
//				notifyChildDataChanged();
			}
		});
		mListViewChild.setOnItemClickListener(this);
		mPopWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 设置PopupWindow外部区域是否可触摸
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);

		mChildDatas = new ArrayList<String>();
		mParentDatas = new ArrayList<String>();
//		mChildAdapter = new FloatingAdapter(mCtx, mChildDatas, LEVEL_CHILD);
		mListViewChild.setAdapter(mChildAdapter);
//		mParentAdapter = new FloatingAdapter(mCtx, mParentDatas, LEVEL_PARENT);
		mListViewParent.setAdapter(mParentAdapter);

		switch (mType) {
		case GlobalConfig.FloatingType.TYPE_AREA:
			mStrAll = GlobalConfig.FloatingStr.STR_ALL_AREA;
			break;
		case GlobalConfig.FloatingType.TYPE_CATEGORY:
		case GlobalConfig.FloatingType.TYPE_ASK_CATEGORY:
			mStrAll = GlobalConfig.FloatingStr.STR_ALL_CATEGOTY;
			break;
		case GlobalConfig.FloatingType.TYPE_ASK_STATE:
			mStrAll = GlobalConfig.FloatingStr.STR_ALL_ASK;
			break;
		}
//		mCurrParent = mStrAll;

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

}
