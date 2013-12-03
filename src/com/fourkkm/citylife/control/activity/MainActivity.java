package com.fourkkm.citylife.control.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.fourkkm.citylife.widget.BasePicPagerAdapter;
import com.fourkkm.citylife.widget.FloatingOneMenuProxy;
import com.fourkkm.citylife.widget.IAddressListener;
import com.fourkkm.citylife.widget.IFloatingItemClick;
import com.fourkkm.citylife.widget.LocationProxy;
import com.zj.app.BaseFragmentActivity;
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
		IFloatingItemClick, ViewPager.OnPageChangeListener, IAddressListener {

	private TextView mTvTitle;
	private List<ModoerArea> mAreaList;
	// private Gallery mGallery;
	// private BaseAdapter mGalleryAdapter;
	private ViewPager mViewPager;
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
		// mGallery = (Gallery) this.findViewById(R.id.main_gallery);
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
		mLocationProxy = new LocationProxy(this,
				this.getSupportFragmentManager());
		mEnableDoubleExit = true;
		mFloatingProxy = new FloatingOneMenuProxy(this,
				GlobalConfig.FloatingType.TYPE_AREA);
		mFloatingProxy.setFloatingItemClickListener(this);
		mAreaList = new ArrayList<ModoerArea>();
		mBcastrList = new ArrayList<ModoerBcastr>();
		mBcastrUrlList = new ArrayList<String>();
		// mGallery.setOnItemSelectedListener(this);
		// mGallery.setOnItemClickListener(this);
		mViewPager.setOnPageChangeListener(this);

		this.fetchCountryArea();
		this.showBcastrLoading();
		this.fetchBcastr();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		mLocationProxy.connect();
	}

	@Override
	protected void prepareRelease() {
		// TODO Auto-generated method stub
		super.prepareRelease();
		mLocationProxy.disconnect();
	}

	@Override
	protected void prepareDestroy() {
		// TODO Auto-generated method stub
		super.prepareDestroy();
		this.stopAutoLooperPic();
	}

	private void fetchCountryArea() {
		String selectCode = "from com.andrnes.modoer.ModoerArea ma where ma.enabled = 1 and ma.level = 1";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_AREA);
		this.getStoreOperation().findAll(selectCode,
				new HashMap<String, Object>(), true, new ModoerArea(), param);
	}

	private void fetchBcastr() {// ？？？？暂时未定位，所以没加上地区的限制
		String selectCode = "from com.andrnes.modoer.ModoerBcastr mb where mb.available = 1 and mb.city.level = 1";
		Param param = new Param(this.hashCode(), GlobalConfig.URL_CONN);
		param.setOperator(GlobalConfig.Operator.OPERATION_FINDALL_BCASTR);
		this.getStoreOperation()
				.findAll(selectCode, new HashMap<String, Object>(), false,
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
			mTimer.schedule(new GalleryTimerTask(), 10000, 5000);
		}
	}

	private void stopAutoLooperPic() {
		if (null != mTimer) {
			mTimer.cancel();
			mTimer = null;
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
		mFloatingProxy.showAsDropDown(mTvTitle);
	}

	public void onClickFood(View view) {// 美食
		// mLocationProxy.fetchAddress(this);
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_FOOD);
		this.startActivity(intent);
	}

	public void onClickTravel(View view) {// 出行
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_TRAVEL);
		this.startActivity(intent);
	}

	public void onClickRecreation(View view) {// 休闲
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_RECREATION);
		this.startActivity(intent);
	}

	public void onClickShopping(View view) {// 购物
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("category", GlobalConfig.CATEGORY_SHOPPING);
		this.startActivity(intent);
	}

	public void onClickSearchCity(View view) {// 搜全城
		Intent intent = new Intent(this, SubjectListActivity.class);
		intent.putExtra("isSearchCity", true);
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
//		this.startActivity(new Intent(this, MoreActivity.class));
		this.startActivity(new Intent(this, LoginActivity.class));
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		super.onSuccessFindAll(out);

		int operator = out.getOperator();
		if (GlobalConfig.Operator.OPERATION_FINDALL_AREA == operator) {
			List<Object> results = (List<Object>) out.getResult();
			if (null != results) {
				List<String> countryNames = new ArrayList<String>();
				for (int i = 0; i < results.size(); i++) {
					ModoerArea area = (ModoerArea) results.get(i);
					if (i == 0) {// 默认第一个为所处国家
						mTvTitle.setText(area.getName());
						((CoreApp) AppUtils.getBaseApp(this)).setCurrArea(area);
					}
					countryNames.add(area.getName());
					mAreaList.add(area);
				}
				mFloatingProxy.setDatas(countryNames);
			}
		} else if (GlobalConfig.Operator.OPERATION_FINDALL_BCASTR == operator) {
			if (null == mBcastrList) {
				return;
			}
			List<Object> results = (List<Object>) out.getResult();
			if (null != results) {
				for (int i = 0; i < results.size(); i++) {
					ModoerBcastr bcastr = (ModoerBcastr) results.get(i);
					mBcastrList.add(bcastr);
					mBcastrUrlList.add(GlobalConfig.URL_PIC + bcastr.getLink());
				}
			}
			// 根据ModoerBcastr条数，加上单选个数
			for (int i = 0; i < mBcastrList.size(); i++) {
				// RadioButton btn = new RadioButton(this);
				// btn.setButtonDrawable(this.getResources().getDrawable(
				// R.drawable.main_radio_btn_selector));
				RadioButton btn = (RadioButton) this.getLayoutInflater()
						.inflate(R.layout.main_radio_btn, null);
				// btn.setBackground(this.getResources().getDrawable(
				// R.drawable.main_recreation_selector));
				// btn.setBackgroundDrawable();
				mRadioGroup.addView(btn);
			}

			// this.notifyGalleryDataChanged();
			this.notifyPagerAdapterChanged();
			this.hideBcastrLoading();
			this.startAautoLooperPic();
		}

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
						// mGallery.setSelection(mBcastrCurrPos);
					}
				}
			});
		}

	}

	// @Override
	// public void onItemSelected(AdapterView<?> parent, View view, int
	// position,
	// long id) {
	// // TODO Auto-generated method stub
	// mBcastrCurrPos = position;
	// RadioButton radBtn = (RadioButton) mRadioGroup.getChildAt(position);
	// radBtn.setChecked(true);
	// ModoerBcastr bcastr = mBcastrList.get(position);
	// if (null != bcastr) {
	// mTvPicName.setText(bcastr.getItemtitle());
	// }
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> parent) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// // TODO Auto-generated method stub
	// ModoerBcastr bacastr = mBcastrList.get(position);
	// String url = bacastr.getItemUrl();
	// Intent intent = new Intent(Intent.ACTION_VIEW);
	// intent.setData(Uri.parse(url));
	// this.startActivity(intent);
	// }

	@Override
	public void onFloatingItemClick(int pos, String key, int type) {
		// TODO Auto-generated method stub
		ModoerArea area = mAreaList.get(pos);
		mTvTitle.setText(area.getName());
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
}
