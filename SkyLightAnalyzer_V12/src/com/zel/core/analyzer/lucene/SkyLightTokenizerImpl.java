package com.zel.core.analyzer.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.standard.StandardTokenizerInterface;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.zel.core.analyzer.StandardAnalyzer;
import com.zel.entity.TermUnit;
import com.zel.entity.util.TermUnitStream;
import com.zel.interfaces.analyzer.lucene.SkyLightTokenizerInterface;

/**
 * 实现流的方式的实现类
 * 
 * @author zel
 * 
 */
public class SkyLightTokenizerImpl implements SkyLightTokenizerInterface {
	private Reader reader;

	private int offset;
	private int length;

	static com.zel.core.analyzer.StandardAnalyzer analyzer = null;
	private TermUnitStream termUnitStream = null;
	
	static {
		analyzer = new StandardAnalyzer();
	}

	public SkyLightTokenizerImpl(Reader in) {
		this.reader = in;
	}

	@Override
	public TermUnit getNextToken() throws IOException {
		// TODO Auto-generated method stub
		TermUnit termUnit = termUnitStream.getNextTermUnit();

		// System.out.println("come getNextToken");
		// System.out.println("####" + termUnit);

		if (termUnit == null) {
			this.offset = this.offset + this.length;
			this.length = 0;
			return null;
		} else {
			this.offset = termUnit.getOffset();
			this.length = termUnit.getLength();
		}

		return termUnit;
	}

	@Override
	public void setTermUnitValueToTextAttr(CharTermAttribute t,
			TermUnit termUnit) {
		// TODO Auto-generated method stub
		t
				.copyBuffer(termUnit.getValue().toCharArray(), 0, termUnit
						.getLength());
	}

	@Override
	public void reset(Reader reader) {
		// System.out.println("come reset");
		// TODO Auto-generated method stub
		this.reader = reader;
		// 流对象重置后，立即分词
		termUnitStream = analyzer.getSplitResult(reader);
	}

	@Override
	public int getCurrentPos() {
		// TODO Auto-generated method stub
		return offset;
	}

	@Override
	public int getTokenLength() {
		// TODO Auto-generated method stub
		return length;
	}
}
