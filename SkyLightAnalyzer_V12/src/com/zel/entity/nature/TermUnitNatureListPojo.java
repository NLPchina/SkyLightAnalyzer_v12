package com.zel.entity.nature;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


/**
 * 每个词条可能有多个词性，该类为每个词条持有的词性集合封装类
 * 
 * @author zel
 * 
 */
public class TermUnitNatureListPojo implements Serializable {

	public static final TermUnitNatureListPojo BEGIN = new TermUnitNatureListPojo(
			TermNatureItem.BEGIN);
	public static final TermUnitNatureListPojo ENGLISTH = new TermUnitNatureListPojo(
			TermNatureItem.EN);
	public static final TermUnitNatureListPojo NUMBER = new TermUnitNatureListPojo(
			TermNatureItem.NB);
	public static final TermUnitNatureListPojo UNKNOWN = new TermUnitNatureListPojo(
			TermNatureItem.UNKNOWN);
	
	@Override
	public String toString() {
		return "TermUnitNatureListPojo [freqs=" + freqs + ", natureList="
				+ natureList + "]";
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<TermNatureItem> natureList = null;// 词性集合列表

	public List<TermNatureItem> getNatureList() {
		return natureList;
	}

	public void setNatureList(List<TermNatureItem> natureList) {
		this.natureList = natureList;
	}

	private int freqs = 0;// 这些词性对应的总的词频

	public int getFreqs() {
		return freqs;
	}

	public void setFreqs(int freqs) {
		this.freqs = freqs;
	}

	// 初始化词性集合类
	public TermUnitNatureListPojo() {
		natureList = new LinkedList<TermNatureItem>();
	}

	// 初始化词性集合类
	public TermUnitNatureListPojo(TermNatureItem naturePojo) {
		natureList = new LinkedList<TermNatureItem>();
		addNature(naturePojo);
	}

	public TermUnitNatureListPojo addNature(TermNatureItem naturePojo) {
		this.natureList.add(naturePojo);
		this.freqs += naturePojo.getFreq();
		return this;
	}
}
