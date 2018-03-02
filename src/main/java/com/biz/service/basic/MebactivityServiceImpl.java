package com.biz.service.basic;


import com.biz.model.Hmodel.Tmebactivity;
import com.biz.model.Pmodel.Pmebactivity;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import com.framework.model.Params;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("mebactivityService")
public class MebactivityServiceImpl extends BaseServiceImpl<Tmebactivity> implements MebactivityServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Autowired
    private BaseDaoI<Tmebactivity> mebactivityDao;


    @Override
    public Paging findMebactivityGrid(Params sqlParams) throws Exception {
        Paging paging= dao.findForPagings("mebactivityMapper.findMebactivityGrid",sqlParams,"mebactivityMapper.findMebactivityCount",sqlParams);
        return paging;
    }

    @Override
    public void delGridById(String ids) {

            String [] idList=ids.split(",");
            for (String id:idList) {
                Tmebactivity mebactivity= mebactivityDao.getById(Tmebactivity.class,id);
                mebactivity.setIsdel(1);
                mebactivityDao.update(mebactivity);
            }

    }

    @Override
    public Pmebactivity findById(String id) throws Exception {
        return (Pmebactivity) dao.findForObject("mebactivityMapper.findMebactivityById",id);
    }

    @Override
    public void saveMebactivity(Pmebactivity pmebactivity) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Date nowDate1=new Date();
        String nowDate2=sdf.format(nowDate1);
        Date nowDate=sdf.parse(nowDate2);
        Date startDate=sdf.parse(pmebactivity.getStartTime());

        Tmebactivity tm=new Tmebactivity();
        tm.setCreateTime(new Date());

        if(nowDate.getTime()>=startDate.getTime()){
            tm.setState(1);
        }else{
            tm.setState(0);
        }
        tm.setCount(Integer.valueOf(pmebactivity.getCount()));
        tm.setIsdel(0);
        tm.setIsOpen(1);
        tm.setStartTime(startDate);
        tm.setLotteryTime(sdf.parse(pmebactivity.getLotteryTime()));
        tm.setEndTime(sdf.parse(pmebactivity.getEndTime()));
        tm.setPeriod(Integer.valueOf(pmebactivity.getPeriod()));
        tm.setName(pmebactivity.getName());
        tm.setNote(pmebactivity.getNote());
        tm.setPicId(pmebactivity.getPicId());
        mebactivityDao.save(tm);
    }

    @Override
    public void updateMebactivity(Pmebactivity pmebactivity) {
        Tmebactivity tm=mebactivityDao.getById(Tmebactivity.class,pmebactivity.getId());
        tm.setCount(Integer.valueOf(pmebactivity.getCount()));
        tm.setName(pmebactivity.getName());
        tm.setNote(pmebactivity.getNote());
        tm.setPicId(pmebactivity.getPicId());
        mebactivityDao.update(tm);
    }

    @Override
    public String findLastDate() throws Exception {
        return (String)dao.findForObject("mebactivityMapper.findLastDate",null);
    }
    @Override
    public String findPeriod() throws Exception {
        return (String)dao.findForObject("mebactivityMapper.findPeriod",null);
    }

    @Override
    public Paging showMebActivityPerson(Map map) throws Exception {
        //参数
        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);
        //查询
        Pager<Map<String, Object>> pager_back = (Pager<Map<String, Object>>) dao
                .findForPager1("mebactivityMapper.showMebActivityPerson",
                        "mebactivityMapper.showMebActivityPersonCount", pager);
        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(pager_back.getExhibitDatas());
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }

    @Override
    public Map<String,Object> changeIsOpen(String id, String isOpen) throws Exception {
        Map<String,Object> map=new HashedMap();
        map.put("id",id);
        map.put("isOpen",isOpen);
        //判断该活动是否有人报名
        int count =(int)dao.findForObject("mebactivityMapper.getCountById",id);
        if(count==0){
            dao.update("mebactivityMapper.changeIsOpen",map);
            map.put("flag","1");
            map.put("msg","操作成功");
            return map;
        }else{
            map.put("flag","0");
            map.put("msg","已有报名的活动不能关闭");
            return map;
        }
    }
}
