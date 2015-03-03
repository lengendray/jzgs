package com.cmeiyuan.hello123.activity;

import com.umeng.analytics.MobclickAgent;

public class AnalysisActivity extends BaseActivity {

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
