package com.fourkkm.citylife.widget;

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.fourkkm.citylife.R;

/**
 * Ư����͸����
 * 
 * @author ShanZha
 * 
 */
public class FloatingTranslucentProxy implements OnClickListener,
		OnItemClickListener {

	private Context mCtx;
	private ListView mListView;
	private BaseAdapter mAdapter;
	private PopupWindow mPopWindow;
	private List<String> mDatas;
	private IFloatingItemClick mOnItemClickListener;
	private int mType = -1;
	private View mParent;

	/**
	 * @param context
	 * @param type
	 *            Ư���Ĳ������ͣ���GlobalConfig.FloatingType
	 */
	public FloatingTranslucentProxy(Context context, int type) {
		this.mCtx = context;
		this.mType = type;
		this.prepare();
	}

	private void prepare() {
		mParent = LayoutInflater.from(mCtx).inflate(
				R.layout.floating_translucent, null);
		mListView = (ListView) mParent
				.findViewById(R.id.floating_translucent_listview);
		mListView.setOnItemClickListener(this);
		mPopWindow = new PopupWindow(mParent, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// ����PopupWindow�ⲿ�����Ƿ�ɴ���
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);

		mParent.setOnClickListener(this);
	}

	/**
	 * ��������
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

	public void showLocation(View parent, int gravity, int x, int y) {
		this.reset();
		if (null != mPopWindow) {
			mPopWindow.showAtLocation(parent, gravity, x, y);
		}
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
		if (null == mDatas) {
			return;
		}
		if (null == mAdapter) {
			mAdapter = new ArrayAdapter<String>(mCtx,
					R.layout.floating_translucent_item,
					R.id.floating_translucent_item_tv_tips, mDatas);
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
			mOnItemClickListener.onFloatingItemClick(position,
					mDatas.get(position), mType);
		}
		this.dismiss();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.floating_translucent_ll_parent:
			this.dismiss();
			break;
		}
	}
}
