package com.cmeiyuan.hello123.api;

import com.cmeiyuan.hello123.bean.EstimateNetValue;

public abstract class EstimateApi<T extends EstimateNetValue> extends
		BaseApi<T> {

	public abstract void get(String fundCode);

}
