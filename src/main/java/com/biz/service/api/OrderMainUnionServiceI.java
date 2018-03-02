package com.biz.service.api;

import com.biz.model.Pmodel.PorderMainUnion;
import com.biz.model.Pmodel.api.Result;
import com.biz.model.Pmodel.basic.Base90Detail;
import com.biz.service.base.BaseServiceI;

import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */
public interface OrderMainUnionServiceI extends BaseServiceI {


    PorderMainUnion getOrderMainUnionByCode(String order_code) throws Exception;

    void updateWhereOrderMainUnion(Map<String, Object> param) throws Exception;

    void insertOrderMainUnion(PorderMainUnion orderMainUnion) throws Exception;

    int add_balance_90(Map<String, Object> pd) throws Exception;

    void insertBase90Detail(Base90Detail base90Detail) throws Exception;

    Result phone_yl_lkl_scan(String shop_id,
                             String user_code, String author_code, String total, String order_code,int pay_type,String card_no)throws Exception;
    Result updatePhone_lkl_back_money(Map<String, Object> map) throws Exception;

    String getOrderGetBalance90(String sourceId) throws Exception;
    /**
     * 付款
     * @param map
     * @return
     * @throws Exception
     */
    Result phone_wx_scan(Map<String,Object> map)throws Exception;

    /**
     * 退款
     * @param map
     * @return
     * @throws Exception
     */
    Result phone_wx_back_money(Map<String,Object> map)throws Exception;

    Result updateBalanceForJLBankBack(String total, String userCode,String order_code)throws Exception;

    int checkPayScene(String order_code)throws Exception;

    Map<String, String> getPeriodizationLinkUrl(double totals, String shopId,String orderId, String brandCode, String userId);


    Result shopGetQrcodeByShopId(String shopId)throws Exception;


    Result getQrcodeStateAndInfo(String mainId)throws Exception;

}
