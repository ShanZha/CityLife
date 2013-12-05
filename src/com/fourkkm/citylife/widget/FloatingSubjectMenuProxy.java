package com.fourkkm.citylife.widget;

import java.util.ArrayList;
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

import com.andrnes.modoer.ModoerCategory;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.SubjectCategoryManager;
import com.fourkkm.citylife.constant.GlobalConfig;

/**
 * 主题分类漂浮的三级菜单代理
 * 
 * @author ShanZha
 * 
 */
public class FloatingSubjectMenuProxy {

	/** “所有类别”的下标 **/
	private static final int POS_ALL = 0;
	private static final int LEVEL_FIRST = 0;
	private static final int LEVEL_SECOND = 1;
	private static final int LEVEL_THIRD = 2;
	private Context mCtx;
	private ListView mListViewFirst, mListViewSecond, mListViewThird;
	private BaseAdapter mAdapterFirst, mAdapterSecond, mAdapterThird;
	private List<String> mFirstList, mSecondList, mThirdList;
	private PopupWindow mPopWindow;

	/** 数据管家 **/
	private SubjectCategoryManager mCategoryMgr;
	private IFloatingItemClick mFloatingItemClick;
	private int mType = -1;
	/** 第一/二级当前选中Pos，默认均选中第一项 **/
	private int mSelectedPosFirst = 0, mSelectedPosSecond = 0;

	public FloatingSubjectMenuProxy(Context context,
			SubjectCategoryManager categoryMgr, int type) {
		this.mCtx = context;
		this.mCategoryMgr = categoryMgr;
		this.mType = type;
		this.prepare();
	}

	private void prepare() {
		View view = LayoutInflater.from(mCtx).inflate(
				R.layout.floating_subject_menu, null);
		mListViewFirst = (ListView) view
				.findViewById(R.id.floating_subject_menu_listview_first);
		mListViewSecond = (ListView) view
				.findViewById(R.id.floating_subject_menu_listview_second);
		mListViewThird = (ListView) view
				.findViewById(R.id.floating_subject_menu_listview_third);
		mListViewFirst.setOnItemClickListener(mFirstItemClickListener);
		mListViewSecond.setOnItemClickListener(mSecondItemClickListener);
		mListViewThird.setOnItemClickListener(mThirdItemClickListener);
		mPopWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		// 设置PopupWindow外部区域是否可触摸
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);

		mFirstList = new ArrayList<String>();
		mSecondList = new ArrayList<String>();
		mThirdList = new ArrayList<String>();
	}

	public void setFloatingtItemListener(IFloatingItemClick listener) {
		this.mFloatingItemClick = listener;
	}

	/**
	 * 初始数据
	 */
	public void prepareDatas() {
		// 默认三级都添加一个“全部类型”
		mFirstList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		mSecondList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		mThirdList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		List<String> firstNames = mCategoryMgr.getFirstLevelNames();
		mFirstList.addAll(firstNames);
		firstNames = null;
		this.notifyFirstDataChanged();
		this.notifySecondDataChanged();
		this.notifyThirdDataChanged();
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

	private void notifyFirstDataChanged() {
		if (null == mAdapterFirst) {
			mAdapterFirst = new ArrayAdapter<String>(mCtx,
					R.layout.floating_subject_menu_item,
					R.id.floating_subject_menu_item_tv_tips, mFirstList);
			mListViewFirst.setAdapter(mAdapterFirst);
		} else {
			mAdapterFirst.notifyDataSetChanged();
		}
	}

	private void notifySecondDataChanged() {
		if (null == mAdapterSecond) {
			mAdapterSecond = new ArrayAdapter<String>(mCtx,
					R.layout.floating_subject_menu_item,
					R.id.floating_subject_menu_item_tv_tips, mSecondList);
			mListViewSecond.setAdapter(mAdapterSecond);
		} else {
			mAdapterSecond.notifyDataSetChanged();
		}
	}

	private void notifyThirdDataChanged() {
		if (null == mAdapterThird) {
			mAdapterThird = new ArrayAdapter<String>(mCtx,
					R.layout.floating_subject_menu_item,
					R.id.floating_subject_menu_item_tv_tips, mThirdList);
			mListViewThird.setAdapter(mAdapterThird);
		} else {
			mAdapterThird.notifyDataSetChanged();
		}
	}

	/**
	 * 选中第一/二级时，重置第二/三级数据
	 * 
	 * @param name
	 * @param level
	 */
	private void resetDataByName(String name, int level) {
		ModoerCategory category = mCategoryMgr.getCategoryByName(name);
		if (null == category) {
			return;
		}
		switch (level) {
		case LEVEL_FIRST:
			// Do nothing
			break;
		case LEVEL_SECOND:
			List<String> secondNames = mCategoryMgr.getNamesByParentId(category
					.getId());
			mSecondList.clear();
			mSecondList.addAll(secondNames);
			notifySecondDataChanged();
			break;
		case LEVEL_THIRD:
			List<String> thirdNames = mCategoryMgr.getNamesByParentId(category
					.getId());
			mThirdList.clear();
			mThirdList.addAll(thirdNames);
			notifyThirdDataChanged();
			break;
		}
	}

	/**
	 * 第一级Item监听
	 */
	private OnItemClickListener mFirstItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (mSelectedPosFirst == position) {
				return;
			}
			mSelectedPosFirst = position;
			if (POS_ALL == position) {
				mSecondList.clear();
				mSecondList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
				notifySecondDataChanged();
				// 第二级连动第三级
				mThirdList.clear();
				mThirdList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
				notifyThirdDataChanged();
				return;
			}
			String name = mFirstList.get(position);
			resetDataByName(name, LEVEL_SECOND);
			// 第二级连动第三级(第三级默认显示第二级的第一项)
			if (mSecondList.size() > 0) {
				String secondName = mSecondList.get(0);
				resetDataByName(secondName, LEVEL_THIRD);
			}

		}
	};

	/**
	 * 第二级Item监听
	 */
	private OnItemClickListener mSecondItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (mSelectedPosSecond == position) {
				return;
			}
			if (POS_ALL == position) {
				mThirdList.clear();
				mThirdList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
				notifyThirdDataChanged();
				return;
			}
			mSelectedPosSecond = position;
			String name = mSecondList.get(position);
			resetDataByName(name, LEVEL_THIRD);
		}
	};

	/**
	 * 第三级Item监听
	 */
	private OnItemClickListener mThirdItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			String name = mThirdList.get(position);
			if (null != mFloatingItemClick) {
				mFloatingItemClick.onFloatingItemClick(position, name, mType);
			}
			dismiss();
		}
	};

}
