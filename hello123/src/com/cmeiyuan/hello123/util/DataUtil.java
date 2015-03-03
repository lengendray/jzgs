package com.cmeiyuan.hello123.util;

import com.cmeiyuan.hello123.log.Logger;

public class DataUtil {

	public static final String TAG = "DataUtil";

	public static double parse(String text) {
		double result = 0;
		try {
			result = Double.parseDouble(text);
		} catch (Exception e) {
			Logger.e(TAG, e.getMessage());
		}
		return result;
	}

}
