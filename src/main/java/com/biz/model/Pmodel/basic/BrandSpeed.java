package com.biz.model.Pmodel.basic;

/**
 * @author zhengXin 2016-11-07 商户快速分类管理实体类
 * 
 * 用于商户的快速分类管理实体类
 */
public class BrandSpeed {
	/**
	 * 快速分类主键
	 */
	private String speed_code;
	/**
	 * 快速分类名称
	 */
	private String speed_name;
	/**
	 * 父类目
	 */
	private String speed_parent;
	/**
	 * 快速分类图标
	 */
	private String speed_icon;
	/**
	 * 快速分类顶部图片
	 */
	private String speed_top_icon;
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
	
	
	public String getSpeed_code() {
		return speed_code;
	}
	public void setSpeed_code(String speed_code) {
		this.speed_code = speed_code;
	}
	public String getSpeed_name() {
		return speed_name;
	}
	public void setSpeed_name(String speed_name) {
		this.speed_name = speed_name;
	}
	public String getSpeed_parent() {
		return speed_parent;
	}
	public void setSpeed_parent(String speed_parent) {
		this.speed_parent = speed_parent;
	}
	public String getSpeed_icon() {
		return speed_icon;
	}
	public void setSpeed_icon(String speed_icon) {
		this.speed_icon = speed_icon;
	}
	public String getSpeed_top_icon() {
		return speed_top_icon;
	}
	public void setSpeed_top_icon(String speed_top_icon) {
		this.speed_top_icon = speed_top_icon;
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
