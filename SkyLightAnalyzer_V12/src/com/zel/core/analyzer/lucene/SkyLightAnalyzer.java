package com.zel.core.analyzer.lucene;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.util.Version;

/**
 * 该类负责天亮中文分词器与lucene的对接
 * 
 * @author zel
 * 
 */
public class SkyLightAnalyzer extends Analyzer {

	private Version matchVersion;

	public SkyLightAnalyzer(Version matchVersion) {
		this.matchVersion = matchVersion;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		// TODO Auto-generated method stub
		final SkyLightTokenizer tokenizer = new SkyLightTokenizer(matchVersion,
				reader);

		return new TokenStreamComponents(tokenizer);
	}

}
