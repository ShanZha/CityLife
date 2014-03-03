package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskAnswer;
import com.andrnes.modoer.ModoerAsks;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.fourkkm.citylife.widget.SpinnerAdapter;
import com.zj.app.BaseActivity;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.support.observer.model.Param;

/**
 * 问题修改或对最佳答案评论
 * 
 * @author ShanZha
 * 
 */
public class AskUpdateActivity extends BaseActivity {

	private static final String TAG = "AskUpdateActivity";
	private TextView mTvTitle;
	private EditText mEtSubject, mEtContent, mEtRewardPoint,
			mEtBestAnswerReview;
	private Spinner mSpBestAnswer;
	private BaseAdapter mAdapter;
	private List<String> mAnswerContentList;

	private ModoerAsks mCurrAsk;
	private List<ModoerAskAnswer> mAskAnswerList;
	private ProgressDialogProxy mDialog;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.ask_update);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtSubject = (EditText) this.findViewById(R.id.ask_update_et_title);
		mEtContent = (EditText) this.findViewById(R.id.ask_update_et_content);
		mEtRewardPoint = (EditText) this
				.findViewById(R.id.ask_update_et_reward_point);
		mEtBestAnswerReview = (EditText) this
				.findViewById(R.id.ask_update_et_best_answer_review);
		mSpBestAnswer = (Spinner) this
				.findViewById(R.id.ask_update_spinner_best_answer);

		mTvTitle.setText(this.getString(R.string.ask_answer_manager));
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mDialog = new ProgressDialogProxy(this);
		mAnswerContentList = new ArrayList<String>();
		mAskAnswerList = new ArrayList<ModoerAskAnswer>();
		Intent intent = this.getIntent();
		mCurrAsk = (ModoerAsks) intent.getSerializableExtra("modoerAsk");
		if (null == mCurrAsk) {
			Log.e(TAG, "mCurrAsk is null");
			return;
		}
		mEtSubject.setText(mCurrAsk.getSubject());
		mEtContent.setText(mCurrAsk.getContent());
		mEtRewardPoint.setText(mCurrAsk.getReward() + "");

		this.fetchOtherAnswers();
	}

	private void fetchOtherAnswers() {
		if (null == mCurrAsk) {
			return;
		}
		String selectCode = "from com.andrnes.modoer.ModoerAskAnswer ma where ma.askid.id = "
				+ mCurrAsk.getId();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerAskAnswer(),
				param);
	}

	@SuppressWarnings("unchecked")
	private void notifyAnswerListDataChanged() {
		if (null == mAdapter) {
			mAdapter = new SpinnerAdapter(this,
					android.R.layout.simple_spinner_item, mAnswerContentList);
			mSpBestAnswer.setAdapter(mAdapter);
			((SpinnerAdapter) mAdapter)
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickUpdateAsk(View view) {// 修改问题
		String askTitle = mCurrAsk.getSubject();
		String askContent = mCurrAsk.getContent();
		int reward = mCurrAsk.getReward();
		String inputTitle = mEtSubject.getText().toString().trim();
		String inputContent = mEtContent.getText().toString().trim();
		String inputReward = mEtRewardPoint.getText().toString().trim();
		if (askTitle.equals(inputTitle) && askContent.equals(inputContent)
				&& reward == Integer.parseInt(inputReward)) {
			this.showToast(this.getString(R.string.ask_update_reward_unchanged));
			return;
		}
		int newReward = Integer.parseInt(inputReward);
		if (newReward < reward) {
			this.showToast(this.getString(R.string.ask_update_reward_desc));
			return;
		}
		mCurrAsk.setSubject(inputTitle);
		mCurrAsk.setContent(inputContent);
		mCurrAsk.setReward(newReward);
		mDialog.showDialog();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_ASK);
		this.getStoreOperation().saveOrUpdate(mCurrAsk, param);

	}

	public void onClickCloseAsk(View view) {// 结束问题
		try {
			int pos = mSpBestAnswer.getSelectedItemPosition();
			if (pos != -1) {
				ModoerAskAnswer bestAnswer = mAskAnswerList.get(pos);
				String bestAnswerReview = mEtBestAnswerReview.getText()
						.toString().trim();
				bestAnswer.setFeel(bestAnswerReview);
				mCurrAsk.setBestanswer(bestAnswer);
				mCurrAsk.setSuccess(1);
				mCurrAsk.setSolvetime((int) CommonUtil.getCurrTimeByPHP());

				mDialog.showDialog();
				Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
				param.setOperator(GlobalConfig.Operator.OPERATION_CLOSE_ASK);
				List<Object> objs = new ArrayList<Object>();
				objs.add(bestAnswer);
				objs.add(mCurrAsk);
				this.getStoreOperation().saveOrUpdateArray(objs, param);
			} else {
				this.showToast(this
						.getString(R.string.ask_close_best_answer_none));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		@SuppressWarnings("unchecked")
		List<Object> results = (List<Object>) out.getResult();
		if (null == results || results.size() == 0) {
			return;
		}
		for (int i = 0; i < results.size(); i++) {
			ModoerAskAnswer answer = (ModoerAskAnswer) results.get(i);
			mAskAnswerList.add(answer);
			mAnswerContentList.add(answer.getContent());
		}
		this.notifyAnswerListDataChanged();
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_SAVE_ASK:
			this.showToast(this.getString(R.string.update_success));
			break;
		}
		Intent intent = new Intent();
		intent.putExtra("modoerAsk", mCurrAsk);
		this.setResult(RESULT_OK, intent);
		mDialog.hideDialog();
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerAsks.class.getName());
		this.finish();
	}

	@Override
	public void onSuccessSaveOrUpdateArray(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdateArray(out);
		this.showToast(this.getString(R.string.commit_success));
		Intent intent = new Intent();
		intent.putExtra("modoerAsk", mCurrAsk);
		this.setResult(RESULT_OK, intent);
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerAsks.class.getName());
		mDialog.hideDialog();
		this.finish();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_SAVE_ASK:
			mDialog.hideDialog();
			this.showToast(this.getString(R.string.update_fail));
			break;
		case GlobalConfig.Operator.OPERATION_CLOSE_ASK:
			mDialog.hideDialog();
			this.showToast(this.getString(R.string.commit_fail));
			break;
		}
	}
}
