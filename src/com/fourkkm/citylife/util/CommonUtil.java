package com.fourkkm.citylife.util;

import java.util.Date;

import com.fourkkm.citylife.R;
import com.zj.app.utils.DateFormatMethod;

import android.content.Context;

public class CommonUtil {

	public static final String FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_MINUTE = "yyyy-MM-dd HH:mm";
	public static final String FORMAT_MONTH = "yyyy-MM";
	public static final String FORMAT_DAY = "yyyy-MM-dd";

	/**
	 * 根据得分获取评价（10分满分）
	 * 
	 * @param ctx
	 * @param score
	 * @return
	 */
	public static String getStringByScore(Context ctx, int score) {
		if (score >= 0 && score < 2) {// 差评
			return ctx.getString(R.string.review_bad);
		} else if (score >= 2 && score < 4) {
			return ctx.getString(R.string.review_good);
		} else if (score >= 4 && score < 6) {
			return ctx.getString(R.string.review_best);
		} else if (score >= 6 && score < 8) {
			return ctx.getString(R.string.review_very_good);
		} else if (score >= 8 && score < 10) {
			return ctx.getString(R.string.review_very_very_good);
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

	public static int getCurrTimeByPHP() {
		return (int) (System.currentTimeMillis() / 1000);
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
}
