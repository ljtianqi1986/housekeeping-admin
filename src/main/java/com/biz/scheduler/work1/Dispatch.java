package com.biz.scheduler.work1;

import com.biz.service.base.OrderServiceI;
import com.biz.service.scheduler.SchedulerServiceI;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * work1下的任务调度
 * Created by liujiajia on 16/8/2.
 */
public class Dispatch {

    @Resource(name = "schedulerService")
    private SchedulerServiceI schedulerService;
    @Resource(name = "orderService")
    private OrderServiceI orderService;

    public void delivery(){
    }

    public void orderWork(){

    }

    public void autoConfirm(){

    }
    /**
     * 每5分钟执行一次，查询超时未支付的订单，关闭并且释放库存
     * **/
    public void closeOrder() throws Exception {
        List<Map<String,Object>> list=orderService.getOutTimeOrder();
        if(list.size()>0)
        {
            orderService.updateCloseOrder(list);
        }
    }
}
