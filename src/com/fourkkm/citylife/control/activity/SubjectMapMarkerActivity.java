package com.fourkkm.citylife.control.activity;

import android.util.Log;
import android.view.View;

import com.andrnes.modoer.ModoerSubject;
import com.fourkkm.citylife.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zj.app.BaseFragmentActivity;

/**
 * 店铺位置展示界面
 * 
 * @author ShanZha
 * 
 */
public class SubjectMapMarkerActivity extends BaseFragmentActivity implements
		OnMarkerClickListener {

	private static final String TAG = "SubjectMapMarkerActivity";
	private GoogleMap mMap;

	private ModoerSubject mSubject;

	@Override
	protected void prepareViews() {
		// TODO Auto-generated method stub
		super.prepareViews();
		this.setContentView(R.layout.subject_map_marker);
		mMap = ((SupportMapFragment) this.getSupportFragmentManager()
				.findFragmentById(R.id.subject_map_marker_fragment)).getMap();
	}

	@Override
	protected void prepareDatas() {
		// TODO Auto-generated method stub
		super.prepareDatas();

		mSubject = (ModoerSubject) this.getIntent().getSerializableExtra(
				"ModoerSubject");
		if (null == mSubject) {
			Log.e(TAG, "shan-->mSubject is null");
			return;
		}

		mMap.setOnMarkerClickListener(this);
		// mMap.setOnInfoWindowClickListener(this);
		// mMap.setOnMarkerDragListener(this);

		// Hide the zoom controls as the button panel will cover it.
		mMap.getUiSettings().setZoomControlsEnabled(false);

		LatLng latlng = new LatLng(mSubject.getMapLat().doubleValue(), mSubject
				.getMapLng().doubleValue());

		MarkerOptions options = new MarkerOptions();
		options.position(latlng);
		options.draggable(false);
		options.visible(true);
		options.anchor(0.5f, 0.5f);
		options.title(mSubject.getName());
		options.snippet(mSubject.getAddress());
		options.icon(BitmapDescriptorFactory
				.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
		mMap.addMarker(options);

		// 将摄影机移动到指定的地理位置
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(latlng).zoom(17) // 缩放比例
				.bearing(0) // 地图方位（East）
				.tilt(0) // 地图倾斜角度
				.build();
		mMap.animateCamera(CameraUpdateFactory
				.newCameraPosition(cameraPosition));
	}

	public void onClickBack(View view) {
		this.finish();
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		// TODO Auto-generated method stub
		return false;
	}
}
