package com.zel.util;

import com.zel.entity.TermUnit;
import com.zel.entity.nature.NatureTermUnit;
import com.zel.manager.BiGramManager;
import com.zel.manager.nature.NatureRelationManager;
import com.zel.manager.nature.TermNatureManager;

public class MathUtil {

	// 平滑参数
	private static final double dSmoothingPara = 0.1;
	// 一个参数
	private static final int MAX_FREQUENCE = 2079997;// 7528283+329805;
	// ﻿Two linked Words frequency
	private static final double dTemp = (double) 1 / MAX_FREQUENCE;

	/**
	 * 从一个词的词性到另一个词的词的分数
	 * 
	 * @param form
	 *            前面的词
	 * @param to
	 *            后面的词
	 * @return 分数
	 */
	public static double calcScore(TermUnit fromTerm, TermUnit toTerm) {
		double frequency = TermNatureManager.getTermUnitFreq(fromTerm) + 1;
		// double frequency = 1;
//		System.out.println(fromTerm.getValue() + " freq***" + frequency);
		if (frequency < 0) {
			return fromTerm.getScore() + MAX_FREQUENCE;
		}
		int freq = BiGramManager.getRelationFreq(fromTerm, toTerm);
		// System.out.println("term relation freq---" + freq);
		
		double value = -Math.log(dSmoothingPara * frequency
				/ (MAX_FREQUENCE + 80000) + (1 - dSmoothingPara)
				* ((1 - dTemp) * freq / frequency + dTemp));
		
		// double value = -Math.log(dSmoothingPara
		// / (MAX_FREQUENCE + 80000) + (1 - dSmoothingPara)
		// * ((1 - dTemp) * freq + dTemp));

		// System.out.println("value*****"+value);
		// if (value < 0)
		// value += frequency;
		//
		if (value < 0) {
			value += frequency;
		}
		return fromTerm.getScore() + value;
	}
	
	/**
	 * 两个词性之间的分数计算
	 * @param from
	 * @param to
	 * @return
	 */
	public static double calTwoNatureRelationFreq(NatureTermUnit from, NatureTermUnit to) {
		double twoNatureFreq = NatureRelationManager.getTwoNatureRelationFreq(from.getTermNatureItem().getNatureItem(),to.getTermNatureItem().getNatureItem());
		if (twoNatureFreq == 0) {
			twoNatureFreq = Math.log(from.selfScore + to.selfScore);
		}
		double score = from.score + Math.log((from.selfScore + to.selfScore) * twoNatureFreq) + to.selfScore;
		return score;
	}
}
