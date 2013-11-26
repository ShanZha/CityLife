package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerParty;
import com.andrnes.modoer.ModoerPartyCategory;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerPartyItemView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * �ۻ��б�
 * 
 * @author ShanZha
 * 
 */
public class PartyListActivity extends BaseListActivity implements
		IFloatingItemClick {

	/** ȫ��״̬ **/
	private static final int PARTY_STATE_ALL = 0;
	/** ������ **/
	private static final int PARTY_STATE_SIGNNING = 1;
	/** ������ **/
	private static final int PARTY_STATE_ONGOING = 2;
	/** �ѽ��� **/
	private static final int PARTY_STATE_OVER = 3;
	/** ���·��� **/
	private static final int PARTY_MOST_NEW = 0;
	/** �������� **/
	private static final int PARTY_MOST_POPULAR = 1;
	private ProgressBar mProBarTopCheck;
	private LinearLayout mLlTopCheck;
	private TextView mTvCategory, mTvState, mTvMost;

	private List<ModoerParty> mPartyList;
	private List<ModoerPartyCategory> mPartyCategoryList;
	/** ��ǰ��� **/
	private ModoerPartyCategory mCurrCategory;
	private int mCurrState = PARTY_STATE_ALL;
	private int mCurrMost = PARTY_MOST_NEW;
	/** ����Ư����ͼ���� **/
	private FloatingOneMenuProxy mFloatingCategory, mFloatingState,
			mFloatingMost;
	private List<String> mStateList, mMostList;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.party_list);
		mProBarTopCheck = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.party_list_ll_top_check);
		mTvCategory = (TextView) this.findViewById(R.id.party_list_tv_category);
		mTvState = (TextView) this.findViewById(R.id.party_list_tv_state);
		mTvMost = (TextView) this.findViewById(R.id.party_list_tv_most);
		mListView = (ListView) this.findViewById(R.id.party_list_listview);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		this.prepareFloatingDatas();
		mPartyList = new ArrayList<ModoerParty>();
		mAdapter = new ItemSingleAdapter<ModoerPartyItemView, ModoerParty>(
				mPartyList, this);
		mListView.setAdapter(mAdapter);

		this.showLoadingCategory();
		this.fetchPartyCategory();
	}

	private void prepareFloatingDatas() {
		mPartyCategoryList = new ArrayList<ModoerPartyCategory>();
		mStateList = new ArrayList<String>();
		mMostList = new ArrayList<String>();

		mFloatingCategory = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_PARTY_CATEGORY);
		mFloatingState = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_PARTY_STATE);
		mFloatingMost = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_PARTY_MOST);

		mFloatingCategory.setFloatingItemClickListener(this);
		mFloatingState.setFloatingItemClickListener(this);
		mFloatingMost.setFloatingItemClickListener(this);

		mStateList.add(this.getString(R.string.party_state_all));
		mStateList.add(this.getString(R.string.party_state_signning));
		mStateList.add(this.getString(R.string.party_state_ongoing));
		mStateList.add(this.getString(R.string.party_state_over));
		mFloatingState.setDatas(mStateList);

		mMostList.add(this.getString(R.string.party_most_new));
		mMostList.add(this.getString(R.string.party_most_popular));
		mFloatingMost.setDatas(mMostList);
	}

	/**
	 * ��������֮���֪�������Ϣ
	 */
	private void prepareFloadingCategoryDatas() {
		if (null == mPartyCategoryList) {
			return;
		}
		List<String> categoryNameList = new ArrayList<String>();
		for (int i = 0; i < mPartyCategoryList.size(); i++) {
			String name = mPartyCategoryList.get(i).getName();
			categoryNameList.add(name);
		}
		mFloatingCategory.setDatas(categoryNameList);
	}

	private void showLoadingCategory() {
		mProBarTopCheck.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mProBarTopCheck.setVisibility(View.GONE);
		mLlTopCheck.setVisibility(View.VISIBLE);
	}

	private void fetchPartyCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerPartyCategory ";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_PARTY_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerPartyCategory(),
				param);
	}

	private void fetchPartys() {
		String selectCode = this.buildFetchPartys();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_PARTY);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerSubject(), param);
	}

	/**
	 * ���ݵ�ǰ�����齨��ѯ���
	 * 
	 * @return
	 */
	private String buildFetchPartys() {
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerParty mp where mp.status = 1 ");
		// Step 1:�������
		if (null != mCurrCategory) {
			sb.append(" and mp.catid.id = ");
			sb.append(mCurrCategory.getId() + "");
		}
		// Step 2:״̬����
		switch (mCurrState) {
		case PARTY_STATE_ALL:
			break;
		case PARTY_STATE_SIGNNING:
			long currTime = System.currentTimeMillis() / 1000;
			sb.append(" and mp.joinendtime > " + currTime);
			break;
		case PARTY_STATE_ONGOING:
			long currTime2 = System.currentTimeMillis() / 1000;
			sb.append(" and mp.begintime < " + currTime2);
			sb.append(" and mp.endtime > " + currTime2);
			break;
		case PARTY_STATE_OVER:
			long currTime3 = System.currentTimeMillis() / 1000;
			sb.append(" and mp.endtime < " + currTime3);
			break;
		}
		// Step 3:��֮����
		if (PARTY_MOST_NEW == mCurrMost) {
			sb.append(" order by dateline DESC");
		} else if (PARTY_MOST_POPULAR == mCurrMost) {
			sb.append(" order by pageview DESC");
		}

		return sb.toString();
	}

	/**
	 * ���ݵ�ǰ�����״̬������Text
	 */
	private void setTextByCurrValue() {
		switch (mCurrState) {
		case PARTY_STATE_ALL:
			mTvState.setText(this.getString(R.string.party_state_all));
			break;
		case PARTY_STATE_SIGNNING:
			mTvState.setText(this.getString(R.string.party_state_signning));
			break;
		case PARTY_STATE_ONGOING:
			mTvState.setText(this.getString(R.string.party_state_ongoing));
			break;
		case PARTY_STATE_OVER:
			mTvState.setText(this.getString(R.string.party_state_over));
			break;
		}

		if (PARTY_MOST_NEW == mCurrMost) {
			mTvMost.setText(this.getString(R.string.party_most_new));
		} else if (PARTY_MOST_POPULAR == mCurrMost) {
			mTvMost.setText(this.getString(R.string.party_most_popular));
		}

		if (null != mCurrCategory) {
			mTvCategory.setText(mCurrCategory.getName());
		}

	}

	/**
	 * �������ı�ʱ ������
	 */
	private void reset() {
		mPage = 0;
		mCurrSize = 0;
		if (null != mPartyList) {
			mPartyList.clear();
		}
		this.setHaveMore(true);
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickCategory(View view) {// ���
		if (null != mFloatingCategory) {
			mFloatingCategory.showAsDropDown(view);
		}
	}

	public void onClickState(View view) {// ״̬
		if (null != mFloatingState) {
			mFloatingState.showAsDropDown(view);
		}
	}

	public void onClickMost(View view) {// ��֮ϵ��
		if (null != mFloatingMost) {
			mFloatingMost.showAsDropDown(view);
		}
	}

	@Override
	public void notifyLoadStart() {
		// TODO Auto-generated method stub
		super.notifyLoadStart();

		this.fetchPartys();
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		int operator = out.getOperator();
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			return;
		}
		if (GlobalConfig.Operator.OPERATION_FINDALL_PARTY_CATEGORY == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerPartyCategory category = (ModoerPartyCategory) results
						.get(i);
				if (i == 0) {// ���õ�һ��ΪĬ������
					mCurrCategory = category;
				}
				mPartyCategoryList.add(category);
			}
			this.setTextByCurrValue();
			this.prepareFloadingCategoryDatas();
			this.hideLoadingCategory();
			this.notifyLoadStart();
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_PARTY == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerParty party = (ModoerParty) results.get(i);
				mPartyList.add(party);
			}
			mCurrSize = mPartyList.size();
			this.notifyLoadOver();
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		// �Ƿ���Ҫ���ò���������
		boolean isReset = true;
		switch (type) {
		case GlobalConfig.FloatingType.TYPE_PARTY_CATEGORY:
			ModoerPartyCategory category = mPartyCategoryList.get(pos);
			if (category.getId() == mCurrCategory.getId()) {
				isReset = false;
				return;
			}
			mCurrCategory = category;
			break;
		case GlobalConfig.FloatingType.TYPE_PARTY_STATE:
			if (pos == mCurrState) {
				isReset = false;
				return;
			}
			mCurrState = pos;
			break;
		case GlobalConfig.FloatingType.TYPE_PARTY_MOST:
			if (pos == mCurrMost) {
				isReset = false;
				return;
			}
			mCurrMost = pos;
			break;
		}
		if (isReset) {
			this.setTextByCurrValue();
			this.reset();
			this.notifyDataChanged();
			this.notifyLoadStart();
		}
	}
}
