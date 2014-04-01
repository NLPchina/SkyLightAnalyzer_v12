package com.zel.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 将一个map无序集合，转化成list的有序集合 k,v为范型
 * 
 * @author zel
 * 
 * @param <K>
 * @param <V>
 */
public class MapToSortListUtil<K, V> {

	public Comparator<Map.Entry<K, List<V>>> defaultComparator = new Comparator<Map.Entry<K, List<V>>>() {
		public int compare(Map.Entry<K, List<V>> mapping1,
				Map.Entry<K, List<V>> mapping2) {
			return mapping1.getValue().size() > mapping2.getValue().size() ? 0
					: 1;
		}
	};

	public Comparator<Map.Entry<K, V>> defaultComparator4NotListValue = new Comparator<Map.Entry<K, V>>() {
		public int compare(Map.Entry<K, V> mapping1, Map.Entry<K, V> mapping2) {
			Comparable comp1 = (Comparable) mapping1.getValue();
			Comparable comp2 = (Comparable) mapping2.getValue();
			return comp2.compareTo(comp1);
		}
	};

	public List<Map.Entry<K, List<V>>> mapToSortList(Map<K, List<V>> map,
			Comparator<Map.Entry<K, List<V>>> comparator) {

		List<Map.Entry<K, List<V>>> mapToSortList = new ArrayList<Map.Entry<K, List<V>>>(
				map.entrySet());

		Collections.sort(mapToSortList, comparator);

		return mapToSortList;
	}

	public List<Map.Entry<K, V>> mapToSortList4NotListValue(Map<K, V> map,
			Comparator<Map.Entry<K, V>> comparator) {
		List<Map.Entry<K, V>> mapToSortList = new ArrayList<Map.Entry<K, V>>(
				map.entrySet());
		Collections.sort(mapToSortList, comparator);

		return mapToSortList;
	}

	public Comparator<Map.Entry<K, List<V>>> getDefaultComparator() {
		return defaultComparator;
	}

	public static void main(String[] args) {
		MapToSortListUtil<String, Integer> mapTranUtil = new MapToSortListUtil<String, Integer>();

		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("one",1);
		map.put("two",2);
		map.put("three",1);
		map.put("three",3);	
		
		List<Map.Entry<String, Integer>> sortList = mapTranUtil
				.mapToSortList4NotListValue(map,mapTranUtil.defaultComparator4NotListValue);

		for (Map.Entry entry : sortList) {
			System.out.println(entry);
		}
	}
}
