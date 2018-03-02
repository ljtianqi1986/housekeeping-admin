package com.biz.controller.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Pmodel.Pmebactivity;
import com.biz.service.basic.MebactivityServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.SqlFactory;
import com.framework.utils.StringUtil;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 商户设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/mebactivity")
public class MebactivityController extends BaseController{

    @Autowired
    private MebactivityServiceI mebactivityService;


    @InitBinder("pmebactivity")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pmebactivity.");
    }
    /**
     * 跳转商户设置页面
     * @param mv
     * @return
     */
    @RequestMapping("toMebactivity")
    public ModelAndView toMenu(ModelAndView mv){
        mv.clear();
        mv.setViewName("basic/mebactivity/mebactivity");
        return mv;
    }
    @RequestMapping("showMebactivity")
    public void showAgent(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
      //  System.out.println(request.getParameter("customer"));
        sqlParams.setSearchtext(request.getParameter("customer")==null?"":request.getParameter("customer").toString());
        Paging paging=mebactivityService.findMebactivityGrid(sqlParams);
        System.out.println(JSON.toJSONString(paging));
        writeJson(paging,response);
    }
    @RequestMapping("delGridById")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
        Map<String,Object> map=new HashMap<>();
        try{
            mebactivityService.delGridById(ids);
            map.put("success",true);
        }
        catch (Exception e)
        {
            map.put("success",false);
        }
        writeJson(map,response);
    }

    @RequestMapping("toAdd")
    public ModelAndView toAdd() throws Exception {
        mv.clear();

        String time=mebactivityService.findLastDate();
        Date newdate=new Date();
        Date newdate2=new Date();
        Date newdate3=new Date();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        Calendar startDate=Calendar.getInstance();
        if(time!=null&&!time.equals("")) {
            Date timeDate = sdf.parse(time);
            Date currentTime = new Date();
            String timeDate2 = sdf.format(currentTime);
            Date currentTime2 = sdf.parse(timeDate2);
            //已经有活动
            if (time != null && !time.equals("") && (timeDate.getTime() >= currentTime2.getTime())) {
                Date date = sdf.parse(time);
                Calendar now = Calendar.getInstance();
                now.setTime(date);
                startDate.setTime(date);
                startDate.add(Calendar.DATE, 1);
                //获取年
                int year = now.get(Calendar.YEAR);
                //获取日
                int day = now.get(Calendar.DAY_OF_MONTH);
                //获取月
                int month = (now.get(Calendar.MONTH) + 1);
                if (year % 4 == 0) {
                    //如果是闰年
                    if (day == 9) {
                        now.add(Calendar.DATE, 10);
                    } else if (day == 19) {
                        now.add(Calendar.DATE, 10);
                    } else if (day == 29) {
                        now.add(Calendar.MONTH, 1);
                        now.add(Calendar.DATE, -20);
                    }
                } else {
                    //如果是平年
                    if (month == 2) {
                        if (day == 9) {
                            now.add(Calendar.DATE, 10);
                        } else if (day == 19) {
                            now.add(Calendar.MONTH, 1);
                            now.add(Calendar.DATE, -10);
                        }
                    } else {
                        if (day == 9) {
                            now.add(Calendar.DATE, 10);
                        } else if (day == 19) {
                            now.add(Calendar.DATE, 10);
                        } else if (day == 29) {
                            now.add(Calendar.MONTH, 1);
                            now.add(Calendar.DATE, -20);
                        }
                    }
                }
                newdate = now.getTime();
                newdate2 = startDate.getTime();
                now.add(Calendar.DATE, -1);
                newdate3 = now.getTime();
            }else{
                //第一次生成活动
                Calendar now =Calendar.getInstance();
                newdate2=startDate.getTime();
                //获取年
                int year=now.get(Calendar.YEAR);
                //获取日
                int day=now.get(Calendar.DAY_OF_MONTH);
                //获取月
                int month=(now.get(Calendar.MONTH) + 1);
                if(year%4 != 0&&month==2){
                    if(day<9){
                        now.add(Calendar.DATE,(9-day));
                    }else if(9<=day&&day<19){
                        now.add(Calendar.DATE,(19-day));
                    }else if(day>=19){
                        now.add(Calendar.MONTH,1);
                        now.add(Calendar.DATE,(9-day));
                    }
                }else{
                    if(day<9){
                        now.add(Calendar.DATE,(9-day));
                    }else if(9<=day&&day<19){
                        now.add(Calendar.DATE,(19-day));
                    }else if(19<=day&&day<29){
                        now.add(Calendar.DATE,(29-day));
                    }else if(day>=29){
                        now.add(Calendar.MONTH,1);
                        now.add(Calendar.DATE,(9-day));
                    }
                }
                newdate=now.getTime();
                now.add(Calendar.DATE,-1);
                newdate3=now.getTime();
            }
        }else{
            //第一次生成活动
            Calendar now =Calendar.getInstance();
            newdate2=startDate.getTime();
            //获取年
            int year=now.get(Calendar.YEAR);
            //获取日
            int day=now.get(Calendar.DAY_OF_MONTH);
            //获取月
            int month=(now.get(Calendar.MONTH) + 1);
            if(year%4 != 0&&month==2){
                if(day<9){
                    now.add(Calendar.DATE,(9-day));
                }else if(9<=day&&day<19){
                    now.add(Calendar.DATE,(19-day));
                }else if(day>=19){
                    now.add(Calendar.MONTH,1);
                    now.add(Calendar.DATE,(9-day));
                }
            }else{
                if(day<9){
                    now.add(Calendar.DATE,(9-day));
                }else if(9<=day&&day<19){
                    now.add(Calendar.DATE,(19-day));
                }else if(19<=day&&day<29){
                    now.add(Calendar.DATE,(29-day));
                }else if(day>=29){
                    now.add(Calendar.MONTH,1);
                    now.add(Calendar.DATE,(9-day));
                }
            }
            newdate=now.getTime();
            now.add(Calendar.DATE,-1);
            newdate3=now.getTime();
        }
        String newTime=sdf.format(newdate);
        String startTime=sdf.format(newdate2);
        String endTime=sdf.format(newdate3);
        mv.addObject("lotteryTime",newTime);
        mv.addObject("startTime",startTime);
        mv.addObject("endTime",endTime);
        //期数
        String period=mebactivityService.findPeriod();
        if(period!=null&&!period.equals("")){
            period=String.valueOf((Integer.valueOf(period)+1));
        }else{
            period="1";
        }
        mv.addObject("period",period);
        mv.setViewName("basic/mebactivity/mebactivity_detail");
        return mv;
    }

    @RequestMapping("toEdit")
    public ModelAndView toEdit(String id) throws Exception {
        mv.clear();
        mv.addObject("id",id);
        mv.setViewName("basic/mebactivity/mebactivity_detail");
        return mv;
    }

    @RequestMapping("loadInfo")
    public void loadInfo(HttpServletResponse response,String id) throws Exception {
        Pmebactivity pmebactivity=mebactivityService.findById(id);
       writeJson(pmebactivity,response);
    }
    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response,Pmebactivity pmebactivity) throws Exception {
     String res="";
        try{
            if(StringUtil.isNullOrEmpty(pmebactivity.getId()))
            {mebactivityService.saveMebactivity(pmebactivity);}
            else
            {mebactivityService.updateMebactivity(pmebactivity);}
            res="success";
        }
        catch (Exception e)
        {res="操作失败！";}

        writeJson(res,response);
    }

    /**
     * 跳转实体卡报名详情
     * @param mv
     * @return
     */
    @RequestMapping("showDetail")
    public ModelAndView showDetail(ModelAndView mv,String id){
        mv.clear();
        mv.addObject("id",id);
        mv.setViewName("basic/mebactivity/mebactivityPerson");
        return mv;
    }

    /**
     * 子表,码库列表翻页查询
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("showMebActivityPerson")
    public void showMebActivityPerson(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);

        Paging paging = mebactivityService.showMebActivityPerson(map);

        writeJson(paging, response);
    }

    /**
     * 子表,码库列表翻页查询
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("changeIsOpen")
    public void changeIsOpen(HttpServletResponse response, HttpServletRequest request,String id,String isOpen) throws Exception {
        Map<String,Object> map=new HashedMap();
        map= mebactivityService.changeIsOpen(id,isOpen);
        writeJson(map, response);
    }

}
