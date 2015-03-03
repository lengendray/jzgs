package com.cmeiyuan.hello123.api;

import java.util.HashMap;
import java.util.Map;

import com.cmeiyuan.hello123.bean.TTEstimateNetValue;

public class TTEstimateApi extends EstimateApi<TTEstimateNetValue> {

	private static final String URL = "http://fundex2.eastmoney.com/FundWebServices/FundDataForMobile.aspx";

	public void get(String fundCode) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("fc", fundCode);
		param.put("t", "gz");
		param.put("rg", "y");
		param.put("rk", "3y");
		get(param);
	}

	@Override
	protected String getUrl() {
		return URL;
	}

}
