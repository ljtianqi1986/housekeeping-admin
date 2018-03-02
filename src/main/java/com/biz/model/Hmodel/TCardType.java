package com.biz.model.Hmodel;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by tomchen on 17/2/4.
 */
@Entity
@Table(name="offline_card_90_type")
public class TCardType {

    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")
    @Column(name="id", unique=true, nullable=false)
    private String id = "";

    @Column(name="name")
    private String name = "";

    @Column(name="percentage")
    private double percentage;

    @Column(name="userId")
    private String userId;

    @Column(name="isdel")
    private int isdel;

    @Column(name="createTime")
    private Date createTime;

    @Column(name="lastUpdateTime")
    private Date lastUpdateTime;

    @Column(name="lastUpadateUserId")
    private String lastUpadateUserId;

    @Column(name="agentId")
    private String agentId;
    @Column(name="isFirst")
    private Integer isFirst=0;
    public TCardType() {
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

    public String getLastUpadateUserId() {
        return lastUpadateUserId;
    }

    public void setLastUpadateUserId(String lastUpadateUserId) {
        this.lastUpadateUserId = lastUpadateUserId;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Integer getIsFirst() {
        return isFirst;
    }

    public void setIsFirst(Integer isFirst) {
        this.isFirst = isFirst;
    }
}
