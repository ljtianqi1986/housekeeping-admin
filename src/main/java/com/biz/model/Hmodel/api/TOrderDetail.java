package com.biz.model.Hmodel.api;

import javax.persistence.*;
import java.lang.Integer;
import java.util.Date;

/**
 * Created by liujiajia on 2017/2/23.
 */
@Entity
@Table(name = "order_detail")
public class TOrderDetail {
    private String id;
    private String orderId;
    private String goodsId;
    private Integer count;
    private Double price;
    private Double goodsTotal;
    private Double payTotal;
    private String sendId;
    private String shopId;
    private Short state;
    private String buyNotes;
    private Integer delType;
    private Short isdel;
    private Date createTime;
    private Date confirmTimes;
    private Short goodsType;
    private String stockId;
    private Short isPayCoupons;
    private String transactionId;
    private Integer coinPayTotal;
    private String whareHouseId;
    private Date backTime;
    private Short confirmType;
    private double balance_90;
    private double balance_shopping_90;
    private double balance_experience_90;
    private Short isPayShopping;
    private Short isPayExperience;
    private double servicePayTotal;


    @Id
    @Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "orderId")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "goodsId")
    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    @Basic
    @Column(name = "count")
    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Basic
    @Column(name = "price")
    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Basic
    @Column(name = "goodsTotal")
    public Double getGoodsTotal() {
        return goodsTotal;
    }

    public void setGoodsTotal(Double goodsTotal) {
        this.goodsTotal = goodsTotal;
    }

    @Basic
    @Column(name = "payTotal")
    public Double getPayTotal() {
        return payTotal;
    }

    public void setPayTotal(Double payTotal) {
        this.payTotal = payTotal;
    }

    @Basic
    @Column(name = "sendId")
    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    @Basic
    @Column(name = "shopId")
    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    @Basic
    @Column(name = "state")
    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    @Basic
    @Column(name = "buyNotes")
    public String getBuyNotes() {
        return buyNotes;
    }

    public void setBuyNotes(String buyNotes) {
        this.buyNotes = buyNotes;
    }

    @Basic
    @Column(name = "delType")
    public Integer getDelType() {
        return delType;
    }

    public void setDelType(Integer delType) {
        this.delType = delType;
    }

    @Basic
    @Column(name = "isdel")
    public Short getIsdel() {
        return isdel;
    }

    public void setIsdel(Short isdel) {
        this.isdel = isdel;
    }


    @Basic
    @Column(name = "createTime")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "confirmTimes")
    public Date getConfirmTimes() {
        return confirmTimes;
    }

    public void setConfirmTimes(Date confirmTimes) {
        this.confirmTimes = confirmTimes;
    }


    @Basic
    @Column(name = "goodsType")
    public Short getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Short goodsType) {
        this.goodsType = goodsType;
    }

    @Basic
    @Column(name = "confirmType")
    public Short getConfirmType() {
        return confirmType;
    }

    public void setConfirmType(Short confirmType) {
        this.confirmType = confirmType;
    }

    @Basic
    @Column(name = "stockId")
    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    @Basic
    @Column(name = "isPayCoupons")
    public Short getIsPayCoupons() {
        return isPayCoupons;
    }

    public void setIsPayCoupons(Short isPayCoupons) {
        this.isPayCoupons = isPayCoupons;
    }

    @Basic
    @Column(name = "transaction_id")
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Basic
    @Column(name = "coinPayTotal")
    public Integer getCoinPayTotal() {
        return coinPayTotal;
    }

    public void setCoinPayTotal(Integer coinPayTotal) {
        this.coinPayTotal = coinPayTotal;
    }

    @Basic
    @Column(name = "whareHouseId")
    public String getWhareHouseId() {
        return whareHouseId;
    }

    public void setWhareHouseId(String whareHouseId) {
        this.whareHouseId = whareHouseId;
    }


    @Basic
    @Column(name = "backTime")
    public Date getBackTime() {
        return backTime;
    }

    public void setBackTime(Date backTime) {
        this.backTime = backTime;
    }


    public double getBalance_90() {
        return balance_90;
    }

    public void setBalance_90(double balance_90) {
        this.balance_90 = balance_90;
    }

    public double getBalance_shopping_90() {
        return balance_shopping_90;
    }

    public void setBalance_shopping_90(double balance_shopping_90) {
        this.balance_shopping_90 = balance_shopping_90;
    }

    public double getBalance_experience_90() {
        return balance_experience_90;
    }

    public void setBalance_experience_90(double balance_experience_90) {
        this.balance_experience_90 = balance_experience_90;
    }

    public Short getIsPayShopping() {
        return isPayShopping;
    }

    public void setIsPayShopping(Short isPayShopping) {
        this.isPayShopping = isPayShopping;
    }

    public Short getIsPayExperience() {
        return isPayExperience;
    }

    public void setIsPayExperience(Short isPayExperience) {
        this.isPayExperience = isPayExperience;
    }

    public double getServicePayTotal() {
        return servicePayTotal;
    }

    public void setServicePayTotal(double servicePayTotal) {
        this.servicePayTotal = servicePayTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TOrderDetail that = (TOrderDetail) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (orderId != null ? !orderId.equals(that.orderId) : that.orderId != null) return false;
        if (goodsId != null ? !goodsId.equals(that.goodsId) : that.goodsId != null) return false;
        if (count != null ? !count.equals(that.count) : that.count != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (goodsTotal != null ? !goodsTotal.equals(that.goodsTotal) : that.goodsTotal != null) return false;
        if (payTotal != null ? !payTotal.equals(that.payTotal) : that.payTotal != null) return false;
        if (sendId != null ? !sendId.equals(that.sendId) : that.sendId != null) return false;
        if (shopId != null ? !shopId.equals(that.shopId) : that.shopId != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (buyNotes != null ? !buyNotes.equals(that.buyNotes) : that.buyNotes != null) return false;
        if (delType != null ? !delType.equals(that.delType) : that.delType != null) return false;
        if (isdel != null ? !isdel.equals(that.isdel) : that.isdel != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (confirmTimes != null ? !confirmTimes.equals(that.confirmTimes) : that.confirmTimes != null) return false;
        if (goodsType != null ? !goodsType.equals(that.goodsType) : that.goodsType != null) return false;
        if (stockId != null ? !stockId.equals(that.stockId) : that.stockId != null) return false;
        if (isPayCoupons != null ? !isPayCoupons.equals(that.isPayCoupons) : that.isPayCoupons != null) return false;
        if (transactionId != null ? !transactionId.equals(that.transactionId) : that.transactionId != null)
            return false;
        if (coinPayTotal != null ? !coinPayTotal.equals(that.coinPayTotal) : that.coinPayTotal != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (orderId != null ? orderId.hashCode() : 0);
        result = 31 * result + (goodsId != null ? goodsId.hashCode() : 0);
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (goodsTotal != null ? goodsTotal.hashCode() : 0);
        result = 31 * result + (payTotal != null ? payTotal.hashCode() : 0);
        result = 31 * result + (sendId != null ? sendId.hashCode() : 0);
        result = 31 * result + (shopId != null ? shopId.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (buyNotes != null ? buyNotes.hashCode() : 0);
        result = 31 * result + (delType != null ? delType.hashCode() : 0);
        result = 31 * result + (isdel != null ? isdel.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (confirmTimes != null ? confirmTimes.hashCode() : 0);
        result = 31 * result + (goodsType != null ? goodsType.hashCode() : 0);
        result = 31 * result + (stockId != null ? stockId.hashCode() : 0);
        result = 31 * result + (isPayCoupons != null ? isPayCoupons.hashCode() : 0);
        result = 31 * result + (transactionId != null ? transactionId.hashCode() : 0);
        result = 31 * result + (coinPayTotal != null ? coinPayTotal.hashCode() : 0);
        return result;
    }
}
