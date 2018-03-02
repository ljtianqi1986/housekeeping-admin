package com.biz.model.Hmodel.api;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by lzq on 2017/4/25.
 */

@Entity
@Table(name="periodization_shop")
public class TperiodizationShop implements java.io.Serializable {

    private String id;
    private String shopId;
    private String userId;
    private Integer state;
    private Date createTime = new Date();
    private Integer isdel = 0;
    private String detailLogId;


    public TperiodizationShop(){}
    /** full constructor */
    public TperiodizationShop(String id, String shopId, String userId, Integer state, Date createTime, Integer isdel, String detailLogId) {
        this.id = id;
        this.shopId = shopId;
        this.userId = userId;
        this.state = state;
        this.createTime = createTime;
        this.isdel = isdel;
        this.detailLogId = detailLogId;
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


    @Column(name="shopId",length=50)
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Column(name="userId",length=50)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Column(name="state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name="createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name="isdel")
    public Integer getIsdel() {
        return isdel;
    }

    public void setIsdel(Integer isdel) {
        this.isdel = isdel;
    }

    @Column(name="detailLogId",length=50)
    public String getDetailLogId() {
        return detailLogId;
    }

    public void setDetailLogId(String detailLogId) {
        this.detailLogId = detailLogId;
    }
}
