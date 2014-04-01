package com.zel.manager;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.zel.core.maketrie.Library;
import com.zel.entity.BiGramItem;
import com.zel.entity.Forest;
import com.zel.entity.WordPojo;
import com.zel.entity.enums.InsertWordStatus;
import com.zel.entity.enums.RemoveWordStatus;
import com.zel.entity.nature.TermUnitNatureListPojo;
import com.zel.manager.nature.TermNatureManager;
import com.zel.util.StaticValue;
import com.zel.util.SystemParas;
import com.zel.util.io.IOUtil;
import com.zel.util.io.ObjectAndByteArrayConvertUtil;
import com.zel.util.io.ObjectIoUtil;
import com.zel.util.log.MyLogger;

public class LibraryManager {
	// 做日志用
	private static MyLogger logger = new MyLogger(LibraryManager.class);
	public static Forest forest = new Forest();

	public static List<WordPojo> wordList = new LinkedList<WordPojo>();// 存放所有的word及其后的参数
	// 用来存放将词典中的所有word分组后的结果
	public static Map<String, List<WordPojo>> wordGroupMap = new HashMap<String, List<WordPojo>>();

	/**
	 * 将词典中的每行转换成对应的WordPojo,并加入到WordList集合中
	 * 
	 * @param dic_path
	 * @param encoding
	 * @return
	 */
	public static List<WordPojo> getWordList4SystemDic(String dic_path,
			String encoding) {
		// 先加载系统词典,由tab制表符隔开的若干数据单位
		String dic_source = IOUtil.readFile(dic_path, encoding);
		// String dic_source = IOUtil.readFile(dic_path, encoding, true);

		BufferedReader br = new BufferedReader(new StringReader(dic_source));// 通过StringReader来读取词典
		String line = null;
		String items[] = null;// 存储每条dic item的param
		// String[] parasArray = null;// 暂存每个词条后边储存的参数列表，包括权重、词性等.
		try {
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0) {
					line = line.trim();// 先去下空白字符,以免影响参数的构成
					items = line.split("\t");// 用制表符/t去分开每个词条的参数列表

					// 适应不同的词典,单列和3列的
					switch (items.length) {
					case 1: // 有1列
						wordList.add(new WordPojo(ComplexToSimpleManager
								.getSimpleFont(items[0])));
						break;
					case 3:// 有3列
						wordList.add(new WordPojo(ComplexToSimpleManager
								.getSimpleFont(items[0]), items[1], items[2]));
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("得到wordList时出现错误!");
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.info("关闭wordList的流时出现错误!");
			}
		}
		return wordList;
	}

