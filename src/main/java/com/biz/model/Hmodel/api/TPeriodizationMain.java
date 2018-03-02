package com.biz.model.Hmodel.api;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lzq on 2017/4/25.
 */

@Entity
@Table(name="periodization_main")
public class TPeriodizationMain implements java.io.Serializable {


    private String id;
    private String orderId;
    private String userId;
    private Double couponTotal;
    private Integer state;
    private Date createTime = new Date();
    private Integer isdel = 0;
    private Integer isRelated = 0;
    private String shopId;


    public TPeriodizationMain(){};
    /** full constructor */
    public TPeriodizationMain(String id, String orderId, String userId ,
                              Integer state , Double couponTotal,Date createTime,
                              Integer isdel,Integer isRelated,String shopId){

        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.state = state;
        this.couponTotal = couponTotal;
        this.createTime = createTime;
        this.isdel = isdel;
        this.isRelated = isRelated;
        this.shopId = shopId;
    }


    // Property accessors
//    @GenericGenerator(name="generator", strategy="uuid.hex")
    @Id
//    @GeneratedValue(generator="generator")

    @Column(name="id", length=50)

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Column(name="orderId", length=80)
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name="userId", length=80)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name="couponTotal", length=80)
    public Double getCouponTotal() {
        return couponTotal;
    }

    public void setCouponTotal(Double couponTotal) {
        this.couponTotal = couponTotal;
    }

    @Column(name="state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name="createTime", length=80)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name="isdel", length=80)
    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    @Column(name="isRelated", length=80)
    public Integer getIsRelated() {
        return isRelated;
    }

    public void setIsRelated(Integer isRelated) {
        this.isRelated = isRelated;
    }

    @Column(name="shopId", length=80)
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
