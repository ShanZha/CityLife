package com.fourkkm.citylife.util;

import java.util.Date;
import java.util.StringTokenizer;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.fourkkm.citylife.R;
import com.zj.app.utils.DateFormatMethod;

public class CommonUtil {

	public static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_MONTH = "yyyy-MM";
	public static final String FORMAT_DAY = "yyyy-MM-dd";

	private static final double PI = Math.PI;
	private static final double EARTH_RADIUS = 6378137.0;

	/**
	 * 根据得分获取评价（10分满分）
	 * 
	 * @param ctx
	 * @param score
	 * @return
	 */
	public static String getStringByScore(Context ctx, float score) {
		if (score <= 2) {
			return ctx.getString(R.string.review_bad);
		} else if (score >= 2 && score < 4) {
			return ctx.getString(R.string.review_middle);
		} else if (score >= 4 && score < 6) {
			return ctx.getString(R.string.review_good);
		} else if (score >= 6 && score < 8) {
			return ctx.getString(R.string.review_very_good);
		} else if (score >= 8) {
			return ctx.getString(R.string.review_best);
		}
		return "";
	}

	public static String getTimeByPHP(int time) {
		long javaTime = time * 1000L;
		return DateFormatMethod.formatDate(new Date(javaTime));
	}

	public static String getTimeByPHP(int time, String format) {
		long javaTime = time * 1000L;
		return DateFormatMethod.formatDate(new Date(javaTime), format);
	}

	public static long getCurrTimeByPHP() {
		return (int) (System.currentTimeMillis() / 1000);
	}

	public static long formatTimeByPHP(String time) {
		if (TextUtils.isEmpty(time)) {
			return 0;
		}
		long jTime = DateFormatMethod.parseDate(time, FORMAT_MINUTE);
		if (jTime == 0) {
			return 0;
		}
		return jTime / 1000;
	}

	public static boolean isEmail(String email) {
		String format = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
		if (email.matches(format)) {
			// 邮箱名合法,返回true
			return true;
		} else {
			// 邮箱名不合法,返回false
			return false;
		}
	}

	/**
	 * 获取某个经纬度多少米以内的最大、最小两个经纬度坐标
	 * 
	 * @param raidus
	 *            单位米
	 * @return minLat,minLng,maxLat,maxLng
	 */

	public static double[] getAround(double lat, double lon, int raidus) {
		Double latitude = lat;
		Double longitude = lon;
		Double degree = (24901 * 1609) / 360.0;
		double raidusMile = raidus;
		Double dpmLat = 1 / degree;
		Double radiusLat = dpmLat * raidusMile;
		Double minLat = latitude - radiusLat;
		Double maxLat = latitude + radiusLat;
		Double mpdLng = degree * Math.cos(latitude * (PI / 180));
		Double dpmLng = 1 / mpdLng;
		Double radiusLng = dpmLng * raidusMile;
		Double minLng = longitude - radiusLng;
		Double maxLng = longitude + radiusLng;
		return new double[] { minLat, minLng, maxLat, maxLng };

	}

	/**
	 * 获取两点之间距离（单位为m）
	 * 
	 * @param longitude1
	 * @param latitude1
	 * @param longitude2
	 * @param latitude2
	 * @return
	 */
	public static double getDistance(double longitude1, double latitude1,
			double longitude2, double latitude2) {
		double Lat1 = rad(latitude1);
		double Lat2 = rad(latitude2);
		double a = Lat1 - Lat2;
		double b = rad(longitude1) - rad(longitude2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(Lat1) * Math.cos(Lat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	public static String formatHtml(String data) {
		if (TextUtils.isEmpty(data)) {
			return "";
		}
		data = data.replaceAll("&amp;", "");
		data = data.replaceAll("quot;", "\"");
		data = data.replaceAll("lt;", "<");
		data = data.replaceAll("gt;", ">");
		return data;
	}

	public static String formatHtml2(String data) {
		if (TextUtils.isEmpty(data)) {
			return "";
		}
		data = data.replaceAll("<br/>", "\n");
		data = data.replaceAll("<br>", "\n");
		data = data.replaceAll("<br />", "\n");
		return data;
	}

	/**
	 * 获取手机ip
	 * 
	 * @param context
	 *            上下文
	 * @return 可用的ip
	 * 
	 */
	public static String getLocalIPAddress(Context context) {
		// try {
		// for (Enumeration<NetworkInterface> mEnumeration = NetworkInterface
		// .getNetworkInterfaces(); mEnumeration.hasMoreElements();) {
		// NetworkInterface intf = mEnumeration.nextElement();
		// for (Enumeration<InetAddress> enumIPAddr = intf
		// .getInetAddresses(); enumIPAddr.hasMoreElements();) {
		// InetAddress inetAddress = enumIPAddr.nextElement();
		// // 如果不是回环地址
		// if (!inetAddress.isLoopbackAddress()) {
		// // 直接返回本地IP地址
		// int i = Integer.parseInt(inetAddress.getHostAddress());
		// String ipStr=(i & 0xFF)+"."+((i>>8) & 0xFF)+"."+
		// ((i>>16) & 0xFF)+"."+(i>>24 & 0xFF);
		// return ipStr;
		// }
		// }
		// }
		// } catch (SocketException ex) {
		// Log.e("Error", ex.toString());
		// }
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		int i = info.getIpAddress();
		String ipStr = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
				+ ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		return ipStr;
	}

	/**
	 * 替换\n为<br/>
	 * 
	 * @param content
	 * @return
	 */
	public static String replaceNewLine(String content) {
		if (TextUtils.isEmpty(content)) {
			return "";
		}
		StringBuilder result = new StringBuilder();
		StringTokenizer t = new StringTokenizer(content, "\n");

		while (t.hasMoreTokens()) {
			result.append(t.nextToken().trim()).append("<br/>");
		}
		return result.toString();
	}

}
