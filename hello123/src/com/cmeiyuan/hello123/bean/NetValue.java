package com.cmeiyuan.hello123.bean;

import java.io.Serializable;
import java.util.Date;

import com.cmeiyuan.hello123.util.DataUtil;
import com.cmeiyuan.hello123.util.DateHelper;

public class NetValue implements INetValue, Serializable {

	private static final long serialVersionUID = 1L;

	private String fundCode;
	private String netValue;
	private String growValue;
	private String growPercent;
	private String applyDate;

	public void setNetValue(String netValue) {
		this.netValue = netValue;
	}

	public void setGrowValue(String growValue) {
		this.growValue = growValue;
	}

	public void setGrowPercent(String growPercent) {
		this.growPercent = growPercent;
	}

	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}

	@Override
	public void setFundCode(String fundCode) {

	}

	@Override
	public String getFundCode() {
		return fundCode;
	}

	@Override
	public double getNetValue() {
		return DataUtil.parse(netValue);
	}

	@Override
	public double getGrowValue() {
		return DataUtil.parse(growValue);
	}

	@Override
	public double getGrowPercent() {
		return DataUtil.parse(growPercent);
	}

	@Override
	public Date getApplyDate() {
		return DateHelper.getInstance().getDate(applyDate);
	}

}
