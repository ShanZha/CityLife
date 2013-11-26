package com.fourkkm.citylife.control.activity;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.BaseActivity;
import com.zj.support.observer.model.Param;

public class BaseListActivity extends BaseActivity implements
		OnItemClickListener {

	protected BaseAdapter mAdapter;
	protected ListView mListView;
	protected View mFooterView;

	protected int mCurrSize = 0;
	private boolean mIsLoading = false;
	protected int mPage = 0;
	/** 是否还有更多 **/
	private boolean mHaveMore = true;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();

		mFooterView = LayoutInflater.from(this).inflate(
				R.layout.listview_footer, null);
		mListView.addFooterView(mFooterView);
		mListView.setOnItemClickListener(this);
		this.hideLoading();
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

	private void showLoading() {
		mFooterView.setVisibility(View.VISIBLE);
	}

	private void hideLoading() {
		mFooterView.setVisibility(View.GONE);
	}

	public boolean isShouldLoadData(int pos) {
		return (mCurrSize - 1) == pos && !mIsLoading && mHaveMore;
	}

	public void notifyLoadStart() {
		mIsLoading = true;
		this.showLoading();
	}

	public void notifyLoadOver() {
		mIsLoading = false;
		this.notifyDataChanged();
		this.hideLoading();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		mPage++;
		List<Object> results = (List<Object>) out.getResult();
		if (null == results || results.size() == 0
				|| results.size() < GlobalConfig.MAX) {
			mHaveMore = false;
		}
	}

}
