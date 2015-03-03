package com.cmeiyuan.hello123.bean;

import java.io.Serializable;

import com.cmeiyuan.hello123.bean.v2.Estimate;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FundHold implements Serializable {

	private static final long serialVersionUID = 1L;
	// 基金代码
	@Expose
	@SerializedName("fundCode")
	public String fundCode;
	// 基金名称
	@Expose
	@SerializedName("fundName")
	public String fundName;
	// 基金类型
	@Expose
	@SerializedName("fundType")
	public String fundType;
	// 基金份额
	@Expose
	@SerializedName("fundShare")
    public String fundShare;
    // 净值估算
    // public EstimateNetValue estimateNetValue;
    // 净值估算
    public Estimate estimate;

	@Override
	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create().toJson(this);
	}

	public float getFundShare() {
		float result = 0;
		try {
			result = Float.parseFloat(fundShare);
		} catch (Exception e) {
		}
		return result;
	}

}
