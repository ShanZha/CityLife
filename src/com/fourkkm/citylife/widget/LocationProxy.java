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
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
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
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

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

	public LocationProxy(Context ctx, FragmentManager fragentMgr) {
		this.mCtx = ctx;
		this.mFragentMgr = fragentMgr;
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
		Log.i(TAG, "shan-->onConnected");
		if (null != mLocationClient) {
			mLocationClient.requestLocationUpdates(mLocationRequest, this);
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		Log.i(TAG, "shan-->onDisconnected");
	}

	public void fetchCurrLocation() {
		if (mLocationClient != null && mLocationClient.isConnected()) {
			Location location = mLocationClient.getLastLocation();
			Log.i(TAG,
					"shan-->fecthCurrLocation: "
							+ (null == location ? " locatoin is null"
									: location.toString()));
		}
	}

	public void fetchAddress(IAddressListener listener) {
		// In Gingerbread and later, use Geocoder.isPresent() to see if a
		// geocoder is available.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				&& !Geocoder.isPresent()) {
			// No geocoder is present. Issue an error message
			this.showToast(mCtx.getString(R.string.no_geocoder_available));
			return;
		}
		if (this.servicesConnected()) {
			Location location = mLocationClient.getLastLocation();
			AppUtils.getExecutors(mCtx).submit(
					new GetAddressTask(location, listener));
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
	 * 获取当前地址的任务
	 * 
	 * @author ShanZha
	 * 
	 */
	private class GetAddressTask implements Runnable {

		private Location mLocation;
		private IAddressListener mListener;

		public GetAddressTask(Location location, IAddressListener listener) {
			this.mLocation = location;
			this.mListener = listener;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (null == mLocation) {
				if (null != mListener) {
					mListener.onAddressError("Location is null");
				}
				return;
			}
			Geocoder geocoder = new Geocoder(mCtx.getApplicationContext(),
					Locale.getDefault());
			List<Address> addrList = null;
			try {// 仅仅获取最接近的那个地址
				addrList = geocoder.getFromLocation(mLocation.getLatitude(),
						mLocation.getLongitude(), 2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				if (null != mListener) {
					mListener.onAddressError(e.getMessage());
				}
			}
			if (null != addrList && addrList.size() > 0) {
				// Get the first address
				Address address = addrList.get(0);
				StringBuilder sb = new StringBuilder();
				sb.append(address.getMaxAddressLineIndex() > 0 ? address
						.getAddressLine(0) : "");
				// sb.append(address.getLocality());
				if (null != mListener) {
					mListener.onAddressSuccess(sb.toString());
				}
			} else {
				if (null != mListener) {
					mListener.onAddressError("Address List is null");
				}
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Log.i(TAG, "shan-->onLocationChanged: " + location.toString());
	}
}
