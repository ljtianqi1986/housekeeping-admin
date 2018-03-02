package com.biz.model.Pmodel.pay17;

/**
 * Created by fp on 2017/3/14.
 */
public class Pay17SelectPay {
    private Integer return_code = 0;//int(1) 0为失败或异常，1为成功
    private String return_info;//错误或异常信息，成功则为空
    //private String err_code;//接口状态返回码，错误信息提示

    /************return_code为1即成功时，还会包括以下字段***********/
    private double trade_state = 0;//int 0：未生效 1：交易成功 2：错误 3：已退款 4：部分退款 5：用户支付中
    private String shop_code;//商户门店标识
    private String user_code;//收银员标识
    private Integer total = 0;//int(9) 订单金额 单位：分
    private String order_code;//商户订单号
    private String customer_id ;//支付宝或微信的用户ID
    private String out_trade_no;//第三方交易单号 out_trade_no
    private Integer is_account=-1;//0非二清，1二清
    private String account_id;//收款账号


    private String trade_type;//支付类型
    // MICROPAY:刷卡支付
    // NATIVE:扫码支付
    // JSAPI:公众号支付
    // ZFB-MICROPAY:支付宝刷卡支付
    // ZFB-NATIVE:支付宝扫码支付
    // YZF-MICROPAY:翼支付刷卡支付
    // XY-JSAPI:兴业微信公众号支付
    // XY-MICROPAY:兴业刷卡支付
    // XY-ZFB-MICROPAY:兴业支付宝刷卡付

    public Integer getReturn_code() {
        return return_code;
    }

    public void setReturn_code(Integer return_code) {
        this.return_code = return_code;
    }

    public String getReturn_info() {
        return return_info;
    }

    public void setReturn_info(String return_info) {
        this.return_info = return_info;
    }

    public double getTrade_state() {
        return trade_state;
    }

    public void setTrade_state(double trade_state) {
        this.trade_state = trade_state;
    }

    public String getShop_code() {
        return shop_code;
    }

    public void setShop_code(String shop_code) {
        this.shop_code = shop_code;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getOrder_code() {
        return order_code;
    }

    public void setOrder_code(String order_code) {
        this.order_code = order_code;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public Integer getIs_account() {
        return is_account;
    }

    public void setIs_account(Integer is_account) {
        this.is_account = is_account;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
