package com.fourkkm.citylife.util;

import java.io.UnsupportedEncodingException;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 按照utf-8的字符控制输入字符的长度。 阅读代码如果觉得晦涩，请自觉参考android源码InputFilter的实现。
 */
public class UTF8LengthFilter implements InputFilter {

	public UTF8LengthFilter(int max) {
		mMax = max;
	}

	/*
	 * public CharSequence filter(CharSequence source, int start, int end,
	 * Spanned dest, int dstart, int dend) { int keep = 0; try { keep = mMax -
	 * (dest.toString().getBytes("UTF-8").length - (dend - dstart)); //
	 * System.out.println(keep); if (keep <= 0) {// 剩余的字符没了 return ""; } else {
	 * // 剩余字符数是keep个，但是不能让用户输入keep个，因为1个汉字相当于3个字符。 int send = end - start; send
	 * = send > keep ? keep : send; CharSequence s = source.subSequence(start,
	 * start + send); // 递减比较，直到字符数小于等于keep个。 while
	 * (s.toString().getBytes("UTF-8").length > keep) { send--; s =
	 * source.subSequence(start, start + send).toString(); } return s; } } catch
	 * (UnsupportedEncodingException e) { e.printStackTrace(); } return source;
	 * }
	 */
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		int keep = 0;
		try {
			keep = mMax
					- (dest.toString().getBytes("UTF-8").length - (dend - dstart));
			// System.out.println(keep);
			if (keep <= 0) {// 剩余的字符没了
				return "";
			} else {
				// 剩余字符数是keep个，但是不能让用户输入keep个，因为1个汉字相当于3个字符。
				int send = end - start;
				CharSequence s = source;
				// 递减比较，直到字符数小于等于keep个。
				while (s.toString().getBytes("UTF-8").length > keep) {
					send--;
					s = source.subSequence(start, start + send).toString();
				}
				return s;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return source;
	}

	private int mMax;
}
