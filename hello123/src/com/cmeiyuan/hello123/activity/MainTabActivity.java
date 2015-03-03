package com.cmeiyuan.hello123.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmeiyuan.hello123.ModuleManager;
import com.cmeiyuan.hello123.R;
import com.cmeiyuan.hello123.bean.Module;

public class MainTabActivity extends AnalysisActivity implements
		OnClickListener {

	protected static final String TAG = "MainTabActivity";

	private int curModule = 0;
	private SparseArray<Fragment> fragmentCache = new SparseArray<Fragment>();
	private LinearLayout ll_navigation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maintab);
		ll_navigation = (LinearLayout) findViewById(R.id.ll_navigation);
		loadNavigation();
		loadContent();
	}

	private void loadNavigation() {
		ll_navigation.removeAllViews();
		List<Module> list = ModuleManager.get();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -2);
		lp.weight = 1;
		for (int i = 0; i < list.size(); i++) {
			Module module = list.get(i);
			TextView textView = new TextView(this);
			textView.setTag(i);
			textView.setGravity(Gravity.CENTER);
			textView.setText(module.getName());
			textView.setBackgroundResource(0);
			ll_navigation.addView(textView, lp);
		}
	}

	private void loadContent() {
		try {
			List<Module> list = ModuleManager.get();
			Module module = list.get(curModule);
			Fragment fragment = fragmentCache.get(curModule);
			if (fragment == null) {
				fragment = module.getFragment().newInstance();
				fragmentCache.put(curModule, fragment);
			} else {
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.ll_content, fragment)
						.commitAllowingStateLoss();
			}
		} catch (Exception e) {
			showToast("加载模块失败");
		}
	}

	@Override
	public void onClick(View v) {
		curModule = (Integer) v.getTag();
		loadContent();
	}
}
