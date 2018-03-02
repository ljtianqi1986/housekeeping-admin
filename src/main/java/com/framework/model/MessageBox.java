package com.framework.model;

public class MessageBox implements java.io.Serializable {

	private static final long serialVersionUID = -5456568221643292233L;

	private boolean success = true;

	private String msg = "操作成功";

	private Object obj = null;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}

}
