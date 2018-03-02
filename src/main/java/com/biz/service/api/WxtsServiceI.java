package com.biz.service.api;

import com.biz.model.Hmodel.api.TpaySceneDetail;
import com.biz.model.Pmodel.api.*;
import com.biz.service.base.BaseServiceI;

import java.util.List;
import java.util.Map;


/**
 * Created by lzq on 2017/2/4.
 */
public interface WxtsServiceI extends BaseServiceI<Object> {
    PayScene getPaySceneById(String id) throws Exception;
    BaseUser getBaseUserByOpen_id(String open_id,String appid) throws Exception;
    RgGift getRgGiftByCode(String code) throws Exception;
    List<BaseUser> getBaseUserByxy_openid(Map<String,Object> selectmap) throws Exception;
    int updateWhereBaseUser(Map<String,Object> pd) throws Exception;
    OrderMainUnion getOrderMainUnionByCode(String code) throws Exception;
    Brand getBrandOnlyByCode(String brand_code) throws Exception;
    void updateWhereOrderMainUnion(Map<String,Object> pd) throws Exception;
    List<BaseUser> selectWhereBaseUser(Map<String,Object> pd) throws Exception;
    void updateWherePayScene(Map<String,Object> pd) throws Exception;

    boolean findHasGiveAway(String open_id) throws Exception;
    boolean addWhereBaseUserByunionpay(Map<String,Object> pd) throws Exception;
    TpaySceneDetail getDetail(int id);
    boolean updateGiftState(String code) throws Exception;
    /**
     * 首次分期
     * @param key
     */
    void doDealPeriodizationByKeyFirst(String key, String openId)throws Exception;

    /**
     * 后期分期
     * @param key
     */
    void doDealPeriodizationByKeyOther(String key, String openId)throws Exception;

    void updateBaseUserSalesId(String open_id, String code) throws Exception;

    void updateShopId(String open_id,String shop_id)throws Exception;
}
