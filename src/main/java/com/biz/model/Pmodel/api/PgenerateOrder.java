package com.biz.model.Pmodel.api;
// default package

//接受立即支付订单生成参数
public class PgenerateOrder {

    private double total = 0;//90券金额
    private String shop_code;//店铺id，门店code
    private String only_code;//付款码
    private boolean is90coin=false;//是否用90币抵消（true 用90贝抵消,false 不用）
    private String user_code;//营业员,登录者code

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getShop_code() {
        return shop_code;
    }

    public void setShop_code(String shop_code) {
        this.shop_code = shop_code;
    }

    public String getOnly_code() {
        return only_code;
    }

    public void setOnly_code(String only_code) {
        this.only_code = only_code;
    }

    public boolean isIs90coin() {
        return is90coin;
    }

    public void setIs90coin(boolean is90coin) {
        this.is90coin = is90coin;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }
}