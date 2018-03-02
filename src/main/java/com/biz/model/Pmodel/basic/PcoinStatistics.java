package com.biz.model.Pmodel.basic;

import java.io.Serializable;

/**
 * Created by qziwm on 2017/4/17.
 */
public class PcoinStatistics implements Serializable {
    private String id;
    private String userId;
    private String chargeAmount;
    private String giveAmount;
    private String extraAmount;
    private String personName;
    private String phone;
    private String orderCount;
    private String orderAmount;
    private String coinAll;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChargeAmount() {
        return chargeAmount;
    }

    public void setChargeAmount(String chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public String getGiveAmount() {
        return giveAmount;
    }

    public void setGiveAmount(String giveAmount) {
        this.giveAmount = giveAmount;
    }

    public String getExtraAmount() {
        return extraAmount;
    }

    public void setExtraAmount(String extraAmount) {
        this.extraAmount = extraAmount;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCoinAll() {
        return coinAll;
    }

    public void setCoinAll(String coinAll) {
        this.coinAll = coinAll;
    }
}
