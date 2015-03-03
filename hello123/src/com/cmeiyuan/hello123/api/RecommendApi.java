package com.cmeiyuan.hello123.api;

import com.cmeiyuan.hello123.bean.Recommend;

public class RecommendApi extends BaseApi<Recommend> {

	private static final String URL = "http://bcs.duapp.com/cmeiyuan/activity_recommend";

	@Override
	protected String getUrl() {
		return URL;
	}

}