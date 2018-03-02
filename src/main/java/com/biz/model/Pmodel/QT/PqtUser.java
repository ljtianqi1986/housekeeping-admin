package com.biz.model.Pmodel.QT;
// default package


/**
 * TUser entity. @author MyEclipse Persistence Tools
 */
//offline_card_90 主表
public class PqtUser {

    private String dxy_code;//店小翼code
    private String money_fixed;//是否固定收银金额 0：不固定 ;1：固定
    private String person_name;
    private Integer type = -1;//类别
    private String shop_name;//店铺名称
    private String shop_logo;//店铺logo
    private String identity_code;//代理商code/品牌code/门店CODE/久零门店 code
    private String login_name;//登录名
    private String user_code;//用户code
    private String phone;//用户手机
    private Integer initial_money = 0;//初始收银金额，默认：0 单位：分
    private String pwd;


    public String getDxy_code() {
        return dxy_code;
    }

    public void setDxy_code(String dxy_code) {
        this.dxy_code = dxy_code;
    }

    public String getMoney_fixed() {
        return money_fixed;
    }

    public void setMoney_fixed(String money_fixed) {
        this.money_fixed = money_fixed;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getShop_logo() {
        return shop_logo;
    }

    public void setShop_logo(String shop_logo) {
        this.shop_logo = shop_logo;
    }

    public String getIdentity_code() {
        return identity_code;
    }

    public void setIdentity_code(String identity_code) {
        this.identity_code = identity_code;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getInitial_money() {
        return initial_money;
    }

    public void setInitial_money(Integer initial_money) {
        this.initial_money = initial_money;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}