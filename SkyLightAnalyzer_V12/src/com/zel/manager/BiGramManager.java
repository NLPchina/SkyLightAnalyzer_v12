package com.zel.manager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import com.zel.entity.BiGramItem;
import com.zel.entity.Forest;
import com.zel.entity.TermUnit;
import com.zel.util.StaticValue;
import com.zel.util.StringUtil;
import com.zel.util.SystemParas;
import com.zel.util.io.IOUtil;
import com.zel.util.log.MyLogger;

/**
 * 查找两个TermUnit之间的词频统计
 * 
 * @author zel
 */
public class BiGramManager {
	// 做日志用
	private static MyLogger logger = new MyLogger(BiGramManager.class);
	// 用来存放词与词之关联的二维矩阵
	public static BiGramItem[][] biGramMatrix = null;
	// 计算关联词之间的id，从而定位
	private static Forest forest = LibraryManager.forest;

	// 调用时先进行矩阵的初始化
	// static {
	// long start = System.currentTimeMillis();
	// init();
	// logger.info("init bigram matrix time :"
	// + (System.currentTimeMillis() - start) + " ms");
	// }

	// 初始化二维矩阵biGramMatrix
	public static void init() {
		// 暂存要初始你给的二维矩阵
		BiGramItem[][] init_biGramMatrix = new BiGramItem[SystemParas.max_branch_array_length][0];

		String biGramStr = IOUtil.readFile(SystemParas.dic_bigram_path,
				StaticValue.System_Encoding);

		StringReader stringReader = new StringReader(biGramStr);
		BufferedReader br = new BufferedReader(stringReader);
		String line = null;
		String[] tabSplitArray = null;

		int fromId = 0;
		int toId = 0;
		int freq = 0;

		try {
			BiGramItem to = null;

			String from_word = null;
			String to_word = null;

			while ((line = br.readLine()) != null) {
				if (StringUtil.isBlank(line)) {
					continue;
				}
				tabSplitArray = line.split(StaticValue.Separator_Tab);
				freq = Integer.parseInt(tabSplitArray[1]);
				tabSplitArray = tabSplitArray[0]
						.split(StaticValue.Separator_At);
				
				from_word = ComplexToSimpleManager
						.getSimpleFont(tabSplitArray[0]);
				to_word = ComplexToSimpleManager
						.getSimpleFont(tabSplitArray[1]);

				if ((fromId = forest.getTermId(from_word)) <= 0) {
					if (StaticValue.TermStart.equals(from_word)) {
						fromId = 0;// 0代表是TermStart开始
					} else {
						fromId = 1;// 代表是未找到对应的词典id
					}
				}
				if ((toId = forest.getTermId(to_word)) <= 0) {
					if (StaticValue.TermEnd.equals(to_word)) {
						toId = 0; // 0代表是TermEnd结束
					} else {
						toId = -1;
					}
				}

				to = new BiGramItem(toId, freq);

				int index = Arrays.binarySearch(init_biGramMatrix[fromId], to);
				if (index > -1) {
					continue;
				} else {
					BiGramItem[] newBranches = new BiGramItem[init_biGramMatrix[fromId].length + 1];
					int insert = -(index + 1);
					System.arraycopy(init_biGramMatrix[fromId], 0, newBranches,
							0, insert);
					System.arraycopy(init_biGramMatrix[fromId], insert,
							newBranches, insert + 1,
							init_biGramMatrix[fromId].length - insert);
					newBranches[insert] = to;
					init_biGramMatrix[fromId] = newBranches;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 将初始化完成的矩阵赋值给全局变量
		biGramMatrix = init_biGramMatrix;
	}

	// 得到两个TermUnit对象之间的关联频率,作为得分
	public static int getRelationFreq(TermUnit from, TermUnit to) {
		if (from.getTid() < 0 || to.getTid() < 0) {
			return 0;
		}
		BiGramItem[] biGram_link_array = biGramMatrix[from.getTid()];

		int index = binarySearch(biGram_link_array, to.getTid());

		if (index < 0) {
			return 0;
		}
		return biGram_link_array[index].getFreq();
	}

	/**
	 * 二分查找某个key是否在某个一维数组中
	 */
	private static int binarySearch(BiGramItem[] biGram, int key) {
		int low = 0;
		int high = biGram.length - 1;

		while (low <= high) {
			int mid = (low + high) >>> 1;
			BiGramItem midVal = biGram[mid];
			int cmp = midVal.getId() - key;

			if (cmp < 0)
				low = mid + 1;
			else if (cmp > 0)
				high = mid - 1;
			else
				return mid; // key found
		}
		return -(low + 1); // key not found.
	}
}
