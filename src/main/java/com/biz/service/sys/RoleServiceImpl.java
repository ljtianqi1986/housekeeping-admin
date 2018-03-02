package com.biz.service.sys;

import com.biz.model.Hmodel.TMenu;
import com.biz.model.Hmodel.TRole;
import com.biz.model.Hmodel.TRoleMenu;
import com.biz.model.Hmodel.TRoleUser;
import com.biz.model.Pmodel.basic.PagentTree;
import com.biz.model.Pmodel.sys.Prole;
import com.biz.service.base.BaseServiceImpl;
import com.framework.dao.hdao.BaseDaoI;
import com.framework.dao.mdao.DaoSupport;
import com.framework.utils.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by yangfan on 2016/6/23.
 */
@Service("roleService")
public class RoleServiceImpl extends BaseServiceImpl<TRole> implements  RoleServiceI {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Autowired
	private BaseDaoI<TMenu> menuDao;
	@Autowired
	private BaseDaoI<TRole> roleDao;
	@Autowired
	private BaseDaoI<TRoleMenu> roleMenuDao;
	@Autowired
	private BaseDaoI<TRoleUser> roleUserDao;



	
	@Override
	public List<PagentTree> showAllRole() throws Exception {
		List<PagentTree> roleList=null;
		roleList=(List<PagentTree>)dao.findForList("roleDao.showAllRole", null);
		return roleList;
	}

	@Override
	public List<PagentTree> getMenuByRole(Prole role) throws Exception {
		List<PagentTree> menuList=null;
		menuList=(List<PagentTree>)dao.findForList("roleDao.getMenuByRole", role);
		//封装jstree对象
		if(menuList.size()>0){
			for(int i=0;i<menuList.size();i++){
				if(menuList.get(i).getIsselected().equals("1")){
					menuList.get(i).getState().setSelected(true);
				}
				menuList.get(i).getState().setOpened(true);
			}
		}
		return menuList;
	}


    @Override
    public List<Prole> getRoleList() throws Exception {
        return (List<Prole>) dao.findForList("roleDao.getRoleList", null);
    }

    @Override
    public List<Prole> getRoleListByUserId(String UserId) throws Exception {
        return (List<Prole>) dao.findForList("roleDao.getRoleListByUserId", UserId);
    }

	@Override
	public void saveRole(Prole role) throws Exception {
		TRole tr=new TRole();
		BeanUtils.copyProperties(role,tr);
		roleDao.save(tr);
		//增加新角色时同时赋予其所有的菜单权限和按钮权限
		List<TMenu> menuList = menuDao.find("from TMenu");
		if(menuList!=null){
			if(menuList.size()>0){
				for(int i=0;i<menuList.size();i++){
					TRoleMenu trm = new TRoleMenu();
					String np=getMenuNameSpace(menuList.get(i));
					trm.setRole_id(tr.getId());
					trm.setMenu_id(menuList.get(i).getId());
					trm.setPermission("add,del,update,query");
					trm.setPermission_nameSpace(np+":add,"+np+":del,"+np+":update,"+np+":query");
					roleMenuDao.save(trm);
				}
			}
		}


	}
    public void updateUserRole(String roles,String userId) throws Exception{
        dao.delete("roleDao.deleteUserRoles",userId);

        if(roles!=null && !roles.trim().equals(""))
        {
            Map userRole ;
            String [] rolesArray = roles.split(",");
            for(int i=0;i<rolesArray.length;i++)
            {
                userRole = new HashMap();
                userRole.put("code", UUID.randomUUID().toString());
                userRole.put("userId",userId);
                userRole.put("roleId",rolesArray[i]);
                dao.save("roleDao.saveUserRole",userRole);
            }
        }
    }

