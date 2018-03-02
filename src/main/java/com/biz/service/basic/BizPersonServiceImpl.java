package com.biz.service.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.TBizPerson;
import com.biz.model.Pmodel.basic.Pbizperson;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.model.Pmodel.basic.PpersonStatistics;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.model.Paging;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by tomchen on 17/1/11.
 */
@Service("bizpersonService")
public class BizPersonServiceImpl extends BaseServiceImpl<TBizPerson> implements BizpersonServiceI {

    private  SimpleDateFormat  shortSdf;


    @Resource(name = "daoSupport")
    private DaoSupport dao;

    @Override
    public Pager<Pbizperson> queryBizpersons(Pager<Pbizperson> pager) throws Exception {
        return dao.findForPager1("bizpersonDao.queryBizpersons", "bizpersonDao.queryBizpersonsCount", pager);
    }

    @Override
    public List<Pbizperson> showBizPersonForList() throws Exception {
        return (List<Pbizperson>) dao.findForList("bizpersonDao.queryBizpersonsAll",null);
    }

    @Override
    public Paging personStatistics(Map map) throws Exception {
        Pager<Map<String, Object>> pager = calculateAndTransformByMap(map);
        //查询主要数据
        Pager<PpersonStatistics> pager_back = (Pager<PpersonStatistics>) dao
                .findForPager1("bizpersonDao.personStatisticsPage",
                        "bizpersonDao.personStatisticsCount", pager);
        //查询POS机数量
        List<PpersonStatistics> ppersonStatisticsList = pager_back.getExhibitDatas();


        //封装成适合前台使用的格式
        Paging paging = new Paging<>();
        paging.setRows(ppersonStatisticsList);
        paging.setTotal((long) pager_back.getRecordCount());
        return paging;
    }



