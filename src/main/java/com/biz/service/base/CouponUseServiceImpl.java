package com.biz.service.base;

import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Pmodel.basic.PbaseDetail;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*************************************************************************
 * create by lzq
 *
 * 文件名称 ：OrderServiceI.java 描述说明 ：
 *
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

@Service("couponUseService")
public class CouponUseServiceImpl extends BaseServiceImpl<TorderMain> implements CouponUseServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Autowired
    private BaseDaoI baseDao;

    @Override
    public Paging queryCouponUseDetail(Map map)throws Exception{

        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("CouponUse.findPageCouponUse",
                        "CouponUse.countCouponUse", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public Map<String,Object> loadTJInfo(Map<String, String> map) throws Exception {
        return (Map<String,Object>) dao.findForObject("CouponUse.loadTJInfo",map);
    }

    @Override
    public Paging queryBase90Detail(Map map) throws Exception {
        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("CouponUse.findBase90Detail",
                        "CouponUse.countBase90Detail", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public Map<String, Object> loadSumInfo(Map<String, String> map) throws Exception {
        return (Map<String, Object>)dao.findForObject("CouponUse.loadSumInfo",map);
    }

    @Override
    public Paging queryMember(Map map) throws Exception {
        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("CouponUse.queryMember",
                        "CouponUse.queryMemberCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public List<Map<String, Object>> loadSumMember(Map<String, String> map) throws Exception {
        return (List<Map<String, Object>>) dao.findForList("CouponUse.loadSumMember",map);
    }

    @Override
    public Paging query90UseCount(Map map) throws Exception {
        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);

        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("CouponUse.query90Use",
                        "CouponUse.query90UseCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public List<PbaseDetail> findCouponList(Map<String, Object> map) throws Exception {
        return (List<PbaseDetail>) dao.findForList("CouponUse.findCouponList",map);
    }

    @Override
    public List<Map<String, Object>> findOrderInfoById(String id) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("id", id);
        List<Map<String, Object>> quanDetail = (List<Map<String, Object>>) dao
                .findForList("CouponUse.findBalance90DetailById", param);
        return  quanDetail;

    }
}
