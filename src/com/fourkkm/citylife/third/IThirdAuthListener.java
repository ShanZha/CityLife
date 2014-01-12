package com.fourkkm.citylife.third;

import android.os.Bundle;

public interface IThirdAuthListener {

	/**
	 * 鉴权成功回调(主线程)
	 * 
	 * @param type
	 * @param bundle
	 */
	public void onThirdAuthSuccess(int type, Bundle bundle);

	/**
	 * 鉴权失败回调(主线程)
	 * 
	 * @param type
	 * @param e
	 */
	public void onThirdAuthFail(int type, String e);

	/**
	 * 鉴权取消回调(主线程)
	 * 
	 * @param type
	 */
	public void onThirdAuthCancel(int type);
}
