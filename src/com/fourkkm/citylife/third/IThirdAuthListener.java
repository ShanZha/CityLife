package com.fourkkm.citylife.third;

import android.os.Bundle;

public interface IThirdAuthListener {

	/**
	 * ��Ȩ�ɹ��ص�(���߳�)
	 * 
	 * @param type
	 * @param bundle
	 */
	public void onThirdAuthSuccess(int type, Bundle bundle);

	/**
	 * ��Ȩʧ�ܻص�(���߳�)
	 * 
	 * @param type
	 * @param e
	 */
	public void onThirdAuthFail(int type, String e);

	/**
	 * ��Ȩȡ���ص�(���߳�)
	 * 
	 * @param type
	 */
	public void onThirdAuthCancel(int type);
}
