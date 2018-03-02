package com.biz.model.Pmodel.api;

/**
 * Created by lzq on 2017/4/19.
 */
public class SectionSum {

    private String userCode             = "";       //收银员id
    private String userName             = "";       //收银员
    private String tradeCount           = "0";      //交易笔数
    private String payCoupons           = "0.00";   //兑换券额(销券)
    private String giveCoupons          = "0.00";   //发放券额
    private String backCount            = "0";      //退款笔数
    private String backCoupons          = "0.00";   //退款券额
    private String backMoney            = "0.00";   //退款金额
    private String serviceMoney         = "0.00";   //服务费金额
    private String aliPayTotal          = "0.00";   //支付宝总额
    private String wxPayTotal           = "0.00";   //微信支付总额
    private String cashPayTotal         = "0.00";   //现金支付总额
    private String coinPayTotal         = "0.00";   //久零呗支付总额
    private String giveCount            = "0";      //发券次数
    private String unionPayTotal        = "0.00";   //银联支付

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTradeCount() {
        return tradeCount;
    }

    public void setTradeCount(String tradeCount) {
        this.tradeCount = tradeCount;
    }

    public String getPayCoupons() {
        return payCoupons;
    }

    public void setPayCoupons(String payCoupons) {
        this.payCoupons = payCoupons;
    }

    public String getGiveCoupons() {
        return giveCoupons;
    }

    public void setGiveCoupons(String giveCoupons) {
        this.giveCoupons = giveCoupons;
    }

    public String getBackCount() {
        return backCount;
    }

    public void setBackCount(String backCount) {
        this.backCount = backCount;
    }

    public String getBackCoupons() {
        return backCoupons;
    }

    public void setBackCoupons(String backCoupons) {
        this.backCoupons = backCoupons;
    }

    public String getBackMoney() {
        return backMoney;
    }

    public void setBackMoney(String backMoney) {
        this.backMoney = backMoney;
    }

    public String getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(String serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public String getAliPayTotal() {
        return aliPayTotal;
    }

    public void setAliPayTotal(String aliPayTotal) {
        this.aliPayTotal = aliPayTotal;
    }

    public String getWxPayTotal() {
        return wxPayTotal;
    }

    public void setWxPayTotal(String wxPayTotal) {
        this.wxPayTotal = wxPayTotal;
    }

    public String getCashPayTotal() {
        return cashPayTotal;
    }

    public void setCashPayTotal(String cashPayTotal) {
        this.cashPayTotal = cashPayTotal;
    }

    public String getCoinPayTotal() {
        return coinPayTotal;
    }

    public void setCoinPayTotal(String coinPayTotal) {
        this.coinPayTotal = coinPayTotal;
    }

    public String getGiveCount() {
        return giveCount;
    }

    public void setGiveCount(String giveCount) {
        this.giveCount = giveCount;
    }

    public String getUnionPayTotal() {
        return unionPayTotal;
    }

    public void setUnionPayTotal(String unionPayTotal) {
        this.unionPayTotal = unionPayTotal;
    }
}
