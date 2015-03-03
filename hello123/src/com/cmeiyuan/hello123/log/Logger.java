package com.cmeiyuan.hello123.log;

public class Logger {

	public static final int INFO = 1;
	public static final int DEBUG = 2;
	public static final int WARN = 3;
	public static final int ERROR = 4;

	private static final LogHandler handler = LogHandler.getInstance();

	/**
	 * 普通信息
	 */
	public static void i(String tag, String message) {
		handler.log(INFO, tag, message);
	}

	/**
	 * 调试信息
	 */
	public static void d(String tag, String message) {
		handler.log(DEBUG, tag, message);
	}

	/**
	 * 警告信息
	 */
	public static void w(String tag, String message) {
		handler.log(WARN, tag, message);
	}

	/**
	 * 错误信息
	 */
	public static void e(String tag, String message) {
		handler.log(ERROR, tag, message);
	}

}
