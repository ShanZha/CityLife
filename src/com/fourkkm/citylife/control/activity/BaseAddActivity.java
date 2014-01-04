package com.fourkkm.citylife.control.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerFenlei;
import com.andrnes.modoer.ModoerMembers;
import com.andrnes.modoer.ModoerParty;
import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.AreaManager;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.widget.ProgressDialogProxy;
import com.fourkkm.citylife.widget.SpinnerAdapter;
import com.zj.app.constant.Config;
import com.zj.app.db.dao.SqliteUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.image.ScaleImageProcessor;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.AsyncImageView;

/**
 * 添加店铺、聚会、唐人巷基类
 * 
 * @author ShanZha
 * 
 */
public class BaseAddActivity extends BaseUploadPicActivity {

	private static final String TAG = "BaseAddActivity";

	protected LinearLayout mLlArea, mLlAreaLoading;
	protected Spinner mSpAreaFirst, mSpAreaSecond;;
	protected AreaManager mAreaMgr;
	protected ArrayAdapter<String> mAdapterAreaFirst, mAdapterAreaSecond;
	protected List<String> mAreaFirst, mAreaSecond;
	protected Button mBtnCommit;
	private ProgressDialogProxy mDialogProxy;

	@Override
	protected void prepare() {
		super.prepare();
		mAreaMgr = new AreaManager();

		mDialogProxy = new ProgressDialogProxy(this);
		this.setSpAdapter();
		this.fetchArea();
	}

	protected void setSpAdapter() {
		mAreaFirst = new ArrayList<String>();
		mAreaSecond = new ArrayList<String>();
		mAdapterAreaFirst = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, mAreaFirst);
		mSpAreaFirst.setAdapter(mAdapterAreaFirst);
		((ArrayAdapter<String>) mAdapterAreaFirst)
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpAreaFirst.setOnItemSelectedListener(mAreaItemSelectedListener);

		mAdapterAreaSecond = new SpinnerAdapter(this,
				android.R.layout.simple_spinner_item, mAreaSecond);
		mSpAreaSecond.setAdapter(mAdapterAreaSecond);
		((ArrayAdapter<String>) mAdapterAreaSecond)
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

	/**
	 * 获取某国家下的所有区域
	 */
	private void fetchArea() {
		if (null == mCurrCountry) {
			Log.e(TAG, "shan-->mCurrCountry is null");
			return;
		}
		int countryId = mCurrCountry.getId();
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerArea ma where (ma.pid.id = ");
		sb.append(countryId);
		sb.append(" and ma.enabled = 1) or");
		sb.append(" (ma.pid.pid.id = ");
		sb.append(countryId);
		sb.append(" and ma.pid.enabled = 1)");
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT_AREA);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		this.getStoreOperation().findAll(sb.toString(), paramsMap, true,
				new ModoerArea(), param);
	}

	// protected void changedBtnState(Button btn, boolean enable) {
	// if (null != btn) {
	// btn.setEnabled(enable);
	// btn.setClickable(enable);
	// btn.setFocusable(enable);
	// }
	// }
	// protected void setBtnText(Button btn, String text) {
	// if (null != btn) {
	// btn.setText(text);
	// }
	// }

	protected void notifyDataChanged(BaseAdapter adapter) {
		if (null != adapter) {
			adapter.notifyDataSetChanged();
		}
	}

	protected void onSaveSuccess() {
		SqliteUtil.getInstance(this.getApplicationContext()).deleteByClassName(
				ModoerMembers.class.getName());
		this.hideWaiting();
		this.showToast(this.getString(R.string.commit_success));
		this.finish();
	}

	/**
	 * 根据地区的父类姓名重置子类列表
	 * 
	 * @param parentName
	 */
	protected void resetAreaChildList(String parentName) {
		ModoerArea parent = mAreaMgr.getSubjectAreaByName(parentName);
		List<String> secondList = mAreaMgr.getAreaCountyChild(parent.getId());
		mAreaSecond.clear();
		mAreaSecond.addAll(secondList);
		secondList = null;
		this.notifyDataChanged(mAdapterAreaSecond);
	}

