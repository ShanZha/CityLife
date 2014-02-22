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
	/** ��ǰ�������� **/
	private ModoerArea mCurrArea;
	/** ��ǰ����--���� **/
	public double mCurrLng = 151.063;
	/** ��ǰγ��--���� **/
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
	 * �Ƿ��Ѿ���¼
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
	 * ��ѯ������
	 */
	public void startPushInfoService() {
		Log.i(TAG, "shan-->pushInfoService started");
		this.startService(new Intent(this, SmsPushService.class));
	}

	/**
	 * ��ѯ����ֹͣ
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
