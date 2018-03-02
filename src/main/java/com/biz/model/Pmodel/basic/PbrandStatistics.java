package com.biz.model.Pmodel.basic;

import java.io.Serializable;

/**
 * Created by qziwm on 2017/4/18.
 */
public class PbrandStatistics implements Serializable {
    private String id;
    private String name;
    private String isLock;
    private String sendWay;
    private String outTime;
    private String newUsers;
    private String sendCount;
    private String sendAmount;
    private String commission;
    private String dxSendCount;
    private String dxSendAmount;
    private String dxCommission;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsLock() {
        return isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public String getSendWay() {
        return sendWay;
    }

    public void setSendWay(String sendWay) {
        this.sendWay = sendWay;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getNewUsers() {
        return newUsers;
    }

    public void setNewUsers(String newUsers) {
        this.newUsers = newUsers;
    }

    public String getSendCount() {
        return sendCount;
    }

    public void setSendCount(String sendCount) {
        this.sendCount = sendCount;
    }

    public String getSendAmount() {
        return sendAmount;
    }

    public void setSendAmount(String sendAmount) {
        this.sendAmount = sendAmount;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getDxSendCount() {
        return dxSendCount;
    }

    public void setDxSendCount(String dxSendCount) {
        this.dxSendCount = dxSendCount;
    }

    public String getDxSendAmount() {
        return dxSendAmount;
    }

    public void setDxSendAmount(String dxSendAmount) {
        this.dxSendAmount = dxSendAmount;
    }

    public String getDxCommission() {
        return dxCommission;
    }

    public void setDxCommission(String dxCommission) {
        this.dxCommission = dxCommission;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
