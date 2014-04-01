package com.zel.entity;

import java.io.Serializable;

/**
 * 据说是google用来统计词与词之间的关联频率的pojo类
 * 
 * @author zel
 * 
 */
public class BiGramItem implements Comparable<BiGramItem>, Serializable {
	/**
	 * 关联词典中，后一个词对应的id, 词对应的id是对trie树的查找确定的，保证了id的唯一性
	 */
	private int id;
	/**
	 * 词频
	 */
	private int freq;

	public BiGramItem(int id, int freq) {
		this.id = id;
		this.freq = freq;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}

	@Override
	public int compareTo(BiGramItem o) {
		// TODO Auto-generated method stub
		return this.id - o.id;
	}
}
