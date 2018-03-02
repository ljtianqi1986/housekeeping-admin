package com.biz.service.task;


import com.alibaba.fastjson.JSON;
import com.biz.conf.Global;
import com.biz.model.Hmodel.TorderServerStatistics;
import com.biz.model.Hmodel.basic.Twxgoods;
import com.biz.model.Pmodel.basic.PwxGoodsStockHistory;
import com.biz.model.Pmodel.task.Pbase90Detail;
import com.biz.model.Pmodel.task.PbaseServer;
import com.biz.model.Pmodel.task.PmebActivity;
import com.biz.model.Pmodel.task.PorderServerStatistics;
import com.biz.service.base.BaseServiceImpl;
import com.biz.service.base.OrderServiceI;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.ConfigUtil;
import com.framework.utils.DateUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.URLConectionUtil;
import com.jswzc.jiuling.service.order.DlServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

//import com.biz.model.Pmodel.basic.PwxgoodsStock;

/**
 * Created by liujiajia on 2017/1/6.
 */
@Service("dlService")
public class DlServiceImpl extends BaseServiceImpl<Twxgoods> implements DlServiceI {

    @Resource(name = "daoSupport")
    private DaoSupport dao;
    @Resource(name = "orderService")
    private OrderServiceI orderService;
    @Autowired
    private BaseDaoI<TorderServerStatistics> orderServerStatisticsDao;


    @Override
    public String closeOrder() throws Exception {
     String ss= "ok";
         System.out.print("进入自动关闭订单DL"+DateUtil.dateToString(new Date()));
        //获取超时订单
        List<Map<String, Object>> list=(List<Map<String,Object>>) dao.findForList("TaskDao.findOrderList", null);
        for(int i=0;i<list.size();i++){
            if(list.get(i).get("orderId")!=null){
                orderService.closeOrder(list.get(i).get("orderId")+"","");
                //docancel(list.get(i).get("detailId").toString());
            }else{
                continue;
            }
        }
       System.out.print("结束关闭订单DL"+DateUtil.dateToString(new Date()));
        return ss;
    }



    /**
     * 取消订单
     * @param detailId
     * @throws Exception
     */
    private void docancel(String detailId) throws Exception {
        //调erp接口加库存
        Map<String, String> paramStockUp = new HashMap<>();
        paramStockUp.put("detailId", detailId);
        paramStockUp.put("cityId", Global.getLocalCity());//cityId
        paramStockUp.put("isTicket","0");
        //90商品
        List<PwxGoodsStockHistory> list = (List<PwxGoodsStockHistory>) dao.findForList("TaskDao.getHistoryListUpStock", paramStockUp);
        if(list != null  && list.size() >0){
            String requestUrl = ConfigUtil.get("ERP_URL")+"/interface/changeStockForWhareHouse.action";
            PwxGoodsStockHistory pwxGoodsStockHistory=new PwxGoodsStockHistory();
            pwxGoodsStockHistory.setParmlist(list);
            String jsontext= JSON.toJSONString(pwxGoodsStockHistory);

            Map<String, String> Param = new HashMap();
            Map<String,Object> resMap=new HashMap<>();
            Param.put("parm",jsontext);
            String x= URLConectionUtil.httpURLConnectionPostDiy(requestUrl,Param);
            x = URLDecoder.decode(x, "utf-8");
            if(null==x||x.trim().equals("")){
                System.out.print("加库存失败");
            }else {
                resMap = JSON.parseObject(x, Map.class);
                if(null!=resMap.get("sign")&&resMap.get("sign").equals("true")){
                    // 取消订单（设置order_detail表 isdel = 1）
                    Integer d = (Integer) dao.update("TaskDao.doCancelOrder", detailId);
                    //取消订单设置商品表(减销量)
                    dao.update("TaskDao.updateGoodsSales", detailId);
                    //修改库存
                    dao.update("TaskDao.updateGoodsStock",detailId);
                }else{
					 System.out.print("加库存失败");
                }
            }
        }else{
            Map<String, String> paramStockUp2 = new HashMap<>();
            paramStockUp2.put("detailId", detailId);
            paramStockUp2.put("isTicket","1");
            //联盟商品
            List<PwxGoodsStockHistory> list2 = (List<PwxGoodsStockHistory>) dao.findForList("TaskDao.getHistoryListUpStock", paramStockUp2); //(List<PwxGoods
            // 取消订单（设置order_detail表 isdel = 1）
            Integer d = (Integer) dao.update("TaskDao.doCancelOrder", detailId);
            //取消订单设置商品表(减销量)
            dao.update("TaskDao.updateGoodsSales", detailId);
            //修改库存
            dao.update("TaskDao.updateGoodsStock",detailId);
        }
    }

