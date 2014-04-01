package com.zel.util;

public class StringUtil {
	public static boolean isAlpha(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	public static boolean isDigital(char c) {
		return (c >= '0' && c <= '9');
	}

	public static boolean isAllDigital(String str) {
		int length = str.length();
		for (int i = 0; i < length; i++) {
			if (!isDigital(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAllAlpha(String str) {
		int length = str.length();
		for (int i = 0; i < length; i++) {
			if (!isAlpha(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static boolean isBlank(String content) {
		if (content == null || content.trim().length() == 0) {
			return true;
		}
		return false;
	}

}
