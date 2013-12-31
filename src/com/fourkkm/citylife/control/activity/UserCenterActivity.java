package com.fourkkm.citylife.control.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.preference.DialogPreference;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerUsergroups;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.zj.app.BaseActivity;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;

/**
 * 会员中心界面
 * 
 * @author ShanZha
 * 
 */
public class UserCenterActivity extends BaseActivity {

	private static final int REQ_LOGIN_CODE = 1;
	private ImageView mIvAvator;
	private TextView mTvTitle, mTvUsernmae, mTvLevel, mTvIntegration, mTvKm;
	private LinearLayout mLlLoadingInfo;
	private RelativeLayout mRlInfo;

	private ModoerMembers mMember;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();

		this.setContentView(R.layout.user_center);
		mIvAvator = (ImageView) this.findViewById(R.id.user_center_iv_avator);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvUsernmae = (TextView) this
				.findViewById(R.id.user_center_tv_username);
		mTvLevel = (TextView) this.findViewById(R.id.user_center_tv_user_level);
		mTvIntegration = (TextView) this
				.findViewById(R.id.user_center_tv_integration);
		mTvKm = (TextView) this.findViewById(R.id.user_center_tv_km);
		mLlLoadingInfo = (LinearLayout) this
				.findViewById(R.id.user_center_ll_loading_info);
		mRlInfo = (RelativeLayout) this.findViewById(R.id.user_center_rl_info);

		mTvTitle.setText(this.getString(R.string.member_center));
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
		mMember = ((CoreApp) AppUtils.getBaseApp(this)).getCurrMember();
		this.showLoadingInfo();
		this.fecthMemberInfo(mMember.getId());
	}

	private void showLoadingInfo() {
		mLlLoadingInfo.setVisibility(View.VISIBLE);
		mRlInfo.setVisibility(View.GONE);
	}

	private void hideLoadingInfo() {
		mLlLoadingInfo.setVisibility(View.GONE);
		mRlInfo.setVisibility(View.VISIBLE);
	}

	private void fecthMemberInfo(int memberId) {
		String selectCode = "from com.andrnes.modoer.ModoerMembers mm where mm.id = "
				+ memberId;
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		this.getStoreOperation().find(selectCode, paramsMap, true,
				new ModoerMembers(), param);
	}

	private void prepareMember() {
		if (null == mMember) {
			return;
		}
		mTvUsernmae.setText(mMember.getUsername());
		ModoerUsergroups group = mMember.getGroupid();
		if (null != group) {
			String level = group.getGroupname();
			mTvLevel.setText(level);
		}
		mTvIntegration.setText(mMember.getPoint() + "");
		mTvKm.setText(mMember.getPoint1() + "");

	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickMyParty(View view) {// 我的聚会活动
		Intent intent = new Intent(this, PartyListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.PARTY_ME);
		this.startActivity(intent);
	}

	public void onClickMyLane(View view) {// 我的唐人巷
		Intent intent = new Intent(this, ChineseLaneListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.CHINA_LANE_ME);
		this.startActivity(intent);
	}

	public void onClickMyReview(View view) {// 我的点评
		Intent intent = new Intent(this, ReviewListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.REVIEW_ME);
		this.startActivity(intent);
	}

	public void onClickMyShop(View view) {// 我的店铺
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.SUBJECT_ME);
		this.startActivity(intent);
	}

	public void onClickMyCollection(View view) {// 我的收藏
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.SUBJECT_FAVORITE);
		this.startActivity(intent);
	}

	public void onClickMyQuestion(View view) {// 我的问题
		Intent intent = new Intent(this, AskListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.ASK_ME);
		this.startActivity(intent);
	}

	public void onClickMyAnswer(View view) {// 我的回答
		Intent intent = new Intent(this, AskListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.ASK_ANSWER_ME);
		this.startActivity(intent);
	}

	public void onClickMySms(View view) {// 我的短信
		this.startActivity(new Intent(this, SmsListActivity.class));
	}

	public void onClickUpdateInfo(View view) {// 修改个人信息
		this.startActivity(new Intent(this, UserUpdateActivity.class));
	}

	public void onClickLoginExit(View view) {// 注销登录
		((CoreApp) AppUtils.getBaseApp(this)).setCurrMember(null);
		SharedPreferenceUtil.getSharedPrefercence().clear(
				this.getApplicationContext(), GlobalConfig.SharePre.KEY_PSWD);
		SharedPreferenceUtil.getSharedPrefercence().clear(
				this.getApplicationContext(),
				GlobalConfig.SharePre.KEY_IS_REMBER_PSWD);
		SharedPreferenceUtil.getSharedPrefercence().clear(
				this.getApplicationContext(),
				GlobalConfig.SharePre.KEY_IS_AUTO_LOGIN);
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (REQ_LOGIN_CODE == requestCode) {
			if (RESULT_OK == resultCode) {
				this.hideLoadingInfo();
				mMember = ((CoreApp) AppUtils.getBaseApp(this)).getCurrMember();
				this.prepareMember();
			} else {
				this.finish();
			}
		}
	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFind(out);
		mMember = (ModoerMembers) out.getResult();
		this.prepareMember();
		this.hideLoadingInfo();
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		this.prepareMember();
		this.hideLoadingInfo();
	}

}
