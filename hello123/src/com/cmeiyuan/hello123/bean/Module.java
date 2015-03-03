package com.cmeiyuan.hello123.bean;

import android.support.v4.app.Fragment;

public class Module {
	private String name;
	private Class<? extends Fragment> cls;

	public Module(String name, Class<? extends Fragment> cls) {
		this.name = name;
		this.cls = null;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends Fragment> getFragment() {
		return cls;
	}
}