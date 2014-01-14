package com.fourkkm.citylife.util;

import java.io.UnsupportedEncodingException;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * ����utf-8���ַ����������ַ��ĳ��ȡ� �Ķ�����������û�ɬ�����Ծ��ο�androidԴ��InputFilter��ʵ�֡�
 */
public class UTF8LengthFilter implements InputFilter {

	public UTF8LengthFilter(int max) {
		mMax = max;
	}

	/*
	 * public CharSequence filter(CharSequence source, int start, int end,
	 * Spanned dest, int dstart, int dend) { int keep = 0; try { keep = mMax -
	 * (dest.toString().getBytes("UTF-8").length - (dend - dstart)); //
	 * System.out.println(keep); if (keep <= 0) {// ʣ����ַ�û�� return ""; } else {
	 * // ʣ���ַ�����keep�������ǲ������û�����keep������Ϊ1�������൱��3���ַ��� int send = end - start; send
	 * = send > keep ? keep : send; CharSequence s = source.subSequence(start,
	 * start + send); // �ݼ��Ƚϣ�ֱ���ַ���С�ڵ���keep���� while
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
			if (keep <= 0) {// ʣ����ַ�û��
				return "";
			} else {
				// ʣ���ַ�����keep�������ǲ������û�����keep������Ϊ1�������൱��3���ַ���
				int send = end - start;
				CharSequence s = source;
				// �ݼ��Ƚϣ�ֱ���ַ���С�ڵ���keep����
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
