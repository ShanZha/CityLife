package com.fourkkm.citylife.control.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.MD5;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.image.ScaleImageProcessor;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.AsyncImageView;

/**
 * 用户信息修改页面
 * 
 * @author ShanZha
 * 
 */
public class UserUpdateActivity extends BaseUploadThumbActivity {

	private TextView mTvTitle, mTvUsername;
	private EditText mEtPswd, mEtPswdSure, mEtEmail;
	private ProgressDialogProxy mDialogProxy;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.user_update);
		mTvTitle = (TextView) this.findViewById(R.id.titlebar_back_tv_title);
		mIvThumb = (AsyncImageView) this
				.findViewById(R.id.user_update_iv_upload);
		mLlThumbLoading = (LinearLayout) this
				.findViewById(R.id.user_update_ll_thumb_loading);
		mTvUsername = (TextView) this
				.findViewById(R.id.user_update_tv_username);
		mEtPswd = (EditText) this.findViewById(R.id.user_update_et_pswd);
		mEtPswdSure = (EditText) this
				.findViewById(R.id.user_update_et_pswd_sure);
		mEtEmail = (EditText) this.findViewById(R.id.user_update_et_email);
	}

	@Override
	protected void prepare() {
		// TODO Auto-generated method stub
		super.prepare();
		mTvTitle.setText(this.getString(R.string.user_update_title));
		mDialogProxy = new ProgressDialogProxy(this);
		if (null != mCurrMember) {
			mTvUsername.setText(mCurrMember.getUsername());
			// mEtPswd.setText(mCurrMember.getPassword());
			// mEtPswdSure.setText(mCurrMember.getPassword());
			mEtEmail.setText(mCurrMember.getEmail());
			String filePath = mCurrMember.getFilePath();
			int thumbWidth = (int) this.getResources().getDimension(
					R.dimen.subject_add_thumbnail_width);
			int thumbHeight = (int) this.getResources().getDimension(
					R.dimen.subject_add_thumbnail_height);
			ScaleImageProcessor scale = new ScaleImageProcessor(thumbWidth,
					thumbHeight);
			mIvThumb.setImageProcessor(scale);
			if (TextUtils.isEmpty(filePath)) {
				Bitmap bm = BitmapFactory.decodeResource(getResources(),
						R.drawable.add_thumb);
				bm = ThumbnailUtils.extractThumbnail(bm, thumbWidth,
						thumbHeight);
				mIvThumb.setDefaultImageBitmap(bm);
			} else {
				mIvThumb.setUrl(GlobalConfig.URL_UPLOAD + filePath);
			}
		}
	}

	private void showWaiting() {
		if (null != mDialogProxy) {
			mDialogProxy.showDialog();
		}
	}

	private void hideWaiting() {
		if (null != mDialogProxy) {
			mDialogProxy.hideDialog();
		}
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickThumb(View view) {// 头像
		if (mIsUploadingThumb) {
			return;
		}
		mUploadType = UPLOAD_MEMBER_THUBM;
		if (null != mFloatingPicProxy) {
			mFloatingPicProxy.showLocation(view, Gravity.CENTER, 0, 0);
		}
	}

	public void onClickCommit(View view) {// 提交
		if (!this.isChanged()) {
			return;
		}
		if (mIsUploadingThumb) {
			this.showToast(this.getString(R.string.user_update_upload_avator));
			return;
		}
		String newPswd = mEtPswd.getText().toString().trim();
		String newPswdSure = mEtPswdSure.getText().toString().trim();
		if (!(TextUtils.isEmpty(newPswd) && TextUtils.isEmpty(newPswdSure))) {
			if (!newPswd.equals(newPswdSure)) {
				this.showToast(this
						.getString(R.string.register_pswd_not_correct));
				return;
			}
			mCurrMember.setPassword(MD5.encryptMd5(newPswd));
		}
		String email = mEtEmail.getText().toString().trim();
		if (!TextUtils.isEmpty(email)) {
			mCurrMember.setEmail(email);
		}
		this.showWaiting();
		mCurrMember.setFilePath(mThumbPath);
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_SAVE_MEMBER);
		this.getStoreOperation().saveOrUpdate(mCurrMember, param);

	}

	/**
	 * 用户信息是否改变
	 * 
	 * @return
	 */
	private boolean isChanged() {
		String pswd = mEtPswd.getText().toString().trim();
		String email = mEtEmail.getText().toString().trim();

		return (!TextUtils.isEmpty(mThumbPath))
				|| (!pswd.equals(mCurrMember.getPassword()))
				|| (!email.equals(mCurrMember.getEmail()));

	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_MEMBER == operator) {
			SqliteUtil.getInstance(this.getApplicationContext())
					.deleteByClassName(ModoerMembers.class.getName());
			((CoreApp) AppUtils.getBaseApp(this)).setCurrMember(mCurrMember);
			String newPswd = mEtPswd.getText().toString().trim();
			if (!TextUtils.isEmpty(newPswd)) {
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_PSWD, newPswd);
			}
			this.hideWaiting();
			this.showToast(this.getString(R.string.update_success));
			this.setResult(RESULT_OK);
			this.finish();
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_SAVE_MEMBER == operator) {
			this.hideWaiting();
			this.showToast(this.getString(R.string.update_fail));
		}
	}
}
