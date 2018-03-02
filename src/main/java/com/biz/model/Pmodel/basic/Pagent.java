package com.biz.model.Pmodel.basic;
// default package

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;


/**
 * TUser entity. @author MyEclipse Persistence Tools
 */

public class Pagent implements java.io.Serializable {


    // Fields    

    private String agent_code;
    private String old_code;
    private String agent_name;
    private String isdel;
    private String create_time;
    private String flag;
    private String type;
    private String agent_p_code;
    private String agent_p_name;
    private String sort;
    private String province;
    private String city;
    private String role_code;
    private String role_Name;
    private String provinceName;
    private String cityName;
    private String role_codeName;
    private String loginName;
    private String pwd;
    private String userCode;
    public String getAgent_code() {
        return agent_code;
    }

    public void setAgent_code(String agent_code) {
        this.agent_code = agent_code;
    }

    public String getOld_code() {
        return old_code;
    }

    public void setOld_code(String old_code) {
        this.old_code = old_code;
    }

    public String getAgent_name() {
        return agent_name;
    }

    public void setAgent_name(String agent_name) {
        this.agent_name = agent_name;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAgent_p_code() {
        return agent_p_code;
    }

    public void setAgent_p_code(String agent_p_code) {
        this.agent_p_code = agent_p_code;
    }

    public String getAgent_p_name() {
        return agent_p_name;
    }

    public void setAgent_p_name(String agent_p_name) {
        this.agent_p_name = agent_p_name;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
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

    public String getRole_code() {
        return role_code;
    }

    public void setRole_code(String role_code) {
        this.role_code = role_code;
    }

    public String getRole_Name() {
        return role_Name;
    }

    public void setRole_Name(String role_Name) {
        this.role_Name = role_Name;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRole_codeName() {
        return role_codeName;
    }

    public void setRole_codeName(String role_codeName) {
        this.role_codeName = role_codeName;
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
}