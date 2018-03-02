package com.biz.service.sys;

import com.biz.model.Hmodel.TRole;
import com.biz.model.Pmodel.basic.PagentTree;
import com.biz.model.Pmodel.sys.Prole;
import com.biz.service.base.BaseServiceI;

import java.util.List;

/**
 * Create By YangFan
 */
public interface RoleServiceI extends BaseServiceI<TRole> {


    /**
     * 获取角色列表--角色设置用
     *
     * @return the list
     * @throws Exception the exception
     */
    List<PagentTree> showAllRole() throws Exception;

    /**
     * 根据选中的角色加载对应菜单列表--角色设置用
     *
     * @param role the role
     * @return the menu by role
     * @throws Exception the exception
     */
    List<PagentTree> getMenuByRole(Prole role) throws Exception;

    /**
     * Gets role list.
     *
     * @return the role list
     * @throws Exception the exception
     */
    List<Prole> getRoleList() throws Exception;

    List<Prole> getRoleListByUserId(String userId) throws Exception;
    /**
     * 保存角色主对象
     *
     * @param role the role
     * @throws Exception the exception
     */
    void saveRole(Prole role) throws Exception;


    /**
     * 使用（多增少减的方法）来更新角色和菜单的关联表
     *
     * @param role the role
     * @throws Exception the exception
     */
    void updateRoleMenu(Prole role) throws Exception;

    void updateUserRole(String roles,String userId) throws Exception;

    /**
     * 删掉少了的关联表
     *
     * @param role the role
     * @throws Exception the exception
     */
    void deleteNotInRoleMenu(Prole role) throws Exception;


    /**
     * 加载按钮权限表格数据
     *
     * @param role the role
     * @throws Exception the exception
     */
    List<Prole> showRoleButtonGrid(Prole role) throws Exception;


    /**
     * 保存角色的按钮权限
     *
     * @param role the role
     * @throws Exception the exception
     */
    void updateRoleButton(Prole role) throws Exception;



    /**
     * 补充角色的按钮权限
     *
     * @param role the role
     * @throws Exception the exception
     */
    void fullRoleMenu(Prole role) throws Exception;

}
