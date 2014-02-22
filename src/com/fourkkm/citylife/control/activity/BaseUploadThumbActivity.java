package com.fourkkm.citylife.control.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.widget.FloatingTranslucentProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.zj.app.BaseActivity;
import com.zj.app.constant.Config;
import com.zj.app.utils.AppUtils;
import com.zj.app.utils.DateFormatMethod;
import com.zj.app.utils.FileHelper;
import com.zj.app.utils.SdcardUtil;
import com.zj.support.image.ScaleImageProcessor;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.AsyncImageView;

/**
 * 上传店铺、头像、聚会、唐人巷缩略图基类
 * 
 * @author ShanZha
 * 
 */
public class BaseUploadThumbActivity extends BaseActivity implements
		IFloatingItemClick {

	private static final String TAG = "BaseUploadThumbActivity";
	protected static final int REQ_LOGIN_CODE = 1;
	protected static final int REQ_CODE_ALBUM_IMG = 2;
	protected static final int REQ_CODE_TAKE_PHOTO = 3;
	protected static final int INDEX_TAKE_PHOTO = 0;
	protected static final int INDEX_ALBUM_SELECT = 1;

	/** 上传缩略图 **/
	protected static final int UPLOAD_THUMB = 1;
	/** 上传图片并生成缩略图 **/
	protected static final int UPLOAD_PIC_WITH_THUMB = 2;
	/** 上传用户头像 **/
	protected static final int UPLOAD_MEMBER_THUBM = 3;

	protected FloatingTranslucentProxy mFloatingPicProxy;
	protected BaseAdapter mAdapterPics;
	protected List<String> mPicSelectList;
	protected AsyncImageView mIvThumb;
	protected LinearLayout mLlThumbLoading;
	protected String mThumbPath;
	/** 上传图片类型 **/
	protected int mUploadType = UPLOAD_THUMB;
	/** 是否正在上传缩略图 **/
	protected boolean mIsUploadingThumb = false;
	/** 是否需要自动登录，默认需要 **/
	protected boolean mIsAutoLogin = true;
	protected boolean mIsBT = false;
	protected ModoerMembers mCurrMember;

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		if (mIsAutoLogin) {
			// 如果没有登录，先登录
			if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
				this.startActivityForResult(new Intent(this,
						LoginActivity.class), REQ_LOGIN_CODE);
				return;
			}
			this.prepare();
		}

	}

	protected void prepare() {
		mCurrMember = ((CoreApp) AppUtils.getBaseApp(this)).getCurrMember();
		this.setPicSelect();
	}

	private void setPicSelect() {
		mPicSelectList = new ArrayList<String>();
		String[] arrays = this.getResources()
				.getStringArray(R.array.pic_select);
		mPicSelectList.addAll(Arrays.asList(arrays));
		mFloatingPicProxy = new FloatingTranslucentProxy(this,
				GlobalConfig.FloatingType.TYPE_PIC_SELECT);
		mFloatingPicProxy.setFloatingItemClickListener(this);
		mFloatingPicProxy.setDatas(mPicSelectList);
	}

	protected void showLoading(View view) {
		if (null != view) {
			view.setVisibility(View.VISIBLE);
		}
	}

	protected void hideLoading(View view) {
		if (null != view) {
			view.setVisibility(View.GONE);
		}
	}

	/**
	 * 上传会员头像缩略图
	 * 
	 * @param filePath
	 */
	private void uploadMemberThumb(String filePath) {
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_UPLOAD_MEMBER_THUMB);
		this.getStoreOperation().uploadMemberPic(mCurrMember.getId(),
				new File(filePath), param);
	}

	/**
	 * 上传缩略图--店铺封面
	 * 
	 * @param filePath
	 */
	protected void uploadThumb(String filePath) {
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_UPLOAD_THUMB);
		this.getStoreOperation().uploadThumb(new File(filePath), param);
	}

	/**
	 * 上传大图，服务器生成缩略图--店铺介绍图片
	 * 
	 * @param filePath
	 */
	protected void uploadPicWithThumb(String filePath) {
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_UPLOAD_PIC_WITH_THUMB);
		this.getStoreOperation().uploadPicWithThumb(new File(filePath), param,mIsBT);
	}

	/**
	 * 上传入口
	 * 
	 * @param filePath
	 */
	protected void doUpload(String filePath) {
		switch (mUploadType) {
		case UPLOAD_THUMB:
			Log.i(TAG, "shan-->doUploadThumb() filepath = " + filePath);
			mIsUploadingThumb = true;
			this.showLoading(mLlThumbLoading);
			this.uploadThumb(filePath);
			break;
		case UPLOAD_MEMBER_THUBM:
			Log.i(TAG, "shan-->doUploadMemberThumb() filepath = " + filePath);
			mIsUploadingThumb = true;
			this.showLoading(mLlThumbLoading);
			this.uploadMemberThumb(filePath);
			break;
		case UPLOAD_PIC_WITH_THUMB:// (交给子类实现)
			Log.i(TAG, "shan-->doUploadPic() filepath = " + filePath);
			break;
		}
	}

	protected void notifyDataChanged(BaseAdapter adapter) {
		if (null != adapter) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 本地选择相册图片返回处理
	 * 
	 * @param data
	 * @return filePath
	 */
	private void onActivityResultAlbumPic(Intent data) {
		Uri uri = data.getData();
		String filePath = FileHelper.getAbsoluteImagePath(this, uri);// 得到文件的绝对路径
		String[] fileNameStrs = filePath.split(GlobalConfig.SEPERATOR);
		int size = fileNameStrs.length;
		String mFileName = fileNameStrs[size - 1].toString();
		this.showToast(this.getString(R.string.upload_ing));
		this.doUpload(filePath);
	}

	private void onActivityResultTakePhoto(Intent data) {
		if (!SdcardUtil.hasSDCard()) {
			this.showToast(this.getString(R.string.storageNot));
			return;
		}
		// 将其保存至sd卡
		Bundle bundle = data.getExtras();
		if (null == bundle) {
			BaseUploadThumbActivity.this
					.showToast(getString(R.string.upload_fail));
			return;
		}
		final Bitmap bitmap = (Bitmap) bundle.get("data");
		// 获取系统图片存储路径
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		path.mkdirs();
		// 拼接文件保存路径
		final String mFileName = DateFormatMethod.getCurrentTime() + ".png";
		String filePath = path.getPath() + GlobalConfig.SEPERATOR + mFileName;
		this.showToast(this.getString(R.string.upload_ing));
		SavePicTask task = new SavePicTask(bitmap);
		task.execute(filePath);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			if (REQ_LOGIN_CODE == requestCode) {
				this.finish();
			}
			return;
		}
		switch (requestCode) {
		case REQ_LOGIN_CODE:// 登录
			this.prepare();
			break;
		case REQ_CODE_ALBUM_IMG:// 图库
			if (null != data) {
				this.onActivityResultAlbumPic(data);
			}
			break;
		case REQ_CODE_TAKE_PHOTO:// 拍照
			if (null != data) {
				this.onActivityResultTakePhoto(data);
			}
			break;
		}

	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		if (GlobalConfig.FloatingType.TYPE_PIC_SELECT == type) {
			switch (pos) {
			case INDEX_TAKE_PHOTO:
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				this.startActivityForResult(intent, REQ_CODE_TAKE_PHOTO);
				break;
			case INDEX_ALBUM_SELECT:
				Intent intent2 = new Intent();
				intent2.setType("image/*");
				intent2.setAction(Intent.ACTION_GET_CONTENT);
				this.startActivityForResult(intent2, REQ_CODE_ALBUM_IMG);
				break;
			}
		}
	}

	@Override
	public void onSuccessUploadFile(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessUploadFile(out);
		int operator = out.getOperator();

		if (GlobalConfig.Operator.OPERATION_UPLOAD_THUMB == operator
				|| GlobalConfig.Operator.OPERATION_UPLOAD_MEMBER_THUMB == operator) {
			Map<String, String> result = (Map<String, String>) out.getResult();
			if (null != result) {
				if (GlobalConfig.Operator.OPERATION_UPLOAD_THUMB == operator) {
					mThumbPath = result.get(Config.KEY_UPLOAD_THUMB);
				} else if (GlobalConfig.Operator.OPERATION_UPLOAD_MEMBER_THUMB == operator) {
					mThumbPath = result.get(Config.KEY_UPLOAD_MEMBER_PATH);
				}
				String filepath = result.get(Config.KEY_UPLOAD_LOCAL_FILEPATH);
				Log.i(TAG, "shan-->onSuccessUploadThumb() filepath = "
						+ filepath);
				ScaleImageProcessor scale = new ScaleImageProcessor((int) this
						.getResources().getDimension(
								R.dimen.subject_add_thumbnail_width),
						(int) this.getResources().getDimension(
								R.dimen.subject_add_thumbnail_height));
				mIvThumb.setImageProcessor(scale);
				if (UPLOAD_MEMBER_THUBM == mUploadType) {
					mIvThumb.reload(true);
				} else {
					mIvThumb.setUrl(filepath);
				}
			}
			mIsUploadingThumb = false;
			this.hideLoading(mLlThumbLoading);
			this.showToast(this.getString(R.string.upload_success));
		}
	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_UPLOAD_THUMB == operator
				|| GlobalConfig.Operator.OPERATION_UPLOAD_MEMBER_THUMB == operator) {
			mIsUploadingThumb = false;
			this.showToast(this.getString(R.string.upload_fail));
			this.hideLoading(mLlThumbLoading);
			Log.e(TAG, "shan-->subject upload thumb fails: " + out.getResult());
		}

	}

	/**
	 * 拍照完毕，保存图片到sd卡
	 * 
	 * @author ShanZha
	 * 
	 */
	private class SavePicTask extends AsyncTask<String, Integer, String> {
		private Bitmap mBm;

		public SavePicTask(Bitmap bm) {
			this.mBm = bm;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String filePath = params[0];
			if (FileHelper.saveToFile(filePath, mBm)) {
				return filePath;
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (TextUtils.isEmpty(result)) {
				BaseUploadThumbActivity.this
						.showToast(getString(R.string.upload_fail));
				return;
			}
			BaseUploadThumbActivity.this.doUpload(result);
		}
	}

}
