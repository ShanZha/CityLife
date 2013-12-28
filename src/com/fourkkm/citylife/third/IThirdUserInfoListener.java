package com.fourkkm.citylife.third;

public interface IThirdUserInfoListener {

	/**
	 * 获取第三方应用用户信息成功
	 * 
	 * @param type
	 * @param nickname
	 */
	public void onThirdUserInfoSuccess(int type, String nickname);

	/**
	 * 获取第三方应用用户信息失败
	 * 
	 * @param type
	 * @param error
	 */
	public void onThirdUserInfoFail(int type, String error);
}
