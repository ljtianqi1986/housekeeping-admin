package com.biz.controller.sys;

import com.biz.model.Pmodel.basic.Parea;
import com.biz.service.home.HomeServiceI;
import com.framework.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/area")
public class AreaController extends BaseController{

    @Autowired
    private HomeServiceI homeService;
    /**
     * 跳转地域设置界面
     * @param mv
     * @return
     */
    @RequestMapping("area")
    public ModelAndView area(ModelAndView mv)throws Exception{
        //查询省
        List<Parea> areaProList = homeService.getList("1");
        //查询市
        List<Parea> areaCityList = homeService.getList("2");
        //查询区
        List<Parea> areaAreaList = homeService.getList("3");

        String cityJson = com.alibaba.fastjson.JSONObject.toJSONString(areaCityList);
        String areajson = com.alibaba.fastjson.JSONObject.toJSONString(areaAreaList);
        String projson = com.alibaba.fastjson.JSONObject.toJSONString(areaProList);
        mv.addObject("json",null);
        mv.addObject("areaProList",areaProList);
        mv.addObject("areaCityList",areaCityList);
        mv.addObject("areaAreaList",areaAreaList);
        mv.addObject("cityJson",cityJson);
        mv.addObject("areajson",areajson);
        mv.addObject("projson",projson);
        mv.setViewName("sys/area");
        return mv;
    }
    @RequestMapping("getArea")
    public void getArea(HttpServletResponse response)throws Exception{
        //查询省
        List<Parea> areaProList = homeService.getList("1");
        //查询市
        List<Parea> areaCityList = homeService.getList("2");
        //查询区
        List<Parea> areaAreaList = homeService.getList("3");
        Map<String,Object> area=new HashMap<>();
        area.put("pro",areaProList);
        area.put("city",areaCityList);
        area.put("area",areaAreaList);
        writeJson(area,response);
    }

    /**
     * 根据区域id加载区域信息
     * lev："1"->省，"2"->市，"3"->区
     * id：可以指定到具体哪一个省
     * @param response
     * @param lev
     * @param id
     */
    @RequestMapping("showArea")
    public void showArea(HttpServletResponse response,String lev,String id){
        List<Parea> areaProList=null;
        try {
            areaProList = homeService.getList(lev,id);
        } catch (Exception e) {e.printStackTrace();

        }
        writeJson(areaProList,response);
    }


    /**
     * 根据区域id加载区域信息
     * lev："1"->省，"2"->市，"3"->区
     * pid：可以根据区域上级加载下一级区域数据
     * @param response
     * @param lev
     * @param pid
     */
    @RequestMapping("showAreaByPid")
    public void showAreaByPid(HttpServletResponse response,String lev,String pid){
        List<Parea> areaProList=null;
        try {
            areaProList = homeService.getListByPid(lev,pid);
        } catch (Exception e) {e.printStackTrace();

        }
        writeJson(areaProList,response);
    }


}
