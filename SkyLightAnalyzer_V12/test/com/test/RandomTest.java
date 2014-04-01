package com.test;

import java.util.TreeSet;

import com.zel.util.io.IOUtil;

public class RandomTest {
	public void testFileFilter() {
		// String srcPath = "resource/dic/user/user.dic";
		String srcPath = "resource/dic/system/system.dic";
		// String destPath = "resource/dic/user/user2.dic";
		String destPath = "resource/dic/system/s2.dic";

		String dic_source = IOUtil.readFile(srcPath, "utf-8");
		System.out.println("***" + dic_source);
		IOUtil.writeFile(destPath, dic_source);
	}

	public static void main(String[] args) {
	}
}
