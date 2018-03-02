package com.biz.service.activity;

import com.biz.model.Hmodel.TCardType;
import com.biz.model.Pmodel.Pactivity;
import com.biz.service.base.BaseServiceI;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.utils.Tools;
import com.framework.utils.UuidUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("activityService")
public class ActivityServiceImpl extends BaseServiceImpl implements ActivityServiceI
{

	@Resource(name = "daoSupport")
	private DaoSupport dao;



    //获得列表翻页
    @Override
    public Paging ActivityListPageM(Map<String, Object> params) throws Exception{
        Paging paging =dao.findForPaging("ActivityDao.sysActivityPage", params, "ActivityDao.sysActivityCode",params);
        //Pager<Map<String,String>> list=dao.findForPager("ReportDao.findOfflineCardTypePage","ReportDao.findOfflineCardTypeCount",pager);
        return paging;
    }


    //新增活动
    @Override
    public boolean addActivity(Pactivity activity) throws Exception{
        return (Integer)dao.save("ActivityDao.insertActivity",activity)>0;
    }

    //开启关闭活动
    @Override
    public boolean UpdateActivityState(Pactivity activity) throws Exception{
        return (Integer)dao.update("ActivityDao.updetActivityState", activity)>0;
    }

    //删除活动
    @Override
    public boolean deleteActivity(Pactivity activity) throws Exception{
        return (Integer)dao.update("ActivityDao.updetActivityIsdel", activity)>0;
    }

    //修改活动主表mapid 即对应活动规则表
    @Override
    public boolean updetActivityMap(String id,String mapid) throws Exception{
        Pactivity activity =new Pactivity();
        activity.setId(id);
        activity.setMapid(mapid);
        return (Integer)dao.update("ActivityDao.updetActivityMapid", activity)>0;
    }


    //活动记录
    @Override
    public boolean addActivityRecord(String activityId) throws Exception{
        if(Tools.isEmpty(activityId.trim())){
            return false;
        }else {
            Map<String, Object> saveMap = new HashMap<>();
            saveMap.put("recordId", UuidUtil.get32UUID());
            saveMap.put("id", activityId.trim());
            return (Integer) dao.save("ActivityDao.insertActivityRecord", saveMap) > 0;
        }
    }

    /**
     * List<String> 转 String 逗号分隔（字段加引号）
     * @param listString
     * @return
     */
    public static String ListToString_Quotes(List<String> listString)
    {
        String rString="";
        for(String tmp:listString)
        {
            rString+="'"+tmp.trim()+"',";
        }

        if(rString.length()>0)
        {
            rString=rString.substring(0,rString.length()-1);
        }

        return  rString;
    }

}
