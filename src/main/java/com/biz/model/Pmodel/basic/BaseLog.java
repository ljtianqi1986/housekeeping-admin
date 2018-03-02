package com.biz.model.Pmodel.basic;
/**
 * 后台操作日志
 * @author ATOM
 *
 */
public class BaseLog
{
	private int id;
	/**
	 * 修改人身份标识
	 */
	private String identity_code;
	/**
	 * 修改人名称
	 */
	private String user_name;
	/**
	 * 修改人身份
	 */
	private int identity;
	/**
	 * 修改人IP
	 */
	private String ip;
	/**
	 * 修改详情
	 */
	private String detail;
	/**
	 * 模块类型(新增，删除，修改等)
	 */
	private String module_type;
	/**
	 * 模块名称
	 */
	private String module_name;
	/**
	 * 涉及表名称
	 */
	private String table_name;
	/**
	 * 涉及记录主键
	 */
	private String table_key;
	private String create_time;
	private int isdel = 0;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getIdentity_code() {
		return identity_code;
	}
	public void setIdentity_code(String identity_code) {
		this.identity_code = identity_code;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public int getIdentity() {
		return identity;
	}
	public void setIdentity(int identity) {
		this.identity = identity;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getModule_type() {
		return module_type;
	}
	public void setModule_type(String module_type) {
		this.module_type = module_type;
	}
	public String getModule_name() {
		return module_name;
	}
	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}
	public String getTable_name() {
		return table_name;
	}
	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}
	public String getTable_key() {
		return table_key;
	}
	public void setTable_key(String table_key) {
		this.table_key = table_key;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public int getIsdel() {
		return isdel;
	}
	public void setIsdel(int isdel) {
		this.isdel = isdel;
	}	
}