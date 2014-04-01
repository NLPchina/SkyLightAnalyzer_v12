package com.zel.entity.enums;

/**
 * 成词类型的枚举类,包括数值型，字母型，符号型，汉字型(又分为1，2,成词与成词可持续型状态)
 * 
 * @author zel
 */
public enum WordPojoType {
	/**
	 * NUMBER: 数值型， ALPHA: 字母型， SIGN:符号型, END：对应汉字成词的1, CONTINUE:对应汉字成词的2
	 * 并与system.dic中的第二列相对应
	 */
	NUMBER, ALPHA, SIGN, END, CONTINUE;
}