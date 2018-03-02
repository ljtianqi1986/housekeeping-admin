package com.biz.controller.basic;

import com.biz.model.Hmodel.basic.TwxWheelMain;
import com.biz.model.Pmodel.basic.PwxWheel;
import com.biz.service.basic.WheelServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.SqlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 大转盘设置
 * Created by qziwm on 2017/4/6.
 */
@Controller
@RequestMapping("/wheel")
public class WheelController extends BaseController {
    @Autowired
    private WheelServiceI wheelService;



    @InitBinder("pwxWheel")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pwxWheel.");
    }
    @InitBinder("pwxWeelMain")
    public void initBinderFormBean2(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pwxWeelMain.");
    }


    /**
     * 跳转中奖纪录页面
     * @param mv
     * @return
     */
    @RequestMapping("toWheelRecord")
    public ModelAndView toWheelRecord(ModelAndView mv){
        mv.setViewName("basic/wheel/record");
        return mv;
    }
    /**
     * 跳转本期中奖名单
     * @param mv
     * @return
     */
    @RequestMapping("toWheelRecordByLevel")
    public ModelAndView toWheelRecordByLevel(ModelAndView mv,String level){
        mv.clear();
        mv.addObject("level",level);
        mv.setViewName("basic/wheel/recordForLevel");
        return mv;
    }
    /**
     * 跳转大转盘设置页面
     * @param mv
     * @return
     */
    @RequestMapping("toWheel")
    public ModelAndView toWheel(ModelAndView mv){
        mv.setViewName("basic/wheel/wheel");
        return mv;
    }

    /**
     * 跳转设置运费赠送券时间段页面
     * @param mv
     * @return
     */
    @RequestMapping("toFreightSetting")
    public ModelAndView toFreightSetting(ModelAndView mv){
        mv.setViewName("basic/freight/freightSetting");
        Map<String,Object> res=new HashMap<>();
        try {
            res=wheelService.getFreightSetting();
            mv.addObject("freighSetting",res);
        }catch (Exception e)
        {
            e.getMessage();
        }
        return mv;
    }

    @RequestMapping(value = "/updateFreight")
    public void updateFreight(String startTime,String endTime, HttpServletResponse response) throws Exception
    {
        String res="";
        try{
            Map<String,Object> map=new HashMap<>();
            map.put("startTime",startTime);
            map.put("endTime",endTime);
            wheelService.updateFreight(map);
            res="success";

        }
        catch (Exception e)
        {
            res="操作失败！";
        }
        writeJson(res,response);
    }

    /**
     * 分页查询
     */
    @RequestMapping("showWheelRecordForLevel")
    public void showWheelRecordForLevel(HttpServletResponse response, HttpServletRequest request,String level) throws Exception {
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        sqlParams.getParm().put("level",level);
        Paging paging=wheelService.findWheelRecordForLevelGrid(sqlParams);
        writeJson(paging,response);
    }

    /**
     * 分页查询
     */
    @RequestMapping("showWheelRecord")
    public void showWheelRecord(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
        String qishu=request.getParameter("qishu")==null?"":request.getParameter("qishu").toString();
        String userName=request.getParameter("userName")==null?"":request.getParameter("userName").toString();
        sqlParams.getParm().put("qishu",qishu);
        sqlParams.getParm().put("userName",userName);
        Paging paging=wheelService.findWheelRecordGrid(sqlParams);
        writeJson(paging,response);
    }
    /**
     * 跳转商户设置页面
     */
    @RequestMapping("getWheelInfo")
    public void getWheelInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        Map<String,Object> res=new HashMap<>();
        try {
            res=wheelService.getWheelInfo();
        }catch (Exception e)
        {
            e.getMessage();
        }

        writeJson(res,response);
    }

    /**
     * 改变大转盘状态
     */
    @RequestMapping(value = "/doChangeState")
    public void doChangeState(String id,String state, HttpServletResponse response) throws Exception
    {
        String res="";
        try{
            String qishu="";
            if("1".equals(state)){
                SimpleDateFormat qssdf = new SimpleDateFormat("yyyyMMddHHmmss");
                qishu=qssdf.format(new Date());
            }
            wheelService.updateMain(id,state,qishu);
            res="success";
        }
        catch (Exception e)
        {
            res="操作失败！";
        }
writeJson(res,response);

    }


    /**
     * 改变大转盘状态
     */
    @RequestMapping(value = "/doSave")
    public void doSave(PwxWheel pwxWheel, HttpServletResponse response) throws Exception
    {
        String res="";
        try{
            TwxWheelMain main=wheelService.getById(pwxWheel.getMainId());
            if(main!=null)
            {
                if(1==main.getState())
                {
                    res="当前大转盘活动正在进行中，请先停止当前活动";

                }
            }
            wheelService.saveWheel(pwxWheel);
            res="success";

        }
        catch (Exception e)
        {
            res="操作失败！";
        }
        writeJson(res,response);
    }

    /**
     * 改变大转盘状态
     */
    @RequestMapping(value = "/doGetGoods")
    public void doGetGoods(String id, HttpServletResponse response) throws Exception
    {
        String res="";
        try{

            wheelService.updateGetGoods(id);
            res="success";
        }
        catch (Exception e)
        {
            res="操作失败！";
        }
        writeJson(res,response);
    }

}
