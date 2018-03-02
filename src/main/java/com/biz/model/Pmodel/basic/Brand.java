package com.biz.model.Pmodel.basic;

import java.io.Serializable;

/**
 * 品牌管理
 */
public class Brand implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 品牌主键
	 */
	private String brand_code;
	/**
	 * 店小翼中的品牌code
	 */
	private String dxy_code;
	/**
	 * 代理商主键code
	 */
	private String agent_code;
	/**
	 * 快速分类code
	 */
	private String speed_code;
	/**
	 * 品牌名称
	 */
	private String name;
	/**
	 * 商户logo
	 */
	private String logo_url;
	/**
	 * 一级目录code
	 */
	private String category_first;
	/**
	 * 二级目录code
	 */
	private String category_second;
	/**
	 * 锁定 0:可用 1：锁定不可用
	 */
	private int islock = 0;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 久零券余额（分）
	 */
	private long balance_90;
	/**
	 * 久零券透支额度（分）
	 */
	private int credit_total_90;
	/**
	 * 久零券透支额度（元）
	 */
	private double credit_total_901;
	/**
	 * 久零券当前透支额度（分）
	 */
	private int credit_now_90;
	/**
	 * 久零佣金百分点
	 */
	private double commission;
	/**
	 * 兴业银行手续费百分点
	 */
	private double procedures;
    /**
     * 发劵比例
     */
    private double proportion=1;
	/**
	 * 业务代表code
	 */
	private String biz_code;
	/**
	 * 删除标志0：未删除，1：删除
	 */
	private int isdel = 0;
	/**
	 * 创建时间
	 */
	private String create_time;
	/**
	 * 是否是90品牌  0联盟商户  190线下体验品牌
	 */
	private int is_90=0;
	/**
	 * 联系电话
	 */
	private String telephone;
	/**
	 * 详细地址
	 */
	private String address;
	/**
	 * 商户简介
	 */
	private String introduction;
	private int sort;
	private int pageviews;
	/*********************** 扩展字段开始 ***************************/
	private String category_first_name;
	private String category_second_name;

	private String isPeriodization = "0";
    private int isTicket;
	/*********************** 扩展字段结束 ***************************/
	
	public double getCredit_total_901()
	{
		return credit_total_901;
	}
	public void setCredit_total_901(double credit_total_901)
	{
		this.credit_total_901 = credit_total_901;
	}
	public String getBrand_code()
	{
		return brand_code;
	}
	public String getSpeed_code() {
		return speed_code;
	}
	public void setSpeed_code(String speed_code) {
		this.speed_code = speed_code;
	}
	public int getIs_90()
	{
		return is_90;
	}
	public void setIs_90(int is_90)
	{
		this.is_90 = is_90;
	}
	public void setBrand_code(String brand_code)
	{
		this.brand_code = brand_code;
	}
	
	public String getDxy_code()
	{
		return dxy_code;
	}
	public void setDxy_code(String dxy_code)
	{
		this.dxy_code = dxy_code;
	}
	public String getAgent_code()
	{
		return agent_code;
	}
	public void setAgent_code(String agent_code)
	{
		this.agent_code = agent_code;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getLogo_url()
	{
		return logo_url;
	}
	public void setLogo_url(String logo_url)
	{
		this.logo_url = logo_url;
	}
	public String getCategory_first()
	{
		return category_first;
	}
	public void setCategory_first(String category_first)
	{
		this.category_first = category_first;
	}
	public String getCategory_second()
	{
		return category_second;
	}
	public void setCategory_second(String category_second)
	{
		this.category_second = category_second;
	}
	public int getIslock()
	{
		return islock;
	}
	public void setIslock(int islock)
	{
		this.islock = islock;
	}
	public String getProvince()
	{
		return province;
	}
	public void setProvince(String province)
	{
		this.province = province;
	}
	public String getCity()
	{
		return city;
	}
	public void setCity(String city)
	{
		this.city = city;
	}
	public long getBalance_90()
	{
		return balance_90;
	}
	public void setBalance_90(long balance_90)
	{
		this.balance_90 = balance_90;
	}
	public long getCredit_total_90()
	{
		return credit_total_90;
	}
	public void setCredit_total_90(int credit_total_90)
	{
		this.credit_total_90 = credit_total_90;
	}
	public long getCredit_now_90()
	{
		return credit_now_90;
	}
	public void setCredit_now_90(int credit_now_90)
	{
		this.credit_now_90 = credit_now_90;
	}
	public double getCommission()
	{
		return commission;
	}
	public void setCommission(double commission)
	{
		this.commission = commission;
	}
	public double getProcedures()
	{
		return procedures;
	}
	public void setProcedures(double procedures)
	{
		this.procedures = procedures;
	}
	public String getBiz_code()
	{
		return biz_code;
	}
	public void setBiz_code(String biz_code)
	{
		this.biz_code = biz_code;
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
	public String getCategory_first_name()
	{
		return category_first_name;
	}
	public void setCategory_first_name(String category_first_name)
	{
		this.category_first_name = category_first_name;
	}
	public String getCategory_second_name()
	{
		return category_second_name;
	}
	public void setCategory_second_name(String category_second_name)
	{
		this.category_second_name = category_second_name;
	}
	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public int getPageviews() {
		return pageviews;
	}
	public void setPageviews(int pageviews) {
		this.pageviews = pageviews;
	}

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

	public String getIsPeriodization() {
		return isPeriodization;
	}

	public void setIsPeriodization(String isPeriodization) {
		this.isPeriodization = isPeriodization;
	}

    public int getIsTicket() {
        return isTicket;
    }

    public void setIsTicket(int isTicket) {
        this.isTicket = isTicket;
    }
}
