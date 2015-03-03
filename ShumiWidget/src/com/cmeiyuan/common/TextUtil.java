package com.cmeiyuan.common;

import android.graphics.Paint;
import android.graphics.Rect;

public class TextUtil {

	private static final Rect mRect = new Rect();

	public static int getTextWidth(Paint paint, String text) {
		paint.getTextBounds(text, 0, text.length(), mRect);
		return mRect.width();
	}

	public static int getTextHeight(Paint paint, String text) {
		paint.getTextBounds(text, 0, text.length(), mRect);
		return mRect.height();
	}

}
