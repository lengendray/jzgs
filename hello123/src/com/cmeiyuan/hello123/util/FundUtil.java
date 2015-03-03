package com.cmeiyuan.hello123.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cmeiyuan.hello123.bean.FundHold;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class FundUtil {

	private static final String FUND_LIST = "fund_list";
	private static final String APP_CONFIG = "app_config";

	public static void saveFund(Context context, FundHold fund) {
		SharedPreferences preferences = context.getSharedPreferences(FUND_LIST,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		String serial = fund.toString();
		editor.putString(fund.fundCode, serial);
		editor.commit();
	}

	public static void deleteFund(Context context, FundHold fund) {
		SharedPreferences preferences = context.getSharedPreferences(FUND_LIST,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.remove(fund.fundCode);
		editor.commit();
	}

	public static List<FundHold> getFunds(Context context) {
		List<FundHold> list = new ArrayList<FundHold>();
		SharedPreferences preferences = context.getSharedPreferences(FUND_LIST,
				Context.MODE_PRIVATE);
		Map<String, ?> map = preferences.getAll();
		Set<String> keySet = map.keySet();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		for (String key : keySet) {
			list.add(gson.fromJson((String) map.get(key), FundHold.class));
		}
		return list;
	}

	public static void saveInterval(Context context, long time) {
		SharedPreferences preferences = context.getSharedPreferences(
				APP_CONFIG, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putLong("interval", time);
		editor.commit();
	}

	public static long getInterval(Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				APP_CONFIG, Context.MODE_PRIVATE);
		return preferences.getLong("interval", 30000);
	}
}
