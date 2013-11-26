package com.fourkkm.citylife;

import com.andrnes.modoer.ModoerArea;
import com.zj.app.BaseApp;

public class CoreApp extends BaseApp {

	private ModoerArea mCurrArea;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
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
