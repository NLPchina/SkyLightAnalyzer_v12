package com.test;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zel.core.analyzer.StandardAnalyzer;
import com.zel.entity.TermUnit;
import com.zel.entity.WordPojo;
import com.zel.manager.LibraryManager;
import com.zel.util.TimeUtil;

/**
 * 分词器性能测试类
 * 
 * @author zel
 * 
 */
public class PerfomanceTest {
	public static void main(String[] args) {
		long begin = 0;
		long end = 0;

		StandardAnalyzer analyzer = new StandardAnalyzer();

		Map<String, List<WordPojo>> wordMap = LibraryManager.wordGroupMap;
		List<TermUnit> list = null;

		int length = 0;
		int repeat = 10*10000;
		int i = 0;
		begin = System.currentTimeMillis();

		List<String> strList = new LinkedList<String>();

		strList.add("长春市长春节讲话");
		strList.add("独立自主和平等互利的原则");
		strList.add("计算机网络管理员用虚拟机实现了手机游戏下载和开源项目的管理金山毒霸");
		strList.add("天天向上生機勃發,很可愛,犯賤的人是沒有辦法的");
		
		while (i < repeat) {
			for (String temp : strList) {
				// if (temp == null) {
				// continue;
				// }
				length += temp.length();
				// list = analyzer.getSplitResult(temp);
				// list = analyzer.getSplitPOSResult(temp);
				list = analyzer.getSplitMergePOSResult(temp);
				// if (list != null) {
				// for (TermUnit term : list) {
				// System.out.print(term.getValue()
				// + "/"
				// + term.getNatureTermUnit().getTermNatureItem()
				// .getNatureItem().getName() + ",");
				// }
				// }
				// System.out.println();
			}
			i++;
		}

		end = System.currentTimeMillis();
		System.out.println("分词共用时" + TimeUtil.getMinuteAndSecond(end - begin));
		System.out.println("分词速率为: "
				+ TimeUtil.getSplitSpeed(length, end - begin) + " 字/秒");
		System.out.println("content length----" + length);
	}
}
