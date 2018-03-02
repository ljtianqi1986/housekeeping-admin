package com.biz.service.api;

import com.biz.service.base.BaseServiceI;
import net.sf.json.JSONObject;

import java.util.Map;


/**
 * Created by lzq on 2017/2/4.
 */
public interface WxTemplateServiceI extends BaseServiceI<Object> {
    JSONObject send_kf_template(String open_id,String content);
    void send_pay_template(int scene_id,String open_id,double jl_total,double money,String Pay_time) throws Exception;

    /**
     * 发送退款申请处理信息模板消息
     */
    void sendOrderRefundDelayInfo(String orderId,String remark,String state);


    /**
     * 发送兑换成功模板消息
     */
    Map<String, Object> sendChangeNoticeTemplate(String goodsName, String changeNumber, String changeCount, String remark, String openId);

}
