package com.fourkkm.citylife.control.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerAskAnswer;
import com.andrnes.modoer.ModoerAsks;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.BaseActivity;
import com.zj.app.utils.DateFormatMethod;
import com.zj.support.observer.model.Param;

/**
 * 问答详情
 * 
 * @author ShanZha
 * 
 */
public class AskDetailActivity extends BaseActivity {

	private TextView mTvSubject, mTvContent, mTvTime, mTvLevelAndUsername;
	private TextView mTvBestContent, mTvBestTime, mTvBestLevelAndUsername,
			mTvBestTips;
	private TextView mTvBestComment, mTvBestCommentTips;
	private LinearLayout mLlOtherAnswerContainer;
	private ProgressBar mProBarOtherAnswer;

	private ModoerAsks mCurrAsk;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.ask_detail);
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
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		Intent intent = this.getIntent();
		mCurrAsk = (ModoerAsks) intent.getSerializableExtra("modoerAsk");
		if (null == mCurrAsk) {
			return;
		}
		mTvSubject.setText(mCurrAsk.getSubject());
		mTvContent.setText(mCurrAsk.getContent());
		mTvTime.setText(DateFormatMethod.formatDate(
				new Date(mCurrAsk.getDateline() * 1000L), "yyyy-MM-dd HH:mm"));
		ModoerMembers member = mCurrAsk.getUid();
		if (null != member && member.getId() != 0) {// 暂时仅设置Username
			mTvLevelAndUsername.setText(member.getUsername());
		}
		ModoerAskAnswer bestAnswer = mCurrAsk.getBestanswer();
		if (null != bestAnswer && bestAnswer.getId() != 0) {
			mTvBestContent.setText(bestAnswer.getContent());
			mTvBestTime.setText(DateFormatMethod.formatDate(
					new Date(bestAnswer.getDateline() * 1000),
					"yyyy-MM-dd HH:mm"));
			mTvBestLevelAndUsername.setText(bestAnswer.getUsername());// 暂时仅设置Username
			mTvBestComment.setText(bestAnswer.getFeel());
		} else {
			mTvBestTips.setVisibility(View.GONE);
			mTvBestTime.setVisibility(View.GONE);
			mTvBestContent.setVisibility(View.GONE);
			mTvBestLevelAndUsername.setVisibility(View.GONE);
			mTvBestComment.setVisibility(View.GONE);
			mTvBestCommentTips.setVisibility(View.GONE);
		}

		this.showLoadingOtherAnswers();
		this.fetchOtherAnswers();
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
			View view = this.getLayoutInflater().inflate(
					R.layout.ask_detail_other, mLlOtherAnswerContainer);
			TextView tvContent = (TextView) view
					.findViewById(R.id.ask_detail_other_tv_content);
			TextView tvTime = (TextView) view
					.findViewById(R.id.ask_detail_other_tv_time);
			TextView tvLevelAndUsername = (TextView) view
					.findViewById(R.id.ask_detail_other_tv_level_and_username);
			tvContent.setText(answer.getContent());
			tvTime.setText(DateFormatMethod.formatDate(
					new Date(answer.getDateline() * 1000L), "yyyy-MM-dd HH:mm"));
			ModoerMembers member = answer.getUid();
			if (null != member && member.getId() != 0) {// 暂时仅设置用户名
				tvLevelAndUsername.setText(member.getUsername());
			}
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
