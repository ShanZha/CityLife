package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskAnswer;
import com.andrnes.modoer.ModoerAsks;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerUsergroups;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

/**
 * 问答详情
 * 
 * @author ShanZha
 * 
 */
public class AskDetailActivity extends BaseActivity {

	private static final String TAG = "AskDetailActivity";
	private static final int REQ_LOGIN_CODE = 1;
	private static final int ANSWER_REQ_CODE = 2;
	private static final int ANSWER_MANAGER_REQ_CODE = 3;
	private TextView mTvTitle, mTvSubject, mTvContent, mTvTime,
			mTvLevelAndUsername;
	private TextView mTvBestContent, mTvBestTime, mTvBestLevelAndUsername,
			mTvBestTips;
	private Button mBtnAnswer;
	private TextView mTvBestComment, mTvBestCommentTips;
	private ImageView mIvDividerBestAnswer, mIvDividerBestAnswerComment;
	private LinearLayout mLlOtherAnswerContainer;
	private ProgressBar mProBarOtherAnswer;

	private ModoerAsks mCurrAsk;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.ask_detail);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvSubject = (TextView) this.findViewById(R.id.ask_detail_tv_subject);
		mTvContent = (TextView) this.findViewById(R.id.ask_detail_tv_content);
		mTvTime = (TextView) this.findViewById(R.id.ask_detail_tv_time);
		mTvLevelAndUsername = (TextView) this
				.findViewById(R.id.ask_detail_tv_level_and_username);
		mTvBestTips = (TextView) this
				.findViewById(R.id.ask_detail_tv_best_answer_tip);
		mTvBestContent = (TextView) this
				.findViewById(R.id.ask_detail_tv_best_answer);
		mTvBestTime = (TextView) this
				.findViewById(R.id.ask_detail_tv_best_time);
		mTvBestLevelAndUsername = (TextView) this
				.findViewById(R.id.ask_detail_tv_best_level_and_username);
		mTvBestComment = (TextView) this
				.findViewById(R.id.ask_detail_tv_best_comment);
		mTvBestCommentTips = (TextView) this
				.findViewById(R.id.ask_detail_tv_best_comment_tip);
		mLlOtherAnswerContainer = (LinearLayout) this
				.findViewById(R.id.ask_detail_ll_other_answer_container);
		mProBarOtherAnswer = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);

		mIvDividerBestAnswer = (ImageView) this
				.findViewById(R.id.ask_detail_iv_divider_best_answer);
		mIvDividerBestAnswerComment = (ImageView) this
				.findViewById(R.id.ask_detail_iv_divider_best_answer_comment);

		mBtnAnswer = (Button) this.findViewById(R.id.ask_detail_btn_answer);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mTvTitle.setText(this.getString(R.string.ask_detail));
		Intent intent = this.getIntent();
		mCurrAsk = (ModoerAsks) intent.getSerializableExtra("modoerAsk");
		this.prepareByAsk();

		this.showLoadingOtherAnswers();
		this.fetchOtherAnswers();
	}

	private void prepareByAsk() {
		if (null == mCurrAsk) {
			Log.e(TAG, "mCurrAsk is null");
			return;
		}
		mTvSubject.setText(mCurrAsk.getSubject());
		mTvContent.setText(mCurrAsk.getContent());

		mTvTime.setText(CommonUtil.getTimeByPHP(mCurrAsk.getDateline(),
				"yyyy-MM-dd HH:mm"));
		ModoerMembers member = mCurrAsk.getUid();
		this.setUserNameAndGroup(member, mTvLevelAndUsername);
		ModoerAskAnswer bestAnswer = mCurrAsk.getBestanswer();
		if (null != bestAnswer && bestAnswer.getId() != 0) {
			mTvBestTips.setVisibility(View.VISIBLE);
			mTvBestTime.setVisibility(View.VISIBLE);
			mTvBestContent.setVisibility(View.VISIBLE);
			mTvBestLevelAndUsername.setVisibility(View.VISIBLE);
			mTvBestComment.setVisibility(View.VISIBLE);
			mTvBestCommentTips.setVisibility(View.VISIBLE);
			mIvDividerBestAnswer.setVisibility(View.VISIBLE);
			mIvDividerBestAnswerComment.setVisibility(View.VISIBLE);
			mTvBestContent.setText(bestAnswer.getContent());
			mTvBestTime.setText(CommonUtil.getTimeByPHP(
					bestAnswer.getDateline(), "yyyy-MM-dd HH:mm"));
			this.setUserNameAndGroup(bestAnswer.getUid(),
					mTvBestLevelAndUsername);
			mTvBestComment.setText(bestAnswer.getFeel());
		} else {
			mTvBestTips.setVisibility(View.GONE);
			mTvBestTime.setVisibility(View.GONE);
			mTvBestContent.setVisibility(View.GONE);
			mTvBestLevelAndUsername.setVisibility(View.GONE);
			mTvBestComment.setVisibility(View.GONE);
			mTvBestCommentTips.setVisibility(View.GONE);
			mIvDividerBestAnswer.setVisibility(View.GONE);
			mIvDividerBestAnswerComment.setVisibility(View.GONE);
		}
		if (mCurrAsk.getSuccess() == 1) {// 如果已经解决，则不提供回答/管理
			mBtnAnswer.setVisibility(View.GONE);
		} else {
			if (null != mCurrAsk) {
				int reward = mCurrAsk.getReward();
				mBtnAnswer.setText(this.getString(R.string.ask_me_answer) + "("
						+ reward + "积分)");
			}
		}
	}

	private void showLoadingOtherAnswers() {
		mLlOtherAnswerContainer.setVisibility(View.GONE);
		mProBarOtherAnswer.setVisibility(View.VISIBLE);
	}

	private void hideLoadingOtherAnswers() {
		mLlOtherAnswerContainer.setVisibility(View.VISIBLE);
		mProBarOtherAnswer.setVisibility(View.GONE);
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

	private void addOtherAnswer(ModoerAskAnswer answer) {
		if (null == mLlOtherAnswerContainer) {
			return;
		}
		if (View.GONE == mLlOtherAnswerContainer.getVisibility()) {
			mLlOtherAnswerContainer.setVisibility(View.VISIBLE);
		}
		View view = this.getLayoutInflater().inflate(R.layout.ask_detail_other,
				null);
		TextView tvContent = (TextView) view
				.findViewById(R.id.ask_detail_other_tv_content);
		TextView tvTime = (TextView) view
				.findViewById(R.id.ask_detail_other_tv_time);
		TextView tvLevelAndUsername = (TextView) view
				.findViewById(R.id.ask_detail_other_tv_level_and_username);
		tvContent.setText(answer.getContent());
		tvTime.setText(CommonUtil.getTimeByPHP(answer.getDateline(),
				"yyyy-MM-dd HH:mm"));
		this.setUserNameAndGroup(answer.getUid(), tvLevelAndUsername);
		mLlOtherAnswerContainer.addView(view);
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

	/**
	 * 根据问题所属人回答界面跳转不一样
	 */
	private void onAnswer() {
		ModoerMembers member = ((CoreApp) AppUtils.getBaseApp(this))
				.getCurrMember();
		if (null == mCurrAsk || null == member) {
			return;
		}
		ModoerMembers askMember = mCurrAsk.getUid();
		if (askMember.getId() == member.getId()) {
			Intent intent = new Intent(this, AskUpdateActivity.class);
			intent.putExtra("modoerAsk", mCurrAsk);
			this.startActivityForResult(intent, ANSWER_MANAGER_REQ_CODE);
		} else {
			Intent intent = new Intent(this, AskAnswerActivity.class);
			intent.putExtra("modoerAsk", mCurrAsk);
			this.startActivityForResult(intent, ANSWER_REQ_CODE);
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickAnswer(View view) {// 我要回答/管理问题
		if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
			this.startActivityForResult(new Intent(this, LoginActivity.class),
					REQ_LOGIN_CODE);
		} else {
			this.onAnswer();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
		case ANSWER_REQ_CODE:// 回答问题成功
			if (null == data) {
				return;
			}
			ModoerAskAnswer answer = (ModoerAskAnswer) data
					.getSerializableExtra("ModoerAskAnswer");
			if (null != answer) {
				this.addOtherAnswer(answer);
			}
			break;
		case REQ_LOGIN_CODE:// 登录成功
			this.onAnswer();
			break;
		case ANSWER_MANAGER_REQ_CODE:// 修改问题/结束问题成功
			if (null == data) {
				return;
			}
			mCurrAsk = (ModoerAsks) data.getSerializableExtra("modoerAsk");
			this.prepareByAsk();
			break;
		}

	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null == results || results.size() == 0) {
			mLlOtherAnswerContainer.setVisibility(View.GONE);
			mProBarOtherAnswer.setVisibility(View.GONE);
			return;
		}
		for (int i = 0; i < results.size(); i++) {
			ModoerAskAnswer answer = (ModoerAskAnswer) results.get(i);
			this.addOtherAnswer(answer);
		}
		this.hideLoadingOtherAnswers();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.hideLoadingOtherAnswers();
	}
}
