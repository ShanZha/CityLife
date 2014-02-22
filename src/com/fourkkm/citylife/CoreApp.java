package com.fourkkm.citylife;

import android.content.Intent;
import android.util.Log;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerMembers;
import com.baidu.mobstat.StatService;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.content.SmsPushService;
import com.taobao.top.android.TopAndroidClient;
import com.zj.app.BaseApp;

public class CoreApp extends BaseApp {

	private static final String TAG = "CoreApp";
	private ModoerMembers mCurrMember;
	/** 当前所处国家 **/
	private ModoerArea mCurrArea;
	/** 当前经度--测试 **/
	public double mCurrLng = 151.063;
	/** 当前纬度--测试 **/
	public double mCurrLat = -33.825;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		TopAndroidClient.registerAndroidClient(this,
				GlobalConfig.Third.TAOBAO_APP_KEY,
				GlobalConfig.Third.TAOBAO_APP_SECRET,
				GlobalConfig.Third.TAOBAO_REDIRECT_URL);

		StatService.setDebugOn(true);
	}

	public void setCurrMember(ModoerMembers member) {
		this.mCurrMember = member;
	}

	public ModoerMembers getCurrMember() {
		return this.mCurrMember;
	}

	/**
	 * 是否已经登录
	 * 
	 * @return
	 */
	public boolean isLogin() {
		return null != this.mCurrMember;
	}

	public void setCurrArea(ModoerArea area) {
		this.mCurrArea = area;
	}

	public ModoerArea getCurrArea() {
		return this.mCurrArea;
	}

	/**
	 * 轮询服务开启
	 */
	public void startPushInfoService() {
		Log.i(TAG, "shan-->pushInfoService started");
		this.startService(new Intent(this, SmsPushService.class));
	}

	/**
	 * 轮询服务停止
	 */
	public void stopPushInfoService() {
		Log.i(TAG, "shan-->pushInfoService stop");
		this.stopService(new Intent(this, SmsPushService.class));
	}

	@Override
	protected void exit(boolean logout) {
		// TODO Auto-generated method stub
		super.exit(logout);

		System.exit(0);
	}

}
