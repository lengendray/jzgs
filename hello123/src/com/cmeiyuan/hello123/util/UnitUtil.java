package com.cmeiyuan.hello123.util;

import android.content.Context;
import android.util.TypedValue;

public class UnitUtil {

	public static int dp2px(Context context, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				context.getResources().getDisplayMetrics());
	}

}
