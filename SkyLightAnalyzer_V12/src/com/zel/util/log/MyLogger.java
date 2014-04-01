package com.zel.util.log;

import org.apache.log4j.Logger;

import com.zel.util.SystemParas;

/**
 * 自定义log类，主要是为了灵活控制日志的输出与否
 * 
 * @author zel
 * 
 */
public class MyLogger {
	private Logger logger = null;

	public MyLogger(Class logClass) {
		this.logger = getLogger(logClass);
	}

	public Logger getLogger(Class logClass) {
		return Logger.getLogger(logClass);
	}

	public void info(Object obj) {
		if (SystemParas.log_output_enable) {
			this.logger.info(obj);
		}
	}

}