	@Override
	public void updateRoleMenu(Prole role) throws Exception {
		//后保存新的菜单列表中额外新增的数据
		List<TRoleMenu> oldRoleMenuList=roleMenuDao.find("from TRoleMenu t where t.role_id='"+role.getId()+"'");
		String[] newMenu=role.getMenu_id().split(",");
		for(int i=0;i<newMenu.length;i++){
			//判断每一个新的菜单ID是否已经在老的表中已存在，如果不存在则新增
			boolean b=true;
			for(int j=0;j<oldRoleMenuList.size();j++){
				if(oldRoleMenuList.get(j).getMenu_id().equals(newMenu[i])){
					b=false;
					break;
				}
			}
			if(b){
				if(!newMenu[i].equals("")){
					TRoleMenu newRoleMenuObj=new TRoleMenu();
					newRoleMenuObj.setRole_id(role.getId());
					newRoleMenuObj.setMenu_id(newMenu[i]);
					//初始保存全部权限，以及全部包含命名空间的权限
					TMenu menu = menuDao.getByHql("from TMenu t where t.id='"+newMenu[i]+"'");
					String np=getMenuNameSpace(menu);
					newRoleMenuObj.setPermission("add,del,update,query");
					newRoleMenuObj.setPermission_nameSpace(np+":add,"+np+":del,"+np+":update,"+np+":query");
					roleMenuDao.save(newRoleMenuObj);
				}
			}
		}

	}


    @Override
    public void fullRoleMenu(Prole role) throws Exception {
        //后保存新的菜单列表中额外新增的数据
        List<TRoleMenu> oldRoleMenuList=roleMenuDao.find("from TRoleMenu t where t.role_id='"+role.getId()+"'");
        String[] newMenu=role.getMenu_id().split(",");
        //判断这个新菜单的父菜单是否在老的表中已存在，如果不存在，则新增
        List<TMenu> pMenulist=menuDao.find("SELECT DISTINCT t.pid FROM TMenu t WHERE t.id IN ("+role.getMenu_id_sql()+") and t.pid!='0'");
        for(int i=0;i<pMenulist.size();i++){
            //判断每一个新的菜单ID是否已经在老的表中已存在，如果不存在则新增
            boolean p=true;
            for(int j=0;j<oldRoleMenuList.size();j++){
                if(oldRoleMenuList.get(j).getMenu_id().equals(pMenulist.get(i))){
                    p=false;
                    break;
                }
            }
            if(p){
                if(!(pMenulist.get(i)+"").equals("")){
                    TRoleMenu newRoleMenuObj=new TRoleMenu();
                    newRoleMenuObj.setRole_id(role.getId());
                    newRoleMenuObj.setMenu_id(pMenulist.get(i)+"");
                    //初始保存全部权限，以及全部包含命名空间的权限
                    TMenu menu = menuDao.getByHql("from TMenu t where t.id='"+pMenulist.get(i)+"'");
                    String np=getMenuNameSpace(menu);
                    newRoleMenuObj.setPermission("add,del,update,query");
                    newRoleMenuObj.setPermission_nameSpace(np+":add,"+np+":del,"+np+":update,"+np+":query");
                    roleMenuDao.save(newRoleMenuObj);
                }
            }
        }
    }



	public void deleteNotInRoleMenu(Prole role) throws Exception {
		String menuin=StringUtil.formatSqlIn(role.getMenu_id());
		role.setMenu_id_sql(menuin);
		String hql="";
		if(StringUtil.isNullOrEmpty(role.getMenu_id_sql())){
			hql="FROM TRoleMenu t WHERE t.role_id = '"+role.getId()+"')";
		}else{
			hql="FROM TRoleMenu t WHERE t.role_id = '"+role.getId()+"' AND t.menu_id NOT IN ("+role.getMenu_id_sql()+")";
		}
		List<TRoleMenu> rolelist = roleMenuDao.find(hql);
		for(int i=0;i<rolelist.size();i++){
			roleMenuDao.delete(rolelist.get(i));
		}
	}


