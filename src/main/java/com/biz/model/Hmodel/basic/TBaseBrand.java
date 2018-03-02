package com.biz.model.Hmodel.basic;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by liujiajia on 2017/1/18.
 */
@Entity
@Table(name = "base_brand")
public class TBaseBrand {
    private String brandCode;
    private String dxyCode;
    private String agentCode;
    private String name;
    private String logoUrl;
    private String categoryFirst;
    private String categorySecond;
    private short isdel;
    private Timestamp createTime;
    private Short islock;
    private String province;
    private String city;
    private Long balance90;
    private Integer creditTotal90;
    private Integer creditNow90;
    private BigDecimal commission;
    private BigDecimal procedures;
    private String bizCode;
    private Short is90;
    private String telephone;
    private String address;
    private String introduction;
    private String speedCode;
    private Integer pageviews;
    private Integer sort;
    private Integer sorts=99;
    private Double proportion;

    private Short iscoin;//pos机，手动发券，是否送90贝，0不送 1:送
    private Double coinproportion;//发贝比例0-1
    private Short isPeriodization; //商户是否分期发券 0：不分期  1：分期
    private Integer expiryDateType = 0;
    private Short isZeroCheck;
    private Integer ticketType;
    private Short isTicket;
    private Long balance90shop;
    private Integer creditTotal90shop;
    private Integer creditNow90shop;
    private Long balance90experience;
    private Integer creditTotal90experience;
    private Integer creditNow90experience;
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    @Column(name = "brand_code")
    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    @Basic
    @Column(name = "dxy_code")
    public String getDxyCode() {
        return dxyCode;
    }

    public void setDxyCode(String dxyCode) {
        this.dxyCode = dxyCode;
    }

