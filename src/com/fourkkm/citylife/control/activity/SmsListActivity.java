package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerPmsgs;
import com.andrnes.modoer.ModoerReview;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerSmsItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 站内短信列表
 * 
 * @author ShanZha
 * 
 */
public class SmsListActivity extends BaseListActivity {

	private TextView mTvTitle;
	private List<ModoerPmsgs> mSmsList;;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.sms_list);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.sms_list_listview);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);

		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mSmsList = new ArrayList<ModoerPmsgs>();
		mAdapter = new ItemSingleAdapter<ModoerSmsItemView, ModoerPmsgs>(
				mSmsList, this);
		mListView.setAdapter(mAdapter);
		mTvTitle.setText(this.getString(R.string.user_my_sms));

		this.onFirstLoadSetting();
		this.onLoadMore();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		super.onLoadMore();

		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerPmsgs mp ");
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		if (null != member) {
			sb.append(" where mp.recvuid.id = " + member.getId());
		}
		sb.append(" order by mp.posttime DESC");

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		this.getStoreOperation().findAll(sb.toString(), paramsMap, true,
				new ModoerReview(),
				new Param(this.hashCode(), GlobalConfig.URL_CONN));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		ModoerPmsgs sms = (ModoerPmsgs) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this, SmsDetailActivity.class);
		intent.putExtra("sms", sms);
		this.startActivity(intent);
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null != results) {
			for (int i = 0; i < results.size(); i++) {
				ModoerPmsgs sms = (ModoerPmsgs) results.get(i);
				// mReviewLists.add(review);
				mSmsList.add(sms);
			}
		}
		this.pretreatmentResults(results);
		this.notifyLoadOver();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.notifyLoadOver();
	}
}
