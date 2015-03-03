package com.cmeiyuan.hello123.activity;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cmeiyuan.hello123.R;

public class TopBarActivity extends FragmentActivity {

	private TextView tv_left;
	private TextView tv_right;
	private TextView tv_center;
	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			onTopBarSelected(v);
		}
	};

	@Override
	public void setContentView(int layoutResID) {
		View view = LayoutInflater.from(this).inflate(layoutResID, null);
		initTopBar(view, null);
	}

	@Override
	public void setContentView(View view) {
		initTopBar(view, null);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		initTopBar(view, params);
	}

	private void initTopBar(View view, LayoutParams params) {
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(-1, -1);
		LayoutInflater.from(this).inflate(R.layout.layout_top_bar, ll);
		if (params == null) {
			ll.addView(view, lp);
		} else {
			ll.addView(view, params);
		}
		super.setContentView(ll, lp);
		tv_left = (TextView) findViewById(R.id.btn_left);
		tv_right = (TextView) findViewById(R.id.btn_right);
		tv_center = (TextView) findViewById(R.id.tv_center);

		tv_left.setOnClickListener(listener);
		tv_right.setOnClickListener(listener);
		tv_center.setOnClickListener(listener);
		
		onInitTopBar(tv_left, tv_right, tv_center);
	}

	@Override
	protected void onTitleChanged(CharSequence title, int color) {
		super.onTitleChanged(title, color);
		if (tv_center != null) {
			tv_center.setText(title);
		}
	}

	protected TextView getLeftTextView() {
		return tv_left;
	}

	protected TextView getRightTextView() {
		return tv_right;
	}

	public TextView getCenterTextView() {
		return tv_center;
	}

	protected void onInitTopBar(TextView left, TextView right, TextView center) {

	}

	protected void onTopBarSelected(View v) {

	}

}
