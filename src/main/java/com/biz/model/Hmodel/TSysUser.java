package com.biz.model.Hmodel;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_sys_user")

public class TSysUser implements java.io.Serializable {


    private String id;
    private Short type;
    private Short identity;
    private String identityCode;
    private String loginName;
    private String pwd;
    private String personName;
    private String mapId;
    private String roles;
    private Short sex;
    private String phone;
    private String email;
    private String fax;
    private String notes;
    private String coverId;
    private Short isLock;
    private Integer sorts;
    private Short isdel;
    private String createTime;
    private Integer isShop;


    // Constructors

    /** default constructor */
    public TSysUser() {
    }


    /** full constructor */
    public TSysUser(Short type, Short identity, String identityCode, String loginName, String pwd, String personName, String mapId, String roles, Short sex, String phone, String email, String fax, String notes, String coverId, Short isLock, Integer sorts, Short isdel, String createTime, Integer isShop) {
        this.type = type;
        this.identity = identity;
        this.identityCode = identityCode;
        this.loginName = loginName;
        this.pwd = pwd;
        this.personName = personName;
        this.mapId = mapId;
        this.roles = roles;
        this.sex = sex;
        this.phone = phone;
        this.email = email;
        this.fax = fax;
        this.notes = notes;
        this.coverId = coverId;
        this.isLock = isLock;
        this.sorts = sorts;
        this.isdel = isdel;
        this.createTime = createTime;
        this.isShop = isShop;
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

    @Column(name="type")

    public Short getType() {
        return this.type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    @Column(name="identity")

    public Short getIdentity() {
        return this.identity;
    }

    public void setIdentity(Short identity) {
        this.identity = identity;
    }

    @Column(name="identity_code", length=50)

    public String getIdentityCode() {
        return this.identityCode;
    }

    public void setIdentityCode(String identityCode) {
        this.identityCode = identityCode;
    }

    @Column(name="loginName")

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Column(name="pwd", length=100)

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Column(name="personName", length=50)

    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Column(name="mapId", length=50)

    public String getMapId() {
        return this.mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    @Column(name="roles", length=355)

    public String getRoles() {
        return this.roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    @Column(name="sex")

    public Short getSex() {
        return this.sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    @Column(name="phone")

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Column(name="email")

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name="fax")

    public String getFax() {
        return this.fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    @Column(name="notes")

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Column(name="coverId", length=300)

    public String getCoverId() {
        return this.coverId;
    }

    public void setCoverId(String coverId) {
        this.coverId = coverId;
    }

    @Column(name="isLock")

    public Short getIsLock() {
        return this.isLock;
    }

    public void setIsLock(Short isLock) {
        this.isLock = isLock;
    }

    @Column(name="sorts")

    public Integer getSorts() {
        return this.sorts;
    }

    public void setSorts(Integer sorts) {
        this.sorts = sorts;
    }

    @Column(name="isdel")

    public Short getIsdel() {
        return this.isdel;
    }

    public void setIsdel(Short isdel) {
        this.isdel = isdel;
    }

    @Column(name="createTime", length=19)

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    @Column(name="isShop")

    public Integer getIsShop() {
        return this.isShop;
    }

    public void setIsShop(Integer isShop) {
        this.isShop = isShop;
    }
}