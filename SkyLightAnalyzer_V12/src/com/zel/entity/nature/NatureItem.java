package com.zel.entity.nature;

import java.io.Serializable;

/**
 * 词性条目pojo类，只包括词性，跟词条无关,即一个该对象对应nature.map中的一条记录
 * 
 * @author zel
 */
public class NatureItem implements Serializable {

	@Override
	public String toString() {
		return "NatureItem [freq=" + freq + ", index=" + index + ", name="
				+ name + ", natureIndex=" + natureIndex + "]";
	}

	public static final NatureItem BEGIN = new NatureItem("始##始", 50610);
	public static final NatureItem END = new NatureItem("末##末", 50610);

	public static final NatureItem EN = new NatureItem("en", 1);// 字母型
	public static final NatureItem NB = new NatureItem("m", 1);// 数值型
	public static final NatureItem UNKNOWN = new NatureItem("unknown", 1);// 无词性型

	public static final int unknown_index = -1;
	public static final int unknown_nature_index = -1;

	// 词性的下标值
	private int natureIndex;
	// 该词性对应的频率,在arrays.dic中所示的v=1,a=10之中的1，10之类的数字
	private int freq;
	// 词性的名称，如n,v,ad等
	private String name;
	// 词性对照表的位置
	private int index;

	/**
	 * 该构造函数用于在system.dic中的词条与词性的对应词频
	 * 
	 * @param name
	 * @param freq
	 */
	public NatureItem(String name, int freq) {
		this.name = name;
		this.freq = freq;
	}

	/**
	 * 该构造参数用于词性之间的关系时用到在NatureRelationManager类中
	 * 
	 * @param name
	 * @param index
	 * @param natureIndex
	 * @param freq
	 */
	public NatureItem(String name, int index, int natureIndex, int freq) {
		this.name = name;
		this.freq = freq;
		this.index = index;
		this.natureIndex = natureIndex;
	}

	public int getIndex() {
		return index;
	}

	public int getNatureIndex() {
		return natureIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

}
