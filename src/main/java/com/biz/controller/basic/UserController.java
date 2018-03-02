package com.biz.controller.basic;

import com.biz.conf.Global;
import com.biz.model.Hmodel.TSysLog;
import com.biz.model.Pmodel.SysUser;
import com.biz.model.Pmodel.User;
import com.biz.model.Pmodel.basic.Pagent;
import com.biz.model.Pmodel.basic.Pbrand;
import com.biz.service.basic.AgentServiceI;
import com.biz.service.basic.LogServiceI;
import com.biz.service.basic.MerchantServiceI;
import com.biz.service.basic.UserServiceI;
import com.biz.service.sys.SysUserServiceI;
import com.framework.controller.BaseController;
import com.framework.model.Operation;
import com.framework.model.Paging;
import com.framework.model.Params;
import com.framework.utils.MD5;
import com.framework.utils.SqlFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
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
@RequestMapping("/user")
public class UserController extends BaseController{

    @Autowired
    private UserServiceI userService;

	@Autowired
	private LogServiceI logService;
    @Autowired
    private SysUserServiceI sysUserService;
    @Autowired
    private AgentServiceI agentService;
    @Autowired
    private MerchantServiceI merchantService;
    // 定义address对象
    @InitBinder("user")
    public void initBinderAddress(WebDataBinder binder) {
        binder.setFieldDefaultPrefix("user.");
    }

	/**
	 * 用户登录系统
	 * @param mv
	 * @return
	 */
	@RequestMapping("toHome")
	public ModelAndView toHome(ModelAndView mv,User user)throws Exception{
        try {
            //使用权限工具进行用户登录，登录成功后跳到shiro配置的successUrl中，与下面的return没什么关系！
            SecurityUtils.getSubject().login(new UsernamePasswordToken(user.getUsername(), user.getPassword()));

            setShiroAttribute("version", userService.getCurrentVersion());
            user=userService.findUser(user);
            setShiroAttribute("user", user);
//            String identity_code=userService.findIdentity_code(user);
            setShiroAttribute("identity_code",user.getIdentity_code());
            setShiroAttribute("identity",user.getIdentity());
			setShiroAttribute("user", user);
			User userinfo=userService.findByName(user.getLogin_name());
			setShiroAttribute("userinfo", userinfo);
			//登陆后已经在shiro中有角色，现在在session中保留一份
			String roles = "";
			String rolesids = "";
			List<Map> roleList=userService.findNameSpaceRolesByUser(userinfo);
			if (roleList!=null&&roleList.size()>0) {
				for (Map map : roleList) {
					if (map != null) {
						roles+=map.get("roleName").toString()+",";
						rolesids+=map.get("id").toString()+",";
					}
				}
				roles=roles.substring(0,roles.length()-1);
				rolesids=rolesids.substring(0,rolesids.length()-1);
			}
			setShiroAttribute("roles",roles);
			setShiroAttribute("rolesids",rolesids);

            Session session=SecurityUtils.getSubject().getSession();
            System.out.println(session.getTimeout()+session.getHost());

			//put shopId in session
			//String appid = ConfigUtil.get("appid");
			String appid = Global.getAppId();
			Map<String,Object> resultMap =  userService.getShopIdByAppId(appid);
			if (resultMap != null && resultMap.get("shopid") != null) {
				setShiroAttribute("shopId", (String) resultMap.get("shopid"));
			}

			TSysLog tSysLog = new TSysLog();
			tSysLog.setUserName(user.getUsername());
			tSysLog.setType(Operation.LOGIN);
			tSysLog.setDetail("用户登录");
			tSysLog.setCreateTime(new Date());
			tSysLog.setIp(getRequest().getRemoteAddr());
			logService.save(tSysLog);
            mv.setViewName("main/index");
        }catch (AuthenticationException e){
            e.printStackTrace();
            mv.setViewName("../login/login_v2");
        }catch (Exception e2){
			//logger.error(e2);
			e2.printStackTrace();
			mv.setViewName("../login/login_v2");
		}
		return mv;
	}
	
	/**
	 * 用户退出系统
	 * @return
	 * 清除session退出系统
	 */
	@RequestMapping("toLogin")
	public String toLogin(HttpServletResponse response){
		return "redirect:/login/login_v2.jsp";
	}


	@RequestMapping("showAgentUser")
	public void showAgentUser(HttpServletResponse response, HttpServletRequest request,String id) throws Exception {
		//必要的分页参数
		SqlFactory factory=new SqlFactory();
		Params sqlParams=factory.getParams();
		sqlParams.setOrder(request.getParameter("order"));
		sqlParams.setPage(Integer.parseInt(request.getParameter("offset"))*Integer.parseInt(request.getParameter("limit")));

		sqlParams.setRows(Integer.parseInt(request.getParameter("limit")));
		sqlParams.setSort(request.getParameter("sort"));
		//  System.out.println(request.getParameter("customer"));
		sqlParams.setSearchtext(id);
		Paging paging=userService.findAgentUserGrid(sqlParams);
		//System.out.println(JSON.toJSONString(paging));
		writeJson(paging,response);
	}

    @RequestMapping(value = "/modifyPwd")
    public void modifyPwd(HttpServletResponse response,String oldPwd,String newPwd2) throws Exception
    {
        Map<String, Object> map = new HashMap<String, Object>();
        User sessionuser = (User)getShiroAttribute("user");
        User user=userService.findByName(sessionuser.getLogin_name());
        String oldPwdMD5= MD5.md5(oldPwd.trim());
        if(user.getPassword().equals(oldPwdMD5)){
            boolean state=userService.changePwd(MD5.md5(newPwd2),user.getId());
            if(state){
                map.put("isok", true);
                map.put("msg", "修改成功！");
            }else{
                map.put("isok", false);
                map.put("msg", "修改失败！");
            }
        }else{
            map.put("isok", false);
            map.put("msg", "原密码输入不正确！");
        }

        writeJson(map, response);
    }
    @RequestMapping("toModifyUser")
    public ModelAndView toModifyUser(ModelAndView mv) throws Exception{
        String identity_code=(String)getShiroAttribute("identity_code");
        User user = (User)getShiroAttribute("user");
        int identity =user.getIdentity();
        SysUser user2 = sysUserService.getUserByCode(user.getId());
        mv.addObject("sysUser",user2);
        if(identity==1){    //管理员
            mv.setViewName("main/admin_edit");
        }else if(identity==2){   //代理商
            Pagent ag=agentService.findById(user.getIdentity_code());
            mv.addObject("agent",ag);
            mv.setViewName("main/agent_edit");
        }else if(identity==3){   //商户
            Pbrand pbrand=merchantService.findById(user.getIdentity_code());
            mv.addObject("brand",pbrand);
            mv.addObject("cityId",pbrand.getCity());//回写的市id
            mv.setViewName("main/brand_edit");
        }
        mv.addObject("identity_code",identity_code);
        mv.addObject("type","1");

        return mv;
    }
}
