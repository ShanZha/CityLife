package com.fourkkm.citylife.control.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;

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
 * 上传多张图片基类
 * 
 * @author ShanZha
 * 
 */
public class BaseUploadPicActivity extends BaseUploadThumbActivity implements
		IFloatingItemClick {

	private static final String TAG = "BaseUploadPicActivity";

	protected GridView mGvPics;

	/** 当前国家 **/
	protected ModoerArea mCurrCountry;
	protected BaseAdapter mAdapterPics;
	protected List<String> mPicSelectList;
	/** 图片是否上传完毕的Map（key-本地图片路径 value-是否上传成功） **/
	private Map<String, Boolean> mUploadFileMap;
	protected List<ModoerPictures> mPicList;
	/** 是否需要上传多张图片，默认不需要 **/
	protected boolean mIsNeedUploadPics = false;

	@Override
	protected void prepare() {
		super.prepare();
		mCurrCountry = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();
		if (mIsNeedUploadPics) {
			this.setUploadPics();
		}
	}

	private void setUploadPics() {
		mPicList = new ArrayList<ModoerPictures>();
		mUploadFileMap = new HashMap<String, Boolean>();
		mAdapterPics = new ItemSingleAdapter<UploadPicItemView, ModoerPictures>(
				mPicList, this);
		mGvPics.setAdapter(mAdapterPics);
		// 默认第一项为添加图片的入口
		ModoerPictures pic = new ModoerPictures();
		pic.setStatus(GlobalConfig.STATUS_UPLOAD_NONE);
		mPicList.add(pic);
		this.notifyDataChanged(mAdapterPics);
	}

	@Override
	protected void doUpload(String filePath) {
		if (UPLOAD_PIC_WITH_THUMB == mUploadType) {
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
		} else {
			super.doUpload(filePath);
		}
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
			mUploadType = UPLOAD_PIC_WITH_THUMB;
			if (null != mFloatingPicProxy) {
				mFloatingPicProxy.showLocation(mGvPics, Gravity.CENTER, 0, 0);
			}
		} else {// 失败了点击，重新上传
			mUploadType = UPLOAD_PIC_WITH_THUMB;
			ModoerPictures pic = (ModoerPictures) mAdapterPics.getItem(pos);
			if (pic.getStatus() == GlobalConfig.STATUS_UPLOAD_FAIL) {
				if (null != pic && !TextUtils.isEmpty(pic.getUrl())) {
					pic.setStatus(GlobalConfig.STATUS_UPLOAD_START);
					this.notifyDataChanged(mAdapterPics);
					this.uploadPicWithThumb(pic.getUrl());
				}
			}
		}
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
	public void onSuccessUploadFile(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessUploadFile(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_UPLOAD_PIC_WITH_THUMB == operator) {
			Map<String, String> result = (Map<String, String>) out.getResult();
			if (null != result) {
				String thumb = result.get(Config.KEY_UPLOAD_THUMB);
				String big = result.get(Config.KEY_UPLOAD_BIG);
				String filePath = result.get(Config.KEY_UPLOAD_LOCAL_FILEPATH);
				String filename = result.get(Config.KEY_UPLOAD_FILENAME);
				String width = result.get(Config.KEY_UPLOAD_WIDTH);
				String height = result.get(Config.KEY_UPLOAD_HEIGHT);
				String filesize = result.get(Config.KEY_UPLOAD_FILESIZE);
				mUploadFileMap.put(filePath, true);
				ModoerPictures pic = this.getModoerPictureByUrl(filePath);
				Log.i(TAG, "shan-->onSuccessUploadWithThumb(): " + filePath);
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
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_UPLOAD_PIC_WITH_THUMB == operator) {
			String filePath = out.getResult() + "";
			Log.e(TAG, "shan-->subject upload pic fails: " + filePath);
			ModoerPictures pic = this.getModoerPictureByUrl(filePath);
			if (null != pic) {// 上传失败
				pic.setStatus(GlobalConfig.STATUS_UPLOAD_FAIL);
			}
			this.notifyDataChanged(mAdapterPics);
			this.showToast(this.getString(R.string.upload_fail));
		}

	}

}
