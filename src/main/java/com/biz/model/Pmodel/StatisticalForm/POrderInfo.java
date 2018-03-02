package com.biz.model.Pmodel.StatisticalForm;

/**
 * 收银员销售统计
 *
 * @author Atom
 */
public class POrderInfo {

    private String code;//订单编号
    private String createTime;//订单时间

    private String payCoupon;//90券金额
    private String payCouponJL;//90券金额
    private String payCouponLG;//90券金额
    private String payCouponTY;//90券金额
    private String coinPayTotal;//90贝支付金额
    private String order_total;//额外支付金额
    private String cash_total;//服务费合计

    private String orderCount;//订单数量

    private String salesUserCode;//导购员id
    private String salesName;//获得名称
    private String salesPhone;//账户

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayCoupon() {
        return payCoupon;
    }

    public void setPayCoupon(String payCoupon) {
        this.payCoupon = payCoupon;
    }

    public String getCoinPayTotal() {
        return coinPayTotal;
    }

    public void setCoinPayTotal(String coinPayTotal) {
        this.coinPayTotal = coinPayTotal;
    }

    public String getOrder_total() {
        return order_total;
    }

    public void setOrder_total(String order_total) {
        this.order_total = order_total;
    }

    public String getCash_total() {
        return cash_total;
    }

    public void setCash_total(String cash_total) {
        this.cash_total = cash_total;
    }

    public String getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(String orderCount) {
        this.orderCount = orderCount;
    }

    public String getSalesUserCode() {
        return salesUserCode;
    }

    public void setSalesUserCode(String salesUserCode) {
        this.salesUserCode = salesUserCode;
    }

    public String getSalesName() {
        return salesName;
    }

    public void setSalesName(String salesName) {
        this.salesName = salesName;
    }

    public String getSalesPhone() {
        return salesPhone;
    }

    public void setSalesPhone(String salesPhone) {
        this.salesPhone = salesPhone;
    }

    public String getPayCouponJL() {
        return payCouponJL;
    }

    public void setPayCouponJL(String payCouponJL) {
        this.payCouponJL = payCouponJL;
    }

    public String getPayCouponLG() {
        return payCouponLG;
    }

    public void setPayCouponLG(String payCouponLG) {
        this.payCouponLG = payCouponLG;
    }

    public String getPayCouponTY() {
        return payCouponTY;
    }

    public void setPayCouponTY(String payCouponTY) {
        this.payCouponTY = payCouponTY;
    }
}