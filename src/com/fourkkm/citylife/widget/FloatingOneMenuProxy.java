package com.fourkkm.citylife.widget;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.fourkkm.citylife.R;

/**
 * 漂浮的一级菜单代理
 * 
 * @author ShanZha
 * 
 */
public class FloatingOneMenuProxy implements OnItemClickListener {

	private Context mCtx;
	private ListView mListView;
	private BaseAdapter mAdapter;
	private PopupWindow mPopWindow;
	private List<String> mDatas;
	private IFloatingItemClick mOnItemClickListener;
	private int mType = -1;

	/**
	 * @param context
	 * @param type
	 *            漂浮的操作类型，见GlobalConfig.FloatingType
	 */
	public FloatingOneMenuProxy(Context context, int type) {
		this.mCtx = context;
		this.mType = type;
		this.prepare();
	}

	private void prepare() {
		mListView = (ListView) LayoutInflater.from(mCtx).inflate(
				R.layout.floating_one_menu, null);
		mListView.setOnItemClickListener(this);
		mPopWindow = new PopupWindow(mListView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow外部区域是否可触摸
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);
	}

	/**
	 * 设置数据
	 * 
	 * @param datas
	 */
	public void setDatas(List<String> datas) {
		this.mDatas = datas;
		this.notifyDataChanged();
	}

	public void setFloatingItemClickListener(IFloatingItemClick listener) {
		this.mOnItemClickListener = listener;
	}

	/**
	 * 漂浮在anchor下面
	 * 
	 * @param anchor
	 */
	public void showAsDropDown(View anchor) {
		if (null != mPopWindow) {
			mPopWindow.showAsDropDown(anchor);
		}
	}

	/**
	 * 漂浮在anchor下据x、y轴xoff、yoff距离
	 * 
	 * @param anchor
	 * @param xoff
	 * @param yoff
	 */
	public void showAsDropDown(View anchor, int xoff, int yoff) {
		if (null != mPopWindow) {
			mPopWindow.showAsDropDown(anchor, xoff, yoff);
		}
	}

	public void dismiss() {
		if (null != mPopWindow) {
			mPopWindow.dismiss();
		}
	}

	private void notifyDataChanged() {
		if (null == mDatas) {
			return;
		}
		if (null == mAdapter) {
			mAdapter = new ArrayAdapter<String>(mCtx,
					R.layout.floating_one_menu_item,
					R.id.floating_one_menu_item_tv_tips, mDatas);
			mListView.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (null != mOnItemClickListener) {
			mOnItemClickListener.onFloatingItemClick(position,mDatas.get(position), mType);
		}
		this.dismiss();
	}
}
