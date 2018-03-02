package com.biz.model.Pmodel.api;

/**
 * Created by lzq on 2017/4/13.
 */
public class OrderBackDetail {
    private String detailId               = "";//order_detail 表id
    private String goodsCode        = "";
    private String goodsName        = "";
    private String goodsProperty    = "";
    private String goodsCount       = "";
    private String couponPay        = "";//用券额
    private String coinPay          = "";//用呗额
    private String servicePay       = "";//服务费金额
    private String ortherPay        = "";//额外支付
    private String detailState      = "";//子单状态
    private String goodsPrice       = "0";
    private int balanceType         =0;

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsProperty() {
        return goodsProperty;
    }

    public void setGoodsProperty(String goodsProperty) {
        this.goodsProperty = goodsProperty;
    }

    public String getGoodsCount() {
        return goodsCount;
    }

    public void setGoodsCount(String goodsCount) {
        this.goodsCount = goodsCount;
    }

    public String getCouponPay() {
        return couponPay;
    }

    public void setCouponPay(String couponPay) {
        this.couponPay = couponPay;
    }

    public String getCoinPay() {
        return coinPay;
    }

    public void setCoinPay(String coinPay) {
        this.coinPay = coinPay;
    }

    public String getServicePay() {
        return servicePay;
    }

    public void setServicePay(String servicePay) {
        this.servicePay = servicePay;
    }

    public String getOrtherPay() {
        return ortherPay;
    }

    public void setOrtherPay(String ortherPay) {
        this.ortherPay = ortherPay;
    }

    public String getDetailState() {
        return detailState;
    }

    public void setDetailState(String detailState) {
        this.detailState = detailState;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public int getBalanceType() {
        return balanceType;
    }

    public void setBalanceType(int balanceType) {
        this.balanceType = balanceType;
    }
}
