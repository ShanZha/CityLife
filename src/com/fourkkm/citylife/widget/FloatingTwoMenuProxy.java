package com.fourkkm.citylife.widget;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.utils.CommonUtil;

/**
 * 漂浮的二级菜单代理
 * 
 * @author ShanZha
 * 
 */
public class FloatingTwoMenuProxy implements OnItemClickListener,
		OnClickListener {

	private static final int LEVEL_PARENT = 1;
	private static final int LEVEL_CHILD = 2;
	private Context mCtx;
	private ListView mListViewParent;
	private ListView mListViewChild;
	private BaseAdapter mParentAdapter;
	private BaseAdapter mChildAdapter;
	private PopupWindow mPopWindow;
	private List<String> mParentDatas, mChildDatas;
	private Map<String, List<String>> mDatas;
	private IFloatingItemClick mOnItemClickListener;
	private int mType = -1;

	private String mStrAll = "";
	private String mCurrParent = "";

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
		view.setOnClickListener(this);
		// mListView.setOnItemClickListener(this);
		mListViewChild = (ListView) view
				.findViewById(R.id.floating_two_menu_listview_child);
		mListViewParent.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				String parentKey = getParentKey(position);
				mCurrParent = parentKey;
				resetChildDatas(parentKey);
				((FloatingAdapter) mParentAdapter).mCurrName = parentKey;
				((FloatingAdapter) mChildAdapter).mCurrName = FloatingAdapter.INVALIDA_NAME;
				notifyParentDataChanged();
				notifyChildDataChanged();
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
		mChildAdapter = new FloatingAdapter(mCtx, mChildDatas, LEVEL_CHILD);
		mListViewChild.setAdapter(mChildAdapter);
		mParentAdapter = new FloatingAdapter(mCtx, mParentDatas, LEVEL_PARENT);
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
		mCurrParent = mStrAll;

	}

	/**
	 * 设置数据
	 * 
	 * @param datas
	 */
	public void setDatas(Map<String, List<String>> datas) {
		this.mDatas = datas;
		this.addDefaultAll();
		this.setParentDatas();
		this.notifyParentDataChanged();
		this.resetChildDatas(mStrAll);
		this.notifyChildDataChanged();
		this.resetViewHeightByFirstLevel();
	}

	private void addDefaultAll() {
		if (null == mDatas) {
			return;
		}
		List<String> all = new ArrayList<String>();
		all.add(GlobalConfig.FloatingStr.STR_ALL_CHILD);
		mDatas.put(mStrAll, all);
	}

	/**
	 * 根据第一级的高度设置第二级，保持整体高度一致
	 */
	private void resetViewHeightByFirstLevel() {
		if (null == mParentAdapter) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < mParentAdapter.getCount(); i++) {
			View listItem = mParentAdapter.getView(i, null, mListViewParent);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight += 200; // 额外加一些
		ViewGroup.LayoutParams params = mListViewParent.getLayoutParams();
		params.height = totalHeight + mListViewParent.getDividerHeight()
				* (mParentAdapter.getCount() - 1);
		int screenHeight = CommonUtil.getScreenHeight(mCtx);
		// 调整大小
		if (params.height > screenHeight) {
			params.height = screenHeight - 200;
		}
		mListViewParent.setLayoutParams(params);
		mListViewChild.setLayoutParams(params);
	}

	public void setFloatingItemClickListener(IFloatingItemClick listener) {
		this.mOnItemClickListener = listener;
	}

	public void setFloatingDismissListener(OnDismissListener listener) {
		if (null != mPopWindow) {
			mPopWindow.setOnDismissListener(listener);
		}
	}

	/**
	 * 根据条件重置数据以及选中项
	 * 
	 * @param key
	 */
	public void resetDataByKey(String childKey) {
		if (GlobalConfig.FloatingStr.STR_ALL_CHILD.equals(childKey)) {
			mCurrParent = mStrAll;
		} else {
			String parentKey = this.getParentKeyByChildKey(childKey);
			if (TextUtils.isEmpty(parentKey)) {
				parentKey = this.getParentKey(childKey);
				childKey = GlobalConfig.FloatingStr.STR_ALL_CHILD;
			}
			mCurrParent = parentKey;
		}
		this.resetChildDatas(mCurrParent);
		((FloatingAdapter) mParentAdapter).mCurrName = mCurrParent;
		((FloatingAdapter) mChildAdapter).mCurrName = childKey;
		this.notifyParentDataChanged();
		this.notifyChildDataChanged();
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
		if (null != mParentAdapter) {
			mParentAdapter.notifyDataSetChanged();
		}
	}

	private void notifyChildDataChanged() {
		if (null != mChildAdapter) {
			mChildAdapter.notifyDataSetChanged();
		}
	}

	private void resetChildDatas(String parentKey) {
		if (null == mDatas || null == mChildDatas) {
			return;
		}
		List<String> tempDatas = mDatas.get(parentKey);
		if (null != tempDatas) {
			mChildDatas.clear();
			if (this.isNeedAddStrAll()) {
				mChildDatas.add(GlobalConfig.FloatingStr.STR_ALL_CHILD);
			}
			mChildDatas.addAll(tempDatas);
		}
	}

	private String getParentKey(String parentKey) {
		if (null == mDatas) {
			return "";
		}
		Set<String> set = mDatas.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			if (parentKey.equals(key)) {
				return key;
			}
		}
		return "";
	}

	private String getParentKeyByChildKey(String childKey) {
		if (null == mDatas) {
			return "";
		}
		Set<String> set = mDatas.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			List<String> tempList = mDatas.get(key);
			if (null == tempList) {
				continue;
			}
			for (String str : tempList) {
				if (childKey.equals(str)) {
					return key;
				}
			}
		}
		return "";
	}

	private boolean isNeedAddStrAll() {
//		if (GlobalConfig.FloatingType.TYPE_ASK_CATEGORY == mType) {
//			return false;
//		}
		if (mCurrParent.equals(mStrAll)) {
			return false;
		}
		return true;
	}

	private String getStrAll() {
		if (null != mDatas) {
			Set<String> set = mDatas.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String temp = it.next();
				if (mStrAll.equals(temp)) {
					return temp;
				}
			}
			return "";
		}
		return "";
	}

	private void setParentDatas() {
		if (null != mDatas) {
			Set<String> set = mDatas.keySet();
			Iterator<String> it = set.iterator();

			mParentDatas.add(mStrAll);
			while (it.hasNext()) {
				String temp = it.next();
				if (!mStrAll.equals(temp)) {
					mParentDatas.add(temp);
				}
			}
		}
	}

	private String getParentKey(int pos) {
		if (null == mParentDatas) {
			return "";
		}
		if (pos <= mParentDatas.size() - 1) {
			return mParentDatas.get(pos);
		}
		return "";
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (null != mOnItemClickListener) {
			String child = mChildDatas.get(position);
			if (GlobalConfig.FloatingStr.STR_ALL_CHILD.equals(child)) {
				mOnItemClickListener
						.onFloatingItemClick(-1, mCurrParent, mType);
			} else {
				mOnItemClickListener
						.onFloatingItemClick(position, child, mType);
			}
		}
		this.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.dismiss();
	}

}
