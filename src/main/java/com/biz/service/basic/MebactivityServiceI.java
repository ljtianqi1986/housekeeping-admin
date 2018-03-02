package com.biz.service.basic;

import com.biz.model.Hmodel.Tmebactivity;
import com.biz.model.Pmodel.Pmebactivity;
import com.biz.service.base.BaseServiceI;
import com.framework.model.Paging;
import com.framework.model.Params;

import java.text.ParseException;
import java.util.Map;

/**
 * 会员日设置service
 */
public interface MebactivityServiceI extends BaseServiceI<Tmebactivity> {
/**
 * 分页查询
 * **/
    Paging findMebactivityGrid(Params sqlParams)throws Exception;
    /**
     * 批量删除
     * **/
    void delGridById(String ids);
    /**
     * 根据id获取活动
     * **/
    Pmebactivity findById(String id)throws Exception;

    void saveMebactivity(Pmebactivity pmebactivity) throws ParseException;

    void updateMebactivity(Pmebactivity pmebactivity);

    String findLastDate() throws Exception;

    String findPeriod()throws  Exception;

    Paging showMebActivityPerson(Map map) throws Exception;

    Map<String,Object> changeIsOpen(String id,String isOpen)throws Exception;
}
