package com.biz.model.Pmodel;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by tomchen on 17/2/4.
 */
public class PcardType implements Serializable {

    private String id = "";

    private String name = "";

    private double percentage;

    private String userId;

    private int isdel;

    private Date createTime;

    private Date lastUpdateTime;

    private Date lastUpadateUserId;

    private String agentId;

    private String  isFirst;
    public PcardType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Date getLastUpadateUserId() {
        return lastUpadateUserId;
    }

    public void setLastUpadateUserId(Date lastUpadateUserId) {
        this.lastUpadateUserId = lastUpadateUserId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(String isFirst) {
        this.isFirst = isFirst;
    }
}
