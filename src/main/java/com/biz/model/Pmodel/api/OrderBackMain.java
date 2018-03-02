package com.biz.model.Pmodel.api;

import java.util.List;

/**
 * Created by lzq on 2017/4/13.
 */
public class OrderBackMain {
    private String code                         = "";//订单号
    private String personName                   = "";//客户昵称
    private String userName                     = "";//收银员
    private String payType                      = "";//支付方式
    private String state                        = "";//订单状态
    private String payTime                      = "";//付款时间
    private List<OrderBackDetail> detailList    = null;//子单列表

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public List<OrderBackDetail> getDetailList() {
        return detailList;
    }

    public void setDetailList(List<OrderBackDetail> detailList) {
        this.detailList = detailList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