    @Override
    public String showApplyRecord() throws Exception {
        String msg = "ok";
        //获取当前生效的活动
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String time = sdf.format(date);
        PmebActivity pmebActivity = (PmebActivity) dao.findForObject("TaskDao.findActivity", time);
        if (pmebActivity != null) {
            //将该期全部设为开奖
            dao.update("TaskDao.updateIsOpen", pmebActivity);
            //将该期抽奖
            dao.update("TaskDao.updateIsGet", pmebActivity);
        } else {
            msg = "该期暂无活动！";
        }
        //将改期活动状态设为一结束
        dao.update("TaskDao.updateNowState", time);
        //将下一期活动状态设为进行中
        dao.update("TaskDao.updateNextState", time);
        return msg;
    }

    @Override
    public String detail90() throws Exception {
        String msg="ok";
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        String time = sdf.format(date);
        //获取过期的90券
        List<Pbase90Detail> list = (List<Pbase90Detail>) dao.findForList("TaskDao.find90Detail", time);

        for (int i = 0; i < list.size(); i++) {
            //更改detail表状态
            dao.update("TaskDao.update90Detail", list.get(i).getId());
            //更新base_user用户久领券
            dao.update("TaskDao.updateBaseUser",list.get(i));
        }
        return msg;
    }

    @Override
    public String showStatistics() throws Exception {
        String msg="ok";
        Calendar c   =   Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        String dateSimple= DateUtil.dateToSimpleString(c.getTime());
        Map<String,String> map=new HashMap<>();
        String json=JSON.toJSONString(map);
        List<PbaseServer> serverList= (List<PbaseServer>) dao.findForList("TaskDao.findServerList",null);
        if(serverList!=null&&serverList.size()>0)
        {
            for(PbaseServer server:serverList)
            {
                String url=server.getUrl()+"/api/statistics/getStatistics.ac";
                Map<String,String> parm=new HashMap<>();
                parm.put("dateString",dateSimple);
                String res=URLConectionUtil.httpURLConnectionPostDiy(url,parm);
                if(!StringUtil.isNullOrEmpty(res)&&!"失败".equals(res))
                {
                    try {
                        PorderServerStatistics resMap=JSON.parseObject(res,PorderServerStatistics.class);
                        if("0".equals(resMap.getFlag()))
                        {
                            String hql= StringUtil.formateString("from TorderServerStatistics where getDate='{0}' and serverId='{1}'",dateSimple,server.getId());
                            TorderServerStatistics order=orderServerStatisticsDao.getByHql(hql);
                            String orderId=null;
                            if(order!=null)
                            {
                                orderId=order.getId();
                            }
                            else
                            {
                                order=new TorderServerStatistics();
                            }


                            order.setGetDate(dateSimple);
                            order.setServerId(server.getId());
                            order.setJson(JSON.toJSONString(resMap.getData()));

                            order.setCreateTime(new Date());
                            if(StringUtil.isNullOrEmpty(orderId))
                            {orderServerStatisticsDao.save(order);}
                            else
                            {
                                orderServerStatisticsDao.update(order);
                            }
                        }


                    }catch (Exception e)
                    {
                        e.getMessage();
                    }
                }
            }
        }
        return msg;
    }

    /**
     * 超过15天自动确认收货
     * @return
     * @throws Exception
     */
    @Override
    public String confirmReceipt() throws Exception {
        String msg="ok";
        //获取需要自动确认收货的订单
        List<Map<String,Object>> list=(List<Map<String,Object>>) dao.findForList("TaskDao.getConfirmOrder",null);
        for(int i=0;i<list.size();i++){
            //更改订单状态
            if(list.get(i).get("id")!=null&&!list.get(i).get("id").equals("")){
                dao.update("TaskDao.updateOrderState",list.get(i).get("id"));
                //发送魔板消息
            }else{
                continue;
            }

        }
        return msg;
    }

    @Override
    public String confirmCustomerService() throws Exception {
        return null;
    }


}
