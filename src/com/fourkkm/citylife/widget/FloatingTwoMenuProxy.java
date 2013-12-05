package com.fourkkm.citylife.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
 * 漂浮的二级菜单代理
 * 
 * @author ShanZha
 * 
 */
public class FloatingTwoMenuProxy implements OnItemClickListener {

	private Context mCtx;
	private ListView mListViewParent;
	private ListView mListViewChild;
	private BaseAdapter mParentAdapter;
	private BaseAdapter mChildAdapter;
	private PopupWindow mPopWindow;
	private List<String> mChildDatas;
	private Map<String, List<String>> mDatas;
	private IFloatingItemClick mOnItemClickListener;
	private int mType = -1;

	public FloatingTwoMenuProxy(Context context, int type) {
		this.mCtx = context;
		this.mType = type;
		this.prepare();
	}

	private void prepare() {
		View view = LayoutInflater.from(mCtx).inflate(
				R.layout.floating_two_menu, null);
		mListViewParent = (ListView) view
				.findViewById(R.id.floating_two_menu_listview_parent);
		// mListView.setOnItemClickListener(this);
		mListViewChild = (ListView) view
				.findViewById(R.id.floating_two_menu_listview_child);
		mListViewParent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				notifyChildDataChanged(getParentKey(position));
			}
		});
		mListViewChild.setOnItemClickListener(this);
		mPopWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow外部区域是否可触摸
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);

		mChildDatas = new ArrayList<String>();
	}

	/**
	 * 设置数据
	 * 
	 * @param datas
	 */
	public void setDatas(Map<String, List<String>> datas) {
		this.mDatas = datas;
		this.notifyParentDataChanged();
		this.notifyChildDataChanged(this.getParentKey(0));
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

	private void notifyParentDataChanged() {
		if (null == mDatas) {
			return;
		}
		if (null == mParentAdapter) {
			mParentAdapter = new ArrayAdapter<String>(mCtx,
					R.layout.floating_one_menu_item,
					R.id.floating_one_menu_item_tv_tips, this.getParentDatas());
			mListViewParent.setAdapter(mParentAdapter);
		} else {
			mParentAdapter.notifyDataSetChanged();
		}
	}

	private void notifyChildDataChanged(String parentKey) {
		if (null == mDatas) {
			return;
		}
		List<String> tempDatas = mDatas.get(parentKey);
		if (null != tempDatas && null != mChildDatas) {
			mChildDatas.clear();
			mChildDatas.addAll(tempDatas);
		}
		if (null == mChildAdapter) {
			mChildAdapter = new ArrayAdapter<String>(mCtx,
					R.layout.floating_two_menu_item,
					R.id.floating_two_menu_item_tv_tips, mChildDatas);
			mListViewChild.setAdapter(mChildAdapter);
		} else {
			mChildAdapter.notifyDataSetChanged();
		}
	}

	private List<String> getParentDatas() {
		if (null != mDatas) {
			Set<String> set = mDatas.keySet();
			Iterator<String> it = set.iterator();
			List<String> datas = new ArrayList<String>();
			while (it.hasNext()) {
				datas.add(it.next());
			}
			return datas;
		}
		return null;
	}

	private String getParentKey(int pos) {
		List<String> datas = this.getParentDatas();
		if (null != datas && pos <= datas.size() - 1) {
			return datas.get(pos);
		}
		return "";
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (null != mOnItemClickListener) {
			mOnItemClickListener.onFloatingItemClick(position,
					mChildDatas.get(position), mType);
		}
		this.dismiss();
	}

}
