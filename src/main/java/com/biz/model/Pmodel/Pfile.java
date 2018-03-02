package com.biz.model.Pmodel;

import java.io.Serializable;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：Pfile.java 描述说明 ：文件系统最小单位
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-28 下午5:29:33  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class Pfile implements Serializable{
	private static final long serialVersionUID = -3747779870197622082L;
	private String id;
	private String fileName;//文件名称
	private String filePath;//文件
	private String fileSize;//文件大小
	private String createTime;//文件创建时间
	
	public Pfile() {
		super();
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileSize() {
		return fileSize;
	}
	public void setFileSize(String fileSize) {
		this.fileSize = fileSize;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
