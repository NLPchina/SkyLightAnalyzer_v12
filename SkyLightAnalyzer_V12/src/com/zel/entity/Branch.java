package com.zel.entity;

import java.io.Serializable;
import java.util.Arrays;

import com.zel.entity.enums.WordPojoType;
import com.zel.entity.nature.TermUnitNatureListPojo;
import com.zel.interfaces.WoodInterface;
import com.zel.manager.nature.TermNatureManager;

/**
 * 节点类,trie树的基本组成单位
 * 
 * @author zel
 */
public class Branch implements WoodInterface, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TermUnitNatureListPojo natureListPojo = null;

	// 只有词的末尾的字才会携带该词的该词的类型属性，如字母，数字，汉字等
	private WordPojoType wordType = null;

	public Branch(char charNode, int position, int frontPosition,
			int base_value, int status, TermUnitNatureListPojo natureListPojo) {
		this.charNode = charNode;
		this.position = position;
		this.frontPosition = frontPosition;
		this.base_value = base_value;
		this.status = status;
		this.natureListPojo = natureListPojo;

		// 在这里设置下词性
		TermNatureManager.setTermNatureListPojo(position, natureListPojo);
		// System.out.println(charNode+ "  "+ position+ "  "+natureListPojo);
	}

	public Branch(char charNode, int position, int frontPosition,
			int base_value, int status, TermUnitNatureListPojo natureListPojo,
			WordPojoType wordType) {
		this.charNode = charNode;
		this.position = position;
		this.frontPosition = frontPosition;
		this.base_value = base_value;
		this.status = status;
		this.natureListPojo = natureListPojo;

		// 在这里设置下词性
		TermNatureManager.setTermNatureListPojo(position, natureListPojo);
		// 在这里设置wordType
		this.wordType = wordType;
	}

	@Override
	public String toString() {
		return "Branch [base_value=" + base_value + ", charNode=" + charNode
				+ ", frontPosition=" + frontPosition + ", position=" + position
				+ ", status=" + status + "]";
	}

	public TermUnitNatureListPojo getNatureListPojo() {
		return natureListPojo;
	}

	public void setNatureListPojo(TermUnitNatureListPojo natureListPojo) {
		this.natureListPojo = natureListPojo;
	}

	// 每个char对象的 int值，来唯一标识一个词中的字的char值
	private char charNode;

	public char getCharNode() {
		return charNode;
	}

	public void setCharNode(char charNode) {
		this.charNode = charNode;
	}

	public int getBase_value() {
		return base_value;
	}

	public void setBase_value(int baseValue) {
		base_value = baseValue;
	}

	// status=0代表只是个字; status=1代表是个词,并没有后续字符;status=2代表不仅是个独立的词，且是某个其它词的中间状态
	private int status;
	private int base_value;
	private int frontPosition;

	public int getFrontPosition() {
		return frontPosition;
	}

	public void setFrontPosition(int frontPosition) {
		this.frontPosition = frontPosition;
	}

	private int position;// 标志每个branch所在的位置的数量表示

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Override
	public Branch getBranch(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void insertBranch(Branch branch) {
		// TODO Auto-generated method stub
	}

	public WordPojoType getWordType() {
		return wordType;
	}

	public void setWordType(WordPojoType wordType) {
		this.wordType = wordType;
	}

}
