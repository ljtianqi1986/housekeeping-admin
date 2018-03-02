package com.biz.service.StatisticalForm;


import com.biz.model.Pmodel.StatisticalForm.POrderInfo;
import com.biz.model.Pmodel.StatisticalForm.PsalesOrder;
import com.biz.model.Pmodel.basic.Psales;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.mdao.DaoSupport;
import com.framework.model.Pager;
import com.framework.utils.Tools;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service("salesOrderService")
public class SalesOrderServiceImpl extends BaseServiceImpl implements
        SalesOrderServiceI {
    @Resource(name = "daoSupport")
    private DaoSupport dao;


    /**
     * 导购员合计统计
     * @param pager
     * @return
     * @throws Exception
     */
    @Override
    public Pager<PsalesOrder> SalesOrderList(Pager<PsalesOrder> pager) throws Exception {
        return dao.findForPager1("salesOrderDao.getSalesOrderList", "salesOrderDao.getSalesOrderCount", pager);
    }

    @Override
    public List<PsalesOrder> findSalesOrderList(Map<String, Object> params) throws Exception{
        return (List<PsalesOrder>)dao.findForList("salesOrderDao.findSalesOrderList", params);
    }
    /**
     * 导购员明细
     * @param pager
     * @return
     * @throws Exception
     */
    @Override
    public Pager<POrderInfo> SalesOrderMX(Pager<POrderInfo> pager) throws Exception {
        return dao.findForPager1("salesOrderDao.getSalesOrderMX", "salesOrderDao.getSalesOrderMXCount", pager);
    }


    /**
     * 导购员订单明细
     * @param start_time
     * @param end_time
     * @return
     * @throws Exception
     */
    @Override
    public List<POrderInfo> SalesOrderDays(String start_time, String end_time,List<String> salesCode) throws Exception {
        Map<String, Object> select_map = new HashMap<>();
        select_map.put("start_time", start_time + " 00:00:00");
        select_map.put("end_time", end_time + " 00:00:00");
        select_map.put("salesCodeList", Tools.ListToString_Quotes(salesCode));
        //发了多少券点
        List<POrderInfo> rList_order = (List<POrderInfo>) dao.findForList("salesOrderDao.getSalesOrderList", select_map);

        return rList_order;
    }

    /**
     * 导购员日统计折现图
     * @param start_time
     * @param end_time
     * @return
     * @throws Exception
     */
    public List<PsalesOrder> SalesOrderMXList(String start_time, String end_time) throws Exception {

        return null;
    }


    /**
     * 两个时间之间相差距离多少天
     *
     * @param str1 时间参数 1：
     * @param str2 时间参数 2：
     * @return 相差天数
     */
    public long getDistanceDays(String str1, String str2) throws Exception {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date one;
        Date two;
        long days = 0;
        try {
            one = df.parse(str1);
            two = df.parse(str2);
            long time1 = one.getTime();
            long time2 = two.getTime();
            long diff;
            if (time1 < time2) {
                diff = time2 - time1;
            } else {
                diff = time1 - time2;
            }
            days = diff / (1000 * 60 * 60 * 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }



    /**
     * 计算起止日期
     * @param date_type
     * @return
     * @throws Exception
     */
    public HashMap<String,Object> calculateDate(int date_type)
    {
        HashMap<String,Object> hashMap = new HashMap<String,Object>();
        List<String> date_list = new ArrayList<String>();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置时间格式
        Date dNow = new Date();   //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        if(date_type == 1)//今日
        {
            date_list.add(sdf.format(dNow).substring(0, 10));
        }
        else if(date_type == 7)//7日内
        {
            date_list.add(sdf.format(dNow).substring(0, 10));
            for(int i = 0 ; i < 6 ; i++)
            {
                calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
                date_list.add(sdf.format(calendar.getTime()).substring(0, 10));
            }
            dBefore = calendar.getTime();

        }
        else if(date_type == 30)//30日内
        {
            date_list.add(sdf.format(dNow).substring(0, 10));
            for(int i = 0 ; i < 29 ; i++)
            {
                calendar.add(Calendar.DAY_OF_MONTH, -1);  //设置为前一天
                date_list.add(sdf.format(calendar.getTime()).substring(0, 10));
            }
            dBefore = calendar.getTime();
        }
        //若date_type=1，则为今日，不需要对起止日期做处理
        String defaultStartDate = sdf.format(dBefore);
        String defaultEndDate = sdf.format(dNow);
        hashMap.put("start_date", defaultStartDate.substring(0,10));
        hashMap.put("end_date", defaultEndDate.substring(0,10));
        hashMap.put("date_list", date_list);
        return hashMap;
    }
}
