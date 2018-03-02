package com.biz.controller.api;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.basic.PyestodayReport;
import com.biz.service.api.ApiInterfaceServiceI;
import com.biz.service.basic.BaseDetailServiceI;
import com.framework.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lzq on 2017/2/20.
 */
@Controller
@RequestMapping("/api/statistics ")
public class StatisticsController extends BaseController{


    @Resource(name = "apiInterfaceService")
    private ApiInterfaceServiceI apiInterfaceService;
    @Autowired
    private BaseDetailServiceI baseDetailService;
    /**
     * 获取每日统计数据接口
     * @param json 传递的参数，内容包括beginTime,endTime
     */
    @RequestMapping(value="/getStatisticsOld",method = RequestMethod.POST)
    public synchronized void operUserBalance90(@RequestParam(value="json", required=true) String json, HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> res = new HashMap<String, Object>();
        try {
            map= JSON.parseObject(json,Map.class);
            res=apiInterfaceService.getStatistics(map);
        } catch (Exception e) {
            res.put("flag", "1");
            res.put("msg","操作失败,参数错误");

        }
        writeJson(res,response);
    }

    /**
     * 获取每日统计数据接口
     * @param dateString 传递的参数，内容为日期
     */
    @RequestMapping(value="/getStatistics",method = RequestMethod.POST)
    public synchronized void getStatistics(@RequestParam(value="dateString", required=true) String dateString, HttpServletResponse response){
        Map<String,Object> map=new HashMap<String, Object>();
        Map<String,Object> res = new HashMap<String, Object>();
        try {
            PyestodayReport report=baseDetailService.getYesTodayData(dateString);
            res.put("flag", "0");
            res.put("msg","操作成功！");
            res.put("data",report);
        } catch (Exception e) {
            res.put("flag", "1");
            res.put("msg","操作失败,参数错误");

        }
        writeJson(res,response);
    }
}
