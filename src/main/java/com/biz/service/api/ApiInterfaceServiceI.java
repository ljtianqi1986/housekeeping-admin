package com.biz.service.api;

import com.biz.model.Pmodel.sys.PwxGoods;

import java.util.List;
import java.util.Map;

/***
 * lzq
 *
 */
public interface ApiInterfaceServiceI {


    Map<String,Object> queryGoodsSkuInfo(String code, String venderIdMain)throws Exception;
    
    Map<String,Object> queryGoodsSkuInfoSingle(String code, String venderIdMain)throws Exception;

    boolean doSellingCoupons(String code)throws Exception;

/**
 * 用户久零贝帐户校验，如果不存在就新增
 * **/
    void checkUserCoin(Map<String, Object> map) throws Exception;
/**
 * 用户的久零贝操作
 * **/
    Map<String,Object> updatecoin_90(Map<String, Object> map) throws Exception;
    /**
     * 用户的久零券操作
     * **/
    Map<String,Object> updatebalance_90(Map<String, Object> map)throws Exception;
/**
 * 保存接口传递过来的商品的品牌/图片/规格等属性的数据
 * **/
    Map<String,Object>  saveGoodsInfo(List<PwxGoods> goodsList)throws Exception;
    /**
     * 保存接口传递过来的商品的基本属性数据
     * **/
    Map<String,Object>  saveGoods(List<PwxGoods> goodsList)throws Exception;

    Map<String,Object> updatecoin_90_(Map<String, Object> map) throws Exception;

    void sendChangeNoticeTemplate(String mainId);
    /**
     * 用户的久零券操作的新接口方法，引入久零券过期概念的
     * **/
    Map<String,Object> updateUserBalance_90(Map<String, Object> map)throws Exception;
    /**
     * 获取每日统计数据接口
     * @param map 传递的参数，内容包括beginTime,endTime
     */
    Map<String,Object> getStatistics(Map<String, Object> map)throws Exception;
    /**
     * 初始化用户数据
     * @return
     */
    Map<String,Object> updateInitUserData()throws Exception;
    /**
     *调用接口抓取第三方订单的数据，给用户发券
     * **/
    Map<String,Object> updateThirdOrderSendBalance(String code,String openId)throws Exception;

    Map<String,String> getUserInfoByOpenId(String open_id)throws Exception;
}
