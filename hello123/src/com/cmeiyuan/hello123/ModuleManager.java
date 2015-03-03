package com.cmeiyuan.hello123;

import java.util.ArrayList;
import java.util.List;

import com.cmeiyuan.hello123.bean.Module;
import com.cmeiyuan.hello123.fragment.EstimateFragment;
import com.cmeiyuan.hello123.fragment.MoneyFragment;
import com.cmeiyuan.hello123.fragment.TeachFragment;

public class ModuleManager {

	private static final List<Module> list = new ArrayList<Module>();

	public static List<Module> get() {
		return list;
	}

	static {
		list.add(new Module("估值", EstimateFragment.class));
		list.add(new Module("捡钱", MoneyFragment.class));
		list.add(new Module("学堂", TeachFragment.class));
	}

}
