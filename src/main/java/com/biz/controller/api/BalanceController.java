package com.biz.controller.api;

import com.alibaba.fastjson.JSON;
import com.biz.service.api.ApiInterfaceServiceI;
import com.framework.controller.BaseController;
import com.framework.utils.StringUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户的余额接口，包括久零券和久零贝
 * Created by 曹凯 on 2017/2/24.
 */
@Controller
@RequestMapping("/api/balance")
public class BalanceController extends BaseController {
    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;



    /**
     * 用户久零贝接口
     */
    @RequestMapping(value="/operUserCoin90",method = RequestMethod.POST)
    public synchronized void operUserCoin90(@RequestParam(value="json", required=true) String json, HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> res = new HashMap<String, Object>();
        try {
            map= JSON.parseObject(json,Map.class);
            apiInterfaceService.checkUserCoin(map);//先进行用户校验，防止该用户不存在久零贝帐户
            res=apiInterfaceService.updatecoin_90(map);//处理用户久零贝

        } catch (Exception e) {
            res.put("flag", "1");
            res.put("msg","操作失败,参数错误");

        }
        writeJson(res,response);
    }

    /**
     * 用户久零贝接口
     */
    @RequestMapping(value="/operUserCoin90_",method = RequestMethod.POST)
    public synchronized void operUserCoin90_(@RequestParam(value="json", required=true) String json, HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> res = new HashMap<String, Object>();
        try {
            map= JSON.parseObject(json,Map.class);
            apiInterfaceService.checkUserCoin(map);//先进行用户校验，防止该用户不存在久零贝帐户
            res=apiInterfaceService.updatecoin_90_(map);//处理用户久零贝

        } catch (Exception e) {
            res.put("flag", "1");
            res.put("msg","操作失败,参数错误");

        }
        writeJson(res,response);
    }


    /**
     * 用户久零券接口
     */
    @RequestMapping(value="/updateUserBalance90")
    public synchronized void operUserBalance90(@RequestParam(value="json", required=true) String json, HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> res = new HashMap<String, Object>();
        try {
            map= JSON.parseObject(json,Map.class);
            res=apiInterfaceService.updatebalance_90(map);//处理用户久零券

        } catch (Exception e) {
            res.put("flag", "1");
            res.put("msg","操作失败,参数错误");

        }
        writeJson(res,response);
    }



    /**
     * 用户新久零券接口
     */
    @RequestMapping(value="/operUserBalance90")
    public synchronized void updateUserBalance90(@RequestParam(value="json", required=true) String json, HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> res = new HashMap<String, Object>();
        try {
            map= JSON.parseObject(json,Map.class);

            res=apiInterfaceService.updateUserBalance_90(map);//处理用户久零券

        } catch (Exception e) {
            res.put("flag", "1");
            res.put("msg","操作失败,参数错误");

        }
        writeJson(res,response);
    }




    /**
     * 初始化用户数据
     */
    @RequestMapping(value="/doInitUserData")
    public synchronized void doInitUserData(HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> res = new HashMap<String, Object>();
        try {

            res=apiInterfaceService.updateInitUserData();//处理用户久零券

        } catch (Exception e) {
            res.put("flag", "1");
            res.put("msg","操作失败,参数错误");

        }
        writeJson(res,response);
    }
}
