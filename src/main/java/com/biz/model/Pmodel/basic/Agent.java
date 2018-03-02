package com.biz.model.Pmodel.basic;

/**
 * 代理商管理
 */
public class Agent {
	/**
	 * 代理商主键
	 */
	private String agent_code;
	/**
	 * 代理商名称
	 */
	private String agent_name;
	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 标示：xiaoai：无线扬州 chihuo:吃货小艾
	 */
	private String flag;
	
	/**
	 * 0:代理商 1：渠道
	 */
	private int type;
	
	private String city;
	private String province;
	
	private String province_name;
	private String city_name;
	private String agent_p_code;
	
	
	public String getAgent_p_code() {
		return agent_p_code;
	}
	public void setAgent_p_code(String agent_p_code) {
		this.agent_p_code = agent_p_code;
	}
	public String getProvince_name()
	{
		return province_name;
	}
	public void setProvince_name(String province_name)
	{
		this.province_name = province_name;
	}
	public String getCity_name()
	{
		return city_name;
	}
	public void setCity_name(String city_name)
	{
		this.city_name = city_name;
	}
	public String getAgent_code()
	{
		return agent_code;
	}
	public void setAgent_code(String agent_code)
	{
		this.agent_code = agent_code;
	}
	public String getAgent_name()
	{
		return agent_name;
	}
	public void setAgent_name(String agent_name)
	{
		this.agent_name = agent_name;
	}
	public int getIsdel()
	{
		return isdel;
	}
	public void setIsdel(int isdel)
	{
		this.isdel = isdel;
	}
	public String getCreate_time()
	{
		return create_time;
	}
	public void setCreate_time(String create_time)
	{
		this.create_time = create_time;
	}
	public String getFlag()
	{
		return flag;
	}
	public void setFlag(String flag)
	{
		this.flag = flag;
	}
	public int getType()
	{
		return type;
	}
	public void setType(int type)
	{
		this.type = type;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}

}
