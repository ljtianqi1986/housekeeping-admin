package com.biz.model.Pmodel.api;

/**
 * 直接付款订单，支付参数
 */
public class PatOrderMain90Parameter {

    private String orderCode;//订单code 即 id
    private String openid;//付款人openid
    private double order_total = 0.0;//用90币支付金额
    private double iscoin_total = 0.0;//额外支付金额
    private String author_code;//微信支付宝支付码, （开头） 判断1为微信（MICROPAY），2为支付宝（ZFB-MICROPAY），3：现金付款,4银联支付
    private String device_info = "";
    private String device_ip = "";
    private String balance_type = "0";


    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public double getOrder_total() {
        return order_total;
    }

    public void setOrder_total(double order_total) {
        this.order_total = order_total;
    }

    public double getIscoin_total() {
        return iscoin_total;
    }

    public void setIscoin_total(double iscoin_total) {
        this.iscoin_total = iscoin_total;
    }

    public String getAuthor_code() {
        return author_code;
    }

    public void setAuthor_code(String author_code) {
        this.author_code = author_code;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public String getBalance_type() {
        return balance_type;
    }

    public void setBalance_type(String balance_type) {
        this.balance_type = balance_type;
    }
}
