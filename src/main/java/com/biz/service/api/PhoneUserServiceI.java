package com.biz.service.api;

import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.PhoneUserIntegral;
import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Brand;
import com.biz.service.base.BaseServiceI;
import com.framework.utils.PageData;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */
public interface PhoneUserServiceI extends BaseServiceI<TorderMain> {

    /**
     * 获取登录用户信息
     * @param map
     * @return
     * @throws Exception
     */
    JSONObject getUserForLoginhashMap(Map<String, Object> map);


    /**
     * 根据shopSid 获取店铺信息
     */
    Shop getShopBySid(String sid)throws Exception;


    /**8
     * 获取
     */
    Integer getOrderMainCount(String order_code)throws Exception;


    /**8
     *久零币支付
     */
//    int doNineZeroPay(String open_id, String order_code, String cash_total)throws Exception;


    /**8
     * 获取用户
     * @param open_id
     * @return
     * @throws Exception
     */
    BaseUser getBaseUserByOpen_id(String open_id) throws Exception;


    void insertOrderMain90(OrderMain orderMain90) throws Exception;


    /**
     * 根据门店code 和用户code 获取当日订单
     *
     */
    JSONObject getPhoneDailyOrderSummaryByShopCodeAndUserCode(String shopCode, String userCode);


    /***
     * 加载收银记录筛选
     * @param mapParam
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> queryRecordsByParam(Map<String,Object> mapParam) throws Exception;

    int queryRecordsCountByParam(Map<String,Object> mapParam) throws Exception;

    List<Map<String,Object>> queryMoneyByParam(Map<String,Object> mapParam) throws Exception;

    int queryMoneyCountByParam(Map<String,Object> mapParam) throws Exception;

    Result resetPwd(String user_code, String pwd, String new_pwd);

    Result getSalesList(String sid);

    BaseUser getBaseUserByPhone(String phone) throws Exception;



    /**
     * 获取品牌
     * @param brand_code
     * @return
     * @throws Exception
     */
    Brand getBrandOnlyByCode(String brand_code) throws Exception;


    /**
     * 插入手工发券记录
     * @param rgGift
     * @throws Exception
     */
    void insertRgGift(RgGift rgGift) throws Exception;

    /**
     * 插入场景值
     * @param payScene
     * @throws Exception
     */
    void insertPayScene(PayScene payScene) throws Exception;

    String getSysUserIdByOpenId(String open_id) throws Exception;

    Double getCoin_90ByTUserId(String id) throws Exception;


    /**
     *
     8 根据orderMainCode 获取ordermain
     *
     */
    OrderMain getOrderMainByCode(String code) throws Exception;


    void updateWhereOrderMainByParam(Map<String, Object> mapPd) throws Exception;


    BaseUser getOneBaseUser(Map<String, Object> pd) throws Exception;

    List<PhoneUserIntegral> queryIntegralListByUser(PageData pd) throws Exception;


    Map<String,Object> qtGetOrderDetailByOrderCode(String code);


    Map<String,Object> qtBackOrderDetailByDetailIds(String detailIds);


    Map<String,Object> qtGetSumInfoByUserCode(String userCode, String shopId);

    void updateMainState(String detailIds);

    BaseUser getOneBaseUserByUnionpay(String unionpayid) throws Exception;
}
