package com.framework.model;

public class Sort {
	
	/**
	 * 字段
	 */
	private String field;
	
	/**
	 * 排序逻辑
	 */
	private String logic;
	private Integer count;
	private Integer count2;
	

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getLogic() {
		return logic;
	}

	public void setLogic(String logic) {
		this.logic = logic;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getCount2() {
		return count2;
	}

	public void setCount2(Integer count2) {
		this.count2 = count2;
	}
	
	
}
