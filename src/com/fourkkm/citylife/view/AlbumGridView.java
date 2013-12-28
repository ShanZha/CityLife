package com.fourkkm.citylife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * @Description 嵌套于ListView中的自定义GridView
 * @author XingRongJing
 * @time 2013-01-06 上午11:16
 * 
 */
public class AlbumGridView extends GridView {

	public AlbumGridView(Context context) {
		super(context);
	}

	public AlbumGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AlbumGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 计算并设置其最大高度，即让其不滚动
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/**
	 * 保证此View不消耗Touch事件，把其往上一级传，即把Touch事件交给其父处理
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
}
