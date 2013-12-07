package com.fourkkm.citylife.control.activity;

import android.view.View;
import android.widget.EditText;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * 商铺添加
 * 
 * @author ShanZha
 * 
 */
public class SubjectAddActivity extends BaseActivity {

	private EditText mEtName, mEtAddr, mEtTel, mEtImpression;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.subject_add);

		mEtName = (EditText) this.findViewById(R.id.more_subject_add_et_name);
		mEtAddr = (EditText) this.findViewById(R.id.more_subject_add_et_addr);
		mEtTel = (EditText) this.findViewById(R.id.more_subject_add_et_tel);
		mEtImpression = (EditText) this
				.findViewById(R.id.more_subject_add_et_impression);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
	}

	public void onClickBack(View view) {
		this.finish();
	}

	public void onClickUploadPic(View view) {// 上传图片

	}

	public void onClickAddrMap(View view) {// 地图选点

	}

	public void onClickAdd(View view) {// 添加

	}
}
