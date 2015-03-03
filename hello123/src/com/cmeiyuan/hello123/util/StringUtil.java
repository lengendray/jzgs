package com.cmeiyuan.hello123.util;

public class StringUtil {

	public static boolean isEmpty(String text) {
		return text == null || text.length() == 0;
	}

	public static boolean isNotEmpty(String text) {
		return !isEmpty(text);
	}
}
