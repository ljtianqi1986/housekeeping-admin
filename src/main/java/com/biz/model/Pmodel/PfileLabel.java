package com.biz.model.Pmodel;

import java.io.Serializable;
import java.sql.Timestamp;

/*************************************************************************
 * 版本： V1.0
 * 
 * 文件名称 ：PfileLabel.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-28 下午4:34:12 修订信息 : modify by ( ) on (date)
 * for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class PfileLabel implements Serializable {
	private static final long serialVersionUID = 8806984945227673885L;
	private String id;
	private String labelName;
	private Integer labelSort;
	private Short state;
	private Timestamp createTime;

	public PfileLabel() {
		super();

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}

	public Integer getLabelSort() {
		return labelSort;
	}

	public void setLabelSort(Integer labelSort) {
		this.labelSort = labelSort;
	}

	public Short getState() {
		return state;
	}

	public void setState(Short state) {
		this.state = state;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}

}
