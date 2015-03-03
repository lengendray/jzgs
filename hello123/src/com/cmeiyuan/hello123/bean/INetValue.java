package com.cmeiyuan.hello123.bean;

import java.util.Date;

public interface INetValue {

	public void setFundCode(String fundCode);

	public String getFundCode();

	public double getNetValue();

	public double getGrowValue();
	
	public double getGrowPercent();
	
	public Date getApplyDate();

}
