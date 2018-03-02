package com.biz.controller.basic;

import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pmessage;
import com.biz.service.basic.MessageServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Pager;
import com.framework.model.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tomchen on 17/1/9.
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    @Autowired
    private MessageServiceI messageService;


    @RequestMapping("toMessage")
    public ModelAndView toLog(ModelAndView mv){
        mv.setViewName("basic/message");
        return mv;
    }

    @RequestMapping("showMessages")
    public void showMessages(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userName", request.getParameter("userName"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<Pmessage> pager = new Pager();

        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        User user = (User) getShiroAttribute("user");
        if (user.getIdentity()==3)//品牌账号登陆
        {
            params.put("brandCode", user.getIdentity_code());
        }
        pager.setParameters(params);
        pager = messageService.queryMessages(pager);

        Paging<Pmessage> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }


    @RequestMapping("updateState")
    public void updateState(HttpServletResponse response, HttpServletRequest request,String ids,String state) throws Exception {
        Map<String,Object> map=new HashMap<>();
        try{
            messageService.updateState(ids,state);
            map.put("success",true);
        }
        catch (Exception e)
        {
            map.put("success",false);
        }
        writeJson(map,response);
    }

    @RequestMapping("showSignDetail")
    public void showSignDetail(HttpServletResponse response, HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("userName", request.getParameter("userName"));
        int limit = Integer.parseInt(request.getParameter("limit"));
        int offset = Integer.parseInt(request.getParameter("offset"));

        Pager<Pmessage> pager = new Pager();
        pager.setParameters(params);
        pager.setStartRecord(offset);
        pager.setPageSize(limit);

        pager = messageService.showSignDetail(pager);

        Paging<Pmessage> paging = new Paging<>();
        paging.setRows(pager.getExhibitDatas());
        paging.setTotal((long) pager.getRecordCount());

        writeJson(paging, response);
    }

    @RequestMapping("toSignDetail")
    public ModelAndView toSignDetail(ModelAndView mv){
        mv.setViewName("basic/sign_detail");
        return mv;
    }
}
