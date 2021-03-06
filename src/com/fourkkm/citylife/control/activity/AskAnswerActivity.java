package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskAnswer;
import com.andrnes.modoer.ModoerAsks;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerUsergroups;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.BaseActivity;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

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
	private ProgressDialogProxy mDialogProxy;

	private ModoerAsks mCurrAsk;
	private ModoerAskAnswer mCurrAnswer;

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
		mTvTitle.setText(this.getString(R.string.ask_answer));
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mDialogProxy = new ProgressDialogProxy(this);
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
		this.setUserNameAndGroup(mCurrAsk.getUid(), mTvAskMemberName);

	}

	private void setUserNameAndGroup(ModoerMembers member, TextView tv) {
		if (null != member && member.getId() != 0) {
			ModoerUsergroups group = member.getGroupid();
			String username = member.getUsername();
			if (null != group && group.getId() != 0) {
				String groupName = group.getGroupname();
				if (!TextUtils.isEmpty(groupName)) {
					tv.setText(groupName + "|" + username);
					return;
				}
			}
			if (TextUtils.isEmpty(username)) {
				return;
			}
			tv.setText(username);
		}
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	public void onClickCommit(View view) {// 提交
		mDialogProxy.showDialog();
		String content = mEtAnswer.getText().toString().trim();
		mCurrAnswer = new ModoerAskAnswer();
		mCurrAnswer.setAskid(mCurrAsk);
		mCurrAnswer.setCatid(mCurrAsk.getCatid());
		mCurrAnswer.setContent(content);
		mCurrAnswer.setIp(com.zj.app.utils.CommonUtil.getLocalIPAddress(this));
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		mCurrAnswer.setUid(member);
		mCurrAnswer.setUsername(member.getUsername());
		long dateline = System.currentTimeMillis() / 1000;
		mCurrAnswer.setDateline((int) dateline);
		mCurrAnswer.setStatus(1);

		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().saveOrUpdate(mCurrAnswer, param);
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
		this.showToast(this.getString(R.string.ask_answer_success));
		Intent data = new Intent();
		data.putExtra("ModoerAskAnswer", mCurrAnswer);
		this.setResult(RESULT_OK, data);
		this.finish();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		mDialogProxy.hideDialog();
	}
}
