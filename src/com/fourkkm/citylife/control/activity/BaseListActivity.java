package com.fourkkm.citylife.control.activity;

import java.util.List;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;

import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.view.PullUpDownListView.IPullDownListViewListener;
import com.zj.app.BaseFragmentActivity;

public abstract class BaseListActivity extends BaseFragmentActivity implements
		OnItemClickListener, IPullDownListViewListener {

	protected BaseAdapter mAdapter;
	protected PullUpDownListView mListView;
	// protected View mFooterView;

	// protected int mCurrSize = 0;
	// private boolean mIsLoading = false;
	protected int mPage = 0;
	/** �Ƿ��и��� **/
	private boolean mHaveMore = true;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		if (null != mListView) {
			mListView.setOnItemClickListener(this);
			mListView.setPullDownListViewListener(this);
			mListView.setPullRefreshEnable(false);
			mListView.setPullLoadEnable(true);
		}
	}

	protected void notifyDataChanged() {
		if (null == mAdapter) {
			return;
		}
		mAdapter.notifyDataSetChanged();
	}

	protected void setHaveMore(boolean more) {
		this.mHaveMore = more;
	}

	protected void setEnbaleLoad(boolean enable) {
		if (null != mListView) {
			mListView.setPullLoadEnable(enable);
		}
	}

	/**
	 * ��һ�μ������ݻ�������֮����ʾ���ڼ��ص���ͼ
	 */
	protected void onFirstLoadSetting() {
		if (null != mListView) {
			mListView.onFirstLoad();
		}
	}

	/**
	 * ������ϵ���
	 */
	protected void notifyLoadOver() {
		this.notifyDataChanged();
		if (null != mListView) {
			// mListView.stopRefresh();
			// mListView.setRefreshTime(new Date().toLocaleString());
			mListView.stopLoadMore();
			if (!mHaveMore) {
				this.setEnbaleLoad(false);
				mListView.setFooterDividersEnabled(true);
			}
		}
	}

	/**
	 * ���ݷ��ؽ������ҳ���Լ��ж��Ƿ��и���
	 * 
	 * @param out
	 */
	protected void pretreatmentResults(List<Object> results) {
		mPage++;
		if (null == results || results.size() == 0
				|| results.size() < GlobalConfig.MAX) {
			mHaveMore = false;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
	}

}
