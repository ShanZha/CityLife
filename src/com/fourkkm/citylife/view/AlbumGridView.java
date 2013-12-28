package com.fourkkm.citylife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * @Description Ƕ����ListView�е��Զ���GridView
 * @author XingRongJing
 * @time 2013-01-06 ����11:16
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
	 * ���㲢���������߶ȣ������䲻����
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	/**
	 * ��֤��View������Touch�¼�����������һ����������Touch�¼������丸����
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}
}
