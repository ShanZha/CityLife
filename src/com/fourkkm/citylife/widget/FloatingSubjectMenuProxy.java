package com.fourkkm.citylife.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.andrnes.modoer.ModoerCategory;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.SubjectCategoryManager;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.utils.CommonUtil;

/**
 * 主题分类漂浮的三级菜单代理
 * 
 * @author ShanZha
 * 
 */
public class FloatingSubjectMenuProxy implements OnClickListener {

	private Context mCtx;
	private View mParent;
	private ListView mListViewFirst, mListViewSecond, mListViewThird;
	private BaseAdapter mAdapterFirst, mAdapterSecond, mAdapterThird;
	private List<String> mFirstList, mSecondList, mThirdList;
	private PopupWindow mPopWindow;

	/** 数据管家 **/
	private SubjectCategoryManager mCategoryMgr;
	private IFloatingItemClick mFloatingItemClick;
	private int mType = -1;
	/** 第一/二级当前选中Pos **/
	private int mSelectedPosFirst = -1, mSelectedPosSecond = -1;
	/** 当前选中的主题分类 -全部时为空 **/
	private ModoerCategory mCurrCategory;

	public FloatingSubjectMenuProxy(Context context,
			SubjectCategoryManager categoryMgr, int type) {
		this.mCtx = context;
		this.mCategoryMgr = categoryMgr;
		this.mType = type;
		this.prepare();
	}

	private void prepare() {
		mParent = LayoutInflater.from(mCtx).inflate(
				R.layout.floating_subject_menu, null);
		mListViewFirst = (ListView) mParent
				.findViewById(R.id.floating_subject_menu_listview_first);
		mListViewSecond = (ListView) mParent
				.findViewById(R.id.floating_subject_menu_listview_second);
		mListViewThird = (ListView) mParent
				.findViewById(R.id.floating_subject_menu_listview_third);
		mListViewFirst.setOnItemClickListener(mFirstItemClickListener);
		mListViewSecond.setOnItemClickListener(mSecondItemClickListener);
		mListViewThird.setOnItemClickListener(mThirdItemClickListener);
		mPopWindow = new PopupWindow(mParent, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 设置PopupWindow外部区域是否可触摸
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);
		mParent.setOnClickListener(this);

		mFirstList = new ArrayList<String>();
		mSecondList = new ArrayList<String>();
		mThirdList = new ArrayList<String>();
	}

	public void setFloatingtItemListener(IFloatingItemClick listener) {
		this.mFloatingItemClick = listener;
	}

	public void setFloatingDismissListener(OnDismissListener listener) {
		if (null != mPopWindow) {
			mPopWindow.setOnDismissListener(listener);
		}
	}

	/**
	 * 初始数据
	 */
	public void prepareDatas() {
		// 默认一级、二级都添加一个“全部类型”
		mFirstList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		mSecondList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		// mThirdList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		List<String> firstNames = mCategoryMgr.getFirstLevelNames();
		mFirstList.addAll(firstNames);
		firstNames = null;
		this.notifyFirstDataChanged();
		this.notifySecondDataChanged();
		this.notifyThirdDataChanged();

		this.resetViewHeightByFirstLevel();
	}

