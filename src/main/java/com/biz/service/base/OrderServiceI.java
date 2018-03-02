package com.biz.service.base;


import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.PorderMain;
import com.biz.model.Pmodel.basic.PorderSend;
import com.biz.model.Pmodel.basic.Ppics;
import com.biz.model.Pmodel.basic.PunionSumInfo;
import com.framework.model.Paging;

import java.util.List;
import java.util.Map;

/*************************************************************************
* create by lzq
 *
* 文件名称 ：OrderServiceI.java 描述说明 ：
*
* 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
**************************************************************************/
public interface OrderServiceI extends BaseServiceI<TorderMain> {
    /**
     * 获取全部订单
     * @return
     * @throws Exception
     */
    Paging queryOrders(Map map)throws Exception;


    /**
     * 根据id获取订单信息
     * @param id
     * @param detailId
     * @return
     * @throws Exception
     */
    Map<String, Object> findOrderInfoById(String id, String detailId)throws Exception;

    List<PorderMain> exportExcel_shopDeMain(Map<String, Map<String, Object>> maps)throws Exception;

    List<PorderSend> orderSend(String orderid) throws Exception;

    boolean saveOrderSend(String orderid, int sendType, String logisticsCompany, String sendCode)throws Exception;

    boolean updateBoolOrderState(String orderid, int state) throws Exception;

    Paging queryRefundOrders(Map map) throws Exception;

    Map<String,Object> updatesubmitToRefund(String orderid,String userId,String id,int type,String remarks) throws Exception;

    boolean updateStates(String orderDetailid, int detailState,String id,String refundState,String userId,String remarks)throws Exception;

    Map<String,Object> orderRefund(String orderId) throws Exception;

    List<Ppics> orderRefundPic(String orderId) throws Exception;

    Map<String,Object> findOrderMainInfo(String id) throws Exception;
/**
 * 查询超时h5订单
 * **/
    List<Map<String,Object>> getOutTimeOrder()throws Exception;

    /**
     * 关闭订单释放库存
     * **/
    void updateCloseOrder(List<Map<String, Object>> list)throws Exception;

    Paging queryOrderUnions(Map map)throws Exception;


    /**
     * 加载统计信息
     */
    PunionSumInfo loadUnionSumInfo(Map map);
    /**
     * 联盟订单的导出
     */
    List<PorderMain> exportExcel_orderUnion(Map<String, Map<String, Object>> maps)throws Exception;

    String findOpenId(String orderid)throws Exception;

    PorderSend findPorderSend(String orderid)throws Exception;

    //关闭订单
    Map closeOrder(String orderMainId,String userCode)throws Exception;
}
