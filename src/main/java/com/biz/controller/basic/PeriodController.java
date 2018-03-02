package com.biz.controller.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pbizperson;
import com.biz.model.Pmodel.offlineCard.PofflineCard;
import com.biz.model.Pmodel.offlineCard.PofflineCardDetail;
import com.biz.service.basic.BizpersonServiceI;
import com.biz.service.offlineCard.OfflineCardServiceI;
import com.biz.service.period.PeriodServiceI;
import com.framework.controller.BaseController;
import com.framework.model.MessageBox;
import com.framework.model.Paging;
import com.framework.utils.ObjectExcelView;
import com.framework.utils.StringUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 分期展示
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/period")
public class PeriodController extends BaseController {

    @Autowired
    private PeriodServiceI periodService;



    /*************************************页面跳转*************************************/

    //1:管理员 2:区域代理商 (base_agent 的id)3:品牌(商户)(base_brand 的id) 4:实体门店

    /**
     * 实体卡类型
     *
     * @param mv
     * @return
     */
    @RequestMapping("toPeriod")
    public ModelAndView toPeriod(ModelAndView mv) {
        mv.clear();
        mv.setViewName("basic/period/period");
        return mv;
    }

    /**
     * 分期详情
     *
     * @param mv
     * @return
     */
    @RequestMapping("toDetail")
    public ModelAndView toDetail(ModelAndView mv,String id) {
        mv.clear();
        mv.addObject("id",id);
        mv.setViewName("basic/period/periodDetail");
        return mv;
    }
/**
 * *********************************方法************************************
 */
    /**
     * 加载分期信息
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("showPeroid")
    public void showPeroid(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);
        //获取session
        Session session = SecurityUtils.getSubject().getSession();

        User user = (User) session.getAttribute("user");
        int identity=user.getIdentity();
        if(identity!=1){
            map.put("identity_code",user.getIdentity_code());
        }
        Paging paging = periodService.showPeroid(map);

        writeJson(paging, response);
    }

    /**
     * *********************************方法************************************
     */
    /**
     * 加载分期详情
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping("showDetail")
    public void showDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
        //必要的分页参数
        Map map = getParameterByRequest(request);
        //获取session
        Session session = SecurityUtils.getSubject().getSession();
        Paging paging = periodService.showDetail(map);
        writeJson(paging, response);
    }
    @RequestMapping("deByIds")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
        MessageBox mb = getBox();
        //获取session
        Session session = SecurityUtils.getSubject().getSession();

        User user = (User) session.getAttribute("user");
        try{
            periodService.delGridById(ids,user);
            mb.setSuccess(true);
        }catch (Exception e){
            mb.setSuccess(false);
        }
        writeJson(mb,response);
    }

}