    @Override
    public List<Pbizperson> getBizList(){
        List<Pbizperson> list = null;
        try {
            list = (List<Pbizperson>) dao.findForList("bizpersonDao.queryBizpersonsAll", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public List<Pbrand> getBrandListWithBizCode(){
        List<Pbrand> list = null;
        try {
            list = (List<Pbrand>) dao.findForList("bizpersonDao.getBrandListWithBizCode", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public  PpersonStatistics personStatisticsTotalUser(Map map)
    {
        PpersonStatistics ppersonStatistics = new PpersonStatistics();
        PpersonStatistics ppersonStatisticsUserCount = new PpersonStatistics();
        try {
            ppersonStatistics = (PpersonStatistics) dao.findForObject("bizpersonDao.personStatisticsTotalUser",map);
            ppersonStatisticsUserCount = (PpersonStatistics) dao.findForObject("bizpersonDao.ppersonStatisticsUserCount",map);
            if(ppersonStatisticsUserCount!=null
                    && ppersonStatisticsUserCount.getMemberCount() != null )
            {
                ppersonStatistics.setMemberCount(ppersonStatisticsUserCount.getMemberCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ppersonStatistics;
    }


    @Override
    public  PpersonStatistics personStatisticsTotal(Map map)
    {
        PpersonStatistics ppersonStatistics = new PpersonStatistics();
        try {
            ppersonStatistics = (PpersonStatistics) dao.findForObject("bizpersonDao.personStatisticsTotal",map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ppersonStatistics;
    }


    @Override
    public Map<String, Object> loadCouponSum(int type, String json) throws Exception{
        Map map = JSON.parseObject(json, Map.class);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> res=new HashMap<String, Object>();
        String beginTime="";
        String endTime="";
        if(type==1)//本周
        {
            beginTime=sdf.format(getCurrentWeekDayStartTime());
            endTime=sdf.format(getCurrentWeekDayEndTime());
        }
        else if(type==2)//本月
        {
            beginTime=sdf.format(getCurrentMonthStartTime());
            endTime=sdf.format(getCurrentMonthEndTime());
        } else if(type==3)//本季度
        {
            beginTime=sdf.format(getCurrentQuarterStartTime());
            endTime=sdf.format(getCurrentQuarterEndTime());
        }

        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        List<Map<String,Object>> list= (List<Map<String, Object>>) dao.findForList("bizpersonDao.loadCouponSum",map);
        list=getFullDayData(list,beginTime,endTime);

        double maxcount=0;
        double amountAll=0;
        for(int j=0;j<list.size();j++)
        {
            BigDecimal account= (BigDecimal) list.get(j).get("count");
            if(maxcount<account.doubleValue())
            {
                maxcount=account.doubleValue();
            }
            amountAll+=account.doubleValue();
        }
        res.put("amountAll",amountAll);
        res.put("maxCount",maxcount);
        res.put("size",(int)list.size()/7);
        res.put("list",list);

        return res;
    }


    @Override
    public Map<String, Object> loadMemberSum(int type, String json) throws Exception{
        Map map = JSON.parseObject(json, Map.class);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> res=new HashMap<String, Object>();
        String beginTime="";
        String endTime="";
        if(type==1)//本周
        {
            beginTime=sdf.format(getCurrentWeekDayStartTime());
            endTime=sdf.format(getCurrentWeekDayEndTime());
        }
        else if(type==2)//本月
        {
            beginTime=sdf.format(getCurrentMonthStartTime());
            endTime=sdf.format(getCurrentMonthEndTime());
        } else if(type==3)//本季度
        {
            beginTime=sdf.format(getCurrentQuarterStartTime());
            endTime=sdf.format(getCurrentQuarterEndTime());
        }

        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        List<Map<String,Object>> list=
                (List<Map<String, Object>>) dao.findForList("bizpersonDao.loadMemberSum",map);
        list=getFullDayData(list,beginTime,endTime);

        double maxcount=0;
        double amountAll=0;
        for(int j=0;j<list.size();j++)
        {
            BigDecimal account= (list.get(j).get("count") instanceof Long)?
                    new BigDecimal( (Long)list.get(j).get("count")) : (BigDecimal)list.get(j).get("count");
            if(maxcount<account.doubleValue())
            {
                maxcount=account.doubleValue();
            }
            amountAll+=account.doubleValue();
        }
        res.put("amountAll",amountAll);
        res.put("maxCount",maxcount);
        res.put("size",(int)list.size()/7);
        res.put("list",list);

        return res;
    }



    @Override
    public Map<String, Object> loadPosSum(int type, String json) throws Exception{
        Map map = JSON.parseObject(json, Map.class);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        HashMap<String, Object> res=new HashMap<String, Object>();
        String beginTime="";
        String endTime="";
        if(type==1)//本周
        {
            beginTime=sdf.format(getCurrentWeekDayStartTime());
            endTime=sdf.format(getCurrentWeekDayEndTime());
        }
        else if(type==2)//本月
        {
            beginTime=sdf.format(getCurrentMonthStartTime());
            endTime=sdf.format(getCurrentMonthEndTime());
        } else if(type==3)//本季度
        {
            beginTime=sdf.format(getCurrentQuarterStartTime());
            endTime=sdf.format(getCurrentQuarterEndTime());
        }

        map.put("beginTime",beginTime);
        map.put("endTime",endTime);
        List<Map<String,Object>> list=
                (List<Map<String, Object>>) dao.findForList("bizpersonDao.loadPosSum",map);
        list=getFullDayData(list,beginTime,endTime);

        double maxcount=0;
        double amountAll=0;
        for(int j=0;j<list.size();j++)
        {
            BigDecimal account= (list.get(j).get("count") instanceof Long)?
                    new BigDecimal( (Long)list.get(j).get("count")) : (BigDecimal)list.get(j).get("count");
            if(maxcount<account.doubleValue())
            {
                maxcount=account.doubleValue();
            }
            amountAll+=account.doubleValue();
        }
        res.put("amountAll",amountAll);
        res.put("maxCount",maxcount);
        res.put("size",(int)list.size()/7);
        res.put("list",list);

        return res;
    }




    private List<Map<String,Object>> getFullDayData(List<Map<String, Object>> list, String beginTime, String endTime) throws
            ParseException {
        List<Map<String,Object>> listRes=new ArrayList<>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date dateBegin=sdf.parse(beginTime);
        Date dateEnd=sdf.parse(endTime);
        Date dateNow=dateBegin;
        for(int i=0;dateNow.getTime()<=dateEnd.getTime();i++)
        {
            Map<String,Object> map=new HashMap<>();
            String dateNowString=sdf.format(dateNow);
            map.put("date",dateNow.getTime());
            Calendar c = Calendar.getInstance();
            c.setTime(dateNow);
            c.add(Calendar.DAY_OF_MONTH, 1);// 今天+1天
            dateNow = c.getTime();


            for(int j=0;j<list.size();j++)
            {
                if(dateNowString.equals(list.get(j).get("date")))
                {
                    if(list.get(j).get("count") != null)
                    {
                        map.put("count",list.get(j).get("count"));
                    }else{
                        map.put("count", BigDecimal.valueOf(0.0));
                    }
                    break;
                }
                else
                {
                    map.put("count", BigDecimal.valueOf(0.0));
                }
            }
            if(list.size()==0)
            { map.put("count",BigDecimal.valueOf(0.0));}
            listRes.add(map);
        }
        return listRes;
    }




    /**
     * 获得本周的第一天，周一
     *
     * @return
     */
    public Date getCurrentWeekDayStartTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 2;
            c.add(Calendar.DATE, -weekday);
            c.setTime(shortSdf.parse(shortSdf.format(c.getTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，周日
     *
     * @return
     */
    public Date getCurrentWeekDayEndTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 8 - weekday);
            c.setTime(shortSdf.parse(shortSdf.format(c.getTime())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return c.getTime();
    }
    /**
     * 获得本月的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public   Date getCurrentMonthStartTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前月的结束时间，即2012-01-31 23:59:59
     *
     * @return
     */
    public   Date getCurrentMonthEndTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的开始时间，即2012-01-1 00:00:00
     *
     * @return
     */
    public   Date getCurrentQuarterStartTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public   Date getCurrentQuarterEndTime() {
        shortSdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }



    @Override
    public List<PpersonStatistics> excelBrandInfo(Map maps)throws Exception{

        return (List<PpersonStatistics>)dao.findForList("bizpersonDao.getExcelBrandInfo",maps);
    }
}
