package com.biz.model.Pmodel.basic;
// default package


import org.glassfish.grizzly.strategies.SameThreadIOStrategy;

/**
 * TUser entity. @author MyEclipse Persistence Tools
 */

public class PbaseDetail implements java.io.Serializable {

    private String id;
    private String agentId;
    private String agentName;
    private String brandId;
    private String brandName;
    private String shopId;
    private String shopIdName;
    private String sourceId;
    private String sourceName;
    private String userId;
    private String userName;
    private String userPhone;
    private String userCode;
    private String operUserName;
    private String openId;
    private String source;
    private String sourceMsg;
    private String type;
    private String inOut;
    private String point90z;
    private String point90s;
    private String point90k;
    private String pcount;
    private String cancelTime;
    private String isdel;
    private String createTime;
    private String orderTotal;
    private String orderState;
    private String tradeType;
    private String commission;
    private String count;

    private String cardName;
    private String cardType;
    private String cardCode;

    private String person_name;
    private String phone;
    private String shopName;
    private String state;
    private String servicePayTotal;
    private String payType;
    private String orderType;

    private String orderId;
    private String Money;
    private String couponsMoney;


    private String point90;//实际订单赠送的券点
    private double point90Now;//使用了多少
    private Integer writeOffType=0;//0:未知状态，1:已核销完，2:从未核销 3：只核销了部分
    private Integer writeOffState=0;//0正常1过期

    private Integer  ticketType=0;

    private String balance_90;
    private String balance_90_shop;
    private String balance_90_experience;



    public Integer getWriteOffState() {
        return writeOffState;
    }

    public void setWriteOffState(Integer writeOffState) {
        this.writeOffState = writeOffState;
    }

    public double getPoint90Now() {
        return point90Now;
    }

    public void setPoint90Now(double point90Now) {
        this.point90Now = point90Now;
    }

    public Integer getWriteOffType() {
        return writeOffType;
    }

    public void setWriteOffType(Integer writeOffType) {
        this.writeOffType = writeOffType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopIdName() {
        return shopIdName;
    }

    public void setShopIdName(String shopIdName) {
        this.shopIdName = shopIdName;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSourceMsg() {
        return sourceMsg;
    }

    public void setSourceMsg(String sourceMsg) {
        this.sourceMsg = sourceMsg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInOut() {
        return inOut;
    }

    public void setInOut(String inOut) {
        this.inOut = inOut;
    }

    public String getPoint90() {
        return point90;
    }

    public void setPoint90(String point90) {
        this.point90 = point90;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getIsdel() {
        return isdel;
    }

    public void setIsdel(String isdel) {
        this.isdel = isdel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardCode() {
        return cardCode;
    }

    public void setCardCode(String cardCode) {
        this.cardCode = cardCode;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPoint90z() {
        return point90z;
    }

    public void setPoint90z(String point90z) {
        this.point90z = point90z;
    }

    public String getPoint90s() {
        return point90s;
    }

    public void setPoint90s(String point90s) {
        this.point90s = point90s;
    }

    public String getPoint90k() {
        return point90k;
    }

    public void setPoint90k(String point90k) {
        this.point90k = point90k;
    }

    public String getPcount() {
        return pcount;
    }

    public void setPcount(String pcount) {
        this.pcount = pcount;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getServicePayTotal() {
        return servicePayTotal;
    }

    public void setServicePayTotal(String servicePayTotal) {
        this.servicePayTotal = servicePayTotal;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getCouponsMoney() {
        return couponsMoney;
    }

    public void setCouponsMoney(String couponsMoney) {
        this.couponsMoney = couponsMoney;
    }

    public Integer getTicketType() {
        return ticketType;
    }

    public void setTicketType(Integer ticketType) {
        this.ticketType = ticketType;
    }

    public String getBalance_90() {
        return balance_90;
    }

    public void setBalance_90(String balance_90) {
        this.balance_90 = balance_90;
    }

    public String getBalance_90_shop() {
        return balance_90_shop;
    }

    public void setBalance_90_shop(String balance_90_shop) {
        this.balance_90_shop = balance_90_shop;
    }

    public String getBalance_90_experience() {
        return balance_90_experience;
    }

    public void setBalance_90_experience(String balance_90_experience) {
        this.balance_90_experience = balance_90_experience;
    }
}