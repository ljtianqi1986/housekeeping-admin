package com.biz.service.activity;

import com.biz.model.Hmodel.TCardType;
import com.biz.model.Pmodel.Pactivity;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Pager;
import com.framework.model.Paging;

import java.util.Map;

/**
 * Created by tomchen on 17/2/4.
 */
public interface ActivityServiceI extends BaseServiceI{

    Paging ActivityListPageM(Map<String, Object> params) throws Exception;
    public boolean addActivity(Pactivity activity) throws Exception;
    public boolean UpdateActivityState(Pactivity activity) throws Exception;
    public boolean deleteActivity(Pactivity activity) throws Exception;
    public boolean updetActivityMap(String id,String mapid) throws Exception;
    public boolean addActivityRecord(String activityId) throws Exception;
}
