package com.zel.core.maketrie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zel.entity.Branch;
import com.zel.entity.Forest;
import com.zel.entity.WordPojo;
import com.zel.entity.enums.InsertWordStatus;
import com.zel.entity.enums.RemoveWordStatus;
import com.zel.util.MapToSortListUtil;
import com.zel.util.SystemParas;
import com.zel.util.log.MyLogger;

public class Library {
	// 做日志用
	private static MyLogger logger = new MyLogger(Library.class);

	public static Forest forest = null;// 由于Obejct序列化必须是非静态字段，故将Forest中的base、check都改为非静态，特在此声明一下

	public static int index = 0;
	public static int word_length = 0;
	public static int word_index = 0;

	public static int temp_index = 0;
	public static int temp_frontPosition = 0;
	// public static List<WordPojo> delList = new ArrayList<WordPojo>();//
	// 暂存待删除集合
	public static List<WordPojo> delList = new LinkedList<WordPojo>();// 暂存待删除集合

	// 真正开始构造trie树
	public static void makeTrie(Forest forest,
			Map<String, List<WordPojo>> wordGroupMap) {
		// 对wordGroupMap进行按value.size()的方式的由高到低排序
		MapToSortListUtil<String, WordPojo> mapToSortListUtil = new MapToSortListUtil<String, WordPojo>();
		List<Map.Entry<String, List<WordPojo>>> mappingWordGroupList = mapToSortListUtil
				.mapToSortList(wordGroupMap, mapToSortListUtil
						.getDefaultComparator());
		int count = mappingWordGroupList.size();

		logger.info("有key共" + count);
		count = 1;
		List<WordPojo> word_list_copy = null;// 对组中每个group进行一个复制

		for (Map.Entry<String, List<WordPojo>> entry_temp : mappingWordGroupList) {// 每个key都是一组以该key为首字符的一个word集合
			// word_list_copy = new ArrayList<WordPojo>(entry_temp.getValue());
			word_list_copy = new LinkedList<WordPojo>(entry_temp.getValue());
			insertWordGroup(forest, entry_temp.getKey(), word_list_copy, 0);// 以组为单位插入
			logger.info("已构建完第---" + (count++));
		}
	}

	public static String sub_key = null;// 暂存每个临时key
	public static List<WordPojo> oneGroupList = null;// 暂存每个组的子集合
	public static char char_temp = 0;// 暂存每次有多个charAt操作的部分

