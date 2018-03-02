package com.biz.model.Pmodel.basic;

import java.io.Serializable;
  /*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：TreeState.java 描述说明 ：
 * 
 * 创建信息 : create by 曹凯 on 2015-12-11 下午8:17:38  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
public class TreeState implements Serializable{
	private boolean opened=false;
	private boolean selected=false;
	public boolean isOpened() {
		return opened;
	}
	public void setOpened(boolean opened) {
		this.opened = opened;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	

}
