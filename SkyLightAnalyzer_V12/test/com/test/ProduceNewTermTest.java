package com.test;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import com.zel.entity.Branch;
import com.zel.entity.WordPojo;
import com.zel.manager.LibraryManager;
import com.zel.util.TimeUtil;

/**
 * 词典相关的单元测试
 * 
 * @author zel
 * 
 */
public class ProduceNewTermTest extends TestCase {
	public void testForestMaxIndexValue() {
		long begin = System.currentTimeMillis();
		LibraryManager.makeTrie();
		long end = System.currentTimeMillis();
		System.out
				.println("加载词典共用时" + TimeUtil.getMinuteAndSecond(end - begin));

		Branch[] base_array = LibraryManager.forest.base_array;
		System.out.println("base_array.length---" + base_array.length);
		int count = 0;
		for (int i = base_array.length - 1; i > 0; i--) {
			if (base_array[i] != null) {
				// System.out.println("最大可用的i为---" + i);
				count++;
				// break;
			}
		}
		System.out.println("实际占用***" + count);
	}

	public void testInsertWord() throws Exception {
		LibraryManager.makeTrie();
		// WordPojo pojo=new WordPojo("的确",null);
		// WordPojo pojo1 = new WordPojo("淘宝");
		// WordPojo pojo1 = new WordPojo("淘宝","2","{nr=123}");
		// WordPojo pojo1 = new WordPojo("大学城");
		// WordPojo pojo2 = new WordPojo("中国");
		// WordPojo pojo2 = new WordPojo("令人恶心");
		// WordPojo pojo2 = new WordPojo("离乡背井");
		// WordPojo pojo2 = new WordPojo("大学生");
//		WordPojo pojo2 = new WordPojo("淘宝","{v=123}");
		WordPojo pojo2 = new WordPojo("变脏");
//		变髒
		// WordPojo pojo2 = new WordPojo("确实", null);
		// WordPojo pojo3 = new WordPojo("在理", null);

		List<WordPojo> list = new LinkedList<WordPojo>();
		// list.add(pojo1);
		list.add(pojo2);
		// list.add(pojo3);

		for (WordPojo pojo : list) {
			LibraryManager.insertNewWordItem(pojo);
		}
	}

	public void testRemoveWord() {
		LibraryManager.makeTrie();
		// WordPojo pojo1 = new WordPojo("结婚", null);
		// WordPojo pojo1 = new WordPojo("和尚");
		// WordPojo pojo2 = new WordPojo("敲钟");
//		WordPojo pojo1 = new WordPojo("周天亮");
		WordPojo pojo1 = new WordPojo("淘宝");
		// WordPojo pojo2 = new WordPojo("网店");

		List<WordPojo> list = new LinkedList<WordPojo>();
		list.add(pojo1);
		// list.add(pojo2);

		for (WordPojo pojo : list) {
			LibraryManager.removeWordItem(pojo);
		}
	}

}
