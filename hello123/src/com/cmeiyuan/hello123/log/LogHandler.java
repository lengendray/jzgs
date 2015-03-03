package com.cmeiyuan.hello123.log;

public class LogHandler {

	private static final LogHandler handler = new LogHandler();

	public static LogHandler getInstance() {
		return handler;
	}

	public void log(int level, String tag, String message) {
		
	}

}
