package com.fourkkm.citylife.widget;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogProxy {

	private ProgressDialog mDialog;

	public ProgressDialogProxy(Context ctx) {
		mDialog = new ProgressDialog(ctx);
	}

	public void setTitle(CharSequence title) {
		if (null != mDialog) {
			mDialog.setTitle(title);
		}
	}

	public void setMessage(CharSequence msg) {
		if (null != mDialog) {
			mDialog.setMessage(msg);
		}
	}

	public void showDialog() {
		if (null != mDialog) {
			mDialog.show();
		}
	}

	public void hideDialog() {
		if (null != mDialog) {
			mDialog.dismiss();
		}
	}
}
