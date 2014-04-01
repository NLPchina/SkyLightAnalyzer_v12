package com.zel.util;

import java.util.List;

public class CollectionUtil {
	public static void printCollection(List list) {
		if (list == null || list.size() == 0) {
			System.err.println("所输出集合为空!");
		} else {
			for (Object obj : list) {
				System.out.println(obj);
			}

		}
	}
}
