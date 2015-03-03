package com.cmeiyuan.hello123.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class SMEstimateNetValue extends EstimateNetValue {

	private String fundCode;
	private List<Item> datatable;

	public static class Item implements Serializable {
		private static final long serialVersionUID = 3l;
		// 行编号
		public Integer _row_id;
		// 基金代码
		public String code;
		// 净值百分比
		public Double estimate_percent;
		// 净值
		public Double estimate_net_value;
		// 净值时间
		public String estimate_time;
		// 时间编号
		public Integer time_id;
	}

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
