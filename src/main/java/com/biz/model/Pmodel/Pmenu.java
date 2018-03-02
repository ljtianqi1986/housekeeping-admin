package com.biz.model.Pmodel;

import java.util.Date;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：Pmenu.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-27 上午11:27:04  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class Pmenu {
	 private String id;
     private String pid;
     private String text;
     private String href;
     private String classIcon;
     private Short state;
     private Date createTime=new Date();
     
     
     
	public Pmenu() {
		super();
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getClassIcon() {
		return classIcon;
	}
	public void setClassIcon(String classIcon) {
		this.classIcon = classIcon;
	}
	public Short getState() {
		return state;
	}
	public void setState(Short state) {
		this.state = state;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
     
     
}
