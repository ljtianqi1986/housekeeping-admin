package com.framework.model;

import java.util.Map;


/**
 * sessionInfo模型，只要登录成功，就需要设置到session里面，便于系统使用
 * 
 * @author 刘佳佳
 */
public class SessionInfo implements java.io.Serializable {

	private static final long serialVersionUID = -3740590693811166633L;
	private Map<String, Object> data;
	private String activityMenuId = "";
	
	public SessionInfo() {
		super();
	}
	
	public Map<String, Object> getData() {
		return data;
	}
	public void setData(Map<String, Object> data) {
		this.data = data;
	}
	public String getActivityMenuId() {
		return activityMenuId;
	}
	public void setActivityMenuId(String activityMenuId) {
		this.activityMenuId = activityMenuId;
	}

	
	
}
