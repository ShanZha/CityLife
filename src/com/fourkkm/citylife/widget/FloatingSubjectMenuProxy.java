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
 * �������Ư���������˵�����
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

	/** ���ݹܼ� **/
	private SubjectCategoryManager mCategoryMgr;
	private IFloatingItemClick mFloatingItemClick;
	private int mType = -1;
	/** ��һ/������ǰѡ��Pos **/
	private int mSelectedPosFirst = -1, mSelectedPosSecond = -1;
	/** ��ǰѡ�е�������� -ȫ��ʱΪ�� **/
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
		// ����PopupWindow�ⲿ�����Ƿ�ɴ���
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
	 * ��ʼ����
	 */
	public void prepareDatas() {
		// Ĭ��һ�������������һ����ȫ�����͡�
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
	 * ���ݵ�һ���ĸ߶�����������������������߶�һ��
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
		totalHeight += 200; // �����һЩ
		ViewGroup.LayoutParams params = mListViewFirst.getLayoutParams();
		params.height = totalHeight + mListViewFirst.getDividerHeight()
				* (mAdapterFirst.getCount() - 1);
		int screenHeight = CommonUtil.getScreenHeight(mCtx);
		// ������С
		if (params.height > screenHeight) {
			params.height = screenHeight - 200;
		}
		mListViewFirst.setLayoutParams(params);
		mListViewSecond.setLayoutParams(params);
		mListViewThird.setLayoutParams(params);
	}

	/**
	 * Ư����anchor����
	 * 
	 * @param anchor
	 */
	public void showAsDropDown(View anchor) {
		if (null != mPopWindow) {
			mPopWindow.showAsDropDown(anchor);
		}
	}

	/**
	 * Ư����anchor�¾�x��y��xoff��yoff����
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
	 * չʾviewʱ������ѡ�����������
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
	 * ����ѡ�еı���(��1��2��3������Ӧ��������)
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
		// Step 1�������Լ�
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
		// Step 2��������
		if (null != parent) {
			this.setFirstLevelBackgroud(parent);
		}
	}

	private void setThirdLevelBackgroud(ModoerCategory category) {
		// Step 1�������Լ�
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
		// Step 2�����������游���ڸ����Զ�����
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
	 * ѡ�е�һ/����ʱ�����õڶ�/��������
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
	 * ��һ��Item����
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
			// ����ѡ��״̬����������ӡ�������ѡ��״̬
			((FloatingAdapter) mAdapterFirst).mCurrName = name;
			((FloatingAdapter) mAdapterSecond).mCurrName = FloatingAdapter.INVALIDA_NAME;
			((FloatingAdapter) mAdapterThird).mCurrName = FloatingAdapter.INVALIDA_NAME;
			notifyFirstDataChanged();

			mSelectedPosFirst = position;
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(name)) {
				mSecondList.clear();
				mSecondList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
				notifySecondDataChanged();
				// // �ڶ�������������
				mThirdList.clear();
				// mThirdList.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
				notifyThirdDataChanged();
				return;
			}
			resetDataByName(name, SubjectCategoryManager.LEVEL_FIRST);
			// // �ڶ�������������(������Ĭ����ʾ�ڶ����ĵ�һ��)
			// if (mSecondList.size() > 0) {
			// String secondName = mSecondList.get(0);
			// resetDataByName(secondName, SubjectCategoryManager.LEVEL_THIRD);
			// }
			// ���õ�����
			mThirdList.clear();
			notifyThirdDataChanged();
		}
	};

	/**
	 * �ڶ���Item����
	 */
	private OnItemClickListener mSecondItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			if (mSelectedPosSecond == position) {
				return;
			}
			// ����ѡ��״̬������Ӧ�����Ӽ�
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
			// ���������û�������ˣ�����Ӧ�ڶ�����������Ӧ�ڵ�����
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
	 * ������Item����
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
