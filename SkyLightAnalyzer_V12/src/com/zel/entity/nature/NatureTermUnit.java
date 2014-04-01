package com.zel.entity.nature;

import com.zel.util.MathUtil;

/**
 * 计算viterbi路径时的pojo类
 * 
 * @author zel
 */
public class NatureTermUnit {
	@Override
	public String toString() {
		return "NatureTermUnit [fromNatureTermUnit=" + fromNatureTermUnit
				+ ", score=" + score + ", selfScore=" + selfScore
				+ ", termNatureItem=" + termNatureItem + "]";
	}

	public TermNatureItem termNatureItem;

	public TermNatureItem getTermNatureItem() {
		return termNatureItem;
	}

	public void setTermNatureItem(TermNatureItem termNatureItem) {
		this.termNatureItem = termNatureItem;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public double getSelfScore() {
		return selfScore;
	}

	public void setSelfScore(double selfScore) {
		this.selfScore = selfScore;
	}

	/**
	 * 路径得分
	 */
	public double score = 0;
	/**
	 * 词性节点单元的自身得分
	 */
	public double selfScore;

	public NatureTermUnit fromNatureTermUnit;

	public NatureTermUnit getFromNatureTermUnit() {
		return fromNatureTermUnit;
	}

	public void setFromNatureTermUnit(NatureTermUnit fromNatureTermUnit) {
		this.fromNatureTermUnit = fromNatureTermUnit;
	}

	public NatureTermUnit(TermNatureItem termNatureItem) {
		this.termNatureItem = termNatureItem;
		selfScore = termNatureItem.getFreq() + 1;
	}

	/**
	 * 计算from-->to的得分
	 * @param natureTerm
	 */
	public void setPathScore(NatureTermUnit natureTerm) {
		double tempScore = MathUtil.calTwoNatureRelationFreq(natureTerm, this);
		if (fromNatureTermUnit == null || score < tempScore) {
			this.score = tempScore;
			this.fromNatureTermUnit = natureTerm;
		}
	}
}
