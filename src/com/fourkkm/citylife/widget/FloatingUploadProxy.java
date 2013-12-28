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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.andrnes.modoer.ModoerPictures;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.itemview.UploadPicItemView;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 漂浮上传框
 * 
 * @author ShanZha
 * 
 */
public class FloatingUploadProxy implements OnItemClickListener {

	private Context mCtx;
	private GridView mGvPics;
	private ImageView mIvClose;
	private Button mBtnCommit;
	private BaseAdapter mAdapter;
	private PopupWindow mPopWindow;
	private List<ModoerPictures> mPicsList;
	private IFloatingItemClick mOnItemClickListener;
	private OnClickListener mOnClickListener;
	private int mType = -1;
	private View mParent;

	/**
	 * @param context
	 * @param type
	 *            漂浮的操作类型，见GlobalConfig.FloatingType
	 */
	public FloatingUploadProxy(Context context, OnClickListener listener,
			int type) {
		this.mCtx = context;
		this.mType = type;
		this.mOnClickListener = listener;
		this.prepare();
	}

	private void prepare() {
		mParent = LayoutInflater.from(mCtx).inflate(R.layout.floating_upload,
				null);
		mGvPics = (GridView) mParent.findViewById(R.id.floating_upload_gv_pics);
		mIvClose = (ImageView) mParent
				.findViewById(R.id.floating_upload_iv_close);
		mBtnCommit = (Button) mParent
				.findViewById(R.id.floating_upload_btn_commit);
		mGvPics.setOnItemClickListener(this);
		mIvClose.setOnClickListener(mOnClickListener);
		mBtnCommit.setOnClickListener(mOnClickListener);

		mPopWindow = new PopupWindow(mParent, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 设置PopupWindow外部区域是否可触摸
		// mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);

	}

	/**
	 * 设置数据
	 * 
	 * @param datas
	 */
	public void setDatas(List<ModoerPictures> datas) {
		this.mPicsList = datas;
		this.notifyDataChanged();
	}

	public void setFloatingItemClickListener(IFloatingItemClick listener) {
		this.mOnItemClickListener = listener;
	}

	public void setOnClickListener(OnClickListener listener) {
		this.mOnClickListener = listener;
	}

	/**
	 * 漂浮在anchor下面
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
	 * 漂浮在anchor下据x、y轴xoff、yoff距离
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
		if (null == mPicsList) {
			return;
		}
		if (null == mAdapter) {
			mAdapter = new ItemSingleAdapter<UploadPicItemView, ModoerPictures>(
					mPicsList, mCtx);
			mGvPics.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		if (null != mOnItemClickListener) {
			mOnItemClickListener.onFloatingItemClick(position, "", mType);
		}
	}
}