	/**
	 * 根据第一级的高度设置其他两级，保持整体高度一致
	 */
	private void resetViewHeightByFirstLevel() {
		if (null == mAdapterFirst) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < mAdapterFirst.getCount(); i++) {
			View listItem = mAdapterFirst.getView(i, null, mListViewFirst);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight += 200; // 额外加一些
		ViewGroup.LayoutParams params = mListViewFirst.getLayoutParams();
		params.height = totalHeight + mListViewFirst.getDividerHeight()
				* (mAdapterFirst.getCount() - 1);
		int screenHeight = CommonUtil.getScreenHeight(mCtx);
		// 调整大小
		if (params.height > screenHeight) {
			params.height = screenHeight - 200;
		}
		mListViewFirst.setLayoutParams(params);
		mListViewSecond.setLayoutParams(params);
		mListViewThird.setLayoutParams(params);
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

	/**
	 * 展示view时，设置选中项及重置数据
	 * 
	 * @param category
	 */
	public void resetByCategory(ModoerCategory category) {
		this.mCurrCategory = category;
		if (null != this.mCurrCategory) {
			int level = mCurrCategory.getLevel();
			switch (level) {
			case SubjectCategoryManager.LEVEL_FIRST:
				this.resetDataByName(mCurrCategory.getName(), level);
				break;
			case SubjectCategoryManager.LEVEL_SECOND:
				ModoerCategory parent = mCurrCategory.getPid();
				if (null != parent) {
					this.resetDataByName(parent.getName(), parent.getLevel());
				}
				break;
			case SubjectCategoryManager.LEVEL_THIRD:
				ModoerCategory parent2 = mCurrCategory.getPid();
				if (null != parent2) {
					this.resetDataByName(parent2.getName(), parent2.getLevel());
					ModoerCategory grandfather = parent2.getPid();
					if (null != grandfather) {
						this.resetDataByName(grandfather.getName(),
								grandfather.getLevel());
					}
				}
				break;
			}
		}
		this.setSelectedItemBackgroud();
	}

	/**
	 * 处理选中的背景(分1、2、3级，对应三个方法)
	 */
	private void setSelectedItemBackgroud() {
		if (null == mCurrCategory) {
			((FloatingAdapter) mAdapterFirst).mCurrName = GlobalConfig.FloatingStr.STR_ALL_CATEGOTY;
			this.notifyFirstDataChanged();
			mSecondList.clear();
			mSecondList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
			notifySecondDataChanged();
		} else {
			int level = mCurrCategory.getLevel();
			switch (level) {
			case SubjectCategoryManager.LEVEL_FIRST:
				this.setFirstLevelBackgroud(mCurrCategory);
				break;
			case SubjectCategoryManager.LEVEL_SECOND:
				this.setSecondLevelBackgroud(mCurrCategory);
				break;
			case SubjectCategoryManager.LEVEL_THIRD:
				this.setThirdLevelBackgroud(mCurrCategory);
				break;
			}
		}
	}

	private void setFirstLevelBackgroud(ModoerCategory category) {
		String name = category.getName();
		for (int i = 0; i < mFirstList.size(); i++) {
			String key = mFirstList.get(i);
			if (name.equals(key)) {
				((FloatingAdapter) mAdapterFirst).mCurrName = name;
				break;
			}
		}
		this.notifyFirstDataChanged();
	}

	private void setSecondLevelBackgroud(ModoerCategory category) {
		// Step 1：处理自己
		String name = category.getName();
		ModoerCategory parent = null;
		for (int i = 0; i < mSecondList.size(); i++) {
			String key = mSecondList.get(i);
			if (name.equals(key)) {
				((FloatingAdapter) mAdapterSecond).mCurrName = name;
				parent = category.getPid();
				break;
			}
		}
		this.notifySecondDataChanged();
		// Step 2：处理父级
		if (null != parent) {
			this.setFirstLevelBackgroud(parent);
		}
	}

	private void setThirdLevelBackgroud(ModoerCategory category) {
		// Step 1：处理自己
		String name = category.getName();
		ModoerCategory parent = null;
		for (int i = 0; i < mThirdList.size(); i++) {
			String key = mThirdList.get(i);
			if (name.equals(key)) {
				((FloatingAdapter) mAdapterThird).mCurrName = name;
				parent = category.getPid();
				break;
			}
		}
		this.notifyThirdDataChanged();
		// Step 2：处理父级（祖父级在父级自动处理）
		if (null != parent) {
			this.setSecondLevelBackgroud(parent);
		}

	}

	public void dismiss() {
		mSelectedPosSecond = -1;
		mSelectedPosFirst = -1;
		if (null != mPopWindow) {
			mPopWindow.dismiss();
		}
	}

	private void notifyFirstDataChanged() {
		if (null == mAdapterFirst) {
			mAdapterFirst = new FloatingAdapter(mCtx, mFirstList,
					SubjectCategoryManager.LEVEL_FIRST);
			mListViewFirst.setAdapter(mAdapterFirst);
		} else {
			mAdapterFirst.notifyDataSetChanged();
		}
	}

	private void notifySecondDataChanged() {
		if (null == mAdapterSecond) {
			mAdapterSecond = new FloatingAdapter(mCtx, mSecondList,
					SubjectCategoryManager.LEVEL_SECOND);
			mListViewSecond.setAdapter(mAdapterSecond);
		} else {
			mAdapterSecond.notifyDataSetChanged();
		}
	}

	private void notifyThirdDataChanged() {
		if (null == mAdapterThird) {
			mAdapterThird = new FloatingAdapter(mCtx, mThirdList,
					SubjectCategoryManager.LEVEL_THIRD);
			mListViewThird.setAdapter(mAdapterThird);
		} else {
			mAdapterThird.notifyDataSetChanged();
		}
	}

	/**
	 * 选中第一/二级时，重置第二/三级数据
	 * 
	 * @param parentName
	 * @param parentLevel
	 */
	private void resetDataByName(String parentName, int parentLevel) {
		ModoerCategory category = mCategoryMgr.getCategoryByName(parentName);
		if (null == category) {
			return;
		}
		switch (parentLevel) {
		case SubjectCategoryManager.LEVEL_FIRST:
			List<String> secondNames = mCategoryMgr.getNamesByParentId(category
					.getId());
			mSecondList.clear();
			mSecondList.addAll(secondNames);
			notifySecondDataChanged();
			break;
		case SubjectCategoryManager.LEVEL_SECOND:
			List<String> thirdNames = mCategoryMgr.getNamesByParentId(category
					.getId());
			mThirdList.clear();
			mThirdList.addAll(thirdNames);
			notifyThirdDataChanged();
			break;
		case SubjectCategoryManager.LEVEL_THIRD:
			// Do nothing
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
			String name = mFirstList.get(position);
			// 处理选中状态，并清除其子、孙子类选中状态
			((FloatingAdapter) mAdapterFirst).mCurrName = name;
			((FloatingAdapter) mAdapterSecond).mCurrName = FloatingAdapter.INVALIDA_NAME;
			((FloatingAdapter) mAdapterThird).mCurrName = FloatingAdapter.INVALIDA_NAME;
			notifyFirstDataChanged();

			mSelectedPosFirst = position;
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(name)) {
				mSecondList.clear();
				mSecondList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
				notifySecondDataChanged();
				// // 第二级连动第三级
				mThirdList.clear();
				// mThirdList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
				notifyThirdDataChanged();
				return;
			}
			resetDataByName(name, SubjectCategoryManager.LEVEL_FIRST);
			// // 第二级连动第三级(第三级默认显示第二级的第一项)
			// if (mSecondList.size() > 0) {
			// String secondName = mSecondList.get(0);
			// resetDataByName(secondName, SubjectCategoryManager.LEVEL_THIRD);
			// }
			// 重置第三级
			mThirdList.clear();
			notifyThirdDataChanged();
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
			// 处理选中状态，并相应处理子级
			((FloatingAdapter) mAdapterSecond).mCurrName = mSecondList
					.get(position);
			notifySecondDataChanged();
			((FloatingAdapter) mAdapterThird).mCurrName = FloatingAdapter.INVALIDA_NAME;
			notifyThirdDataChanged();
			mSelectedPosSecond = position;
			String name = mSecondList.get(position);
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(name)) {
				if (null != mFloatingItemClick) {
					mFloatingItemClick.onFloatingItemClick(position, name,
							mType);
				}
				dismiss();
				return;
			}
			ModoerCategory category = mCategoryMgr.getCategoryByName(name);
			List<String> thirdNames = mCategoryMgr.getNamesByParentId(category
					.getId());
			// 如果第三级没有内容了，则响应第二级，否则响应在第三级
			if (null == thirdNames || thirdNames.size() == 0) {
				if (null != mFloatingItemClick) {
					mFloatingItemClick.onFloatingItemClick(position, name,
							mType);
				}
				dismiss();
				return;
			}
			resetDataByName(name, SubjectCategoryManager.LEVEL_SECOND);
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
			((FloatingAdapter) mAdapterThird).mCurrName = mThirdList
					.get(position);
			notifyThirdDataChanged();
			String name = mThirdList.get(position);
			if (null != mFloatingItemClick) {
				mFloatingItemClick.onFloatingItemClick(position, name, mType);
			}
			dismiss();
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.dismiss();
	}

}
