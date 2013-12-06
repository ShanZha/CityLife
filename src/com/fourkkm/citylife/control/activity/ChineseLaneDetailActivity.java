package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerFenlei;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.BaseActivity;
import com.zj.support.image.file.AsyncImageLoader;

/**
 * Ã∆»ÀœÔœÍ«È
 * 
 * @author ShanZha
 * 
 */
public class ChineseLaneDetailActivity extends BaseActivity {

	private static final String TAG = "ChineseLaneDetailActivity";
	private TextView mTvTitle, mTvSubject, mTvUsername, mTvInitiateTime,
			mTvExpireTime, mTvContact, mTvContactTel, mTvIM, mTvEmail, mTvAddr,
			mTvContent;
	private ImageView mIvThumb;
	private LinearLayout mLlEmail, mLlIM, mLlAddr;

	private ModoerFenlei mChinaLane;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.china_lane_detail);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mTvSubject = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_subject);
		mTvUsername = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_initiator);
		mTvInitiateTime = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_initiate_time);
		mTvExpireTime = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_expire_time);
		mTvContact = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_contact);
		mTvContactTel = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_contact_tel);
		mTvIM = (TextView) this.findViewById(R.id.china_lane_detail_tv_im);
		mTvEmail = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_email);
		mTvAddr = (TextView) this.findViewById(R.id.china_lane_detail_tv_addr);
		mTvContent = (TextView) this
				.findViewById(R.id.china_lane_detail_tv_desc);

		mIvThumb = (ImageView) this.findViewById(R.id.thumb_detail_iv_thumb);
		mLlEmail = (LinearLayout) this
				.findViewById(R.id.china_lane_detail_ll_email);
		mLlIM = (LinearLayout) this.findViewById(R.id.china_lane_detail_ll_im);
		mLlAddr = (LinearLayout) this
				.findViewById(R.id.china_lane_detail_ll_addr);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		mTvTitle.setText(this.getString(R.string.china_lane_detail));
		Intent intent = this.getIntent();
		mChinaLane = (ModoerFenlei) intent.getSerializableExtra("ModoerFeilei");
		if (null == mChinaLane) {
			Log.e(TAG, "shan-->mChineseLane is null");
			return;
		}
		mTvSubject.setText(mChinaLane.getSubject());
		mTvUsername.setText(mChinaLane.getUsername());
		mTvInitiateTime.setText(CommonUtil.getTimeByPHP(
				mChinaLane.getDateline(), "yyyy-MM-dd HH:mm"));
		mTvExpireTime.setText(CommonUtil.getTimeByPHP(mChinaLane.getEndtime(),
				"yyyy-MM-dd HH:mm"));
		mTvContact.setText(mChinaLane.getLinkman());
		mTvContactTel.setText(mChinaLane.getContact());
		String email = mChinaLane.getEmail();
		if (TextUtils.isEmpty(email)) {
			mLlEmail.setVisibility(View.GONE);
		} else {
			mTvEmail.setText(email);
		}
		String im = mChinaLane.getIm();
		if (TextUtils.isEmpty(im)) {
			mLlIM.setVisibility(View.GONE);
		} else {
			mTvIM.setText(im);
		}
		String addr = mChinaLane.getAddress();
		if (TextUtils.isEmpty(addr)) {
			mLlAddr.setVisibility(View.GONE);
		} else {
			mTvAddr.setText(addr);
		}
		mTvContent.setText(mChinaLane.getContent());
		String url = GlobalConfig.URL_PIC + mChinaLane.getThumb();
		AsyncImageLoader.getImageLoad(this).showPic(url, mIvThumb);
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickThumbnail(View view) {// Àı¬‘Õº
		// Do nothing
	}

}
