package com.cmeiyuan.hello123.bean;

import java.util.Date;

import com.cmeiyuan.hello123.util.DateHelper;

public class TTEstimateNetValue extends EstimateNetValue {

	private static final long serialVersionUID = 1004L;

	private String fundCode;

	// 估算净值数据
	private String[] gzdata;
	private String gz;
	// 估算净值日期2014-11-12
	private String gztime;
	// 单位净值
	private String dwjz;

	// 真实净值日期2014-11-12
	// private String jzrq;

	@Override
	public String getFundCode() {
		return fundCode;
	}

	@Override
	public double getNetValue() {
		double value = 0;
		try {
			value = Double.parseDouble(gz);
		} catch (Exception e) {
		}
		return value;
	}

	@Override
	public double getGrowPercent() {
		double value = 0;
		try {
			double estimateValue = getNetValue();
			double lastNetValue = Double.parseDouble(dwjz);
			if (lastNetValue != 0) {
				value = (estimateValue - lastNetValue) / lastNetValue;
			}
		} catch (Exception e) {
		}
		return value;
	}

	@Override
	public Date getApplyDate() {
		Date date = null;
		try {
			if (gzdata != null && gzdata.length > 0) {
				String str = gzdata[gzdata.length - 1];
				if (str != null) {
					String[] array = str.split(",");
					if (array != null && array.length == 3) {
						date = DateHelper.getInstance().getDate(
								gztime + " " + array[1]);
					}
				}
			}

		} catch (Exception e) {
		}
		return date;
	}

	@Override
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	@Override
	public double getGrowValue() {
		double value = 0;
		try {
			double estimateValue = getNetValue();
			double lastNetValue = Double.parseDouble(dwjz);
			value = estimateValue - lastNetValue;
		} catch (Exception e) {
		}
		return value;
	}

}