    @Basic
    @Column(name = "agent_code")
    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "logo_url")
    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    @Basic
    @Column(name = "category_first")
    public String getCategoryFirst() {
        return categoryFirst;
    }

    public void setCategoryFirst(String categoryFirst) {
        this.categoryFirst = categoryFirst;
    }

    @Basic
    @Column(name = "category_second")
    public String getCategorySecond() {
        return categorySecond;
    }

    public void setCategorySecond(String categorySecond) {
        this.categorySecond = categorySecond;
    }

    @Basic
    @Column(name = "isdel")
    public short getIsdel() {
        return isdel;
    }

    public void setIsdel(short isdel) {
        this.isdel = isdel;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "islock")
    public Short getIslock() {
        return islock;
    }

    public void setIslock(Short islock) {
        this.islock = islock;
    }

    @Basic
    @Column(name = "province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "balance_90")
    public Long getBalance90() {
        return balance90;
    }

    public void setBalance90(Long balance90) {
        this.balance90 = balance90;
    }

    @Basic
    @Column(name = "credit_total_90")
    public Integer getCreditTotal90() {
        return creditTotal90;
    }

    public void setCreditTotal90(Integer creditTotal90) {
        this.creditTotal90 = creditTotal90;
    }

    @Basic
    @Column(name = "credit_now_90")
    public Integer getCreditNow90() {
        return creditNow90;
    }

    public void setCreditNow90(Integer creditNow90) {
        this.creditNow90 = creditNow90;
    }

    @Basic
    @Column(name = "commission")
    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    @Basic
    @Column(name = "procedures")
    public BigDecimal getProcedures() {
        return procedures;
    }

    public void setProcedures(BigDecimal procedures) {
        this.procedures = procedures;
    }

    @Basic
    @Column(name = "biz_code")
    public String getBizCode() {
        return bizCode;
    }

    public void setBizCode(String bizCode) {
        this.bizCode = bizCode;
    }

    @Basic
    @Column(name = "is_90")
    public Short getIs90() {
        return is90;
    }

    public void setIs90(Short is90) {
        this.is90 = is90;
    }

    @Basic
    @Column(name = "telephone")
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "introduction")
    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @Basic
    @Column(name = "speed_code")
    public String getSpeedCode() {
        return speedCode;
    }

    public void setSpeedCode(String speedCode) {
        this.speedCode = speedCode;
    }

    @Basic
    @Column(name = "pageviews")
    public Integer getPageviews() {
        return pageviews;
    }

    public void setPageviews(Integer pageviews) {
        this.pageviews = pageviews;
    }

    @Basic
    @Column(name = "sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    @Basic
    @Column(name = "proportion")
    public Double getProportion() {
        return proportion;
    }

    public void setProportion(Double proportion) {
        this.proportion = proportion;
    }

    @Basic
    @Column(name = "iscoin")
    public Short getIscoin() {
        return iscoin;
    }

    public void setIscoin(Short iscoin) {
        this.iscoin = iscoin;
    }

    @Basic
    @Column(name = "coinproportion")
    public Double getCoinproportion() {
        return coinproportion;
    }

    public void setCoinproportion(Double coinproportion) {
        this.coinproportion = coinproportion;
    }

    @Basic
    @Column(name = "isPeriodization")
    public Short getIsPeriodization() {
        return isPeriodization;
    }

    public void setIsPeriodization(Short isPeriodization) {
        this.isPeriodization = isPeriodization;
    }


    @Basic
    @Column(name = "expiryDateType")
    public Integer getExpiryDateType() {
        return expiryDateType;
    }

    public void setExpiryDateType(Integer expiryDateType) {
        this.expiryDateType = expiryDateType;
    }

    @Basic
    @Column(name = "isZeroCheck")
    public Short getIsZeroCheck() {
        return isZeroCheck;
    }

    public void setIsZeroCheck(Short isZeroCheck) {
        this.isZeroCheck = isZeroCheck;
    }
    @Basic
    @Column(name = "sorts")
    public Integer getSorts() {
        return sorts;
    }

    public void setSorts(Integer sorts) {
        this.sorts = sorts;
    }

    @Basic
    @Column(name = "ticketType")
    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    @Basic
    @Column(name = "isTicket")
    public Short getIsTicket() {
        return isTicket;
    }

    public void setIsTicket(Short isTicket) {
        this.isTicket = isTicket;
    }
    @Basic
    @Column(name = "balance_90_shop")
    public Long getBalance90shop() {
        return balance90shop;
    }

    public void setBalance90shop(Long balance90shop) {
        this.balance90shop = balance90shop;
    }
    @Basic
    @Column(name = "credit_total_90_shop")
    public Integer getCreditTotal90shop() {
        return creditTotal90shop;
    }

    public void setCreditTotal90shop(Integer creditTotal90shop) {
        this.creditTotal90shop = creditTotal90shop;
    }
    @Basic
    @Column(name = "credit_now_90_shop")
    public Integer getCreditNow90shop() {
        return creditNow90shop;
    }

    public void setCreditNow90shop(Integer creditNow90shop) {
        this.creditNow90shop = creditNow90shop;
    }
    @Basic
    @Column(name = "balance_90_experience")
    public Long getBalance90experience() {
        return balance90experience;
    }

    public void setBalance90experience(Long balance90experience) {
        this.balance90experience = balance90experience;
    }
    @Basic
    @Column(name = "credit_total_90_experience")
    public Integer getCreditTotal90experience() {
        return creditTotal90experience;
    }

    public void setCreditTotal90experience(Integer creditTotal90experience) {
        this.creditTotal90experience = creditTotal90experience;
    }
    @Basic
    @Column(name = "credit_now_90_experience")
    public Integer getCreditNow90experience() {
        return creditNow90experience;
    }

    public void setCreditNow90experience(Integer creditNow90experience) {
        this.creditNow90experience = creditNow90experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TBaseBrand that = (TBaseBrand) o;

        if (isdel != that.isdel) return false;
        if (brandCode != null ? !brandCode.equals(that.brandCode) : that.brandCode != null) return false;
        if (dxyCode != null ? !dxyCode.equals(that.dxyCode) : that.dxyCode != null) return false;
        if (agentCode != null ? !agentCode.equals(that.agentCode) : that.agentCode != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (logoUrl != null ? !logoUrl.equals(that.logoUrl) : that.logoUrl != null) return false;
        if (categoryFirst != null ? !categoryFirst.equals(that.categoryFirst) : that.categoryFirst != null)
            return false;
        if (categorySecond != null ? !categorySecond.equals(that.categorySecond) : that.categorySecond != null)
            return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (islock != null ? !islock.equals(that.islock) : that.islock != null) return false;
        if (province != null ? !province.equals(that.province) : that.province != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (balance90 != null ? !balance90.equals(that.balance90) : that.balance90 != null) return false;
        if (creditTotal90 != null ? !creditTotal90.equals(that.creditTotal90) : that.creditTotal90 != null)
            return false;
        if (creditNow90 != null ? !creditNow90.equals(that.creditNow90) : that.creditNow90 != null) return false;
        if (commission != null ? !commission.equals(that.commission) : that.commission != null) return false;
        if (procedures != null ? !procedures.equals(that.procedures) : that.procedures != null) return false;
        if (bizCode != null ? !bizCode.equals(that.bizCode) : that.bizCode != null) return false;
        if (is90 != null ? !is90.equals(that.is90) : that.is90 != null) return false;
        if (telephone != null ? !telephone.equals(that.telephone) : that.telephone != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (introduction != null ? !introduction.equals(that.introduction) : that.introduction != null) return false;
        if (speedCode != null ? !speedCode.equals(that.speedCode) : that.speedCode != null) return false;
        if (pageviews != null ? !pageviews.equals(that.pageviews) : that.pageviews != null) return false;
        if (sort != null ? !sort.equals(that.sort) : that.sort != null) return false;
        if (proportion != null ? !proportion.equals(that.proportion) : that.proportion != null) return false;
        if (iscoin != null ? !iscoin.equals(that.iscoin) : that.iscoin != null) return false;
        if (coinproportion != null ? !coinproportion.equals(that.coinproportion) : that.coinproportion != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = brandCode != null ? brandCode.hashCode() : 0;
        result = 31 * result + (dxyCode != null ? dxyCode.hashCode() : 0);
        result = 31 * result + (agentCode != null ? agentCode.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (logoUrl != null ? logoUrl.hashCode() : 0);
        result = 31 * result + (categoryFirst != null ? categoryFirst.hashCode() : 0);
        result = 31 * result + (categorySecond != null ? categorySecond.hashCode() : 0);
        result = 31 * result + (int) isdel;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (islock != null ? islock.hashCode() : 0);
        result = 31 * result + (province != null ? province.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (balance90 != null ? balance90.hashCode() : 0);
        result = 31 * result + (creditTotal90 != null ? creditTotal90.hashCode() : 0);
        result = 31 * result + (creditNow90 != null ? creditNow90.hashCode() : 0);
        result = 31 * result + (commission != null ? commission.hashCode() : 0);
        result = 31 * result + (procedures != null ? procedures.hashCode() : 0);
        result = 31 * result + (bizCode != null ? bizCode.hashCode() : 0);
        result = 31 * result + (is90 != null ? is90.hashCode() : 0);
        result = 31 * result + (telephone != null ? telephone.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (introduction != null ? introduction.hashCode() : 0);
        result = 31 * result + (speedCode != null ? speedCode.hashCode() : 0);
        result = 31 * result + (pageviews != null ? pageviews.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + (proportion != null ? proportion.hashCode() : 0);
        result = 31 * result + (iscoin != null ? iscoin.hashCode() : 0);
        result = 31 * result + (coinproportion != null ? coinproportion.hashCode() : 0);
        return result;
    }
}
