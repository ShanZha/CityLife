package com.fourkkm.citylife.third;

public interface IThirdUserInfoListener {

	/**
	 * ��ȡ������Ӧ���û���Ϣ�ɹ�
	 * 
	 * @param type
	 * @param nickname
	 */
	public void onThirdUserInfoSuccess(int type, String nickname);

	/**
	 * ��ȡ������Ӧ���û���Ϣʧ��
	 * 
	 * @param type
	 * @param error
	 */
	public void onThirdUserInfoFail(int type, String error);
}
