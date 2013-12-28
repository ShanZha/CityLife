package com.fourkkm.citylife.control.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerPictures;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.itemview.UploadPicItemView;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.FloatingTranslucentProxy;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.zj.app.BaseActivity;
import com.zj.app.constant.Config;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.app.utils.DateFormatMethod;
import com.zj.app.utils.FileHelper;
import com.zj.app.utils.SdcardUtil;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.adapter.ItemSingleAdapter;

/**
 * �ϴ�����ͼƬ����
 * 
 * @author ShanZha
 * 
 */
public class BaseUploadPicActivity extends BaseActivity implements
		IFloatingItemClick, OnItemClickListener {

	private static final String TAG = "BaseUploadPicActivity";
	protected static final int REQ_LOGIN_CODE = 1;
	protected static final int REQ_CODE_ALBUM_IMG = 2;
	protected static final int REQ_CODE_TAKE_PHOTO = 3;
	protected static final int INDEX_TAKE_PHOTO = 0;
	protected static final int INDEX_ALBUM_SELECT = 1;

	protected GridView mGvPics;
	/** ��ǰ���� **/
	protected ModoerArea mCurrCountry;
	protected ModoerMembers mCurrMember;
	protected FloatingTranslucentProxy mFloatingPicProxy;
	protected BaseAdapter mAdapterPics;
	protected List<String> mPicSelectList;
	/** ͼƬ�Ƿ��ϴ���ϵ�Map��key-����ͼƬ·�� value-�Ƿ��ϴ��ɹ��� **/
	private Map<String, Boolean> mUploadFileMap;
	protected List<ModoerPictures> mPicList;
	private ProgressDialogProxy mDialogProxy;
	/** �Ƿ��ϴ�����ͼ��������ͼ **/
	protected boolean mIsUploadThumb = false;
	/** �Ƿ������ϴ�����ͼ **/
	protected boolean mIsUploadingThumb = false;
	/** �Ƿ���Ҫ�ϴ�����ͼƬ,Ĭ�ϲ���Ҫ **/
	protected boolean mIsNeedUploadPics = false;
	/** �Ƿ���Ҫ�Զ���¼��Ĭ����Ҫ **/
	protected boolean mIsAutoLogin = true;

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		if (mIsAutoLogin) {
			// ���û�е�¼���ȵ�¼
			if (!((CoreApp) AppUtils.getBaseApp(this)).isLogin()) {
				this.startActivityForResult(new Intent(this,
						LoginActivity.class), REQ_LOGIN_CODE);
				return;
			}
			this.prepare();
		}

	}

	protected void prepare() {
		mDialogProxy = new ProgressDialogProxy(this);
		mCurrCountry = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();
		mCurrMember = ((CoreApp) AppUtils.getBaseApp(this)).getCurrMember();
		this.setPicSelect();
		if (mIsNeedUploadPics) {
			this.setUploadPics();
		}
	}

	protected void showWaiting() {
		if (null != mDialogProxy) {
			mDialogProxy.showDialog();
		}
	}

	protected void hideWaiting() {
		if (null != mDialogProxy) {
			mDialogProxy.hideDialog();
		}
	}

	private void setUploadPics() {
		mPicList = new ArrayList<ModoerPictures>();
		mUploadFileMap = new HashMap<String, Boolean>();
		mAdapterPics = new ItemSingleAdapter<UploadPicItemView, ModoerPictures>(
				mPicList, this);
		mGvPics.setAdapter(mAdapterPics);
		// Ĭ�ϵ�һ��Ϊ���ͼƬ�����
		ModoerPictures pic = new ModoerPictures();
		pic.setStatus(GlobalConfig.STATUS_UPLOAD_NONE);
		mPicList.add(pic);
		this.notifyDataChanged(mAdapterPics);
		mGvPics.setOnItemClickListener(this);
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
	 * �ϴ���ͼ����������������ͼ--���̽���ͼƬ
	 * 
	 * @param filePath
	 */
	private void uploadPicWithThumb(String filePath) {
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_UPLOAD_PIC_WITH_THUMB);
		this.getStoreOperation().uploadPicWithThumb(new File(filePath), param);
	}

	/**
	 * �ϴ����
	 * 
	 * @param filePath
	 */
	protected void doUpload(String filePath) {
		System.out.println(" doUpload : " + filePath);
		if (mIsUploadThumb) {// �ϴ���������ͼ��������ʵ��
			return;
		}
		if (null != mUploadFileMap) {
			if (this.isExist(filePath)) {
				this.showToast(this.getString(R.string.upload_is_join));
				return;
			}
			if (this.isOverMax()) {
				this.showToast(this.getString(R.string.upload_max));
				return;
			}
			mUploadFileMap.put(filePath, false);
		}
		ModoerPictures pic = new ModoerPictures();
		pic.setStatus(GlobalConfig.STATUS_UPLOAD_START);
		pic.setUrl(filePath);
		mPicList.add(pic);
		this.notifyDataChanged(mAdapterPics);
		this.uploadPicWithThumb(filePath);
	}

	protected void notifyDataChanged(BaseAdapter adapter) {
		if (null != adapter) {
			adapter.notifyDataSetChanged();
		}
	}

	public void onClickUploadPic(int pos) {
		if (pos == 0) {
			if (this.isOverMax()) {
				this.showToast(this.getString(R.string.upload_max));
				return;
			}
			mIsUploadThumb = false;
			if (null != mFloatingPicProxy) {
				mFloatingPicProxy.showLocation(mGvPics, Gravity.CENTER, 0, 0);
			}
		} else {// ʧ���˵���������ϴ�
			ModoerPictures pic = (ModoerPictures) mAdapterPics.getItem(pos);
			if (pic.getStatus() == GlobalConfig.STATUS_UPLOAD_FAIL) {
				if (null != pic && !TextUtils.isEmpty(pic.getUrl())) {
					this.uploadPicWithThumb(pic.getUrl());
				}
			}
		}
	}

	/**
	 * ����ѡ�����ͼƬ���ش���
	 * 
	 * @param data
	 * @return filePath
	 */
	private void onActivityResultAlbumPic(Intent data) {
		Uri uri = data.getData();
		String filePath = FileHelper.getAbsoluteImagePath(this, uri);// �õ��ļ��ľ���·��
		String[] fileNameStrs = filePath.split(GlobalConfig.SEPERATOR);
		int size = fileNameStrs.length;
		String mFileName = fileNameStrs[size - 1].toString();

		Log.i(TAG, "onActivityResultAlbumPic() filepath = " + filePath
				+ " filename = " + mFileName);

		this.showToast(this.getString(R.string.upload_ing));
		this.doUpload(filePath);
	}

	private void onActivityResultTakePhoto(Intent data) {
		if (!SdcardUtil.hasSDCard()) {
			this.showToast(this.getString(R.string.storageNot));
			return;
		}
		// ���䱣����sd��
		Bundle bundle = data.getExtras();
		if (null == bundle) {
			BaseUploadPicActivity.this
					.showToast(getString(R.string.upload_fail));
			return;
		}
		final Bitmap bitmap = (Bitmap) bundle.get("data");
		// ��ȡϵͳͼƬ�洢·��
		File path = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		path.mkdirs();
		// ƴ���ļ�����·��
		final String mFileName = DateFormatMethod.getCurrentTime() + ".png";
		String filePath = path.getPath() + GlobalConfig.SEPERATOR + mFileName;
		this.showToast(this.getString(R.string.upload_ing));
		SavePicTask task = new SavePicTask(bitmap);
		task.execute(filePath);
	}

	private boolean isExist(String filePath) {
		return mUploadFileMap.containsKey(filePath);
	}

	private boolean isOverMax() {
		return mUploadFileMap.size() >= GlobalConfig.UPLOAD_PIC_MAX;
	}

	protected boolean isUploadFinished() {
		if (null == mUploadFileMap) {
			return true;
		}
		Set<String> set = mUploadFileMap.keySet();
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String key = it.next();
			boolean isSuccess = mUploadFileMap.get(key);
			if (!isSuccess) {
				return false;
			}
		}
		return true;
	}

	protected int getPicCount() {
		return mUploadFileMap == null ? 0 : mUploadFileMap.size();
	}

	protected void onSaveSuccess() {
		this.hideWaiting();
		this.showToast(this.getString(R.string.commit_success));
		this.finish();
	}

	private ModoerPictures getModoerPictureByUrl(String filePath) {
		if (TextUtils.isEmpty(filePath) || null == mPicList) {
			return null;
		}
		for (int i = 0; i < mPicList.size(); i++) {
			ModoerPictures pic = mPicList.get(i);
			if (filePath.equals(pic.getUrl())) {
				return pic;
			}
		}
		return null;
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
		case REQ_LOGIN_CODE:// ��¼
			this.prepare();
			break;
		case REQ_CODE_ALBUM_IMG:// ͼ��
			if (null != data) {
				this.onActivityResultAlbumPic(data);
			}
			break;
		case REQ_CODE_TAKE_PHOTO:// ����
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		// if (position == 0) {
		// if (null != mFloatingPicProxy) {
		// mFloatingPicProxy.showLocation(view, Gravity.CENTER, 0, 0);
		// }
		// }
	}

	@Override
	public void onSuccessUploadFile(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessUploadFile(out);
		int operator = out.getOperator();
		/*
		 * if (GlobalConfig.Operator.OPERATION_UPLOAD_THUMB == operator) {
		 * Map<String, String> result = (Map<String, String>) out.getResult();
		 * if (null != result) { mThumbPath =
		 * result.get(Config.KEY_UPLOAD_THUMB); String filepath =
		 * result.get(Config.KEY_UPLOAD_LOCAL_FILEPATH);
		 * System.out.println(" filePath = " + filepath); ScaleImageProcessor
		 * scale = new ScaleImageProcessor((int) this
		 * .getResources().getDimension( R.dimen.subject_add_thumbnail_width),
		 * (int) this.getResources().getDimension(
		 * R.dimen.subject_add_thumbnail_height));
		 * mIvThumb.setImageProcessor(scale); mIvThumb.setUrl(filepath); }
		 * this.hideLoading(mLlThumbLoading);
		 * this.showToast(this.getString(R.string.upload_success)); } else
		 */if (GlobalConfig.Operator.OPERATION_UPLOAD_PIC_WITH_THUMB == operator) {
			Map<String, String> result = (Map<String, String>) out.getResult();
			if (null != result) {
				String thumb = result.get(Config.KEY_UPLOAD_THUMB);
				String big = result.get(Config.KEY_UPLOAD_BIG);
				String filePath = result.get(Config.KEY_UPLOAD_LOCAL_FILEPATH);
				String filename = result.get(Config.KEY_UPLOAD_FILENAME);
				String width = result.get(Config.KEY_UPLOAD_WIDTH);
				String height = result.get(Config.KEY_UPLOAD_HEIGHT);
				String filesize = result.get(Config.KEY_UPLOAD_FILESIZE);
				System.out.println("upload Success: " + filePath);
				mUploadFileMap.put(filePath, true);
				ModoerPictures pic = this.getModoerPictureByUrl(filePath);
				if (null != pic) {
					pic.setStatus(GlobalConfig.STATUS_UPLOAD_SUCCESS);
					pic.setThumb(thumb);
					pic.setFilename(big);
					pic.setAddtime((int) CommonUtil.getCurrTimeByPHP());
					pic.setCityId(mCurrCountry);
					pic.setUid(mCurrMember);
					pic.setUsername(mCurrMember.getUsername());
					pic.setWidth(Integer.parseInt(width));
					pic.setHeight(Integer.parseInt(height));
					pic.setSize(Integer.parseInt(filesize));

					if (!TextUtils.isEmpty(filename) && filename.contains(".")) {
						pic.setTitle(filename.substring(0,
								filename.lastIndexOf(".")));
					}
					this.notifyDataChanged(mAdapterPics);
				}
			}
		}
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_SAVE_CHINA_LANE:
		case GlobalConfig.Operator.OPERATION_SAVE_PARTY:
			this.onSaveSuccess();
			break;
		}

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		/*
		 * if (GlobalConfig.Operator.OPERATION_UPLOAD_THUMB == operator) {
		 * this.showToast(this.getString(R.string.upload_fail));
		 * this.hideLoading(mLlThumbLoading); Log.e(TAG,
		 * "shan-->subject upload thumb fails: " + out.getResult()); } else
		 */if (GlobalConfig.Operator.OPERATION_UPLOAD_PIC_WITH_THUMB == operator) {
			String filePath = out.getResult() + "";
			Log.e(TAG, "shan-->subject upload pic fails: " + filePath);
			ModoerPictures pic = this.getModoerPictureByUrl(filePath);
			if (null != pic) {// �ϴ�ʧ�ܣ���������ͼ��չʾ
				pic.setStatus(GlobalConfig.STATUS_UPLOAD_FAIL);
			}
			this.notifyDataChanged(mAdapterPics);
			this.showToast(this.getString(R.string.upload_fail));
		} else if (GlobalConfig.Operator.OPERATION_SAVE_PARTY == operator
				|| GlobalConfig.Operator.OPERATION_SAVE_CHINA_LANE == operator
				|| GlobalConfig.Operator.OPERATION_SAVE_SUBJECT == operator) {
			this.hideWaiting();
			this.showToast(this.getString(R.string.commit_fail));
		}

	}

	/**
	 * ������ϣ�����ͼƬ��sd��
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
				BaseUploadPicActivity.this
						.showToast(getString(R.string.upload_fail));
				return;
			}
			BaseUploadPicActivity.this.doUpload(result);
		}
	}

}
