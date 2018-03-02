package com.biz.controller.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.api.ConfirmAnOrder;
import com.biz.service.api.MyOrderServiceI;
import com.framework.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ldd_person on 2017/2/7.
 */
public class OrderController  extends BaseController{

    @Resource(name = "myOrderService")
    private MyOrderServiceI myOrderService;

    //生成订单
    @RequestMapping(value = "/produceOrder", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> produceOrder(@RequestParam String json) throws Exception{
        Map<String,String> resultmap=new HashMap<String,String>();

        json=json.trim();
        try {
            ConfirmAnOrder list = new ConfirmAnOrder();
            if (!json.equals(null)  && json.length()>0) {
                list = JSON.parseObject(json, ConfirmAnOrder.class);
                resultmap=myOrderService.addproduceOrder(list);
            }else {
                resultmap.put("result", "fail");
                resultmap.put("msg", "没有选中商品");
            }

        } catch (Exception e) {
            resultmap.put("result", "fail");
            resultmap.put("msg", e.toString());
        }
        return  resultmap;
    }

    //修改订单状态
    @RequestMapping(value = "/changeOrderState", method = RequestMethod.POST)
    public @ResponseBody Map<String, String> changeOrderState(String ids,String states,String mainStates) throws Exception{
        Map<String,String> resultmap=new HashMap<String,String>();
        ids=ids.trim();
        states=states.trim();
        mainStates=mainStates.trim();
        try {
            if ((!ids.equals(null)  && ids.length()>0)&&(!states.equals(null)&&states.length()>0)&&(!mainStates.equals(null)&&mainStates.length()>0)) {
                resultmap=myOrderService.changeOrderState(ids,states,mainStates);
            }else {
                resultmap.put("result", "fail");
                resultmap.put("msg", "没有选中商品");
            }

        } catch (Exception e) {
            resultmap.put("result", "fail");
            resultmap.put("msg", e.toString());
        }
        return  resultmap;
    }
}
