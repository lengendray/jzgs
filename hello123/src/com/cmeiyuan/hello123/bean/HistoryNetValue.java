package com.cmeiyuan.hello123.bean;

import java.util.ArrayList;
import java.util.List;

public class HistoryNetValue {

	private String[] jzdata;
	private String fundCode;

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public List<NetValue> getNetValues() {
		List<NetValue> list = null;
		try {
			if (jzdata != null) {
				list = new ArrayList<NetValue>();
				for (int i = 0; i < jzdata.length; i++) {
					String[] value = jzdata[i].split(",");
					NetValue netValue = new NetValue();
					netValue.setFundCode(getFundCode());
					netValue.setApplyDate(value[0]);
					netValue.setNetValue(value[1]);
					netValue.setGrowPercent(value[3]);
					list.add(netValue);
				}
			}
		} catch (Exception e) {

		}
		return list;
	}

}
