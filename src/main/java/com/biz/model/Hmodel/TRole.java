package com.biz.model.Hmodel;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="t_role")

public class TRole  implements java.io.Serializable {


    // Fields
    private String id;
    private String roleName;
    private String roleGroup;
    private String roleMark;
    private String iconCls;
    private Integer isdel=0;
    private Date createTime=new Date();


    // Constructors
    /** default constructor */
    public TRole() {
    }


    /** full constructor */
    public TRole(String id, String roleName, String roleGroup, String roleMark, String iconCls, Integer isdel, Date createTime) {
        this.id = id;
        this.roleName = roleName;
        this.roleGroup = roleGroup;
        this.roleMark = roleMark;
        this.iconCls = iconCls;
        this.isdel = isdel;
        this.createTime = createTime;
    }


    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")
    @Id
    @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="roleName")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Column(name="roleGroup")
    public String getRoleGroup() {
        return roleGroup;
    }

    public void setRoleGroup(String roleGroup) {
        this.roleGroup = roleGroup;
    }

    @Column(name="roleMark")
    public String getRoleMark() {
        return roleMark;
    }

    public void setRoleMark(String roleMark) {
        this.roleMark = roleMark;
    }

    @Column(name="iconCls")
    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    @Column(name="isdel")
    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    @Column(name="createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}