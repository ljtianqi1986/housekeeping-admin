package com.framework.utils;

import java.io.Closeable;

import org.apache.log4j.Logger;

/*************************************************************************
 * 版本： V1.0
 * 
 * 文件名称 ：SerializeTranscoder.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-29 下午3:49:39 修订信息 : modify by ( ) on (date)
 * for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public abstract class SerializeTranscoder {
	protected static Logger logger = Logger.getLogger(SerializeTranscoder.class);

	public abstract byte[] serialize(Object value);

	public abstract Object deserialize(byte[] in);

	public void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				logger.info("Unable to close " + closeable, e);
			}
		}
	}
}
