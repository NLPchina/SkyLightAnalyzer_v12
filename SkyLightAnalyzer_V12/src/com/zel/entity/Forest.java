package com.zel.entity;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.zel.entity.enums.InsertWordStatus;
import com.zel.entity.enums.WordPojoType;
import com.zel.interfaces.WoodInterface;
import com.zel.manager.nature.TermNatureManager;
import com.zel.util.StringUtil;
import com.zel.util.SystemParas;
import com.zel.util.log.MyLogger;

/**
 * 用双数组形式来构造的trie树结构，理论上有最快的机械分词的查询方式
 * 
 * @author zel
 * 
 */
public class Forest implements WoodInterface, Serializable {
	// 做日志用
	private static MyLogger logger = new MyLogger(Forest.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 作为双数组中的base和check双数组 都在最大的基础之上*2,是省去给每个不同字符找index的时间了,便于方便的插入操作 即以空间换时间
	 */
	public Branch[] base_array = new Branch[SystemParas.max_branch_array_length];
	/**
	 * 用来为防止后边的字符被意外占用,不需要持久化到缓存中，故加static
	 */
	// public static PlaceHoderBranch[] placeHolder_array = new
	// PlaceHoderBranch[SystemParas.max_branch_array_length];
	public static boolean[] placeHolder_array = new boolean[SystemParas.max_branch_array_length];

	// public CheckPojo[] check_array = new
	// CheckPojo[SystemParas.max_branch_array_length];

	public void insertBranch(Branch branch) {
		int position = branch.getPosition();
		// 首先判断要插入的节点是否会产生冲突，如果产生冲突，则要首先调整this对象
		Branch myBranch = base_array[position];

		// 首先判断该节点是否已存在
		if (myBranch == null) {// 说明该节点是第一次插入
			// System.out.println("position----"+position);
			base_array[position] = branch;// 给该空白位置赋值
			// check_array[position] = new CheckPojo(branch.getFrontPosition());
		} else {// 不是第一次插入
			switch (myBranch.getStatus()) {// 先判断在root中的status
			case 0:
				if (branch.getStatus() == 1) {// 现在已经是个词了
					myBranch.setStatus(2);
				}
				break;
			case 1:
				if (branch.getStatus() == 0) {// 原先是个词，现在新加的有延长，故设置为2
					myBranch.setStatus(2);
				}
				break;
			}
		}
	}

	@Override
	public int getStatus() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Branch getBranch(int index) {
		if (index >= SystemParas.max_branch_array_length) {
			return null;
		}
		// System.out.println("index---"+base_array[96970]);
		// System.out.println("index1---"+base_array[20013]);
		return base_array[index];
	}

	// public boolean isNull(int baseValue, int c) {
	// return base_array[baseValue + c] == null;
	// }

	public boolean isNull(int baseValue, char nextChar) {
		int temp_value = baseValue + nextChar;
		return base_array[temp_value] == null
				&& (!placeHolder_array[temp_value]);
	}

	// 在相应位置设置占位符
	public void setPlaceHoldValue(int frontPositionBaseValue,
			char nextChar) {
		placeHolder_array[frontPositionBaseValue + nextChar] = true;
	}

	/**
	 * 判断这个词是否能直接插入,用插入新词的状态来作为返回值
	 * 
	 * @param wordItem
	 * @return
	 */
	public InsertWordStatus isValidToSingleWord(String wordItem) {
		char[] chars = wordItem.toCharArray();
		Branch branch = null;
		int base_value = 0;
		int front_position = 0;
		// 为解决插入一个现有词典中的字符前缀而添加的标志变量
		boolean isZero4LastCharStatu = false;
		for (char c : chars) {
			branch = getBranch(base_value + c);
			// System.out.println("****"+branch);
			if (branch != null) {
				// 该节点字符相同，并且check数组中的位置也要相同
				if (branch.getCharNode() == c
						&& front_position == branch.getFrontPosition()) {
					if (branch.getStatus() == 0) {
						isZero4LastCharStatu = true;
					} else {
						isZero4LastCharStatu = false;
					}
				} else {
					return InsertWordStatus.Conflict;
				}
				front_position = branch.getPosition();
				base_value = branch.getBase_value();
			} else {
				// 说明前边的无冲突，到此为null，即也无冲突，即为合法
				return InsertWordStatus.Valid;
			}
		}
		// 执行到这里要返回false,是由于前边的都判断过了，说明该词在Trie树中，无需再加入了!
		return isZero4LastCharStatu ? InsertWordStatus.Valid
				: InsertWordStatus.Exist;
	}

	/**
	 * 得到一个wordItem上各个节点的Branch
	 * 
	 * @param wordItem
	 * @return
	 */
	public List<Branch> getWordItemNodeList(String wordItem) {
		char[] chars = wordItem.toCharArray();
		List<Branch> list = new LinkedList<Branch>();
		Branch branch = null;
		int base_value = 0;
		for (char c : chars) {
			branch = getBranch(base_value + c);
			if (branch != null) {
				list.add(branch);
			} else {
				logger.info("删掉节点时出现异常了,得到空节点!");
			}
			base_value = branch.getBase_value();
		}
		return list;
	}

	public List<Branch> getWordGroupNodeList(List<WordPojo> wordList) {
		List<Branch> list = new LinkedList<Branch>();
		Branch branch = null;
		for (WordPojo wordItem : wordList) {
			char[] chars = wordItem.getWord().toCharArray();
			int base_value = 0;
			for (char c : chars) {
				branch = getBranch(base_value + c);
				if (branch != null) {
					list.add(branch);
				} else {
					logger.info("删掉节点时出现异常了,得到空节点!");
				}
				base_value = branch.getBase_value();
			}
		}
		return list;
	}

	public void setNull4BaseAndCheck(List<Branch> list) {
		for (Branch branch : list) {
			this.base_array[branch.getPosition()] = null;
			// 清空一下某些词对应的词性部分
			TermNatureManager.clearTermNatureListPojo(branch.getPosition());
		}
	}

	@Override
	public int getBase_value() {
		return 0;
	}

	@Override
	public int getPosition() {
		return 0;
	}

	/**
	 * 得到一个词在词典中的id，和匹配该词的过程一致,找不到则置0
	 * 
	 * @param str
	 * @return
	 */
	public int getTermId(String str) {
		if (StringUtil.isBlank(str)) {
			return 0;
		}
		int baseValue = str.charAt(0);
		int checkValue = 0;
		for (int i = 1; i < str.length(); i++) {
			checkValue = baseValue;
			if (base_array[baseValue] == null) {
				return 0;
			}
			baseValue = base_array[baseValue].getBase_value() + str.charAt(i);
			if (baseValue > base_array.length - 1)
				return 0;
			if ((base_array[baseValue] == null)
					|| (base_array[baseValue].getFrontPosition() > 0 && base_array[baseValue]
							.getFrontPosition() != checkValue)) {
				return 0;
			}
		}
		return baseValue;
	}

	@Override
	public WordPojoType getWordType() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 为已知的确定位置占位
	 * 
	 * @author zel
	 * 
	 */
	class PlaceHoderBranch implements Serializable {
		private static final long serialVersionUID = 1L;

		public PlaceHoderBranch(char frontChar, int frontPositionValue,
				char nextChar) {
			this.frontChar = frontChar;
			this.frontPositionValue = frontPositionValue;
			this.nextChar = nextChar;
		}

		private char frontChar;

		public char getFrontChar() {
			return frontChar;
		}

		public void setFrontChar(char frontChar) {
			this.frontChar = frontChar;
		}

		public int getFrontPositionValue() {
			return frontPositionValue;
		}

		public void setFrontPositionValue(int frontPositionValue) {
			this.frontPositionValue = frontPositionValue;
		}

		public char getNextChar() {
			return nextChar;
		}

		public void setNextChar(char nextChar) {
			this.nextChar = nextChar;
		}

		private int frontPositionValue;
		private char nextChar;

	}
}