	public void onClickThumb(View view) {// 缩略图
		if (mIsUploadingThumb) {
			return;
		}
		mUploadType = UPLOAD_THUMB;
		if (null != mFloatingPicProxy) {
			mFloatingPicProxy.showLocation(view, Gravity.CENTER, 0, 0);
		}
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);
		List<Object> results = (List<Object>) out.getResult();
		if (null == results) {
			Log.e(TAG, "shan-->results is null");
			return;
		}
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_SUBJECT_AREA == operator) {
			for (int i = 0; i < results.size(); i++) {
				ModoerArea area = (ModoerArea) results.get(i);
				mAreaMgr.add(area);
			}
			try {// Step 1：设置地区一级（省/州）列表
				List<String> firstList = mAreaMgr.getAreaCityChild(mCurrCountry
						.getId());
				mAreaFirst.addAll(firstList);
				firstList = null;
				this.notifyDataChanged(mAdapterAreaFirst);
				// Step 2：设置地区二级列表(县级，默认显示第一级的第一个地区的所有子类别)
				this.resetAreaChildList(mAreaFirst.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.showLoading(mLlArea);
			this.hideLoading(mLlAreaLoading);
		}
	}

	@Override
	public void onSuccessUploadFile(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessUploadFile(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_UPLOAD_THUMB == operator) {
			Map<String, String> result = (Map<String, String>) out.getResult();
			if (null != result) {
				mThumbPath = result.get(Config.KEY_UPLOAD_THUMB);
				String filepath = result.get(Config.KEY_UPLOAD_LOCAL_FILEPATH);
				Log.i(TAG, "shan-->onSuccessUploadThumb() filepath = "
						+ filepath);
				ScaleImageProcessor scale = new ScaleImageProcessor((int) this
						.getResources().getDimension(
								R.dimen.subject_add_thumbnail_width),
						(int) this.getResources().getDimension(
								R.dimen.subject_add_thumbnail_height));
				mIvThumb.setImageProcessor(scale);
				mIvThumb.setUrl(filepath);
			}
			mIsUploadingThumb = false;
			this.hideLoading(mLlThumbLoading);
			this.showToast(this.getString(R.string.upload_success));
		}
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessSaveOrUpdate(out);
		int operator = out.getOperator();
		switch (operator) {
		case GlobalConfig.Operator.OPERATION_SAVE_CHINA_LANE:
			SqliteUtil.getInstance(this.getApplicationContext())
					.deleteByClassName(ModoerFenlei.class.getName());
			this.onSaveSuccess();
			break;
		case GlobalConfig.Operator.OPERATION_SAVE_PARTY:
			SqliteUtil.getInstance(this.getApplicationContext())
					.deleteByClassName(ModoerParty.class.getName());
			this.onSaveSuccess();
			break;
		case GlobalConfig.Operator.OPERATION_SAVE_SUBJECT:
			SqliteUtil.getInstance(this.getApplicationContext())
					.deleteByClassName(ModoerSubject.class.getName());
			this.onSaveSuccess();
			break;
		}

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);
		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_UPLOAD_THUMB == operator) {
			mIsUploadingThumb = false;
			this.showToast(this.getString(R.string.upload_fail));
			this.hideLoading(mLlThumbLoading);
			Log.e(TAG, "shan-->subject upload thumb fails: " + out.getResult());
		} else if (GlobalConfig.Operator.OPERATION_SAVE_PARTY == operator
				|| GlobalConfig.Operator.OPERATION_SAVE_CHINA_LANE == operator
				|| GlobalConfig.Operator.OPERATION_SAVE_SUBJECT == operator) {
			this.hideWaiting();
			this.showToast(this.getString(R.string.commit_fail));
		}

	}

	/**
	 * 地区一级列表监听
	 */
	private OnItemSelectedListener mAreaItemSelectedListener = new OnItemSelectedListener() {

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			resetAreaChildList(mAreaFirst.get(position));
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub

		}
	};

}
