package com.cmeiyuan.hello123.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.support.v4.util.LruCache;
import android.text.TextUtils;

public class DateHelper {

	private static DateHelper instance;
	private static Locale DEFAULT_LOCALE = Locale.CHINA;
	// 星期
	private final String WeekDays[] = new String[] { "周一", "周二", "周三", "周四",
			"周五", "周六", "周日" };
	private final LruCache<String, SimpleDateFormat> cache;
	private final List<String> templates = new ArrayList<String>() {

		private static final long serialVersionUID = 1533660831138836937L;
		{
			add("yyyy-MM-dd'T'HH:mm:ss.SSS");
			add("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			add("yyyy-MM-dd'T'HH:mm:ss");
			add("yyyy-MM-dd'T'HH:mm");
			add("yyyyMMdd HHmmss");
			add("yyyyMMdd");
			add("yyyy-MM-dd HH:mm:ss.SSS");
			add("yyyy-MM-dd HH:mm:ss");
			add("yyyy-MM-dd HH:mm");
			add("yyyy-MM-dd");
			add("MM-dd HH:mm");
			add("yyyy/MM/dd");
		}
	};

	private DateHelper() {
		cache = new LruCache<String, SimpleDateFormat>(20);
	}

	public synchronized static DateHelper getInstance() {
		if (instance == null) {
			instance = new DateHelper();
		}
		return instance;
	}

	public Date getDate(String text) {
		for (String template : templates) {
			Date date = getDate(text, template);
			if (date != null) {
				return date;
			}
		}
		return null;
	}

	public Date getDate(String text, String template) {
		return getDate(text, template, DEFAULT_LOCALE);
	}

	public Date getDate(Date text, String template) {
		return getDate(getStringDate(text, template), template, DEFAULT_LOCALE);
	}

	public Date getDate(String text, String template, Locale locale) {
		if (TextUtils.isEmpty(text)) {
			return null;
		}
		try {
			return getSimpleDateFormat(template, locale).parse(text);
		} catch (ParseException e) {
			return null;
		}
	}

	protected SimpleDateFormat getSimpleDateFormat(String template,
			Locale locale) {
		if (TextUtils.isEmpty(template)) {
			throw new IllegalArgumentException("template is empty.");
		}
		String key = getKey(template, locale);
		SimpleDateFormat format = cache.get(key);
		if (format == null) {
			format = new SimpleDateFormat(template, locale);
			cache.put(key, format);
		}
		return format;
	}

	protected String getKey(String template, Locale locale) {
		return template + "_" + locale.toString();
	}

	/**
	 * 获取中文星期
	 * 
	 * @param day
	 *            星期:1-7
	 * @return String
	 */
	public String toWeekDay(int day) {
		String dateStr = Integer.toString(day);
		try {
			dateStr = WeekDays[day - 1];
		} catch (Exception e) {

		}
		return dateStr;
	}

	/**
	 * 以一种格式来比较时间
	 * 
	 * @param date1
	 * @param date2
	 * @param format
	 * @return
	 */
	public int compareTo(Date date1, Date date2, String format) {
		String text1 = getSimpleDateFormat(format, DEFAULT_LOCALE)
				.format(date1);
		date1 = getDate(text1, format, DEFAULT_LOCALE);

		String text2 = getSimpleDateFormat(format, DEFAULT_LOCALE)
				.format(date2);
		date2 = getDate(text2, format, DEFAULT_LOCALE);
		if (date1 == null && date2 != null) {
			return -1;
		} else if (date1 != null && date2 == null) {
			return 1;
		} else if (date1 == null && date2 == null) {
			return 0;
		} else {
			return date1.compareTo(date2);
		}
	}

	public boolean isSameDay(Date date1, Date date2) {
		return compareTo(date1, date2, "yyyy-MM-dd") == 0;
	}

	/**
	 * 格式化字符串时间
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            时间格式
	 * @return 字符串时间
	 */
	public String getStringDate(String date, String format) {
		return getStringDate(getDate(date), format);
	}

	/**
	 * 
	 * @param date
	 *            时间
	 * @param format
	 *            时间格式
	 * @return 字符串时间
	 */
	public String getStringDate(Date date, String format) {
		return getSimpleDateFormat(format, DEFAULT_LOCALE).format(date);
	}

}
