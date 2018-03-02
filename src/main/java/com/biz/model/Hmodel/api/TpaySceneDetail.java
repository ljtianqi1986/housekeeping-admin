package com.biz.model.Hmodel.api;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by qziwm on 2017/4/21.
 */
@Entity
@Table(name = "pay_scene_jlDetail")
public class TpaySceneDetail implements java.io.Serializable {
    private String id;
    private Integer mainId;
    private Integer isdel = 0;
    private Date createTime=new Date();
    private String brandId;
    private String shopId;
    private String userId;
    private Integer point90=0;
    private Double orderTotal=0.0;
    private Double commission=0.0;
    @GenericGenerator(name="generator", strategy="uuid.hex")@Id
    @GeneratedValue(generator="generator")

    @Column(name="id", unique=true, nullable=false)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @Column(name="mainId")
    public Integer getMainId() {
        return mainId;
    }

    public void setMainId(Integer mainId) {
        this.mainId = mainId;
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
    @Column(name="brandId")
    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }
    @Column(name="shopId")
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
    @Column(name="userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @Column(name="point90")
    public Integer getPoint90() {
        return point90;
    }

    public void setPoint90(Integer point90) {
        this.point90 = point90;
    }
    @Column(name="orderTotal")
    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }
    @Column(name="commission")
    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }
}
