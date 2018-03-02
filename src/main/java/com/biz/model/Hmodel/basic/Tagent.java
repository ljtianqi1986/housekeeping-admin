package com.biz.model.Hmodel.basic;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name="base_agent")

public class Tagent implements java.io.Serializable {


    // Fields    

     private String agent_code;
    private String old_code;
    private String agent_name;
    private Integer isdel=0;
    private Date create_time=new Date();
    private String flag;
    private Integer type=0;
    private String agent_p_code;
    private Integer sort=99;
    private String province;
    private String city;
    private String role_code;



    // Constructors

    /** default constructor */
    public Tagent() {
    }


   
    // Property accessors
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id @GeneratedValue(generator="generator")
    
    @Column(name="agent_code", unique=true, nullable=false)
    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }
    
    @Column(name="old_code")
    public String getOld_code() {
        return old_code;
    }

    public void setOld_code(String old_code) {
        this.old_code = old_code;
    }
    @Column(name="agent_name")
    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }
    @Column(name="isdel")
    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }
    @Column(name="create_time")
    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
    @Column(name="flag")
    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
    @Column(name="type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    @Column(name="agent_p_code")
    public String getAgent_p_code() {
        return agent_p_code;
    }

    public void setAgent_p_code(String agent_p_code) {
        this.agent_p_code = agent_p_code;
    }
    @Column(name="sort")
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
    @Column(name="province")
    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    @Column(name="city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    @Column(name="role_code")
    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }
}