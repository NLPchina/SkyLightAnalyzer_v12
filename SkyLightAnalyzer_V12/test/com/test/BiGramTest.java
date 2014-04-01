package com.test;

import junit.framework.TestCase;

import com.zel.manager.LibraryManager;
import com.zel.util.TimeUtil;

public class BiGramTest extends TestCase {
	public void testGetFreq() {
		long begin = System.currentTimeMillis();
		LibraryManager.makeTrie();
		long end = System.currentTimeMillis();
		System.out
				.println("加载词典共用时" + TimeUtil.getMinuteAndSecond(end - begin));
	}
}
