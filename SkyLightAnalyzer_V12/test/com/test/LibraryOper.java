package com.test;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.zel.util.io.IOUtil;

public class LibraryOper {
	public static void createNewWordLib() throws Exception {
		String str = IOUtil.readFile("d://test.txt", "utf-8");
		StringReader sr = new StringReader(str);
		BufferedReader br = new BufferedReader(sr);

		String temp_line = null;
		StringBuilder sb = new StringBuilder();
		while (((temp_line = br.readLine()) != null)) {
			if (!temp_line.isEmpty()) {
				// System.out.println("http://www.weibo.com/"+temp_line);
				// sb.append("http://www.weibo.com/" + temp_line + "\n");
				sb.append(temp_line + "\t" + "l" + "\t" + "2000" + "\n");
			}
		}

		IOUtil.writeFile("d://result.txt", sb.toString());
		br.close();
	}

	public static void main(String[] args) throws Exception {
		String str = IOUtil.readFile("d://topDomain.txt", "utf-8");
		StringReader sr = new StringReader(str);
		BufferedReader br = new BufferedReader(sr);

		String temp_line = null;
		StringBuilder sb = new StringBuilder();

		Set<String> hs = new HashSet<String>();
		int count = 0;
		while (((temp_line = br.readLine()) != null)) {

			if (!temp_line.isEmpty()) {
				hs.add(temp_line);
			}
			count++;
		}

		Iterator<String> iter = hs.iterator();
		while (iter.hasNext()) {
			sb.append(iter.next().trim() + "\t" + "E" + "\n");
		}

		br.close();
		IOUtil.writeFile("d://result.txt", sb.toString());

		System.out.println("一共--" + count);
		System.out.println("hash set size--" + hs.size());
	}
}
