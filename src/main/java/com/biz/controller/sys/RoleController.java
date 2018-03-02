package com.biz.controller.sys;

import com.biz.model.Hmodel.TRole;
import com.biz.model.Pmodel.basic.PagentTree;
import com.biz.model.Pmodel.sys.Prole;
import com.biz.service.sys.RoleServiceI;
import com.framework.controller.BaseController;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*************************************************************************
 * 版本：         V1.0  
 *
 * 文件名称 ：HomeController.java 描述说明 ：
 * 
 * 创建信息 : create by 杨帆 on 2017-01-10 下午3:34:13  修订信息 : modify by ( ) on (date) for ( )
 * 
 * 版权信息 : Copyright (c) 2015 扬州北斗软件有限公司
 **************************************************************************/

@Controller
@RequestMapping("/role")
public class RoleController extends BaseController {
	@Autowired
	private RoleServiceI roleService;


	@InitBinder("prole")
	public void initBinderFormBean1(WebDataBinder binder)
	{
		binder.setFieldDefaultPrefix("prole.");
	}




	/**
	 * 跳转角色首页
	 * @param mv
	 * @return
	 */
	@RequestMapping("toRole")
	public ModelAndView toRole(ModelAndView mv) throws Exception{
		mv.setViewName("sys/role/role");
		return mv;
	}


	/**
	 * 获取角色列表--角色设置用
	 */
	@RequestMapping("getAllRoleList")
	public void getAllRoleList(HttpServletResponse response)throws Exception{
		List<PagentTree> roles=roleService.showAllRole();
		writeJson(roles,response);
	}


    /**
     * 根据选中的角色加载对应菜单列表--角色设置用
     */
    @RequestMapping("getMenuByRole")
    public void getMenuByRole(HttpServletResponse response,Prole role)throws Exception{
        List<PagentTree> roles=roleService.getMenuByRole(role);
        writeJson(roles,response);
    }



	@RequestMapping("getRoleList")
	public void getRoleList(HttpServletResponse response)throws Exception{
		List<Prole> roles=roleService.getRoleList();
		writeJson(roles,response);
	}

    @RequestMapping("getRoleListByUser")
    public void getRoleList(HttpServletResponse response,String userId)throws Exception{
        List<Prole> roles=roleService.getRoleListByUserId(userId);
        writeJson(roles,response);
    }

	/**
	 * 保存角色主对象
	 * @param role
	 */
	@RequestMapping("saveRole")
	public void saveRole(HttpServletResponse response,Prole role)throws Exception{
		String msg="success";
		try {
			roleService.saveRole(role);
		} catch (Exception e) {
			msg="系统故障，角色保存失败";
		}
		writeJson(msg,response);
	}


    /**
     * 跳转修改角色主对象
     * @param role
     */
    @RequestMapping("toUpdateRole")
    public ModelAndView toUpdateRole(Prole role)throws Exception{
        TRole trole = roleService.getById(role.getId());
        mv.addObject("role",trole);
        mv.setViewName("sys/role/role_edit");
        return mv;
    }


    /**
     * 修改角色主对象
     * @param role
     */
    @RequestMapping("updateRole")
    public void updateRole(HttpServletResponse response,Prole role)throws Exception{
        String msg="success";
        TRole tr=roleService.getById(role.getId());
        tr.setRoleName(role.getRoleName());
        tr.setRoleGroup(role.getRoleGroup());
        tr.setRoleMark(role.getRoleMark());
        try {
            roleService.update(tr);
        } catch (Exception e) {
            msg="系统故障，角色保存失败";
        }
        writeJson(msg,response);
    }


    /**
     * 修改用户角色
     * @param roles
     * @param userId
     */
    @RequestMapping("updateUserRole")
    public void updateUserRole(HttpServletResponse response,String roles,String userId)throws Exception{
        String msg="修改成功！";
        try {
            roleService.updateUserRole(roles,userId);
        } catch (Exception e) {

            e.printStackTrace();
            msg="系统故障，角色保存失败";
        }
        writeJson(msg,response);
    }

    /**
     * 删除角色主对象
     * @param role
     */
    @RequestMapping("delRole")
    public void delRole(HttpServletResponse response,Prole role)throws Exception{
        String msg="success";
        TRole tr=roleService.getById(role.getId());
        tr.setIsdel(1);
        try {
            roleService.update(tr);
        } catch (Exception e) {
            msg="系统故障，角色删除失败";
        }
        writeJson(msg,response);
    }


    /**
     * 修改角色和菜单关联对象
     * @param role
     */
    @RequestMapping("updateRoleMenu")
    public void updateRoleMenu(HttpServletResponse response,Prole role)throws Exception{
        String msg="success";
        try {
            roleService.deleteNotInRoleMenu(role);
            roleService.updateRoleMenu(role);
            roleService.fullRoleMenu(role);
        } catch (Exception e) {
            msg="系统故障，保存失败";
        }
        writeJson(msg,response);
    }



    /**
     * 跳转角色按钮权限配置页面
     * @param role
     */
    @RequestMapping("toChangeRoleButton")
    public ModelAndView toChangeRoleButton(Prole role)throws Exception{
        TRole tr = roleService.getById(role.getId());
        mv.addObject("role",tr);
        mv.setViewName("sys/role/role_button");
        return mv;
    }


    /**
     * 加载按钮权限表格数据
     * @param role
     */
    @RequestMapping("showRoleButtonGrid")
    public void showRoleButtonGrid(HttpServletResponse response,Prole role)throws Exception{
        List<Prole> roleMenuButtonList = roleService.showRoleButtonGrid(role);
        writeJson(roleMenuButtonList,response);
    }


    /**
     * 保存角色的按钮权限
     * @param role
     */
    @RequestMapping("saveRoleButton")
    public void saveRoleButton(HttpServletResponse response,Prole role)throws Exception{
        String msg="success";
        try {
            roleService.updateRoleButton(role);
        } catch (Exception e) {
            msg="系统故障，保存失败";
        }
        writeJson(msg,response);
    }





}
