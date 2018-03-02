package com.biz.model.Hmodel.api;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Created by ldd_person on 2017/5/2.
 */
@Entity
@javax.persistence.Table(name = "order_main_90", schema = "")
public class TOrderMain90 {
    private String id;

    @Id
    @javax.persistence.Column(name = "id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String code;

    @Basic
    @javax.persistence.Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    private BigDecimal orderTotal;

    @Basic
    @javax.persistence.Column(name = "order_total")
    public BigDecimal getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(BigDecimal orderTotal) {
        this.orderTotal = orderTotal;
    }

    private BigDecimal cashTotal;

    @Basic
    @javax.persistence.Column(name = "cash_total")
    public BigDecimal getCashTotal() {
        return cashTotal;
    }

    public void setCashTotal(BigDecimal cashTotal) {
        this.cashTotal = cashTotal;
    }

    private Short cashPayType;

    @Basic
    @javax.persistence.Column(name = "cash_payType")
    public Short getCashPayType() {
        return cashPayType;
    }

    public void setCashPayType(Short cashPayType) {
        this.cashPayType = cashPayType;
    }

    private BigDecimal cardTotal;

    @Basic
    @javax.persistence.Column(name = "card_total")
    public BigDecimal getCardTotal() {
        return cardTotal;
    }

    public void setCardTotal(BigDecimal cardTotal) {
        this.cardTotal = cardTotal;
    }

    private Short cardCount;

    @Basic
    @javax.persistence.Column(name = "card_count")
    public Short getCardCount() {
        return cardCount;
    }

    public void setCardCount(Short cardCount) {
        this.cardCount = cardCount;
    }

    private String openId;

    @Basic
    @javax.persistence.Column(name = "open_id")
    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    private String userCode;

    @Basic
    @javax.persistence.Column(name = "user_code")
    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    private String shopCode;

    @Basic
    @javax.persistence.Column(name = "shop_code")
    public String getShopCode() {
        return shopCode;
    }

    public void setShopCode(String shopCode) {
        this.shopCode = shopCode;
    }

    private String brandCode;

    @Basic
    @javax.persistence.Column(name = "brand_code")
    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    private Short state;

    @Basic
    @javax.persistence.Column(name = "state")
    public Short getState() {
        return state;
    }

    public void setState(Short state) {
        this.state = state;
    }

    private String payTime;

    @Basic
    @javax.persistence.Column(name = "pay_time")
    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    private String backCode;

    @Basic
    @javax.persistence.Column(name = "back_code")
    public String getBackCode() {
        return backCode;
    }

    public void setBackCode(String backCode) {
        this.backCode = backCode;
    }

    private String backTime;

    @Basic
    @javax.persistence.Column(name = "back_time")
    public String getBackTime() {
        return backTime;
    }

    public void setBackTime(String backTime) {
        this.backTime = backTime;
    }

    private String backUserCode;

    @Basic
    @javax.persistence.Column(name = "back_user_code")
    public String getBackUserCode() {
        return backUserCode;
    }

    public void setBackUserCode(String backUserCode) {
        this.backUserCode = backUserCode;
    }

    private String errorPayMsg;

    @Basic
    @javax.persistence.Column(name = "error_pay_msg")
    public String getErrorPayMsg() {
        return errorPayMsg;
    }

    public void setErrorPayMsg(String errorPayMsg) {
        this.errorPayMsg = errorPayMsg;
    }

    private String tradeType;

    @Basic
    @javax.persistence.Column(name = "trade_type")
    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    private Integer gift90;

    @Basic
    @javax.persistence.Column(name = "gift_90")
    public Integer getGift90() {
        return gift90;
    }

    public void setGift90(Integer gift90) {
        this.gift90 = gift90;
    }

    private Integer pay90;

    @Basic
    @javax.persistence.Column(name = "pay_90")
    public Integer getPay90() {
        return pay90;
    }

    public void setPay90(Integer pay90) {
        this.pay90 = pay90;
    }

    private Short payType;

    @Basic
    @javax.persistence.Column(name = "pay_type")
    public Short getPayType() {
        return payType;
    }

    public void setPayType(Short payType) {
        this.payType = payType;
    }

    private String payUserId;

    @Basic
    @javax.persistence.Column(name = "pay_user_id")
    public String getPayUserId() {
        return payUserId;
    }

    public void setPayUserId(String payUserId) {
        this.payUserId = payUserId;
    }

    private short isdel;

    @Basic
    @javax.persistence.Column(name = "isdel")
    public short getIsdel() {
        return isdel;
    }

    public void setIsdel(short isdel) {
        this.isdel = isdel;
    }

    private Timestamp createTime;

    @Basic
    @javax.persistence.Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    private String otherOrderCode;

    @Basic
    @javax.persistence.Column(name = "other_order_code")
    public String getOtherOrderCode() {
        return otherOrderCode;
    }

    public void setOtherOrderCode(String otherOrderCode) {
        this.otherOrderCode = otherOrderCode;
    }

    private BigDecimal ticketsTotal;

    @Basic
    @javax.persistence.Column(name = "tickets_total")
    public BigDecimal getTicketsTotal() {
        return ticketsTotal;
    }

    public void setTicketsTotal(BigDecimal ticketsTotal) {
        this.ticketsTotal = ticketsTotal;
    }

    private BigDecimal payCoin;

    @Basic
    @javax.persistence.Column(name = "pay_coin")
    public BigDecimal getPayCoin() {
        return payCoin;
    }

    public void setPayCoin(BigDecimal payCoin) {
        this.payCoin = payCoin;
    }

    private Short type;

    @Basic
    @javax.persistence.Column(name = "type")
    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    private Timestamp oldcreateTime;

    @Basic
    @javax.persistence.Column(name = "oldcreate_time")
    public Timestamp getOldcreateTime() {
        return oldcreateTime;
    }

    public void setOldcreateTime(Timestamp oldcreateTime) {
        this.oldcreateTime = oldcreateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TOrderMain90 that = (TOrderMain90) o;

        if (isdel != that.isdel) return false;
        if (backCode != null ? !backCode.equals(that.backCode) : that.backCode != null) return false;
        if (backTime != null ? !backTime.equals(that.backTime) : that.backTime != null) return false;
        if (backUserCode != null ? !backUserCode.equals(that.backUserCode) : that.backUserCode != null) return false;
        if (brandCode != null ? !brandCode.equals(that.brandCode) : that.brandCode != null) return false;
        if (cardCount != null ? !cardCount.equals(that.cardCount) : that.cardCount != null) return false;
        if (cardTotal != null ? !cardTotal.equals(that.cardTotal) : that.cardTotal != null) return false;
        if (cashPayType != null ? !cashPayType.equals(that.cashPayType) : that.cashPayType != null) return false;
        if (cashTotal != null ? !cashTotal.equals(that.cashTotal) : that.cashTotal != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (errorPayMsg != null ? !errorPayMsg.equals(that.errorPayMsg) : that.errorPayMsg != null) return false;
        if (gift90 != null ? !gift90.equals(that.gift90) : that.gift90 != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (oldcreateTime != null ? !oldcreateTime.equals(that.oldcreateTime) : that.oldcreateTime != null)
            return false;
        if (openId != null ? !openId.equals(that.openId) : that.openId != null) return false;
        if (orderTotal != null ? !orderTotal.equals(that.orderTotal) : that.orderTotal != null) return false;
        if (otherOrderCode != null ? !otherOrderCode.equals(that.otherOrderCode) : that.otherOrderCode != null)
            return false;
        if (pay90 != null ? !pay90.equals(that.pay90) : that.pay90 != null) return false;
        if (payCoin != null ? !payCoin.equals(that.payCoin) : that.payCoin != null) return false;
        if (payTime != null ? !payTime.equals(that.payTime) : that.payTime != null) return false;
        if (payType != null ? !payType.equals(that.payType) : that.payType != null) return false;
        if (payUserId != null ? !payUserId.equals(that.payUserId) : that.payUserId != null) return false;
        if (shopCode != null ? !shopCode.equals(that.shopCode) : that.shopCode != null) return false;
        if (state != null ? !state.equals(that.state) : that.state != null) return false;
        if (ticketsTotal != null ? !ticketsTotal.equals(that.ticketsTotal) : that.ticketsTotal != null) return false;
        if (tradeType != null ? !tradeType.equals(that.tradeType) : that.tradeType != null) return false;
        if (type != null ? !type.equals(that.type) : that.type != null) return false;
        if (userCode != null ? !userCode.equals(that.userCode) : that.userCode != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (orderTotal != null ? orderTotal.hashCode() : 0);
        result = 31 * result + (cashTotal != null ? cashTotal.hashCode() : 0);
        result = 31 * result + (cashPayType != null ? cashPayType.hashCode() : 0);
        result = 31 * result + (cardTotal != null ? cardTotal.hashCode() : 0);
        result = 31 * result + (cardCount != null ? cardCount.hashCode() : 0);
        result = 31 * result + (openId != null ? openId.hashCode() : 0);
        result = 31 * result + (userCode != null ? userCode.hashCode() : 0);
        result = 31 * result + (shopCode != null ? shopCode.hashCode() : 0);
        result = 31 * result + (brandCode != null ? brandCode.hashCode() : 0);
        result = 31 * result + (state != null ? state.hashCode() : 0);
        result = 31 * result + (payTime != null ? payTime.hashCode() : 0);
        result = 31 * result + (backCode != null ? backCode.hashCode() : 0);
        result = 31 * result + (backTime != null ? backTime.hashCode() : 0);
        result = 31 * result + (backUserCode != null ? backUserCode.hashCode() : 0);
        result = 31 * result + (errorPayMsg != null ? errorPayMsg.hashCode() : 0);
        result = 31 * result + (tradeType != null ? tradeType.hashCode() : 0);
        result = 31 * result + (gift90 != null ? gift90.hashCode() : 0);
        result = 31 * result + (pay90 != null ? pay90.hashCode() : 0);
        result = 31 * result + (payType != null ? payType.hashCode() : 0);
        result = 31 * result + (payUserId != null ? payUserId.hashCode() : 0);
        result = 31 * result + (int) isdel;
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (otherOrderCode != null ? otherOrderCode.hashCode() : 0);
        result = 31 * result + (ticketsTotal != null ? ticketsTotal.hashCode() : 0);
        result = 31 * result + (payCoin != null ? payCoin.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (oldcreateTime != null ? oldcreateTime.hashCode() : 0);
        return result;
    }
}
