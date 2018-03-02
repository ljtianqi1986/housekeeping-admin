package com.biz.service.period;

import com.biz.model.Hmodel.TbaseDetail;
import com.biz.model.Hmodel.TorderMain;
import com.biz.model.Hmodel.api.TPeriodizationMain;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.model.Pmodel.offlineCard.PofflineCardDetail;
import com.biz.model.Pmodel.offlineCard.PofflineCardGrant;
import com.biz.service.api.ApiInterfaceServiceI;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.offlineCard.OfflineCardServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.StringUtil;
import com.framework.utils.UuidUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * **********************************************************************
 * create by lzq
 * <p>
 * 文件名称 ：OrderServiceI.java 描述说明 ：
 * <p>
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 * ************************************************************************
 */

@Service("periodService")
public class PeriodServiceImpl extends BaseServiceImpl<TPeriodizationMain> implements PeriodServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Paging showPeroid(Map map) throws Exception {
        //参数
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);
        //查询
        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("periodDao.showPeroid",
                        "periodDao.showPeroidCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public Paging showDetail(Map map) throws Exception {
        //参数
        int limit = Integer.parseInt(map.get("limit").toString());
        int offset = Integer.parseInt(map.get("offset").toString());
        Pager<Map<String, Object>> pager = new Pager();
        pager.setParameters(map);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);
        //查询
        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("periodDao.showDetail",
                        "periodDao.showDetailCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public void delGridById(String id,User user) throws Exception {
        //更新主表
        dao.update("periodDao.updateMain",id);
        //更新子表
        dao.update("periodDao.updateDetail",id);
        //添加日志
        String detailId=(String)dao.findForObject("periodDao.findDetailId",id);
        String userName=user.getPersonName();
        String msg=userName+"后台取消客户发券额度";
        Map map=new HashMap();
        map.put("detailId",detailId);
        map.put("msg",msg);
        map.put("userId",user.getId());
        map.put("state",1);
        map.put("id",UuidUtil.get32UUID());
        dao.save("periodDao.saveLog",map);
    }
}

