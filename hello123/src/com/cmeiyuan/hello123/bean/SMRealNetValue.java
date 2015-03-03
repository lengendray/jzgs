package com.cmeiyuan.hello123.bean;

import java.util.Date;

public class SMRealNetValue extends RealEstimateNetValue{

	
	private String fundCode;
	
	@Override
	public String getFundCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getNetValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getGrowPercent() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Date getApplyDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	@Override
	public double getGrowValue() {
		return 0;
	}

}
