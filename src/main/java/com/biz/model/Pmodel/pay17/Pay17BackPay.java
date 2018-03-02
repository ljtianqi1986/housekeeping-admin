package com.biz.model.Pmodel.pay17;

/**
 * Created by fp on 2017/3/14.
 */
public class Pay17BackPay {
    private Integer return_code = 0;//int(1) 0为失败或异常，1为成功
    private String return_info;//错误或异常信息，成功则为空
    private String err_code;//错误码
    /**
     * *********return_code为1即成功时，还会包括以下字段**********
     */
    /*private String shop_code;//商户门店标识
    private Integer back_total = 0;//退款金额 分
    private String order_code;//退款所属订单订单号
    private Integer trade_state = 0;//订单状态 0：未生效 1：退款成功 2：错误
    private String error_msg;//退款失败说明
    private String back_code;//退款单号
    private String create_time;//退款时间*/

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


}
