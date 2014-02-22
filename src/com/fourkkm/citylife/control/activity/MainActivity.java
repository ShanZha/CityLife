package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerBcastr;
import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.util.CommonUtil;
import com.fourkkm.citylife.widget.BasePicPagerAdapter;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.IAddressListener;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.fourkkm.citylife.widget.ILocationConnListener;
import com.fourkkm.citylife.widget.LocationProxy;
import com.zj.app.BaseFragmentActivity;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.utils.AppUtils;
import com.zj.support.observer.model.Param;
import com.zj.support.widget.HackyViewPager;

/**
 * 主界面
 * 
 * @author ShanZha
 * 
 */
public class MainActivity extends BaseFragmentActivity implements
		IFloatingItemClick, OnDismissListener, ViewPager.OnPageChangeListener,
		IAddressListener, ILocationConnListener {

	private static final int REQ_WELCOME_CODE = 1;
	private TextView mTvTitle;
	private List<ModoerArea> mAreaList;
	private HackyViewPager mViewPager;
	private BasePicPagerAdapter mPagerAdapter;
	/** 图片轮播对象集 **/
	private List<ModoerBcastr> mBcastrList;
	private List<String> mBcastrUrlList;
	private RadioGroup mRadioGroup;
	/** 图片轮播加载框 **/
	private ProgressBar mProBarBcastr;
	/** 图片轮播整个View **/
	private RelativeLayout mRlBcastr;
	private TextView mTvPicName;
	private Timer mTimer;
	private int mBcastrCurrPos = 0;
	/** 图片轮播是否反向 **/
	private boolean mIsBcastrReverse = false;
	/** 国家切换-漂浮菜单代理 **/
	private FloatingOneMenuProxy mFloatingProxy;

	private LocationProxy mLocationProxy;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		setContentView(R.layout.main);

		mTvTitle = (TextView) this.findViewById(R.id.titlebar_drop_tv_title);
		mViewPager = (HackyViewPager) this.findViewById(R.id.main_view_pager);
		mRlBcastr = (RelativeLayout) this.findViewById(R.id.main_rl_scroll_pic);
		mProBarBcastr = (ProgressBar) this
				.findViewById(R.id.progress_bar_small_probar);
		mRadioGroup = (RadioGroup) this.findViewById(R.id.main_radio_group);
		mTvPicName = (TextView) this.findViewById(R.id.main_tv_pic_name);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		boolean mIsWelcome = SharedPreferenceUtil.getSharedPrefercence()
				.getBoolean(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_WELCOME);
		if (!mIsWelcome) {
			Intent intent = new Intent(this, HelperActivity.class);
			intent.putExtra("isWelcome", true);
			this.startActivityForResult(intent, REQ_WELCOME_CODE);
			return;
		}
		this.prepare();
	}

	private void prepare() {
		((CoreApp) AppUtils.getBaseApp(this)).startPushInfoService();
		mTvTitle.setText(this.getString(R.string.main_title));
		mLocationProxy = new LocationProxy(this,
				this.getSupportFragmentManager(), this);
		mEnableDoubleExit = true;
		mFloatingProxy = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_AREA);
		mFloatingProxy.setFloatingItemClickListener(this);
		mFloatingProxy.setFloatingDismissListener(this);
		mAreaList = new ArrayList<ModoerArea>();
		mBcastrList = new ArrayList<ModoerBcastr>();
		mBcastrUrlList = new ArrayList<String>();
		mViewPager.setOnPageChangeListener(this);

		this.fetchCountryArea();
	}

	@Override
	protected void prepareResume() {
		// TODO Auto-generated method stub
		super.prepareResume();
		if (null != mLocationProxy) {
			mLocationProxy.connect();
		}
	}

	@Override
	protected void preparePause() {
		// TODO Auto-generated method stub
		super.preparePause();
		if (null != mLocationProxy) {
			mLocationProxy.disconnect();
		}
	}

	@Override
	protected void prepareDestroy() {
		// TODO Auto-generated method stub
		super.prepareDestroy();
		this.stopAutoLooperPic();
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		// TODO Auto-generated method stub
		super.onActivityResult(arg0, arg1, arg2);
		if (REQ_WELCOME_CODE == arg0) {
			if (RESULT_OK != arg1) {
				this.finish();
				return;
			} else {
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_IS_WELCOME, true);
				this.prepare();
			}
		}
	}

	private void fetchCountryArea() {
		String selectCode = "from com.andrnes.modoer.ModoerArea ma where ma.enabled = 1 and ma.level = 1";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_AREA);
		this.getStoreOperation().findAll(selectCode, paramsMap, true,
				new ModoerArea(), param);
	}

	private void fetchBcastr() {
		ModoerArea city = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerBcastr mb where mb.available = 1");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		if (null != city) {
			sb.append(" and mb.city.id = :cityId");
			sb.append(" and mb.groupname = :groupName");
			paramsMap.put("cityId", city.getId());
			paramsMap.put("groupName", "indexapp");
		}
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_BCASTR);
		this.getStoreOperation().findAll(sb.toString(), paramsMap, true,
				new ModoerBcastr(), param);
	}

	private void notifyPagerAdapterChanged() {
		if (null == mPagerAdapter) {
			mPagerAdapter = new BasePicPagerAdapter(mBcastrUrlList, this);
			mViewPager.setAdapter(mPagerAdapter);
			this.onPageSelected(0);
		} else {
			mPagerAdapter.notifyDataSetChanged();
		}
	}

	private void showBcastrLoading() {
		mRlBcastr.setVisibility(View.GONE);
		mProBarBcastr.setVisibility(View.VISIBLE);
	}

	private void hideBcastrLoading() {
		mRlBcastr.setVisibility(View.VISIBLE);
		mProBarBcastr.setVisibility(View.GONE);
	}

	private void startAautoLooperPic() {
		if (null == mTimer) {
			mTimer = new Timer();
			mTimer.schedule(new GalleryTimerTask(), 3 * 1000, 3 * 1000);
		}
	}

	private boolean isCurrArea(ModoerArea area) {
		ModoerArea city = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();
		if (null == city) {
			return false;
		}
		return city.getId() == area.getId();
	}

	private void resetBcastr() {
		if (null != mBcastrList) {
			mBcastrList.clear();
		}
		if (null != mBcastrUrlList) {
			mBcastrUrlList.clear();
		}
		mBcastrCurrPos = 0;
		if (null != mRadioGroup) {
			mRadioGroup.removeAllViews();
		}
	}

	private void stopAutoLooperPic() {
		if (null != mTimer) {
			mTimer.cancel();
			mTimer = null;
		}
	}

	private void setCurrCountry() {
		try {
			Location location = mLocationProxy.fetchCurrLocation();
			if (null == location) {
				ModoerArea area = mAreaList.get(0);
				((CoreApp) AppUtils.getBaseApp(this)).setCurrArea(area);
				mTvTitle.setText(area.getName());
			} else {
				double minDistance = 0.0d;
				ModoerArea temp = null;
				for (int i = 0; i < mAreaList.size(); i++) {
					ModoerArea area = mAreaList.get(i);
					String mappoint = area.getMappoint();
					if (TextUtils.isEmpty(mappoint)) {
						continue;
					}
					String[] points = mappoint.split(",");
					double distance = CommonUtil.getDistance(
							location.getLongitude(), location.getLatitude(),
							Double.valueOf(points[0]),
							Double.valueOf(points[1]));
					if (i == 0) {
						minDistance = distance;
						temp = area;
					}
					if (distance < minDistance) {
						minDistance = distance;
						temp = area;
					}
				}
				if (null != temp) {
					SharedPreferenceUtil.getSharedPrefercence().put(
							this.getApplicationContext(),
							GlobalConfig.SharePre.KEY_CURR_AREA_ID,
							temp.getId());
				}
				((CoreApp) AppUtils.getBaseApp(this)).setCurrArea(temp);
				mTvTitle.setText(temp.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onBcastrItemClick(int position) {
		if (null != mBcastrList && (position <= mBcastrList.size() - 1)) {
			ModoerBcastr bacastr = mBcastrList.get(position);
			String url = bacastr.getItemUrl();
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.parse(url));
			this.startActivity(intent);
		}
	}

	public void onClickSwitchCountry(View view) {
		if (null == mAreaList || mAreaList.size() == 0) {
			return;
		}
		mFloatingProxy.showAsDropDown(view);
		mFloatingProxy.resetSelectItemBg(mTvTitle.getText().toString().trim());
	}

	public void onClickFood(View view) {// 美食
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_FOOD);
		intent.putExtra("operator", GlobalConfig.IntentKey.SUBJECT_NEAR);
		this.startActivity(intent);
	}

	public void onClickTravel(View view) {// 出行
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_TRAVEL);
		intent.putExtra("operator", GlobalConfig.IntentKey.SUBJECT_NEAR);
		this.startActivity(intent);
	}

	public void onClickRecreation(View view) {// 休闲
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_RECREATION);
		intent.putExtra("operator", GlobalConfig.IntentKey.SUBJECT_NEAR);
		this.startActivity(intent);
	}

	public void onClickShopping(View view) {// 购物
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_SHOPPING);
		intent.putExtra("operator", GlobalConfig.IntentKey.SUBJECT_NEAR);
		this.startActivity(intent);
	}

	public void onClickSearchCity(View view) {// 搜全城
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("operator", GlobalConfig.IntentKey.SUBJECT_SEACH_CITY);
		this.startActivity(intent);
	}

	public void onClickAnswer(View view) {// 问答
		this.startActivity(new Intent(this, AskListActivity.class));
	}

	public void onClickParty(View view) {// 聚会
		this.startActivity(new Intent(this, PartyListActivity.class));
	}

	public void onClickChineseLane(View view) {// 唐人巷
		this.startActivity(new Intent(this, ChineseLaneListActivity.class));
	}

	public void onClickVipCenter(View view) {// 会员中心
		this.startActivity(new Intent(this, UserCenterActivity.class));
	}

	public void onClickMore(View view) {// 更多
		this.startActivity(new Intent(this, MoreActivity.class));
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);

		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_AREA == operator) {
			List<Object> results = (List<Object>) out.getResult();
			if (null != results) {
				int currAreaId = SharedPreferenceUtil.getSharedPrefercence()
						.getInt(this.getApplicationContext(),
								GlobalConfig.SharePre.KEY_CURR_AREA_ID);
				ModoerArea currArea = null;
				List<String> countryNames = new ArrayList<String>();
				for (int i = 0; i < results.size(); i++) {
					ModoerArea area = (ModoerArea) results.get(i);
					if (null == currArea && currAreaId == area.getId()) {
						currArea = area;
					}
					countryNames.add(area.getName());
					mAreaList.add(area);
				}
				mFloatingProxy.setDatas(countryNames);
				// 如果已经设置过当前Area，则默认，否则提示用户选择
				if (null != currArea) {
					((CoreApp) AppUtils.getBaseApp(this)).setCurrArea(currArea);
					mTvTitle.setText(currArea.getName());
				} else {
					this.setCurrCountry();
					// this.onClickSwitchCountry(mTvTitle);
				}
				this.showBcastrLoading();
				this.fetchBcastr();
			}
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_BCASTR == operator) {
			if (null == mBcastrList) {
				return;
			}
			this.resetBcastr();
			List<Object> results = (List<Object>) out.getResult();
			if (null != results) {
				for (int i = 0; i < results.size(); i++) {
					ModoerBcastr bcastr = (ModoerBcastr) results.get(i);
					mBcastrList.add(bcastr);
					mBcastrUrlList.add(GlobalConfig.URL_PIC + bcastr.getLink());
					// 根据ModoerBcastr条数，加上单选个数
					RadioButton btn = (RadioButton) this.getLayoutInflater()
							.inflate(R.layout.main_radio_btn, null);
					mRadioGroup.addView(btn);
				}
			}
			this.notifyPagerAdapterChanged();
			int size = mBcastrList.size();
			if (size > 1) {
				this.startAautoLooperPic();
			}
			if (size > 0) {
				this.hideBcastrLoading();
			} else {
				mRlBcastr.setVisibility(View.GONE);
				mProBarBcastr.setVisibility(View.GONE);
			}
		}

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		super.onFails(out);

	}

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		ModoerArea area = mAreaList.get(pos);
		if (this.isCurrArea(area)) {
			return;
		}
		SharedPreferenceUtil.getSharedPrefercence().put(
				this.getApplicationContext(),
				GlobalConfig.SharePre.KEY_CURR_AREA_ID, area.getId());
		mTvTitle.setText(area.getName());
		((CoreApp) AppUtils.getBaseApp(this)).setCurrArea(area);
		this.showBcastrLoading();
		this.fetchBcastr();
	}

	@Override
	public void onDismiss() {
		// TODO Auto-generated method stub
		ModoerArea area = ((CoreApp) AppUtils.getBaseApp(this)).getCurrArea();
		if (null == area) {// 如果没选择，则默认第一项
			if (mAreaList.size() > 0) {
				ModoerArea a = mAreaList.get(0);
				SharedPreferenceUtil.getSharedPrefercence().put(
						this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_CURR_AREA_ID, a.getId());
				((CoreApp) AppUtils.getBaseApp(this)).setCurrArea(a);
				this.showBcastrLoading();
				this.fetchBcastr();
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		mBcastrCurrPos = arg0;
		try {
			RadioButton radBtn = (RadioButton) mRadioGroup.getChildAt(arg0);
			radBtn.setChecked(true);
			ModoerBcastr bcastr = mBcastrList.get(arg0);
			if (null != bcastr) {
				mTvPicName.setText(bcastr.getItemtitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onAddressSuccess(String addr) {
		// TODO Auto-generated method stub
		System.out.println(" onAddressSuccess addr: " + addr);
	}

	@Override
	public void onAddressError(String error) {
		// TODO Auto-generated method stub
		System.out.println(" onAddressError: " + error);
	}

	@Override
	public void onConnected(Bundle data) {
		// TODO Auto-generated method stub
		if (null != mLocationProxy) {
			Location location = mLocationProxy.fetchCurrLocation();
			if (null == location) {

			}
		}
	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectFail(String error) {
		// TODO Auto-generated method stub

	}

	/**
	 * 图片自动轮播任务
	 * 
	 * @author ShanZha
	 * 
	 */
	private class GalleryTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			AppUtils.getHandler(MainActivity.this).post(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (null != mBcastrUrlList) {
						int size = mBcastrUrlList.size();
						if (size == 0) {
							return;
						}
						if (mBcastrCurrPos == size - 1) {
							mIsBcastrReverse = true;
						}
						if (mBcastrCurrPos == 0) {
							mIsBcastrReverse = false;
						}
						if (mIsBcastrReverse) {
							mBcastrCurrPos--;
						} else {
							mBcastrCurrPos++;
						}
						mViewPager.setCurrentItem(mBcastrCurrPos, true);
					}
				}
			});
		}

	}

}
