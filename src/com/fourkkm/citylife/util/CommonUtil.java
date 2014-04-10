package com.fourkkm.citylife.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
	 * 鏍规嵁寰楀垎鑾峰彇璇勪环锛�0鍒嗘弧鍒嗭級
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
		return (System.currentTimeMillis() / 1000);
	}
	
	public static long getAskExpireTimeByPHP() {
		return (System.currentTimeMillis() / 1000)+7*24*60*60;
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
		if (TextUtils.isEmpty(email)) {
			return false;
		}
		String format = "^[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";
		if (email.matches(format)) {
			// 閭鍚嶅悎娉�杩斿洖true
			return true;
		} else {
			// 閭鍚嶄笉鍚堟硶,杩斿洖false
			return false;
		}
	}

	/**
	 * 鑾峰彇鏌愪釜缁忕含搴﹀灏戠背浠ュ唴鐨勬渶澶с�鏈�皬涓や釜缁忕含搴﹀潗鏍�
	 * 
	 * @param raidus
	 *            鍗曚綅绫�
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
	 * 鑾峰彇涓ょ偣涔嬮棿璺濈锛堝崟浣嶄负m锛�
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

	public static String formatUnitM(double unitM) {
		if (unitM > 1000) {
			double temp = unitM / 1000;
			DecimalFormat df1 = new DecimalFormat("###.0");
			return df1.format(temp) + "km";
		} else {
			DecimalFormat df2 = new DecimalFormat("###");
			return df2.format(unitM) + "m";
		}
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
	 * 鑾峰彇鎵嬫満ip
	 * 
	 * @param context
	 *            涓婁笅鏂�
	 * @return 鍙敤鐨刬p
	 * 
	 */
	public static String getLocalIPAddress(Context context) {
		WifiManager wifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiManager.getConnectionInfo();
		int i = info.getIpAddress();
		String ipStr = (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
				+ ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
		return ipStr;
	}

	/**
	 * 鏇挎崲\n涓�br/>
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

	public static String transferHtmlToJava(String temp) {
		if (TextUtils.isEmpty(temp)) {
			return "";
		}
		temp = temp.replaceAll("&amp;", "&");
		temp = temp.replaceAll("&#039;", "'");
		temp = temp.replaceAll("&quot;", "\"");
		temp = temp.replaceAll("&lt;", "<");
		temp = temp.replaceAll("&gt;", ">");
		temp = temp.replaceAll("&quot;", ";");
		temp = temp.replaceAll("&reg;", "®");
		temp = temp.replaceAll("&copy", "©");
		temp = temp.replaceAll("&trade;", "™");
		temp = temp.replaceAll("&bull;", ".");
		temp = temp.replaceAll("&nbsp;", " ");
		temp = temp.replaceAll("&laquo;", "<<");
		temp = temp.replaceAll("&raquo;", ">>");
		temp = temp.replaceAll("&#8206;", "");
		return temp;
	}

	public static String transferHtml(String temp) {
		if (TextUtils.isEmpty(temp)) {
			return "";
		}
		temp = temp.replaceAll("<.+?>", "");
		temp = temp.replaceAll("&.+?;", "");
		temp = temp.replaceAll("/n", "");
		temp = temp.replaceAll("/r", "");
		return temp;
	}

	public static List<String> getSubjectAttrIconList(String attr1, String attr2) {
		List<String> tempList = new ArrayList<String>();
		if (!TextUtils.isEmpty(attr1)) {
			String[] temp1 = attr1.split(",");
			for (String str : temp1) {
				tempList.add(str);
			}
		}
		if (!TextUtils.isEmpty(attr2)) {
			String[] temp = attr2.split(",");
			for (String str : temp) {
				tempList.add(str);
			}
		}
		return tempList;
	}
}
