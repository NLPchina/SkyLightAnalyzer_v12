package com.zel.entity.util;

import java.util.Iterator;
import java.util.List;

import com.zel.entity.TermUnit;

/**
 * 对分词结果以类似于流的形式的包装类
 * 
 * @author zel
 * 
 */
public class TermUnitStream {
	private List<TermUnit> termList;

	public List<TermUnit> getTermList() {
		return termList;
	}

	public void setTermList(List<TermUnit> termList) {
		this.termList = termList;
	}

	private Iterator<TermUnit> iterator = null;

	public TermUnitStream(List<TermUnit> termList) {
		this.termList = termList;
		this.iterator = this.termList.iterator();
	}

	public TermUnitStream(TermUnitStream termUnitStream) {
		this.termList.addAll(termUnitStream.getTermList());
		this.iterator = this.termList.iterator();
	}

	public TermUnit getNextTermUnit() {
		if (iterator.hasNext()) {
			return this.iterator.next();
		}
		return null;
	}

	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public TermUnit getNext() {
		return this.iterator.next();
	}
}
