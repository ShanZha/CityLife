package com.fourkkm.citylife;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerMembers;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.taobao.top.android.TopAndroidClient;
import com.zj.app.BaseApp;

public class CoreApp extends BaseApp {

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

	@Override
	protected void exit(boolean logout) {
		// TODO Auto-generated method stub
		super.exit(logout);

		System.exit(0);
	}

}
