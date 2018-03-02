package com.biz.model.Pmodel.api;


/**
 * 门店管理
 */
public class Shop {
	private String sid;//门店主键，商户自己的id
	private String dxy_code;//店小翼中门店的code
	private String dxy_person_code;//店小翼门店中店长的code
	private String business_name;//门店名称
	private String logo_url;
	private String province;//门店所在的省份
	private String city;
	private String district;//门店所在地区
	private String address;//门店所在的详细街道地址
	private String telephone;//门店的电话
	private int offset_type;//坐标类型，1 为火星坐标（目前只能选1）
	private String longitude;//门店所在地理位置的经度
	private String latitude;//门店所在地理位置的纬度
	private String recommend;//推荐品
	private String special;//特色服务
	private String introduction;//商户简介，主要介绍商户信息等
	private String open_time;// 营业时间
	private int avg_price;//人均价格，大于0 的整数
	private String category_first;//一级目录code
	private String category_second;//二级目录code
	private String district_code;//商圈主键
	private String brand_code = "";//品牌主键
	private String agent_code;//代理商主键
	private int isdel;//删除标志0：未删除，1：删除
	private String create_time;//创建时间
	private String hook_code = "";//收银软件主键代码
	private int initial_money = 0;//初始收银金额，默认：0 单位：分
	private int money_fixed = 0;	//是否固定收银金额 0：不固定 ;1：固定
	private int is_focus = 0;//是否捕获焦点 0:不捕获;1:捕获
	private String receiptStyle;//小票打印样式
	private int is_90 = 0;
	private int sort=0;
	private int pageviews=0;
	private String brand_name;
	private String agent_name;
	private String district_name;// 商圈名称
	private String province_name;// 省
	private String city_name;// 市
	private String dis_name;// 区
	private String category_first_name;
	private String category_second_name;
	private Double distance;//距离

    private int iscoin=0;//pos机，手动发券，是否送90贝，0不送 1:送

	private String isPeriodization = "0";
    private int ticketType=0;
	
	public String getDxy_person_code()
	{
		return dxy_person_code;
	}
	public void setDxy_person_code(String dxy_person_code)
	{
		this.dxy_person_code = dxy_person_code;
	}
	public String getSid()
	{
		return sid;
	}
	public void setSid(String sid)
	{
		this.sid = sid;
	}
	public String getDxy_code()
	{
		return dxy_code;
	}
	public void setDxy_code(String dxy_code)
	{
		this.dxy_code = dxy_code;
	}
	public String getBusiness_name()
	{
		return business_name;
	}
	public void setBusiness_name(String business_name)
	{
		this.business_name = business_name;
	}
	public String getLogo_url()
	{
		return logo_url;
	}
	public void setLogo_url(String logo_url)
	{
		this.logo_url = logo_url;
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
	public String getDistrict()
	{
		return district;
	}
	public void setDistrict(String district)
	{
		this.district = district;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	public int getOffset_type()
	{
		return offset_type;
	}
	public void setOffset_type(int offset_type)
	{
		this.offset_type = offset_type;
	}
	public String getLongitude()
	{
		return longitude;
	}
	public void setLongitude(String longitude)
	{
		this.longitude = longitude;
	}
	public String getLatitude()
	{
		return latitude;
	}
	public void setLatitude(String latitude)
	{
		this.latitude = latitude;
	}
	public String getRecommend()
	{
		return recommend;
	}
	public void setRecommend(String recommend)
	{
		this.recommend = recommend;
	}
	public String getSpecial()
	{
		return special;
	}
	public void setSpecial(String special)
	{
		this.special = special;
	}
	public String getIntroduction()
	{
		return introduction;
	}
	public void setIntroduction(String introduction)
	{
		this.introduction = introduction;
	}
	public String getOpen_time()
	{
		return open_time;
	}
	public void setOpen_time(String open_time)
	{
		this.open_time = open_time;
	}
	public int getAvg_price()
	{
		return avg_price;
	}
	public void setAvg_price(int avg_price)
	{
		this.avg_price = avg_price;
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
	public String getDistrict_code()
	{
		return district_code;
	}
	public void setDistrict_code(String district_code)
	{
		this.district_code = district_code;
	}
	public String getBrand_code()
	{
		return brand_code;
	}
	public void setBrand_code(String brand_code)
	{
		this.brand_code = brand_code;
	}
	public String getAgent_code()
	{
		return agent_code;
	}
	public void setAgent_code(String agent_code)
	{
		this.agent_code = agent_code;
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
	public String getHook_code()
	{
		return hook_code;
	}
	public void setHook_code(String hook_code)
	{
		this.hook_code = hook_code;
	}
	public int getInitial_money()
	{
		return initial_money;
	}
	public void setInitial_money(int initial_money)
	{
		this.initial_money = initial_money;
	}
	public int getMoney_fixed()
	{
		return money_fixed;
	}
	public void setMoney_fixed(int money_fixed)
	{
		this.money_fixed = money_fixed;
	}
	public int getIs_focus()
	{
		return is_focus;
	}
	public void setIs_focus(int is_focus)
	{
		this.is_focus = is_focus;
	}
	public String getReceiptStyle()
	{
		return receiptStyle;
	}
	public void setReceiptStyle(String receiptStyle)
	{
		this.receiptStyle = receiptStyle;
	}
	public String getBrand_name()
	{
		return brand_name;
	}
	public void setBrand_name(String brand_name)
	{
		this.brand_name = brand_name;
	}
	public String getAgent_name()
	{
		return agent_name;
	}
	public void setAgent_name(String agent_name)
	{
		this.agent_name = agent_name;
	}
	public String getDistrict_name()
	{
		return district_name;
	}
	public void setDistrict_name(String district_name)
	{
		this.district_name = district_name;
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
	public String getDis_name()
	{
		return dis_name;
	}
	public void setDis_name(String dis_name)
	{
		this.dis_name = dis_name;
	}
	public int getIs_90() {
		return is_90;
	}
	public void setIs_90(int is_90) {
		this.is_90 = is_90;
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
	public String getCategory_first_name() {
		return category_first_name;
	}
	public void setCategory_first_name(String category_first_name) {
		this.category_first_name = category_first_name;
	}
	public String getCategory_second_name() {
		return category_second_name;
	}
	public void setCategory_second_name(String category_second_name) {
		this.category_second_name = category_second_name;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}

    public int getIscoin() {
        return iscoin;
    }

    public void setIscoin(int iscoin) {
        this.iscoin = iscoin;
    }


	public String getIsPeriodization() {
		return isPeriodization;
	}

	public void setIsPeriodization(String isPeriodization) {
		this.isPeriodization = isPeriodization;
	}

    public int getTicketType() {
        return ticketType;
    }

    public void setTicketType(int ticketType) {
        this.ticketType = ticketType;
    }
}
