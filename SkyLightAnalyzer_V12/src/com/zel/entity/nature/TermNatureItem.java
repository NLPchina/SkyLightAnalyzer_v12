package com.zel.entity.nature;

import java.io.Serializable;

import com.zel.manager.nature.NatureRelationManager;

/**
 * 词条与词性对应的pojo类
 * 
 * @author zel
 */
public class TermNatureItem implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + freq;
		result = prime * result
				+ ((natureItem == null) ? 0 : natureItem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		TermNatureItem other = (TermNatureItem) obj;
		if (freq != other.freq)
			return false;
		if (natureItem == null) {
			if (other.natureItem != null)
				return false;
		} else if (!natureItem.equals(other.natureItem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TermNatureItem [freq=" + freq + ", natureItem=" + natureItem
				+ "]";
	}

	public static final TermNatureItem BEGIN = new TermNatureItem("始##始", 50610);
	public static final TermNatureItem END = new TermNatureItem("末##末", 50610);

	public static final TermNatureItem EN = new TermNatureItem("en", 1);// 字母型
	public static final TermNatureItem NB = new TermNatureItem("m", 1);// 数值型
	public static final TermNatureItem UNKNOWN = new TermNatureItem("unknown",
			1);// 无词性型

	public int freq;
	private NatureItem natureItem;

	public NatureItem getNatureItem() {
		return natureItem;
	}

	public void setNatureItem(NatureItem natureItem) {
		this.natureItem = natureItem;
	}

	public TermNatureItem(String natureName, int freq) {
		this.natureItem = NatureRelationManager.getNatureItem(natureName);
		this.freq = freq;
	}

	public int getFreq() {
		return freq;
	}

	public void setFreq(int freq) {
		this.freq = freq;
	}
}
