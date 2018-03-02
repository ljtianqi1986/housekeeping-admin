package com.biz.service.StatisticalForm;



import com.biz.model.Pmodel.StatisticalForm.POrderInfo;
import com.biz.model.Pmodel.StatisticalForm.PsalesOrder;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public interface SalesOrderServiceI extends BaseServiceI {
    Pager<PsalesOrder> SalesOrderList(Pager<PsalesOrder> pager) throws Exception;
    List<PsalesOrder> findSalesOrderList(Map<String, Object> params) throws Exception;
    List<POrderInfo> SalesOrderDays(String start_time, String end_time,List<String> salesCode) throws Exception;
    Pager<POrderInfo> SalesOrderMX(Pager<POrderInfo> pager) throws Exception;
}
