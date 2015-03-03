package com.cmeiyuan.hello123.api;

import java.util.HashMap;
import java.util.Map;

import com.cmeiyuan.hello123.bean.SMEstimateNetValue;

public class SMEstimateApi extends EstimateApi<SMEstimateNetValue>{

	private static final String URL = "http://funddata.smbserver.fund123.cn/mobile_estimate_by_code";

	public void get(String fundCode) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("format", "json");
		param.put("fund_code", fundCode);
		get(param);
	}

	@Override
	protected String getUrl() {
		return URL;
	}

}
