package com.biz.service.api;

import com.biz.model.Pmodel.api.ConfirmAnOrder;
import com.biz.service.base.BaseServiceI;

import java.util.Map;

public interface MyOrderServiceI extends BaseServiceI<Object> {

	Map<String, String> addproduceOrder(ConfirmAnOrder orderList) throws Exception;
    Map<String, Object> PayOrderMain90(String jsonSting) throws Exception;
    Map<String,String> changeOrderState(String ids, String states, String mainStates) throws Exception;
    Map<String, Object> addGenerateOrderMain90(String jsonSting) throws Exception;
    Map<String, Object> Verification_Payment_Code(String shop_code,String only_code,String total,String iscoin) throws Exception;
    Map<String,Object> getOrdersByBrandId(String brandId, String code, String beginTime, String endTime, String page, String rows)throws Exception;
    Map<String, Object> Verification_Payment_Phone(String shop_code,String phone,String total,boolean iscoin) throws Exception;
    /**
     * 生成订单
     */
    Map<String,Object> doProductOrder(String json);



    Map<String, Object> payOrderMainAgain(String jsonSting) throws Exception;


    Map<String, Object> payGoodsOrderAgain(String jsonSting) throws Exception;

    /**
     * QT取消订单
     * @param jsonString
     */
    Map<String,Object> cancleQTOrder(String jsonString);
}
