package com.biz.service.api;

import com.biz.model.Pmodel.api.*;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/4.
 */
public interface OrderMainServiceI extends BaseServiceI {

    List<Record90Detail> phone_90Record(Map<String, Object> hashMap) throws Exception;
    Pager<DetailVoucher> queryDetailVoucherList(Pager<DetailVoucher> pager) throws Exception;
    int phone_90RecordCount(Map<String, Object> hashMap) throws Exception;

    OrderMain90 getOrderMain90ByCode(String code) throws Exception;

    Shop getShopBySid(String sid) throws Exception;

    String getSysUserIdByOpenId(String openid)throws Exception;

    Double getBalanceByOrderNum(String orderNum)throws Exception;

    void updateWhereOrderMain90(Map<String,Object> params) throws Exception;


    List<OrderMainUnion> collectRecordForClerk(Map<String, Object> pd)throws Exception;

    int checkNum(Map<String, Object> pd)throws Exception;

    double checkMoneyNum(Map<String, Object> pd)throws Exception;

    double collectMoneyNum(Map<String, Object> pd)throws Exception;

    int collectNum(Map<String, Object> pd)throws Exception;

    int getBalance_90(Map<String, Object> pd)throws Exception;

    List<Map<String,Object>> getWxReply()throws Exception;

    Map<String,Object> doSaveReply(Pbrand pbrand)throws Exception;

    List<Pbrand> loadWxReply()throws Exception;

    int getBalance_90_shop(Map<String, Object> pd)throws Exception;

    int getBalance_90_experience(Map<String, Object> pd)throws Exception;
}
