package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
	/** 常量定义见ModoerParty实体 **/
	private static final int SEX_LIMIT_NONE = 0;
	private static final int SEX_LIMIT_WOMAN = 1;
	private static final int SEX_LIMIT_MAN = 2;
	private ImageView mIvThumb;
	private TextView mTvSubject, mTvInitiator, mTvInitiateTime,
			mTvSignUpEndTime, mTvTime, mTvAddr, mTvSexLimit, mTvCost,
			mTvPlanNum, mTvSignUpNum, mTvDesc, mTvSignUpMemberNames;

	private ProgressBar mProBarSignUpMembers;

	private ModoerParty mParty;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.party_detail);
		mIvThumb = (ImageView) this.findViewById(R.id.party_detail_iv_thumb);
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
		mTvDesc = (TextView) this.findViewById(R.id.party_detail_tv_desc);
		mTvSignUpMemberNames = (TextView) this
				.findViewById(R.id.party_detail_tv_sign_up_members_name);

		mProBarSignUpMembers = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
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
		mTvPlanNum.setText(mParty.getPlannum()+"");
		mTvSignUpNum.setText(mParty.getApplynum()+"");
		mTvDesc.setText(mParty.getDes());

		this.showLoadingSignUpMember();
		this.fetchSignUpMember();
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
		mTvSignUpMemberNames.setVisibility(View.GONE);
	}

	private void hideLoadingSignUpMember() {
		mProBarSignUpMembers.setVisibility(View.GONE);
		mTvSignUpMemberNames.setVisibility(View.VISIBLE);
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

	public void onClickSignUp(View view) {// 我要报名

	}

	public void onClickWeiInfo(View view) {// 查看网页信息

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
				ModoerMembers member = apply.getUid();
				if (null != member) {
					String username = member.getUsername();
					if (TextUtils.isEmpty(username)) {
						continue;
					}
					sb.append(username);
					sb.append(";");
				}
			}
			mTvSignUpMemberNames.setText(sb.toString());
		}
		this.hideLoadingSignUpMember();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.hideLoadingSignUpMember();
	}
}
