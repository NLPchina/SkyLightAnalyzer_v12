package com.zel.manager.nature;

import java.util.List;

import com.zel.entity.TermUnit;
import com.zel.entity.nature.NatureTermUnit;
import com.zel.entity.nature.TermNatureItem;
import com.zel.entity.nature.TermUnitNatureListPojo;
import com.zel.util.StaticValue;

/**
 * 词性标注管理器,为分词结果添加词性标注
 * 
 * @author zel
 * 
 */
public class PosManager {
	private NatureTermUnit root = new NatureTermUnit(TermNatureItem.BEGIN);

	// end组永远只有一个元素，只是为了处理方便
	private NatureTermUnit[] end = new NatureTermUnit[] { new NatureTermUnit(
			TermNatureItem.END) };

	private List<TermUnit> termList = null;

	private NatureTermUnit[][] natureTermRelationTable = null;

	public PosManager(List<TermUnit> termList) {
		this.termList = termList;

		natureTermRelationTable = new NatureTermUnit[termList.size() + 1][];
		natureTermRelationTable[termList.size()] = end;
	}

	/**
	 * 重置预处理的集合 因为线程安全问题，废弃该方法，直接new一个新对象来解决
	 * 
	 * @param termList
	 */
	@Deprecated
	public void reset(List<TermUnit> termList) {
		if (termList == null || termList.size() == 0) {
			return;
		}
		this.termList = termList;
		natureTermRelationTable = new NatureTermUnit[termList.size() + 1][];

		end[0].setFromNatureTermUnit(null);
		natureTermRelationTable[termList.size()] = end;
	}

	// 传入词意消歧后的分词结果串
	public void addPos() {
		if (termList == null || termList.size() == 0) {
			return;
		}
		int length = termList.size();
		for (int i = 0; i < length; i++) {
			natureTermRelationTable[i] = getTermNaturePojoArray(termList.get(i)
					.getTermUnitNatureListPojo());
		}
		walkToPOS();
	}

	// 作相关词性的合并，尤其是数词m和量词q的结合
	public void mergePOS() {
		boolean isMergePOS = false;
		int length = termList.size();
		TermUnit termUnit_temp = null;
		for (int i = 0; i < length; i++) {
			if ((termUnit_temp = termList.get(i)) != null) {
				isMergePOS = mergePOS(i, termUnit_temp, termList, length);
				if (isMergePOS) {
					termList.remove(i + 1);
					i--;
					length--;
				}
			}
		}
	}

	// 用于合并相关词性对应的词条,如数词m和量词q
	private boolean mergePOS(int currentTermOffset, TermUnit front,
			List<TermUnit> termArrayList, int termArray_Length) {
		// 判断是不是数词，如果不是，暂不列入词性的合并范围
		String front_nature_name = front.getNatureTermUnit()
				.getTermNatureItem().getNatureItem().getName();
		boolean isFindMerge = false;
		if (!(front_nature_name.equals("m") || front_nature_name.equals("q") || front_nature_name.equals("mq"))) {
			return isFindMerge;
		}
		TermUnit termUnit_temp = null;

		for (int i = currentTermOffset + 1; i < termArray_Length; i++) {
			if ((termUnit_temp = termArrayList.get(i)) != null) {
				// 发现跟着量词，则合并
				front_nature_name = termUnit_temp.getNatureTermUnit()
						.getTermNatureItem().getNatureItem().getName();
				// 后边跟的是m,q,u，主要针对"之"的情况
				if (StaticValue.pos_can_merge_set_normal.contains(front_nature_name)) {
					front.merge(termUnit_temp);
					isFindMerge = true;
				} else if (StaticValue.pos_can_merge_value_set_special.contains(termUnit_temp.getValue())) {// 即为了处理"之"的情况
					// 首先看下后边的词单元是否为数词，若为数词则合并该u，如果不是数词则不合并该u
					TermUnit termUnit_temp2=null;
					if (i + 1 < termArray_Length
							&& (termUnit_temp2 = termArrayList.get(i + 1)) != null) {
						front_nature_name = termUnit_temp2.getNatureTermUnit()
								.getTermNatureItem().getNatureItem().getName();
						if (StaticValue.pos_can_merge_set_normal.contains(front_nature_name)) {
							front.merge(termUnit_temp);
							isFindMerge = true;
						}
					}
				}
				break;
			}
		}
		return isFindMerge;
	}

	public void walkToPOS() {
		int length = natureTermRelationTable.length - 1;
		setPathScore(root, natureTermRelationTable[0]);
		for (int i = 0; i < length; i++) {
			for (int j = 0; j < natureTermRelationTable[i].length; j++) {
				setPathScore(natureTermRelationTable[i][j],
						natureTermRelationTable[i + 1]);
			}
		}
		optimalRoot();
	}

	/**
	 * 获得最优路径
	 */
	private void optimalRoot() {
		NatureTermUnit to = end[0];
		NatureTermUnit from = null;
		int index = natureTermRelationTable.length - 1;
		while ((from = to.getFromNatureTermUnit()) != null && index > 0) {
			termList.get(--index).setNatureTermUnit(from);
			to = from;
		}
	}

	private void setPathScore(NatureTermUnit natureTermUnit,
			NatureTermUnit[] natureTermUnitArray) {
		for (int i = 0; i < natureTermUnitArray.length; i++) {
			natureTermUnitArray[i].setPathScore(natureTermUnit);
		}
	}

	private NatureTermUnit[] getTermNaturePojoArray(
			TermUnitNatureListPojo termNaturePojoList) {
		List<TermNatureItem> naturePojoList = termNaturePojoList
				.getNatureList();
		NatureTermUnit[] natureTerms = new NatureTermUnit[naturePojoList.size()];
		for (int i = 0; i < natureTerms.length; i++) {
			natureTerms[i] = new NatureTermUnit(naturePojoList.get(i));
		}
		return natureTerms;
	}

}
