package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskCategory;
import com.andrnes.modoer.ModoerAsks;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.AskCategoryManager;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.fourkkm.citylife.widget.SpinnerAdapter;
import com.zj.app.BaseActivity;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

/**
 * 添加问题
 * 
 * @author ShanZha
 * 
 */
public class AskAddActivity extends BaseActivity implements
		OnItemSelectedListener {

	private static final String TAG = "AskAddActivity";
	private static final int REQ_LOGIN_CODE = 1;
	private TextView mTvTitle;
	private EditText mEtSubject, mEtContent, mEtRewardPoint, mEtKeywords;
	private Spinner mSpCategoryParent, mSpCategoryChild;

	private BaseAdapter mAdapterCategotyParent, mAdapterCategoryChild;
	private List<String> mCategoryParentList, mCategoryChildList;

	private AskCategoryManager mAskCategoryMgr;
	private ProgressDialogProxy mDialogProxy;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.ask_add);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtSubject = (EditText) this.findViewById(R.id.ask_add_et_title);
		mEtContent = (EditText) this.findViewById(R.id.ask_add_et_content);
		mEtRewardPoint = (EditText) this
				.findViewById(R.id.ask_add_et_reward_point);
		mEtKeywords = (EditText) this.findViewById(R.id.ask_add_et_keyword);
		mSpCategoryParent = (Spinner) this
				.findViewById(R.id.ask_add_spinner_category_parent);
		mSpCategoryChild = (Spinner) this
				.findViewById(R.id.ask_add_spinner_category_child);

		mSpCategoryParent.setOnItemSelectedListener(this);
		mTvTitle.setText(this.getString(R.string.ask_title_me));
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		// 如果没有登录，先登录
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE);
			return;
		}
		this.prepare();
	}

	private void prepare() {
		mDialogProxy = new ProgressDialogProxy(this);
		mAskCategoryMgr = new AskCategoryManager();
		mCategoryParentList = new ArrayList<String>();
		mCategoryChildList = new ArrayList<String>();

		this.fetchAskCategory();
	}

	private void fetchAskCategory() {
		String selectCode = "from com.andrnes.modoer.ModoerAskCategory";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_ASK_CATEGORY);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerAskCategory(),
				param);
	}

	@SuppressWarnings("unchecked")
	private void notifyCategoryParentDataChanged() {
		if (null == mAdapterCategotyParent) {
			mAdapterCategotyParent = new SpinnerAdapter(this,
					android.R.layout.simple_spinner_item, mCategoryParentList);
			mSpCategoryParent.setAdapter(mAdapterCategotyParent);
			((ArrayAdapter<String>) mAdapterCategotyParent)
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		} else {
			mAdapterCategotyParent.notifyDataSetChanged();
		}
	}

	@SuppressWarnings("unchecked")
	private void notifyCategoryChildDataChanged() {
		if (null == mAdapterCategoryChild) {
			mAdapterCategoryChild = new SpinnerAdapter(this,
					android.R.layout.simple_spinner_item, mCategoryChildList);
			mSpCategoryChild.setAdapter(mAdapterCategoryChild);
			((ArrayAdapter<String>) mAdapterCategoryChild)
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		} else {
			mAdapterCategoryChild.notifyDataSetChanged();
		}
	}

	private boolean validate() {
		String rewardPoint = mEtRewardPoint.getText().toString().trim();
		if (TextUtils.isEmpty(rewardPoint)) {
			this.showToast("悬赏积分不能为空");
			return false;
		}
		return true;
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickAdd(View view) {// 提交
		String subject = mEtSubject.getText().toString().trim();
		String content = mEtContent.getText().toString().trim();
		String rewardPoint = mEtRewardPoint.getText().toString().trim();
		String keywords = mEtKeywords.getText().toString().trim();

		if (!this.validate()) {
			return;
		}
		ModoerAsks ask = new ModoerAsks();
		ask.setSubject(subject);
		ask.setContent(content);
		ask.setReward(Integer.valueOf(rewardPoint));
		ask.setKeywords(keywords);
		ask.setDateline((int) CommonUtil.getCurrTimeByPHP());

		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		ask.setAuthor(member.getUsername());
		ask.setUid(member);
		ask.setCityId(((CoreApp) AppUtils.getBaseApp(this)).getCurrArea());
		String childName = mSpCategoryChild.getSelectedItem() + "";
		ModoerAskCategory childCategory = mAskCategoryMgr
				.getAskCategoryByName(childName);
		ask.setCatid(childCategory);

		mDialogProxy.showDialog();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_ASK);
		this.getStoreOperation().saveOrUpdate(ask, param);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK == resultCode && REQ_LOGIN_CODE == requestCode) {
			this.prepare();
		} else {
			this.finish();
		}
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
		for (int i = 0; i < results.size(); i++) {
			ModoerAskCategory category = (ModoerAskCategory) results.get(i);
			mAskCategoryMgr.add(category);
		}
		try {
			// Step 1:设置父级问题类别数据
			List<String> parentNames = mAskCategoryMgr.getParentNameList();
			mCategoryParentList.addAll(parentNames);
			this.notifyCategoryParentDataChanged();
			// Step 2: 获取父级类别第一项做子级的父类数据
			ModoerAskCategory parentCategory = mAskCategoryMgr
					.getAskCategoryByName(mCategoryParentList.get(0));
			List<String> childNames = mAskCategoryMgr
					.getAskCategoryChild(parentCategory.getId());
			mCategoryChildList.addAll(childNames);
			this.notifyCategoryChildDataChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerAsks.class.getName());
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerMembers.class.getName());
		mDialogProxy.hideDialog();
		this.showToast(this.getString(R.string.add_success));
		this.finish();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_ASK == operator) {
			mDialogProxy.hideDialog();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		try {
			String parentName = mCategoryParentList.get(position);
			// 父级类别改变时，重置子级类别数据，实现连动
			ModoerAskCategory parentCategory = mAskCategoryMgr
					.getAskCategoryByName(parentName);
			List<String> childNames = mAskCategoryMgr
					.getAskCategoryChild(parentCategory.getId());
			mCategoryChildList.clear();
			mCategoryChildList.addAll(childNames);
			// mSpCategoryChild.setSelection(0);
			this.notifyCategoryChildDataChanged();
		} catch (Exception e) {
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}
