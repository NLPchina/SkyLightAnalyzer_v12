package com.zel.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.zel.util.StaticValue;
import com.zel.util.SystemParas;
import com.zel.util.io.IOUtil;

/**
 * 简繁转换器
 * 
 * @author zel
 * 
 */
public class ComplexToSimpleManager {
	public static char[] complexToSimpleArray = new char[65536];

	// private static char[] simpleToComplexArray = new char[65536];

	// 初始化管理器
	public static void init() {
		String src = IOUtil.readFile(SystemParas.dic_system_translation_path,
				StaticValue.System_Encoding);
		BufferedReader br = new BufferedReader(new StringReader(src));
		String line = null;

		try {
			String[] charArray = null;
			int array_length = 0;
			int index_count;
			while ((line = br.readLine()) != null) {
				if (line != null && line.trim().length() > 0) {
					// charArray = line.split(StaticValue.Separator_Whitespace);
					charArray = line.split(StaticValue.Separator_Tab);
					array_length = charArray.length;
					for (index_count = 1; index_count < array_length; index_count++) {
						// complexToSimpleArray[charArray[index_count].charAt(0)]
						// = charArray[0]
						// .charAt(0);
						complexToSimpleArray[charArray[0].charAt(0)] = charArray[index_count]
								.charAt(0);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getSimpleFont(String src) {
		StringBuilder sb = new StringBuilder();
		int word_len = src.trim().length();
		for (int i = 0; i < word_len; i++) {
			sb.append(convert(src.charAt(i)));
		}
		return sb.toString();
	}

	// 繁体转简体
	public static char convert(char origin) {
		char c = 0;
		if ((c = complexToSimpleArray[origin]) == 0) {
			return origin;
		}
		return c;
	}

	public static void main(String[] args) {
		ComplexToSimpleManager.init();
		// char origin = '左';
		char origin = '嶴';
		System.out.println(ComplexToSimpleManager.convert(origin));
	}
}
