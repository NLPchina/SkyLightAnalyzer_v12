package com.zel.core.analyzer;

import java.util.List;

import com.zel.entity.TermUnit;
import com.zel.manager.nature.PosManager;
import com.zel.util.SystemParas;
import com.zel.util.log.MyLogger;

/**
 * 标准查询分析器，包括不带词性标注和带词性标注两种分词结果接口
 * 
 * @author zel
 */
public class StandardAnalyzer extends BaseAnalyzer {
	// 做日志用
	private static MyLogger logger = new MyLogger(StandardAnalyzer.class);

	public StandardAnalyzer() {
		// 如果不是hadoop的分布式环境，则正常init()，除之外要进行上层应用程序的distributed_init
		if (!SystemParas.is_hadoop_use_analyzer) {
			init();
		}
	}
	
	/**
	 * 得到所有的成词,即状态为2和3的term结果
	 */
	@Override
	public List<TermUnit> getSplitWords(String content) {
		// TODO Auto-generated method stub
		List<TermUnit> termList = getSplitResult(content);
		return termList;
	}

	@Override
	public List<TermUnit> getSplitPOSResult(String content) {
		// TODO Auto-generated method stub
		List<TermUnit> termList = getSplitResult(content);

		// 为了线程安全，需在每次都要new一个新的posManager
		PosManager posManager = new PosManager(termList);
		// posManager.reset(termList);
		posManager.addPos();

		return termList;
	}
	
	@Override
	public List<TermUnit> getSplitMergePOSResult(String content) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<TermUnit> termList = getSplitResult(content);

		// 为了线程安全，需在每次都要new一个新的posManager
		PosManager posManager = new PosManager(termList);
		posManager.addPos();
		posManager.mergePOS();
		
		return termList;

	}
}
