package com.biz.controller.api;

import com.alibaba.fastjson.JSON;

import com.biz.model.Pmodel.sys.PwxGoods;
import com.biz.service.api.ApiInterfaceServiceI;
import com.biz.service.mq.PrmI;
import com.framework.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lzq on 2017/2/20.
 */
@Controller
@RequestMapping("/api/QTGoods")
public class ApiGoodsController extends BaseController{


    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;

    @Autowired
    private PrmI prm;
    /**
     * 根据商品编码查询商品SKU信息接口
     */
    @RequestMapping(value="/querySku",method = RequestMethod.POST)
    public void querySku(@RequestParam(value="code", required=true) String code, @RequestParam(value="venderIdMain", required=true) String venderIdMain, @RequestParam(value="type", required=true) String type, HttpServletResponse response){
        try {
            //查询券状态
            Map<String,Object> map = new HashMap<String,Object>();
            if("2".equals(type))
            {
                map = apiInterfaceService.queryGoodsSkuInfoSingle(code,venderIdMain);
            }else{

                map = apiInterfaceService.queryGoodsSkuInfo(code,venderIdMain);
            }
            //销券
//            if(map!=null && map.size()>0 && "1".equals(type))
//            {
//                //满足销券条件准备销券
//                if(map.get("resultCode") !=null && "3".equals(map.get("resultCode").toString()))
//                {
//                    boolean flag = apiInterfaceService.doSellingCoupons(code);
//                    if(flag)
//                    {
//                        map.put("resultCode", "4");//销券成功
//                        map.put("resultMsg", "销券成功");
//                    }else{
//                        map.put("resultCode", "5");//销券成功
//                        map.put("resultMsg", "销券失败");
//                    }
//                }
//            }
            writeJson(map,response);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 根据商品编码查询商品SKU信息接口
     */
    @RequestMapping(value="/addGoodsInfo",method = RequestMethod.POST)
    public void addGoodsInfo(HttpServletResponse response,String json){
        Map<String,Object> res=new HashMap<>();
        List<PwxGoods> goodsList=new ArrayList<>();
        try {
           goodsList = JSON.parseArray(json, PwxGoods.class);
        } catch (Exception e)
            {
                res.put("flag","3");
                res.put("msg","操作失败，json错误!");
            }
        try{
            res=apiInterfaceService.saveGoodsInfo(goodsList);
        }catch (Exception e)
        {
            res.put("flag","1");
            res.put("msg","操作失败，附加属性错误!");
        }
        try{
            if("0".equals(res.get("flag"))) {
                res=  apiInterfaceService.saveGoods(goodsList);
            }
        }catch (Exception e)
        {
            if("NOSTCOK".equals(e.getMessage()))
            {
                res.put("flag","4");
                res.put("msg","操作失败，商品数量不足!");
            }
            else
            {   res.put("flag","2");
                res.put("msg","操作失败，商品属性错误!");
            }


        }

        writeJson(res,response);
    }

    @RequestMapping(value = "/tt")
    public void test22(){
        if (prm!=null){
            prm.sendQueue("","",null);
        }
    }
}
