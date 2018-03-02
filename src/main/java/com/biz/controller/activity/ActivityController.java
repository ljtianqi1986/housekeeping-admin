package com.biz.controller.activity;

import com.biz.model.Pmodel.Pactivity;
import com.biz.model.Pmodel.PfirstBalance;
import com.biz.model.Pmodel.User;
import com.biz.service.activity.ActivityServiceI;
import com.biz.service.activity.FirstBalanceServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.utils.Tools;
import com.framework.utils.UuidUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 *
 * @author GengLong
 */
@Controller
@RequestMapping(value = "/activity")
public class ActivityController extends BaseController {

    @Resource(name = "activityService")
    private ActivityServiceI activityService;
    @Resource(name = "firstBalanceService")
    private FirstBalanceServiceI firstBalanceService;


    // pactivity
    @InitBinder("pactivity")
    public void initBinderActivity(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pactivity.");
    }

    @InitBinder("pfirstBalance")
    public void initBinderFirst(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pfirstBalance.");
    }

    /**
     * 跳转活动查询
     */
    @RequestMapping(value = "/toQueryActivity")
    public ModelAndView toOfflineCardType(HttpSession session) throws Exception {
        mv.clear();
        mv.setViewName("activity/queryActivity");
        return mv;
    }


    /**
     * 加载获得列表
     */
    @RequestMapping(value = "/showActivityGrid")
    public void showOfflineCardTypeGrid(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Map<String,Object> params=new HashMap<String, Object>();
        params.put("name", request.getParameter("name")==null?"":request.getParameter("name"));
        params.put("type", request.getParameter("type")==null?"":request.getParameter("type"));
        params.put("state", request.getParameter("state")==null?"":request.getParameter("state"));

        //必要的分页参数

        params.put("order",request.getParameter("order"));
        params.put("offset",Integer.parseInt(request.getParameter("offset")));
        params.put("limit",Integer.parseInt(request.getParameter("limit")));
        params.put("sort",request.getParameter("sort"));
        params.put("begin",Integer.parseInt(request.getParameter("offset")));
        params.put("rows",Integer.parseInt(request.getParameter("limit")));

        Paging list = activityService.ActivityListPageM(params);
        writeJson(list, response);
    }


    /**
     * 跳转新增
     */
    @RequestMapping(value = "/toInsertActivity")
    public ModelAndView toInsertActivity(HttpSession session) throws Exception {
        mv.clear();
        mv.setViewName("activity/addActivity");
        return mv;
    }

    /**
     * 保存新增信息
     */
    @RequestMapping(value = "/doInsert", method = RequestMethod.POST)
    public void doInsertType(HttpServletResponse response, Pactivity pactivity, HttpSession session) throws Exception {
        String msg = "";
        User user = (User) getShiroAttribute("userinfo");
        String id= UuidUtil.get32UUID();
        pactivity.setId(id);
        pactivity.setUserid(user.getUser_code());
        if (activityService.addActivity(pactivity)) {
            msg = "success";
        } else {
            msg = "保存失败";
        }

        activityService.addActivityRecord(id);//记录

        writeJson(msg, response);
    }

    /**
     * 开启关闭活动
     *
     * @param response
     * @param id
     * @param state    0:开启活动,1:关闭活动
     * @throws Exception
     */
    @RequestMapping(value = "/doUpdateActivityState")
    public void doUpdateActivityState(HttpServletResponse response, String id, String state) throws Exception {
        String msg = "";
        if (Tools.isEmpty(id.trim())) {
            msg = "选中失败";
        } else {
            Pactivity pactivity = new Pactivity();
            pactivity.setId(id);
            pactivity.setState(Integer.parseInt(state));
            if (activityService.UpdateActivityState(pactivity)) {
                msg = "success";
            } else {
                msg = "保存失败";
            }

            activityService.addActivityRecord(id);//记录
        }
        writeJson(msg, response);
    }

    /**
     * 删除活动
     */
    @RequestMapping(value = "/dodeleteActivity")
    public void dodeleteActivity(HttpServletResponse response, String id) throws Exception {
        String msg = "";
        if (Tools.isEmpty(id.trim())) {
            msg = "选中失败";
        } else {
            Pactivity pactivity = new Pactivity();
            pactivity.setId(id);

            if (activityService.deleteActivity(pactivity)) {
                msg = "success";
            } else {
                msg = "删除失败";
            }

            activityService.addActivityRecord(id);//记录
        }
        writeJson(msg, response);
    }

/********************************************开始:关注送券活动设置*********************************************************/

    /**
     * 跳转关注送券活动设置页
     */
    @RequestMapping(value = "/toAddUpdateFollowGiveTicket")
    public ModelAndView toAddUpdateFollowGiveTicket(String id,String mapid) throws Exception {
        mv.clear();
        mv.addObject("mapid", mapid);
        mv.addObject("activityId", id);
        mv.setViewName("activity/addFirstBalance");
        return mv;
    }

    /**
     * 保存新增信息
     */
    @RequestMapping(value = "/doUpdatefirst", method = RequestMethod.POST)
    public void doUpdatefirst(HttpServletResponse response, PfirstBalance pfirstBalance, HttpSession session) throws Exception {
        String msg = "";
        User user = (User) getShiroAttribute("userinfo");
        pfirstBalance.setUserid(user.getUser_code());
        boolean savefirst=false;
        boolean updateMap=false;
        if(pfirstBalance.getId().equals(""))
        {
            String id=UuidUtil.get32UUID();
            pfirstBalance.setId(id);
            savefirst = firstBalanceService.addFirstBalance(pfirstBalance);
            updateMap = activityService.updetActivityMap(pfirstBalance.getActivityId(),id);//修改主表Map
        }else
        {
            savefirst = firstBalanceService.updateFirstBalance(pfirstBalance);//修改规则表
        }


        if ((pfirstBalance.getId().equals("")&&savefirst==true&&updateMap==true)||(!pfirstBalance.getId().equals("")&&savefirst==true)) {
            msg = "success";
        } else {
            msg = "保存失败";
        }

        writeJson(msg, response);
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/showFirst", method = RequestMethod.POST)
    public void showFirst(HttpServletResponse response, String mapid) throws Exception {
        PfirstBalance firstObject = firstBalanceService.selectFirstBalance(mapid);
        writeJson(firstObject, response);
    }

    /********************************************结束:关注送券活动设置*********************************************************/

}
