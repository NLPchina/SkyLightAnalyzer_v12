package com.zel.interfaces.analyzer;

import java.util.List;

import com.zel.entity.TermUnit;
import com.zel.entity.util.TermUnitStream;

/**
 * 分词器接口
 * 
 * @author zel
 */
public interface Analyzer {
	/**
	 * 初始化分词器
	 */
	public void init();

	// 分布式环境下初始化分词器
	public void init_distributed(byte[] complexToSimpleArray,
			byte[] wordGroupMapArray, byte[] trieForestArray,
			byte[] termNatureArray, byte[] biGramMatrixArray);

	/**
	 * 得到content串的全切分结果
	 * 
	 * @param content
	 * @return
	 */
	public List<TermUnit> getAllSplitList(String content);

	/**
	 * 该方法取得分词结果，只能得到分词后所有匹配的分词结果，如祖国，成功等词
	 * 
	 * @return
	 */
	public List<TermUnit> getSplitWords(String content);

	/**
	 * 该方法取得分词结果，包括字符串的所有数据的分词结果
	 * 
	 * @return
	 */
	public List<TermUnit> getSplitResult(String content);

	/**
	 * 该方法取得分词结果，包括字符串的所有数据的分词结果
	 * 
	 * @return
	 */
	public TermUnitStream getSplitResult(java.io.Reader sourceReader);

	/**
	 * 带有词性标注的分词结果
	 * 
	 * @param content
	 * @return
	 */
	public List<TermUnit> getSplitPOSResult(String content);

	/**
	 * 带有词性标注的分词结果,并合并相关词性
	 * 
	 * @param content
	 * @return
	 */
	public List<TermUnit> getSplitMergePOSResult(String content);
}
