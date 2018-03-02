package com.biz.service.api;

import com.biz.model.Pmodel.PorderMainUnion;

/**
 * Created by lzq on 2017/2/9.
 */
public interface WxUtilServiceI {

    /**
     * 获取token
     */
    String getAccessToken() throws Exception;


    /**
     * 获取jsApiTicket
     * @return
     */
    String getJsApiTicket() throws Exception;

    /**
     * 推送退款信息（90体验店）
     */
     void send_refund_template(String back_code, String code,String openId, Integer int_total) throws Exception;

    void send_pay_template(int scene_id, String open_id, PorderMainUnion orderMainUnion) throws Exception;

    String getQrcodeTicket(String code,int couponsMoney) throws Exception;

    String getAccessToken_bd() throws Exception;
}