	// 以组为单位构造trie
	// 此处的postion指定位到word的哪个第几个位置
	public static void insertWordGroup(Forest forest, String key,
			List<WordPojo> word_list_copy, int position) {
		Map<String, List<WordPojo>> subWordGroupMap = new HashMap<String, List<WordPojo>>();// 暂存每个小组集合的map对象
		int temp_first_position_value = 0;// 暂存key对应的位置上的value值
		int temp_first_position_value_bak = 0;// 暂存key对应的位置上的value值
		// 将new_word_list拆分成以position不同，依次递近的小word_list数组
		for (WordPojo temp_pojo : word_list_copy) {
			// 由于处理完的串已经在集中去掉了，故不在此做判断了
			// if (temp_pojo.getWord().length() >= position + 1) {
			sub_key = temp_pojo.getWord().substring(0, position + 1);// 取到每个键
			// 将得到的每个wordPojo对象放到wordGroupMap中去
			if (subWordGroupMap.containsKey(sub_key)) {
				subWordGroupMap.get(sub_key).add(temp_pojo);
			} else {
				oneGroupList = new LinkedList<WordPojo>();
				oneGroupList.add(temp_pojo);
				subWordGroupMap.put(sub_key, oneGroupList);
			}
			// }
		}
		// 将数组拆分结束
		// 遍历subWordGroupMap里边的每个List，依次构造进trie树中
		Set<String> sub_keys = subWordGroupMap.keySet();
		for (String temp_key : sub_keys) {// 每个key都是一组以该key为首字符的一个word集合
			List<WordPojo> sub_word_list = subWordGroupMap.get(temp_key);
			// 每个小组的处理开始
			boolean isFound_Base_Value = false;
			int temp_base_init_value = SystemParas.base_init_value;
			/**
			 * 对每个要插入的集合做最开始的base_value的值的计算
			 */
			while (!isFound_Base_Value) {
				for (WordPojo pojo : sub_word_list) {
					if (pojo.getWord().length() >= position + 2) {
						if (!forest.isNull(temp_base_init_value, pojo.getWord()
								.charAt(position + 1))) {
							isFound_Base_Value = false;
							break;
						} else {
							isFound_Base_Value = true;
						}
					} else {
						isFound_Base_Value = true;
					}
				}
				if (isFound_Base_Value) {
					break;
				} else {
					temp_base_init_value = temp_base_init_value
							+ SystemParas.conflict_add_number;
				}
			}

			/**
			 * 对前边的while循环占位成功的位置，要预置占位对象，以防因分组导致的先后顺序使位置错乱，当然只需对后边还有字符的串占位，
			 * 无有的不做操作
			 */
			String temp_word_str = null;
			for (WordPojo pojo : sub_word_list) {
				temp_word_str = pojo.getWord();
				if (temp_word_str.length() >= position + 2) {
					forest.setPlaceHoldValue(temp_base_init_value,
							temp_word_str.charAt(position + 1));
				}
			}

			// 如果是首字符则直接插入根branch中即可
			if (position == 0) {
				for (WordPojo pojo : sub_word_list) {
					char_temp = key.charAt(position);
					if (pojo.getWord().length() == position + 1) {
						forest.insertBranch(new Branch(char_temp, char_temp,
								position, temp_base_init_value, 1, pojo
										.getTermNatureList(), pojo
										.getWordType()));
						delList.add(pojo);// 如果等于1，即为一个字直接去掉，其余部分进入下一轮循环
					} else {
						forest.insertBranch(new Branch(char_temp, char_temp,
								position, temp_base_init_value, 0, null));
					}
				}
			} else {
				temp_first_position_value_bak = temp_first_position_value = key
						.charAt(0);
				// 此步最重要为如何得前一个位置frontPosition
				for (WordPojo pojo : sub_word_list) {
					for (temp_index = 0; temp_index < position; temp_index++) {
						if (position == 1) {
							temp_frontPosition = temp_first_position_value_bak;
							break;
						} else {
							if (temp_index >= 1) {
								temp_frontPosition = forest.base_array[temp_first_position_value]
										.getBase_value()
										+ pojo.getWord().charAt(temp_index);
								temp_first_position_value = temp_frontPosition;
							}
						}
					}
					if (pojo.getWord().length() == position + 1) {
						char_temp = pojo.getWord().charAt(position);
						forest.insertBranch(new Branch(char_temp,
								forest.base_array[temp_frontPosition]
										.getBase_value()
										+ char_temp, temp_frontPosition,
								temp_base_init_value, 1, pojo
										.getTermNatureList(), pojo
										.getWordType()));
						delList.add(pojo);// 如果等于1，即为一个字直接去掉，其余部分进入下一轮循环
					} else {
						char_temp = pojo.getWord().charAt(position);
						forest.insertBranch(new Branch(char_temp,
								forest.base_array[temp_frontPosition]
										.getBase_value()
										+ char_temp, temp_frontPosition,
								temp_base_init_value, 0, null));
					}
					// 清空临时变量
					temp_first_position_value = temp_first_position_value_bak;
				}
			}
			word_list_copy.removeAll(delList);
			sub_word_list.removeAll(delList);
			delList.clear();
			// 每个小组的处理完一遍

			// 在此做是否对每一个子集合做递归操作
			if (!sub_word_list.isEmpty()) {
				insertWordGroup(forest, key, sub_word_list, (position + 1));
			}
		}
		// 如果集合中依然有没有完成的词条，则继续进行
//		if (!word_list_copy.isEmpty()) {
//			insertWordGroup(forest, key, word_list_copy, ++position);
//		}
	}

	// 无条件插入一个新词，主要是之前已经判断过了
	public static void insertSingleWordItemNoCondition(Forest forest,
			Map<String, List<WordPojo>> wordGroupMap, WordPojo wordPojo) {
		String temp_key = "" + wordPojo.getWord().charAt(0);
		/*
		 * 首先加入wordGroupMap中
		 */
		List<WordPojo> new_word_list = new ArrayList<WordPojo>();// 先初始化插入新词时的类型集合--list
		new_word_list.add(wordPojo);// 将新wordPojo加入到上边初始化的list集合中

		/**
		 * 作为一个新组,插入到Trie树中
		 */
		insertWordGroup(forest, temp_key, new_word_list, 0);// 将该词条作为独立的组构造进Trie树中
	}

	// 无条件删除一个词，主要是之前已经判断过了
	public static void removeSingleWordItemNoCondition(Forest forest,
			Map<String, List<WordPojo>> wordGroupMap, WordPojo wordPojo) {
		String temp_key = "" + wordPojo.getWord().charAt(0);
		List<Branch> list = forest.getWordItemNodeList(wordPojo.getWord());
		forest.setNull4BaseAndCheck(list);
		wordGroupMap.remove(temp_key);
	}

	// 无条件删除一组词，主要是之前已经判断过了
	public static void removeWordItemListNoCondition(Forest forest,
			Map<String, List<WordPojo>> wordGroupMap, List<WordPojo> wordList) {
		List<Branch> list = forest.getWordGroupNodeList(wordList);
		forest.setNull4BaseAndCheck(list);
	}

