package com.fourkkm.citylife.third;

import android.os.Bundle;

public class BaseAuthListener {

	protected int mType = -1;
	protected IThirdAuthListener mAuthListener;

	public BaseAuthListener(int type, IThirdAuthListener listener) {
		this.mType = type;
		this.mAuthListener = listener;
	}

	protected void notifyAuthSuccess(Bundle bundle) {
		if (null != mAuthListener) {
			mAuthListener.onThirdAuthSuccess(mType, bundle);
		}
	}

	protected void notifyAuthFail(String error) {
		if (null != mAuthListener) {
			mAuthListener.onThirdAuthFail(mType, error);
		}
	}
}
