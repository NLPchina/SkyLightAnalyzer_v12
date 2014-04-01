package com.zel.interfaces.analyzer.lucene;

import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.zel.entity.TermUnit;

/**
 * 获取分词单元的接口定义类
 * 
 * @author zel
 * 
 */
public interface SkyLightTokenizerInterface {
	/**
	 * Copies the matched text into the CharTermAttribute
	 */
	public void setTermUnitValueToTextAttr(CharTermAttribute t,
			TermUnit termUnit);

	/**
	 * Resets the scanner to read from a new input stream. Does not close the
	 * old reader.
	 * 
	 * All internal variables are reset, the old input stream <b>cannot</b> be
	 * reused (internal buffer is discarded and lost). Lexical state is set to
	 * <tt>ZZ_INITIAL</tt>.
	 * 
	 * @param reader
	 *            the new input stream
	 */
	public void reset(Reader reader);

	/**
	 * Resumes scanning until the next regular expression is matched, the end of
	 * input is encountered or an I/O-Error occurs.
	 * 
	 * @return the next token, {@link #YYEOF} on end of stream
	 * @exception IOException
	 *                if any I/O-Error occurs
	 */
	public TermUnit getNextToken() throws IOException;

	// 返回当前返回词的位置值，即offset
	public int getCurrentPos();

	// 返回当前返回词条的长度
	public int getTokenLength();

}
