package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskCategory;
import com.andrnes.modoer.ModoerAsks;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerAskItemView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.FloatingTwoMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * �ʴ��б�
 * 
 * @author ShanZha
 * 
 */
public class AskListActivity extends BaseListActivity implements
		IFloatingItemClick {

	// ���������ʵ����
	private static final int ASK_STATE_UNRESOLVED = 0;
	private static final int ASK_STATE_RESOLVED = 1;
	private static final int ASK_STATE_REWARD = 3;

	private static final int ASK_CATEGORY_LEVEL_PARENT = 0;
	private static final int ASK_CATEGORY_LEVEL_CHILD = 1;

	private LinearLayout mLlTopCheck;
	private ProgressBar mProBarTopCheck;
	private TextView mTvCategory, mTvState;
	private List<ModoerAsks> mAskList;
	private List<ModoerAskCategory> mAskCategoryList;
	/** ״̬�����б� **/
	private List<String> mAskStateList;
	/** ��ǰ����ʾ�������Ĭ�����ѡ���һ��������� **/
	private ModoerAskCategory mCurrAskCategory;
	/** Ĭ��δ��� **/
	private int mCurrAskState = ASK_STATE_UNRESOLVED;

	private FloatingTwoMenuProxy mFloatingCategory;
	private FloatingOneMenuProxy mFloatingState;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.ask_list);
		mListView = (ListView) this.findViewById(R.id.ask_list_listview);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.ask_list_ll_top_check);
		mProBarTopCheck = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mTvCategory = (TextView) this.findViewById(R.id.ask_list_tv_category);
		mTvState = (TextView) this.findViewById(R.id.ask_list_tv_state);
		super.prepareViews();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mAskList = new ArrayList<ModoerAsks>();
		mAskCategoryList = new ArrayList<ModoerAskCategory>();
		mAskStateList = new ArrayList<String>();
		mAdapter = new ItemSingleAdapter<ModoerAskItemView, ModoerAsks>(
				mAskList, this);
		mListView.setAdapter(mAdapter);

		mFloatingCategory = new FloatingTwoMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_ASK_CATEGORY);
		mFloatingState = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_ASK_STATE);
		mFloatingCategory.setFloatingItemClickListener(this);
		mFloatingState.setFloatingItemClickListener(this);

		this.prepareAskStateNames();
		mFloatingState.setDatas(mAskStateList);

		this.showLoadingCategory();
		this.fetchAskCategory();
	}

	private void prepareAskStateNames() {
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
		if (null == mCurrAskCategory) {
			return;
		}
		String selectCode = this.buildSelectCode();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX);
		paramsMap.put("offset", GlobalConfig.MAX * mPage);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_ASK);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerAsks(), param);
	}

	private void showLoadingCategory() {
		mProBarTopCheck.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mProBarTopCheck.setVisibility(View.GONE);
		mLlTopCheck.setVisibility(View.VISIBLE);
	}

	/**
	 * ���ݵ�ǰ����״̬������ѯ���
	 * 
	 * @return
	 */
	private String buildSelectCode() {
		if (null == mCurrAskCategory) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerAsks ma where ");
		if (ASK_CATEGORY_LEVEL_PARENT == mCurrAskCategory.getUse_area()) {
			// ��ȡ���������id����Ϊ���ݲ������ڸ������
			List<Integer> childIdList = this.getChildIdList();
			if (null != childIdList) {
				int size = childIdList.size();
				for (int i = 0; i < size; i++) {
					sb.append("(");
					sb.append(" ma.catid.id = ");
					sb.append(childIdList.get(i) + "");
					// �������ͻ��ֲ�ѯ�Ƚ����⣬�������⴦����ʱ�ݲ������Ƿ���
					if (ASK_STATE_REWARD == mCurrAskState) {
						sb.append(")");
						if (i == (size - 1)) {
							sb.append(" order by reward DESC");
						} else {
							sb.append(" or ");
						}
					} else {
						sb.append(" and ma.success = ");
						sb.append(mCurrAskState + ")");
						if (i != (size - 1)) {
							sb.append(" or ");
						}
					}
				}
			}
		} else {// �������ֱ�ӻ�ȡ��Ӧ����
			sb.append(" ma.catid.id = " + mCurrAskCategory.getId());
		}
		return sb.toString();
	}

	/**
	 * ��ȡ��ǰ����������������Id
	 * 
	 * @return
	 */
	private List<Integer> getChildIdList() {
		List<Integer> idList = new ArrayList<Integer>();
		int parentId = mCurrAskCategory.getId();
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory category = mAskCategoryList.get(i);
			// �ų�����
			if (ASK_CATEGORY_LEVEL_PARENT == category.getUse_area()) {
				continue;
			}
			int id = category.getId();
			ModoerAskCategory parent = category.getPid();
			if (null != parent && parent.getId() == parentId) {// �и������ǵ�ǰ���
				idList.add(id);
			}
		}
		return idList;
	}

	/**
	 * �����ʴ���������ƹ�ϵ
	 * 
	 * @return
	 */
	private Map<String, List<String>> buildAskCategoryRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mAskCategoryList) {
			return null;
		}
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory ask = mAskCategoryList.get(i);
			if (ASK_CATEGORY_LEVEL_PARENT == ask.getUse_area()) {
				map.put(ask.getName(), this.getAskCategoryChild(ask.getId()));
			}
		}
		return map;
	}

	/**
	 * ��ȡĳ����������Name�б�
	 * 
	 * @param parentId
	 * @return
	 */
	private List<String> getAskCategoryChild(int parentId) {
		if (null == mAskCategoryList) {
			return null;
		}
		List<String> childNames = new ArrayList<String>();
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory ask = mAskCategoryList.get(i);
			if (ASK_CATEGORY_LEVEL_CHILD == ask.getUse_area()) {
				ModoerAskCategory parent = ask.getPid();
				if (null != parent && parentId == parent.getId()) {
					childNames.add(ask.getName());
				}
			}
		}
		return childNames;
	}

	private ModoerAskCategory getAskCategoryByName(String name) {
		if (null == mAskCategoryList) {
			return null;
		}
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory ask = mAskCategoryList.get(i);
			if (name.equals(ask.getName())) {
				return ask;
			}
		}
		return null;
	}

	/**
	 * ����pos��ȡ��ǰ����״̬
	 * 
	 * @param pos
	 * @return
	 */
	private int getStateByPos(int pos) {
		if (null == mAskStateList || pos >= mAskStateList.size()) {
			return ASK_STATE_UNRESOLVED;
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
		mCurrSize = 0;
		if (null != mAskList) {
			mAskList.clear();
		}
		this.setHaveMore(true);
	}

	@Override
	public void notifyLoadStart() {
		// TODO Auto-generated method stub
		super.notifyLoadStart();
		this.fecthAsks();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickCategory(View view) {// ���ѡ��
		if (null == mFloatingCategory) {
			return;
		}
		mFloatingCategory.showAsDropDown(view);
	}

	public void onClickState(View view) {// ״̬ѡ��
		if (null == mFloatingState) {
			return;
		}
		mFloatingState.showAsDropDown(view);
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_ASK_CATEGORY:
			if (null != results) {
				for (int i = 0; i < results.size(); i++) {
					ModoerAskCategory category = (ModoerAskCategory) results
							.get(i);
					// Ĭ������һ��������ǰ���
					if (ASK_CATEGORY_LEVEL_PARENT == category.getUse_area()
							&& null == mCurrAskCategory) {
						mCurrAskCategory = category;
						mTvCategory.setText(category.getName());
						mTvState.setText(this
								.getString(R.string.ask_state_unresolved));
					}
					mAskCategoryList.add(category);
				}
				mFloatingCategory.setDatas(this.buildAskCategoryRelation());
			}
			this.hideLoadingCategory();
			this.notifyLoadStart();
			break;
		case GlobalConfig.Operator.OPERATION_FINDALL_ASK:
			if (null != results) {
				for (int i = 0; i < results.size(); i++) {
					ModoerAsks ask = (ModoerAsks) results.get(i);
					mAskList.add(ask);
				}
				mCurrSize = mAskList.size();
			}
			this.notifyLoadOver();
			break;
		}

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
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
		if (GlobalConfig.FloatingType.TYPE_ASK_CATEGORY == type) {// �������ı�
			if (this.isCurrCategory(key)) {
				return;
			}
			ModoerAskCategory category = this.getAskCategoryByName(key);
			if (null != category) {
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
		// ���¼�������
		this.notifyLoadStart();
	}
}
