package com.fourkkm.citylife;

import com.andrnes.modoer.ModoerArea;
import com.andrnes.modoer.ModoerMembers;
import com.zj.app.BaseApp;

public class CoreApp extends BaseApp {

	private ModoerMembers mCurrMember;
	/** ��ǰ�������� **/
	private ModoerArea mCurrArea;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
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

	@Override
	protected void exit(boolean logout) {
		// TODO Auto-generated method stub
		super.exit(logout);

		System.exit(0);
	}

}
