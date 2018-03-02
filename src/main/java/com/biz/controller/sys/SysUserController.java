package com.biz.controller.sys;

import com.biz.model.Pmodel.SysUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.model.Singleton.ZkNode;
import com.biz.service.basic.AgentServiceI;
import com.biz.service.basic.MerchantServiceI;
import com.biz.service.sys.SysUserServiceI;
import com.biz.service.weixin.WeiXinServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Paging;
import com.framework.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：UserController.java 描述说明 ：
 * 
 * 创建信息 : create by 刘佳佳 on 2016-6-14 下午3:11:14  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/
@Controller
@RequestMapping("/sysUser")
public class SysUserController extends BaseController {

    @Autowired
    private SysUserServiceI sysUserService;
    @Autowired
    private AgentServiceI agentService;
    @Autowired
    private MerchantServiceI merchantService;
    @Autowired
    private WeiXinServiceI weiXinService;
    // 定义address对象
    @InitBinder("sysUser")
    public void initBinderAddress(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("sysUser.");
    }

    /**
     * 跳转至管理页用户面
     * @param mv
     * @return
     * @throws Exception
     */
    @RequestMapping("toUserManager")
    public ModelAndView toHome(ModelAndView mv) throws Exception{
        mv.setViewName("sys/user/userManager");
        User user = (User)getShiroAttribute("user");
        //user.setIdentity(3);
        mv.addObject("identity",user.getIdentity());
        return mv;
    }

    /**
     * Demo Table Json Data
     * @param response
     */
    @RequestMapping("showSysUserGrid")
    public void showSysUserGrid(HttpServletResponse response,HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("loginName", request.getParameter("login_name")==null?"":request.getParameter("login_name").trim());
        params.put("personName", request.getParameter("person_name")==null?"":request.getParameter("person_name").trim());
        //必要的分页参数

        params.put("order",request.getParameter("order"));
        params.put("offset",Integer.parseInt(request.getParameter("offset")));
        params.put("limit",Integer.parseInt(request.getParameter("limit")));
        params.put("sort",request.getParameter("sort"));
        params.put("begin",Integer.parseInt(request.getParameter("offset")));
        params.put("rows",Integer.parseInt(request.getParameter("limit")));
        //分页
        Paging paging=sysUserService.getSysUserGridPage(params);

        writeJson(paging, response);
    }

    /**
     * Demo Table Json Data
     * @param response
     */
    @RequestMapping("showBaseUserGrid")
    public void showBaseUserGrid(HttpServletResponse response,HttpServletRequest request)throws Exception{
        //前端封装的查询参数
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("q_person_name", request.getParameter("q_person_name")==null?"":request.getParameter("q_person_name"));
        params.put("biz_person_name", request.getParameter("biz_person_name")==null?"":request.getParameter("biz_person_name"));
        params.put("startDate", request.getParameter("startdate"));
        params.put("endDate", request.getParameter("enddate"));
        params.put("user_balanceType", request.getParameter("user_balanceType"));
        //必要的分页参数

        params.put("order",request.getParameter("order"));
        params.put("offset",Integer.parseInt(request.getParameter("offset")));
        params.put("limit",Integer.parseInt(request.getParameter("limit")));
        params.put("sort",request.getParameter("sort"));
        params.put("begin",Integer.parseInt(request.getParameter("offset")));
        params.put("rows",Integer.parseInt(request.getParameter("limit")));
        params.put("appid", ZkNode.getIstance().getJsonConfig().get("appid"));
        //分页
        Paging paging=sysUserService.getBaseUserGridPage(params);

        writeJson(paging, response);
    }

    @RequestMapping(value = "/doLock")
    public void doLock(String user_code, int islock, PrintWriter out)
            throws Exception {
        Map<String,Object> params=new HashMap<String, Object>();
        params.put("id",user_code);
        params.put("islock",islock);
        sysUserService.updateUserLock(params);
        out.write("success");
        out.close();
    }

    @RequestMapping("toEdit")
    public ModelAndView toEdit(String userCode) throws Exception {
        mv.clear();
        mv.addObject("id",userCode.trim());
        SysUser user = sysUserService.getUserByCode(userCode);
        mv.addObject("sysUser",user);
        mv.setViewName("sys/user/user_detail");
        return mv;
    }

    @RequestMapping("doSave")
    public void doSave(HttpServletResponse response, HttpServletRequest request,SysUser user) throws Exception {
        String msg="";
        try {
            if(StringUtil.isNullOrEmpty(user.getId()))
            {User sessionuser = (User)getShiroAttribute("user");
                if(sessionuser.getIdentity()==1){
                    user.setIdentity((short)1);
                    sysUserService.insertUser(user);
                    msg="success";
                }else{
                    msg="添加失败，只有管理员可以添加！";
                }
            }
            else
            { sysUserService.updateUser(user);
            }
            msg="success";
        }
        catch (Exception e)
        {
            msg="异常失败";
        }
        writeJson(msg,response);
    }

    @RequestMapping("updateUser")
    public void updateUser(HttpServletResponse response, HttpServletRequest request,SysUser user,String type) throws Exception {
        String msg="";
        try {
            if(type.trim().equals("1")){   //管理员修改
                sysUserService.updateUser(user);
            }else if(type.trim().equals("2")){   //代理商修改
                user.setPersonName(user.getAgent_name());
                sysUserService.updateUser(user);
                Pagent pagent = new Pagent();
                pagent.setAgent_name(user.getAgent_name());
                pagent.setProvince(user.getProvince());
                pagent.setCity(user.getCity());
                pagent.setAgent_code(user.getIdentityCode());
                agentService.updateAgent(pagent);
            }else if(type.trim().equals("3")){


                Pbrand pbrand = new Pbrand();
                pbrand.setBrandCode(user.getIdentityCode());
                pbrand.setName(user.getBrandName());
                pbrand.setLogoUrl(user.getLogoUrl());
                pbrand.setProvince(user.getProvince());
                pbrand.setCity(user.getCity());
                pbrand.setAddress(user.getAddress());
                pbrand.setIntroduction(user.getIntroduction());
                String isOk=merchantService.checkInfo(pbrand);//去重检查
                if("1".equals(isOk))
                {
                    msg = "商户名称已存在";
                }else{
                    sysUserService.updateUser(user);
                    merchantService.updateMerchant(pbrand);//否则修改
                }

            }

            msg="success";
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
            sysUserService.delGridById(ids);
            map.put("success",true);
        }
        catch (Exception e)
        {
            map.put("success",false);
        }
        writeJson(map,response);
    }

    @RequestMapping("getAllAppid")
    public void getAllAppid(HttpServletResponse response, HttpServletRequest request) throws Exception {
       List<Map<String,Object>> map=new ArrayList<>();
        try{
            map=   sysUserService.getAllAppid();
        }
        catch (Exception e)
        {
         e.getMessage();
        }
        writeJson(map,response);
    }

    @RequestMapping("doSyncByAppid")
    public void doSyncByAppid(HttpServletResponse response, HttpServletRequest request,String appid) throws Exception {
        Map<String,Object> map=new HashMap<>();
        try{
          String token=weiXinService.get_authorizer_access_token(appid);
            SyncUserInfo info=new SyncUserInfo();
            info.setAccessToken(token);
            info.setAppid(appid);
            info.run();
            map.put("flag","0");
        }
        catch (Exception e)
        {
            e.getMessage();
            map.put("flag","1");
        }
        writeJson(map,response);
    }

}
