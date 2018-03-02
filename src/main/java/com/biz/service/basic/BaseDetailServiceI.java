package com.biz.service.basic;

import com.biz.model.Hmodel.TbaseDetail;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.api.OrderMainUnion;
import com.biz.model.Pmodel.basic.PbaseDetail;
import com.biz.model.Pmodel.basic.PyestodayReport;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;
import com.framework.model.Params;

import java.util.List;
import java.util.Map;

/**
 * 商户Service
 * Created by liujiajia on 2017/1/6.
 */
public interface BaseDetailServiceI extends BaseServiceI<TbaseDetail> {
/**
 * 发券流水记录表分页查询
 * @param sqlParams
 * @return Paging
 * **/
    Paging findBaseDetailGrid(Params sqlParams)throws  Exception;
    List<PbaseDetail> findBaseDetailList(Map<String,Object> params)throws  Exception;
    List<OrderMainUnion> findBaseDetailAutoList(Map<String,Object> params)throws Exception;
    /**
     * 查询自动发券详情
     * @param id
     * @return PbaseDetail
     * **/
    PbaseDetail findDetailAuto(String id)throws  Exception;

    Paging showBaseDetailStatistics(Params sqlParams)throws  Exception;

    Map<String,Object> queryGiftData(User user, String type, String code, String dataType, String startTime, String endTime, String s)throws  Exception;

    Map<String,String> loadZDInfo(Map<String, String> map)throws  Exception;

    Map<String,String> loadSDInfo(Map<String, String> map)throws  Exception;

    Map<String,String> loadSTKInfo(Map<String, String> map)throws  Exception;

    Paging findBaseDetailAutoGrid(Params sqlParams)throws  Exception;

    Paging showCoinStatistics(Params sqlParams)throws  Exception;

    Map<String,String> loadJLBInfo(Map<String, String> map)throws  Exception;
    Map<String, Object> deBase90Detail(String id) throws Exception;

    Map<String,Object> loadUseInfo(Map<String, Object> map)throws Exception;

    List<Map<String,Object>> getOfflineCardTypeForSelect() throws Exception;
    /**
     * 获取昨日报表数据
     */
    PyestodayReport getYesTodayData(String dateString)throws Exception;

    /**
     * 查询首次关注送券
     * @param sqlParams
     * @return
     * @throws Exception
     */
    Paging findFirstConcernGrid(Params sqlParams) throws Exception;

    /**
     * 查询充值90贝送券
     * @param sqlParams
     * @return
     */
    Paging findCharge90CoinGrid(Params sqlParams) throws Exception;

    /**
     * 查询退款送券
     * @param sqlParams
     * @return
     */
    Paging findRefundCouponsGrid(Params sqlParams) throws Exception;

    /**
     * 查询首次送券统计详情
     * @param map
     * @return
     */
    Map<String,String> loadFCInfo(Map<String, String> map) throws Exception;

    /**
     * 充值久零贝送券汇总
     * @param map
     * @return
     */
    Map<String,String> loadCCInfo(Map<String, String> map) throws Exception;

    /**
     * 加载退券汇总
     * @param map
     * @return
     */
    Map<String,String> loadRCInfo(Map<String, String> map) throws Exception;

    /**
     * 查询首次关注送券列表4导出
     * @param params
     * @return
     */
    List<PbaseDetail> findFirstConcernList(Map<String, Object> params) throws Exception;

    /**
     * 查询充值90贝列表
     * @param params
     * @return
     */
    List<PbaseDetail> findCharge90CoinList(Map<String, Object> params) throws Exception;

    /**
     * 查询退款退券列表
     * @param params
     * @return
     */
    List<PbaseDetail> findRefundCouponsList(Map<String, Object> params) throws Exception;

    PyestodayReport getDataByDate(String date) throws Exception;
//扫码送券流水分页显示
    Paging findScanGrid(Params sqlParams)throws Exception;
    //扫码送券汇总
    Map<String,String> loadScanInfo(Map<String, String> map)throws Exception;
    //扫码送券流水导出
    List<PbaseDetail> findScanCouponsList(Map<String, Object> params)throws Exception;
}
