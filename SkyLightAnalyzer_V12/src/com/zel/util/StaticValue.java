package com.zel.util;

import java.util.HashSet;
import java.util.Set;

/**
 * 存储系统常用参数
 * 
 * @author zel
 */
public class StaticValue {
	public static String TermStart = "始##始";
	public static String TermEnd = "末##末";

	public static String System_Encoding = "UTF-8";

	public static String Separator_Whitespace = " ";
	public static String Separator_Tab = "	";
	public static String Separator_At = "@";

	/**
	 * 对合并的词性的预定义
	 */
	public static Set<String> pos_can_merge_set_normal = new HashSet<String>();
	static {
		String[] pos_array = { "m", "q", "mq" };
		for (String temp_pos : pos_array) {
			pos_can_merge_set_normal.add(temp_pos);
		}
	}

	public static Set<String> pos_can_merge_value_set_special = new HashSet<String>();
	static {
		// String[] pos_array = { "u", "w", "unknown" };
		String[] pos_array = { "之", ".", "*", "-", "/", "+" };
		for (String temp_pos : pos_array) {
			pos_can_merge_value_set_special.add(temp_pos);
		}
	}

}
