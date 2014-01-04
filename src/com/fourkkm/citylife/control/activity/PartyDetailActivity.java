package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.TextSize;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerParty;
import com.andrnes.modoer.ModoerPartyApply;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;
import com.zj.support.image.file.AsyncImageLoader;
import com.zj.support.observer.model.Param;

/**
 * 聚会详情
 * 
 * @author ShanZha
 * 
 */
public class PartyDetailActivity extends BaseActivity {

	private static final String TAG = "PartyDetailActivity";
	public static final TextSize[] FONT_SIZES = new TextSize[] {
			TextSize.SMALLER, TextSize.NORMAL, TextSize.LARGER };
	private static final int REQ_SIGN_UP_CODE = 1;
	/** 常量定义见ModoerParty实体 **/
	private static final int SEX_LIMIT_NONE = 0;
	private static final int SEX_LIMIT_WOMAN = 1;
	private static final int SEX_LIMIT_MAN = 2;
	private Button mBtnSignUp;
	private ImageView mIvThumb;
	private WebView mWvDesc;
	private TextView mTvTitle, mTvSubject, mTvInitiator, mTvInitiateTime,
			mTvSignUpEndTime, mTvTime, mTvAddr, mTvSexLimit, mTvCost,
			mTvPlanNum, mTvSignUpNum, mTvSignUpMemberNames;
	private LinearLayout mLlSignUpMembers;
	private ProgressBar mProBarSignUpMembers;

