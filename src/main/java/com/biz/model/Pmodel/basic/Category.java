package com.biz.model.Pmodel.basic;

/**
 * 门店行业类目
 */
public class Category {
	/**
	 * 类目主键
	 */
	private String category_code;
	/**
	 * 类目名称
	 */
	private String category_name;
	/**
	 * 父类目
	 */
	private String category_parent;
	/**
	 * 排序,越小越往前
	 */
	private int sort;
	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel;
	/**
	 * 创建时间
	 */
	private String create_time;

	public String getCategory_code() {
		return category_code;
	}

	public void setCategory_code(String category_code) {
		this.category_code = category_code;
	}

	public String getCategory_name() {
		return category_name;
	}

	public void setCategory_name(String category_name) {
		this.category_name = category_name;
	}

	public String getCategory_parent() {
		return category_parent;
	}

	public void setCategory_parent(String category_parent) {
		this.category_parent = category_parent;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public int getIsdel() {
		return isdel;
	}

	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

}
