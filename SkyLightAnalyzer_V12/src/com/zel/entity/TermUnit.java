package com.zel.entity;

import com.zel.entity.nature.NatureTermUnit;
import com.zel.entity.nature.TermUnitNatureListPojo;
import com.zel.manager.nature.TermNatureManager;
import com.zel.util.MathUtil;

/**
 * 切分出来对象的词条单元
 * 
 * @author zel
 */
public class TermUnit {

	@Override
	public String toString() {
		return "TermUnit [fromTerm=" + fromTerm + ", length=" + length
				+ ", natureTermUnit=" + natureTermUnit + ", nextTerm="
				+ nextTerm + ", offset=" + offset + ", score=" + score
				+ ", termUnitNatureListPojo=" + termUnitNatureListPojo
				+ ", tid=" + tid + ", value=" + value + "]";
	}

	/**
	 * 分词单元的串值
	 */
	private String value;
	/**
	 * 在源串的起始位置
	 */
	private int offset;
	/**
	 * 在源串的结束位置
	 */
	private int length;
	/**
	 * 上一个termUnit
	 */
	private TermUnit fromTerm;
	/**
	 * 下一个termUnit
	 */
	private TermUnit nextTerm;
	/**
	 * 到这个termUnit路径的得分
	 */
	private double score;

	/**
	 * 唯一标志该termUnit的id，即position
	 * 与bi-gram词典中的词有对应该关系，方便系统词典与bi-gram关联关系的词频查询，通过tid作为下标去查找
	 * -1代表没有对象关系，即不在词典中
	 */
	private int tid;

	/**
	 * 专门存放词性标注的数据
	 */
	private NatureTermUnit natureTermUnit;

	public NatureTermUnit getNatureTermUnit() {
		return natureTermUnit;
	}

	public void setNatureTermUnit(NatureTermUnit natureTermUnit) {
		this.natureTermUnit = natureTermUnit;
	}

	//词条对应的词列表
	private TermUnitNatureListPojo termUnitNatureListPojo = null;

	public TermUnit(String value, int offset, int length, int tid) {
		this.value = value;
		this.offset = offset;
		this.length = length;
		this.tid = tid;
		//将词性列表放入当前词条对象中
		if(tid>-1){
			TermUnitNatureListPojo natureListPojo=TermNatureManager.getTermNatureListPojo(this);
			if(natureListPojo==null){
				this.setTermUnitNatureListPojo(TermUnitNatureListPojo.UNKNOWN);
			}else {
				this.setTermUnitNatureListPojo(natureListPojo);				
			}
		}
	}
	
	//该方法中的tid设置为多少，有待确定
	public TermUnit(String value, int offset, int length, int tid,TermUnitNatureListPojo termUnitNatureListPojo) {
		this.value = value;
		this.offset = offset;
		this.length = length;
		this.tid = tid;
		//将词性列表放入当前词条对象中
		this.setTermUnitNatureListPojo(termUnitNatureListPojo);
	}

	// 计算当前term和from term的距离
	public void setFromPath(TermUnit fromTerm) {
		// 维特比进行最优路径的构建
		double score = MathUtil.calcScore(fromTerm, this);
		if (this.getFromTerm() == null || this.getScore() > score) {
			this.setFromAndScore(fromTerm, score);
		}
	}

	private void setFromAndScore(TermUnit fromTerm, double score) {
		this.fromTerm = fromTerm;
		this.score = score;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public TermUnit getFromTerm() {
		return fromTerm;
	}

	public void setFromTerm(TermUnit fromTerm) {
		this.fromTerm = fromTerm;
	}

	public TermUnit getNextTerm() {
		return nextTerm;
	}

	public void setNextTerm(TermUnit nextTerm) {
		this.nextTerm = nextTerm;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}
	public TermUnitNatureListPojo getTermUnitNatureListPojo() {
		return termUnitNatureListPojo;
	}

	public void setTermUnitNatureListPojo(
			TermUnitNatureListPojo termUnitNatureListPojo) {
		this.termUnitNatureListPojo = termUnitNatureListPojo;
	}
	
	public void merge(TermUnit termUnit){
		this.setValue(this.getValue()+termUnit.getValue());
		//合成后定义为量词
//		this.getNatureTermUnit().setTermNatureItem(this.getNatureTermUnit().getTermNatureItem());
//		this.getNatureTermUnit().setTermNatureItem(termUnit.getNatureTermUnit().getTermNatureItem());
		this.setLength(this.getLength()+termUnit.getLength());
	}
	
}
