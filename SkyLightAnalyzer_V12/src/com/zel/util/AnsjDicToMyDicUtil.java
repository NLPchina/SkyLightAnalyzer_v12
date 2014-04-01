package com.zel.util;

import java.io.BufferedReader;
import java.io.StringReader;

import com.zel.util.io.IOUtil;

/**
 * 主要是将ansj分词中的arrays.dic词典转化成SkyLightAnalyzer分词所需要的词典格式
 * 
 * @author zel
 * 
 */
public class AnsjDicToMyDicUtil {
	public static String Tab_Split_Tag = "	";

	// 转换成3列的形式
	public static void transDicToThreeColoums(String sourceDicPath,
			String descDicPath) throws Exception {
		String dicStr = IOUtil.readFile(sourceDicPath, "UTF-8");

		StringReader sr = new StringReader(dicStr);
		BufferedReader br = new BufferedReader(sr);

		StringBuilder sb = new StringBuilder();

		String temp_line = null;
		String[] split_array = null;
		while ((temp_line = br.readLine()) != null) {
			split_array = temp_line.split(Tab_Split_Tag);
			if (split_array.length == 6) {// 保证每条信息的完整性
				if (!split_array[4].equals("1")) {// 只要可成词，1代表不是词,也非词的过滤
					sb.append(split_array[1] + Tab_Split_Tag + split_array[4]
							+ Tab_Split_Tag + split_array[5]);
					sb.append("\n");
				}
			}
		}
		br.close();

		IOUtil.writeFile(descDicPath, sb.toString());
	}

	// 转换成2列的形式
	public static void transDicToTwoColoums(String sourceDicPath,
			String descDicPath) throws Exception {
		String dicStr = IOUtil.readFile(sourceDicPath, "UTF-8");

		StringReader sr = new StringReader(dicStr);
		BufferedReader br = new BufferedReader(sr);

		StringBuilder sb = new StringBuilder();

		String temp_line = null;
		String[] split_array = null;
		while ((temp_line = br.readLine()) != null) {
			split_array = temp_line.split(Tab_Split_Tag);
			if (split_array.length == 6) {// 保证每条信息的完整性
				if (!split_array[4].equals("1")) {// 只要可成词，1代表不是词,也非词的过滤
					sb.append(split_array[1] + Tab_Split_Tag + split_array[5]);
					sb.append("\n");
				}
			}
		}
		br.close();

		IOUtil.writeFile(descDicPath, sb.toString());
	}

	public static void main(String[] args) throws Exception {
		String sourceDicPath = "resource/dic/system/arrays.dic";
		String descDicPath = "resource/dic/system/arrays_new.dic";
//		transDicToThreeColoums(sourceDicPath, descDicPath);
		transDicToTwoColoums(sourceDicPath, descDicPath);

		System.out.println("完成");
	}

}
