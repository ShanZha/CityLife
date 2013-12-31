package com.fourkkm.citylife.widget;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fourkkm.citylife.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.zj.app.utils.AppUtils;

public class LocationProxy implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener {

	private static final String TAG = "LocationProxy";
	private static final String APPTAG = "CityLife";
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	// Milliseconds per second
	public static final int MILLISECONDS_PER_SECOND = 1000;

	// The update interval
	public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

	// A fast interval ceiling
	public static final int FAST_CEILING_IN_SECONDS = 16;

	// Update interval in milliseconds
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	// A fast ceiling of update intervals, used when the app is visible
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;

	private Context mCtx;
	/** Stores the current instantiation of the location client in this object **/
	private LocationClient mLocationClient;
	/** A request to connect to Location Services **/
	private LocationRequest mLocationRequest;
	private boolean mUpdatesRequested = false;

	private FragmentManager mFragentMgr;
	private ILocationConnListener mLocConnListener;

	public LocationProxy(Context ctx, FragmentManager fragentMgr,
			ILocationConnListener connListener) {
		this.mCtx = ctx;
		this.mFragentMgr = fragentMgr;
		this.mLocConnListener = connListener;
		this.prepareDatas();
	}

	private void prepareDatas() {
		mLocationClient = new LocationClient(this.mCtx.getApplicationContext(),
				this, this);
		// Create a new global location parameters object
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest
				.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);

		// Note that location updates are off until the user turns them on
		mUpdatesRequested = false;

	}

	/**
	 * Verify that Google Play services is available before making a request.
	 * 
	 * @return true if Google Play services is available, otherwise false
	 */
	private boolean servicesConnected() {

		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(mCtx);

		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			return true;
		} else {
			// Display an error dialog
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode,
					(Activity) mCtx, 0);
			if (dialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(dialog);
				errorFragment.show(mFragentMgr, APPTAG);
			}
			return false;
		}
	}

	private void showToast(String msg) {
		Toast.makeText(mCtx, msg, Toast.LENGTH_SHORT).show();
	}

	private void showErrorDialog(int errorCode) {
		// Get the error dialog from Google Play services
		Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode,
				(Activity) mCtx, CONNECTION_FAILURE_RESOLUTION_REQUEST);
		// If Google Play services can provide an error dialog
		if (errorDialog != null) {
			// Create a new DialogFragment in which to show the error dialog
			ErrorDialogFragment errorFragment = new ErrorDialogFragment();
			// Set the dialog in the DialogFragment
			errorFragment.setDialog(errorDialog);
			// Show the error dialog in the DialogFragment
			errorFragment.show(mFragentMgr, APPTAG);
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		// TODO Auto-generated method stub
		if (null != mLocConnListener) {
			mLocConnListener.onConnectFail(mCtx
					.getString(R.string.location_conn_fail));
		}
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult((Activity) mCtx,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {

			// If no resolution is available, display a dialog to the user with
			// the error.
			showErrorDialog(connectionResult.getErrorCode());
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (null != mLocConnListener) {
			mLocConnListener.onConnected(connectionHint);
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		if (null != mLocConnListener) {
			mLocConnListener.onDisconnect();
		}
	}

	/**
	 * 注册Location改变监听
	 * 
	 * @param listener
	 */
	public void registerLocationChangedListener(LocationListener listener) {
		if (null != mLocationClient) {
			mLocationClient.requestLocationUpdates(mLocationRequest, listener);
		}
	}

	/**
	 * 取消Location改变监听
	 * 
	 * @param listener
	 */
	public void removeLocationChangedListener(LocationListener listener) {
		if (null != mLocationClient) {
			mLocationClient.removeLocationUpdates(listener);
		}
	}

	/**
	 * 获取当前位置
	 * 
	 * @return location
	 */
	public Location fetchCurrLocation() {
		if (mLocationClient != null && mLocationClient.isConnected()) {
			Location location = mLocationClient.getLastLocation();
			return location;
		}
		return null;
	}

	/**
	 * 获取当前地址
	 * 
	 * @param listener
	 */
	public void fetchAddress(IAddressListener listener) {
		// In Gingerbread and later, use Geocoder.isPresent() to see if a
		// geocoder is available.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				&& !Geocoder.isPresent()) {
			// No geocoder is present. Issue an error message
			this.showToast(mCtx.getString(R.string.no_geocoder_available));
			if (null != listener) {
				listener.onAddressError(mCtx.getString(R.string.location_fail));
			}
			return;
		}
		if (this.servicesConnected()) {
			Location location = mLocationClient.getLastLocation();
			if (null == location) {
				if (null != listener) {
					listener.onAddressError(mCtx.getString(R.string.location_fail));
				}
				return;
			}
			FecthCurrAddrTask task = new FecthCurrAddrTask(listener);
			task.execute(location);
		}

	}

	public void connect() {
		if (null != mLocationClient) {
			mLocationClient.connect();
		}
	}

	public void disconnect() {
		if (null != mLocationClient) {
			mLocationClient.disconnect();
		}
	}

	/**
	 * Define a DialogFragment to display the error dialog generated in
	 * showErrorDialog.
	 */
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		/**
		 * Default constructor. Sets the dialog field to null
		 */
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		/**
		 * Set the dialog to display
		 * 
		 * @param dialog
		 *            An error dialog
		 */
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		/*
		 * This method must return a Dialog to the DialogFragment.
		 */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	/**
	 * 获取当前地址
	 * 
	 * @author Administrator
	 * 
	 */
	private class FecthCurrAddrTask extends
			AsyncTask<Location, Integer, String> {

		private IAddressListener mListener;

		public FecthCurrAddrTask(IAddressListener listener) {
			this.mListener = listener;
		}

		@Override
		protected String doInBackground(Location... params) {
			// TODO Auto-generated method stub
			Location location = params[0];
			Geocoder geocoder = new Geocoder(mCtx.getApplicationContext(),
					Locale.getDefault());
			List<Address> addrList = null;
			try {// 仅仅获取最接近的那个地址
				addrList = geocoder.getFromLocation(location.getLatitude(),
						location.getLongitude(), 2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;

			}
			if (null != addrList && addrList.size() > 0) {
				// Get the first address
				Address address = addrList.get(0);
				StringBuilder sb = new StringBuilder();
				sb.append(address.getMaxAddressLineIndex() > 0 ? address
						.getAddressLine(0) : "");
				// sb.append(address.getLocality());
				return sb.toString();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (TextUtils.isEmpty(result)) {
				if (null != mListener) {
					mListener.onAddressError(mCtx
							.getString(R.string.location_fail));
				}
			} else {
				if (null != mListener) {
					mListener.onAddressSuccess(result);
				}
			}
		}

	}

}
