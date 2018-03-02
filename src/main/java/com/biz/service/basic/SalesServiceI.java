package com.biz.service.basic;

import com.biz.model.Hmodel.basic.Tsales;
import com.biz.model.Pmodel.basic.Psales;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;

import java.util.List;
import java.util.Map;

/**
 * Created by tomchen on 17/2/6.
 */
public interface SalesServiceI extends BaseServiceI<Tsales> {
    Pager<Psales> querySales(Pager<Psales> pager) throws Exception;

    List<Map<String, Object>> getShopsForSales(int identity, String identityCode) throws Exception;
}
