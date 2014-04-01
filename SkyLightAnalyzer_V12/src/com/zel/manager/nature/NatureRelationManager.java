package com.zel.manager.nature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import com.zel.entity.nature.NatureItem;
import com.zel.util.StaticValue;
import com.zel.util.StringUtil;
import com.zel.util.io.IOUtil;

/**
 * 词性间关系管理器,如两个词性间的频率
 * 
 * @author zel
 * 
 */
public class NatureRelationManager {

	/**
	 * 词性的字符串对照索引位的hashmap
	 */
	public static final HashMap<String, NatureItem> NatureMap = new HashMap<String, NatureItem>();

	/**
	 * 词与词之间的关系.对照natureARRAY,natureMap
	 */
	public static int[][] NatureRelationFreqTable = null;

	public static final int unknown_index = -1;
	public static final int unknown_nature_index = -1;
	public static final int unknown_nature_freq = 1;

	private static void init() throws IOException {
		// 加载词对照性表
		String natureMapString = IOUtil.readFile(
				"resource/dic/nature/nature.map", StaticValue.System_Encoding);
		StringReader sr = new StringReader(natureMapString);
		BufferedReader reader = new BufferedReader(sr);

		String temp = null;
		String[] strs = null;
		int maxLength = 0;
		int p0 = 0;
		int p1 = 0;
		int p2 = 0;
		while ((temp = reader.readLine()) != null) {
			strs = temp.split(StaticValue.Separator_Tab);
			if (strs.length != 4)
				continue;

			p0 = Integer.parseInt(strs[0]);
			p1 = Integer.parseInt(strs[1]);
			p2 = Integer.parseInt(strs[3]);
			NatureMap.put(strs[2], new NatureItem(strs[2], p0, p1, p2));
			maxLength = Math.max(maxLength, p1);
		}
		reader.close();

		// 加载词性关系
		NatureRelationFreqTable = new int[maxLength + 1][maxLength + 1];

		natureMapString = IOUtil.readFile("resource/dic/nature/nature.table",
				StaticValue.System_Encoding);
		sr = new StringReader(natureMapString);
		reader = new BufferedReader(sr);

		int j = 0;
		while ((temp = reader.readLine()) != null) {
			if (StringUtil.isBlank(temp))
				continue;
			strs = temp.split(StaticValue.Separator_Tab);
			for (int i = 0; i < strs.length; i++) {
				NatureRelationFreqTable[j][i] = Integer.parseInt(strs[i]);
			}
			j++;
		}
		reader.close();
	}

	public static int getTwoNatureRelationFreq(NatureItem fromNature,
			NatureItem toNature) {
		if (fromNature.getIndex() < 0 || toNature.getIndex() < 0) {
			return 0;
		}
		return NatureRelationFreqTable[fromNature.getIndex()][toNature
				.getIndex()];
	}

	/**
	 * 根据词性名称得到一个对应的词性条目对象，没有则重新创建一个
	 */
	public static NatureItem getNatureItem(String natureStr) {
		NatureItem natureItem = NatureMap.get(natureStr);
		if (natureItem == null) {
			natureItem = new NatureItem(natureStr, unknown_index,
					unknown_nature_index, unknown_nature_freq);
			NatureMap.put(natureStr, natureItem);
			return natureItem;
		}
		return natureItem;
	}

	public static void main(String[] args) throws Exception {
		NatureRelationManager.init();

		NatureItem n1 = new NatureItem("a", 2, 2, 1);
		NatureItem n2 = new NatureItem("d", 9, 9, 1);

		int freq = NatureRelationManager.getTwoNatureRelationFreq(n1, n2);

		System.out.println(freq);
	}
}