	public static List<WordPojo> getWordList4UserDic(String dic_path,
			String encoding) {
		// 先加载系统词典,由tab制表符隔开的若干数据单位,允许加载目录
		// String dic_source = IOUtil.readFile(dic_path, encoding);
		String dic_source = IOUtil.readDirOrFile(dic_path, encoding);

		BufferedReader br = new BufferedReader(new StringReader(dic_source));// 通过StringReader来读取词典
		String line = null;
		String items[] = null;// 存储每条dic item的param
		// String[] parasArray = null;// 暂存每个词条后边储存的参数列表，包括权重、词性等.
		try {
			while ((line = br.readLine()) != null) {
				if (line.trim().length() > 0) {
					line = line.trim();// 先去下空白字符,以免影响参数的构成
					items = line.split("\t");// 用制表符/t去分开每个词条的参数列表

					// 适应不同的词典,单列和3列的
					switch (items.length) {
					case 1: // 有1列
						wordList.add(new WordPojo(ComplexToSimpleManager
								.getSimpleFont(items[0])));
						break;
					case 3:// 有3列
						wordList.add(new WordPojo(ComplexToSimpleManager
								.getSimpleFont(items[0]), "{" + items[1].trim()
								+ "=" + items[2].trim() + "}"));
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("得到wordList时出现错误!");
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				logger.info("关闭wordList的流时出现错误!");
			}
		}
		return wordList;
	}

	// 此时的dic_path将多个词典文件用空格格开就可以
	public static void loadLibrary(String dic_path, String encoding,
			boolean isSystemDic) {
		String[] dic_path_array = dic_path.split(" ");
		if (isSystemDic) {// 来自系统词典
			for (String temp_dic_file : dic_path_array) {
				getWordList4SystemDic(temp_dic_file, encoding);
			}
		} else {// 来自用户自定义词典，与上种情况的词典格式不同
			for (String temp_dic_file : dic_path_array) {
				getWordList4UserDic(temp_dic_file, encoding);
			}
		}

	}

	// 对已排序好的进行分组操作，用map来存储，每个key存放的是该词的首个char
	public static void getGroupMap() {
		WordPojo temp_pojo = null;
		String key = null;
		List<WordPojo> oneGroupList = null;
		Iterator<WordPojo> wordPojoIter = wordList.iterator();

		while (wordPojoIter.hasNext()) {
			// temp_pojo = wordList.get(0);
			temp_pojo = wordPojoIter.next();
			key = "" + temp_pojo.getWord().charAt(0);
			// 将得到的每个wordPojo对象放到wordGroupMap中去
			if (wordGroupMap.containsKey(key)) {
				wordGroupMap.get(key).add(temp_pojo);
			} else {
				// oneGroupList = new ArrayList<WordPojo>();
				oneGroupList = new LinkedList<WordPojo>();
				oneGroupList.add(temp_pojo);
				wordGroupMap.put(key, oneGroupList);
			}
			// wordList.remove(0);
		}
		wordList.clear();
	}

	public static void makeTrie() {
		/**
		 * 第一步要进行简繁体的转换字典,有缓存的繁简转换器则加载，没有则直接初始化
		 */
		if (new File(SystemParas.cache_translation_path).exists()) {
			ComplexToSimpleManager.complexToSimpleArray = (char[]) ObjectIoUtil
					.readObject(SystemParas.cache_translation_path);
			logger.info("成功--加载缓存的简繁关联词典!");
		} else {
			ComplexToSimpleManager.init();// 重新load到bi-gram关联词典
			/**
			 * 缓存一份
			 */
			ObjectIoUtil.writeObject(SystemParas.cache_translation_path,
					ComplexToSimpleManager.complexToSimpleArray);
			logger.info("成功缓存简繁关联词典!");
		}

		/**
		 * 缓存或重构wordGroupMap
		 */
		if (SystemParas.cache_word_all_items_enable
				&& new File(SystemParas.cache_word_all_items_path).exists()) {
			LibraryManager.wordGroupMap = (Map<String, List<WordPojo>>) ObjectIoUtil
					.readObject(SystemParas.cache_word_all_items_path);
			logger.info("成功--加载词典库文件!");
		} else {
			loadLibrary(SystemParas.dic_system_path,
					StaticValue.System_Encoding, true);// 加载系统词典文件组,用空格隔开的多个文件即可

			// 统一加载用户自定义词典,不再具体分哪一类的用户词典
			loadLibrary(SystemParas.dic_user_path, StaticValue.System_Encoding,
					false);
			// loadLibrary(SystemParas.dic_user_path,
			// StaticValue.System_Encoding,
			// false);
			// loadLibrary(SystemParas.dic_user_self_path,
			// StaticValue.System_Encoding, false);// 用户自定义词典
			// 加载系统词典文件组,用空格隔开的多个文件即可
			// 加载情感词典
			loadLibrary(SystemParas.dic_system_positive_path,
					StaticValue.System_Encoding, true);
			loadLibrary(SystemParas.dic_system_negative_path,
					StaticValue.System_Encoding, true);

			getGroupMap();// 对所有的wordPojo按首字符分组
			/**
			 * 备份一下最新的wordGroupMap，即最大词条的map集合对象
			 */
			if (SystemParas.cache_word_all_items_enable) {
				ObjectIoUtil.writeObject(SystemParas.cache_word_all_items_path,
						wordGroupMap);
				logger.info("成功--缓存wordGroupMap树!");
			}
		}
		/**
		 * 加载或重构Trie树,由于NatureManager中的词条词性对应关系，依赖于Trie树的构建，故其是否缓存也和Trie树保持一致
		 */
		if (SystemParas.cache_trie_enable
				&& new File(SystemParas.cache_trie_path).exists()) {
			LibraryManager.forest = (Forest) ObjectIoUtil
					.readObject(SystemParas.cache_trie_path);

			TermNatureManager.termUnitNatureList = (TermUnitNatureListPojo[]) ObjectIoUtil
					.readObject(SystemParas.cache_term_nature_relation_path);

			logger.info("成功--加载缓存的Trie树和词性对应表!");
		} else {
			Library.forest = LibraryManager.forest;
			Library.makeTrie(forest, wordGroupMap);// 构造trie树
			/**
			 * 构造完成后先缓存一份
			 */
			if (SystemParas.cache_trie_enable) {
				ObjectIoUtil.writeObject(SystemParas.cache_trie_path, forest);
				ObjectIoUtil.writeObject(
						SystemParas.cache_term_nature_relation_path,
						TermNatureManager.termUnitNatureList);
				logger.info("成功缓存Trie树和词性对应表!");
			}
		}

		/**
		 * 缓存中加载或直接重新load文件中的bi-gram关联词典
		 */
		if (SystemParas.cache_bigram_relation_enable
				&& new File(SystemParas.cache_bigram_relation_path).exists()) {
			BiGramManager.biGramMatrix = (BiGramItem[][]) ObjectIoUtil
					.readObject(SystemParas.cache_bigram_relation_path);
			logger.info("成功--加载缓存的bi-gram关联词典!");
		} else {
			BiGramManager.init();// 重新load到bi-gram关联词典
			/**
			 * 构造完成后先缓存一份
			 */
			if (SystemParas.cache_bigram_relation_enable) {
				ObjectIoUtil.writeObject(
						SystemParas.cache_bigram_relation_path,
						BiGramManager.biGramMatrix);
				logger.info("成功缓存bi-gram关联词典!");
			}
		}
	}

	public static void makeTrie_distributed(byte[] complexToSimpleArray,
			byte[] wordGroupMapArray, byte[] trieForestArray,
			byte[] termNatureArray, byte[] biGramMatrixArray) {
		if (!SystemParas.is_hadoop_use_analyzer) {
			logger.info("不是分布式环境，不需要调用 distributed_init方法!");
			return;
		}
		// 繁简转换文件
		ComplexToSimpleManager.complexToSimpleArray = (char[]) ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(complexToSimpleArray);

		// wordGroupMap 文件的加载
		LibraryManager.wordGroupMap = (Map<String, List<WordPojo>>) ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(wordGroupMapArray);

		// trie.dat
		LibraryManager.forest = (Forest) ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(trieForestArray);

		// 词性文件.dat
		TermNatureManager.termUnitNatureList = (TermUnitNatureListPojo[]) ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(termNatureArray);

		// 加载消歧时的文件biGramMatrixArray
		BiGramManager.biGramMatrix = (BiGramItem[][]) ObjectAndByteArrayConvertUtil
				.ByteArrayToObject(biGramMatrixArray);
	}

	/**
	 * 向已构建完成的trie树中，添加新词或是用户自定义词
	 * 
	 * @param wordPojo
	 */
	public static void insertNewWordItem(WordPojo wordPojo) {
		InsertWordStatus insertWordStatus = Library.insertSingleWordItem(
				LibraryManager.forest, LibraryManager.wordGroupMap, wordPojo);// 插入一个新词条
		/**
		 * 构造完成后先缓存一份Trie和wordGroupMap
		 */
		if (SystemParas.cache_trie_enable
				&& insertWordStatus == InsertWordStatus.Valid) {
			ObjectIoUtil.writeObject(SystemParas.cache_trie_path,
					LibraryManager.forest);
			ObjectIoUtil.writeObject(
					SystemParas.cache_term_nature_relation_path,
					TermNatureManager.termUnitNatureList);
			logger.info("成功缓存Trie树和词性对应表!");
		}
		/**
		 * 缓存一份wordPojo
		 */
		if (SystemParas.cache_word_all_items_enable
				&& insertWordStatus == InsertWordStatus.Valid) {
			ObjectIoUtil.writeObject(SystemParas.cache_word_all_items_path,
					LibraryManager.wordGroupMap);
			logger.info("成功缓存wordGroupMap文件中!");
		}

		/**
		 * 缓存一份bi-gram对应的词典
		 */
		if (SystemParas.cache_bigram_relation_enable
				&& insertWordStatus == InsertWordStatus.Valid) {
			BiGramManager.init();// 重新load到bi-gram关联词典
			ObjectIoUtil.writeObject(SystemParas.cache_bigram_relation_path,
					BiGramManager.biGramMatrix);
			logger.info("成功缓存bi-gram关联词典!");
		}
	}

	public static void removeWordItem(WordPojo wordPojo) {
		RemoveWordStatus removeWordStatus = Library.removeSingleWordItem(
				forest, wordGroupMap, wordPojo);// 插入一个新词条
		/**
		 * 构造完成后先缓存一份Trie和wordGroupMap
		 */
		if (SystemParas.cache_trie_enable
				&& removeWordStatus == RemoveWordStatus.Exist) {
			ObjectIoUtil.writeObject(SystemParas.cache_trie_path, forest);
			ObjectIoUtil.writeObject(
					SystemParas.cache_term_nature_relation_path,
					TermNatureManager.termUnitNatureList);
			logger.info("成功缓存Trie树和词性对应表!");
		}
		if (SystemParas.cache_word_all_items_enable
				&& removeWordStatus == RemoveWordStatus.Exist) {
			ObjectIoUtil.writeObject(SystemParas.cache_word_all_items_path,
					LibraryManager.wordGroupMap);
			logger.info("成功缓存wordGroupMap文件中!");
		}
		/**
		 * 缓存一份bi-gram对应的词典
		 */
		if (SystemParas.cache_bigram_relation_enable
				&& removeWordStatus == RemoveWordStatus.Exist) {
			BiGramManager.init();// 重新load到bi-gram关联词典
			ObjectIoUtil.writeObject(SystemParas.cache_bigram_relation_path,
					BiGramManager.biGramMatrix);
			logger.info("成功缓存bi-gram关联词典!");
		}

	}
}
