package com.fourkkm.citylife.util;

import com.fourkkm.citylife.R;

import android.content.Context;

public class CommonUtil {

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
}
