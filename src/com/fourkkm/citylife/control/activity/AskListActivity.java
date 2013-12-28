package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskAnswer;
import com.andrnes.modoer.ModoerAskCategory;
import com.andrnes.modoer.ModoerAsks;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.AskCategoryManager;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerAskItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.FloatingTwoMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * �ʴ��б�
 * 
 * @author ShanZha
 * 
 */
public class AskListActivity extends BaseListActivity implements
		IFloatingItemClick, OnDismissListener {

	private static final String TAG = "AskListActivity";
	// ���������ʵ����
	private static final int ASK_STATE_UNRESOLVED = 0;
	private static final int ASK_STATE_RESOLVED = 1;
	private static final int ASK_STATE_REWARD = 2;
	private static final int ASK_STATE_ALL = 3;

	private LinearLayout mLlTopCheck, mLlTopCheckLoading;
	private RelativeLayout mRlFloatingFirst, mRlFloatingSecond;
	private TextView mTvTitle, mTvCategory, mTvState;
	private List<ModoerAsks> mAskList;
	/** ״̬�����б� **/
	private List<String> mAskStateList;
	/** ��ǰ����ʾ�������Ĭ�����У����ζ���Ϊ�� **/
	private ModoerAskCategory mCurrAskCategory;
	/** Ĭ���������� **/
	private int mCurrAskState = ASK_STATE_ALL;

	private FloatingTwoMenuProxy mFloatingCategory;
	private FloatingOneMenuProxy mFloatingState;

	private AskCategoryManager mAskCategoryMgr;
	private int mOperator = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.ask_list);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.ask_list_listview);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mLlTopCheckLoading = (LinearLayout) this
				.findViewById(R.id.ask_list_ll_top_loading);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvState = (TextView) this.findViewById(R.id.floating_layout_tv_second);
		mRlFloatingFirst = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_first);
		mRlFloatingSecond = (RelativeLayout) this
				.findViewById(R.id.floating_layout_rl_second);
		this.findViewById(R.id.floating_layout_rl_third).setVisibility(
				View.GONE);

		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mAskList = new ArrayList<ModoerAsks>();
		mAdapter = new ItemSingleAdapter<ModoerAskItemView, ModoerAsks>(
				mAskList, this);
		Intent intent = this.getIntent();
		mOperator = intent.getIntExtra("operator", -1);
		if (GlobalConfig.IntentKey.ASK_ME == mOperator) {
			mLlTopCheck.setVisibility(View.GONE);
			mLlTopCheckLoading.setVisibility(View.GONE);
			mTvTitle.setText(this.getString(R.string.user_my_question));
			mListView.setAdapter(mAdapter);
			this.onFirstLoadSetting();
			this.onLoadMore();
		} else if (GlobalConfig.IntentKey.ASK_ANSWER_ME == mOperator) {
			mLlTopCheck.setVisibility(View.GONE);
			mLlTopCheckLoading.setVisibility(View.GONE);
			mTvTitle.setText(this.getString(R.string.user_my_answer));
			mListView.setAdapter(mAdapter);
			this.onFirstLoadSetting();
			this.onLoadMore();
		} else {
			mTvTitle.setText(this.getString(R.string.ask));
			mAskCategoryMgr = new AskCategoryManager();
			mAskStateList = new ArrayList<String>();

			mFloatingCategory = new FloatingTwoMenuProxy(this,
					GlobalConfig.FloatingType.TYPE_CATEGORY);
			mFloatingState = new FloatingOneMenuProxy(this,
					GlobalConfig.FloatingType.TYPE_ASK_STATE);
			mFloatingCategory.setFloatingItemClickListener(this);
			mFloatingCategory.setFloatingDismissListener(this);
			mFloatingState.setFloatingItemClickListener(this);
			mFloatingState.setFloatingDismissListener(this);

			this.prepareAskStateNames();
			mFloatingState.setDatas(mAskStateList);
			this.showLoadingCategory();
			this.fetchAskCategory();
		}

	}

	private void prepareAskStateNames() {
		mAskStateList.add(GlobalConfig.FloatingStr.STR_ALL_ASK);
		mAskStateList.add(this.getString(R.string.ask_state_unresolved));
		mAskStateList.add(this.getString(R.string.ask_state_resolved));
		mAskStateList.add(this.getString(R.string.ask_state_reward));
	}

	private void fetchAskCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerAskCategory";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_ASK_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerAskCategory(),
				param);
	}

	private void fecthAsks() {
		String selectCode = this.buildSelectCode();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_ASK);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerAsks(), param);
	}

	private void fetchAskAnswer() {
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		if (null == member) {
			return;
		}
		String selectCode = "from com.andrnes.modoer.ModoerAskAnswer maa where maa.uid.id = "
				+ member.getId();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_ASK_ANSWER);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerAskAnswer(), param);
	}

	private void showLoadingCategory() {
		mLlTopCheckLoading.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mLlTopCheckLoading.setVisibility(View.GONE);
		mLlTopCheck.setVisibility(View.VISIBLE);
	}

	/**
	 * ���ݵ�ǰ����״̬������ѯ���
	 * 
	 * @return
	 */
	private String buildSelectCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerAsks");
		if (GlobalConfig.IntentKey.REVIEW_ME == mOperator) {
			ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
					.getCurrMember();
			if (null != member) {
				sb.append(" ma where uid.id = " + member.getId());
			}
			return sb.toString();
		}
		if (null == mCurrAskCategory) {// ѡ���������ʱ
			// �������ͻ��ֲ�ѯ�Ƚ����⣬�������⴦����ʱ�ݲ������Ƿ���
			if (ASK_STATE_REWARD == mCurrAskState) {
				sb.append(" order by reward DESC");
			} else if (ASK_STATE_ALL == mCurrAskState) {// ���������⡱
				// do nothing
			} else {
				sb.append(" ma where ma.success = " + mCurrAskState);
			}
		} else {
			sb.append(" ma where ma.catid.id = " + mCurrAskCategory.getId());
			// �������ͻ��ֲ�ѯ�Ƚ����⣬�������⴦����ʱ�ݲ������Ƿ���
			if (ASK_STATE_REWARD == mCurrAskState) {
				sb.append(" order by reward DESC");
			} else if (ASK_STATE_ALL == mCurrAskState) {// ���������⡱
				// do nothing
			} else {
				sb.append(" and ma.success = " + mCurrAskState);
			}
		}

		return sb.toString();
	}

	/**
	 * ����pos��ȡ��ǰ����״̬
	 * 
	 * @param pos
	 * @return
	 */
	private int getStateByPos(int pos) {
		if (null == mAskStateList || pos >= mAskStateList.size()) {
			return ASK_STATE_ALL;
		}
		switch (pos) {
		case 0:
			return ASK_STATE_UNRESOLVED;
		case 1:
			return ASK_STATE_RESOLVED;
		case 2:
			return ASK_STATE_REWARD;
		}
		return ASK_STATE_UNRESOLVED;
	}

	/**
	 * ѡ���������ǰ����Ƿ�һ��
	 * 
	 * @param name
	 * @return
	 */
	private boolean isCurrCategory(String name) {
		if (null == mCurrAskCategory) {
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(name)) {
				return true;
			}
			return false;
		}
		String currName = mCurrAskCategory.getName();
		return name.equals(currName);
	}

	/**
	 * ѡ������״̬����ǰ״̬�Ƿ�һ��
	 * 
	 * @param state
	 * @return
	 */
	private boolean isCurrState(int state) {
		return mCurrAskState == state;
	}

	/**
	 * �������ı�ʱ ������
	 */
	private void reset() {
		mPage = 0;
		if (null != mAskList) {
			mAskList.clear();
		}
		this.setHaveMore(true);
		this.setEnbaleLoad(true);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		super.onLoadMore();
		if (GlobalConfig.IntentKey.ASK_ANSWER_ME == mOperator) {
			this.fetchAskAnswer();
		} else {
			this.fecthAsks();
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRight(View view) {// ���
		this.startActivity(new Intent(this, AskAddActivity.class));
	}

	public void onClickFloatingFirst(View view) {// ���ѡ��
		if (null == mFloatingCategory) {
			return;
		}
		mFloatingCategory.showAsDropDown(view);
		if (null == mCurrAskCategory) {
			mFloatingCategory
					.resetDataByKey(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		} else {
			mFloatingCategory.resetDataByKey(mCurrAskCategory.getName());
		}
		mRlFloatingFirst
				.setBackgroundResource(R.drawable.floating_item_selected_bg);
	}

	public void onClickFloatingSecond(View view) {// ״̬ѡ��
		if (null == mFloatingState) {
			return;
		}
		mFloatingState.showAsDropDown(view);
		mFloatingState.resetSelectItemBg(mTvState.getText().toString().trim());
		mRlFloatingSecond
				.setBackgroundResource(R.drawable.floating_item_selected_bg);
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan-->results is null");
			return;
		}
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_ASK_CATEGORY:
			for (int i = 0; i < results.size(); i++) {
				ModoerAskCategory category = (ModoerAskCategory) results.get(i);

				mAskCategoryMgr.add(category);
			}
			mTvCategory.setText(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
			mTvState.setText(GlobalConfig.FloatingStr.STR_ALL_ASK);
			mFloatingCategory.setDatas(mAskCategoryMgr
					.buildAskCategoryRelation());

			mListView.setAdapter(mAdapter);
			this.hideLoadingCategory();
			// ����ѯ�ɹ�֮�󣬼�������
			this.onFirstLoadSetting();
			this.onLoadMore();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_ASK:
			for (int i = 0; i < results.size(); i++) {
				ModoerAsks ask = (ModoerAsks) results.get(i);
				mAskList.add(ask);
			}
			this.pretreatmentResults(results);
			this.notifyLoadOver();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_ASK_ANSWER:
			for (int i = 0; i < results.size(); i++) {
				ModoerAskAnswer answer = (ModoerAskAnswer) results.get(i);
				ModoerAsks ask = answer.getAskid();
				if (null != ask && ask.getId() != 0) {
					mAskList.add(ask);
				}
			}
			this.pretreatmentResults(results);
			this.notifyLoadOver();
			break;
		}

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_ASK == operator
				|| GlobalConfig.Operator.OPERATION_FINDALL_ASK_ANSWER == operator) {
			this.notifyLoadOver();
		} else {
			this.hideLoadingCategory();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);

		ModoerAsks ask = (ModoerAsks) parent.getAdapter().getItem(position);
		Intent intent = new Intent(this, AskDetailActivity.class);
		intent.putExtra("modoerAsk", ask);
		this.startActivity(intent);
	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		if (GlobalConfig.FloatingType.TYPE_CATEGORY == type) {// �������ı�
			if (this.isCurrCategory(key)) {
				return;
			}
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(key)) {
				mCurrAskCategory = null;
			} else {
				ModoerAskCategory category = mAskCategoryMgr
						.getAskCategoryByName(key);
				mCurrAskCategory = category;
			}
			mTvCategory.setText(key);
		} else if (GlobalConfig.FloatingType.TYPE_ASK_STATE == type) {// ����״̬�ı�
			int state = this.getStateByPos(pos);
			if (this.isCurrState(state)) {
				return;
			}
			mCurrAskState = state;
			mTvState.setText(key);
		}
		this.reset();
		this.notifyDataChanged();
		this.onFirstLoadSetting();
		// ���¼�������
		this.onLoadMore();
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		mRlFloatingFirst.setBackgroundResource(0);
		mRlFloatingSecond.setBackgroundResource(0);
	}
}
