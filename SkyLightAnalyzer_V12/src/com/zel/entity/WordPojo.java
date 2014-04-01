package com.zel.entity;

import java.io.Serializable;

import com.zel.entity.enums.WordPojoType;
import com.zel.entity.nature.TermNatureItem;
import com.zel.entity.nature.TermUnitNatureListPojo;

public class WordPojo implements Comparable<WordPojo>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int hashCode() {
		return 1;
	}

	@Override
	public boolean equals(Object obj) {
		WordPojo wordPojo = (WordPojo) obj;
		// System.out.println("***this*****"+this.getWord());
		// System.out.println("***wordPojo*****"+wordPojo.getWord());
		return this.getWord().equals(wordPojo.getWord());
	}

	@Override
	public String toString() {
		return "WordPojo [word=" + word + "]";
	}

	public WordPojo(String word) {
		this.word = word;
	}

	/**
	 * 词条对应的词性集合的包装类
	 */
	private TermUnitNatureListPojo termNatureList = null;

	// 将typeStr转换成WordPojoType类型，将paras转化为词性集合
	public WordPojo(String word, String typeStr, String natureStr)throws Exception {
		this.word = word;
		this.wordType = getWordType(typeStr);

		// 此处初始化词条的词性列表
		if (natureStr != null) {
			this.termNatureList = getTermNatureList(natureStr);
		}
	}
	
	public WordPojo(String word, String natureStr) throws Exception{
		this.word = word;
		// 此处初始化词条的词性列表
		if (natureStr != null) {
			this.termNatureList = getTermNatureList(natureStr);
		}
	}

	
	public TermUnitNatureListPojo getTermNatureList(String natureStr) throws Exception{
		if (natureStr == null || natureStr.trim().length() == 0)
			return null;

		natureStr = natureStr.substring(1, natureStr.length() - 1);
		String[] split = natureStr.split(",");
		String[] strs = null;
		int frequency = 0;
		TermUnitNatureListPojo natureList = new TermUnitNatureListPojo();
		for (int i = 0; i < split.length; i++) {
			strs = split[i].split("=");
			frequency = Integer.parseInt(strs[1]);
			natureList.addNature(new TermNatureItem(strs[0].trim(), frequency));
		}
		return natureList;
	}

	/**
	 * 将字符串类的wordType转换成枚举类型
	 * 
	 * @param typeStr
	 * @return
	 */
	public WordPojoType getWordType(String typeStr) {
		if (typeStr == null) {
			return null;
		}
		int type = Integer.valueOf(typeStr);
		switch (type) {
		case 2:// 符号和中文
			return WordPojoType.CONTINUE;
		case 3:// 符号和中文
			return WordPojoType.END;
		case 5:// 数字
			return WordPojoType.NUMBER;
		case 4:// 字母
			return WordPojoType.ALPHA;
		}
		return null;
	}

	private String word;
	private WordPojoType wordType;// wordPojo的类型，数值型、字母型等

	public TermUnitNatureListPojo getTermNatureList() {
		return termNatureList;
	}

	public void setTermNatureList(TermUnitNatureListPojo termNatureList) {
		this.termNatureList = termNatureList;
	}

	public WordPojoType getWordType() {
		return wordType;
	}

	public void setWordType(WordPojoType wordType) {
		this.wordType = wordType;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	// 若有比较，则升序排列
	@Override
	public int compareTo(WordPojo o) {
		return this.getWord().length()-o.getWord().length();
	}
}
