package com.fourkkm.citylife.control.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fourkkm.citylife.R;
import com.zj.app.BaseFragment;

/**
 * ÷˜Ã‚œ‡≤·Fragment
 * 
 * @author ShanZha
 * 
 */
public class SubjectAlbumFragment extends BaseFragment {

	private String mThumb;

	@Override
	protected View prepareViews(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ImageView iv = (ImageView) LayoutInflater.from(this.getActivity())
				.inflate(R.layout.main_gallery_item, null);
		return iv;
	}

	public void setThumb(String url) {
		this.mThumb = url;
	}

}
