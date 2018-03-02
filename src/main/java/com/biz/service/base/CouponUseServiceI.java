package com.biz.service.base;


import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.basic.PbaseDetail;
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
public interface CouponUseServiceI extends BaseServiceI<TorderMain> {
    /**
     * 获取用券流水
     * @return
     * @throws Exception
     */
    Paging queryCouponUseDetail(Map map)throws Exception;


    Map<String,Object> loadTJInfo(Map<String, String> map)throws Exception;

    Paging queryBase90Detail(Map map)throws Exception;

    Map<String,Object> loadSumInfo(Map<String, String> map)throws Exception;

    Paging queryMember(Map map)throws Exception;

    List<Map<String,Object>> loadSumMember(Map<String,String> map)throws Exception;

    Paging query90UseCount(Map map)throws Exception;

    List<PbaseDetail> findCouponList(Map<String, Object> map)throws Exception;
    /**
     * 加载用券明细
     * @return
     */
    List<Map<String,Object>> findOrderInfoById(String id)throws Exception;
}
