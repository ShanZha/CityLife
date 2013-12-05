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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskCategory;
import com.andrnes.modoer.ModoerAsks;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.ModoerAskItemView;
import com.fourkkm.citylife.view.PullUpDownListView;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.FloatingTwoMenuProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * 问答列表
 * 
 * @author ShanZha
 * 
 */
public class AskListActivity extends BaseListActivity implements
		IFloatingItemClick {

	private static final String TAG = "AskListActivity";
	// 常量定义见实体类
	private static final int ASK_STATE_UNRESOLVED = 0;
	private static final int ASK_STATE_RESOLVED = 1;
	private static final int ASK_STATE_REWARD = 2;
	private static final int ASK_STATE_ALL = 3;

	private static final int ASK_CATEGORY_LEVEL_PARENT = 0;
	private static final int ASK_CATEGORY_LEVEL_CHILD = 1;

	private LinearLayout mLlTopCheck;
	private ProgressBar mProBarTopCheck;
	private TextView mTvTitle,mTvCategory, mTvState;
	private List<ModoerAsks> mAskList;
	private List<ModoerAskCategory> mAskCategoryList;
	/** 状态名字列表 **/
	private List<String> mAskStateList;
	/** 当前的显示问题类别，默认所有，即次对象为空 **/
	private ModoerAskCategory mCurrAskCategory;
	/** 默认所有问题 **/
	private int mCurrAskState = ASK_STATE_ALL;

	private FloatingTwoMenuProxy mFloatingCategory;
	private FloatingOneMenuProxy mFloatingState;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		this.setContentView(R.layout.ask_list);
		mTvTitle = (TextView)this.findViewById(R.id.titlebar_back_right_tv_title);
		mListView = (PullUpDownListView) this
				.findViewById(R.id.ask_list_listview);
		mLlTopCheck = (LinearLayout) this
				.findViewById(R.id.floating_layout_ll_all);
		mProBarTopCheck = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mTvCategory = (TextView) this
				.findViewById(R.id.floating_layout_tv_first);
		mTvState = (TextView) this.findViewById(R.id.floating_layout_tv_second);
		this.findViewById(R.id.floating_layout_ll_third).setVisibility(
				View.GONE);
		
		mTvTitle.setText(this.getString(R.string.ask));
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

	private void showLoadingCategory() {
		mProBarTopCheck.setVisibility(View.VISIBLE);
		mLlTopCheck.setVisibility(View.GONE);
	}

	private void hideLoadingCategory() {
		mProBarTopCheck.setVisibility(View.GONE);
		mLlTopCheck.setVisibility(View.VISIBLE);
	}

	/**
	 * 根据当前类别和状态构建查询语句
	 * 
	 * @return
	 */
	private String buildSelectCode() {
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerAsks");
		if (null == mCurrAskCategory) {// 选择“所有类别”时
			// 按照悬赏积分查询比较特殊，所以特殊处理，此时暂不区别是否解决
			if (ASK_STATE_REWARD == mCurrAskState) {
				sb.append(" order by reward DESC");
			} else if (ASK_STATE_ALL == mCurrAskState) {// “所有问题”
				// do nothing
			} else {
				sb.append(" ma where ma.success = " + mCurrAskState);
			}
		} else {
			sb.append(" ma where ma.catid.id = " + mCurrAskCategory.getId());
			// 按照悬赏积分查询比较特殊，所以特殊处理，此时暂不区别是否解决
			if (ASK_STATE_REWARD == mCurrAskState) {
				sb.append(" order by reward DESC");
			} else if (ASK_STATE_ALL == mCurrAskState) {// “所有问题”
				// do nothing
			} else {
				sb.append(" and ma.success = " + mCurrAskState);
			}
		}
		// if (ASK_CATEGORY_LEVEL_PARENT == mCurrAskCategory.getUse_area()) {
		// sb.append("where ");
		// // 获取所有子类别id，因为数据不存在于父类别中
		// List<Integer> childIdList = this.getChildIdList();
		// if (null != childIdList) {
		// int size = childIdList.size();
		// for (int i = 0; i < size; i++) {
		// sb.append("(");
		// sb.append(" ma.catid.id = ");
		// sb.append(childIdList.get(i) + "");
		// // 按照悬赏积分查询比较特殊，所以特殊处理，此时暂不区别是否解决
		// if (ASK_STATE_REWARD == mCurrAskState) {
		// sb.append(")");
		// if (i == (size - 1)) {
		// sb.append(" order by reward DESC");
		// } else {
		// sb.append(" or ");
		// }
		// } else {
		// sb.append(" and ma.success = ");
		// sb.append(mCurrAskState + ")");
		// if (i != (size - 1)) {
		// sb.append(" or ");
		// }
		// }
		// }
		// }
		// } else {// 子类别，则直接获取对应数据
		// sb.append("where ma.catid.id = " + mCurrAskCategory.getId());
		// }
		return sb.toString();
	}

	/**
	 * 获取当前父类型所有子类型Id
	 * 
	 * @return
	 */
	private List<Integer> getChildIdList() {
		List<Integer> idList = new ArrayList<Integer>();
		int parentId = mCurrAskCategory.getId();
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory category = mAskCategoryList.get(i);
			// 排除父级
			if (ASK_CATEGORY_LEVEL_PARENT == category.getUse_area()) {
				continue;
			}
			int id = category.getId();
			ModoerAskCategory parent = category.getPid();
			if (null != parent && parent.getId() == parentId) {// 有父级且是当前类别
				idList.add(id);
			}
		}
		return idList;
	}

	/**
	 * 建立问答类别父子名称关系
	 * 
	 * @return
	 */
	private Map<String, List<String>> buildAskCategoryRelation() {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		if (null == mAskCategoryList) {
			return null;
		}
		// 加上“所有类型”项
		List<String> all = new ArrayList<String>();
		all.add(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
		map.put(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY, all);
		for (int i = 0; i < mAskCategoryList.size(); i++) {
			ModoerAskCategory ask = mAskCategoryList.get(i);
			if (ASK_CATEGORY_LEVEL_PARENT == ask.getUse_area()) {
				map.put(ask.getName(), this.getAskCategoryChild(ask.getId()));
			}
		}

		return map;
	}

	/**
	 * 获取某类别的所有子Name列表
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
	 * 根据pos获取当前问题状态
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
	 * 选择的类别跟当前类别是否一致
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
	 * 选择问题状态跟当前状态是否一致
	 * 
	 * @param state
	 * @return
	 */
	private boolean isCurrState(int state) {
		return mCurrAskState == state;
	}

	/**
	 * 当条件改变时 ，重置
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
		this.fecthAsks();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRight(View view) {// 添加

	}

	public void onClickFloatingFirst(View view) {// 类别选择
		if (null == mFloatingCategory) {
			return;
		}
		mFloatingCategory.showAsDropDown(view);
	}

	public void onClickFloatingSecond(View view) {// 状态选择
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
		if (null == results) {
			Log.e(TAG, "shan-->results is null");
			return;
		}
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_FINDALL_ASK_CATEGORY:
			for (int i = 0; i < results.size(); i++) {
				ModoerAskCategory category = (ModoerAskCategory) results.get(i);

				mAskCategoryList.add(category);
			}
			mTvCategory.setText(GlobalConfig.FloatingStr.STR_ALL_CATEGOTY);
			mTvState.setText(GlobalConfig.FloatingStr.STR_ALL_ASK);
			mFloatingCategory.setDatas(this.buildAskCategoryRelation());

			this.hideLoadingCategory();
			// 类别查询成功之后，加载数据
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
		}

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_ASK == operator) {
			this.notifyLoadOver();
		}else{
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
		if (GlobalConfig.FloatingType.TYPE_ASK_CATEGORY == type) {// 问题类别改变
			if (this.isCurrCategory(key)) {
				return;
			}
			if (GlobalConfig.FloatingStr.STR_ALL_CATEGOTY.equals(key)) {
				mCurrAskCategory = null;
			} else {
				ModoerAskCategory category = this.getAskCategoryByName(key);
				mCurrAskCategory = category;
			}
			mTvCategory.setText(key);
		} else if (GlobalConfig.FloatingType.TYPE_ASK_STATE == type) {// 问题状态改变
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
		// 重新加载数据
		this.onLoadMore();
	}
}