	@Override
	public List<Prole> showRoleButtonGrid(Prole role) throws Exception {
		List<Prole> roleMenuButtonList = (List<Prole>)dao.findForList("roleDao.showRoleButtonGrid", role);
		return roleMenuButtonList;
	}


	@Override
	public void updateRoleButton(Prole role) throws Exception {
		//从list中去除数据，对每条数据的 增删改查 权限进行格式化，并且增加菜单的命名空间，最后重新保存
		List<Prole> newButtonList = role.getRoleButtonList();
		if(newButtonList!=null&&newButtonList.size()>0){
			for(int i=0;i<newButtonList.size();i++){
				TRoleMenu rolemenu = roleMenuDao.getByHql("From TRoleMenu t where t.id='"+newButtonList.get(i).getId()+"'");
				if(rolemenu!=null){
					//按钮权限重新生成
					String new_permission="";
					String new_permission_nameSpace="";
					//获取当前菜单的命名空间，如果没有，则取href的第一段
					String nameSpace="";
					if(!StringUtil.isNullOrEmpty(newButtonList.get(i).getNameSpace())){
						//直接获取命名空间
						nameSpace=newButtonList.get(i).getNameSpace();
					}else if(!StringUtil.isNullOrEmpty(newButtonList.get(i).getHref())){
						//从href截取命名空间
						String[] hrefHead = newButtonList.get(i).getHref().split("/");
						nameSpace=hrefHead[0];
					}else{
						//都为空则赋予#作为命名空间
						nameSpace="#";
					}

					//判断 "增" "删" "改" "查"权限
					if(!StringUtil.isNullOrEmpty(newButtonList.get(i).getAdd())){
						if(newButtonList.get(i).getAdd().equals("on")){
							new_permission+="add,";
							//组装带命名空间的按钮权限
							new_permission_nameSpace+=nameSpace+":add,";

						}
					}
					if(!StringUtil.isNullOrEmpty(newButtonList.get(i).getDel())){
						if(newButtonList.get(i).getDel().equals("on")){
							new_permission+="del,";
							//组装带命名空间的按钮权限
							new_permission_nameSpace+=nameSpace+":del,";
						}
					}
					if(!StringUtil.isNullOrEmpty(newButtonList.get(i).getUpdate())){
						if(newButtonList.get(i).getUpdate().equals("on")){
							new_permission+="update,";
							//组装带命名空间的按钮权限
							new_permission_nameSpace+=nameSpace+":update,";
						}
					}
					if(!StringUtil.isNullOrEmpty(newButtonList.get(i).getQuery())){
						if(newButtonList.get(i).getQuery().equals("on")){
							new_permission+="query,";
							//组装带命名空间的按钮权限
							new_permission_nameSpace+=nameSpace+":query,";
						}
					}
					//去除两个权限的结尾逗号
					if(!StringUtil.isNullOrEmpty(new_permission)){
						new_permission=new_permission.substring(0,new_permission.length()-1);
					}
					if(!StringUtil.isNullOrEmpty(new_permission_nameSpace)){
						new_permission_nameSpace=new_permission_nameSpace.substring(0,new_permission_nameSpace.length()-1);
					}

					rolemenu.setPermission(new_permission);
					rolemenu.setPermission_nameSpace(new_permission_nameSpace);
					roleMenuDao.update(rolemenu);
				}

			}
		}



	}




	public String getMenuNameSpace(TMenu menu) throws Exception {
		//获取当前菜单的命名空间，如果没有，则取href的第一段
		String nameSpace="";
		if(!StringUtil.isNullOrEmpty(menu.getNameSpace())){
			//直接获取命名空间
			nameSpace=menu.getNameSpace();
		}else if(!StringUtil.isNullOrEmpty(menu.getHref())){
			//从href截取命名空间
			String[] hrefHead = menu.getHref().split("/");
			nameSpace=hrefHead[0];
		}else{
			//都为空则赋予#作为命名空间
			nameSpace="#";
		}
		return nameSpace;
	}








}
