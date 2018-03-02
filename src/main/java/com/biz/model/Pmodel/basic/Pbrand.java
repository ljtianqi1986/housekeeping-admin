package com.biz.model.Pmodel.basic;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 品牌管理
 */
public class Pbrand implements Serializable {
    private String brandCode;
    private String dxyCode;
    private String agentCode;
    private String name;
    private String logoUrl;
    private String categoryFirst;
    private String categorySecond;
    private String isdel;
    private String createTime;
    private String islock;
    private String province;
    private String city;
    private String balance90;
    private String creditTotal90;
    private String creditNow90;
    private String balance90_shop;
    private String creditTotal90_shop;
    private String creditNow90_shop;
    private String balance90_experience;
    private String creditTotal90_experience;
    private String creditNow90_experience;
    private String commission;
    private String procedures;
    private String bizCode;
    private String is90;
    private String telephone;
    private String address;
    private String introduction;
    private String speedCode;
    private String pageviews;
    private String sort="";
    private String proportion;
    private String loginName;
    private String pwd;
    private String userCode;
    private String roleCode;
    private Integer expiryDateType = 0;
    private Integer zeroGoDateType;
    private Integer experienceDateType;
    private String sorts;
    private String iscoin;//pos机，手动发券，是否送90贝，0不送 1:送
    private String coinproportion;//发贝比例0-1

    private String isPeriodization = "0";//是否分期发券0：不分期  1：分期

    private Integer ticketType;//发券类型

    private List<Map<String,Object>> periodization;

    private String isZeroCheck;//是否支持零购卡

    private String title="";

    private String path="";
    private String url="";
    private String type="";
    private String id="";
    private int count=0;
    private String isTicket;

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getDxyCode() {
        return dxyCode;
    }

    public void setDxyCode(String dxyCode) {
        this.dxyCode = dxyCode;
    }

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getCategoryFirst() {
        return categoryFirst;
    }

    public void setCategoryFirst(String categoryFirst) {
        this.categoryFirst = categoryFirst;
    }

    public String getCategorySecond() {
        return categorySecond;
    }

    public void setCategorySecond(String categorySecond) {
        this.categorySecond = categorySecond;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getIslock() {
        return islock;
    }

    public void setIslock(String islock) {
        this.islock = islock;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBalance90() {
        return balance90;
    }

    public void setBalance90(String balance90) {
        this.balance90 = balance90;
    }

    public String getCreditTotal90() {
        return creditTotal90;
    }

    public void setCreditTotal90(String creditTotal90) {
        this.creditTotal90 = creditTotal90;
    }

    public String getCreditNow90() {
        return creditNow90;
    }

    public void setCreditNow90(String creditNow90) {
        this.creditNow90 = creditNow90;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getProcedures() {
        return procedures;
    }

    public void setProcedures(String procedures) {
        this.procedures = procedures;
    }

    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    public String getIs90() {
        return is90;
    }

    public void setIs90(String is90) {
        this.is90 = is90;
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

    public String getSpeedCode() {
        return speedCode;
    }

    public void setSpeedCode(String speedCode) {
        this.speedCode = speedCode;
    }

    public String getPageviews() {
        return pageviews;
    }

    public void setPageviews(String pageviews) {
        this.pageviews = pageviews;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getIscoin() {
        return iscoin;
    }

    public void setIscoin(String iscoin) {
        this.iscoin = iscoin;
    }

    public String getCoinproportion() {
        return coinproportion;
    }

    public void setCoinproportion(String coinproportion) {
        this.coinproportion = coinproportion;
    }

    public String getIsPeriodization() {
        return isPeriodization;
    }

    public void setIsPeriodization(String isPeriodization) {
        this.isPeriodization = isPeriodization;
    }

    public List<Map<String, Object>> getPeriodization() {
        return periodization;
    }

    public void setPeriodization(List<Map<String, Object>> periodization) {
        this.periodization = periodization;
    }

    public Integer getExpiryDateType() {
        return expiryDateType;
    }

    public void setExpiryDateType(Integer expiryDateType) {
        this.expiryDateType = expiryDateType;
    }

    public String getIsZeroCheck() {
        return isZeroCheck;
    }

    public void setIsZeroCheck(String isZeroCheck) {
        this.isZeroCheck = isZeroCheck;
    }

    public String getSorts() {
        return sorts;
    }

    public void setSorts(String sorts) {
        this.sorts = sorts;
    }

    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Integer getZeroGoDateType() {
        return zeroGoDateType;
    }

    public void setZeroGoDateType(Integer zeroGoDateType) {
        this.zeroGoDateType = zeroGoDateType;
    }

    public Integer getExperienceDateType() {
        return experienceDateType;
    }

    public void setExperienceDateType(Integer experienceDateType) {
        this.experienceDateType = experienceDateType;
    }

    public String getIsTicket() {
        return isTicket;
    }

    public void setIsTicket(String isTicket) {
        this.isTicket = isTicket;
    }

    public String getBalance90_shop() {
        return balance90_shop;
    }

    public void setBalance90_shop(String balance90_shop) {
        this.balance90_shop = balance90_shop;
    }

    public String getCreditTotal90_shop() {
        return creditTotal90_shop;
    }

    public void setCreditTotal90_shop(String creditTotal90_shop) {
        this.creditTotal90_shop = creditTotal90_shop;
    }

    public String getCreditNow90_shop() {
        return creditNow90_shop;
    }

    public void setCreditNow90_shop(String creditNow90_shop) {
        this.creditNow90_shop = creditNow90_shop;
    }

    public String getBalance90_experience() {
        return balance90_experience;
    }

    public void setBalance90_experience(String balance90_experience) {
        this.balance90_experience = balance90_experience;
    }

    public String getCreditTotal90_experience() {
        return creditTotal90_experience;
    }

    public void setCreditTotal90_experience(String creditTotal90_experience) {
        this.creditTotal90_experience = creditTotal90_experience;
    }

    public String getCreditNow90_experience() {
        return creditNow90_experience;
    }

    public void setCreditNow90_experience(String creditNow90_experience) {
        this.creditNow90_experience = creditNow90_experience;
    }
}