	private ModoerParty mParty;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.party_detail);
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mBtnSignUp = (Button) this
				.findViewById(R.id.titlebar_back_right_btn_operator);
		mIvThumb = (ImageView) this.findViewById(R.id.thumb_detail_iv_thumb);
		mTvSubject = (TextView) this.findViewById(R.id.party_detail_tv_subject);
		mTvInitiator = (TextView) this
				.findViewById(R.id.party_detail_tv_initiator);
		mTvInitiateTime = (TextView) this
				.findViewById(R.id.party_detail_tv_initiate_time);
		mTvSignUpEndTime = (TextView) this
				.findViewById(R.id.party_detail_tv_sign_up_end_time);
		mTvTime = (TextView) this.findViewById(R.id.party_detail_tv_time);
		mTvAddr = (TextView) this.findViewById(R.id.party_detail_tv_addr);
		mTvSexLimit = (TextView) this
				.findViewById(R.id.party_detail_tv_sex_limit);
		mTvCost = (TextView) this.findViewById(R.id.party_detail_tv_cost);
		mTvPlanNum = (TextView) this
				.findViewById(R.id.party_detail_tv_plan_person_num);
		mTvSignUpNum = (TextView) this
				.findViewById(R.id.party_detail_tv_sign_up_num);
		mWvDesc = (WebView) this.findViewById(R.id.party_detail_wv_desc);
		mTvSignUpMemberNames = (TextView) this
				.findViewById(R.id.party_detail_tv_sign_up_members_name);

		mLlSignUpMembers = (LinearLayout) this
				.findViewById(R.id.party_detail_ll_sign_up);
		mProBarSignUpMembers = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);

		this.handleWebView();
		mTvTitle.setText(this.getString(R.string.party_detail));
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		Intent intent = this.getIntent();
		mParty = (ModoerParty) intent.getSerializableExtra("party");
		if (null == mParty) {
			Log.e(TAG, "shan-->party is null");
			return;
		}
		String url = GlobalConfig.URL_PIC + mParty.getThumb();
		AsyncImageLoader.getImageLoad(this).showPic(url, mIvThumb);
		mTvSubject.setText(mParty.getSubject());
		mTvInitiator.setText(mParty.getUsername());
		mTvInitiateTime.setText(CommonUtil.getTimeByPHP(mParty.getDateline(),
				CommonUtil.FORMAT_MINUTE));
		mTvSignUpEndTime.setText(CommonUtil.getTimeByPHP(
				mParty.getJoinendtime(), CommonUtil.FORMAT_MINUTE));
		String startTime = CommonUtil.getTimeByPHP(mParty.getBegintime(),
				CommonUtil.FORMAT_MINUTE);
		String endTime = CommonUtil.getTimeByPHP(mParty.getEndtime(),
				CommonUtil.FORMAT_MINUTE);
		mTvTime.setText(startTime + "~" + endTime);
		mTvAddr.setText(mParty.getAddress());
		mTvSexLimit.setText(this.getStrBySex(mParty.getSex()));
		mTvCost.setText(mParty.getPrice() + "/人");
		mTvPlanNum.setText(mParty.getPlannum() + "");
		mTvSignUpNum.setText(mParty.getApplynum() + "");
		// mTvDesc.setText(mParty.getDes());
		String desc = mParty.getDes();
		if (TextUtils.isEmpty(desc)) {
			mWvDesc.setVisibility(View.GONE);
		} else {
			System.out.println(" " + CommonUtil.formatHtml(desc));
			mWvDesc.loadDataWithBaseURL(null, CommonUtil.formatHtml(desc),
					"text/html", "utf-8", null);
		}
		int joinedTime = mParty.getJoinendtime();
		int currTime = (int) CommonUtil.getCurrTimeByPHP();
		if (currTime > joinedTime) {// 报名时间已过
			mBtnSignUp.setVisibility(View.GONE);
		}

		this.showLoadingSignUpMember();
		this.fetchSignUpMember();
	}

	private void handleWebView() {
		mWvDesc.setBackgroundColor(0);
		mWvDesc.getBackground().setAlpha(0);
		WebSettings wb = mWvDesc.getSettings();
		wb.setTextSize(FONT_SIZES[1]);
	}

	private String getStrBySex(int sex) {
		switch (sex) {
		case SEX_LIMIT_NONE:
			return this.getString(R.string.party_sex_limit_no);
		case SEX_LIMIT_WOMAN:
			return this.getString(R.string.party_sex_limit_woman);
		case SEX_LIMIT_MAN:
			return this.getString(R.string.party_sex_limit_man);
		default:
			return this.getString(R.string.party_sex_limit_no);
		}
	}

	private void showLoadingSignUpMember() {
		mProBarSignUpMembers.setVisibility(View.VISIBLE);
		mLlSignUpMembers.setVisibility(View.GONE);
	}

	private void hideLoadingSignUpMember() {
		mProBarSignUpMembers.setVisibility(View.GONE);
		mLlSignUpMembers.setVisibility(View.VISIBLE);
	}

	private void fetchSignUpMember() {
		String selectCode = "from com.andrnes.modoer.ModoerPartyApply mpa where mpa.status = 1 and mpa.partyid.id = "
				+ mParty.getId();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerPartyApply(),
				param);
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickRight(View view) {// 我要报名
		Intent intent = new Intent(this, PartyApplyActivity.class);
		intent.putExtra("party", mParty);
		this.startActivityForResult(intent, REQ_SIGN_UP_CODE);
	}

	public void onClickWonderfulReview(View view) {// 精彩回顾
		this.showToast("暂无链接");
	}

	public void onClickEventPhoto(View view) {// 活动图片
		this.showToast("暂无链接");
	}

	public void onClickThumbnail(View view) {// 点击缩略图
		// Do nothing
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (REQ_SIGN_UP_CODE == requestCode) {
			if (RESULT_OK == resultCode) {
				int applyNum = mParty.getApplynum();
				mTvSignUpNum.setText("" + (applyNum + 1));
				if (View.GONE == mLlSignUpMembers.getVisibility()) {
					mLlSignUpMembers.setVisibility(View.VISIBLE);
				}
				if (null == data) {
					return;
				}
				String temp = mTvSignUpMemberNames.getText().toString();
				String signUpName = data.getStringExtra("signUpName");
				mTvSignUpMemberNames.setText(temp + signUpName + ";");

			}
		}
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null != results && results.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < results.size(); i++) {
				ModoerPartyApply apply = (ModoerPartyApply) results.get(i);
				sb.append(apply.getLinkman());
				sb.append(";");
			}
			mTvSignUpMemberNames.setText(sb.toString());
			this.hideLoadingSignUpMember();
		} else {
			mProBarSignUpMembers.setVisibility(View.GONE);
			mLlSignUpMembers.setVisibility(View.GONE);
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		mProBarSignUpMembers.setVisibility(View.GONE);
		mLlSignUpMembers.setVisibility(View.GONE);
	}
}