	/**
	 * 插入一个新词条
	 */
	public static InsertWordStatus insertSingleWordItem(Forest forest,
			Map<String, List<WordPojo>> wordGroupMap, WordPojo wordPojo) {
		/**
		 * 首先查看是否是个新组,即wordGroupMap中的key中没有以该词条的首字母开头
		 */
		InsertWordStatus insertWordStatus = null;
		if (wordPojo != null && wordPojo.getWord().trim().length() > 0) {
			/*
			 * 词条首字符有重复，但不一定插入就会重复,故先进行单词条插入的是否有冲突 如果没有冲突，则直接插入亦可
			 */
			insertWordStatus = forest.isValidToSingleWord(wordPojo.getWord());
			String temp_key = "" + wordPojo.getWord().charAt(0);
			if (insertWordStatus == InsertWordStatus.Valid) {
				// System.out.println("可以直接插入该词");
				insertSingleWordItemNoCondition(forest, wordGroupMap, wordPojo);
				// 将词条插入至wordGroupMap中
				if (wordGroupMap.containsKey(temp_key)) {
					wordGroupMap.get(temp_key).add(wordPojo);
					// System.out.println("插入完成后---wordGroupMap.get(temp_key)---"
					// + wordGroupMap.get(temp_key).size());
				} else {
					// List<WordPojo> temp_list = new ArrayList<WordPojo>(1);
					List<WordPojo> temp_list = new LinkedList<WordPojo>();
					temp_list.add(wordPojo);
					wordGroupMap.put(temp_key, temp_list);
				}
				logger.info("插入词条--" + wordPojo + "--成功");
			} else if (insertWordStatus == InsertWordStatus.Exist) {
				logger.info("词条--" + wordPojo + "--已存在!");
			} else {
				// 处理有冲突的插入
				// 将有冲突的词条的所属的组找
				logger.info("开始插入有冲突的词条--" + wordPojo + "!");
				List<WordPojo> temp_word_list = wordGroupMap.get(temp_key);
				removeWordItemListNoCondition(forest, wordGroupMap,
						temp_word_list);
				logger.info("Trie树中该词条对应组的所有节点位置已清空!");
				temp_word_list.add(wordPojo);// 将新词条加入本该属于的组中
				// 由于集合对象是共享的，故在此先复制一份再去用
				// temp_word_list = new ArrayList<WordPojo>(temp_word_list);
				temp_word_list = new LinkedList<WordPojo>(temp_word_list);
				insertWordGroup(forest, temp_key, temp_word_list, 0);// 重新插入Trie树中
				logger.info("成功插入的词条--" + wordPojo + "");
				insertWordStatus = InsertWordStatus.Valid;
			}
		} else {
			logger.info("新插入WordPojo词条不可为空!");
			insertWordStatus = InsertWordStatus.NoEffect;
		}
		return insertWordStatus;
	}

	/**
	 * 删除一个词条
	 */
	public static RemoveWordStatus removeSingleWordItem(Forest forest,
			Map<String, List<WordPojo>> wordGroupMap, WordPojo wordPojo) {
		/**
		 * 首先查看是否是个新组,即wordGroupMap中的key中没有以该词条的首字母开头
		 */
		RemoveWordStatus removeWordStatus = null;
		if (wordPojo != null && wordPojo.getWord().trim().length() > 0) {
			/*
			 * 判断该词是不是在wordGroupMap中的一个词
			 */
			String temp_key = "" + wordPojo.getWord().charAt(0);
			List<WordPojo> temp_word_list = null;// 暂存与wordPojo同组的所有wordPojo的集合
			if (wordGroupMap.containsKey(temp_key)
					&& (temp_word_list = wordGroupMap.get(temp_key))
							.contains(wordPojo)) {
				// System.out.println("要处理删除的词!");
				/**
				 * 第一步找到该词在wordGroupMap中的所有以temp_key开头的词
				 */
				int temp_word_list_size = temp_word_list.size();
				// System.out.println("同组的词的个数为---" + temp_word_list_size);
				if (temp_word_list_size == 1) {// 如果该组就这一个词则可以直接删除掉,找到对象的每个char对应的位置后直接删除
					removeSingleWordItemNoCondition(forest, wordGroupMap,
							wordPojo);
					logger.info("Trie树相应位置已清空!");
					logger.info("wordGroupMap中已清除词条---" + wordPojo);
				} else {
					/**
					 * 首先获取该组中的所有词条,即temp_word_list
					 */
					/**
					 * 其次，将这个集合中的所有branch都得到
					 */
					removeWordItemListNoCondition(forest, wordGroupMap,
							temp_word_list);
					logger.info("Trie树中该词条对应组的所有节点位置已清空!");
					temp_word_list.remove(wordPojo);// 在该组中删除wordPojo,再重新插一遍进Trie树中
					// 由于集合对象是共享的，故在此先复制一份再去用
					temp_word_list = new LinkedList<WordPojo>(temp_word_list);
					insertWordGroup(forest, temp_key, temp_word_list, 0);// 重新插入Trie树中
					// 在wordGroupMap中删除要去掉的词条wordPojo
					// 由于temp_word_list与wordGroupMap中共享该组词条，故已完成删除!
				}
				removeWordStatus = RemoveWordStatus.Exist;// 说明该词条已完成删除
				logger.info("删除词条--" + wordPojo + "--成功");
			} else {
				logger.info("要删除的词--" + wordPojo + "--不在词典中!");
				removeWordStatus = RemoveWordStatus.NotExist;
			}
		} else {
			logger.info("要删除的词不可为空!");
			removeWordStatus = RemoveWordStatus.NoEffect;
		}
		return removeWordStatus;
	}

	public static void main(String[] args) {

	}
}
