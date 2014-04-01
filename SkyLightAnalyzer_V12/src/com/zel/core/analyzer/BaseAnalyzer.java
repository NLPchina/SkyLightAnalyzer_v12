package com.zel.core.analyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zel.entity.BiGramItem;
import com.zel.entity.Branch;
import com.zel.entity.Forest;
import com.zel.entity.Graph;
import com.zel.entity.TermUnit;
import com.zel.entity.WordPojo;
import com.zel.entity.enums.WordPojoType;
import com.zel.entity.nature.TermUnitNatureListPojo;
import com.zel.entity.util.TermUnitStream;
import com.zel.interfaces.WoodInterface;
import com.zel.interfaces.analyzer.Analyzer;
import com.zel.manager.BiGramManager;
import com.zel.manager.ComplexToSimpleManager;
import com.zel.manager.LibraryManager;
import com.zel.manager.nature.TermNatureManager;
import com.zel.util.SystemParas;
import com.zel.util.TimeUtil;
import com.zel.util.io.ObjectAndByteArrayConvertUtil;
import com.zel.util.log.MyLogger;

/**
 * 基本分词，得到全分后的Graph
 * 
 * @author zel
 * 
 */
public abstract class BaseAnalyzer implements Analyzer {
	// 做日志用
	private static MyLogger logger = new MyLogger(BaseAnalyzer.class);
	private Forest forest;

	private static boolean isInitial = false;

	// 分词器初始化,而且不会初始化多次
	@Override
	public void init() {
		synchronized (BaseAnalyzer.class) {
			if (!isInitial) {
				long begin = System.currentTimeMillis();
				LibraryManager.makeTrie();
				long end = System.currentTimeMillis();
				System.out.println("本地构建或加载共用时"
						+ TimeUtil.getMinuteAndSecond(end - begin));
				this.forest = LibraryManager.forest;
				isInitial = true;
			} else {
				this.forest = LibraryManager.forest;
				System.out
						.println("SkyLightAnalyzer has init successful,not again");
			}
		}
	}

	// 主要思路即将外部的调用程序，将缓存得到的数据文件以byte array的方式传过
	@Override
	public void init_distributed(byte[] complexToSimpleArray,
			byte[] wordGroupMapArray, byte[] trieForestArray,
			byte[] termNatureArray, byte[] biGramMatrixArray) {
		synchronized (BaseAnalyzer.class) {
			if (!isInitial) {
				long begin = System.currentTimeMillis();
				LibraryManager.makeTrie_distributed(complexToSimpleArray,
						wordGroupMapArray, trieForestArray, termNatureArray,
						biGramMatrixArray);
				long end = System.currentTimeMillis();
				this.forest = LibraryManager.forest;
				isInitial = true;

//				System.out.println("1---"
//						+ ComplexToSimpleManager.complexToSimpleArray.length);
//				System.out.println("2---" + LibraryManager.wordGroupMap.size());
//				System.out.println("3---" + LibraryManager.forest);
//				System.out.println("4---"
//						+ TermNatureManager.termUnitNatureList.length);
//				System.out.println("5---" + BiGramManager.biGramMatrix.length);

				System.out.println("hdfs构建或加载共用时"
						+ TimeUtil.getMinuteAndSecond(end - begin));

			} else {
				this.forest = LibraryManager.forest;
				System.out
						.println("in hadoop,SkyLightAnalyzer has init successful,not again");
			}
		}
	}

