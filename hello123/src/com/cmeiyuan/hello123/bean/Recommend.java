package com.cmeiyuan.hello123.bean;

import java.util.List;

public class Recommend {

	public List<Item> data;

	public static class Item {
		/**
		 * 活动ID
		 */
		public String id;
		/**
		 * 活动类型 1、下载apk 2、跳转url
		 */
		public String type;
		/**
		 * 活动标题
		 */
		public String title;
		/**
		 * 活动内容
		 */
		public String content;
		/**
		 * 活动链接
		 */
		public String url;
		/**
		 * 开始时间
		 */
		public String startTime;
		/**
		 * 结束时间
		 */
		public String endTime;
		/**
		 * 作者
		 */
		public String author;
	}
}
