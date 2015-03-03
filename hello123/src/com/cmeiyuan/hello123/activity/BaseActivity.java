package com.cmeiyuan.hello123.activity;

import android.widget.Toast;

public class BaseActivity extends TopBarActivity {

	protected void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
	

}
