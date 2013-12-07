package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskAnswer;
import com.andrnes.modoer.ModoerAsks;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.BaseActivity;
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
	private EditText mEtSubject, mEtContent, mEtBestAnswerReview;
	private Spinner mSpBestAnswer;
	private BaseAdapter mAdapter;
	private List<String> mAnswerContentList;

	private ModoerAsks mCurrAsk;
	private List<ModoerAskAnswer> mAskAnswerList;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.ask_update);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mEtSubject = (EditText) this.findViewById(R.id.ask_update_et_title);
		mEtContent = (EditText) this.findViewById(R.id.ask_update_et_content);
		mEtBestAnswerReview = (EditText) this
				.findViewById(R.id.ask_update_et_best_answer_review);
		mSpBestAnswer = (Spinner) this
				.findViewById(R.id.ask_update_spinner_best_answer);

		mTvTitle.setText(this.getString(R.string.ask_update_title));
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
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
			mAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, mAnswerContentList);
			mSpBestAnswer.setAdapter(mAdapter);
			((ArrayAdapter<String>) mAdapter)
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickAdd(View view) {// 提交

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
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
	}
}
