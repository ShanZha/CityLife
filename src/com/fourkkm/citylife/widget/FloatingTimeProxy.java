package com.fourkkm.citylife.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TimePicker;

import com.fourkkm.citylife.R;

/**
 * 时间选择漂浮
 * 
 * @author ShanZha
 * 
 */
public class FloatingTimeProxy implements OnClickListener, OnDismissListener {

	private DatePicker mDpDate;
	private TimePicker mTpTime;
	private Context mCtx;
	private PopupWindow mPopWindow;
	private View mParent;
	private IFloatingTimeListener mTimeListener;
	private int mType = -1;

	public FloatingTimeProxy(Context context, IFloatingTimeListener listener) {
		this.mCtx = context;
		this.mTimeListener = listener;
		this.prepare();
	}

	private void prepare() {
		mParent = LayoutInflater.from(mCtx).inflate(R.layout.floating_time,
				null);
		mDpDate = (DatePicker) mParent
				.findViewById(R.id.floating_time_date_picker);
		mTpTime = (TimePicker) mParent
				.findViewById(R.id.floating_time_time_picker);
		Button mBtnSure = (Button) mParent
				.findViewById(R.id.floating_time_btn_sure);
		Button mBtnCancel = (Button) mParent
				.findViewById(R.id.floating_time_btn_cancel);
		LinearLayout llContent = (LinearLayout) mParent
				.findViewById(R.id.floating_time_ll_content);
		mBtnSure.setOnClickListener(this);
		mBtnCancel.setOnClickListener(this);
		mParent.setOnClickListener(this);
		llContent.setOnClickListener(this);
		mTpTime.setIs24HourView(true);
		mPopWindow = new PopupWindow(mParent, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// 设置PopupWindow外部区域是否可触摸
		mPopWindow.setOutsideTouchable(true);
		mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopWindow.setFocusable(true);
		mPopWindow.setTouchable(true);
		mPopWindow.setOnDismissListener(this);

	}

	public void setType(int type) {
		this.mType = type;
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.floating_time_ll_parent:
			this.dismiss();
			break;
		case R.id.floating_time_ll_content:
			// Do nothing
			break;
		case R.id.floating_time_btn_sure:
			if (null != mTimeListener) {
				mTimeListener.onCurrTime(mType, this.buildTime());
			}
			this.dismiss();
			break;
		case R.id.floating_time_btn_cancel:
			this.dismiss();
			break;
		}
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
	}

	private String buildTime() {
		if (null == mDpDate || null == mTpTime) {
			return "";
		}
		int year = mDpDate.getYear();
		int month = mDpDate.getMonth();
		month += 1;// 从0-11
		int day = mDpDate.getDayOfMonth();
		int hour = mTpTime.getCurrentHour();
		int minute = mTpTime.getCurrentMinute();
		StringBuilder sb = new StringBuilder();
		sb.append(String.valueOf(year));
		sb.append("-");
		if (month < 10) {
			sb.append("0");
		}
		sb.append(String.valueOf(month));
		sb.append("-");
		if (day < 10) {
			sb.append("0");
		}
		sb.append(String.valueOf(day));
		sb.append(" ");
		if (hour < 10) {
			sb.append("0");
		}
		sb.append(String.valueOf(hour));
		sb.append(":");
		if (minute < 10) {
			sb.append("0");
		}
		sb.append(String.valueOf(minute));
		return sb.toString();
	}
}
