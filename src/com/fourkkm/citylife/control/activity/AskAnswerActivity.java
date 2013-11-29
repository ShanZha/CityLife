package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAsks;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;

/**
 * 回答问题界面
 * 
 * @author ShanZha
 * 
 */
public class AskAnswerActivity extends BaseActivity {

	private static final String TAG = "AskAnswerActivity";
	private TextView mTvTitle, mTvAskTitle, mTvAskDesc, mTvAskTime,
			mTvAskMemberName;
	private EditText mEtAnswer;

	private ModoerAsks mCurrAsk;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.ask_answer);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvAskTitle = (TextView) this.findViewById(R.id.ask_answer_tv_title);
		mTvAskDesc = (TextView) this.findViewById(R.id.ask_answer_tv_content);
		mTvAskTime = (TextView) this.findViewById(R.id.ask_answer_tv_time);
		mTvAskMemberName = (TextView) this
				.findViewById(R.id.ask_answer_tv_username);
		mEtAnswer = (EditText) this
				.findViewById(R.id.ask_answer_et_answer_content);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		Intent intent = this.getIntent();
		mCurrAsk = (ModoerAsks) intent.getSerializableExtra("modoerAsk");
		if (null == mCurrAsk) {
			Log.e(TAG, "mCurrAsk is null");
			return;
		}
		mTvAskTitle.setText(mCurrAsk.getSubject());
		mTvAskDesc.setText(mCurrAsk.getContent());
		mTvAskTime.setText(CommonUtil.getTimeByPHP(mCurrAsk.getDateline(),
				"yyyy-MM-dd HH:mm"));
		ModoerMembers member = mCurrAsk.getUid();
		if (null != member && member.getId() != 0) {
			String name = member.getUsername();
			if (!TextUtils.isEmpty(name)) {
				mTvAskMemberName.setText(name);
			}
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickCommit(View view) {// 提交
		String content = mEtAnswer.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			this.showToast(this.getString(R.string.not_null));
			return;
		}
	}
}
