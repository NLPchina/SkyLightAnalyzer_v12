package com.zel.util;

import com.zel.util.io.ReadConfigUtil;

public class SystemParas {
	// public static ReadConfigUtil readConfigUtil=new
	// ReadConfigUtil("resource/config.properties");
	public static ReadConfigUtil readConfigUtil = new ReadConfigUtil(
			"config.properties", true);
	// 初始化双数组trie树时的数组大小为最大的Charactor*的倍数，该值即伙倍数部分
	public static int double_trie_init_max_array_size_multi = Integer
			.parseInt(readConfigUtil
					.getValue("double_trie_init_max_array_size_multi"));

	/**
	 * trie树中的最大节点数目,*2是要将前一半分给第一个char,后一半用做非第一个字的存储，
	 * 这样可以避免因为第一个char的问题去反复定位每个字的index, 现在直接将char的int值做为该char的数组中的index值
	 */
	// public static int max_branch_array_length = Character.MAX_VALUE * 12*2;
	public static int max_branch_array_length = Character.MAX_VALUE
			* SystemParas.double_trie_init_max_array_size_multi;
	// 遇到冲突后要对base值进行加10操作,循环判断直到所有的keyword中的char都能唯一放下
	public static int conflict_add_number = 5;
	/**
	 * 刚开始时候第一个base的值,即base[index]=base_init_value
	 * 从max_branch_array_length开始时,是为了排除与首字符的冲突
	 */
	public static int base_init_value = Character.MAX_VALUE;
	// 刚开始时候第一个check的值,即check[index]=check_init_value
	/**
	 * 由于以对象为操作单位，故将此处的check数组去掉,在base数组的对象中加了frontPosition属性
	 */
	// public static int check_init_value = 0;

	/**
	 * 读取配置文件的参数
	 */
	public static String dic_system_path = readConfigUtil
			.getValue("dic.system.path");// 读取系统核心词典文件所在文件路径

	public static String dic_user_path = readConfigUtil
			.getValue("dic.user.path");// 读取用户核心词典文件所在文件路径

	// 读取情感词典的路径
	public static String dic_system_positive_path = readConfigUtil
			.getValue("dic.system.positive.path");// 积极性词典
	public static String dic_system_negative_path = readConfigUtil
			.getValue("dic.system.negative.path");// 消极性词典

	// 自定义用户词典--常用临时词--词性为l
	public static String dic_user_self_path = readConfigUtil
			.getValue("dic.user.self.path");// 消极性词典

	public static String dic_bigram_path = readConfigUtil
			.getValue("dic.bigram.path");// 读词典文件所在文件路径

	public static String cache_trie_path = readConfigUtil
			.getValue("cache.trie.path");// 缓存Trie树结构
	public static String cache_word_all_items_path = readConfigUtil
			.getValue("cache.word.all.items.path");// 缓存Trie树结构

	/**
	 * 读取繁简体的转换词典
	 */
	public static String dic_system_translation_path = readConfigUtil
			.getValue("dic.system.translation.path");// 繁简转换词典路径
	public static String cache_translation_path = readConfigUtil
			.getValue("cache.translation.path");// 缓存繁简转换的数据

	/**
	 * 为了测试方便，配置下是否启用cache trie
	 */
	public static boolean cache_trie_enable = Boolean
			.parseBoolean(readConfigUtil.getValue("cache.trie.enable"));
	public static boolean cache_word_all_items_enable = Boolean
			.parseBoolean(readConfigUtil
					.getValue("cache.word.all.items.enable"));

	/**
	 * 为了测试方便,是否缓存bi-gram关联词典
	 */
	public static boolean cache_bigram_relation_enable = Boolean
			.parseBoolean(readConfigUtil
					.getValue("cache.bigram.relation.enable"));
	public static String cache_bigram_relation_path = readConfigUtil
			.getValue("cache.bigram.relation.path");// 缓存的bi-gram关系词典的路径

	public static String cache_term_nature_relation_path = readConfigUtil
			.getValue("cache.term.nature.relation.path");// 缓存的词条与词性对应关系文件的路径

	/**
	 * 20M大小的一个字节数组,在加载缓存文件时，用字节数组的方式，一次性读取进内存，大大加块了IO速度,不再是FileInputStream去直接读取
	 */
	public static int cache_trie_init_load_bytes_length = Integer
			.parseInt(readConfigUtil
					.getValue("cache.trie.init.load.bytes.length"));

	/**
	 * 日志管理
	 */
	public static boolean log_output_enable = Boolean
			.parseBoolean(readConfigUtil.getValue("log_output_enable"));
	// 判断命令行是否开启,主要是为了命令行测试
	public static boolean console_input_enable = Boolean
			.parseBoolean(readConfigUtil.getValue("console_input_enable"));

	// 判断命令行的编码
	public static String console_input_encoding = readConfigUtil
			.getValue("console_input_encoding");

	/**
	 * 关于hadoop使用时的配置
	 */
	// 定义分词是否是使用在hadoop等这样的分布式环境中，如此的话，需要先copy资源到hdfs等上，作分发使用
	public static boolean is_hadoop_use_analyzer = Boolean
			.parseBoolean(readConfigUtil.getValue("is_hadoop_use_analyzer"));
	// 系统在操作需要上传到hdfs上的操作的根路径,然后再加上在配置文件中配置的传统使用的时候的路径，而构成了全路径
	public static String hadoop_hdfs_root_path = readConfigUtil
			.getValue("hadoop_hdfs_root_path");

}
