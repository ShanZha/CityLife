package com.fourkkm.citylife.control.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fourkkm.citylife.CoreApp;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zj.app.BaseFragmentActivity;
import com.zj.app.utils.AppUtils;

/**
 * 地图选点界面
 * 
 * @author ShanZha
 * 
 */
public class MapMarkerActivity extends BaseFragmentActivity implements
		OnMarkerClickListener, OnMarkerDragListener {

	private static final String TAG = "MapMarkerActivity";
	private GoogleMap mMap;
	private Marker mCurrMarker;
	private TextView mTvTitle;
	private Button mBtnSure;
	private int mOperator = -1;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.subject_map_marker);
		mMap = ((SupportMapFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.subject_map_marker_fragment)).getMap();
		mTvTitle = (TextView) this
				.findViewById(R.id.titlebar_back_right_tv_title);
		mBtnSure = (Button) this
				.findViewById(R.id.titlebar_back_right_btn_operator);
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();
		Intent intent = this.getIntent();
		mOperator = intent.getIntExtra("operator", -1);

		LatLng latlng = null;
		if (GlobalConfig.IntentKey.MAP_POINT_ADD == mOperator) {
			mTvTitle.setText(this.getString(R.string.map_point));
			// 虚拟当前位置，初始时在当前位置
			latlng = new LatLng(((CoreApp) AppUtils.getBaseApp(this)).mCurrLat,
					((CoreApp) AppUtils.getBaseApp(this)).mCurrLng);
		} else if (GlobalConfig.IntentKey.MAP_POINT_ERROR == mOperator) {
			mTvTitle.setText(this.getString(R.string.subject_error_map));
			double[] locArray = intent.getDoubleArrayExtra("latlng");
			latlng = new LatLng(locArray[0],locArray[1]);
		}
		mBtnSure.setVisibility(View.VISIBLE);

		mMap.setOnMarkerClickListener(this);
		// mMap.setOnInfoWindowClickListener(this);
		mMap.setOnMarkerDragListener(this);

		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(false);

		MarkerOptions options = new MarkerOptions();
		options.position(latlng);
		options.draggable(true);
		options.visible(true);
		options.anchor(0.5f, 0.5f);
		options.title(this.getString(R.string.map_point_tips));
		// options.snippet(mSubject.getAddress());
		options.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		mCurrMarker = mMap.addMarker(options);
		//
		// 将摄影机移动到指定的地理位置
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latlng).zoom(17) // 缩放比例
				.bearing(0) // 地图方位（East）
				.tilt(0) // 地图倾斜角度
				.build();
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));

		this.onMarkerClick(mCurrMarker);
	}

	public void onClickBack(View view) {
		this.setResult(RESULT_CANCELED);
		this.finish();
	}

	public void onClickSure(View view) {
		try {
			Intent data = new Intent();
			LatLng position = mCurrMarker.getPosition();
			data.putExtra("lat", position.latitude);
			data.putExtra("lng", position.longitude);
			this.setResult(RESULT_OK, data);
			this.finish();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onMarkerDrag(Marker marker) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		// TODO Auto-generated method stub
		mCurrMarker = marker;
		LatLng position = marker.getPosition();
		Log.i(TAG, "shan-->onMarkerDragEnd(): (" + position.latitude + ","
				+ position.longitude + ")");
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		// TODO Auto-generated method stub
		LatLng position = marker.getPosition();
		Log.i(TAG, "shan-->onMarkerDragStart(): (" + position.latitude + ","
				+ position.longitude + ")");
	}
}
