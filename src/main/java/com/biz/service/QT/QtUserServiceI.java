package com.biz.service.QT;

import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.api.Result;
import com.biz.service.base.BaseServiceI;

import java.util.Map;

/**
 * Created by lzq on 2017/2/20.
 */
public interface QtUserServiceI extends BaseServiceI<TorderMain> {

    /**
     *8
     * 获取导购员
     * @param shopCode
     * @return
     */
    Map<String,Object> getQtDaoGouYuanByShopCode(String shopCode);




    /**
     *8
     * 获取商品信息
     * @param
     * @return
     */
    String getGoodsDetailByCodes(String whareid, String code, String type,String count)throws Exception;


    /**
     * 生成订单
     */
    Map<String,Object> doProductOrder(String json, String only_code);


    /**
     * 确认支付
     */
    Map<String,Object> toDoCashPay (String isCoinPay, String orderCode, String userPayCode, String shop_id,String device_info, String device_ip);

    /**
     * 通过shopId获取仓储信息
     * @param shopId
     * @return
     * @throws Exception
     */
    String getWhareid(String shopId) throws Exception;


    TorderMain getOrderMainById(String code)throws Exception;


    Result goodsCashBackMoney(String orderCode, String userCode)throws Exception;



    /**
     * 商品收银生成订单
     */
    Map<String,Object> toDoGoodPay (String isCoinPay, String str, String userPayCode, String shop_id,String type,String onlyCode,String ip) throws Exception;

    Map<String,Object>  checkPhoneCode(String orderCode,String onlyCode)throws Exception;

    Map<String,Object> checkPhoneCodeAgain(String orderCode,String onlyCode,String ip,String needMoney)throws Exception;
}
