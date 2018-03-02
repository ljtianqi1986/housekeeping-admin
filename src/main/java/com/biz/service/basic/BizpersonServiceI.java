package com.biz.service.basic;

import com.biz.model.Hmodel.TBizPerson;
import com.biz.model.Pmodel.basic.Pbizperson;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.model.Pmodel.basic.PpersonStatistics;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;
import com.framework.model.Paging;

import java.util.List;
import java.util.Map;

/**
 * Created by tomchen on 17/1/11.
 */
public interface BizpersonServiceI extends BaseServiceI<TBizPerson> {
    Pager<Pbizperson> queryBizpersons(Pager<Pbizperson> pager) throws Exception;

    List<Pbizperson> showBizPersonForList() throws Exception;

    /**
     * 业务员统计
     *
     * @return
     * @throws Exception
     */
    Paging personStatistics(Map map) throws Exception;


    /**
     * 获取业务员
     */
    List<Pbizperson> getBizList();


    /**
     *
     */
    List<Pbrand> getBrandListWithBizCode();


    PpersonStatistics personStatisticsTotal(Map map);


    PpersonStatistics personStatisticsTotalUser(Map map);


    Map<String, Object> loadCouponSum(int type, String json)throws Exception;


    Map<String, Object> loadMemberSum(int type, String json)throws Exception;


    Map<String, Object> loadPosSum(int type, String json)throws Exception;


    List<PpersonStatistics> excelBrandInfo(Map maps)throws Exception;

}

