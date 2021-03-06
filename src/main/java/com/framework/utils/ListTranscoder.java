package com.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*************************************************************************
 * 版本： V1.0
 * 
 * 文件名称 ：ListTranscoder.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-29 下午3:51:08 修订信息 : modify by ( ) on (date)
 * for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class ListTranscoder<M extends Serializable> extends SerializeTranscoder {

	@Override
	public byte[] serialize(Object value) {
		if (value == null)
			throw new NullPointerException("Can't serialize null");

		List<M> values = (List<M>) value;

		byte[] results = null;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;

		try {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			for (M m : values) {
				os.writeObject(m);
			}

			// os.writeObject(null);
			os.close();
			bos.close();
			results = bos.toByteArray();
		} catch (IOException e) {
			throw new IllegalArgumentException("Non-serializable object", e);
		} finally {
			close(os);
			close(bos);
		}

		return results;
	}

	@Override
	public Object deserialize(byte[] in) {
		List<M> list = new ArrayList<M>();
		ByteArrayInputStream bis = null;
		ObjectInputStream is = null;
		try {
			if (in != null) {
				bis = new ByteArrayInputStream(in);
				is = new ObjectInputStream(bis);
				while (true) {
					M m = (M) is.readObject();
					if (m == null) {
						break;
					}

					list.add(m);

				}
				is.close();
				bis.close();
			}
		} catch (IOException e) {
		} catch (ClassNotFoundException e) {
		} finally {
			close(is);
			close(bis);
		}

		return list;
	}
}
