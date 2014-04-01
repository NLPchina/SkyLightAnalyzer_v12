package com.zel.core.analyzer.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.Version;

import com.zel.entity.TermUnit;
import com.zel.interfaces.analyzer.lucene.SkyLightTokenizerInterface;

/**
 * 真正的分词器,这里的token相当于以往的termUnit,即一个分词单元
 * 
 * @author zel
 * 
 */
public class SkyLightTokenizer extends Tokenizer {

	protected SkyLightTokenizer(Reader input) {
		super(input);
		// TODO Auto-generated constructor stub
	}

	public SkyLightTokenizer(Version matchVersion, Reader input) {
		super(input);
		init(matchVersion);
	}

	private final void init(Version matchVersion) {
		if (matchVersion.onOrAfter(Version.LUCENE_40)) {
			this.scanner = new SkyLightTokenizerImpl(null);
		}
	}

	private int skippedPositions = 0;

	private SkyLightTokenizerInterface scanner;

	// this tokenizer generates three attributes:
	// term offset, positionIncrement and type
	private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
	private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
	private final PositionIncrementAttribute posIncrAtt = addAttribute(PositionIncrementAttribute.class);
	private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);

	@Override
	public boolean incrementToken() throws IOException {
		clearAttributes();
		// TODO Auto-generated method stub
		TermUnit termUnit = scanner.getNextToken();

		if (termUnit == null) {// 即已结束，不用再往下取token了
			return false;
		}
		skippedPositions=this.scanner.getTokenLength();
		
		posIncrAtt.setPositionIncrement(1);
		scanner.setTermUnitValueToTextAttr(termAtt, termUnit);
		offsetAtt.setOffset(termUnit.getOffset(), termUnit.getOffset()
				+ skippedPositions);

		return true;
	}

	@Override
	public final void end() throws IOException {
//		System.out.println("end()方法中");
		super.end();
		// set final offset
		int finalOffset = correctOffset(scanner.getCurrentPos()
				+ scanner.getTokenLength());
		offsetAtt.setOffset(finalOffset, finalOffset);
		// adjust any skipped tokens
		posIncrAtt.setPositionIncrement(posIncrAtt.getPositionIncrement()
				+ scanner.getTokenLength());
	}

	@Override
	public void reset() throws IOException {
		scanner.reset(input);
		skippedPositions = 0;
	}
}
