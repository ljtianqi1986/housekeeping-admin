package com.biz.model.Hmodel;// default package

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


/**
 * TBaseUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="base_user")

public class TBaseUser  implements java.io.Serializable {


    // Fields    

     private String id;
     private String openId;
     private String personName;
     private Short sex;
     private String phone;
     private String cover;
     private Long balanceCash;
     private Long balance90;
     private Short state;
     private String country;
     private String province;
     private String city;
     private String birthday;
     private String scanAliId;
     private String scanYiId;
     private String scanYhkId;
     private String xyOpenid;
     private Short isdel;
     private String createTime;
     private String onlyCode;
     private Long balance90Total;


    // Constructors

    /** default constructor */
    public TBaseUser() {
    }

	/** minimal constructor */
    public TBaseUser(Short isdel, String createTime) {
        this.isdel = isdel;
        this.createTime = createTime;
    }
    
    /** full constructor */
    public TBaseUser(String openId, String personName, Short sex, String phone, String cover, Long balanceCash, Long balance90, Short state, String country, String province, String city, String birthday, String scanAliId, String scanYiId, String scanYhkId, String xyOpenid, Short isdel, String createTime, String onlyCode, Long balance90Total) {
        this.openId = openId;
        this.personName = personName;
        this.sex = sex;
        this.phone = phone;
        this.cover = cover;
        this.balanceCash = balanceCash;
        this.balance90 = balance90;
        this.state = state;
        this.country = country;
        this.province = province;
        this.city = city;
        this.birthday = birthday;
        this.scanAliId = scanAliId;
        this.scanYiId = scanYiId;
        this.scanYhkId = scanYhkId;
        this.xyOpenid = xyOpenid;
        this.isdel = isdel;
        this.createTime = createTime;
        this.onlyCode = onlyCode;
        this.balance90Total = balance90Total;
    }

   
    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    
    @Column(name="id", unique=true, nullable=false, length=50)

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    @Column(name="open_id", length=80)

    public String getOpenId() {
        return this.openId;
    }
    
    public void setOpenId(String openId) {
        this.openId = openId;
    }
    
    @Column(name="person_name", length=200)

    public String getPersonName() {
        return this.personName;
    }
    
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
    @Column(name="sex")

    public Short getSex() {
        return this.sex;
    }
    
    public void setSex(Short sex) {
        this.sex = sex;
    }
    
    @Column(name="phone", length=50)

    public String getPhone() {
        return this.phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    @Column(name="cover", length=200)

    public String getCover() {
        return this.cover;
    }
    
    public void setCover(String cover) {
        this.cover = cover;
    }
    
    @Column(name="balance_cash")

    public Long getBalanceCash() {
        return this.balanceCash;
    }
    
    public void setBalanceCash(Long balanceCash) {
        this.balanceCash = balanceCash;
    }
    
    @Column(name="balance_90")

    public Long getBalance90() {
        return this.balance90;
    }
    
    public void setBalance90(Long balance90) {
        this.balance90 = balance90;
    }
    
    @Column(name="state")

    public Short getState() {
        return this.state;
    }
    
    public void setState(Short state) {
        this.state = state;
    }
    
    @Column(name="country", length=150)

    public String getCountry() {
        return this.country;
    }
    
    public void setCountry(String country) {
        this.country = country;
    }
    
    @Column(name="province", length=150)

    public String getProvince() {
        return this.province;
    }
    
    public void setProvince(String province) {
        this.province = province;
    }
    
    @Column(name="city", length=150)

    public String getCity() {
        return this.city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    @Column(name="birthday", length=50)

    public String getBirthday() {
        return this.birthday;
    }
    
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    
    @Column(name="scan_ali_id", length=50)

    public String getScanAliId() {
        return this.scanAliId;
    }
    
    public void setScanAliId(String scanAliId) {
        this.scanAliId = scanAliId;
    }
    
    @Column(name="scan_yi_id", length=50)

    public String getScanYiId() {
        return this.scanYiId;
    }
    
    public void setScanYiId(String scanYiId) {
        this.scanYiId = scanYiId;
    }
    
    @Column(name="scan_yhk_id", length=50)

    public String getScanYhkId() {
        return this.scanYhkId;
    }
    
    public void setScanYhkId(String scanYhkId) {
        this.scanYhkId = scanYhkId;
    }
    
    @Column(name="xy_openid", length=100)

    public String getXyOpenid() {
        return this.xyOpenid;
    }
    
    public void setXyOpenid(String xyOpenid) {
        this.xyOpenid = xyOpenid;
    }
    
    @Column(name="isdel", nullable=false)

    public Short getIsdel() {
        return this.isdel;
    }
    
    public void setIsdel(Short isdel) {
        this.isdel = isdel;
    }
    
    @Column(name="create_time", nullable=false, length=19)

    public String getCreateTime() {
        return this.createTime;
    }
    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    
    @Column(name="only_code", length=20)

    public String getOnlyCode() {
        return this.onlyCode;
    }
    
    public void setOnlyCode(String onlyCode) {
        this.onlyCode = onlyCode;
    }
    
    @Column(name="balance_90_total")

    public Long getBalance90Total() {
        return this.balance90Total;
    }
    
    public void setBalance90Total(Long balance90Total) {
        this.balance90Total = balance90Total;
    }

}