	/**
	 * 该方法取得分词结果，包括字符串的所有数据的全切分结果，即只要是词都会被分出来,以Graph对象的形式返回，从而去做到词意消歧
	 */
	public Graph getSplitGraph(String content) {
		if (content == null || content.trim().length() < 1) {
			return null;
		}
		int begin = 0;// 去遍历content对应的charArray的position值
		int current = 0;// 去遍历content对应的charArray的current值
		int array_length = 0;// 每个charArray的数组长度
		String temp_string = null;// 暂存程序中任意字符串
		WoodInterface branch = null;// 临时存储branch对象

		Graph graph = new Graph(content);

		array_length = content.length();

		char origin = 0;
		char tran_origin = 0;
		for (begin = 0; begin < array_length; begin++) {
			current = begin;

			origin = content.charAt(begin);
			tran_origin = ComplexToSimpleManager.convert(origin);

			branch = this.forest.getBranch(tran_origin);
			if (branch == null) {// 没查着，说明trie树中没有该char开始的词条,故直接加入到graph中
				graph.addTermUnit(new TermUnit(origin + "", begin, 1, origin));// 为null代表只有一个字符,且该字符不在为词典的列表中，故第3个参数直接置1
			} else {// 说明有这个char,可以继续往下寻找
				/**
				 * 首先判断该串的开始是否为数字或是字母，进行字母串或是数字串的自动识别
				 */
				WordPojoType wordPojoType = branch.getWordType();
				if (wordPojoType != null) {
					switch (wordPojoType) {
					case NUMBER:
						int start = begin;
						int end = 1;
						Branch temp_branch = null;
						while (++begin < array_length
								&& ((temp_branch = this.forest
										.getBranch(ComplexToSimpleManager.convert(content.charAt(begin)))) != null)
								&& (temp_branch.getWordType() == WordPojoType.NUMBER || temp_branch.getWordType() == WordPojoType.ALPHA)) {
							end++;
							temp_branch = this.forest.getBranch(content
									.charAt(begin));
						}
						
						temp_string = content.substring(start, start + end);
						graph.addTermUnit(new TermUnit(temp_string, start,
								temp_string.length(), branch.getPosition(),
								TermUnitNatureListPojo.NUMBER));
						begin--;
						continue;
					case ALPHA:
						start = begin;
						end = 1;
						while (++begin < array_length
								&& ((temp_branch = this.forest
										.getBranch(ComplexToSimpleManager.convert(content.charAt(begin)))) != null)
								&& (WordPojoType.ALPHA == temp_branch
										.getWordType() || temp_branch.getWordType() == WordPojoType.NUMBER)) {
							end++;
							temp_branch = this.forest.getBranch(content
									.charAt(begin));
						}
						temp_string = content.substring(start, start + end);
						graph.addTermUnit(new TermUnit(temp_string, start,
								temp_string.length(), branch.getPosition(),
								TermUnitNatureListPojo.ENGLISTH));
						begin--;
						continue;
					}
				}

				// 首先判断该branch的staus是否为1，若为1则不往下找了
				switch (branch.getStatus()) {
				case 1:// 直接加入结果集
					temp_string = content.substring(begin, current + 1);
					graph.addTermUnit(new TermUnit(temp_string, begin,
							temp_string.length(), branch.getPosition()));// 将这个词加入分词结果集合
					break;// 直接break掉，进入下一个最外层的for循环
				case 0:// 继续往下寻找,当全切分时，此状态的term也要直接切出,但依然往下走
					temp_string = content.substring(begin, current + 1);
					graph.addTermUnit(new TermUnit(temp_string, begin,
							temp_string.length(), branch.getPosition()));// 将这个词加入分词结果集合
					if (begin == array_length - 1) {// 到了最后一个字符的切分，不管是什么直接切分,上步已做完
						break;
					}
				case 2:// 继续往下循找
					// 只要为2，可直接切分出来
					temp_string = content.substring(begin, current + 1);
					graph.addTermUnit(new TermUnit(temp_string, begin,
							temp_string.length(), branch.getPosition()));// 将这个词加入分词结果集合
					// 在这里给endFor添加标志，用来直接结束外层的for循环
					endFor: for (; (current + 1) < array_length; current++) {// 数组的第二个下标向下遍历
						origin = content.charAt(current + 1);
						tran_origin = ComplexToSimpleManager.convert(origin);// 先转换一下

						Branch temp_branch = forest.base_array[branch
								.getBase_value()
								+ tran_origin];
						if (temp_branch != null
								&& temp_branch.getFrontPosition() == branch
										.getPosition()
								&& temp_branch.getCharNode() == tran_origin) {// 说明找到下一个char
							switch (temp_branch.getStatus()) {
							case 1:
								temp_string = content.substring(begin,
										current + 2);
								graph.addTermUnit(new TermUnit(temp_string,
										begin, temp_string.length(),
										temp_branch.getPosition()));// 将这个词加入分词结果集合
								break endFor;// 直接break掉for循环
							case 2:
								temp_string = content.substring(begin,
										current + 2);
								graph.addTermUnit(new TermUnit(temp_string,
										begin, temp_string.length(),
										temp_branch.getPosition()));// 将这个词加入分词结果集合
								break;// 只break掉switch循环
							}
							branch = temp_branch;
						} else {
							break;
						}
					}
				}
			}
		}
		return graph;
	}

	/**
	 * content字符串的全切分的termUnit对象序列集合
	 * 
	 * @param content
	 * @return
	 */
	public List<TermUnit> getAllSplitList(String content) {
		Graph graph = getSplitGraph(content);
		if (graph == null) {
			return null;
		}
		TermUnit[] termArray = graph.getTermArray();
		List<TermUnit> resultList = new LinkedList<TermUnit>();// 存储分词后的结果数组

		for (TermUnit term : termArray) {
			resultList.add(term);
			while ((term = term.getNextTerm()) != null) {
				resultList.add(term);
			}
		}
		return resultList;
	}

	/**
	 * 得到词意消歧的分词结果
	 */
	@Override
	public List<TermUnit> getSplitResult(String content) {
		Graph graph = getSplitGraph(content);
		if (graph == null) {
			return null;
		}
		List<TermUnit> resultList = graph.getWsdResult();
		return resultList;
	}

	@Override
	public TermUnitStream getSplitResult(Reader sourceReader) {
		// TODO Auto-generated method stub
		String line = null;
		BufferedReader br = new BufferedReader(sourceReader);
		List<TermUnit> resultList = new LinkedList<TermUnit>();
		List<TermUnit> temp_list = null;
		try {
			while ((line = br.readLine()) != null) {
				temp_list = getSplitResult(line);
				if (temp_list != null) {
					resultList.addAll(temp_list);
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		TermUnitStream termUnitStream = new TermUnitStream(resultList);

		return termUnitStream;
	}

}
