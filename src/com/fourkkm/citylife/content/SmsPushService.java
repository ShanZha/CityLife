package com.fourkkm.citylife.content;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.andrnes.modoer.ModoerPmsgs;
import com.fourkkm.citylife.R;
import com.fourkkm.citylife.constant.GlobalConfig;
import com.fourkkm.citylife.control.activity.SmsDetailActivity;
import com.fourkkm.citylife.util.CommonUtil;
import com.zj.app.db.dao.SharedPreferenceUtil;
import com.zj.app.http.StoreOperation;
import com.zj.support.observer.Observer;
import com.zj.support.observer.inter.ICallback;
import com.zj.support.observer.model.Param;

/**
 * 后台轮询短信服务
 * 
 * @author ShanZha
 * 
 */
public class SmsPushService extends Service implements ICallback {

	private static final String TAG = "SmsPushService";
	/** Push间隔时间-->两小时 **/
	private static final int PUSH_INTERVAL_TIME = 1000 * 60 * 60 * 2;
	private static final int NOTIFICATION_ID = 10;
	private Timer mTimer = null;
	private NotificationManager mNotifmgr = null;
	private Notification mNotification = null;
	private boolean mTimerStarted = false;

	private int mRetryCount = 0;

	private TimerTask mTask = new TimerTask() {

		@Override
		public void run() {
			SmsPushService.this.fetchInfoPush();
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i(TAG, "shan-->onCreate()");
		mNotifmgr = (NotificationManager) this
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Observer.getObserver().addCallback(this.hashCode(), this);
		this.startTimer();
	}

	private void startTimer() {
		if (mTimerStarted) {
			return;
		}
		mTimer = new Timer();
		mTimer.schedule(mTask, 0, PUSH_INTERVAL_TIME);
		mTimerStarted = true;
	}

	private void stopTimer() {
		if (null != mTimer) {
			mTimer.cancel();
			mTimer = null;
		}
		mTimerStarted = false;
	}

	private String buildSelectCode() {
		int memberId = SharedPreferenceUtil.getSharedPrefercence().getInt(
				this.getApplicationContext(),
				GlobalConfig.SharePre.KEY_MEMBER_ID);
		StringBuilder sb = new StringBuilder();
		sb.append("from com.andrnes.modoer.ModoerPmsgs mp ");
		if (memberId > 0) {
			sb.append(" where mp.recvuid.id in (" + memberId + ","
					+ GlobalConfig.PUBLIC_MEMBER_ID + ")");
		} else {
			sb.append(" where mp.recvuid.id in ("
					+ GlobalConfig.PUBLIC_MEMBER_ID + ")");
		}
		long lastPushTime = SharedPreferenceUtil.getSharedPrefercence()
				.getLong(this.getApplicationContext(),
						GlobalConfig.SharePre.KEY_PUSH_TIME);
		sb.append(" and mp.posttime >= " + lastPushTime);
		sb.append(" and mp.isNew = 1");
		sb.append(" order by mp.posttime DESC");
		Log.i(TAG, "shan-->selectCode = " + sb.toString());
		return sb.toString();
	}

	private void fetchInfoPush() {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("max", GlobalConfig.MAX_ALL);
		paramsMap.put("offset", 0);
		this.getStoreOperation().findAll(this.buildSelectCode(), paramsMap,
				false, new ModoerPmsgs(),
				new Param(this.hashCode(), GlobalConfig.URL_CONN));
	}

	private StoreOperation getStoreOperation() {
		return StoreOperation.getInstance(this);
	}

	private void showNotification(ModoerPmsgs sms) {
		mNotification = new Notification();
		mNotification.icon = R.drawable.logo;
		mNotification.tickerText = sms.getSubject();
		Intent intent = new Intent(this, SmsDetailActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("isPush", true);
		intent.putExtra("sms", sms);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		mNotification.setLatestEventInfo(this, sms.getSubject(),
				sms.getContent(), contentIntent);
		mNotifmgr.notify(NOTIFICATION_ID, mNotification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Observer.getObserver().deleteCallback(this.hashCode(), this);
		super.onDestroy();
		this.stopTimer();
		Log.i(TAG, "shan-->onDestroy()");
	}

	@Override
	public void onSuccessFindAll(Param out) {
		// TODO Auto-generated method stub
		Log.i(TAG, "shan-->Fetch ModoerPmsgs success,there are something?");
		mRetryCount = 0;
		List<ModoerPmsgs> infos = (List<ModoerPmsgs>) out.getResult();
		if (null != infos && !infos.isEmpty()) {
			Log.i(TAG, "shan-->Fetch ModoerPmsgs size = " + infos.size());
			ModoerPmsgs sms = infos.get(infos.size() - 1);
			this.showNotification(sms);
		}
		SharedPreferenceUtil.getSharedPrefercence().put(
				this.getApplicationContext(),
				GlobalConfig.SharePre.KEY_PUSH_TIME,
				CommonUtil.getCurrTimeByPHP());
	}

	@Override
	public void onSuccessSaveOrUpdate(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessSaveOrUpdateArray(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessDelete(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessShowDomain(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessFind(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessUploadFile(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccessExecuteQuery(Param out) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFails(Param out) {
		// TODO Auto-generated method stub
		Log.e(TAG, "shan-->Fetch ModoerPmsgs fails: " + mRetryCount);
		if (mRetryCount < 3) {
			mRetryCount++;
			this.fetchInfoPush();
		}
	}
}
