package com.zel.entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.zel.util.StaticValue;

/**
 * 1、构建求词单元之间最可能路径的图 2、存储各个的TermUnit对象，并通过from,next指针构成图 3、一个分词的串对应一个Graph对象
 * 
 * @author zel
 */
public class Graph {
	/**
	 * 1、存储图中各个节点的termUnit数组 2、在构造方法中对其根据串的长度进行初始化 3、要考虑到root和end两个节点
	 */
	private TermUnit[] termArray = null;

	// 图的根节点，即头节点，即开始节点
	private TermUnit root;
	// 图的末尾节点
	private TermUnit end;
	// 待分词的字符串
	private String content;

	public Graph(String content) {
		this.content = content;
		// 首先设置头节点，用来计算第1个实际节点的得分
		// -1 代表是头节点
		// tid=0代表头或尾
		root = new TermUnit(StaticValue.TermStart, -1, StaticValue.TermStart
				.length(), 0);
		end = new TermUnit(StaticValue.TermEnd, this.content.length(),
				StaticValue.TermEnd.length(), -1);

		// 因为始终有个多余的节点， 即头或尾， 故在元素列表中多申请一个空间
		termArray = new TermUnit[this.content.length() + 1];
		// 将结束标志end对象直接加入到数组的最后一个位置上
		termArray[this.content.length()] = end;
	}

	public void addTermUnit(TermUnit termUnit) {
		if (termUnit == null)
			return;

		if (termArray[termUnit.getOffset()] == null) {
			termArray[termUnit.getOffset()] = termUnit;
		} else {
			// 将同一offset位置上的数组元素形成链表，先到排后，后到排前的顺序
			termUnit.setNextTerm(termArray[termUnit.getOffset()]);
			termArray[termUnit.getOffset()] = termUnit;
		}
	}

	/**
	 * 取得termUnit的全切分序列组成的graph对象的viterbi最佳路径
	 * 
	 * @return
	 */
	public List<TermUnit> getWsdResult() {
		// 得到每个termUnit对象，及其附带的from指针
		travelByBestScore();
		// 通过from指针去剔除termArray中的多余termUnit，即剩下的termArray中的所有的非空元素，即为构成的消歧后的分词结果
		// 这是viterbi算法的典型作法，计算完成后会保留一个尾对象的前向指针，从而得到最终的最短路径
		optimalPaths();

		int length = termArray.length - 1;
		List<TermUnit> result = new ArrayList<TermUnit>(length);
		for (int i = 0; i < length; i++) {
			if (termArray[i] != null) {
				result.add(termArray[i]);
			}
		}
		return result;
	}

	/**
	 * 通过修改TermUnit的from指针来说明分词的最佳路径 其结果直接通过termArray来反应
	 */
	private void travelByBestScore() {
		// 首先计算第一个termUnit的打分,相当于初始化，即相当于从root单元开始
		mergeTerms(root, termArray[0]);

		int term_array_len = termArray.length - 1;// 包括了最后的end,这样会走到实际当中的末尾字节，即计算了末尾字符和TermEnd的距离
		TermUnit fromTerm = null;// 暂存开始单元
		TermUnit toTerm = null;// 暂存到的单元
		for (int i = 0; i < term_array_len; i++) {
			fromTerm = termArray[i];
			while (fromTerm != null && fromTerm.getFromTerm() != null
					&& fromTerm != end) {
				toTerm = termArray[fromTerm.getOffset() + fromTerm.getLength()];
				mergeTerms(fromTerm, toTerm);
				fromTerm = fromTerm.getNextTerm();
			}
		}
	}

	/**
	 * 回溯from,将from和to之间的元素清空，无from的不变，则非null处的元素即为最短路径的组成
	 * 
	 * @return
	 */
	private void optimalPaths() {
		TermUnit toTerm = end;
		// to.clearScore();
		TermUnit fromTerm = null;
		while ((fromTerm = toTerm.getFromTerm()) != null) {
			for (int i = fromTerm.getOffset() + 1; i < toTerm.getOffset(); i++) {
				termArray[i] = null;
			}
			if (fromTerm.getOffset() > -1) {
				termArray[fromTerm.getOffset()] = fromTerm;
			}
			// 断开横向链表.节省内存
			fromTerm.setNextTerm(null);
			toTerm = fromTerm;
		}
	}

	/**
	 * 对两个termUnit之间的所有可能的路径进行打分计算， 得出这两个termUnit之间的最佳的一条路径
	 * 
	 * @param fromTerm
	 * @param toTerm
	 */
	private void mergeTerms(TermUnit fromTerm, TermUnit toTerm) {
		while (toTerm != null) {
			toTerm.setFromPath(fromTerm);
			toTerm = toTerm.getNextTerm();
		}
	}

	public TermUnit[] getTermArray() {
		return termArray;
	}

	public void setTermArray(TermUnit[] termArray) {
		this.termArray = termArray;
	}
}
