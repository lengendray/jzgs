package com.cmeiyuan.hello123.api;

import java.util.HashMap;

import com.cmeiyuan.hello123.bean.HistoryNetValue;

public class HistoryNetValueApi extends BaseApi<HistoryNetValue> {

	/**
	 * 一月
	 */
	public static final String MONTH = "y";
	/**
	 * 季度
	 */
	public static final String SEASON = "3y";
	/**
	 * 半年
	 */
	public static final String HALF = "6y";
	/**
	 * 一年
	 */
	public static final String YEAR = "n";
	private static final String URL = "http://fundex2.eastmoney.com/FundWebServices/FundDataForMobile.aspx";

	@Override
	protected String getUrl() {
		return URL;
	}

	public void get(String fundCode, String range) {
		HashMap<String, String> param = new HashMap<String, String>();
		param.put("t", "dwjznew");
		param.put("fc", fundCode);
		param.put("rg", range);
		param.put("rk", "3y");
		get(param);
	}
}
