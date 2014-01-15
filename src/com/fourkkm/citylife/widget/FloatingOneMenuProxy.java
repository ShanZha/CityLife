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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.fourkkm.citylife.R;
import com.zj.app.utils.CommonUtil;

/**
 * Ư����һ���˵�����
 * 
 * @author ShanZha
 * 
 */
public class FloatingOneMenuProxy implements OnItemClickListener,
		OnClickListener {

	private Context mCtx;
	private View mParent;
	private ListView mListView;
	private BaseAdapter mAdapter;
	private PopupWindow mPopWindow;
	private List<String> mDatas;
	private IFloatingItemClick mOnItemClickListener;
	private int mType = -1;

	/**
	 * @param context
	 * @param type
	 *            Ư���Ĳ������ͣ���GlobalConfig.FloatingType
	 */
	public FloatingOneMenuProxy(Context context, int type) {
		this.mCtx = context;
		this.mType = type;
		this.prepare();
	}

	private void prepare() {
		mParent = LayoutInflater.from(mCtx).inflate(R.layout.floating_one_menu,
				null);
		mListView = (ListView) mParent
				.findViewById(R.id.floating_one_menu_listview);
		mListView.setOnItemClickListener(this);
		mPopWindow = new PopupWindow(mParent, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		mParent.setOnClickListener(this);
		// ����PopupWindow�ⲿ�����Ƿ�ɴ���
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);

		mDatas = new ArrayList<String>();

		mAdapter = new FloatingAdapter(mCtx, mDatas, 2);
		mListView.setAdapter(mAdapter);
	}

	/**
	 * ��������
	 * 
	 * @param datas
	 */
	public void setDatas(List<String> datas) {
		this.mDatas.addAll(datas);
		this.notifyDataChanged();
		this.resetViewHeightByFirstLevel();
	}

	/**
	 * ����ListView�߶�
	 */
	private void resetViewHeightByFirstLevel() {
		if (null == mAdapter) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < mAdapter.getCount(); i++) {
			View listItem = mAdapter.getView(i, null, mListView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}
		totalHeight += 50; // �����һЩ
		ViewGroup.LayoutParams params = mListView.getLayoutParams();
		params.height = totalHeight + mListView.getDividerHeight()
				* (mAdapter.getCount() - 1);
		int screenHeight = CommonUtil.getScreenHeight(mCtx);
		// ������С
		if (params.height > screenHeight) {
			params.height = screenHeight - 200;
		}
		mListView.setLayoutParams(params);
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
	 * Ư����anchor����
	 * 
	 * @param anchor
	 */
	public void showAsDropDown(View anchor) {
		this.reset();
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
		this.reset();
		if (null != mPopWindow) {
			mPopWindow.showAsDropDown(anchor, xoff, yoff);
		}
	}

	public void resetSelectItemBg(String key) {
		((FloatingAdapter) mAdapter).mCurrName = key;
		this.notifyDataChanged();
	}

	private void reset() {
		if (null != mParent && mParent.getParent() != null) {
			((ViewGroup) mParent.getParent()).removeAllViews();
		}
	}

	public void dismiss() {
		if (null != mPopWindow) {
			mPopWindow.dismiss();
		}
	}

	private void notifyDataChanged() {
		if (null != mAdapter) {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (null != mOnItemClickListener) {
			mOnItemClickListener.onFloatingItemClick(position,
					mDatas.get(position), mType);
		}
		this.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		this.dismiss();
	}
}
