package com.fourkkm.citylife.control.activity;

import android.view.View;
import android.widget.EditText;

import com.fourkkm.citylife.R;
import com.zj.app.BaseActivity;

/**
 * �������
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

	public void onClickUploadPic(View view) {// �ϴ�ͼƬ

	}

	public void onClickAddrMap(View view) {// ��ͼѡ��

	}

	public void onClickAdd(View view) {// ���

	}
}
