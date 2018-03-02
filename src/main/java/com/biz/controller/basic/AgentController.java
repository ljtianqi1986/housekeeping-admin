package com.biz.controller.basic;

import com.alibaba.fastjson.JSON;
import com.biz.model.Hmodel.basic.Tagent;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.PagentTree;
import com.biz.service.basic.AgentServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.SqlFactory;
import com.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商户设置相关
 * Created by liujiajia on 2016/11/14.
 */
@Controller
@RequestMapping("/agent")
public class AgentController extends BaseController{

    @Autowired
    private AgentServiceI agentService;


    @InitBinder("pagent")
    public void initBinderFormBean1(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("pagent.");
    }

    /**
     * 跳转商户设置页面
     * @param mv
     * @return
     */
    @RequestMapping("toAgent")
    public ModelAndView toMenu(ModelAndView mv){
        mv.setViewName("basic/agent/agent");
        return mv;
    }
    @RequestMapping("getAgentById")
    public void getAgentById(HttpServletResponse response, HttpServletRequest request,String id) throws Exception {
        Tagent ag=agentService.getById(id);
        writeJson(ag,response);
    }
    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,Pagent pagent) throws Exception {
    String msg="";
        try {
            String isOk=agentService.checkInfo(pagent);
            if("0".equals(isOk)) {
                if (StringUtil.isNullOrEmpty(pagent.getAgent_code())) {
                    agentService.saveAgent(pagent);
                } else {
                    agentService.updateAgent(pagent);
                }
                msg = "success";
            }
            else if("1".equals(isOk))
            {msg = "标识已存在";}
            else if("2".equals(isOk))
            {msg = "登录名已存在";}
        }
        catch (Exception e)
        {
            msg="异常失败";
        }
        writeJson(msg,response);
    }
    @RequestMapping("delGridById")
    public void delGridById(HttpServletResponse response, HttpServletRequest request,String ids) throws Exception {
      Map<String,Object> map=new HashMap<>();
        try{
            agentService.delGridById(ids);
            map.put("success",true);
        }
        catch (Exception e)
        {
            map.put("success",false);
        }
        writeJson(map,response);
    }

    @RequestMapping("getAgentList")
    public void getAgentList(HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user=new User();
        List<PagentTree> list=agentService.getAgentListByUser(user);

        writeJson(list,response);
     }
    @RequestMapping("showAgent")
    public void showAgent(HttpServletResponse response, HttpServletRequest request){
        //必要的分页参数
        SqlFactory factory=new SqlFactory();
        Params sqlParams=factory.getParams();
        sqlParams.setOrder(request.getParameter("order"));
        sqlParams.setPage(Integer.parseInt(request.getParameter("offset")));
        sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
        sqlParams.setSort(request.getParameter("sort"));
      //  System.out.println(request.getParameter("customer"));
        sqlParams.setSearchtext(request.getParameter("customer")==null?"":request.getParameter("customer").toString());
        Paging paging=agentService.findAgentGrid(sqlParams);
        System.out.println(JSON.toJSONString(paging));
        writeJson(paging,response);
    }
    @RequestMapping("toEdit")
    public ModelAndView toEdit(String id) throws Exception {
        mv.clear();
        mv.addObject("id",id);
        Pagent ag=agentService.findById(id);
        mv.addObject("agent",ag);
        mv.setViewName("basic/agent/agent_detail");
        return mv;
    }


    @RequestMapping("toAgentUser")
    public ModelAndView toAgentUser(String id) throws Exception {
        mv.clear();
        mv.addObject("id",id);
        Pagent ag=agentService.findById(id);
        mv.addObject("agent",ag);
        mv.setViewName("basic/agent/agent_userList");
        return mv;
    }


    @RequestMapping("getAgentListForSelect")
    public void getAgentListForSelect(HttpServletResponse response, HttpServletRequest request) throws Exception {
        User user= (User) getShiroAttribute("user");
        List<Pagent> list=agentService.getAgentListForSelect(user);
        writeJson(list,response);
    }
}
