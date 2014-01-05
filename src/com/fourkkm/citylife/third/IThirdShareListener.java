package com.fourkkm.citylife.third;

import android.os.Bundle;

public interface IThirdShareListener {

	public void onShareSuccess(int type,Bundle bundle);
	
	public void onShareFail(int type,String error);
}
