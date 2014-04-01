package com.zel.manager.nature;

import com.zel.entity.TermUnit;
import com.zel.entity.nature.TermNatureItem;
import com.zel.entity.nature.TermUnitNatureListPojo;
import com.zel.util.SystemParas;

/**
 * 词条的词性的管理类
 * 
 * @author zel
 * 
 */
public class TermNatureManager {
	// 初始化一个和Base数组相同大小的数组，专门存放词条的词性列表包括类
	public static TermUnitNatureListPojo[] termUnitNatureList = new TermUnitNatureListPojo[SystemParas.max_branch_array_length];

	static {
		termUnitNatureList[0] = (TermUnitNatureListPojo.BEGIN);
	}

	// 设置某个位置上词性列表
	public static void setTermNatureListPojo(int position,
			TermUnitNatureListPojo natureListPojo) {
		// 不需要做 natureListPojo的为null验证，在插入前已经做过了
		if (termUnitNatureList[position] == null) {
			termUnitNatureList[position] = natureListPojo;
		} else {
			// 代表词性的追加,这里只有增加新词条的时候才会用到
			if (natureListPojo != null) {
				TermUnitNatureListPojo ori_natureList = termUnitNatureList[position];
//				System.out.println("1----" + ori_natureList);
//				System.out.println("2----" + natureListPojo);
				if ((natureListPojo.getNatureList()) != null) {
					for (TermNatureItem termNatureItem : natureListPojo
							.getNatureList()) {
						ori_natureList.addNature(termNatureItem);
					}
				}
			}
		}
	}

	// 主要是在词条删除的时候
	public static void clearTermNatureListPojo(int position) {
		termUnitNatureList[position] = null;
	}

	// 得到某词条对应的词性列表
	public static TermUnitNatureListPojo getTermNatureListPojo(TermUnit termUnit) {
		return termUnitNatureList[termUnit.getTid()];
	}

	// 得该词条单元对应的词性对应的词频，辅助词意消歧使用
	public static int getTermUnitFreq(TermUnit termUnit) {
		TermUnitNatureListPojo natureListPojo = getTermNatureListPojo(termUnit);
		if (natureListPojo == null) {
			return 0;
		} else {
			return natureListPojo.getFreqs();
		}
	}
}
